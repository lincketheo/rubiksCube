
import java.util.*;

public class Cube3x3 extends Cube {
    // Does not solve 2x2
    public static ArrayList<String> algorithm;

    public Cube3x3(int dimension) {
        super(dimension);
    }

    public void scrambleCube(int moves) {
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
            randomInt = (int) (Math.random() * 16);
            scrambleAlgorithm.add(validMoves.get(randomInt));
        }
        this.moveSequence(scrambleAlgorithm);
        algorithm = scrambleAlgorithm;
    }

    /************************************************************************************
     * SOLVING METHODS
     * **********************************************************************************
     */

    /************************************************************************************
     * FIRST LAYER METHODS
     * **********************************************************************************
     */

    // EDGES

    // Moves desired the edge piece that cooresponds to whatever color is on back
    // and top to right place without disrupting anything important
    public void solveTopBackEdgePiece() {
        int base = this.getDimension() * this.getDimension();
        int topFaceColor = this.getFaces().get(getPosition(2, 1, 1)) / base;
        // Find what colors are surrounding
        // Assuming center is already solved
        int backFaceColor = this.getFaces().get(getPosition(0, 1, 1)) / base;

        ArrayList<EdgeCubie> edgeCubie = this.getEdgeCubie(topFaceColor, backFaceColor);

        // The magnitude of the certain edge cubie we are looking for
        int colorTopEdgeCubie;

        // a cubie has two indexes, test which one cooresponds to the color on the top
        // of the cube
        if (edgeCubie.get(0).index1 / base == topFaceColor) {
            colorTopEdgeCubie = edgeCubie.get(0).index1;
        } else {
            colorTopEdgeCubie = edgeCubie.get(0).index2;
        }
        int cubieBackTopFace = this.getFaceOrientation(colorTopEdgeCubie);
        // cubie is located on the back face
        // -------------------------------------------------------------
        if (cubieBackTopFace == 0) {
            // cubie is on bottom
            if (this.getFaces().indexOf(colorTopEdgeCubie) < this.getDimension()) {
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, 0);
                this.rotateSide(-1, 0);
                this.rotateLayer(1, 0);
            }
            // if cubie is on the "left" edge
            else if (this.getFaces().indexOf(colorTopEdgeCubie) % this.getDimension() == 0) {
                this.rotateLayer(-1, 0);
                this.rotateSide(-1, 0);
                this.rotateLayer(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(colorTopEdgeCubie) + 1) % this.getDimension() == 0) {
                this.rotateFrontFace(1, 0);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, 0);
                this.rotateSide(-1, 0);
                this.rotateLayer(1, 0);
            } else {
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, 0);
                this.rotateSide(-1, 0);
                this.rotateLayer(1, 0);

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
        // Cubie is on left face
        // -------------------------------------------------------------------------
        else if (cubieBackTopFace == 1) {
            if (this.getFaces().indexOf(colorTopEdgeCubie) < base + this.getDimension()) {
                this.rotateFrontFace(1, 0);
            }
            // if cubie is on the "left" edge
            else if (this.getFaces().indexOf(colorTopEdgeCubie) % this.getDimension() == 0) {
                this.rotateSide(-1, 0);
                this.rotateFrontFace(1, 0);
                this.rotateSide(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(colorTopEdgeCubie) + 1) % this.getDimension() == 0) {
                this.rotateSide(1, 0);
                this.rotateFrontFace(1, 0);
            } else {
                this.rotateSide(1, 0);
                this.rotateSide(1, 0);
                this.rotateFrontFace(1, 0);
                this.rotateSide(1, 0);
                this.rotateSide(1, 0);

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }
        // Cubie is on front face
        // --------------------------------------------------------------------------
        else if (cubieBackTopFace == 5) {
            if (this.getFaces().indexOf(colorTopEdgeCubie) < 5 * base + this.getDimension()) {
                this.rotateLayer(1, 0);
                this.rotateLayer(1, 0);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, 0);
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateLayer(-1, 0);

            }
            // if cubie is on the "left" edge
            else if (this.getFaces().indexOf(colorTopEdgeCubie) % this.getDimension() == 0) {
                this.rotateLayer(-1, 0);
                this.rotateSide(1, 0);
                this.rotateLayer(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(colorTopEdgeCubie) + 1) % this.getDimension() == 0) {
                this.rotateLayer(1, 0);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, 0);
            } else {
                this.rotateFrontFace(-1, this.getDimension() - 1);
                this.rotateLayer(1, 0);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, 0);
                this.rotateFrontFace(1, this.getDimension() - 1);

            }
            // this.printCubeNums();
        }
        // Cubie is on right face
        // --------------------------------------------------------------------------
        else if (cubieBackTopFace == 3) {
            if (this.getFaces().indexOf(colorTopEdgeCubie) < 3 * base + this.getDimension()) {
                this.rotateFrontFace(-1, 0);
            }
            // if cubie is on the "right" edge
            else if ((this.getFaces().indexOf(colorTopEdgeCubie) + 1) % this.getDimension() == 0) {
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateSide(1, this.getDimension() - 1);
            }
            // if cubie is on the "left edge"
            else if (this.getFaces().indexOf(colorTopEdgeCubie) % this.getDimension() == 0) {
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
            } else {
                this.rotateLayer(-1, 0);
                this.rotateLayer(-1, 0);
                this.rotateFrontFace(-1, this.getDimension() - 1);
                this.rotateLayer(-1, 0);
                this.rotateLayer(-1, 0);
            }

            // this.printCubeNums();
        }
        // Cubie is on the desired face (top face)
        // ---------------------------------------------------------
        else if (cubieBackTopFace == 2) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(colorTopEdgeCubie) % this.getDimension() == 0) {
                this.rotateSide(1, 0);
                this.rotateLayer(-1, 0);
                this.rotateSide(-1, 0);
                this.rotateLayer(1, 0);
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(colorTopEdgeCubie) + 1) % this.getDimension() == 0) {
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(1, 0);
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateLayer(-1, 0);
            } else if (this.getFaces().indexOf(colorTopEdgeCubie) < 3 * base && this.getFaces()
                    .indexOf(colorTopEdgeCubie) > base * 2 + this.getDimension() * (this.getDimension() - 1)) {
                this.rotateLayer(-1, 0);
                this.rotateLayer(-1, 0);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, 0);
                this.rotateLayer(1, 0);
                this.rotateFrontFace(-1, 0);
            }
            // this.printCubeNums();
        }
        // cubie is on bottom
        // ------------------------------------------------------------------------------
        else if (cubieBackTopFace == 4) {
            if (this.getFaces().indexOf(colorTopEdgeCubie) < 4 * base + this.getDimension()) {
                this.rotateFrontFace(1, 0);
                this.rotateFrontFace(1, 0);
            }
            // if cubie is on the "left" edge
            else if ((this.getFaces().indexOf(colorTopEdgeCubie) + 1) % this.getDimension() == 0) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateFrontFace(1, 0);
            }
            // if cubie is on the "right edge"
            else if (this.getFaces().indexOf(colorTopEdgeCubie) % this.getDimension() == 0) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateFrontFace(1, 0);
            } else {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateFrontFace(1, 0);
            }
            // this.printCubeNums();
        }
    }

    // does solvetopbackedgepiece four times all while rotating cube everytime
    public void solveCross() {
        for (int i = 0; i < 4; i++) {
            this.solveTopBackEdgePiece();
            this.rotateCubeClockwise();
        }
    }

    // CORNERS

    // moves any corner piece to index 0,0,0 or 0,dim-1,0 then does the following
    // two methods to put it in place
    public void solveTopBackRightCornerPiece() {
        int base = this.getDimension() * this.getDimension();
        int topFaceColor = this.getFaces().get(getPosition(2, 1, 1)) / base;
        // Find what colors are surrounding
        // Assuming center is already solved
        int backFaceColor = this.getFaces().get(getPosition(0, 1, 1)) / base;
        int rightFaceColor = this.getFaces().get(getPosition(3, 1, 1)) / base;

        CornerCubie cornerCubie = this.getCornerCubie(topFaceColor, backFaceColor, rightFaceColor);

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

        int cubieBackTopFace = this.getFaceOrientation(colorTopCornerCubie);
        // cubie is located on the back face
        // -------------------------------------------------------------
        if (cubieBackTopFace == 0) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.backBottomLeftCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base
                    + (this.getDimension() - 1)) {
                this.backBottomRightCornertoTopBackRightCorner();
            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                // get piece to bottom left corner of back
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);

                // Now it is on the bottom right corner, but it is backwards, move it to left
                // corner

                // move piece to place
                this.backBottomRightCornertoTopBackRightCorner();
            }

            // bottom left corner
            else {

                // get piece to bottom right corner
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);

                // get piece to place
                this.backBottomLeftCornertoTopBackRightCorner();
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
        // Cubie is on left face
        // -------------------------------------------------------------------------
        else if (cubieBackTopFace == 1) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base
                    + (this.getDimension() - 1)) {
                this.rotateSide(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(-1, 0);
                this.backBottomRightCornertoTopBackRightCorner();
            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                this.rotateSide(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();

            }

            // bottom left corner
            else {
                this.rotateLayer(1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
        // Cubie is on right face
        // --------------------------------------------------------------------------
        else if (cubieBackTopFace == 3) {
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();
            }
            // if cubie is on the "right" edge
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base
                    + (this.getDimension() - 1)) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();
            }
            // if cubie is on the "left edge"
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();
            } else {
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();

            }

            // this.printCubeNums();
        }
        // Cubie is on the desired face (top face)
        // ---------------------------------------------------------
        else if (cubieBackTopFace == 2) {
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateSide(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();
            }

            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();
            }

            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base)
                    + ((this.getDimension()) * (this.getDimension() - 1))) {
                this.rotateSide(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();

            }

            // this.printCubeNums();
        } else if (cubieBackTopFace == 5) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {

                this.rotateFrontFace(-1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base
                    + (this.getDimension() - 1)) {
                this.rotateFrontFace(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();

            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomLeftCornertoTopBackRightCorner();
            }

            // bottom left corner
            else {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        } else if (cubieBackTopFace == 4) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base
                    + (this.getDimension() - 1)) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();

            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (base - 1)) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();
            }

            // bottom left corner
            else {

                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.backBottomRightCornertoTopBackRightCorner();

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
    }

    // FOLLOWING TWO ARE USED A LOT IN THE SOLVE CORNER METHOD
    // if you're looking at the cube foldout, moves
    // index 0,0,0 to 2,0,dim-1)
    public void backBottomLeftCornertoTopBackRightCorner() {
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateFrontFace(1, 0);
        this.rotateLayer(-1, this.getDimension() - 1);
        this.rotateFrontFace(-1, 0);

    }

    // if you're looking at the cube foldout, moves
    // index 0,dim-1,0 to 2,0,dim-1)
    public void backBottomRightCornertoTopBackRightCorner() {
        this.rotateLayer(-1, this.getDimension() - 1);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateSide(-2, this.getDimension() - 1);

    }

    // Repeates solvetopbackrightcornerpiece four times
    public void solveCorners() {
        for (int i = 0; i < 4; i++) {
            this.solveTopBackRightCornerPiece();
            this.rotateCubeClockwise();

        }
    }

    /************************************************************************************
     * SECOND LAYER METHODS
     * **********************************************************************************
     */

    // moves top/front edge piece to the right/left edge spot THIS CAN DO
    // EVERYTHING, YOU DON'T NEED MOVE TOP FRONT TO LEFT I'VE INCLUDED IT ANYWAYS
    public void moveTopFrontEdgePiecetoRight() {
        int base = this.getDimension() * this.getDimension();
        int colorRight = this.getFaces().get(this.getPosition(3, 1, 1)) / base;
        int colorFront = this.getFaces().get(this.getPosition(5, 1, 1)) / base;
        ArrayList<EdgeCubie> cubie = this.getEdgeCubie(colorRight, colorFront);
        int faceFrontCubie;
        int faceFrontCubieNo;
        if (cubie.get(0).index1 / 9 == colorFront) {
            faceFrontCubie = this.getFaceOrientation(cubie.get(0).index1);
            faceFrontCubieNo = cubie.get(0).index1;
        } else {
            faceFrontCubie = this.getFaceOrientation(cubie.get(0).index2);
            faceFrontCubieNo = cubie.get(0).index2;
        }
        // Move it to its place

        // on back face somewhere
        if (faceFrontCubie == 0) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubieNo) % this.getDimension() == 0) {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoRight();
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(faceFrontCubieNo) + 1) % this.getDimension() == 0) {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.rotateCubeCounterClockwise();
                this.leftEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoRight();
            } else {
                this.rotateLayer(1, 0);
                this.rotateLayer(1, 0);
                this.rightEdgePieceLayerTwo();
                // ENDS METHOD
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }

        else if (faceFrontCubie == 1) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubieNo) < 1 * base + this.getDimension()) {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.leftEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoRight();

            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(faceFrontCubieNo) + 1) % this.getDimension() == 0) {
                this.rotateLayer(-1, 0);
                this.rightEdgePieceLayerTwo();
                // ENDS METHOD

            } else {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoRight();
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }

        else if (faceFrontCubie == 2) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubieNo) < 2 * base + this.getDimension()) {

                // Only exception is when face is on top, then we have to use the right
                // algorithm
                this.rotateLayer(1, 0);
                this.rotateCubeClockwise();
                this.leftEdgePieceLayerTwo();
                this.rotateCubeCounterClockwise();
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(faceFrontCubieNo) + 1) % this.getDimension() == 0) {
                this.rotateCubeClockwise();
                this.leftEdgePieceLayerTwo();
                this.rotateCubeCounterClockwise();
            } else if (this.getFaces().indexOf(faceFrontCubieNo) % this.getDimension() == 0) {
                this.rotateLayer(-1, 0);
                this.rotateLayer(-1, 0);
                this.rotateCubeClockwise();
                this.leftEdgePieceLayerTwo();
                this.rotateCubeCounterClockwise();
            }

            else {
                this.rotateLayer(-1, 0);
                this.rotateCubeClockwise();
                this.leftEdgePieceLayerTwo();
                this.rotateCubeCounterClockwise();
            }
        }

        else if (faceFrontCubie == 3) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubieNo) < 3 * base + this.getDimension()) {
                // Get the edge piece out of there
                this.rotateCubeClockwise();
                this.rightEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeCounterClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoRight();
            }
            // if cubie is on the "right edge"
            else if (this.getFaces().indexOf(faceFrontCubieNo) % this.getDimension() == 0) {
                this.rotateLayer(1, 0);
                this.rightEdgePieceLayerTwo();
                // ENDS METHOD

            } else {
                // Get the edge piece out of there
                this.rotateCubeClockwise();
                this.leftEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeCounterClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoRight();
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }

        else if (faceFrontCubie == 5) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubieNo) < 5 * base + this.getDimension()) {
                this.rightEdgePieceLayerTwo();
                // ENDS METHOD
            }
            // if cubie is on the "right edge"
            else if (this.getFaces().indexOf(faceFrontCubieNo) % this.getDimension() == 0) {
                this.leftEdgePieceLayerTwo();
                this.moveTopFrontEdgePiecetoRight();
            }
        }

    }

    // does movetopfront... four times while rotating cube
    public void solveSecondLayer() {
        for (int i = 0; i < 4; i++) {
            this.moveTopFrontEdgePiecetoRight();
            this.rotateCubeClockwise();
        }
    }

    // DON'T NEED - TORIGHT DOES EVERYTHING
    public void moveTopFrontEdgePiecetoLeft() {
        int base = this.getDimension() * this.getDimension();
        int colorLeft = this.getFaces().get(this.getPosition(1, 1, 1)) / base;
        int colorFront = this.getFaces().get(this.getPosition(5, 1, 1)) / base;
        EdgeCubie cubie = this.getEdgeCubie(colorLeft, colorFront);
        int faceFrontCubie;
        if (cubie.index1 / 9 == colorFront) {
            faceFrontCubie = this.getFaceOrientation(cubie.index1);
        } else {
            faceFrontCubie = this.getFaceOrientation(cubie.index2);
        }

        // Move it to its place

        // on back face somewhere
        if (faceFrontCubie == 0) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubie) % this.getDimension() == 0) {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoLeft();
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(faceFrontCubie) + 1) % this.getDimension() == 0) {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.rotateCubeCounterClockwise();
                this.leftEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoLeft();
            } else {
                this.rotateLayer(1, 0);
                this.rotateLayer(1, 0);
                this.leftEdgePieceLayerTwo();
                // ENDS METHOD
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }

        else if (faceFrontCubie == 1) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubie) < 5 * base + this.getDimension()) {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.leftEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoLeft();

            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(faceFrontCubie) + 1) % this.getDimension() == 0) {
                this.rotateLayer(-1, 0);
                this.leftEdgePieceLayerTwo();
                // ENDS METHOD

            } else {
                // Get the edge piece out of there
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoLeft();
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }

        else if (faceFrontCubie == 2) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubie) < 2 * base + this.getDimension()) {

                // Only exception is when face is on top, then we have to use the right
                // algorithm
                this.rotateLayer(-1, 0);
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();
            }
            // if cubie is on the "right edge"
            else if (this.getFaces().indexOf(faceFrontCubie) % this.getDimension() == 0) {
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();
            } else if ((this.getFaces().indexOf(faceFrontCubie) + 1) % this.getDimension() == 0) {
                this.rotateLayer(-1, 0);
                this.rotateLayer(-1, 0);
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();
            }

            else {
                this.rotateLayer(1, 0);
                this.rotateCubeCounterClockwise();
                this.rightEdgePieceLayerTwo();
            }
        }

        else if (faceFrontCubie == 3) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubie) < 3 * base + this.getDimension()) {
                // Get the edge piece out of there
                this.rotateCubeClockwise();
                this.rightEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeCounterClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoLeft();

            }
            // if cubie is on the "right edge"
            else if (this.getFaces().indexOf(faceFrontCubie) % this.getDimension() == 0) {
                this.rotateLayer(1, 0);
                this.leftEdgePieceLayerTwo();
                // ENDS METHOD

            } else {
                // Get the edge piece out of there
                this.rotateCubeClockwise();
                this.leftEdgePieceLayerTwo();

                // return to origional orientation
                this.rotateCubeCounterClockwise();

                // Do this method (now that it's out of its edge spot)
                this.moveTopFrontEdgePiecetoLeft();
            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();
        }

        else if (faceFrontCubie == 5) {
            // if cubie is on the "left" edge
            if (this.getFaces().indexOf(faceFrontCubie) < 5 * base + this.getDimension()) {
                this.leftEdgePieceLayerTwo();
                // ENDS METHOD
            }
            // if cubie is on the "right edge"
            else if ((this.getFaces().indexOf(faceFrontCubie) + 1) % this.getDimension() == 0) {
                this.rightEdgePieceLayerTwo();
                this.moveTopFrontEdgePiecetoLeft();
            }
        }

    }

    // Algorithm for top front to right front edge
    public void rightEdgePieceLayerTwo() {
        // U R U' R' U' F' U F
        this.rotateLayer(1, 0);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateFrontFace(-1, this.getDimension() - 1);
        this.rotateLayer(1, 0);
        this.rotateFrontFace(1, this.getDimension() - 1);
    }

    // Algorithm for top front to left front edge
    public void leftEdgePieceLayerTwo() {
        // U' L' U L U F U' F'
        this.rotateLayer(-1, 0);
        this.rotateSide(1, 0);
        this.rotateLayer(1, 0);
        this.rotateSide(-1, 0);
        this.rotateLayer(1, 0);
        this.rotateFrontFace(1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateFrontFace(-1, this.getDimension() - 1);
    }

    /************************************************************************************
     * THIRD LAYER METHODS
     * **********************************************************************************
     */

    // Step one
    // -----------------------------------------------------

    // Solves the cross on top (doesn't alighn edge pieces to right place though)
    public void solveThirdLayerCross() {
        // how many top level squares are here, if theres one, do
        // algorithmthirdlayercross in any orientation and then do this method again
        int base = this.getDimension() * this.getDimension();

        // 10 01 dim - 1 dim - 2 dim - 2 dim -1
        int colorTopFace = this.getFaces().get(this.getPosition(2, 1, 1)) / base;
        int colorLeftEdge = this.getFaces().get(this.getPosition(2, 1, 0)) / base;
        int colorRightEdge = this.getFaces().get(this.getPosition(2, 1, this.getDimension() - 1)) / base;
        int colorBottomEdge = this.getFaces().get(this.getPosition(2, this.getDimension() - 1, 1)) / base;
        int colorTopEdge = this.getFaces().get(this.getPosition(2, 0, 1)) / base;

        int noTopLayerFaces = ((colorTopFace == colorLeftEdge) ? 1 : 0) + ((colorTopFace == colorRightEdge) ? 1 : 0)
                + ((colorTopFace == colorBottomEdge) ? 1 : 0) + ((colorTopFace == colorTopEdge) ? 1 : 0);

        if (noTopLayerFaces < 2) {
            this.alogirthmThirdLayerCross();
            this.solveThirdLayerCross();
            return;
        } else if (noTopLayerFaces == 4) {
            return;
        }

        // For both l orientation and line, the bottom is not the same color while the
        // left piece is
        boolean correctOrientation = (colorLeftEdge == colorTopFace) && (colorBottomEdge != colorTopFace);
        while (!correctOrientation) {
            this.rotateCubeClockwise();
            colorLeftEdge = this.getFaces().get(this.getPosition(2, 1, 0)) / base;
            colorRightEdge = this.getFaces().get(this.getPosition(2, 1, this.getDimension() - 1)) / base;
            colorBottomEdge = this.getFaces().get(this.getPosition(2, this.getDimension() - 1, 1)) / base;
            colorTopEdge = this.getFaces().get(this.getPosition(2, 0, 1)) / base;
            correctOrientation = (colorLeftEdge == colorTopFace) && (colorBottomEdge != colorTopFace);
        }
        int noRepetitions;
        if (colorLeftEdge == colorRightEdge) {
            noRepetitions = 1;
        } else {
            noRepetitions = 2;
        }

        for (int i = 0; i < noRepetitions; i++) {
            this.alogirthmThirdLayerCross();
        }
    }

    // algorithm for the above
    public void alogirthmThirdLayerCross() {
        this.rotateFrontFace(1, this.getDimension() - 1);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(1, 0);
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateFrontFace(-1, this.getDimension() - 1);

    }

    // Aligns the edge pieces
    public void alignThirdLayerCross() {
        // align so that front equals top
        // three cases:
        // all match: return
        // cross match: do algorithm, do this again return
        // touching faces match turn cube until front and top dont match do algorithm do
        // this again return
        // no match do the algorithm do this again return
        int base = this.getDimension() * this.getDimension();
        boolean leftMatch = (this.getFaces().get(this.getPosition(1, 1, 1))
                / base) == (this.getFaces().get(this.getPosition(1, 1, this.getDimension() - 1)) / base);
        boolean rightMatch = (this.getFaces().get(this.getPosition(3, 1, 1))
                / base) == (this.getFaces().get(this.getPosition(3, 1, 0)) / base);
        boolean frontMatch = (this.getFaces().get(this.getPosition(5, 1, 1))
                / base) == (this.getFaces().get(this.getPosition(5, 0, 1)) / base);
        boolean backMatch = (this.getFaces().get(this.getPosition(0, 1, 1))
                / base) == (this.getFaces().get(this.getPosition(0, this.getDimension() - 1, 1)) / base);
        int noMatching = ((leftMatch ? 1 : 0) + (rightMatch ? 1 : 0) + (frontMatch ? 1 : 0) + (backMatch ? 1 : 0));
        int failSafe = 0;

        while (failSafe < 4 && noMatching < 2) {
            this.rotateLayer(1, 0);
            leftMatch = (this.getFaces().get(this.getPosition(1, 1, 1))
                    / base) == (this.getFaces().get(this.getPosition(1, 1, this.getDimension() - 1)) / base);
            rightMatch = (this.getFaces().get(this.getPosition(3, 1, 1))
                    / base) == (this.getFaces().get(this.getPosition(3, 1, 0)) / base);
            frontMatch = (this.getFaces().get(this.getPosition(5, 1, 1))
                    / base) == (this.getFaces().get(this.getPosition(5, 0, 1)) / base);
            backMatch = (this.getFaces().get(this.getPosition(0, 1, 1))
                    / base) == (this.getFaces().get(this.getPosition(0, this.getDimension() - 1, 1)) / base);
            noMatching = ((leftMatch ? 1 : 0) + (rightMatch ? 1 : 0) + (frontMatch ? 1 : 0) + (backMatch ? 1 : 0));

            failSafe++;
        }

        if (noMatching == 4) {
            return;
        } else if (frontMatch && backMatch || noMatching < 2 || (rightMatch && leftMatch)) {
            this.algorithmMatchEdgePiecesThirdLayer();
            this.alignThirdLayerCross();
            return;
        } else {
            while (frontMatch || !rightMatch) {
                this.rotateCubeCounterClockwise();
                rightMatch = (this.getFaces().get(this.getPosition(3, 1, 1))
                        / base) == (this.getFaces().get(this.getPosition(3, 1, 0)) / base);
                frontMatch = (this.getFaces().get(this.getPosition(5, 1, 1))
                        / base) == (this.getFaces().get(this.getPosition(5, 0, 1)) / base);
            }
            this.algorithmMatchEdgePiecesThirdLayer();
            this.rotateLayer(1, 0);
        }

    }

    // Algorithm for above
    public void algorithmMatchEdgePiecesThirdLayer() {
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(1, 0);
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(1, 0);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(1, 0);
        this.rotateLayer(1, 0);
        this.rotateSide(-1, this.getDimension() - 1);
    }
    // -----------------------------------------------------

    // step two
    // -----------------------------------------------------

    // returns whether top front right corner piece is in right place (regardless of
    // orientation)
    public boolean checkTopFrontRightCornerPiece() {
        int base = this.getDimension() * this.getDimension();

        int colorTop = this.getFaces().get(this.getPosition(2, 1, 1)) / base;
        int colorFront = this.getFaces().get(this.getPosition(5, 1, 1)) / base;
        int colorRight = this.getFaces().get(this.getPosition(3, 1, 1)) / base;

        ArrayList<Integer> colorsTopRightFront = new ArrayList<>();
        colorsTopRightFront.add(colorTop);
        colorsTopRightFront.add(colorFront);
        colorsTopRightFront.add(colorRight);
        Collections.sort(colorsTopRightFront);

        ArrayList<Integer> colorsTopRightFrontCompare = new ArrayList<>();
        colorsTopRightFrontCompare
                .add(this.getFaces().get(this.getPosition(2, this.getDimension() - 1, this.getDimension() - 1)) / base);
        colorsTopRightFrontCompare.add(this.getFaces().get(this.getPosition(3, this.getDimension() - 1, 0)) / base);
        colorsTopRightFrontCompare.add(this.getFaces().get(this.getPosition(5, 0, this.getDimension() - 1)) / base);
        Collections.sort(colorsTopRightFrontCompare);
        return colorsTopRightFront.equals(colorsTopRightFrontCompare);
    }

    // puts third layer coreners in correct spot (regardless of orientation of
    // pieces)
    public void orientThirdLayerCorners() {
        int numMatches = 0;
        boolean matcher = this.checkTopFrontRightCornerPiece();

        for (int i = 0; i < 4; i++) {
            matcher = this.checkTopFrontRightCornerPiece();
            if (matcher) {
                numMatches++;
            }
            this.rotateCubeClockwise();

        }

        if (numMatches == 4) {
            return;
        } else if (numMatches == 0) {
            this.thirdLayerCornerAlgorithm();
            this.orientThirdLayerCorners();
            return;
        } else {
            while (!matcher) {
                this.rotateCubeClockwise();
                matcher = this.checkTopFrontRightCornerPiece();
            }
            this.thirdLayerCornerAlgorithm();
            // sometimes need to do it again
            this.orientThirdLayerCorners();
            return;
        }

    }

    // algorithm for above
    public void thirdLayerCornerAlgorithm() {
        this.rotateLayer(1, 0);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateSide(1, 0);
        this.rotateLayer(1, 0);
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateSide(-1, 0);
    }
    // -----------------------------------------------------

    // step three
    // -----------------------------------------------------
    // tests whether corneres are in right orientation
    public boolean thirdLayerColorAlignmentTest() {
        int base = this.getDimension() * this.getDimension();
        int bottomCorner = this.getFaces().get(this.getPosition(2, this.getDimension() - 1, this.getDimension() - 1))
                / base;
        int frontFace = this.getFaces().get(this.getPosition(2, 1, 1)) / base;
        return frontFace == bottomCorner;
    }

    // Orients third layer corners
    public void solveThirdLayerCorners() {
        boolean status = this.thirdLayerColorAlignmentTest();
        for (int i = 0; i < 4; i++) {
            while (!status) {
                this.lastCornerAlignmentAlgorithm();
                status = this.thirdLayerColorAlignmentTest();
            }
            this.rotateLayer(1, 0);
            status = this.thirdLayerColorAlignmentTest();
        }

    }

    // algorithm for above
    public void lastCornerAlignmentAlgorithm() {
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(-1, this.getDimension() - 1);

    }
    // -----------------------------------------------------

    // ---------------------------------------------TOOLS----------------------------------------
    // Generally, I'm not using strings to represent colors unless debugging. The
    // program knows that a color is returned from the number / base (as ints)
    // To find a color, just integer divide the number by the (dimension ^ 2)

    // takes in any cube on the desired face (doesn't matter what color the cube is,
    // returns what face that cube is on)

    public void solve() {

        this.solveCross();
        this.solveCorners();
        this.rotateCubeDown();
        this.rotateCubeDown();
        this.solveSecondLayer();
        this.solveThirdLayerCross();
        this.alignThirdLayerCross();
        this.orientThirdLayerCorners();
        this.solveThirdLayerCorners();
    }


    //Run this to see a 3x3 cube being solved
    //the idea is to be able to put any dimension in (scramble only moves 3x3 like faces)
    //I wrote all of the code to be able to adapt to a high dimensional cube, however because I used recursion, I am getting a stack overflow error
    //This will be fixed by the time I finish the project
    public static void test() {
        // shows the cube being sovled

        Cube3x3 myCube3x3 = new Cube3x3(3);
        // print the origional cube
        myCube3x3.printCube();

        // scramble it 1000000 times
        myCube3x3.scrambleCube(100);

        // print the scrambled cube
        myCube3x3.printCube();
        
        // solve the scrambled cube
        //Check the sequence of steps in the method solve
        myCube3x3.solve();

        // print the solved cube
       
        myCube3x3.printCube();
        
    }  




    public static void main(String[] args) {
        test();
    }

}