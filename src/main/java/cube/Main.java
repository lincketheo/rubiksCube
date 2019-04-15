package cube;

public class Main{
    
    public static void main(String [] args){
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

    }
}
