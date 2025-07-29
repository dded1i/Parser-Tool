package main.rice.parse;
import main.rice.node.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * this class represents the config file parser. It should parse config files of the format specified in the API page.
 * Parsing this config file should be a two-phase process:
 * 1.use the org.json package to extract the value associated with each key
 * 2.After extracting these values, you will need to write your own custom parsing function(s) to process the individual strings within the "types", "exhaustive domain", and "random domain" arrays.
 */
public class ConfigFileParser {
    /**
     * Reads and returns the contents of the file located at the input filepath
     *
     * @param filepath string representing file that is to be read
     * @return the contents of the file located at the input filepath or throws IOException if file doesn't exist
     * @throws IOException: it gets thrown if  the file does not exist or cannot be read
     */
    public static String readFile(String filepath) throws IOException {
        //since we want to read the entire file at once, no fileReader
        return new String(Files.readAllBytes(Paths.get(filepath)));
    }

    /**
     * the method parses the input string ( contents of a JSON file)
     *
     * @param contents a JSON file  comprised of a single valid JSON object with 5 keys.
     * @return new ConfigFile object of the list of nodes, parsed function name, and number of random tests to generate
     * @throws InvalidConfigException : gets thrown  with a descriptive error message if any part of the config file
     *                                is missing or malformed
     */
    public static ConfigFile parse(String contents) throws InvalidConfigException {
        try {
            JSONObject jsonObj = new JSONObject(contents);
            ConfigFileParser.checkKeys(jsonObj);

            // Parse fname and num random
            String fname = jsonObj.getString("fname");
            Object numRandObj = jsonObj.get("num random");
            if (!(numRandObj instanceof Integer)) {
                throw new NumberFormatException();
            }
            int numRand = jsonObj.getInt("num random");
            if (numRand < 0) {
                throw new InvalidConfigException("Invalid value for 'num random': " +
                        Integer.toString(numRand) + ". num random msut be non-negative");
            }

            // Parse types
            JSONArray typesArray = jsonObj.getJSONArray("types");
            List<APyNode<?>> parsedTypes = new ArrayList<>();
            for (int i = 0; i < typesArray.length(); ++i) {
                parsedTypes.add(parseType(typesArray.getString(i)));
            }

            //Parse exhaustive domains
            JSONArray exhaustiveDomainArray = jsonObj.getJSONArray("exhaustive domain");
            if (parsedTypes.size() != exhaustiveDomainArray.length()) {
                throw new InvalidConfigException("Array sizes mismatch. Array of types has length " +
                        Integer.toString(parsedTypes.size()) + " while array of exhaustive domains - " +
                        Integer.toString(exhaustiveDomainArray.length()));
            }
            for (int i = 0; i < parsedTypes.size(); ++i) {
                ConfigFileParser.parseDomain(parsedTypes.get(i),
                        exhaustiveDomainArray.getString(i), true);
            }

            //Parse random domains
            JSONArray randomDomainArray = jsonObj.getJSONArray("random domain");
            if (parsedTypes.size() != randomDomainArray.length()) {
                throw new InvalidConfigException("Array sizes mismatch. Array of types has length " +
                        Integer.toString(parsedTypes.size()) + " while array of random domains - " +
                        Integer.toString(randomDomainArray.length()));
            }
            for (int i = 0; i < parsedTypes.size(); ++i) {
                ConfigFileParser.parseDomain(parsedTypes.get(i),
                        randomDomainArray.getString(i), false);
            }

            return new ConfigFile(fname, parsedTypes, numRand);

        } catch (JSONException e) {
            throw new InvalidConfigException("JSON parsing exception: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidConfigException("Couldn't parse value for 'num random'.");
        }
    }

    /**
     * the method parses the input string ( contents of a JSON file)
     * @param jsonObj a JSON file  comprised of a single valid JSON object with 5 keys.
     * @throws InvalidConfigException : gets thrown  with a descriptive error message if any part of the config file
     *                                is missing or malformed
     */
    private static void checkKeys(JSONObject jsonObj) throws InvalidConfigException {
        //checks if the keys are valid
        //create a set of valid strings we'll compare with
        Set<String> validKeys = Set.of(
                "fname",
                "types",
                "exhaustive domain",
                "random domain",
                "num random");

        if (!jsonObj.keySet().equals(validKeys)) {
            throw new InvalidConfigException("JSON object does not contain exactly the specified keys.");
        }
    }

    // vvv THE PARSING HELL vvv
    /**
     * Splits an input string into a list of tokens based on delimiter regex.
     * @param parsedString The string we split into tokens.
     * @param delimRegex   The regular expression used as a delimiter. It specifies
     *  where the string should be split.
     * @return a list of String tokens, split according to the delimiter regex and
     *         that includes the delimiters themselves.
     */
    private static List<String> getStringTokens(String parsedString, String delimRegex) {
        return Arrays.asList(parsedString.split("(?=" + delimRegex + ")|(?<=" + delimRegex + ")")); // splits while saving delimeters
    }
    /**
     * The class represents a token that can either hold a string or a node of type T.
     * We will use it to differentiate between simple string tokens and
     * compound node tokens.
     * @param <T> The type of the node this token can hold.
     */
    private static class Token<T> {
        /**
         * the field represents a flag indicating whether this token represents a node.
         */
        private final Boolean isNodeFlag;
        /**
         * The string value of the token.
         */
        private final String str;
        /**
         * The value of the node of the token.
         */
        private final T node;
        /**
         * the method constructs a token representing a string.
         * @param str string value of token
         */
        public Token(String str) {
            this.str = str;
            this.isNodeFlag = false;
            this.node = null;
        }
        /**
         * the method constructs a token representing a node of type T.
         * @param node: node value of the token
         */
        public Token(T node) {
            this.node = node;
            this.isNodeFlag = true;
            this.str = null;
        }
        /**
         * A "checker" for whether the token represents a node
         * @return true if the token is a node, false otherwise
         */
        public Boolean isNode() {
            return isNodeFlag;
        }
        /**
         * The method gets the string value.
         * @return string value of the node or null if it's node
         */
        public String getStr() {
            return str;
        }
        /**
         * The method gets the node value of the token.
         * @return The node value of the token or null if it's a simple string.
         */
        public T getNode() {
            return node;
        }
    }

    //TYPE PARSER
    /**
     * The method parses a given string into an APyNode using stack-based approach.
     * @param type the type of string to be parsed.
     * @return an APyNode representing the parsed type.
     * @throws InvalidConfigException gets thrown if the type fails to satisfy semantics of fails parsing
     */
    private static APyNode<?> parseType(String type) throws InvalidConfigException {
        String delimRegex = "\\(|:";
        List<String> stringTokens = getStringTokens(type, delimRegex);

        ArrayDeque<String> stringTokenDeque = new ArrayDeque<>(stringTokens);
        Stack<Token<APyNode<?>>> tokenStack = new Stack<>();

        // kinda reversed polish notation
        while (!stringTokenDeque.isEmpty()) {
            String lastStringToken = stringTokenDeque.pollLast().trim();
            if (lastStringToken.isBlank()) {
                continue;
            }

            try {
                //DELIMETERS
                if (lastStringToken.matches(delimRegex)) {
                    tokenStack.push(new Token<>(lastStringToken));

                    //INT
                } else if (lastStringToken.matches("int")) {
                    tokenStack.push(new Token<>(new PyIntNode()));

                    //BOOL
                } else if (lastStringToken.matches("bool")) {
                    tokenStack.push(new Token<>(new PyBoolNode()));

                    //FLOAT
                } else if (lastStringToken.matches("float")) {
                    tokenStack.push(new Token<>(new PyFloatNode()));

                    //LIST, TUPLE, SET
                } else if (lastStringToken.matches("list|tuple|set")) {
                    Token<APyNode<?>> lastDelim = tokenStack.pop();
                    if (lastDelim.isNode() || !lastDelim.getStr().matches("\\(")) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. No left parentesis were found after '" + lastStringToken + " '.");
                    }
                    Token<APyNode<?>> leftChild = tokenStack.pop();
                    if (!leftChild.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. Type was expected after '" + lastStringToken + " '(, but '" +
                                leftChild.getStr() + "' was found'.");
                    }

                    if (lastStringToken.matches("list")) {
                        tokenStack.push(new Token<>(new PyListNode<>(leftChild.getNode())));
                    } else if (lastStringToken.matches("tuple")) {
                        tokenStack.push(new Token<>(new PyTupleNode<>(leftChild.getNode())));
                    } else if (lastStringToken.matches("set")) {
                        tokenStack.push(new Token<>(new PySetNode<>(leftChild.getNode())));
                    } else {
                        throw new InvalidConfigException("Internal parser error: word '" + lastStringToken +
                                "' shouldn't appear in this branch of code. Fix the source code.");
                    }

                    //DICT
                } else if (lastStringToken.matches("dict")) {
                    Token<APyNode<?>> lastDelim = tokenStack.pop();
                    if (lastDelim.isNode() || !lastDelim.getStr().matches("\\(")) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. No left parentesis were found after '" + lastStringToken + " '.");
                    }
                    Token<APyNode<?>> leftChild = tokenStack.pop();
                    if (!leftChild.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. Type was expected after '" + lastStringToken + " '(, but '" +
                                leftChild.getStr() + "' was found [left child].");
                    }

                    lastDelim = tokenStack.pop();
                    if (lastDelim.isNode() || !lastDelim.getStr().matches(":")) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. No colon were found after '" + lastStringToken + " '.");
                    }
                    Token<APyNode<?>> rightChild = tokenStack.pop();
                    if (!rightChild.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. Type was expected after '" + lastStringToken + " '(, but '" +
                                leftChild.getStr() + "' was found [right child].");
                    }

                    tokenStack.push(new Token<>(new PyDictNode<>(leftChild.getNode(), rightChild.getNode())));

                    //STR
                } else if (lastStringToken.matches("str")) {
                    Token<APyNode<?>> lastDelim = tokenStack.pop();
                    if (lastDelim.isNode() || !lastDelim.getStr().matches("\\(")) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. No left parentesis were found after '" + lastStringToken + " '.");
                    }
                    Token<APyNode<?>> strVal = tokenStack.pop();
                    if (strVal.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. String was expected but a type was found after '" + lastStringToken + " '(.");
                    } else if (strVal.getStr().matches(delimRegex)) { // I don't wanna allow ( or : as the strVal
                        throw new InvalidConfigException("Exception while parsing '" + type +
                                "'. String was expected but a delimeter was found after '" + lastStringToken + " '(.");
                    }

                    Set<Character> strSet = new HashSet<>();
                    for (char c : strVal.getStr().toCharArray()) {
                        strSet.add(Character.valueOf(c));
                    }
                    tokenStack.push(new Token<>(new PyStringNode(strSet)));

                    //STRVAL
                } else {
                    tokenStack.push(new Token<>(lastStringToken));
                }

            } catch (EmptyStackException e) {
                throw new InvalidConfigException("Exception while parsing '" + type +
                        "'. Invalid syntax after '" + lastStringToken + "'.");
            }
        }

        // PARSING CHECKING
        if (tokenStack.size() != 1) {
            throw new InvalidConfigException("Expected single type definition in '" +
                    type + "'. Check the type definitions for potential misspells.");
        }
        try {
            Token<APyNode<?>> finalToken = tokenStack.peek();
            if (!finalToken.isNode()) {
                throw new InvalidConfigException("Expected single type definition in '" +
                        type + "', but '" + finalToken.getStr() + "' was found.");
            }
            return finalToken.getNode();
        } catch (Exception e) {
            throw new InvalidConfigException(e.getMessage());
        }
    }

    // DOMAIN HELPERS

    /**
     * The method checks if all numbers in the list are non-negative.
     * @param domain the list of Number's to be checked.
     * @return true if all numbers are non-negative, false otherwise.
     */
    private static Boolean checkDomainNonNegative(List<Number> domain) {
        for (Number num : domain) {
            if (num.doubleValue() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * The method checks if all numbersare valid boolean values.
     * @param domain The list of Number's to be checked.
     * @return true if all numbers are 0 or 1, false otherwise.
     */
    private static Boolean checkDomainBoolean(List<Number> domain) {
        for (Number num : domain) {
            if (num.doubleValue() != 0 && num.doubleValue() != 1) {
                return false;
            }
        }
        return true;
    }
    /**
     * The method checks if all numbers in the list are integers.
     *
     * @param domain The list of Number objects to be checked.
     * @return true if all numbers are integers, false otherwise.
     */
    private static Boolean checkDomainInteger(List<Number> domain) {
        for (Number num : domain) {
            if (num.doubleValue() != num.intValue()) {
                return false;
            }
        }
        return true;
    }
    /**
     * the method converts a list of Numbers to a list of Doubles.
     * @param domain the list of Number objects to get converted.
     * @return a list of Doubles converted from the list.
     */
    private static List<Double> convertDomainToDouble(List<Number> domain) {
        List<Double> list = new ArrayList<>();
        for (Number num : domain) {
            list.add(num.doubleValue());
        }
        return list;
    }

    /**
     * The method converts the list of Numbers to a list of Integers.
     *
     * @param domain The list of Number objects to get converted.
     * @return a list of Integers converted from the input list.
     */
    private static List<Integer> convertDomainToInteger(List<Number> domain) {
        List<Integer> list = new ArrayList<>();
        for (Number num : domain) {
            list.add(num.intValue());
        }
        return list;
    }

    //DOMAIN PARSER
    /**
     * The method parses a string that represents a domain, using a stack-based approach.
     * @param tree the APyNode tree
     * @param domain domain string
     * @param isExhaustive flag indicating if exhaustive parsing is required
     * @throws InvalidConfigException gets thrown if the parsing fails
     */
    private static void parseDomain(APyNode<?> tree, String domain, Boolean isExhaustive) throws InvalidConfigException {
        String delimRegex = "\\(|:|\\[|\\]|,|~";
        List<String> stringTokens = getStringTokens(domain, delimRegex);

        ArrayDeque<String> stringTokenDeque = new ArrayDeque<>(stringTokens);
        ArrayDeque<Token<List<Number>>> tokenStack = new ArrayDeque<>();

        while (!stringTokenDeque.isEmpty()) {
            String lastStringToken = stringTokenDeque.pollLast().trim();
            if (lastStringToken.isBlank()) {
                continue;
            }

            try {
                //DELIMETERS
                if (lastStringToken.matches(delimRegex)) {
                    if (lastStringToken.matches("\\[")) {
                        ArrayList<Number> list = new ArrayList<>();
                        Token<List<Number>> lastToken = tokenStack.pop();
                        Boolean commaAwaiting = false;
                        while (!lastToken.isNode() && !lastToken.getStr().matches("\\]")) {
                            if (commaAwaiting) {
                                if (lastToken.getStr().matches(",")) {
                                    commaAwaiting = false;
                                } else {
                                    throw new InvalidConfigException("Exception while parsing '" + domain +
                                            "'. A comma was expected but found '" + lastToken.getStr() + "'.");
                                }
                            } else {
                                try {
                                    Number num = Double.parseDouble(lastToken.getStr());
                                    list.add(num);
                                    commaAwaiting = true;
                                } catch (NumberFormatException e) {
                                    throw new InvalidConfigException("Exception while parsing '" + domain +
                                            "'. Could not parse a supposed number '" + lastToken.getStr() + "'.");
                                } catch (Exception e) {
                                    throw new InvalidConfigException(e.getMessage());
                                }
                            }
                            lastToken = tokenStack.pop();
                        }
                        if (lastToken.isNode()) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. An array lacks closing bracket.");
                        }
                        // fix 1
                        if (list.isEmpty()) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. An array is empty.");
                        }
                        // fix 2
                        list = new ArrayList<>(new LinkedHashSet<>(list));
                        tokenStack.push(new Token<>(list));
                    } else {
                        tokenStack.push(new Token<>(lastStringToken));
                    }
                    //SUPPOSED NUMBER
                } else {
                    Token<List<Number>> lastToken = tokenStack.peek();
                    if (lastToken == null || lastToken.isNode() || !lastToken.getStr().matches("~")) {
                        tokenStack.push(new Token<>(lastStringToken));
                    } else {
                        tokenStack.pop();
                        Token<List<Number>> previousNumber = tokenStack.pop();
                        if (previousNumber.isNode()) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. Expected an integer number after '" + lastStringToken + "~'.");
                        }
                        try {
                            Integer leftInt = Integer.parseInt(lastStringToken);
                            Integer rightInt = Integer.parseInt(previousNumber.getStr());

                            if (leftInt > rightInt) {
                                throw new InvalidConfigException("Exception while parsing '" + domain +
                                        "'. In the pair '" + lastStringToken + "~" + previousNumber.getStr() +
                                        "' the left number is greater than the right number.");
                            }

                            ArrayList<Number> list = new ArrayList<>();
                            for (Integer i = leftInt; i <= rightInt; ++i) {
                                list.add(i);
                            }
                            tokenStack.push(new Token<>(list));
                        } catch (NumberFormatException e) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. Expected a pair of integers separated by a tilde, but found '" + lastStringToken + "~" +
                                    previousNumber.getStr() + "'.");
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                throw new InvalidConfigException("Exception while parsing '" + domain +
                        "'. Invalid syntax after '" + lastStringToken + "'.");
            }
        }

        // now is time for a tree walk
        Stack<APyNode<?>> nodeRecursionStack = new Stack<>();
        Stack<Integer> flagRecursionStack = new Stack<>(); //dict has to be visited twice
        nodeRecursionStack.push(tree);
        flagRecursionStack.push(0);

        try {
            while (!nodeRecursionStack.isEmpty()) {
                APyNode<?> currentNode = nodeRecursionStack.pop();
                Integer visitFlag = flagRecursionStack.pop();

                //BOOL
                if (currentNode instanceof PyBoolNode) {
                    Token<List<Number>> currentToken = tokenStack.pop();
                    if (!currentToken.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Expected a domain for bool, found '" + currentToken.getStr() + "'.");
                    } else if (!checkDomainBoolean(currentToken.getNode())) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Invalid domain for bool.");
                    }
                    List<Integer> boolDomain = convertDomainToInteger(currentToken.getNode());
                    if (isExhaustive) {
                        currentNode.setExDomain(boolDomain);
                    } else {
                        currentNode.setRanDomain(boolDomain);
                    }

                    //INT
                } else if (currentNode instanceof PyIntNode) {
                    Token<List<Number>> currentToken = tokenStack.pop();
                    if (!currentToken.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Expected a domain for int, found '" + currentToken.getStr() + "'.");
                    } else if (!checkDomainInteger(currentToken.getNode())) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Invalid domain for int.");
                    }
                    List<Integer> intDomain = convertDomainToInteger(currentToken.getNode());
                    if (isExhaustive) {
                        currentNode.setExDomain(intDomain);
                    } else {
                        currentNode.setRanDomain(intDomain);
                    }

                    //FLOAT
                } else if (currentNode instanceof PyFloatNode) {
                    Token<List<Number>> currentToken = tokenStack.pop();
                    if (!currentToken.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Expected a domain for float, found '" + currentToken.getStr() + "'.");
                    }
                    List<Double> floatDomain = convertDomainToDouble(currentToken.getNode());
                    if (isExhaustive) {
                        currentNode.setExDomain(floatDomain);
                    } else {
                        currentNode.setRanDomain(floatDomain);
                    }

                    //STR
                } else if (currentNode instanceof PyStringNode) {
                    Token<List<Number>> currentToken = tokenStack.pop();
                    if (!currentToken.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Expected a domain for str, found '" + currentToken.getStr() + "'.");
                    } else if (!checkDomainNonNegative(currentToken.getNode())) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Non-iterable domain for str.");
                    }
                    List<Integer> strDomain = convertDomainToInteger(currentToken.getNode());
                    if (isExhaustive) {
                        currentNode.setExDomain(strDomain);
                    } else {
                        currentNode.setRanDomain(strDomain);
                    }

                    // LIST, SET, TUPLE
                } else if (currentNode instanceof PyListNode ||
                        currentNode instanceof PyTupleNode ||
                        currentNode instanceof PySetNode) {

                    Token<List<Number>> currentToken = tokenStack.pop();
                    if (!currentToken.isNode()) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Expected a domain for list/tuple/set, found '" + currentToken.getStr() + "'.");
                    } else if (!checkDomainNonNegative(currentToken.getNode())) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. Non-iterable domain for list/tuple/set.");
                    }
                    List<Integer> itrDomain = convertDomainToInteger(currentToken.getNode());
                    if (isExhaustive) {
                        currentNode.setExDomain(itrDomain);
                    } else {
                        currentNode.setRanDomain(itrDomain);
                    }

                    Token<List<Number>> lastDelim = tokenStack.pop();
                    if (lastDelim.isNode() || !lastDelim.getStr().matches("\\(")) {
                        throw new InvalidConfigException("Exception while parsing '" + domain +
                                "'. No left parentesis were found for a list/tuple/set domain.");
                    }

                    nodeRecursionStack.push(currentNode.getLeftChild());
                    flagRecursionStack.push(0);

                    //DICT
                } else if (currentNode instanceof PyDictNode) {
                    //first visit - parse iterableDomain, (, left child domain
                    if (visitFlag == 0) {
                        Token<List<Number>> currentToken = tokenStack.pop();
                        if (!currentToken.isNode()) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. Expected a domain for dict, found '" + currentToken.getStr() + "'.");
                        } else if (!checkDomainNonNegative(currentToken.getNode())) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. Non-iterable domain for dict.");
                        }
                        List<Integer> itrDomain = convertDomainToInteger(currentToken.getNode());
                        if (isExhaustive) {
                            currentNode.setExDomain(itrDomain);
                        } else {
                            currentNode.setRanDomain(itrDomain);
                        }

                        Token<List<Number>> lastDelim = tokenStack.pop();
                        if (lastDelim.isNode() || !lastDelim.getStr().matches("\\(")) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. No left parentesis were found for a dict domain.");
                        }

                        nodeRecursionStack.push(currentNode);
                        flagRecursionStack.push(1);

                        nodeRecursionStack.push(currentNode.getLeftChild());
                        flagRecursionStack.push(0);
                        //second visit - parse :, right child domain
                    } else {
                        Token<List<Number>> lastDelim = tokenStack.pop();
                        if (lastDelim.isNode() || !lastDelim.getStr().matches(":")) {
                            throw new InvalidConfigException("Exception while parsing '" + domain +
                                    "'. No colon were found for a dict domain.");
                        }

                        nodeRecursionStack.push(currentNode.getRightChild());
                        flagRecursionStack.push(0);
                    }
                }
            }

        } catch (NoSuchElementException e) {
            throw new InvalidConfigException("Exception while parsing '" + domain +
                    "'. Likely there is not enough domains for the types.");
        } catch (Exception e) {
            throw new InvalidConfigException(e.getMessage());
        }

        // PARSING CHECKING
        if (!tokenStack.isEmpty()) {
            throw new InvalidConfigException("Exception while parsing '" + domain +
                    "'. Either there are unattributed domains or redundant delimiters.");
        }
    }
}