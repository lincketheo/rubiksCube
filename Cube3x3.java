
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
            for(int i = 0 ; i < 3; i++){
                if(colors.get(i) != testingColors.get(i)){
                    match = false;
                    break;
                }
            }
            if(match){
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
        } else if(cornerCubie.index2 / base == topFaceColor) {
            colorTopCornerCubie = cornerCubie.index2;
        }else{
            colorTopCornerCubie = cornerCubie.index3;
        }

        int cubieBackTopFace = this.getFaceOrientation(colorTopCornerCubie);
        // cubie is located on the back face
        // -------------------------------------------------------------
        if (cubieBackTopFace == 0) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (this.getDimension() - 1)) {
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);

            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base + 1) - 1) {
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
            } 

            // bottom left corner
            else {
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
        // Cubie is on left face
        // -------------------------------------------------------------------------
        else if  (cubieBackTopFace == 1) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (this.getDimension() - 1)) {
                this.rotateSide(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(-1, 0);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);

            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base + 1) - 1) {
                this.rotateSide(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);

            } 

            // bottom left corner
            else {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);

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
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
            }
            // if cubie is on the "right" edge
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (this.getDimension() - 1)) {
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
            }
            // if cubie is on the "left edge"
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base + 1) - 1) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
            } else {
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1,  this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
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
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
                
            }

            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base + 1) - 1) {
                this.rotateSide(-1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
            }   
            
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base) + ((this.getDimension())*(this.getDimension() - 1))) {
                this.rotateSide(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);

            }

            // this.printCubeNums();
        }
        else if (cubieBackTopFace == 5) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateFrontFace(-1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (this.getDimension() - 1)) {
                this.rotateFrontFace(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);

            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base + 1) - 1) {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateSide(-1, this.getDimension() - 1);
            } 

            // bottom left corner
            else {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
        else if (cubieBackTopFace == 4) {
            // Top left corner
            if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base) {
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
            }
            // Top right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * base + (this.getDimension() - 1)) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);

            }
            // bottom right corner
            else if (this.getFaces().indexOf(colorTopCornerCubie) == cubieBackTopFace * (base + 1) - 1) {
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);
            } 

            // bottom left corner
            else {
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1, 0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1, 0);
                this.rotateLayer(-1, this.getDimension() - 1);
                this.rotateFrontFace(1,0);
                this.rotateLayer(1, this.getDimension() - 1);
                this.rotateFrontFace(-1,0);

            }
            // else, cubie is bordering top and middle
            // this.printCubeNums();

        }
    }


    public void solveCorners(){
        for(int i = 0; i < 4; i ++){
            this.solveTopBackRightCornerPiece();
            this.rotateCubeClockwise();
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

    public static void main(String[] args) {
        
        Cube3x3 myCube3x3 = new Cube3x3();
        myCube3x3.scrambleCube(100);
        myCube3x3.printCube();
        myCube3x3.solveCross();
        myCube3x3.printCube();
        myCube3x3.solveTopBackRightCornerPiece();
        myCube3x3.printCube();
        


    }

}