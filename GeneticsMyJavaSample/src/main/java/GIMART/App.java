package GIMART;

import java.util.Random;

/**
 * Program that implements a simple genetic algorithm
 *   for searching a local minimum of a function
 *   within an area [0..1][0..1]
 */
public class App 
{
    public static final int NPARAMS = 2;
    public static final int NCHROMOSOMES = 10;
    public static final int REPEATS = 100;
    public static final double MUTATIONJITTER = 1/1000.0;


    // Point of extremum
    public static final double A = 0.9;
    public static final double B = 0.1;

    // Genome stores values of a and b
    //   so that meGenome is an array of [NCHROMOSOMES][2]
    private static double[][] myGenome;

    // Fenotype stores a value of function fitness_fun
    //   for appropriate cromosome
    //   myFenotype[i] = fitness_fun(myGenome[i][0], myGenome[i][1])
    private static double[] myFenotype;

    // a paraboloid function is used as a fitness function
    private static Double fitness_fun(double a, double b, double x1, double x2) {
        return (a - A) * (a - A) + (b - B) * (b - B);
    }

    public static void main( String[] args ) {
        myGenome = new double[NCHROMOSOMES][NPARAMS];
        myFenotype = new double[NCHROMOSOMES];

        Random rand = new Random();

        // Fill in all chromosomes with random values in range [0..1]
        for (int j = 0; j < NCHROMOSOMES; j++)
        {
            myGenome[j][0] = rand.nextDouble();
            myGenome[j][1] = rand.nextDouble();
        }

        // main loop of REPEATS count
        for (int i = 0; i < REPEATS; i++)
        {
            // calculate a fitness function on the
            //  basis of each cromosome
            for (int j = 0; j < NCHROMOSOMES; j++) {
                myFenotype[j] = fitness_fun(myGenome[j][0], myGenome[j][1],  0, 1);
            }
            for (int j = 0; j < NCHROMOSOMES; j++)
            {
                // calculate an index of 2 chromosomes in genome
                //   that have minimum fenotype value
                //   (function value)
                int[] idx = new int[2];
                double[] val = new double[2];
                val[0] = myFenotype[0];
                for (int k = 1; k < NCHROMOSOMES; k++)
                {
                    if(val[0] > myFenotype[k]) {
                        val[0] = myFenotype[k];
                        idx[0] = k;
                    }
                }

                idx[1] = -1;
                for (int k = 0; k < NCHROMOSOMES; k++)
                {
                    if(idx[1] == -1 && k != idx[0]) {
                        idx[1] = k;
                        val[1] = myFenotype[k];
                        continue;
                    }
                    if((val[1] > myFenotype[k]) && (k != idx[0])) {
                        val[1] = myFenotype[k];
                        idx[1] = k;
                    }
                }

                // the top chromosome
                //    before order of chromosomes in changed
                System.out.print( "a = ");
                System.out.print ( myGenome[0][0] );
                System.out.print( " b = ");
                System.out.print( myGenome[0][1] );
                System.out.print( " f = ");
                System.out.println( val[0] );

                // set 2 top chromosomes the ones which
                //   have minimal value of fenotype
                //   they "survive"
                double myGenome2[][] = new double[NCHROMOSOMES][NPARAMS];

                // add some mutation - a small jittering factor to top genes
                myGenome2[0][0] =  myGenome[idx[0]][0] + (0.5 - rand.nextDouble()) * MUTATIONJITTER;
                myGenome2[0][1] =  myGenome[idx[0]][1] + (0.5 - rand.nextDouble()) * MUTATIONJITTER;
                myGenome2[1][0] =  myGenome[idx[1]][0] + (0.5 - rand.nextDouble()) * MUTATIONJITTER;
                myGenome2[1][1] =  myGenome[idx[1]][1] + (0.5 - rand.nextDouble()) * MUTATIONJITTER;

                // crossover:
                //   the following genea are produces by some recombination
                //   of 2 top chromosome
                // other genes do not survive
                myGenome2[2][0] =  (myGenome[idx[0]][0] + myGenome[idx[1]][0]) / 2;
                myGenome2[2][1] =  (myGenome[idx[0]][1] + myGenome[idx[1]][1]) / 2;
                myGenome2[3][0] =  Math.max(myGenome[idx[0]][0], myGenome[idx[1]][0]);
                myGenome2[3][1] =  Math.max(myGenome[idx[0]][1], myGenome[idx[1]][1]);
                myGenome2[4][0] =  Math.min(myGenome[idx[0]][0], myGenome[idx[1]][0]);
                myGenome2[4][1] =  Math.min(myGenome[idx[0]][1], myGenome[idx[1]][1]);
                myGenome2[5][0] =  Math.max(myGenome[idx[0]][0], myGenome[idx[1]][0]);
                myGenome2[5][1] =  Math.min(myGenome[idx[0]][1], myGenome[idx[1]][1]);
                myGenome2[6][0] =  Math.min(myGenome[idx[0]][0], myGenome[idx[1]][0]);
                myGenome2[6][1] =  Math.max(myGenome[idx[0]][1], myGenome[idx[1]][1]);

                // a generation of new random population of chromosomes appears
                for (int l = 7; l < NCHROMOSOMES; l++)
                {
                    myGenome2[l][0] = rand.nextDouble();
                    myGenome2[l][1] = rand.nextDouble();
                }

                // replace an old array of genome with a new one
                myGenome = myGenome2;
            }
        }
        System.out.println( "END!" );
    }
}
