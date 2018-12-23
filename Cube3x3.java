
import java.util.*;

public class Cube3x3 extends Cube {
    // Does not solve 2x2
    private ArrayList<EdgeCubie> edgeCubies;
    private ArrayList<CornerCubie> cornerCubies;

    public Cube3x3() {
        super(3);
        this.fillCornerCubies();
        this.fillEdgeCubies();
    }

    public void fillEdgeCubies() {
        edgeCubies = new ArrayList<>();

        for (int i = 1; i < this.getDimension() - 1; i++) {
            edgeCubies.add(new EdgeCubie(this.getPosition(0, i, 0), this.getPosition(1, 0, i)));
            edgeCubies
                    .add(new EdgeCubie(this.getPosition(0, 0, i), this.getPosition(4, 0, this.getDimension() - 1 - i)));
            edgeCubies.add(new EdgeCubie(this.getPosition(0, i, this.getDimension() - 1),
                    this.getPosition(3, 0, this.getDimension() - 1 - i)));
            edgeCubies.add(new EdgeCubie(this.getPosition(1, i, 0), this.getPosition(4, i, this.getDimension() - 1)));
            edgeCubies.add(new EdgeCubie(this.getPosition(1, this.getDimension() - 1, i),
                    this.getPosition(5, this.getDimension() - 1 - i, 0)));
            edgeCubies.add(new EdgeCubie(this.getPosition(5, i, this.getDimension() - 1),
                    this.getPosition(3, this.getDimension() - 1, i)));
            edgeCubies.add(new EdgeCubie(this.getPosition(5, this.getDimension() - 1, i),
                    this.getPosition(4, this.getDimension() - 1, this.getDimension() - 1 - i)));
            edgeCubies.add(new EdgeCubie(this.getPosition(3, i, this.getDimension() - 1), this.getPosition(4, i, 0)));
            edgeCubies.add(new EdgeCubie(this.getPosition(1, i, this.getDimension() - 1), this.getPosition(2, i, 0)));
            edgeCubies.add(new EdgeCubie(this.getPosition(0, this.getDimension() - 1, i), this.getPosition(2, 0, i)));
            edgeCubies.add(new EdgeCubie(this.getPosition(2, this.getDimension() - 1, i), this.getPosition(5, 0, i)));
            edgeCubies.add(new EdgeCubie(this.getPosition(2, i, this.getDimension() - 1), this.getPosition(3, i, 0)));
        }
    }

    public void fillCornerCubies() {
        cornerCubies = new ArrayList<>();
        cornerCubies.add(new CornerCubie(this.getPosition(0, 0, 0), this.getPosition(1, 0, 0),
                this.getPosition(4, 0, this.getDimension() - 1)));
        cornerCubies.add(new CornerCubie(this.getPosition(0, 0, this.getDimension() - 1),
                this.getPosition(3, 0, this.getDimension() - 1), this.getPosition(4, 0, 0)));
        cornerCubies.add(new CornerCubie(this.getPosition(0, this.getDimension() - 1, 0),
                this.getPosition(1, 0, this.getDimension() - 1), this.getPosition(2, 0, 0)));
        cornerCubies.add(new CornerCubie(this.getPosition(0, this.getDimension() - 1, this.getDimension() - 1),
                this.getPosition(2, 0, this.getDimension() - 1), this.getPosition(3, 0, 0)));
        cornerCubies.add(new CornerCubie(this.getPosition(2, this.getDimension() - 1, 0),
                this.getPosition(1, this.getDimension() - 1, this.getDimension() - 1), this.getPosition(5, 0, 0)));
        cornerCubies.add(new CornerCubie(this.getPosition(2, this.getDimension() - 1, this.getDimension() - 1),
                this.getPosition(3, this.getDimension() - 1, 0), this.getPosition(5, 0, this.getDimension() - 1)));
        cornerCubies.add(new CornerCubie(this.getPosition(5, this.getDimension() - 1, 0),
                this.getPosition(1, this.getDimension() - 1, 0),
                this.getPosition(4, this.getDimension() - 1, this.getDimension() - 1)));
        cornerCubies.add(new CornerCubie(this.getPosition(5, this.getDimension() - 1, this.getDimension() - 1),
                this.getPosition(3, this.getDimension() - 1, this.getDimension() - 1),
                this.getPosition(4, this.getDimension() - 1, 0)));

    }

    public ArrayList<EdgeCubie> getEdgeCubie(int color1, int color2) {
        int base = this.getDimension() * this.getDimension();
        ArrayList<EdgeCubie> cubies = new ArrayList<>();
        for (EdgeCubie cubie : edgeCubies) {
            if ((cubie.index1 / base == color1 && cubie.index2 / base == color2)
                    || (cubie.index1 / base == color2 && cubie.index2 / base == color1)) {
                cubies.add(cubie);
            }
        }
        return cubies;

    }

    public CornerCubie getCornerCubie(int color1, int color2, int color3) {
        int base = this.getDimension() * this.getDimension();
        ArrayList<Integer> colors;
        ArrayList<Integer> testingColors = new ArrayList<>();
        testingColors.add(color1);
        testingColors.add(color2);
        testingColors.add(color3);
        Collections.sort(testingColors);

        for (CornerCubie cubie : cornerCubies) {
            colors = new ArrayList<>();
            colors.add(cubie.index1 / base);
            colors.add(cubie.index2 / base);
            colors.add(cubie.index3 / base);
            Collections.sort(colors);
            boolean match = true;
            for (int i = 0; i < 3; i++) {
                if (colors.get(i) != testingColors.get(i)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return cubie;
            }
        }
        return null;

    }

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

    public void backBottomLeftCornertoTopBackRightCorner() {
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateFrontFace(1, 0);
        this.rotateLayer(-1, this.getDimension() - 1);
        this.rotateFrontFace(-1, 0);

    }

    // Don't have very creative names, (if you're looking at the cube foldout, moves
    // index 0,0,0 to 2,0,dim-1)
    public void backBottomRightCornertoTopBackRightCorner() {
        this.rotateLayer(-1, this.getDimension() - 1);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateSide(-2, this.getDimension() - 1);

    }

    public void solveCorners() {
        for (int i = 0; i < 4; i++) {
            this.solveTopBackRightCornerPiece();
            this.rotateCubeClockwise();

        }
    }

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

    public void solveSecondLayer() {
        for (int i = 0; i < 4; i++) {
            this.moveTopFrontEdgePiecetoRight();
            this.rotateCubeClockwise();
        }
    }

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

    // Moves top front edge piece to either left / front or right / front edge place
    // (the correct one)
    public void moveTopFrontEdgePieceToSide() {
        int base = this.getDimension() * this.getDimension();

        int colorLeft = this.getFaces().get(this.getPosition(1, 1, 1)) / base;
        int colorRight = this.getFaces().get(this.getPosition(3, 1, 1)) / base;
        int colorFront = this.getFaces().get(this.getPosition(5, 1, 1)) / base;

        EdgeCubie cubie = this.getEdgeCubie(colorLeft, colorFront);
        int faceFrontCubie;
        if (cubie.index1 / 9 == colorFront) {
            faceFrontCubie = this.getFaceOrientation(cubie.index1);
        } else {
            faceFrontCubie = this.getFaceOrientation(cubie.index2);
        }

    }

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

    public void alogirthmThirdLayerCross() {
        this.rotateFrontFace(1, this.getDimension() - 1);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(1, 0);
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(-1, 0);
        this.rotateFrontFace(-1, this.getDimension() - 1);

    }

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
        colorsTopRightFrontCompare.add(this.getFaces().get(this.getPosition(2, this.getDimension() - 1, this.getDimension() - 1)) / base);
        colorsTopRightFrontCompare.add(this.getFaces().get(this.getPosition(3, this.getDimension() - 1, 0)) / base);
        colorsTopRightFrontCompare.add(this.getFaces().get(this.getPosition(5, 0, this.getDimension() - 1)) / base);
        Collections.sort(colorsTopRightFrontCompare);
        return colorsTopRightFront.equals(colorsTopRightFrontCompare);
    }

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
            while(!matcher){
                this.rotateCubeClockwise();
                matcher = this.checkTopFrontRightCornerPiece();
            }
            this.thirdLayerCornerAlgorithm();
            //sometimes need to do it again
            this.orientThirdLayerCorners();
            return;
        }

    }

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


    public void lastCornerAlignmentAlgorithm(){
        this.rotateSide(-1, this.getDimension() - 1);
        this.rotateLayer(1, this.getDimension() - 1);
        this.rotateSide(1, this.getDimension() - 1);
        this.rotateLayer(-1, this.getDimension() - 1);


    }

    public boolean thirdLayerColorAlignmentTest(){
        int base = this.getDimension() * this.getDimension();
        int bottomCorner = this.getFaces().get(this.getPosition(2, this.getDimension() - 1, this.getDimension() - 1)) / base;
        int frontFace = this.getFaces().get(this.getPosition(2, 1, 1)) / base;
        return frontFace == bottomCorner;
    }

    public void solveThirdLayerCorners(){
        boolean status = this.thirdLayerColorAlignmentTest();
        for(int i = 0; i < 4; i++){
            while(!status){
                this.lastCornerAlignmentAlgorithm();
                status = this.thirdLayerColorAlignmentTest();
            }
            this.rotateLayer(1, 0);
            status = this.thirdLayerColorAlignmentTest();
        }
        
    }

    // ---------------------------------------------TOOLS----------------------------------------
    // Generally, I'm not using strings to represent colors unless debugging. The
    // program knows that a color is returned from the number / base (as ints)
    // To find a color, just integer divide the number by the (dimension ^ 2)

    // takes in any cube on the desired face (doesn't matter what color the cube is,
    // returns what face that cube is on)
    public int getFaceColor(int color) {
        // find index of the element color
        int valuesIndex = this.getFaces().indexOf(color);
        // find the value of values index / base (dim ^ 2) this is the "sudoFace" the
        // cube is on, but faces change a lot, so we need to compare what the face is
        // compared to the center piece
        int sudoFace = valuesIndex / (this.getDimension() * this.getDimension());
        // (by center piece, I've just done dim + 2, any cube's (face, 1, 1) pos will be
        // the color of the face if the center piece is solved
        return this.getFaces().get(getPosition(sudoFace, 1, 1)) / (this.getDimension() * this.getDimension());
    }

    // wheareas getfacecolor represents the color (not face in getposition(face,
    // row, col)) getfaceorientation returns whether the square is on top, bottom
    // left etc by returning the magnitude of the face (the actual index / 9)
    public int getFaceOrientation(int color) {
        return this.getFaces().indexOf(color) / 9;
    }

    public int getCoorespondingEdgePiece(int color) {
        for (EdgeCubie cubie : edgeCubies) {
            if (cubie.index1 == color)
                return cubie.index2;
            if (cubie.index2 == color)
                return cubie.index1;
        }
        return -1;
    }

    public int[] getCoorespondingCorners(int color) {
        for (CornerCubie cubie : cornerCubies) {
            if (cubie.index1 == color) {
                int[] result = { cubie.index2, cubie.index3 };
                return result;
            } else if (cubie.index2 == color) {
                int[] result = { cubie.index1, cubie.index3 };
                return result;
            } else if (cubie.index3 == color) {
                int[] result = { cubie.index1, cubie.index2 };
                return result;
            }

        }
        int[] falseArray = { -1, -1 };
        return falseArray;
    }

    // Takes in a readable list of moves (classic rubik's cube notation U R T T' U'
    // etc.) executes those moves
    public void moveSequence(ArrayList<String> moves) {
        for (String move : moves) {
            if (move.equals("R")) {
                this.rotateSide(1, this.getDimension() - 1);
            } else if (move.equals("R'")) {
                this.rotateSide(-1, this.getDimension() - 1);
            } else if (move.equals("L")) {

                // rotate Side rotates clockwise with respect to the right face, you have to
                // switch 1 with -1 (see B)
                this.rotateSide(-1, 0);
            } else if (move.equals("L'")) {
                this.rotateSide(1, 0);
            } else if (move.equals("U")) {
                this.rotateLayer(1, 0);
            } else if (move.equals("U'")) {
                this.rotateLayer(-1, 0);
            } else if (move.equals("B")) {

                // When rotating the back, rotate FrontFace assumes you are still looking at the
                // cube
                // All major cube algorithms turn back clockwise when you look at back,
                // rotateFrontFace
                // always turns clockwise if you're looking at it, this I switched 1 with -1
                this.rotateFrontFace(-1, 0);
            } else if (move.equals("B'")) {
                this.rotateFrontFace(1, 0);
            } else if (move.equals("F")) {
                this.rotateFrontFace(1, this.getDimension() - 1);
            } else if (move.equals("F'")) {
                this.rotateFrontFace(-1, this.getDimension() - 1);
            } else if (move.equals("D")) {
                this.rotateLayer(1, this.getDimension() - 1);
            } else if (move.equals("D'")) {
                this.rotateLayer(-1, this.getDimension() - 1);
            }
        }
    }

    // The following rotate the entire cube (so that the cube orientation changes)
    public void rotateCubeClockwise() {
        for (int i = 0; i < this.getDimension(); i++) {
            this.rotateLayer(1, i);
        }
    }

    public void rotateCubeCounterClockwise() {
        for (int i = 0; i < this.getDimension(); i++) {
            this.rotateLayer(-1, i);
        }
    }

    public void rotateCubeDown() {
        for (int i = 0; i < this.getDimension(); i++) {
            this.rotateSide(-1, i);
        }
    }

    public void rotateCubeUp() {
        for (int i = 0; i < this.getDimension(); i++) {
            this.rotateSide(1, i);
        }
    }

    // Puts the above together tangibly
    // orients the cube just so that the colorfacefront is in front
    public void orientCube(int colorFaceFront) {
        // If desired face is on top, rotate whole cube down towards you
        // if desired face is on bottom, rotate whole cube up towards you
        // If desired face is on right rotate whole cube clockwise
        // If desired face is on left rotate whole cube counterclockwise
        // If desired face is on back rotate cube twice (clockwise in this case)
        int face = this.getFaceOrientation(colorFaceFront);
        switch (face) {
        case 0:
            this.rotateCubeDown();
            this.rotateCubeDown();
            break;
        case 1:
            this.rotateCubeCounterClockwise();
            break;
        case 2:
            this.rotateCubeDown();
            break;
        case 3:
            this.rotateCubeClockwise();
            break;
        case 4:
            this.rotateCubeUp();
        }

    }

    // Orients the cube so that facefront is the front face, face top is the top
    // face and facebottom is the bottom face color
    public void orientCube(int colorFaceFront, int colorFaceTop) {
        // If impossible orientation, only puts color face top on top
        // (ifnorescolorfacefront)
        switch (this.getFaceOrientation(colorFaceTop)) {
        case 0:
            this.rotateCubeDown();
        case 1:
            this.rotateCubeCounterClockwise();
            this.rotateCubeUp();
        case 3:
            this.rotateCubeClockwise();
            this.rotateCubeUp();
        case 4:
            this.rotateCubeClockwise();
            this.rotateCubeClockwise();
        case 5:
            this.rotateCubeUp();
        }

        switch (this.getFaceOrientation(colorFaceFront)) {
        case 0:
            this.rotateCubeClockwise();
            this.rotateCubeClockwise();
        case 1:
            this.rotateCubeCounterClockwise();
        case 3:
            this.rotateCubeClockwise();
        }

    }




    public void solve(){
        
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

    public static void test(){
        //shows the cube being sovled

        Cube3x3 myCube3x3 = new Cube3x3();
        //print the origional cube
        myCube3x3.printCube();

        //scramble it 1000000 times
        myCube3x3.scrambleCube(1000000);

        //print the scrambled cube
        myCube3x3.printCube();

        //solve the scrambled cube
        myCube3x3.solve();

        //print the solved cube
        myCube3x3.printCube();
    }

    public static void main(String[] args) {
        test();
    }

}