
import java.util.ArrayList;

public class Cube3x3 extends Cube {
    //Does not solve 2x2
    private Cube cube;

    public Cube3x3() {
        super(3);
    }

    //Begins the top cross, puts a 
    //I could make a seperate cubie class but that's a lot of extra that I don't really need
    public void moveEdgePieceNo1



























































    //---------------------------------------------TOOLS----------------------------------------
    //Generally, I'm not using strings to represent colors unless debugging. The program knows that a color is returned from the number / base (as ints)
    //To find a color, just integer divide the number by the (dimension ^ 2)

    //takes in any cube on the desired face (doesn't matter what color the cube is, returns what face that cube is on)
    public int getFaceColor(int color){
        // find index of the element color
        int valuesIndex = this.getFaces().indexOf(color);
        // find the value of values index / base (dim ^ 2) this is the "sudoFace" the cube is on, but faces change a lot, so we need to compare what the face is compared to the center piece
        int sudoFace = valuesIndex / (this.getDimension() * this.getDimension());
        // (by center piece, I've just done dim + 2, any cube's (face, 1, 1) pos will be the color of the face if the center piece is solved
        return this.getFaces().get(getPosition(sudoFace, 1, 1)) / (this.getDimension() * this.getDimension());
    }
    //wheareas getfacecolor represents the color (not face in getposition(face, row, col)) getfaceorientation returns whether the square is on top, bottom left etc by returning the magnitude of the face (the actual index / 9)
    public int getFaceOrientation(int color){
        return this.getFaces().indexOf(color) / 9;
    }


    //Takes in a readable list of moves (classic rubik's cube notation U R T T' U' etc.) executes those moves
    public void moveSequence(ArrayList<String> moves) {
        for (String move : moves) {
            if (move.equals("R")) {
                this.rotateSide(1, this.getDimension() - 1);
            } else if (move.equals("R'")) {
                this.rotateSide(-1, this.getDimension() - 1);
            } else if (move.equals("L")) {
                
                //rotate Side rotates clockwise with respect to the right face, you have to
                //switch 1 with -1 (see B)
                this.rotateSide(-1, 0);
            } else if (move.equals("L'")) {
                this.rotateSide(1, 0);
            } else if (move.equals("U")) {
                this.rotateLayer(1, 0);
            } else if (move.equals("U'")) {
                this.rotateLayer(-1, 0);
            } else if (move.equals("B")) {

                //When rotating the back, rotate FrontFace assumes you are still looking at the cube
                //All major cube algorithms turn back clockwise when you look at back, rotateFrontFace
                //always turns clockwise if you're looking at it, this I switched 1 with -1
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

    //The following rotate the entire cube (so that the cube orientation changes)
    public void rotateCubeClockwise(){
        for(int i = 0; i < this.getDimension(); i++){
            this.rotateLayer(1, i);
        }
    }
    public void rotateCubeCounterClockwise(){
        for(int i = 0; i < this.getDimension(); i++){
            this.rotateLayer(-1, i);
        }
    }
    public void rotateCubeDown(){
        for(int i = 0; i < this.getDimension(); i++){
            this.rotateSide(-1, i);
        }
    }
    public void rotateCubeUp(){
        for(int i = 0; i < this.getDimension(); i++){
            this.rotateSide(1, i);
        }
    }

    //Puts the above together tangibly
    //orients the cube just so that the colorfacefront is in front
    public void orientCube(int colorFaceFront){
        //If desired face is on top, rotate whole cube down towards you
        //if desired face is on bottom, rotate whole cube up towards you
        //If desired face is on right rotate whole cube clockwise
        //If desired face is on left rotate whole cube counterclockwise
        //If desired face is on back rotate cube twice (clockwise in this case)
        int face = this.getFaceOrientation(colorFaceFront);
        switch(face){
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
    //Orients the cube so that facefront is the front face, face top is the top face and facebottom is the bottom face color
    public void orientCube(int colorFaceFront, int colorFaceTop){
        //If impossible orientation, only puts color face top on top (ifnorescolorfacefront)
        switch(this.getFaceOrientation(colorFaceTop)){
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

        switch(this.getFaceOrientation(colorFaceFront)){
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
        Cube3x3 myCube = new Cube3x3();
        ArrayList<String> algorithm = new ArrayList<String>();
        myCube.printCubeNums();
        myCube.orientCube(2, 9);
        myCube.printCubeNums();
        //21 = 
        System.out.print(myCube.getFaceOrientation(1));


    }

}