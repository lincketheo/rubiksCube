import java.util.*;

public class Cubenxn extends Cube {

    /**
     * A Cube nxn is a cube that is more than 3 dimensions. This is a step of moves
     * that solves only the center and the edge pieces of the rubiks cube When a 3x3
     * cube is input, it goes to Cube3x3. All cubes are interacted with in the main
     * Cube class However, they change class, starting from a cube nxnn to a cube
     * 3x3 then to acube while being sovled. These methods will never be used on a
     * 3x3 cube.
     * 
     */

    // local variable cubePieces used to identify when all pieces are in place to
    // move on to a 3x3 cube
    private ArrayList<CubePiece> cubePieces = new ArrayList<>();

    // used for testing reduction of cube
    public boolean reduced;

    public Cubenxn(int dimension) {
        super(dimension);
        for (int i = 0; i < dimension * dimension * 6; i++) {
            cubePieces.add(new CubePiece(i, false));
        }
        reduced = false;
    }

    // Solving methods
    /**
     * ***********************************************************************************
     * SOLVING THE CENTERS
     * ***********************************************************************************
     */

    // Cube is facing you, find a piece that matechs the coordinate pair (row, col)

    // finds all pieces on the cube that can fit in a given center spot (returns an
    // array of all the absolute indexes of the positions)
    private ArrayList<Integer> findAvaliableCenterPieces(int row, int col, int color) {
        // either 1, 2 or 4, the rest will be zero
        ArrayList<Integer> indexes = new ArrayList<>();
        if (col <= 0 || col >= dimension - 1) {
            return indexes;
        }

        // for each, check whether cubie can fit into the slot row col and that it is
        // not already in place (if in place, we don't really want to move it)
        int base = dimension * dimension;
        for (int i = 0; i < 6; i++) {
            if (!cubePieces.get(faces.get(getPosition(i, row, col))).inPlace
                    && color == faces.get(getPosition(i, row, col)) / base) {
                indexes.add(getPosition(i, row, col));
            }
            if (!cubePieces.get(faces.get(getPosition(i, col, dimension - 1 - row))).inPlace
                    && color == faces.get(getPosition(i, col, dimension - 1 - row)) / base) {
                indexes.add(getPosition(i, col, dimension - 1 - row));
            }
            if (!cubePieces.get(faces.get(getPosition(i, dimension - 1 - col, row))).inPlace
                    && color == faces.get(getPosition(i, dimension - 1 - col, row)) / base) {
                indexes.add(getPosition(i, dimension - 1 - col, row));
            }
            if (!cubePieces.get(faces.get(getPosition(i, dimension - 1 - row, dimension - 1 - col))).inPlace
                    && color == faces.get(getPosition(i, dimension - 1 - row, dimension - 1 - col)) / base) {
                indexes.add(getPosition(i, dimension - 1 - row, dimension - 1 - col));
            }

        }
        return indexes;

        // remove dubplicates

    }

    // moves a piece on the right face row2 col2 to position row col on the top face
    // without disturbing anyfaces except right and top
    // A COMMON PLACE FOR PROBLEMS - Going in infinite loops
    public void rightFaceCenterPieceToFront(int row, int col, int row2, int col2) {

        // both rows and cols match
        boolean inPlace = (row == row2) && (col == col2);
        int cubieNum = faces.get(getPosition(3, row2, col2));
        int failsafe = 0;
        while (failsafe < 4 && !inPlace) {
            rotateSide(1, dimension - 1);
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

        boolean rotateDirection = (row < (dimension / 2) && col < (dimension / 2))
                || (row >= (dimension / 2) && col >= (dimension / 2));

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
        if (row == dimension / 2 || dimension % 2 == 0) {
            cubePieces.get(faces.get(getPosition(2, dimension / 2, dimension / 2))).inPlace = true;
            return;
        }

        int color = getColor(2);

        // These are the locations of all the possible cubes we could use (locations!
        // not actual cubes!)
        ArrayList<Integer> possibleIndexes = findAvaliableCenterPieces(row, dimension / 2, color);

        // All of them are in place, then just return, we don't need to do anything
        if (possibleIndexes.size() == 0) {
            return;
        }

        int desiredCubie = faces.get(possibleIndexes.get(0));

        // origionally I just picked the first, but on rare cases, everytime you
        // transfer a cube from face 2 to face 5,
        // a new cube is transfered to face 2, so it would cause infinite recursive
        // loops (pretty rare, but it happens)
        // so now, just pick the first one that isn't on face 2
        if (getFaceOrientation(desiredCubie) == 2) {
            for (int i = 0; i < possibleIndexes.size(); i++) {
                if (getFaceOrientation(faces.get(possibleIndexes.get(i))) != 2) {
                    desiredCubie = faces.get(possibleIndexes.get(i));
                    break;
                }
            }
        }

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
            boolean backInPlace = rowDesiredCubie == dimension / 2 && colDesiredCubie == row;
            while (!backInPlace) {
                // rotate the back face clockwise
                rotateFrontFace(1, 0);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == dimension / 2 && colDesiredCubie == row;
            }

            // cubie's col now equals row, so rotate row index of side
            rotateSide(-1, row);

            // put things back to where they started
            rotateLayer(1, 0);
            rotateSide(1, row);
            return;

        }

        // desired cubie is on the left face
        else if (faceDesiredCubie == 1) {
            // Ask if the cubie is in the right spot to move on (row == row col == col)
            boolean backInPlace = rowDesiredCubie == row && colDesiredCubie == dimension / 2;
            while (!backInPlace) {
                rotateSide(1, 0);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == row && colDesiredCubie == dimension / 2;

            }
            rotateFrontFace(1, row);

            // return everything back to normal
            rotateLayer(1, 0);
            rotateFrontFace(-1, row);
            rotateLayer(-1, 0);
            return;
        }

        // desiredCubie is on top face, will move to front face because front face is
        // always temporary storage
        else if (faceDesiredCubie == 2) {

            // if cubie is on the col that can be solved (too complicated to just keep it
            // there, so we will move it to the front face)
            if (colDesiredCubie == dimension / 2) {

                colDesiredCubie = getCol(desiredCubie);
                rotateLayer(-1, 0);

                rotateSide(-1, colDesiredCubie);

                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);

                rotateFrontFace(1, dimension - 1);

                rotateSide(1, colDesiredCubie);
                rotateLayer(1, 0);

                solveTopMiddleColPos(row);

                return;

            } else {
                rotateSide(-1, colDesiredCubie);
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                boolean quadrant = (rowDesiredCubie < dimension / 2 && colDesiredCubie < dimension / 2)
                        || (rowDesiredCubie >= dimension / 2 && colDesiredCubie >= dimension / 2);
                if (quadrant) {
                    rotateFrontFace(1, dimension - 1);
                } else {
                    rotateFrontFace(-1, dimension - 1);
                }
                rotateSide(1, colDesiredCubie);

                solveTopMiddleColPos(row);
                return;

                // Problem before: entire row moved to right face and we rotated the right face
                // and messed shit up
            }
        }

        // desired cubie is on the right face (same as part two of front face (*above))
        else if (faceDesiredCubie == 3) {
            boolean backInPlace = rowDesiredCubie == row && colDesiredCubie == dimension / 2;
            while (!backInPlace) {
                rotateSide(1, dimension - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == row && colDesiredCubie == dimension / 2;

            }

            rotateFrontFace(-1, row);

            // return everything back to normal
            rotateLayer(1, 0);
            rotateFrontFace(1, row);
            rotateLayer(-1, 0);
            return;

        }

        // desired cubie is on the bottom face (OH NO) do the same thing as back, but
        // rotate two times
        else if (faceDesiredCubie == 4) {
            rotateLayer(-1, 0);

            // we will be rotating the back face until the desired cubie and the cubie slot
            // match
            boolean backInPlace = rowDesiredCubie == dimension / 2 && colDesiredCubie == dimension - 1 - row;
            while (!backInPlace) {
                // rotate the bottom face clockwise
                rotateLayer(1, dimension - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == dimension / 2 && colDesiredCubie == dimension - 1 - row;
            }

            // cubie's col now equals row, so rotate row index of side
            rotateSide(-1, row);
            rotateSide(-1, row);

            // put things back to where they started
            rotateLayer(1, 0);
            rotateSide(1, row);
            rotateSide(1, row);
            return;

        }

        // cube is on front face
        else if (faceDesiredCubie == 5) {
            rotateLayer(-1, 0);

            // we will be rotating the back face until the desired cubie and the cubie slot
            // match
            boolean backInPlace = rowDesiredCubie == dimension / 2 && colDesiredCubie == row;
            while (!backInPlace) {
                // rotate the front face clockwise
                rotateFrontFace(1, dimension - 1);

                // Is the cubie in the correct spot to move on?
                rowDesiredCubie = getRow(desiredCubie);
                colDesiredCubie = getCol(desiredCubie);
                backInPlace = rowDesiredCubie == dimension / 2 && colDesiredCubie == row;
            }

            // cubie's col now equals row, so rotate row index of side
            rotateSide(1, row);

            // put things back to where they started
            rotateLayer(1, 0);
            rotateSide(-1, row);
            return;

        }

    }

    public void solveTopMiddleCol() {
        if (dimension % 2 == 0)
            return;
        for (int i = 1; i < dimension - 1; i++) {
            solveTopMiddleColPos(i);
            cubePieces.get(faces.get(getPosition(2, i, dimension / 2))).inPlace = true;

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

        int cubieTemp = faces.get(getPosition(5, desiredRow, desiredCol));

        // cubie is on the "middle layer"
        if (face == 0) {

            inPlace = dimension - 1 - row == desiredRow && dimension - 1 - col == desiredCol;

            // cubie will be slidden in horizontally so no need to change orientation of
            // frontface
            while (!inPlace) {
                rotateFrontFace(1, 0);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);
                inPlace = dimension - 1 - row == desiredRow && dimension - 1 - col == desiredCol;
            }
        } else if (face == 3) {
            inPlace = dimension - 1 - row == desiredCol && col == desiredRow;

            // cubie will be slidden in horizontally so no need to change orientation of
            // frontface
            while (!inPlace) {

                rotateSide(1, dimension - 1);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);
                inPlace = ((dimension - 1 - row) == desiredCol) && (col == desiredRow);
            }

        }

        else if (face == 1) {
            // move piece to the right face, then do this again and return
            rotateLayer(-1, dimension - 1 - col);
            rotateLayer(-1, dimension - 1 - col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                    || (row >= dimension / 2 && col >= dimension / 2);

            if (quadrant) {
                rotateSide(1, dimension - 1);
            } else {
                rotateSide(-1, dimension - 1);

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

            // do a quick check to see which side is solved, bottom or right
            // if back is bottom is solved, we can move cubie to the right face
            // if bottom is not slolved we can move cubie to the bottom face

            // so if the bottom is solved, every front and center piece has an inplace value
            // of true

            // if bottom is solved then we can sotore the cubie on the right face

            rotateLayer(1, 0);

            // col becomes row so rotate col
            rotateFrontFace(1, col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                    || (row >= dimension / 2 && col >= dimension / 2);

            if (quadrant) {
                rotateSide(-1, dimension - 1);
            } else {
                rotateSide(1, dimension - 1);
            }

            rotateFrontFace(-1, row);
            rotateLayer(-1, 0);

            alignCubies(desiredCubie, desiredRow, desiredCol);

            return;

        } else if (face == 4) {

            // to insert cubie into face, col we're solving for on front face must be
            // horizontal
            rotateFrontFace(1, dimension - 1);
            // THIS IS REVERED IN insertcubie

            desiredCol = getCol(cubieTemp);
            desiredRow = getRow(cubieTemp);
            // if we're find it at the bottom, then we are on the first step so just align
            // the row to equal row, only difference is bottom is mirror image
            inPlace = dimension - 1 - row == desiredRow && dimension - 1 - col == desiredCol;

            // cubie will be slidden in horizontally so no need to change orientation of
            // frontface
            while (!inPlace) {
                rotateLayer(1, dimension - 1);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);
                inPlace = dimension - 1 - row == desiredRow && dimension - 1 - col == desiredCol;
            }

        } else if (face == 5) {
            // f its on the front, its on our col face, so move it to the right face an then
            // do this again.
            // move piece to the right face, then do this again and return

            // FIX THIS, CAUSES SOME BUGS

            if (col == desiredCol) {
                rotateLayer(-1, row);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);

                boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                        || (row >= dimension / 2 && col >= dimension / 2);

                if (quadrant) {
                    rotateSide(1, dimension - 1);
                } else {
                    rotateSide(-1, dimension - 1);

                }

                // IMPORTANT col changed so now col equals the laayer we want to turn back to
                // place
                rotateLayer(1, col);
                // now cubie is on the right face so do this again
                alignCubies(desiredCubie, desiredRow, desiredCol);
                return;
            } else {
                rotateFrontFace(1, dimension - 1);
                rotateLayer(-1, col);

                row = getRow(desiredCubie);
                col = getCol(desiredCubie);

                boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                        || (row >= dimension / 2 && col >= dimension / 2);

                if (quadrant) {
                    rotateSide(1, dimension - 1);
                } else {
                    rotateSide(-1, dimension - 1);

                }

                // IMPORTANT col changed so now col equals the laayer we want to turn back to
                // place
                rotateLayer(1, col);
                // now cubie is on the right face so do this again
                rotateFrontFace(-1, dimension - 1);
                alignCubies(desiredCubie, desiredRow, desiredCol);
                return;
            }
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
            rotateLayer(1, dimension - 1 - row);
            rotateLayer(1, dimension - 1 - row);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                    || (row >= dimension / 2 && col >= dimension / 2);

            int direction;

            if (quadrant) {
                direction = -1;
            } else {
                direction = 1;
            }

            rotateFrontFace(direction, dimension - 1);
            rotateLayer(1, row);
            rotateLayer(1, row);
            rotateFrontFace(-direction, dimension - 1);

        } else if (face == 3) {
            rotateLayer(1, col);
            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                    || (row >= dimension / 2 && col >= dimension / 2);

            int direction;

            if (quadrant) {
                direction = -1;
            } else {
                direction = 1;
            }

            rotateFrontFace(direction, dimension - 1);
            rotateLayer(-1, row);
            rotateFrontFace(-direction, dimension - 1);
        } else if (face == 4) {

            rotateSide(1, dimension - 1 - col);

            row = getRow(desiredCubie);
            col = getCol(desiredCubie);

            boolean quadrant = (row < dimension / 2 && col < dimension / 2)
                    || (row >= dimension / 2 && col >= dimension / 2);

            if (quadrant) {
                rotateFrontFace(1, dimension - 1);
                rotateSide(-1, col);
                rotateFrontFace(-1, dimension - 1);
                rotateFrontFace(-1, dimension - 1);
            } else {
                rotateFrontFace(-1, dimension - 1);
                rotateSide(-1, col);
            }
            // remember front face is in the wrong orientation so MAKE IT RIGHT

        }

    }

    // moves the correct colored cubie to the position row col on the front face
    // assuming we are making a col of cubies
    public void solveCubieFrontFaceRowCol(int row, int col, int color) {
        if (row < 1 || row > dimension - 2 || col < 1 || col > dimension - 2) {
            System.out.println("Error: tried to move an edge piece to the center. Line no "
                    + Thread.currentThread().getStackTrace()[1].getLineNumber());
            return;
        } else if ((faces.get(getPosition(5, row, col)) / (dimension * dimension)) == color) {
            cubePieces.get(faces.get(getPosition(5, row, col))).inPlace = true;
            return;
        } else if (dimension % 2 == 1 && col == dimension / 2) {
            System.out.println("Error: you can't execute alignCubie on a center col piece line no "
                    + Thread.currentThread().getStackTrace()[1].getLineNumber());
            return;
        }

        // find desired cubie
        ArrayList<Integer> possibleIndexes = findAvaliableCenterPieces(row, col, color);

        // findabaliablecenters only returns indexes when the cubies are not solved
        if (possibleIndexes.size() == 0) {
            return;
        }
        int desiredCubie = faces.get(possibleIndexes.get(0));
        alignCubies(desiredCubie, row, col);
        insertCubie(desiredCubie);

        // Don't want to mess up what we've alreay done so mark this as true
        cubePieces.get(desiredCubie).inPlace = true;

    }

    public void solveColFrontFace(int col, int color) {
        for (int i = 1; i < dimension - 1; i++) {

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
            rotateFrontFace(-1, dimension - 1);
            rotateSide(1, 0);
            rotateSide(1, 0);
            rotateLayer(1, dimension - 1 - col);
            rotateSide(1, 0);
            rotateSide(1, 0);
            rotateLayer(-1, dimension - 1 - col);
        }

    }

    public void solveFirstFourFronts() {
        orientCube(5, 2);
        int[] topOrder = { 2, 4, 2, 2 };
        int[] frontOrder = { 5, 5, 5, 3 };
        int[] colorSolveOrder = { 2, 4, 1, 5 };
        for (int i = 0; i < 4; i++) {
            orientCube(frontOrder[i], colorSolveOrder[i]);
            solveTopMiddleCol();
            orientCube(frontOrder[i], topOrder[i]);
            for (int j = 1; j < dimension - 1; j++) {
                solveColFrontFace(j, colorSolveOrder[i]);
                insertColFrontFace(i, getFaceOrientation(getCenterCubie(colorSolveOrder[i])));
            }
        }
    }


    // DEBUGGING
    /*
     * private ArrayList<Integer> printAllIndecesInPlace() {
     * 
     * ArrayList<Integer> cubepieces = new ArrayList<>();
     * 
     * for (CubePiece cube : cubePieces) { if (cube.inPlace) {
     * cubepieces.add(cube.cubeVal); } } return cubepieces; }
     */



    public void solveLeftFaceCenter(int color) {

        // solve the top front center
        rotateCubeCounterClockwise();
        rotateCubeUp();
        rotateCubeClockwise();

        solveTopMiddleCol();

        rotateCubeCounterClockwise();
        rotateCubeDown();
        rotateCubeClockwise();

        for (int i = 1; i < dimension / 2; i++) {

            solveColFrontFace(i, color);
            insertColFrontFace(i, 1);
        }

        for (int i = (dimension / 2) - 1; i > 0; i--) {

            solveColFrontFace(i, color);
            insertColFrontFace(i, 1);

        }
    }

    public void solveTopFaceCenter(int color) {

        solveTopMiddleCol();

        for (int i = 1; i < dimension / 2; i++) {

            solveColFrontFace(i, color);
            insertColFrontFace(i, 2);
        }

        for (int i = (dimension / 2) - 1; i > 0; i--) {

            solveColFrontFace(i, color);
            insertColFrontFace(i, 2);

        }

    }

 
     //uses a special algorithm to move center piece to desired face without disrupting any other faces
     //its a long algorithm so thats why I didn't use this for all faces (I could)
    public void solveLastCenterColPos(int row) {
        int color = getColor(2);
        int base = dimension * dimension;

        if (faces.get(getPosition(2, row, dimension / 2)) / base == color) {

            // maybe call cubiePlaces true, its not necessary but it would be consistant
            return;
        } else if (row >= dimension - 1 || row <= 0 || row == dimension / 2) {
            return;
        }

        int desiredCubie;

        if (faces.get(getPosition(5, row, dimension / 2)) / base == color) {
            desiredCubie = faces.get(getPosition(5, row, dimension / 2));
        } else if (faces.get(getPosition(5, dimension / 2, row)) / base == color) {
            desiredCubie = faces.get(getPosition(5, dimension / 2, row));
        } else if (faces.get(getPosition(5, dimension - 1 - row, dimension / 2)) / base == color) {
            desiredCubie = faces.get(getPosition(5, dimension - 1 - row, dimension / 2));
        } else if (faces.get(getPosition(5, dimension / 2, dimension - 1 - row)) / base == color) {
            desiredCubie = faces.get(getPosition(5, dimension / 2, dimension - 1 - row));
        } else {
            // not neccessary, will help debugging
            return;
        }

        int desiredCubieRow = getRow(desiredCubie);

        int desiredCubieCol = getCol(desiredCubie);

        rotateLayer(-1, 0);

        int col = row;
        row = dimension / 2;

        boolean inPlace = desiredCubieCol == col && desiredCubieRow == row;
        int failsafe = 0;

        while (failsafe < 4 && !inPlace) {
            rotateFrontFace(1, dimension - 1);
            desiredCubieRow = getRow(desiredCubie);
            desiredCubieCol = getCol(desiredCubie);
            inPlace = desiredCubieCol == col && desiredCubieRow == row;
            failsafe++;
        }
        if (failsafe == 4) {
            System.out.println("Got stuck in loop line 816");
            return;
        }
        // cubies are now lined up so insert cubie
        rotateSide(1, desiredCubieCol);
        rotateLayer(1, 0);
        rotateSide(-1, desiredCubieCol);
    }

    public void solveLastCenterCol() {
        for (int i = 1; i < dimension - 1; i++) {
            solveLastCenterColPos(i);
            cubePieces.get(faces.get(getPosition(2, i, dimension / 2))).inPlace = true;
        }
    }

    public void alignTopCubieWithFrontCubie(int desiredRow, int desiredCol) {

        int base = dimension * dimension;

        int desiredCubiePos = faces.get(getPosition(5, desiredRow, desiredCol));
        // place we want to insert the cubie
        // last two colors will be 1 and 3

        int colorTop = desiredCubiePos / base;

        int colorFront;
        if (colorTop == 0) {
            colorFront = 3;
        } else {
            colorFront = 0;
        }

        ArrayList<Integer> possibleIndexes = new ArrayList<>();

        possibleIndexes = findAvaliableCenterPieces(desiredRow, desiredCol, colorFront);

        int desiredCubie = -1;

        for (int n : possibleIndexes) {
            if (getFaceOrientation(faces.get(n)) == 2) {
                desiredCubie = faces.get(n);
                break;
            }

        }

        boolean inPlace = getRow(desiredCubie) == desiredRow && getCol(desiredCubie) == desiredCol;

        int failsafe = 0;

        while (failsafe < 4 && !inPlace) {
            rotateLayer(1, 0);
            inPlace = getRow(desiredCubie) == desiredRow && getCol(desiredCubie) == desiredCol;
            failsafe++;
        }

        if (failsafe == 5) {
            System.out.println("Stuck in loop 884");
        }

    }

    public void switchTopCubieWithFrontCubie(int row, int col) {
        // assuming cubies are already aligned
        // if edge cubies, assuming they are on the sides and not on top or bottom

        // this is the cubie that wants to be on the front face but is not
        int desiredCubie = faces.get(getPosition(2, row, col));

        rotateSide(-1, col);

        int rowDesiredCubie = getRow(desiredCubie);
        int colDesiredCubie = getCol(desiredCubie);

        boolean quadrant = (rowDesiredCubie < dimension / 2 && colDesiredCubie < dimension / 2)
                || (rowDesiredCubie >= dimension / 2 && colDesiredCubie >= dimension / 2);

        int direction;

        if (quadrant) {
            direction = 1;
        } else {
            direction = -1;
        }

        rotateFrontFace(direction, dimension - 1);

        colDesiredCubie = getCol(desiredCubie);

        rotateSide(-1, colDesiredCubie);

        rotateFrontFace(-direction, dimension - 1);

        rotateSide(1, col);

        rotateFrontFace(direction, dimension - 1);

        rotateSide(1, colDesiredCubie);

        rotateFrontFace(-direction, dimension - 1);

    }

    public void solveLastTwoFaces(int colorFront, int colorTop) {

        int base = dimension * dimension;
        int tempCubie;

        for (int i = 1; i < dimension - 1; i++) {
            for (int x = 1; x < dimension - 1; x++) {
                tempCubie = faces.get(getPosition(5, i, x));
                if (tempCubie / base != colorFront) {
                    alignTopCubieWithFrontCubie(i, x);
                    switchTopCubieWithFrontCubie(i, x);
                }
                cubePieces.get(tempCubie).inPlace = true;
            }
        }
    }


    //puts everything together from above
    public void solveCenters() {
        orientCube(5, 2);
        // System.out.println("oriented 5 front 2 top");
        // printCube();
        solveTopFaceCenter(2);
        // System.out.println("2 center col solved");
        // printCube();
        rotateCubeUp();
        rotateCubeUp();
        // System.out.println("oriented for bottom face");
        // printCube();
        solveTopFaceCenter(4);
        // System.out.println("first two faces are solved");
        // printCube();
        rotateCubeUp();
        rotateCubeUp();
        solveLeftFaceCenter(1);
        // System.out.println("left face is solved");
        // printCube();
        rotateCubeClockwise();
        solveLeftFaceCenter(5);
        // System.out.println("first 4 faces solved");
        // printCube();
        rotateCubeUp();
        rotateCubeClockwise();
        // System.out.println("oriented for last step");
        // solveLastCenterCol();
        // printCube();
        solveLastTwoFaces(0, 3);
        // System.out.println("all faces solved");
        // printCube();
        boolean solved = checkCenters();
        if (!solved) {
            // really lazy coding but I want this project fucking done
            // so the deal is, about 1 in 100 center solves just dont work so I'm just
            // starting over and solving again
            // really really lazy I know but I cant find why this is so I'm just giving up
            // and redoing it
            scrambleNxN(10);
            solveCenters();
            return;
        }
    }
    


    /**
     * Edge solver methods
     * solves all 10 edges then does parity and swapping algorithms to solve last two edges
     * 
     * general method moves an unsolved piece to the left front edge (this is the piece we're solving for)
     * it solves for whatever color is on top
     * It then moves an unsolved piece to the top left edge, which is used once we insert a cubie into place, we replace
     * front left with top left temporarily so that the only edge we break is an unsolved one
     * moves desired insertion piece to front right edge
     */

     //only moves an unsolved edge piece (no inserting or anything)
    // we don't have to worry about dirupting any edge pieces on this step
    public void moveUnsolvedEdgePieceToLeftFrontEdge() {

        // we are solving the left edge from top to bottom, so if any of them are
        // unsolved at this step, we can just keep the left edge where it is if it is
        // unsolved already

        if (!cubePieces.get(faces.get(getPosition(5, 1, 0))).inPlace) {
            return;
        }

        ArrayList<EdgeCubie> cubies = findUnsolvedEdgePieces();

        // at this point, we aren't halfway solving an edge, so any unsolved edge pieces
        // means the entire edge is unsolved

        if (cubies.size() == 0) {
            // don't know why this would happen, but it means that all the edges are solved,
            // pretty nice
            return;
        }

        // we already checked if edge front / left is unsolved, if it is solved, then
        // there are no cubies in cubies that are contained on face front / left. if it
        // is not, we already returned
        EdgeCubie desiredCubie = cubies.get(0);

        orientCubeFrontLeft(getFaceOrientation(desiredCubie.index1), getFaceOrientation(desiredCubie.index2));

    }

    // moves an unsolved piece without disrupting the front left or right edges to top left edge (for replacing desired solved edge)
    public void moveUnsolvedEdgePiecetoTopLeftEdge() {

        if (!cubePieces.get(faces.get(getPosition(2, 1, 0))).inPlace) {
            return;
        }

        ArrayList<EdgeCubie> cubies = findUnsolvedEdgePieces();

        // at this point, we aren't halfway solving an edge, so any unsolved edge pieces
        // means the entire edge is unsolved

        if (cubies.size() == 0) {
            // don't know why this would happen, but it means that all the edges are solved,
            // pretty nice
            return;
        }

        int failsafe = 0;
        EdgeCubie desiredCubie = null;

        while (failsafe < cubies.size()) {
            int cubie1Face = getFaceOrientation(cubies.get(failsafe).index1);
            int cubie2Face = getFaceOrientation(cubies.get(failsafe).index2);
            if ((cubie1Face == 5 && (cubie2Face == 1 || cubie2Face == 3))
                    || (cubie2Face == 5 && (cubie1Face == 1 || cubie1Face == 3))) {
                failsafe++;
            } else {
                desiredCubie = cubies.get(failsafe);
                break;
            }

        }

        failsafe = 0;

        // we already checked if edge front / left is unsolved, if it is solved, then
        // there are no cubies in cubies that are contained on face front / left. if it
        // is not, we already returned

        int face1 = getFaceOrientation(desiredCubie.index1);
        int face2 = getFaceOrientation(desiredCubie.index2);
        boolean top;

        if (face1 == 2 || face2 == 2) {
            top = (face1 == 2 && face2 == 1) || (face1 == 1 && face2 == 2);
            while (failsafe < 4 && !top) {
                rotateLayer(1, 0);
                face1 = getFaceOrientation(desiredCubie.index1);
                face2 = getFaceOrientation(desiredCubie.index2);

                top = (face1 == 2 && face2 == 1) || (face1 == 1 && face2 == 2);
                failsafe++;
            }
            if (failsafe >= 4) {
                System.out.println("Error in while loop in move unsolved edge piece to top left edge part one");
            }
            return;
        } else if (face1 == 4 || face2 == 4) {
            top = (face1 == 4 && face2 == 0) || (face1 == 0 && face2 == 4);
            while (failsafe < 4 && !top) {
                rotateLayer(1, dimension - 1);
                face1 = getFaceOrientation(desiredCubie.index1);
                face2 = getFaceOrientation(desiredCubie.index2);
                top = (face1 == 4 && face2 == 0) || (face1 == 0 && face2 == 4);
                failsafe++;
            }
            rotateFrontFace(1, 0);
            rotateFrontFace(1, 0);
            rotateLayer(-1, 0);
            if (failsafe >= 4) {
                System.out.println("Error in while loop in move unsolved edge piece to top left edge part two");
            }
            return;
        } else if (face1 == 1 || face2 == 1) {
            rotateFrontFace(1, 0);
            rotateLayer(-1, 0);
            return;
        } else if (face1 == 3 || face2 == 3) {
            rotateFrontFace(-1, 0);
            rotateLayer(-1, 0);
            return;
        }

    }

    // aligns an edge cubie to fit on the right edge so that the top is not
    // disturbed
    // we will be solving the edge piece based on which edge piece is on top
    // in this method, we only need to worry about dirupting the left edgepiece
    public void alignEdgeCubie(int row) {

        int base = dimension * dimension;

        // always judging color off of top left edge piece
        int colorFront = faces.get(getPosition(5, 1, 0)) / base;
        int colorLeft = faces.get(getPosition(1, dimension - 1, dimension - 2)) / base;

        if (row <= 0 || row >= dimension - 1) {
            return;
        }

        if (faces.get(getPosition(5, 1, 0)) / base == faces.get(getPosition(5, row, 0)) / base
                && faces.get(getPosition(1, dimension - 1, dimension - 2))
                        / base == faces.get(getPosition(1, dimension - 1, dimension - 1 - row)) / base) {
            return;
        }

        EdgeCubie desiredCubie = findAvailableEdgeCubiePositions(colorFront, colorLeft, row);

        int frontFaceCubie;
        int leftFaceCubie;

        // get the singular cubie face that should be in front
        if (desiredCubie.index1 / base == colorFront) {
            frontFaceCubie = desiredCubie.index1;
            leftFaceCubie = desiredCubie.index2;
        } else {
            frontFaceCubie = desiredCubie.index2;
            leftFaceCubie = desiredCubie.index1;
        }

        int faceFrontCubie = getFaceOrientation(frontFaceCubie);
        int faceLeftCubie = getFaceOrientation(leftFaceCubie);

        // if either one of the cubies are on the bottom face
        if (faceFrontCubie == 4 || faceLeftCubie == 4) {
            boolean inPlace;
            if (faceFrontCubie == 4) {
                inPlace = faceLeftCubie == 0;

                while (!inPlace) {
                    rotateLayer(1, dimension - 1);
                    faceLeftCubie = getFaceOrientation(leftFaceCubie);
                    inPlace = faceLeftCubie == 0;
                }

                rotateFrontFace(-1, 0);
                rotateSide(1, dimension - 1);
                rotateSide(1, dimension - 1);

            } else {
                inPlace = faceFrontCubie == 3;
                while (!inPlace) {
                    rotateLayer(1, dimension - 1);
                    faceFrontCubie = getFaceOrientation(frontFaceCubie);
                    inPlace = faceFrontCubie == 3;
                }

                rotateSide(1, dimension - 1);
            }

        }
        // edge piece is located on the top layer
        else if (faceFrontCubie == 2 || faceLeftCubie == 2) {
            boolean inPlace;
            if (faceFrontCubie == 2) {
                inPlace = faceLeftCubie == 0;
                while (!inPlace) {
                    rotateLayer(1, 0);
                    faceLeftCubie = getFaceOrientation(leftFaceCubie);
                    inPlace = faceLeftCubie == 0;
                }

                rotateFrontFace(1, 0);
                rotateSide(1, dimension - 1);
                rotateSide(1, dimension - 1);

            } else {
                inPlace = faceFrontCubie == 3;

                while (!inPlace) {
                    rotateLayer(1, 0);
                    faceFrontCubie = getFaceOrientation(frontFaceCubie);
                    inPlace = faceFrontCubie == 3;
                }

                rotateSide(-1, dimension - 1);
            }

        }
        // edge piece is located in the equator
        else {
            if (faceFrontCubie == 0 || faceLeftCubie == 0) {
                rotateFrontFace(1, 0);
                alignEdgeCubie(row);
                return;
            } else {
                if (faceFrontCubie == 1 || faceLeftCubie == 1) {
                    // make sure there is an unsolved piece at the top left edge piece
                    int tempRow = getRow(leftFaceCubie);
                    moveUnsolvedEdgePiecetoTopLeftEdge();
                    rotateLayer(1, 0);
                    rotateLayer(1, 0);
                    rotateSide(-1, dimension - 1);
                    moveUnsolvedEdgePiecetoTopLeftEdge();
                    rotateLayer(-1, tempRow);
                    rotateSide(1, dimension - 1);
                    rotateLayer(1, 0);
                    rotateLayer(1, 0);
                    rotateSide(-1, dimension - 1);
                    rotateLayer(1, tempRow);
                    alignEdgeCubie(row);

                    return;
                } else if (faceFrontCubie == 3) {
                    return;
                } else {
                    rotateSide(1, dimension - 1);
                    rotateLayer(-1, 0);
                    rotateFrontFace(1, 0);
                    rotateSide(1, dimension - 1);
                    rotateSide(1, dimension - 1);

                }
            }
        }

    }


    //inserts the edge cubie that is on the right front edge to the left front while only disturbing unsolved edges
    public void insertEdgeCubie(int row) {
        int base = dimension * dimension;

        if (faces.get(getPosition(5, 1, 0)) / base == faces.get(getPosition(5, row, 0)) / base
                && faces.get(getPosition(1, dimension - 1, dimension - 2))
                        / base == faces.get(getPosition(1, dimension - 1, dimension - 1 - row)) / base) {
            return;
        }

        rotateLayer(1, row);
        rotateFrontFace(1, dimension - 1);
        rotateLayer(-1, 0);
        rotateFrontFace(-1, dimension - 1);
        rotateLayer(-1, row);
        rotateLayer(1, 0);
        rotateFrontFace(-1, dimension - 1);
    }

    //solves entire left edge for the color on top
    public void solveLeftEdge() {
        moveUnsolvedEdgePieceToLeftFrontEdge();
        cubePieces.get(faces.get(getPosition(5, 1, 0))).inPlace = true;
        cubePieces.get(faces.get(getPosition(1, dimension - 1, dimension - 2))).inPlace = true;

        for (int i = 2; i < dimension - 1; i++) {
            alignEdgeCubie(i);
            moveUnsolvedEdgePiecetoTopLeftEdge();
            insertEdgeCubie(i);
            cubePieces.get(faces.get(getPosition(5, i, 0))).inPlace = true;
            cubePieces.get(faces.get(getPosition(1, dimension - 1, dimension - 1 - i))).inPlace = true;

        }

    }

    //solves first ten edges (no shit sherlock)
    public void solveFirstTenEdges() {
        for (int i = 0; i < 10; i++) {
            solveLeftEdge();
        }
        moveUnsolvedEdgePieceToLeftFrontEdge();
        orientCubeForLastStep();
        rotateCubeUp();
        rotateCubeClockwise();

    }

    //orients so that last two unsolved edge pieces are on top/front and top/back
    public void orientCubeForLastStep() {
        ArrayList<EdgeCubie> cubies = findUnsolvedEdgePieces();

        // unsolved should be found on the left front edge rn, so ignore that part
        int desiredCubie1 = 0;
        int desiredCubie2 = 0;

        for (int i = 0; i < cubies.size(); i++) {
            desiredCubie1 = cubies.get(i).index1;
            desiredCubie2 = cubies.get(i).index2;
            if (!((getFaceOrientation(desiredCubie1) == 5 && getFaceOrientation(desiredCubie2) == 1)
                    || (getFaceOrientation(desiredCubie1) == 1 && getFaceOrientation(desiredCubie2) == 5))) {
                break;
            }
        }

        int face1 = getFaceOrientation(desiredCubie1);
        int face2 = getFaceOrientation(desiredCubie2);

        boolean inPlace;
        if (face1 == 2 || face2 == 2) {
            inPlace = face1 == 3 || face2 == 3;
            while (!inPlace) {
                rotateLayer(1, 0);
                face1 = getFaceOrientation(desiredCubie1);
                face2 = getFaceOrientation(desiredCubie2);
                inPlace = face1 == 3 || face2 == 3;

            }
            rotateSide(-1, dimension - 1);
        } else if (face1 == 4 || face2 == 4) {
            inPlace = face1 == 3 || face2 == 3;
            while (!inPlace) {
                rotateLayer(1, dimension - 1);
                face1 = getFaceOrientation(desiredCubie1);
                face2 = getFaceOrientation(desiredCubie2);
                inPlace = face1 == 3 || face2 == 3;

            }
            rotateSide(1, dimension - 1);

        } else if (face1 == 3 || face2 == 3) {
            inPlace = face1 == 5 || face2 == 5;
            while (!inPlace) {
                rotateSide(1, dimension - 1);
                face1 = getFaceOrientation(desiredCubie1);
                face2 = getFaceOrientation(desiredCubie2);
                inPlace = face1 == 5 || face2 == 5;

            }
        } else {
            rotateFrontFace(1, 0);
            orientCubeForLastStep();
            return;
        }

    }

    // algorithm to swap cubies on top face
    public void swapCubiesTopFace(int i) {
        int index = dimension - 1 - i;
        rotateSide(1, index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(1, index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateFrontFace(1, dimension - 1);
        rotateFrontFace(1, dimension - 1);
        rotateSide(1, index);
        rotateFrontFace(1, dimension - 1);
        rotateFrontFace(1, dimension - 1);
        rotateSide(1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(-1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(1, index);
        rotateSide(1, index);

    }

    // algorithm to flip parity edges (if edge is in wrong orientation compared to center / desired color)
    //note that parity can only affect mirror cubies, so this is done to only half the cube, when I flip col index,
    //col dimension - 1 - index is also changed
    public void flipEdges(int index) {
        rotateSide(1, dimension - 1 - index);
        rotateSide(1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(1, dimension - 1 - index);
        rotateSide(1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(-1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateFrontFace(1, 0);
        rotateFrontFace(1, 0);
        rotateLayer(-1, 0);
        rotateSide(-1, dimension - 1 - index);
        rotateLayer(1, 0);
        rotateFrontFace(1, 0);
        rotateFrontFace(1, 0);
        rotateLayer(-1, 0);
        rotateSide(1, dimension - 1 - index);
        rotateLayer(-1, 0);

    }

    // flips edge (only one edge) on the front / top face
    public void flipFrontTopEdge() {
        rotateLayer(1, 0);
        rotateSide(-1, 0);
        rotateLayer(-1, 0);
        rotateFrontFace(1, dimension - 1);
        rotateLayer(-1, 0);
        rotateFrontFace(-1, dimension - 1);
        rotateLayer(1, 0);
    }

    //puts above algorithms together to solve parity (for now)
    //even cubes have OLL and PLL parity that is solved in cube 3x3, but rn, we can't 
    //tell if OLL or PLL parity exists or not
    public void solveLastTwoEdges() {

        int base = dimension * dimension;
        int colorFront = 0;
        int colorFront_top = 0;
        int colorBack = 0;
        int colorBack_top = 0;
        int colorFrontTemp;
        int colorFront_topTemp;
        int colorBackTemp;
        int colorBack_topTemp;
        // assign colors, these are permanent and will be the desired orientation of the
        // cube in the end
        boolean match;
        if (dimension % 2 == 1) {
            colorFront = faces.get(getPosition(5, 0, dimension / 2)) / base;
            colorFront_top = faces.get(getPosition(2, dimension - 1, dimension / 2)) / base;
            colorBack = faces.get(getPosition(0, dimension - 1, dimension / 2)) / base;
            colorBack_top = faces.get(getPosition(2, 0, dimension / 2)) / base;
        } else {
            colorFront = faces.get(getPosition(5, 0, 1)) / base;
            colorFront_top = faces.get(getPosition(2, dimension - 1, 1)) / base;
            for (int i = 1; i < dimension - 1; i++) {
                colorBack = faces.get(getPosition(0, dimension - 1, i)) / base;
                colorBack_top = faces.get(getPosition(2, 0, i)) / base;
                // do they match?
                match = (colorFront == colorBack && colorFront_top == colorBack_top)
                        || (colorFront == colorBack_top && colorFront_top == colorBack);
                if (!match) {
                    break;
                }
            }
        }
        // firstly, we need to get all the edge cubies on the right side, then we'll
        // deal with parity
        for (int i = 1; i < dimension - 1; i++) {
            // check if the color is in the right spot
            colorFrontTemp = faces.get(getPosition(5, 0, i)) / base;
            colorFront_topTemp = faces.get(getPosition(2, dimension - 1, i)) / base;
            colorBackTemp = faces.get(getPosition(0, dimension - 1, i)) / base;
            colorBack_topTemp = faces.get(getPosition(2, 0, i)) / base;
            match = (colorFrontTemp == colorFront && colorFront_topTemp == colorFront_top)
                    || (colorFrontTemp == colorFront_top && colorFront_topTemp == colorFront);
            // if piece doesn't belong there, we need to swap it for a piece on the other
            // side
            if (!match) {
                match = (colorBackTemp == colorBack && colorBack_topTemp == colorBack_top)
                        || (colorBackTemp == colorBack_top && colorBack_topTemp == colorBack);
                if (match) {
                    rotateCubeDown();
                    flipFrontTopEdge();
                    rotateCubeUp();
                }
                swapCubiesTopFace(i);

            }
        }

        colorFront = faces.get(getPosition(5, 0, dimension / 2)) / base;
        colorBack_top = faces.get(getPosition(2, 0, dimension / 2)) / base;

        for (int i = 1; i < dimension / 2; i++) {
            colorFrontTemp = faces.get(getPosition(5, 0, i)) / base;
            if (colorFrontTemp != colorFront) {
                flipEdges(i);
            }
        }
        rotateCubeDown();
        for (int i = 1; i < dimension / 2; i++) {
            colorBack_topTemp = faces.get(getPosition(5, 0, i)) / base;
            if (colorBack_topTemp != colorBack_top) {
                flipEdges(i);
            }
        }

    }


    // due to parity rules, there is only one edge cubie that will fit in a desired
    // row
    public boolean edgeCubieChecker(EdgeCubie cubie, int row, int colorBack, int colorFront) {
        // colors match
        int base = dimension * dimension;
        boolean colorsMatch = (colorBack == cubie.index1 / base && colorFront == cubie.index2 / base)
                || (colorBack == cubie.index2 / base && colorFront == cubie.index1 / base);
        if (!colorsMatch) {
            return false;
        }

        int colorFrontFace;

        if (colorFront == cubie.index1 / base) {
            colorFrontFace = cubie.index1;
        } else {
            colorFrontFace = cubie.index2;
        }

        int desiredCubieRow = getRow(colorFrontFace);
        int desiredCubieCol = getCol(colorFrontFace);

        boolean quad1 = desiredCubieCol == 0 && desiredCubieRow == row;
        boolean quad2 = desiredCubieCol == dimension - 1 - row && desiredCubieRow == 0;

        boolean quad3 = desiredCubieCol == row && dimension - 1 == desiredCubieRow;

        boolean quad4 = desiredCubieCol == dimension - 1 && desiredCubieRow == dimension - 1 - row;
        return quad1 || quad2 || quad3 || quad4;

    }

    public EdgeCubie findAvailableEdgeCubiePositions(int colorFront, int colorBack, int row) {

        ArrayList<EdgeCubie> edgeCubies = getEdgeCubies();

        for (EdgeCubie cubie : edgeCubies) {
            if (edgeCubieChecker(cubie, row, colorBack, colorFront)) {
                return cubie;
            }
        }

        return null;
    }

    //HELPER METHODS FOR ABOVE
    
    // there always needs to be two open edges, one that we can use to replace and
    // one that we can use as the solving edge piece
    // takes the color on the top of the left edge as the left edge cubie



    /**
     * Final methods, solving the cube and overal checkers
     */

    public void reduceCubeto3x3() {
        if (reduced) {
            return;
        }
        algorithm.clear();
        solveCenters();
        solveFirstTenEdges();
        solveLastTwoEdges();

        // will take this out
        boolean cubeGood = edgeMatches();
        // has never happened yet, where edges don't solve, but there are a fuckload of
        // combos for a cube
        // I might have missed something (I've tested it 1000 times on repeat in debug
        // and it hasn't not solved, so if this is called, it is rediculously rare)
        if (!cubeGood) {
            scrambleNxN(10);
            //also clears the algorithm
            reduceCubeto3x3();
            return;
        }
        reduced = true;
    }

    //checks if centers are solved
    public boolean checkCenters() {
        int base = dimension * dimension;
        for (int x = 0; x < 6; x++) {
            int color = faces.get(getPosition(x, 1, 1)) / base;
            for (int i = 1; i < dimension - 1; i++) {
                for (int j = 1; j < dimension - 2; j++) {
                    if (faces.get(getPosition(x, i, j)) / base != color) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //checks if edges are solved
    public boolean edgeMatches() {
        boolean value = true;
        int base = dimension * dimension;
        for (int j = 0; j < 6; j++) {
            for (int i = 1; i < dimension - 2; i++) {
                value = faces.get(getPosition(j, 0, i)) / base == faces.get(getPosition(j, 0, i + 1)) / base
                        && faces.get(getPosition(j, dimension - 1, i))
                                / base == faces.get(getPosition(j, dimension - 1, i + 1)) / base
                        && faces.get(getPosition(j, i, dimension - 1))
                                / base == faces.get(getPosition(j, i + 1, dimension - 1)) / base
                        && faces.get(getPosition(j, i, 0)) / base == faces.get(getPosition(j, i + 1, 0)) / base;
                if (!value) {
                    return value;
                }
            }
        }

        return value;
    }

    public void scrambleNxN(int moves) {
        reduced = false;
        String[] possibleMoves = { "X+", "Y+", "Z+", "X-", "Y-", "Z-" };
        int randomLayer;
        int randomChooser;
        for (int i = 0; i < moves; i++) {
            randomLayer = (int) (Math.random() * dimension);
            randomChooser = (int) (Math.random() * 6);
            Move move = new Move(possibleMoves[randomChooser], randomLayer);
            moveSequenceNxN(move);
            scrambleAlgorithm.add(move);
        }
        cubePieces = new ArrayList<>();
        for (int i = 0; i < dimension * dimension * 6; i++) {
            cubePieces.add(new CubePiece(i, false));
        }
    }



    //finds unsolved edge pieces (duh)
    public ArrayList<EdgeCubie> findUnsolvedEdgePieces() {
        ArrayList<EdgeCubie> result = new ArrayList<>();
        for (EdgeCubie cubie : getEdgeCubies()) {
            if (!cubePieces.get(cubie.index1).inPlace) {
                result.add(new EdgeCubie(cubie.index1, cubie.index2));
            }
        }

        return result;

    }

    private static void testnxn(int dimension){
        // shows the cube being sovled while timing scramble and solve
        Cubenxn myCube = new Cubenxn(dimension);
        // print the origional cube
        System.out.println("Cube OG at dimension: " + dimension);
        myCube.printCube();

        // Timing the scramble
        long startTime = System.nanoTime();

        // scramble the cube 1000000 times (for large nxn cubes, you could remove Y and
        // X from scrambleCube)
        myCube.scrambleNxN(1000);

        long endTime = System.nanoTime();

        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;

        System.out.println("Scrambled cube at 1000 moves");
        // print the scrambled cube
        myCube.printCube();

        System.out.println("Execution time of scramble in nanoseconds  : " + timeElapsed);

        System.out.println("Execution time of scramble in milliseconds : " + timeElapsed / 1000000);

        // Timing the scramble
        startTime = System.nanoTime();

        // solve the scrambled cube
        // Check the sequence of steps in the method solve
        myCube.reduceCubeto3x3();

        endTime = System.nanoTime();

        // get difference of two nanoTime values
        timeElapsed = endTime - startTime;
        System.out.println("reduced cube");
        myCube.printCube();

        System.out.println("Execution time of reduction in nanoseconds  : " + timeElapsed);

        System.out.println("Execution time of reduction in milliseconds : " + timeElapsed / 1000000);

    }

    public static void main(String[] args) {
        testnxn(11);
    }

}