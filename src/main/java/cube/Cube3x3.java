package cube;

import java.util.*;

public class Cube3x3 extends Cube {
    // Does not solve 2x2

    public Cube3x3(int dimension) {
        super(dimension);
    }

    public Cube3x3(Cubenxn cube) {
        super(cube);

    }

    //scrambles cube like a 3x3 (only moves outside edges)
    private void scrambleCube3x3(int moves) {
        ArrayList<String> validMoves = new ArrayList<>();
        validMoves.add("R");
        validMoves.add("R'");
        validMoves.add("L");
        validMoves.add("L'");
        validMoves.add("U");
        validMoves.add("U'");
        validMoves.add("D");
        validMoves.add("D'");
        validMoves.add("F");
        validMoves.add("F'");
        validMoves.add("B");
        validMoves.add("B'");
        validMoves.add("X");
        validMoves.add("X'");
        validMoves.add("Y");
        validMoves.add("Y'");
        ArrayList<String> scrambleAlgorithm = new ArrayList<>();
        int randomInt;

        for (int i = 0; i < moves; i++) {
            randomInt = (int) (Math.random() * 12);
            scrambleAlgorithm.add(validMoves.get(randomInt));
            moveSequence3x3(scrambleAlgorithm.get(scrambleAlgorithm.size() - 1));

        }
    }

    /************************************************************************************
     * SOLVING METHODS
     * **********************************************************************************
     */

    /************************************************************************************
     * FIRST LAYER METHODS
     * **********************************************************************************
     */



    // Moves desired the edge piece that cooresponds to whatever color is on back
    // and top to right place without disrupting anything important
    private void solveTopBackEdgePiece() {
        int base = dimension * dimension;
        int topFaceColor = faces.get(getPosition(2, 1, 1)) / base;
        // Find what colors are surrounding
        // Assuming center is already solved
        int backFaceColor = faces.get(getPosition(0, 1, 1)) / base;

        ArrayList<EdgeCubie> edgeCubie = getEdgeCubie(topFaceColor, backFaceColor);

        // The magnitude of the certain edge cubie we are looking for
        int colorTopEdgeCubie;

        // a cubie has two indexes, test which one cooresponds to the color on the top
        // of the cube
        if (edgeCubie.get(0).index1 / base == topFaceColor) {
            colorTopEdgeCubie = edgeCubie.get(0).index1;
        } else {
            colorTopEdgeCubie = edgeCubie.get(0).index2;
        }
        int cubieBackTopFace = getFaceOrientation(colorTopEdgeCubie);
        // cubie is located on the back face
        // -------------------------------------------------------------
        if (cubieBackTopFace == 0) {
            // cubie is on bottom
            if (faces.indexOf(colorTopEdgeCubie) < dimension) {
                rotateFrontFace(1, 0);
                rotateLayer(-1, 0);
                rotateSide(-1, 0);
                rotateLayer(1, 0);
            }
            // if cubie is on the "left" edge
            else if (faces.indexOf(colorTopEdgeCubie) % dimension == 0) {
                rotateLayer(-1, 0);
                rotateSide(-1, 0);
                rotateLayer(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(colorTopEdgeCubie) + 1) % dimension == 0) {
                rotateFrontFace(1, 0);
                rotateFrontFace(1, 0);
                rotateLayer(-1, 0);
                rotateSide(-1, 0);
                rotateLayer(1, 0);
            } else {
                rotateFrontFace(-1, 0);
                rotateLayer(-1, 0);
                rotateSide(-1, 0);
                rotateLayer(1, 0);

            }
            // else, cubie is bordering top and middle
            // printCubeNums();

        }
        // Cubie is on left face
        // -------------------------------------------------------------------------
        else if (cubieBackTopFace == 1) {
            if (faces.indexOf(colorTopEdgeCubie) < base + dimension) {
                rotateFrontFace(1, 0);
            }
            // if cubie is on the "left" edge
            else if (faces.indexOf(colorTopEdgeCubie) % dimension == 0) {
                rotateSide(-1, 0);
                rotateFrontFace(1, 0);
                rotateSide(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(colorTopEdgeCubie) + 1) % dimension == 0) {
                rotateSide(1, 0);
                rotateFrontFace(1, 0);
            } else {
                rotateSide(1, 0);
                rotateSide(1, 0);
                rotateFrontFace(1, 0);
                rotateSide(1, 0);
                rotateSide(1, 0);

            }
            // else, cubie is bordering top and middle
            // printCubeNums();
        }
        // Cubie is on front face
        // --------------------------------------------------------------------------
        else if (cubieBackTopFace == 5) {
            if (faces.indexOf(colorTopEdgeCubie) < 5 * base + dimension) {
                rotateLayer(1, 0);
                rotateLayer(1, 0);
                rotateFrontFace(1, 0);
                rotateLayer(-1, 0);
                rotateSide(-1, dimension - 1);
                rotateLayer(-1, 0);

            }
            // if cubie is on the "left" edge
            else if (faces.indexOf(colorTopEdgeCubie) % dimension == 0) {
                rotateLayer(-1, 0);
                rotateSide(1, 0);
                rotateLayer(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(colorTopEdgeCubie) + 1) % dimension == 0) {
                rotateLayer(1, 0);
                rotateSide(1, dimension - 1);
                rotateLayer(-1, 0);
            } else {
                rotateFrontFace(-1, dimension - 1);
                rotateLayer(1, 0);
                rotateSide(1, dimension - 1);
                rotateLayer(-1, 0);
                rotateFrontFace(1, dimension - 1);

            }
            // printCubeNums();
        }
        // Cubie is on right face
        // --------------------------------------------------------------------------
        else if (cubieBackTopFace == 3) {
            if (faces.indexOf(colorTopEdgeCubie) < 3 * base + dimension) {
                rotateFrontFace(-1, 0);
            }
            // if cubie is on the "right" edge
            else if ((faces.indexOf(colorTopEdgeCubie) + 1) % dimension == 0) {
                rotateSide(-1, dimension - 1);
                rotateFrontFace(-1, 0);
                rotateSide(1, dimension - 1);
            }
            // if cubie is on the "left edge"
            else if (faces.indexOf(colorTopEdgeCubie) % dimension == 0) {
                rotateSide(1, dimension - 1);
                rotateFrontFace(-1, 0);
            } else {
                rotateLayer(-1, 0);
                rotateLayer(-1, 0);
                rotateFrontFace(-1, dimension - 1);
                rotateLayer(-1, 0);
                rotateLayer(-1, 0);
            }

            // printCubeNums();
        }
        // Cubie is on the desired face (top face)
        // ---------------------------------------------------------
        else if (cubieBackTopFace == 2) {
            // if cubie is on the "left" edge
            if (faces.indexOf(colorTopEdgeCubie) % dimension == 0) {
                rotateSide(1, 0);
                rotateLayer(-1, 0);
                rotateSide(-1, 0);
                rotateLayer(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(colorTopEdgeCubie) + 1) % dimension == 0) {
                rotateSide(1, dimension - 1);
                rotateLayer(1, 0);
                rotateSide(-1, dimension - 1);
                rotateLayer(-1, 0);
            } else if (faces.indexOf(colorTopEdgeCubie) < 3 * base
                    && faces.indexOf(colorTopEdgeCubie) > base * 2 + dimension * (dimension - 1)) {
                rotateLayer(-1, 0);
                rotateLayer(-1, 0);
                rotateFrontFace(1, 0);
                rotateLayer(1, 0);
                rotateLayer(1, 0);
                rotateFrontFace(-1, 0);
            }
            // printCubeNums();
        }
        // cubie is on bottom
        // ------------------------------------------------------------------------------
        else if (cubieBackTopFace == 4) {
            if (faces.indexOf(colorTopEdgeCubie) < 4 * base + dimension) {
                rotateFrontFace(1, 0);
                rotateFrontFace(1, 0);
            }
            // if cubie is on the "left" edge
            else if ((faces.indexOf(colorTopEdgeCubie) + 1) % dimension == 0) {
                rotateLayer(1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateFrontFace(1, 0);
            }
            // if cubie is on the "right edge"
            else if (faces.indexOf(colorTopEdgeCubie) % dimension == 0) {
                rotateLayer(-1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateFrontFace(1, 0);
            } else {
                rotateLayer(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateFrontFace(1, 0);
            }
            // printCubeNums();
        }
    }

    // does solvetopbackedgepiece four times all while rotating cube everytime
    private void solveCross() {
        for (int i = 0; i < 4; i++) {
            solveTopBackEdgePiece();
            rotateCubeClockwise();
        }
    }

    // CORNERS

    // moves any corner piece to index 0,0,0 or 0,dim-1,0 then does the following
    // two methods to put it in place
    private void solveTopBackRightCornerPiece() {
        int base = dimension * dimension;
        int topFaceColor = faces.get(getPosition(2, 1, 1)) / base;
        // Find what colors are surrounding
        // Assuming center is already solved
        int backFaceColor = faces.get(getPosition(0, 1, 1)) / base;
        int rightFaceColor = faces.get(getPosition(3, 1, 1)) / base;

        CornerCubie cornerCubie = getCornerCubie(topFaceColor, backFaceColor, rightFaceColor);

        // The magnitude of the certain edge cubie we are looking for
        int colorTopCornerCubie;

        // a cubie has two indexes, test which one cooresponds to the color on the top
        // of the cube
        if (cornerCubie.index1 / base == topFaceColor) {
            colorTopCornerCubie = cornerCubie.index1;
        } else if (cornerCubie.index2 / base == topFaceColor) {
            colorTopCornerCubie = cornerCubie.index2;
        } else {
            colorTopCornerCubie = cornerCubie.index3;
        }

        int cubieBackTopFace = getFaceOrientation(colorTopCornerCubie);
        // cubie is located on the back face
        // -------------------------------------------------------------
        if (cubieBackTopFace == 0) {
            // Top left corner
            if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                backBottomLeftCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (dimension - 1)) {
                backBottomRightCornertoTopBackRightCorner();
            }
            // bottom right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                // get piece to bottom left corner of back
                rotateFrontFace(1, 0);
                rotateLayer(1, dimension - 1);
                rotateFrontFace(-1, 0);
                rotateLayer(-1, dimension - 1);

                // Now it is on the bottom right corner, but it is backwards, move it to left
                // corner

                // move piece to place
                backBottomRightCornertoTopBackRightCorner();
            }

            // bottom left corner
            else {

                // get piece to bottom right corner
                rotateFrontFace(-1, 0);
                rotateLayer(-1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateLayer(1, dimension - 1);

                // get piece to place
                backBottomLeftCornertoTopBackRightCorner();
            }
            // else, cubie is bordering top and middle
            // printCubeNums();

        }
        // Cubie is on left face
        // -------------------------------------------------------------------------
        else if (cubieBackTopFace == 1) {
            // Top left corner
            if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                rotateLayer(1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (dimension - 1)) {
                rotateSide(1, 0);
                rotateLayer(1, dimension - 1);
                rotateSide(-1, 0);
                backBottomRightCornertoTopBackRightCorner();
            }
            // bottom right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                rotateSide(-1, 0);
                rotateLayer(-1, dimension - 1);
                rotateSide(1, 0);
                rotateLayer(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();

            }

            // bottom left corner
            else {
                rotateLayer(1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();

            }
            // else, cubie is bordering top and middle
            // printCubeNums();

        }
        // Cubie is on right face
        // --------------------------------------------------------------------------
        else if (cubieBackTopFace == 3) {
            if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                rotateSide(1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                rotateSide(-1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();
            }
            // if cubie is on the "right" edge
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (dimension - 1)) {
                rotateLayer(-1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();
            }
            // if cubie is on the "left edge"
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();
            } else {
                rotateSide(-1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateSide(1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();

            }

            // printCubeNums();
        }
        // Cubie is on the desired face (top face)
        // ---------------------------------------------------------
        else if (cubieBackTopFace == 2) {
            if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                rotateSide(1, 0);
                rotateLayer(1, dimension - 1);
                rotateSide(-1, 0);
                rotateLayer(-1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();
            }

            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                rotateSide(-1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateSide(1, dimension - 1);
                rotateLayer(1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();
            }

            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * (base)
                    + ((dimension) * (dimension - 1))) {
                rotateSide(-1, 0);
                rotateLayer(-1, dimension - 1);
                rotateSide(1, 0);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();

            }

            // printCubeNums();
        } else if (cubieBackTopFace == 5) {
            // Top left corner
            if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {

                rotateFrontFace(-1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateFrontFace(1, dimension - 1);
                rotateLayer(1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (dimension - 1)) {
                rotateFrontFace(1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                rotateFrontFace(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();

            }
            // bottom right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                rotateLayer(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                backBottomLeftCornertoTopBackRightCorner();
            }

            // bottom left corner
            else {
                rotateLayer(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();
            }
            // else, cubie is bordering top and middle
            // printCubeNums();

        } else if (cubieBackTopFace == 4) {
            // Top left corner
            if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                rotateFrontFace(1, 0);
                rotateLayer(1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateFrontFace(-1, 0);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (dimension - 1)) {
                rotateLayer(1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateLayer(1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateFrontFace(-1, 0);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();

            }
            // bottom right corner
            else if (faces.indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                rotateLayer(-1, dimension - 1);
                rotateLayer(-1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateLayer(1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateFrontFace(-1, 0);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();
            }

            // bottom left corner
            else {

                rotateLayer(-1, dimension - 1);
                rotateFrontFace(1, 0);
                rotateLayer(1, dimension - 1);
                rotateLayer(1, dimension - 1);
                rotateFrontFace(-1, 0);
                rotateLayer(-1, dimension - 1);
                backBottomRightCornertoTopBackRightCorner();

            }
            // else, cubie is bordering top and middle
            // printCubeNums();

        }
    }

    // FOLLOWING TWO ARE USED A LOT IN THE SOLVE CORNER METHOD
    // if you're looking at the cube foldout, moves
    // index 0,0,0 to 2,0,dim-1)
    private void backBottomLeftCornertoTopBackRightCorner() {
        rotateLayer(1, dimension - 1);
        rotateLayer(1, dimension - 1);
        rotateFrontFace(1, 0);
        rotateLayer(-1, dimension - 1);
        rotateFrontFace(-1, 0);

    }

    // if you're looking at the cube foldout, moves
    // index 0,dim-1,0 to 2,0,dim-1)
    private void backBottomRightCornertoTopBackRightCorner() {
        rotateLayer(-1, dimension - 1);
        rotateSide(1, dimension - 1);
        rotateLayer(1, dimension - 1);
        rotateSide(-2, dimension - 1);

    }

    // Repeates solvetopbackrightcornerpiece four times
    private void solveCorners() {
        for (int i = 0; i < 4; i++) {
            solveTopBackRightCornerPiece();
            rotateCubeClockwise();

        }
    }

    /************************************************************************************
     * SECOND LAYER METHODS
     * **********************************************************************************
     */

    // moves top/front edge piece to the right/left edge spot THIS CAN DO
    // EVERYTHING, YOU DON'T NEED MOVE TOP FRONT TO LEFT I'VE INCLUDED IT ANYWAYS
    private void moveTopFrontEdgePiecetoRight() {
        int base = dimension * dimension;
        int colorRight = faces.get(getPosition(3, 1, 1)) / base;
        int colorFront = faces.get(getPosition(5, 1, 1)) / base;
        ArrayList<EdgeCubie> cubie = getEdgeCubie(colorRight, colorFront);
        int faceFrontCubie;
        int faceFrontCubieNo;
        if (cubie.get(0).index1 / base == colorFront) {
            faceFrontCubie = getFaceOrientation(cubie.get(0).index1);
            faceFrontCubieNo = cubie.get(0).index1;
        } else {
            faceFrontCubie = getFaceOrientation(cubie.get(0).index2);
            faceFrontCubieNo = cubie.get(0).index2;
        }
        // Move it to its place

        // on back face somewhere
        if (faceFrontCubie == 0) {
            // if cubie is on the "left" edge
            if (faces.indexOf(faceFrontCubieNo) % dimension == 0) {
                // Get the edge piece out of there
                rotateCubeCounterClockwise();
                rotateCubeCounterClockwise();
                rightEdgePieceLayerTwo();

                // return to origional orientation
                rotateCubeClockwise();
                rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                moveTopFrontEdgePiecetoRight();
            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(faceFrontCubieNo) + 1) % dimension == 0) {
                // Get the edge piece out of there
                rotateCubeCounterClockwise();
                rotateCubeCounterClockwise();
                leftEdgePieceLayerTwo();

                // return to origional orientation
                rotateCubeClockwise();
                rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                moveTopFrontEdgePiecetoRight();
            } else {
                rotateLayer(1, 0);
                rotateLayer(1, 0);
                rightEdgePieceLayerTwo();
                // ENDS METHOD
            }
            // else, cubie is bordering top and middle
            // printCubeNums();
        }

        else if (faceFrontCubie == 1) {
            // if cubie is on the "left" edge
            if (faces.indexOf(faceFrontCubieNo) < 1 * base + dimension) {
                // Get the edge piece out of there
                rotateCubeCounterClockwise();
                leftEdgePieceLayerTwo();

                // return to origional orientation
                rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                moveTopFrontEdgePiecetoRight();

            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(faceFrontCubieNo) + 1) % dimension == 0) {
                rotateLayer(-1, 0);
                rightEdgePieceLayerTwo();
                // ENDS METHOD

            } else {
                // Get the edge piece out of there
                rotateCubeCounterClockwise();
                rightEdgePieceLayerTwo();

                // return to origional orientation
                rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                moveTopFrontEdgePiecetoRight();
            }
            // else, cubie is bordering top and middle
            // printCubeNums();
        }

        else if (faceFrontCubie == 2) {
            // if cubie is on the "left" edge
            if (faces.indexOf(faceFrontCubieNo) < 2 * base + dimension) {

                // Only exception is when face is on top, then we have to use the right
                // algorithm
                rotateLayer(1, 0);
                rotateCubeClockwise();
                leftEdgePieceLayerTwo();
                rotateCubeCounterClockwise();
            }
            // if cubie is on the "right edge"
            else if ((faces.indexOf(faceFrontCubieNo) + 1) % dimension == 0) {
                rotateCubeClockwise();
                leftEdgePieceLayerTwo();
                rotateCubeCounterClockwise();
            } else if (faces.indexOf(faceFrontCubieNo) % dimension == 0) {
                rotateLayer(-1, 0);
                rotateLayer(-1, 0);
                rotateCubeClockwise();
                leftEdgePieceLayerTwo();
                rotateCubeCounterClockwise();
            }

            else {
                rotateLayer(-1, 0);
                rotateCubeClockwise();
                leftEdgePieceLayerTwo();
                rotateCubeCounterClockwise();
            }
        }

        else if (faceFrontCubie == 3) {
            // if cubie is on the "left" edge
            if (faces.indexOf(faceFrontCubieNo) < 3 * base + dimension) {
                // Get the edge piece out of there
                rotateCubeClockwise();
                rightEdgePieceLayerTwo();

                // return to origional orientation
                rotateCubeCounterClockwise();

                // Do this method (now that it's out of its edge spot)
                moveTopFrontEdgePiecetoRight();
            }
            // if cubie is on the "right edge"
            else if (faces.indexOf(faceFrontCubieNo) % dimension == 0) {
                rotateLayer(1, 0);
                rightEdgePieceLayerTwo();
                // ENDS METHOD

            } else {
                // Get the edge piece out of there
                rotateCubeClockwise();
                leftEdgePieceLayerTwo();

                // return to origional orientation
                rotateCubeCounterClockwise();

                // Do this method (now that it's out of its edge spot)
                moveTopFrontEdgePiecetoRight();
            }
            // else, cubie is bordering top and middle
            // printCubeNums();
        }

        else if (faceFrontCubie == 5) {
            // if cubie is on the "left" edge
            if (faces.indexOf(faceFrontCubieNo) < 5 * base + dimension) {
                rightEdgePieceLayerTwo();
                // ENDS METHOD
            }
            // if cubie is on the "right edge"
            else if (faces.indexOf(faceFrontCubieNo) % dimension == 0) {
                leftEdgePieceLayerTwo();
                moveTopFrontEdgePiecetoRight();
            }
        }

    }

    // does movetopfront... four times while rotating cube
    private void solveSecondLayer() {
        for (int i = 0; i < 4; i++) {
            moveTopFrontEdgePiecetoRight();
            rotateCubeClockwise();
        }
    }

    // Algorithm for top front to right front edge
    private void rightEdgePieceLayerTwo() {
        // U R U' R' U' F' U F
        rotateLayer(1, 0);
        rotateSide(1, dimension - 1);
        rotateLayer(-1, 0);
        rotateSide(-1, dimension - 1);
        rotateLayer(-1, 0);
        rotateFrontFace(-1, dimension - 1);
        rotateLayer(1, 0);
        rotateFrontFace(1, dimension - 1);
    }

    // Algorithm for top front to left front edge
    private void leftEdgePieceLayerTwo() {
        // U' L' U L U F U' F'
        rotateLayer(-1, 0);
        rotateSide(1, 0);
        rotateLayer(1, 0);
        rotateSide(-1, 0);
        rotateLayer(1, 0);
        rotateFrontFace(1, dimension - 1);
        rotateLayer(-1, 0);
        rotateFrontFace(-1, dimension - 1);
    }

    /************************************************************************************
     * THIRD LAYER METHODS
     * **********************************************************************************
     */

    // Step one
    // -----------------------------------------------------

    // Solves the cross on top (doesn't alighn edge pieces to right place though)
    private void solveThirdLayerCross() {
        // how many top level squares are here, if theres one, do
        // algorithmthirdlayercross in any orientation and then do this method again
        int base = dimension * dimension;

        // 10 01 dim - 1 dim - 2 dim - 2 dim -1
        int colorTopFace = faces.get(getPosition(2, 1, 1)) / base;
        int colorLeftEdge = faces.get(getPosition(2, 1, 0)) / base;
        int colorRightEdge = faces.get(getPosition(2, 1, dimension - 1)) / base;
        int colorBottomEdge = faces.get(getPosition(2, dimension - 1, 1)) / base;
        int colorTopEdge = faces.get(getPosition(2, 0, 1)) / base;

        int noTopLayerFaces = ((colorTopFace == colorLeftEdge) ? 1 : 0) + ((colorTopFace == colorRightEdge) ? 1 : 0)
                + ((colorTopFace == colorBottomEdge) ? 1 : 0) + ((colorTopFace == colorTopEdge) ? 1 : 0);

        /*
         * if(dimension % 2 == 0 && noTopLayerFaces == 1){ parityOLL(); }
         */
        /*
        printCube();
        if (dimension % 2 == 0 && noTopLayerFaces == 1) {
            alogirthmThirdLayerCross();
            printCube();
            boolean inPlace = colorBottomEdge == colorTopFace;
            while (!inPlace) {
                rotateCubeClockwise();
                colorBottomEdge = faces.get(getPosition(2, dimension - 1, 1)) / base;
                inPlace = colorBottomEdge == colorTopFace;
            }
            parityPLL();
            printCube();

        }
        */
        if(noTopLayerFaces == 0){
            alogirthmThirdLayerCross();
            solveThirdLayerCross();
            return;
        }
        /*
        if (noTopLayerFaces == 1) {

            rotateCubeClockwise();
            printCube();
            alogirthmThirdLayerCross();
            printCube();
            return;
        }*/ else if (noTopLayerFaces == 4) {
            return;
        }

        // For both l orientation and line, the bottom is not the same color while the
        // left piece is
        boolean correctOrientation = (colorLeftEdge == colorTopFace) && (colorBottomEdge != colorTopFace);
        while (!correctOrientation) {
            rotateCubeClockwise();
            colorLeftEdge = faces.get(getPosition(2, 1, 0)) / base;
            colorRightEdge = faces.get(getPosition(2, 1, dimension - 1)) / base;
            colorBottomEdge = faces.get(getPosition(2, dimension - 1, 1)) / base;
            colorTopEdge = faces.get(getPosition(2, 0, 1)) / base;
            correctOrientation = (colorLeftEdge == colorTopFace) && (colorBottomEdge != colorTopFace);
        }

        int noRepetitions = 0;
        colorTopEdge = faces.get(getPosition(2, 0, 1)) / base;

        if (colorLeftEdge == colorRightEdge) {
            if (colorTopEdge == colorTopFace && dimension % 2 == 0) {
                parityOLL();
                return;
            } else {
                noRepetitions = 1;
            }
        } else {
            noRepetitions = 2;
        }

        for (int i = 0; i < noRepetitions; i++) {
            alogirthmThirdLayerCross();
        }
    }

    // algorithm for the above
    private void alogirthmThirdLayerCross() {
        rotateFrontFace(1, dimension - 1);
        rotateSide(1, dimension - 1);
        rotateLayer(1, 0);
        rotateSide(-1, dimension - 1);
        rotateLayer(-1, 0);
        rotateFrontFace(-1, dimension - 1);

    }

    //Parity algorithms
    /**
     *For even cubes, there's no way of knowing whether an edge piece is fully solved (unlike odd dimension cubes)
     *sometimes (50% chance) we will unintentionally flip an edge piece when trying to solve it
     *for that we need an algorithm that will flip the edge piece again which is actually very long
     */
    public void parityOLL() {
        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(-1, dimension - 1 - i);
        }

        rotateLayer(1, 0);
        rotateLayer(1, 0);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(-1, i);
        }

        rotateFrontFace(1, dimension - 1);
        rotateFrontFace(1, dimension - 1);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(1, i);
        }

        rotateFrontFace(1, dimension - 1);
        rotateFrontFace(1, dimension - 1);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(-1, dimension - 1 - i);
            rotateSide(-1, dimension - 1 - i);
        }

        rotateLayer(1, 0);
        rotateLayer(1, 0);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(1, dimension - 1 - i);
        }

        rotateLayer(1, 0);
        rotateLayer(1, 0);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(-1, dimension - 1 - i);
        }

        rotateLayer(1, 0);
        rotateLayer(1, 0);

        rotateFrontFace(1, dimension - 1);
        rotateFrontFace(1, dimension - 1);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(1, dimension - 1 - i);
            rotateSide(1, dimension - 1 - i);

        }

        rotateFrontFace(1, dimension - 1);
        rotateFrontFace(1, dimension - 1);

    }

    public void parityPLL() {
        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(1, dimension - 1 - i);
            rotateSide(1, dimension - 1 - i);
        }

        rotateLayer(1, 0);
        rotateLayer(1, 0);

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(1, dimension - 1 - i);
            rotateSide(1, dimension - 1 - i);
        }

        for (int i = 0; i < dimension / 2; i++) {
            rotateLayer(1, i);
            rotateLayer(1, i);
        }

        for (int i = 1; i < dimension / 2; i++) {
            rotateSide(1, dimension - 1 - i);
            rotateSide(1, dimension - 1 - i);
        }

        for (int i = 0; i < dimension / 2; i++) {
            rotateLayer(1, i);
            rotateLayer(1, i);
        }

    }

    public void solveEvenViaOLL(){
        if(dimension % 2 == 1){
            return;
        }
        int base = dimension * dimension;
        int colorTop = faces.get(getPosition(2, 1, 1)) / base;
        int currentColor;
        for(int i = 0; i < 4; i++){
            currentColor = faces.get(getPosition(2, dimension - 1, 1)) / base;
            if(currentColor != colorTop){
                parityOLL();
            }
            rotateCubeClockwise();
        }
    }

    // Aligns the edge pieces
    private void alignThirdLayerCross() {
        solveEvenViaOLL();
        // align so that front equals top
        // three cases:
        // all match: return
        // cross match: do algorithm, do this again return
        // touching faces match turn cube until front and top dont match do algorithm do
        // this again return
        // no match do the algorithm do this again return
        int base = dimension * dimension;
        boolean leftMatch = (faces.get(getPosition(1, 1, 1))
                / base) == (faces.get(getPosition(1, 1, dimension - 1)) / base);
        boolean rightMatch = (faces.get(getPosition(3, 1, 1)) / base) == (faces.get(getPosition(3, 1, 0))
                / base);
        boolean frontMatch = (faces.get(getPosition(5, 1, 1)) / base) == (faces.get(getPosition(5, 0, 1))
                / base);
        boolean backMatch = (faces.get(getPosition(0, 1, 1))
                / base) == (faces.get(getPosition(0, dimension - 1, 1)) / base);
        int noMatching = ((leftMatch ? 1 : 0) + (rightMatch ? 1 : 0) + (frontMatch ? 1 : 0) + (backMatch ? 1 : 0));
        int failSafe = 0;

        while (failSafe < 4 && noMatching < 2) {
            rotateLayer(1, 0);
            leftMatch = (faces.get(getPosition(1, 1, 1))
                    / base) == (faces.get(getPosition(1, 1, dimension - 1)) / base);
            rightMatch = (faces.get(getPosition(3, 1, 1)) / base) == (faces.get(getPosition(3, 1, 0)) / base);
            frontMatch = (faces.get(getPosition(5, 1, 1)) / base) == (faces.get(getPosition(5, 0, 1)) / base);
            backMatch = (faces.get(getPosition(0, 1, 1))
                    / base) == (faces.get(getPosition(0, dimension - 1, 1)) / base);
            noMatching = ((leftMatch ? 1 : 0) + (rightMatch ? 1 : 0) + (frontMatch ? 1 : 0) + (backMatch ? 1 : 0));

            failSafe++;
        }

        if (noMatching == 4) {
            return;
        } else if (frontMatch && backMatch || noMatching < 2 || (rightMatch && leftMatch)) {
            /*
            printCube();
            if (dimension % 2 == 0 && faces.get(getPosition(2, dimension - 1, 1))
                    / base != faces.get(getPosition(2, dimension - 2, 1)) / base) {
                parityPLL();
            }
            */
            algorithmMatchEdgePiecesThirdLayer();
            alignThirdLayerCross();
            return;
        } else {
            while (frontMatch || !rightMatch) {
                rotateCubeCounterClockwise();
                rightMatch = (faces.get(getPosition(3, 1, 1)) / base) == (faces.get(getPosition(3, 1, 0))
                        / base);
                frontMatch = (faces.get(getPosition(5, 1, 1)) / base) == (faces.get(getPosition(5, 0, 1))
                        / base);
            }
            backMatch = (faces.get(getPosition(0, 1, 1))
                    / base) == (faces.get(getPosition(0, dimension - 1, 1)) / base);
            /*
                    if(!backMatch){
                parityOLL();
                return;
            }
            */

            algorithmMatchEdgePiecesThirdLayer();
            rotateLayer(1, 0);
        }

    }

    // Algorithm for above
    private void algorithmMatchEdgePiecesThirdLayer() {
        rotateSide(1, dimension - 1);
        rotateLayer(1, 0);
        rotateSide(-1, dimension - 1);
        rotateLayer(1, 0);
        rotateSide(1, dimension - 1);
        rotateLayer(1, 0);
        rotateLayer(1, 0);
        rotateSide(-1, dimension - 1);
    }
    // -----------------------------------------------------

    // step two
    // -----------------------------------------------------

    // returns whether top front right corner piece is in right place (regardless of
    // orientation)
    private boolean checkTopFrontRightCornerPiece() {
        int base = dimension * dimension;

        int colorTop = faces.get(getPosition(2, 1, 1)) / base;
        int colorFront = faces.get(getPosition(5, 1, 1)) / base;
        int colorRight = faces.get(getPosition(3, 1, 1)) / base;

        ArrayList<Integer> colorsTopRightFront = new ArrayList<>();
        colorsTopRightFront.add(colorTop);
        colorsTopRightFront.add(colorFront);
        colorsTopRightFront.add(colorRight);
        Collections.sort(colorsTopRightFront);

        ArrayList<Integer> colorsTopRightFrontCompare = new ArrayList<>();
        colorsTopRightFrontCompare.add(faces.get(getPosition(2, dimension - 1, dimension - 1)) / base);
        colorsTopRightFrontCompare.add(faces.get(getPosition(3, dimension - 1, 0)) / base);
        colorsTopRightFrontCompare.add(faces.get(getPosition(5, 0, dimension - 1)) / base);
        Collections.sort(colorsTopRightFrontCompare);
        return colorsTopRightFront.equals(colorsTopRightFrontCompare);
    }

    // puts third layer coreners in correct spot (regardless of orientation of
    // pieces)
    private void orientThirdLayerCorners() {
        int numMatches = 0;
        boolean matcher = checkTopFrontRightCornerPiece();

        for (int i = 0; i < 4; i++) {
            matcher = checkTopFrontRightCornerPiece();
            if (matcher) {
                numMatches++;
            }
            rotateCubeClockwise();

        }

        if (numMatches == 4) {
            return;
            /*
             * }
             * 
             * else if(numMatches == 2){ parityPLL(); orientThirdLayerCorners(); return;
             */
        } else if (numMatches == 0) {
            thirdLayerCornerAlgorithm();
            orientThirdLayerCorners();
            return;
        } else {
            while (!matcher) {
                rotateCubeClockwise();
                matcher = checkTopFrontRightCornerPiece();
            }
            if (dimension % 2 == 0 && numMatches == 2) {
                while (matcher) {
                    rotateCubeClockwise();
                    matcher = checkTopFrontRightCornerPiece();
                }
                parityPLL();
                alignThirdLayerCross();
                orientThirdLayerCorners();
                return;
            }
            thirdLayerCornerAlgorithm();
            // sometimes need to do it again
            orientThirdLayerCorners();
            return;
        }

    }

    // algorithm for above
    private void thirdLayerCornerAlgorithm() {
        rotateLayer(1, 0);
        rotateSide(1, dimension - 1);
        rotateLayer(-1, 0);
        rotateSide(1, 0);
        rotateLayer(1, 0);
        rotateSide(-1, dimension - 1);
        rotateLayer(-1, 0);
        rotateSide(-1, 0);
    }
    // -----------------------------------------------------

    // step three
    // -----------------------------------------------------
    // tests whether corneres are in right orientation
    private boolean thirdLayerColorAlignmentTest() {
        int base = dimension * dimension;
        int bottomCorner = faces.get(getPosition(2, dimension - 1, dimension - 1)) / base;
        int frontFace = faces.get(getPosition(2, 1, 1)) / base;
        return frontFace == bottomCorner;
    }

    // Orients third layer corners
    private void solveThirdLayerCorners() {
        boolean status = thirdLayerColorAlignmentTest();
        for (int i = 0; i < 4; i++) {
            while (!status) {
                lastCornerAlignmentAlgorithm();
                status = thirdLayerColorAlignmentTest();
            }
            rotateLayer(1, 0);
            status = thirdLayerColorAlignmentTest();
        }

    }

    // algorithm for above
    private void lastCornerAlignmentAlgorithm() {
        rotateSide(-1, dimension - 1);
        rotateLayer(1, dimension - 1);
        rotateSide(1, dimension - 1);
        rotateLayer(-1, dimension - 1);

    }
    // -----------------------------------------------------

    // ---------------------------------------------TOOLS----------------------------------------


    // takes in any cube on the desired face (doesn't matter what color the cube is,
    // returns what face that cube is on)
    //THIS IS NOT THE MAIN MOVE SEQUENCE I'M USING, BUT IT CAN BE USED
    //I included it just so that classic rubiks cube people can put in 3x3 algorithms

    /*
    private void moveSequence3x3(ArrayList<String> moves) {
        for (String move : moves) {
            if (move.equals("R")) {
                rotateSide(1, dimension - 1);
            } else if (move.equals("R'")) {
                rotateSide(-1, dimension - 1);
            } else if (move.equals("L")) {
                // rotate Side rotates clockwise with respect to the right face, you have to
                // switch 1 with -1 (see B)
                rotateSide(-1, 0);
            } else if (move.equals("L'")) {
                rotateSide(1, 0);
            } else if (move.equals("U")) {
                rotateLayer(1, dimension - 1);
            } else if (move.equals("U'")) {
                rotateLayer(-1, dimension - 1);
            } else if (move.equals("T")) {
                rotateLayer(1, 0);
            } else if (move.equals("U'")) {
                rotateLayer(-1, 0);
            } else if (move.equals("B")) {

                // When rotating the back, rotate FrontFace assumes you are still looking at the
                // cube
                // All major cube algorithms turn back clockwise when you look at back,
                // rotateFrontFace
                // always turns clockwise if you're looking at it, this I switched 1 with -1
                rotateFrontFace(-1, 0);
            } else if (move.equals("B'")) {
                rotateFrontFace(1, 0);
            } else if (move.equals("F")) {
                rotateFrontFace(1, dimension - 1);
            } else if (move.equals("F'")) {
                rotateFrontFace(-1, dimension - 1);
            } else if (move.equals("D")) {
                rotateLayer(1, dimension - 1);
            } else if (move.equals("D'")) {
                rotateLayer(-1, dimension - 1);
            } else if (move.equals("X")) {
                rotateCubeUp();
            } else if (move.equals("X'")) {
                rotateCubeDown();
            } else if (move.equals("Y")) {
                rotateCubeCounterClockwise();
            } else if (move.equals("Y'")) {
                rotateCubeCounterClockwise();
            } else if (move.equals("Z")) {
                rotateCubeDown();
                rotateCubeCounterClockwise();
            } else if (move.equals("Z'")) {
                rotateCubeUp();
                rotateCubeClockwise();

            }
        }
    }
    */

    private void moveSequence3x3(String move) {

        if (move.equals("R")) {
            rotateSide(1, dimension - 1);
        } else if (move.equals("R'")) {
            rotateSide(-1, dimension - 1);
        } else if (move.equals("L")) {
            // rotate Side rotates clockwise with respect to the right face, you have to
            // switch 1 with -1 (see B)
            rotateSide(-1, 0);
        } else if (move.equals("L'")) {
            rotateSide(1, 0);
        } else if (move.equals("U")) {
            rotateLayer(1, 0);
        } else if (move.equals("U'")) {
            rotateLayer(-1, 0);
        } else if (move.equals("B")) {

            // When rotating the back, rotate FrontFace assumes you are still looking at the
            // cube
            // All major cube algorithms turn back clockwise when you look at back,
            // rotateFrontFace
            // always turns clockwise if you're looking at it, this I switched 1 with -1
            rotateFrontFace(-1, 0);
        } else if (move.equals("B'")) {
            rotateFrontFace(1, 0);
        } else if (move.equals("F")) {
            rotateFrontFace(1, dimension - 1);
        } else if (move.equals("F'")) {
            rotateFrontFace(-1, dimension - 1);
        } else if (move.equals("D")) {
            rotateLayer(1, dimension - 1);
        } else if (move.equals("D'")) {
            rotateLayer(-1, dimension - 1);
        } else if (move.equals("X")) {
            rotateCubeUp();
        } else if (move.equals("X'")) {
            rotateCubeDown();
        } else if (move.equals("Y")) {
            rotateCubeCounterClockwise();
        } else if (move.equals("Y'")) {
            rotateCubeCounterClockwise();
        } else if (move.equals("Z")) {
            rotateCubeDown();
            rotateCubeCounterClockwise();
        } else if (move.equals("Z'")) {
            rotateCubeUp();
            rotateCubeClockwise();

        }
    }


    /**
     * Fuckin hey bruh, these methods solve the cube bruh
     */
    private void solveFirstLayer() {
        // solves first layer edge pieces
        solveCross();

        // solves first layer corner pieces
        solveCorners();
    }

    private void solveThirdLayer() {
        // solves the third layer cross
        solveThirdLayerCross();

        // aligns the edge pieces on the third layer
        alignThirdLayerCross();

        // aligns the corner pieces on the third layer
        orientThirdLayerCorners();

        // flips the corner pieces to the correct orientation
        solveThirdLayerCorners();
    }

    public void solve() {
        // Technically, because Im using recursion in a lot of steps, if you skip or
        // switch these, the code will enter an infinite recursive loop
        // so you must solve first second then third

        // solves first layer
        solveFirstLayer();

        // flips the cube (not entirely necessary, could have solved bottom layer above,
        // I did it exactly how I would solve it though)
        rotateCubeDown();
        rotateCubeDown();

        // solves the middle layer
        solveSecondLayer();

        // solves third layer
        solveThirdLayer();

        testOLLParity();

    }

    // Run this to see a n x n cube being solved (assuming front and edge faces are
    // already solved)
    public static void testnxn(int n) {
        // shows the cube being sovled while timing scramble and solve
        Cube3x3 myCube3x3 = new Cube3x3(n);
        // print the origional cube
        System.out.println("Cube OG at dimension: " + n);
        myCube3x3.printCube();

        // Timing the scramble
        long startTime = System.nanoTime();

        // scramble the cube 1000000 times (for large nxn cubes, you could remove Y and
        // X from scrambleCube)
        myCube3x3.scrambleCube3x3(1000000);

        long endTime = System.nanoTime();

        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;

        // print the scrambled cube
        System.out.println("Scrambled Cube at 1000000 moves (only outside edges moved)");
        myCube3x3.printCube();

        System.out.println("Execution time of scramble in nanoseconds  : " + timeElapsed);

        System.out.println("Execution time of scramble in milliseconds : " + timeElapsed / 1000000);

        // Timing the scramble
        startTime = System.nanoTime();

        // solve the scrambled cube
        // Check the sequence of steps in the method solve
        myCube3x3.solve();

        endTime = System.nanoTime();

        // get difference of two nanoTime values
        timeElapsed = endTime - startTime;
        System.out.println("solved 3x3");
        myCube3x3.printCube();

        System.out.println("Execution time of solve in nanoseconds  : " + timeElapsed);

        System.out.println("Execution time of solve in milliseconds : " + timeElapsed / 1000000);

    }

    public void solveCubenxn(Cubenxn cube) {
        cube.reduceCubeto3x3();
        if (!cube.reduced) {
            System.out.println("Cube is not reduced for some reason");
            return;
        }

    }

    public void testOLLParity() {
        int base = dimension * dimension;
        if (dimension % 2 == 1) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (faces.get(getPosition(2, dimension - 1, 1))
                    / base != faces.get(getPosition(2, dimension - 2, 1)) / base) {
                parityOLL();
                return;
            }
        }
    }

}
