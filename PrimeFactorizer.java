package main.rice;

import javax.print.attribute.standard.PrinterMessageFromOperator;

/**
 * This class implements a relatively simple algorithm for computing the prime
 * factors of a number.  At initialization, a list of primes is computed. Given a
 * number, this list is used to efficiently compute the prime factors of that number.
 */
public class PrimeFactorizer {
    //initialize fields:
    private int[] primesArray;
    private int maxNumber;

    public PrimeFactorizer(int maxNumToFactorize) {
        // TODO: implement this constructor
        //create an array of all prime candidates:
        maxNumber=maxNumToFactorize;
        //assignment
        int maxPrime = (int) Math.ceil(Math.sqrt(maxNumToFactorize));
        int[] primeCandidates=new int[maxPrime-1];
        //populating the array:
        //use a for loop for that:
        int i;
        //I start with (i-2) because we need to start with first element, yet
        //initial condition is i=2
        //delete non primes by crossing them out through iteration
        for(i=2;i<=maxPrime;i++) primeCandidates[i - 2] = i;
        int primesNumber=0;
        //keeps track of the number of prime numbers to create an array
        // of only primes later
        for(int candidate: primeCandidates){
            if (candidate!=0){
                for (i=0; i<=(maxPrime-2);i++){
                    if ((primeCandidates[i] != candidate) && (primeCandidates[i] % candidate == 0))
                    {primeCandidates[i]=0;}
                }
                primesNumber++;
            }

        }
        this.primesArray=new int[primesNumber];
        //we just created an array of # of primes size

        //now add elements that weren't crossed out:
        i=0;
        for (int candidate: primeCandidates){
            if (candidate!=0){
                this.primesArray[i]=candidate;
                i++;}

        }
    }
    //
    public int[] computePrimeFactorization(int numToFactorize) {
        // TODO: implement this method
        int[] factorsArray;
        //initializes a place to store factors

        //edge cases:
        if ((numToFactorize<1)||(numToFactorize>maxNumber)) {
            factorsArray = null;
        }
            //what should i do in this case? like i want to return nothing in case
//            //numToFactorize is like this
          else if (numToFactorize==1){
            factorsArray=new int[1];
            factorsArray[0]=1;
        }

        // standard case scenario:
        //we are going to iterate all the elements of primeList
        //in every iteration we check if iterable is a factor of numToFactorize
        //if it is, we will keep track of the numbers
        else {
            int number=0;
            int iterableNumber=numToFactorize;
            for (int prime: this.primesArray){
                boolean stillFactors=true;
                while (stillFactors){
                    if (iterableNumber%prime==0)
                    {
                        number++;
                        iterableNumber/=prime;
                    }
                    else{
                        stillFactors=false;
                    }
                }
            }
            // Check if there's a remaining factor
            if (iterableNumber > 1) {
                number++;
            }
          if (number==0)
          {
              factorsArray=new int [1];
              factorsArray[0]=numToFactorize;
              //here we cover the case where a prime number is larger than the sqrt
              //of max number
          }
          else{
              factorsArray=new int[number];
              number=0;
              iterableNumber=numToFactorize;
              for (int prime: this.primesArray)
              {
                  boolean stillFactors=true;
                  while (stillFactors){
                      if (iterableNumber%prime==0)
                      {
                          factorsArray[number]=prime;
                          number++;
                          iterableNumber/=prime;
                      }
                      else{
                          stillFactors=false;
                      }
                  }
              }
              if (iterableNumber > 1) {
                  factorsArray[number] = iterableNumber;
              }
          }

        }
        return factorsArray;
    }
}
