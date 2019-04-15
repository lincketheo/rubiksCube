package cube;

public class Main{
    
    public static void main(String [] args){
        if(args.length < 2 || args.length > 3){
            System.out.println("USAGE:  \nrun java -jar {jar file} <DIMENSION> <SCRAMBLE>");
            System.out.println("Dimension = cube dimension\nScramble = number of scramble moves");
            System.out.println("-a to reveal the algorithm applied to the cube");
            return; 
        }        

        int dim = Integer.parseInt(args[0]);
        int scramble = Integer.parseInt(args[1]);        

        Cubenxn myCube = new Cubenxn(dim);
        System.out.println("============= Origional Cube at " + dim + " dimension=============");
        myCube.printCube();

        long startTime = System.nanoTime();
        
        myCube.scrambleNxN(scramble);

        long endTime = System.nanoTime();

        long timeElapsed = endTime - startTime;

        System.out.println("Scrambled cube at " + scramble + " moves");
        
        
        myCube.printCube();

        System.out.println("Execution time of scramble in nanoseconds : " + timeElapsed);

        System.out.println("Execution time of scramble in milliseconds : " + timeElapsed / 1000000);
        startTime = System.nanoTime();

        Cube solvedCube = myCube.solveCube(myCube);

        endTime = System.nanoTime();

        // get difference of two nanoTime values
        timeElapsed = endTime - startTime;
        System.out.println("============ Solved cube =============");
        solvedCube.printCube();

        System.out.println("Execution time of reduction in nanoseconds  : " + timeElapsed);

        System.out.println("Execution time of reduction in milliseconds : " + timeElapsed / 1000000);
        if(args.length > 2 && args[2].equals("-a")){
            System.out.println(solvedCube.algorithm);
        }

    }
}
