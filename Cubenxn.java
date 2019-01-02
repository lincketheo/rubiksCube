import java.util.*;

import com.sun.java.swing.action.AlignCenterAction;

public class Cubenxn extends Cube {

    /**
     * A Cube nxn is a cube that is more than 3 dimensions. This is a step of moves
     * that solves only the center and the edge pieces of the rubiks cube When a 3x3
     * cube is input, it goes to Cube3x3. All cubes are interacted with in the main
     * Cube class However, they change class, starting from a cube nxnn to a cube
     * 3x3 then to acube while being sovled. These methods will never be used on a
     * 3x3 cube.
     * 
     * @param dimension
     */

    ArrayList<CubePiece> cubePieces = new ArrayList<>();

    public Cubenxn(int dimension) {
        super(dimension);
        for (int i = 0; i < dimension * dimension * 6; i++) {
            cubePieces.add(new CubePiece(i, false));
        }
    }

    // Solving methods
    /**
     * ***********************************************************************************
     * SOLVING THE CENTERS
     * ***********************************************************************************
     */

    // Cube is facing you, find a piece that matechs the coordinate pair (row, col)

    // returns the face color of a face of a scrambled cube (if dimension is odd,
    // just get the center piece, otherwise, assume the cube is oriented with
    // classic top bottom left right orientation)

    // finds all pieces on the cube that can fit in a given center spot (returns an
    // array of all the absolute indexes of the positions)
    public ArrayList<Integer> findAvaliableCenterPieces(int row, int col, int color) {
        // either 1, 2 or 4, the rest will be zero
        ArrayList<Integer> indexes = new ArrayList<>();
        if (col <= 0 || col >= getDimension() - 1) {
            return indexes;
        }

        // for each, check whether cubie can fit into the slot row col and that it is
        // not already in place (if in place, we don't really want to move it)
        int base = getDimension() * getDimension();
        for (int i = 0; i < 6; i++) {
            if (!cubePieces.get(getFaces().get(getPosition(i, row, col))).inPlace
                    && color == getFaces().get(getPosition(i, row, col)) / base) {
                indexes.add(getPosition(i, row, col));
            }
            if (!cubePieces.get(getFaces().get(getPosition(i, col, getDimension() - 1 - row))).inPlace
                    && color == getFaces().get(getPosition(i, col, getDimension() - 1 - row)) / base) {
                indexes.add(getPosition(i, col, getDimension() - 1 - row));
            }
            if (!cubePieces.get(getFaces().get(getPosition(i, getDimension() - 1 - col, row))).inPlace
                    && color == getFaces().get(getPosition(i, getDimension() - 1 - col, row)) / base) {
                indexes.add(getPosition(i, getDimension() - 1 - col, row));
            }
            if (!cubePieces
                    .get(getFaces().get(getPosition(i, getDimension() - 1 - row, getDimension() - 1 - col))).inPlace
                    && color == getFaces().get(getPosition(i, getDimension() - 1 - row, getDimension() - 1 - col))
                            / base) {
                indexes.add(getPosition(i, getDimension() - 1 - row, getDimension() - 1 - col));
            }

        }
        return indexes;

        //remove dubplicates


    }

    // moves a piece on the right face row2 col2 to position row col on the top face
    // without disturbing anyfaces except right and top
    // A COMMON PLACE FOR PROBLEMS - Going in infinite loops
    public void rightFaceCenterPieceToFront(int row, int col, int row2, int col2) {

        // both rows and cols match
        boolean inPlace = (row == row2) && (col == col2);
        int cubieNum = getFaces().get(getPosition(3, row2, col2));
        int failsafe = 0;
        while (failsafe < 4 && !inPlace) {
            rotateSide(1, getDimension() - 1);
            printCubeNums();
            row2 = getRow(cubieNum);
            // col of desired piece (now on the right face)
            col2 = getCol(cubieNum);
            inPlace = (row == row2) && (col == col2);
            failsafe++;

        }

        if (failsafe == 4) {
            System.out.println("ERROR, row and col doesn't match up - row = " + row + " col = " + col + " row2 = "
                    + row2 + " col2 = " + col2);
        }
        rotateFrontFace(-1, row);
        // printCubeNums();

        boolean rotateDirection = (row < (getDimension() / 2) && col < (getDimension() / 2))
                || (row >= (getDimension() / 2) && col >= (getDimension() / 2));

        // if cubie is on top left or bottom right, rotate right face clockwise
        if (rotateDirection) {
            rotateLayer(-1, 0);
            // printCubeNums();
            rotateFrontFace(1, row);
            // printCubeNums();
            rotateLayer(1, 0);
            // printCubeNums();
        }

        // If cubie is on top right or bottom left, rotate right face counter clockwise
        else {
            rotateLayer(1, 0);
            // printCubeNums();
            rotateFrontFace(1, row);
            // printCubeNums();
            rotateLayer(-1, 0);
            // printCubeNums();
        }

    }

    // Solve top middle col only used for odd dimensional cubes
    public void solveTopMiddleColPos(int row) {
        // Could take this out cause probably wont need it, but for now, only needs to
        // be done with an odd dimensional
        // Checks if the row is the center piece, if it is, its technically already
        // solved so just return
        // (never call center piece inplace even though we never check, could probably
        // do that but really no need
        if (row == getDimension() / 2 || getDimension() % 2 == 0) {
            return;
        }

        int color = getFaces().get(getPosition(2, getDimension() / 2, getDimension() / 2))
                / (getDimension() * getDimension());

        // These are the locations of all the possible cubes we could use (locations!
        // not actual cubes!)
        ArrayList<Integer> possibleIndexes = findAvaliableCenterPieces(row, getDimension() / 2, color);

        // All of them are in place, then just return, we don't need to do anything
        if (possibleIndexes.size() == 0) {
            return;
        }

        // We'll pick the first cubie from the list, we could pick any, but we know the
        // list is at least one now
        int desiredCubie = getFaces().get(possibleIndexes.get(0));

        // which face is the cubie on
        int faceDesiredCubie = getFaceOrientation(desiredCubie);

        // used in the if statements
        int rowDesiredCubie = getRow(desiredCubie);
        int colDesiredCubie = getCol(desiredCubie);

        // desired cubie is on the back face
        if (faceDesiredCubie == 0) {
            rotateLayer(-1, 0);

            // we will be rotating the back face until the desired cubie and the cubie slot
            // match
            boolean backInPlace = rowDesiredCubie == getDimension() / 2 && colDesiredCubie == row;
            while (!backInPlace) {
                // rotate the back face clockwise
                rotateFrontFace(1, 0);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == getDimension() / 2 && colDesiredCubie == row;
            }

            // cubie's col now equals row, so rotate row index of side
            rotateSide(-1, row);

            // put things back to where they started
            rotateLayer(1, 0);
            rotateSide(1, row);

        }

        // desired cubie is on the left face
        else if (faceDesiredCubie == 1) {
            // Ask if the cubie is in the right spot to move on (row == row col == col)
            boolean backInPlace = rowDesiredCubie == row && colDesiredCubie == getDimension() / 2;
            while (!backInPlace) {
                rotateSide(1, 0);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == row && colDesiredCubie == getDimension() / 2;

            }
            rotateFrontFace(1, row);

            // return everything back to normal
            rotateLayer(1, 0);
            rotateFrontFace(-1, row);
            rotateLayer(-1, 0);
        }

        // desired cubie is on the top face (two step process, move to right, then move
        // to front again)
        else if (faceDesiredCubie == 2) {
            // if the cubie is already in the col, then it is in place, and we gotta find
            // another cubie
            if (colDesiredCubie == getDimension() / 2) {
                solveTopMiddleColPos(row);
                return;
            }

            // otherwise, the cubie is on a different col than what we want, so we will put
            // it on the back
            // remember right face is always our "storing face" or the temporary placeholder
            // for our cubies
            rotateLayer(1, 0);

            // could go back and grab getRow and getCol, but because its a square matrix, if
            // I rotate clockwise, it'll only switch row with col (not if ccw)
            // so rotate the new row
            rotateFrontFace(1, colDesiredCubie);

            // because its a center col piece, it doesn't matter what direction I rotate
            // right face
            rotateSide(1, getDimension() - 1);

            // return to normal for now
            rotateFrontFace(-1, colDesiredCubie);
            rotateLayer(-1, 0);

            // now cubie is on the right face, do the right face thingy
            // Ask if the cubie is in the right spot to move on (row == row col == col)
            ////////////////////////////////////// ?AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            // I CAN CHANGE THIS IF I MAKE A SEPERATE METHOD
            boolean backInPlace = rowDesiredCubie == row && colDesiredCubie == getDimension() / 2;
            while (!backInPlace) {
                rotateSide(1, getDimension() - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == row && colDesiredCubie == getDimension() / 2;

            }

            rotateFrontFace(-1, row);

            // return everything back to normal
            rotateLayer(1, 0);
            rotateFrontFace(1, row);
            rotateLayer(-1, 0);

        }

        // desired cubie is on the right face (same as part two of front face (*above))
        else if (faceDesiredCubie == 3) {
            boolean backInPlace = rowDesiredCubie == row && colDesiredCubie == getDimension() / 2;
            while (!backInPlace) {
                rotateSide(1, getDimension() - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == row && colDesiredCubie == getDimension() / 2;

            }

            rotateFrontFace(-1, row);

            // return everything back to normal
            rotateLayer(1, 0);
            rotateFrontFace(1, row);
            rotateLayer(-1, 0);

        }

        // desired cubie is on the bottom face (OH NO) do the same thing as back, but
        // rotate two times
        else if (faceDesiredCubie == 4) {
            rotateLayer(-1, 0);

            // we will be rotating the back face until the desired cubie and the cubie slot
            // match
            boolean backInPlace = rowDesiredCubie == getDimension() / 2 && colDesiredCubie == getDimension() - 1 - row;
            while (!backInPlace) {
                // rotate the bottom face clockwise
                rotateLayer(1, getDimension() - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == getDimension() / 2 && colDesiredCubie == getDimension() - 1 - row;
            }

            // cubie's col now equals row, so rotate row index of side
            rotateSide(-1, row);
            rotateSide(-1, row);

            // put things back to where they started
            rotateLayer(1, 0);
            rotateSide(1, row);
            rotateSide(1, row);

        }

        // cube is on front face
        else if (faceDesiredCubie == 5) {
            rotateLayer(-1, 0);

            // we will be rotating the back face until the desired cubie and the cubie slot
            // match
            boolean backInPlace = rowDesiredCubie == getDimension() / 2 && colDesiredCubie == row;
            while (!backInPlace) {
                // rotate the front face clockwise
                rotateFrontFace(1, getDimension() - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == getDimension() / 2 && colDesiredCubie == row;
            }

            // cubie's col now equals row, so rotate row index of side
            rotateSide(1, row);

            // put things back to where they started
            rotateLayer(1, 0);
            rotateSide(-1, row);

        }

    }

    public void solveTopMiddleCol() {
        for (int i = 1; i < getDimension() - 1; i++) {
            solveTopMiddleColPos(i);
            cubePieces.get(getFaces().get(getPosition(2, i, getDimension() / 2))).inPlace = true;

        }
    }

    // desiredCubie is the value of the cubie we want in position desiredRow,
    // desiredCol on the front face (face 2)
    // wherever cubie is, moves it to a spot so that all we need to do is rotate
    // desired face and then insert the cubie in the right place.
    public void alignCubies(int desiredCubie, int desiredRow, int desiredCol) {
        int face = getFaceOrientation(desiredCubie);

        int row = getRow(desiredCubie);
        int col = getCol(desiredCubie);

        boolean inPlace;

        int cubieTemp = getFaces().get(getPosition(5, desiredRow, desiredCol));

        // cubie is on the "middle layer"
        if (face == 0) {

            inPlace = getDimension() - 1 - row == desiredRow && getDimension() - 1 - col == desiredCol;

            // cubie will be slidden in horizontally so no need to change orientation of
            // frontface
            while (!inPlace) {
                rotateFrontFace(1, 0);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);
                inPlace = getDimension() - 1 - row == desiredRow && getDimension() - 1 - col == desiredCol;
            }
        } else if (face == 3) {
            inPlace = getDimension() - 1 - row == desiredCol && col == desiredRow;

            // cubie will be slidden in horizontally so no need to change orientation of
            // frontface
            while (!inPlace) {

                rotateSide(1, getDimension() - 1);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);
                inPlace = ((getDimension() - 1 - row) == desiredCol) && (col == desiredRow);
            }

        }

        else if (face == 1) {
            // move piece to the right face, then do this again and return
            rotateLayer(-1, getDimension() - 1 - col);
            rotateLayer(-1, getDimension() - 1 - col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < getDimension() / 2 && col < getDimension() / 2)
                    || (row >= getDimension() / 2 && col >= getDimension() / 2);

            if (quadrant) {
                rotateSide(1, getDimension() - 1);
            } else {
                rotateSide(-1, getDimension() - 1);

            }

            // IMPORTANT col changed so now col equals the laayer we want to turn back to
            // place
            rotateLayer(-1, col);
            rotateLayer(-1, col);
            // now cubie is on the right face so do this again
            alignCubies(desiredCubie, desiredRow, desiredCol);
            return;
        } else if (face == 2) {
            // all edges are free because we haven't solved the top and bottom face yet and
            // we solve sides after top and bottom
            // but assume the bottom is solved
            // so move the piece to the right face then do this again
            rotateLayer(1, 0);

            // col becomes row so rotate col
            rotateFrontFace(1, col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < getDimension() / 2 && col < getDimension() / 2)
                    || (row >= getDimension() / 2 && col >= getDimension() / 2);

            if (quadrant) {
                rotateSide(-1, getDimension() - 1);
            } else {
                rotateSide(1, getDimension() - 1);
            }

            rotateFrontFace(-1, row);
            rotateLayer(-1, 0);
            alignCubies(desiredCubie, desiredRow, desiredCol);
            return;
        } else if (face == 4) {

            // to insert cubie into face, col we're solving for on front face must be
            // horizontal
            rotateFrontFace(1, getDimension() - 1);
            // THIS IS REVERED IN insertcubie

            desiredCol = getCol(cubieTemp);
            desiredRow = getRow(cubieTemp);
            // if we're find it at the bottom, then we are on the first step so just align
            // the row to equal row, only difference is bottom is mirror image
            inPlace = getDimension() - 1 - row == desiredRow && getDimension() - 1 - col == desiredCol;

            // cubie will be slidden in horizontally so no need to change orientation of
            // frontface
            while (!inPlace) {
                rotateLayer(1, getDimension() - 1);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);
                inPlace = getDimension() - 1 - row == desiredRow && getDimension() - 1 - col == desiredCol;
            }

        } else if (face == 5) {
            // f its on the front, its on our col face, so move it to the right face an then
            // do this again.
            // move piece to the right face, then do this again and return
            rotateFrontFace(1, getDimension() - 1);

            rotateLayer(-1, col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < getDimension() / 2 && col < getDimension() / 2)
                    || (row >= getDimension() / 2 && col >= getDimension() / 2);

            if (quadrant) {
                rotateSide(-1, getDimension() - 1);
            } else {
                rotateSide(1, getDimension() - 1);

            }

            // IMPORTANT col changed so now col equals the laayer we want to turn back to
            // place
            rotateLayer(-1, row);
            // now cubie is on the right face so do this again
            alignCubies(desiredCubie, desiredRow, desiredCol);
            return;
        }

    }

    // align cubie has already been done, so desiredCubie row just needs to be
    // turned
    // also cubie is on face 0 3 or 4
    public void insertCubie(int desiredCubie) {
        int face = getFaceOrientation(desiredCubie);
        int row = getRow(desiredCubie);
        int col = getCol(desiredCubie);
        if (face == 0) {
            rotateLayer(1, getDimension() - 1 - row);
            rotateLayer(1, getDimension() - 1 - row);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < getDimension() / 2 && col < getDimension() / 2)
                    || (row >= getDimension() / 2 && col >= getDimension() / 2);

            int direction;

            if (quadrant) {
                direction = -1;
            } else {
                direction = 1;
            }

            rotateFrontFace(direction, getDimension() - 1);
            rotateLayer(1, row);
            rotateLayer(1, row);
            rotateFrontFace(-direction, getDimension() - 1);

        } else if (face == 3) {
            rotateLayer(1, col);
            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < getDimension() / 2 && col < getDimension() / 2)
                    || (row >= getDimension() / 2 && col >= getDimension() / 2);

            int direction;

            if (quadrant) {
                direction = -1;
            } else {
                direction = 1;
            }

            rotateFrontFace(direction, getDimension() - 1);
            rotateLayer(-1, row);
            rotateFrontFace(-direction, getDimension() - 1);
        } else if (face == 4) {

            rotateSide(1, getDimension() - 1 - col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < getDimension() / 2 && col < getDimension() / 2)
                    || (row >= getDimension() / 2 && col >= getDimension() / 2);

            if (quadrant) {
                rotateFrontFace(1, getDimension() - 1);
                rotateSide(-1, col);
                rotateFrontFace(-1, getDimension() - 1);
                rotateFrontFace(-1, getDimension() - 1);
            } else {
                rotateFrontFace(-1, getDimension() - 1);
                rotateSide(-1, col);
            }
            // remember front face is in the wrong orientation so MAKE IT RIGHT

        }

    }

    // moves the correct colored cubie to the position row col on the front face
    // assuming we are making a col of cubies
    public void solveCubieFrontFaceRowCol(int row, int col, int color) {
        if (row < 1 || row > getDimension() - 2 || col < 1 || col > getDimension() - 2) {
            System.out.println("Error: tried to move an edge piece to the center. Line no "
                    + Thread.currentThread().getStackTrace()[1].getLineNumber());
            return;
        } /*else if ((getFaces().get(getPosition(5, row, col)) / (getDimension() * getDimension())) == color) {
            cubePieces.get(getFaces().get(getPosition(5, row, col))).inPlace = true;
            return;
        } */else if (getDimension() % 2 == 1 && col == getDimension() / 2) {
            System.out.println("Error: you can't execute alignCubie on a center col piece line no "
                    + Thread.currentThread().getStackTrace()[1].getLineNumber());
            return;
        }

        // find desired cubie
        ArrayList<Integer> possibleIndexes = findAvaliableCenterPieces(row, col, color);

        // findabaliablecenters only returns indexes when the cubies are not solved
        int desiredCubie = getFaces().get(possibleIndexes.get(0));

        alignCubies(desiredCubie, row, col);
        insertCubie(desiredCubie);

        // Don't want to mess up what we've alreay done so mark this as true
        cubePieces.get(desiredCubie).inPlace = true;
        printCube();


    }

    public void solveColFrontFace(int col, int color) {
        for (int i = 1; i < getDimension() - 1; i++) {

            solveCubieFrontFaceRowCol(i, col, color);
        }
    }

    // inserts vertical col into place (correctly on the desiredFace, (orientation
    // of col depends on the face))
    // will only be inserting cols into the top face and the left face
    // col is always stored on the front face

    public void insertColFrontFace(int col, int desiredFace) {
        if (desiredFace == 2) {
            rotateLayer(1, 0);
            rotateLayer(1, 0);
            rotateSide(1, col);
            rotateLayer(1, 0);
            rotateLayer(1, 0);
            rotateSide(-1, col);
        } else if (desiredFace == 1) {
            rotateFrontFace(-1, getDimension() - 1);
            rotateSide(1, 0);
            rotateSide(1, 0);
            rotateLayer(1, getDimension() - 1 - col);
            rotateSide(1, 0);
            rotateSide(1, 0);
            rotateLayer(-1, getDimension() - 1 - col);
        }

    }

    public void solveFirstFourFronts() {
        orientCube(5, 2);
        int [] topOrder = { 2, 4, 2, 2 };
        int [] frontOrder = { 5, 5, 5, 3 };
        int [] colorSolveOrder = { 2, 4, 1, 5 };
        for (int i = 0; i < 4; i++) {
            orientCube(frontOrder[i], colorSolveOrder[i]);
            solveTopMiddleCol();
            orientCube(frontOrder[i], topOrder[i]);
            for (int j = 1; j < getDimension() - 1; j++) {
                solveColFrontFace(j, colorSolveOrder[i]);
                insertColFrontFace(i, getFaceOrientation(getCenterCubie(colorSolveOrder[i])));
            }
        }
    }

    public void scrambleNxN(int moves) {
        String[] possibleMoves = { "X", "Y", "Z", "X'", "Y'", "Z'" };
        int randomLayer;
        int randomChooser;
        for (int i = 0; i < moves; i++) {
            randomLayer = (int) (Math.random() * getDimension());
            randomChooser = (int) (Math.random() * 6);
            Move move = new Move(possibleMoves[randomChooser], randomLayer);
            moveSequenceNxN(move);
        }
        cubePieces = new ArrayList<>();
        for (int i = 0; i < getDimension() * getDimension() * 6; i++) {
            cubePieces.add(new CubePiece(i, false));
        }
    }

    public static A printAllIndecesInPlace(ArrayList<CubePiece> arraylist) {
        
        ArrayList<Integer> cubepieces = new ArrayList<>();


        for (CubePiece cube : arraylist) {
            if (cube.inPlace) {
                cubepieces.add(cube.cubeVal);
            }
        }
        return cubepieces;
    }

    public static void main(String[] args) {

        Cubenxn myCube = new Cubenxn(7);
        
        myCube.printCube();
        myCube.scrambleNxN(100);
        
        myCube.orientCube(5, 2);
        
        myCube.printCube();
        myCube.solveTopMiddleCol();
        myCube.solveColFrontFace(1, 2);
        
        System.out.println("1");
        myCube.printCube();
        myCube.insertColFrontFace(1, 2);
        
        System.out.println("2");
        myCube.printCube();
        myCube.solveColFrontFace(2, 2);
        
        System.out.println("3");
        myCube.printCube();
        myCube.insertColFrontFace(2, 2);
        
        System.out.println("4");
        myCube.printCube();
        myCube.solveColFrontFace(2, 2);
        
        System.out.println("5");
        myCube.printCube();
        myCube.insertColFrontFace(2, 2);
        
        System.out.println("6");
        myCube.printCube();
        myCube.solveColFrontFace(1, 2);
        
        System.out.println("7");
        myCube.printCube();
        myCube.insertColFrontFace(1, 2);
        
        System.out.print("8");
        myCube.printCube();

    }

}