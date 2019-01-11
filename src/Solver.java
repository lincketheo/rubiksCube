import java.util.ArrayList;

public class Solver {

    public static ArrayList<Move> solveCubeFromScramble(int dimension, ArrayList<Move> scramble) {
        ArrayList<Move> solver = new ArrayList<>();
        Cubenxn cube = new Cubenxn(dimension);
        cube.moveSequenceNxN(scramble);
        cube.algorithm.clear();
        Cube newCube = cube.solveCube(cube);

        solver = newCube.algorithm;
        return solver;
    }

    public static ArrayList<Move> generateScramble(int dimension, int noMoves) {
        ArrayList<Move> scramble = new ArrayList<>();
        String[] possibleMoves = { "X+", "Y+", "Z+", "X-", "Y-", "Z-" };
        int randomLayer;
        int randomChooser;
        for (int i = 0; i < noMoves; i++) {
            randomLayer = (int) (Math.random() * dimension);
            randomChooser = (int) (Math.random() * 6);
            Move move = new Move(possibleMoves[randomChooser], randomLayer);
            scramble.add(move);
        }

        return scramble;
    }

    public static void main(String[] args) {
        ArrayList<Move> scramble = generateScramble(11, 1000);
        ArrayList<Move> solver = solveCubeFromScramble(11, scramble);

        Cubenxn myCube = new Cubenxn(11);
        myCube.moveSequenceNxN(scramble);
        myCube.printCube();
        myCube.moveSequenceNxN(solver);
        myCube.printCube();
    }
}