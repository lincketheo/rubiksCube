import java.util.*;

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

    public static ArrayList<Move> filterMoveSetBasic(ArrayList<Move> moveSet) {
        boolean filtered = false;
        while (!filtered) {
            filtered = true;
            // check for inverses
            for (int i = 0; i < moveSet.size() - 1; i++) {
                String move1 = moveSet.get(i).main;
                String move2 = moveSet.get(i + 1).main;
                int layerNo1 = moveSet.get(i).layerNo;
                int layerNo2 = moveSet.get(i + 1).layerNo;
                boolean translations = moveSet.get(i).translation || moveSet.get(i + 1).translation;
                if (!(translations) && layerNo1 == layerNo2 && move1.substring(0, 1).equals(move2.substring(0, 1))
                        && !move1.substring(1).equals(move2.substring(1))) {
                    moveSet.remove(i);
                    moveSet.remove(i);
                    i--;
                    filtered = false;
                } else if (move1.equals(move2) && layerNo1 == -layerNo2 && layerNo1 != 0) {
                    moveSet.remove(i);
                    moveSet.remove(i);
                    i--;
                    filtered = false;
                }
            }
            // check for 3 moves = 1 move
            for (int i = 0; i < moveSet.size() - 2; i++) {
                boolean translations = moveSet.get(i).translation || moveSet.get(i + 1).translation
                        || moveSet.get(i + 2).translation;

                if (!(translations) && moveSet.get(i).equals(moveSet.get(i + 1))
                        && moveSet.get(i).equals(moveSet.get(i + 2))) {
                    moveSet.remove(i);
                    moveSet.remove(i);
                    if (moveSet.get(i).main.substring(1).equals("-")) {
                        moveSet.get(i).main = moveSet.get(i).main.substring(0, 1) + "+";
                    } else {
                        moveSet.get(i).main = moveSet.get(i).main.substring(0, 1) + "-";
                    }
                    i--;
                    filtered = false;
                } else if (moveSet.get(i).equals(moveSet.get(i + 1)) && moveSet.get(i).equals(moveSet.get(i + 2))) {
                    moveSet.remove(i);
                    moveSet.remove(i);
                    moveSet.get(i).layerNo *= -1;
                    i--;
                    filtered = false;
                }
            }
            // doesn't happen in 3x3s, but it does happen in larger cubes
            for (int i = 0; i < moveSet.size() - 3; i++) {

                if (moveSet.get(i).equals(moveSet.get(i + 1)) && moveSet.get(i).equals(moveSet.get(i + 2))
                        && moveSet.get(i).equals(moveSet.get(i + 3))) {
                    moveSet.remove(i);
                    moveSet.remove(i);
                    moveSet.remove(i);
                    moveSet.remove(i);
                    i--;
                    filtered = false;
                }

            }

        }

        return moveSet;
    }


    public static void runJSObject(int dimension, int id) {
        System.out.println();
        ArrayList<Move> scramble = generateScramble(dimension, 100);
        ArrayList<Move> solver = solveCubeFromScramble(dimension, scramble);
        scramble = filterMoveSetBasic(scramble);
        solver = filterMoveSetBasic(solver);
        System.out.print("var cube" + dimension + "" + id + " = {");
        System.out.println();
        System.out.print("dimension:" + dimension + ",");
        System.out.println();
        System.out.print("scrambleMoves: [");
        for (int i = 0; i < scramble.size(); i++) {
            System.out.print("'" + scramble.get(i).main + "', ");
        }
        System.out.print("],");
        System.out.println();
        System.out.print("scrambleSlices: [");
        for (int i = 0; i < scramble.size(); i++) {
            System.out.print("" + scramble.get(i).layerNo + ", ");
        }
        System.out.print("],");
        System.out.println();
        System.out.print("solverMoves: [");
        for (int i = 0; i < solver.size(); i++) {
            System.out.print("'" + solver.get(i).main + "', ");
        }
        System.out.print("],");
        System.out.println();
        System.out.print("solverSlices: [");
        for (int i = 0; i < solver.size(); i++) {
            System.out.print("" + solver.get(i).layerNo + ", ");
        }
        System.out.print("]");
        System.out.println();
        System.out.print("};");
        System.out.println();

    }

    public static void main(String[] args) {

	int maxDimension = 30;
        for(int i = 3; i < maxDimension + 1; i++){
            runJSObject(i, 01);
        }
        
        System.out.println();
        System.out.print("var cubeArray = [");
        for(int i = 3; i < maxDimension + 1; i++){
            System.out.print("cube" + i + "" + 01 + " , ");
        }

        System.out.print("];");
        

    }
}
