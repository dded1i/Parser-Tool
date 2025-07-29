package main.rice.concisegen;
import java.util.*;
import main.rice.test.*;

/**
 * ConciseSetGenerator is a class with only one method and single purpose to generate concise (reduced) sets
 * using that method
 */
public class ConciseSetGenerator {
    /**
     * the method finds an approximately minimal set of test cases that "cover" or "hit"
     * all of the known buggy implementations (that failed at least one test in the base test set)
     * using greedy hitting set algorithm
     *
     * @param results object that encapsulates
     *                1)all TestCases (allCases)
     *                2)caseToFiles (indicates which files were "caught" by which test cases)
     *                3)wrongSet (set of all files that failed one or more tests)
     * @return minimal set of test cases that "cover" all  known buggy implementations
     */
    public static Set<TestCase> setCover(TestResults results) {
        // do not mutate input fields

        List<Set<Integer>> copiedCaseToFiles = new ArrayList<>();
        for (Set<Integer> origSet : results.getCaseToFiles()) {
            Set<Integer> copiedSet = new HashSet<>(origSet); // Create a new set and copy the elements
            copiedCaseToFiles.add(copiedSet);
        }
        Set<TestCase> conciseSet = new HashSet<>();
        //copy wrongSet not to mutate it
        Set<Integer> wrongSet = new HashSet<>(results.getWrongSet());


        while (!wrongSet.isEmpty()) {
            Set<Integer> largestSet = new HashSet<>();
            // Find the largest set and its index
            System.out.println(copiedCaseToFiles);
            for (Set<Integer> currentSet : copiedCaseToFiles) {
                if (currentSet.size() > largestSet.size()) {
                    largestSet = currentSet;
                }
            }
            //add currently largest element to concise set
            int largestIdx=copiedCaseToFiles.indexOf(largestSet);
            conciseSet.add(results.getTestCase(largestIdx));
            wrongSet.removeAll(largestSet);//update wrongset

            //remove elements of largest set from copied caseToFiles
            Set<Integer> duplicates=new HashSet<>(largestSet);
            for (Set<Integer> set: copiedCaseToFiles){
                set.removeAll(duplicates);
            }
        }

        return conciseSet;
    }
}
//use wrongset to keep track of covered files
//find test case that covers the most files
//remove the files from wrong set
