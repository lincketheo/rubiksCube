import java.util.*;

public class Cube {
  /*
   * Author: theolincke.com
   *
   * Date: 2018-12-14
   *
   * Description
   *
   * This is a rubiks cube class A cube is a 1D array, so all opperations are done
   * to the cube by switching array elements This class does not solve the cube,
   * it only turns faces and fixes the 1D array to coorespond with a certain move
   * This is essentially a "tools" class
   */

  private ArrayList<EdgeCubie> edgeCubies;
  private ArrayList<CornerCubie> cornerCubies;
  // Faces is a one dimentional array (Woah! a 3D cube is represented in 1D?)
  private ArrayList<Integer> faces;
  // a 3x3 cube has dimension 3
  private int dimension;
  private ArrayList<Move> algorithm = new ArrayList<>();
  public static ArrayList<String> algorithm3x3 = new ArrayList<>();


  // creates a new cube of _dimension
  public Cube(int _dimension) {
    dimension = _dimension;
    int base = dimension * dimension;
    faces = new ArrayList<Integer>();
    for (int i = 0; i < 6 * base; i++) {
      faces.add(i);
    }
    fillCornerCubies();
    fillEdgeCubies();
  }

  public void fillEdgeCubies() {
    edgeCubies = new ArrayList<>();

    for (int i = 1; i < dimension - 1; i++) {
      edgeCubies.add(new EdgeCubie(getPosition(0, i, 0), getPosition(1, 0, i)));
      edgeCubies.add(new EdgeCubie(getPosition(0, 0, i), getPosition(4, 0, dimension - 1 - i)));
      edgeCubies.add(new EdgeCubie(getPosition(0, i, dimension - 1), getPosition(3, 0, dimension - 1 - i)));
      edgeCubies.add(new EdgeCubie(getPosition(1, i, 0), getPosition(4, i, dimension - 1)));
      edgeCubies.add(new EdgeCubie(getPosition(1, dimension - 1, i), getPosition(5, dimension - 1 - i, 0)));
      edgeCubies.add(new EdgeCubie(getPosition(5, i, dimension - 1), getPosition(3, dimension - 1, i)));
      edgeCubies.add(new EdgeCubie(getPosition(5, dimension - 1, i), getPosition(4, dimension - 1, dimension - 1 - i)));
      edgeCubies.add(new EdgeCubie(getPosition(3, i, dimension - 1), getPosition(4, i, 0)));
      edgeCubies.add(new EdgeCubie(getPosition(1, i, dimension - 1), getPosition(2, i, 0)));
      edgeCubies.add(new EdgeCubie(getPosition(0, dimension - 1, i), getPosition(2, 0, i)));
      edgeCubies.add(new EdgeCubie(getPosition(2, dimension - 1, i), getPosition(5, 0, i)));
      edgeCubies.add(new EdgeCubie(getPosition(2, i, dimension - 1), getPosition(3, i, 0)));
    }
  }

  public void fillCornerCubies() {
    cornerCubies = new ArrayList<>();
    cornerCubies.add(new CornerCubie(getPosition(0, 0, 0), getPosition(1, 0, 0), getPosition(4, 0, dimension - 1)));
    cornerCubies
        .add(new CornerCubie(getPosition(0, 0, dimension - 1), getPosition(3, 0, dimension - 1), getPosition(4, 0, 0)));
    cornerCubies
        .add(new CornerCubie(getPosition(0, dimension - 1, 0), getPosition(1, 0, dimension - 1), getPosition(2, 0, 0)));
    cornerCubies.add(new CornerCubie(getPosition(0, dimension - 1, dimension - 1), getPosition(2, 0, dimension - 1),
        getPosition(3, 0, 0)));
    cornerCubies.add(new CornerCubie(getPosition(2, dimension - 1, 0), getPosition(1, dimension - 1, dimension - 1),
        getPosition(5, 0, 0)));
    cornerCubies.add(new CornerCubie(getPosition(2, dimension - 1, dimension - 1), getPosition(3, dimension - 1, 0),
        getPosition(5, 0, dimension - 1)));
    cornerCubies.add(new CornerCubie(getPosition(5, dimension - 1, 0), getPosition(1, dimension - 1, 0),
        getPosition(4, dimension - 1, dimension - 1)));
    cornerCubies.add(new CornerCubie(getPosition(5, dimension - 1, dimension - 1),
        getPosition(3, dimension - 1, dimension - 1), getPosition(4, dimension - 1, 0)));

  }

  // returns faces (an array list)
  public ArrayList<Integer> getFaces() {
    return faces;
  }

  public ArrayList<EdgeCubie> getEdgeCubie(int color1, int color2) {
    int base = dimension * dimension;
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
    int base = dimension * dimension;
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

  // Returns dimension
  public int getDimension() {
    return dimension;
  }

  // The general formula that returns the index in the 1d array that cooresponds
  // to
  // the coordinate triplet (face, row, col)
  public int getPosition(int face, int row, int index) {
    return dimension * dimension * face + dimension * row + index;
  }

  // rotates a given face (does not change the cubies around so this is not a
  // valid move, it helps the other algorithms)

  // transposes and flips horizontally an n x n matrix being represented by a 1 x
  // n^2 vector
  public void rotateFullFace(int direction, int face) {
    int[] temp1 = new int[dimension * dimension];
    int[] temp2 = new int[dimension * dimension];
    int starter = getPosition(face, 0, 0);

    for (int i = 0; i < dimension * dimension; i++) {
      temp1[i] = faces.get(starter + i);
    }

    for (int i = 0; i < temp1.length; i++) {

      temp2[(i % dimension) * dimension + (dimension - 1 - (i / (dimension)))] = temp1[i];

    }

    if (direction == 1) {
      for (int i = 0; i < temp1.length; i++) {
        faces.set(starter + i, temp2[i]);
      }
    } else {
      int counter = 0;
      for (int i = temp2.length - 1; i >= 0; i--) {
        faces.set(starter + counter, temp2[i]);
        counter++;
      }
    }
  }

  /******************************************************************************
   * BASIC TOOLS - ROTATE FACES
   ********************************************************************/

  // colNum 0 is left face colNum dimension - 1 is right face
  // always rotates clockwise where right face is in front (unlike the standard R
  // vs L notation in rubiks cube algorithms)
  public void rotateSide(int direction, int colNum) {

    if (colNum == 0) {
      rotateFullFace(-direction, 1);
      algorithm3x3.add("L" + ((direction == 1) ? "'" : ""));
    } else if (colNum == dimension - 1) {
      rotateFullFace(direction, 3);
      algorithm3x3.add("L" + ((direction == -1) ? "'" : ""));

    }

    if (direction == 1) {
      for (int i = 0; i < dimension; i++) {
        int temp0 = faces.get(getPosition(0, i, colNum));
        int temp2 = faces.get(getPosition(2, i, colNum));
        int temp4 = faces.get(getPosition(4, dimension - i - 1, dimension - colNum - 1));
        int temp5 = faces.get(getPosition(5, i, colNum));
        faces.set(getPosition(2, i, colNum), temp5);
        faces.set(getPosition(0, i, colNum), temp2);
        faces.set(getPosition(4, dimension - i - 1, dimension - colNum - 1), temp0);
        faces.set(getPosition(5, i, colNum), temp4);
      }
      algorithm.add(new Move("Y", colNum));
    } else {
      for (int i = 0; i < dimension; i++) {
        int temp0 = faces.get(getPosition(0, i, colNum));
        int temp2 = faces.get(getPosition(2, i, colNum));
        int temp4 = faces.get(getPosition(4, dimension - i - 1, dimension - colNum - 1));
        int temp5 = faces.get(getPosition(5, i, colNum));
        faces.set(getPosition(5, i, colNum), temp2);
        faces.set(getPosition(2, i, colNum), temp0);
        faces.set(getPosition(0, i, colNum), temp4);
        faces.set(getPosition(4, dimension - i - 1, dimension - colNum - 1), temp5);
      }
      algorithm.add(new Move("Y'", colNum));

    }
  }

  // Layer 0 is top layer layer dimension - 1 is bottom
  // Always rotates clockwise where top is facing you
  public void rotateLayer(int direction, int layerNum) {
    if (layerNum == 0) {
      rotateFullFace(direction, 2);
      algorithm3x3.add("T" + ((direction == -1) ? "'" : ""));
    } else if (layerNum == dimension - 1) {
      rotateFullFace(-direction, 4);
      algorithm3x3.add("U" + ((direction == -1) ? "'" : ""));
    }

    if (direction == 1) {
      for (int i = 0; i < dimension; i++) {
        int temp0 = faces.get(getPosition(0, dimension - layerNum - 1, i));
        int temp3 = faces.get(getPosition(3, i, layerNum));
        int temp5 = faces.get(getPosition(5, layerNum, dimension - i - 1));
        int temp1 = faces.get(getPosition(1, dimension - i - 1, dimension - layerNum - 1));

        faces.set(getPosition(3, i, layerNum), temp0);
        faces.set(getPosition(5, layerNum, dimension - i - 1), temp3);
        faces.set(getPosition(1, dimension - i - 1, dimension - layerNum - 1), temp5);
        faces.set(getPosition(0, dimension - layerNum - 1, i), temp1);
      }
      algorithm.add(new Move("X", layerNum));
    } else {
      for (int i = 0; i < dimension; i++) {
        int temp0 = faces.get(getPosition(0, dimension - layerNum - 1, i));
        int temp3 = faces.get(getPosition(3, i, layerNum));
        int temp5 = faces.get(getPosition(5, layerNum, dimension - i - 1));
        int temp1 = faces.get(getPosition(1, dimension - i - 1, dimension - layerNum - 1));

        faces.set(getPosition(3, i, layerNum), temp5);
        faces.set(getPosition(5, layerNum, dimension - i - 1), temp1);
        faces.set(getPosition(1, dimension - i - 1, dimension - layerNum - 1), temp0);
        faces.set(getPosition(0, dimension - layerNum - 1, i), temp3);
      }
      algorithm.add(new Move("X'", layerNum));

    }

  }

  // faceNum 0 is the back faceNym dimension - 1 is front
  // always rotates face where front is facing you
  public void rotateFrontFace(int direction, int faceNum) {
    if (faceNum == 0) {
      rotateFullFace(-direction, 0);
      algorithm3x3.add("B" + ((direction == 1) ? "'" : ""));
    } else if (faceNum == dimension - 1) {
      rotateFullFace(direction, 5);
      algorithm3x3.add("F" + ((direction == -1) ? "'" : ""));
    }
    int temp1;
    int temp2;
    int temp3;
    int temp4;
    if (direction == 1) {
      temp1 = faces.get(getPosition(1, faceNum, 0));
      for (int x = 0; x < dimension; x++) {
        temp1 = faces.get(getPosition(1, faceNum, x));
        temp2 = faces.get(getPosition(2, faceNum, x));
        temp3 = faces.get(getPosition(3, faceNum, x));
        temp4 = faces.get(getPosition(4, faceNum, x));

        faces.set(getPosition(1, faceNum, x), temp4);
        faces.set(getPosition(2, faceNum, x), temp1);
        faces.set(getPosition(3, faceNum, x), temp2);
        faces.set(getPosition(4, faceNum, x), temp3);

      }
      algorithm.add(new Move("Z", faceNum));
    } else {
      for (int x = 0; x < dimension; x++) {
        temp1 = faces.get(getPosition(1, faceNum, x));
        temp2 = faces.get(getPosition(2, faceNum, x));
        temp3 = faces.get(getPosition(3, faceNum, x));
        temp4 = faces.get(getPosition(4, faceNum, x));

        faces.set(getPosition(4, faceNum, x), temp1);
        faces.set(getPosition(1, faceNum, x), temp2);
        faces.set(getPosition(2, faceNum, x), temp3);
        faces.set(getPosition(3, faceNum, x), temp4);
      }
      algorithm.add(new Move("Z'", faceNum));

    }

  }

  /****************************************************************************
   * COMPLEX TOOLS - ORIENTS THE CUBE
   ****************************************************************/

  // The following rotate the entire cube (so that the cube orientation changes)
  public void rotateCubeClockwise() {
    for (int i = 0; i < dimension; i++) {
      rotateLayer(1, i);
    }
  }

  public void rotateCubeCounterClockwise() {
    for (int i = 0; i < dimension; i++) {
      rotateLayer(-1, i);
    }
  }

  public void rotateCubeDown() {
    for (int i = 0; i < dimension; i++) {
      rotateSide(-1, i);
    }
  }

  public void rotateCubeUp() {
    for (int i = 0; i < dimension; i++) {
      rotateSide(1, i);
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
    int face = getFaceOrientation(colorFaceFront);
    switch (face) {
    case 0:
      rotateCubeDown();
      rotateCubeDown();
      break;
    case 1:
      rotateCubeCounterClockwise();
      break;
    case 2:
      rotateCubeDown();
      break;
    case 3:
      rotateCubeClockwise();
      break;
    case 4:
      rotateCubeUp();
    }

  }

  // Orients the cube so that facefront is the front face, face top is the top
  // face and facebottom is the bottom face color
  public void orientCube(int colorFaceFront, int colorFaceTop) {
    // If impossible orientation, only puts color face top on top
    // (ifnorescolorfacefront)
    switch (getFaceOrientation(colorFaceTop)) {
    case 0:
      rotateCubeDown();
    case 1:
      rotateCubeCounterClockwise();
      rotateCubeUp();
    case 3:
      rotateCubeClockwise();
      rotateCubeUp();
    case 4:
      rotateCubeClockwise();
      rotateCubeClockwise();
    case 5:
      rotateCubeUp();
    }

    switch (getFaceOrientation(colorFaceFront)) {
    case 0:
      rotateCubeClockwise();
      rotateCubeClockwise();
    case 1:
      rotateCubeCounterClockwise();
    case 3:
      rotateCubeClockwise();
    }

  }

  /****************************************************************************
   * FINDER TOOLS - LOCATE PIECES
   ****************************************************************/

  // takes in a specific value somewhere in the array and returns which face that
  // piece is on (0 - 5)
  public int getFaceOrientation(int color) {
    int base = dimension * dimension;
    return faces.indexOf(color) / base;
  }

  // Takes in a number as an edgepiece (a specific number in the array) returns
  // the cooresponding edge piece (all edge cubies are two edgepieces) returns -1
  // if corner or invalid
  public int getCoorespondingEdgePiece(int color) {
    for (EdgeCubie cubie : edgeCubies) {
      if (cubie.index1 == color)
        return cubie.index2;
      if (cubie.index2 == color)
        return cubie.index1;
    }
    return -1;
  }

  // Same as above, but returns and int array of the cooresponding corner pieces
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

  /****************************************************************************
   * PUTTING EVERYTHING TOGETHER - PRACTICAL
   ****************************************************************/
  // Takes in a readable list of moves (classic rubik's cube notation U R T T' U'
  // etc.) executes those moves
  private void moveSequence(ArrayList<Move> moves) {
    for (Move move : moves) {
      if (move.main.equals("X")) {
        rotateLayer(1, move.layerNo);
      }else if(move.main.equals("X'")){
        rotateLayer(1, move.layerNo);
      }else if(move.main.equals("Y")){
        rotateSide(1, move.layerNo);
      }else if(move.main.equals("Y'")){
        rotateSide(-1, move.layerNo);
      }else if(move.main.equals("Z")){
        rotateFrontFace(1, move.layerNo);
      }else if(move.main.equals("Z'")){
        rotateFrontFace(-1, move.layerNo);
      }
    }
  }

  private void moveSequence(Move move) {

      if (move.main.equals("X")) {
        rotateLayer(1, move.layerNo);
      }else if(move.main.equals("X'")){
        rotateLayer(1, move.layerNo);
      }else if(move.main.equals("Y")){
        rotateSide(1, move.layerNo);
      }else if(move.main.equals("Y'")){
        rotateSide(-1, move.layerNo);
      }else if(move.main.equals("Z")){
        rotateFrontFace(1, move.layerNo);
      }else if(move.main.equals("Z'")){
        rotateFrontFace(-1, move.layerNo);
      }
    
  }

  /****************************************************************************
   * DEBUGGING
   ****************************************************************/

  /**
   * ------BACK
   * 
   * LEFT TOP RIGHT BOTTOM
   * 
   * ------FRONT
   */

  // Prints the cube with colors as letters (r g w y b o)
  public void printCube() {
    System.out.println("___________________________________________________________________");
    System.out.println();
    int base = dimension * dimension;
    ArrayList<String> colors = new ArrayList();
    for (int i = 0; i < faces.size(); i++) {
      switch (faces.get(i) / base) {
      case 0:
        colors.add("r");
        break;
      case 1:
        colors.add("g");
        break;
      case 2:
        colors.add("b");
        break;
      case 3:
        colors.add("w");
        break;
      case 4:
        colors.add("y");
        break;
      case 5:
        colors.add("o");
        break;
      }
    }

    int count = 0;
    for (int i = 0; i < dimension; i++) {
      for (int x = 0; x < dimension + 3; x++) {
        System.out.print("  ");
      }

      System.out.print("| ");
      for (int j = 0; j < dimension; j++) {
        System.out.print(colors.get(count) + " ");
        count++;
      }
      System.out.print("| ");
      System.out.println();
    }

    System.out.println();

    for (int i = 0; i < dimension; i++) {
      count = base + dimension * i;

      for (int j = 0; j < 4; j++) {

        System.out.print(" | ");
        for (int x = 0; x < dimension; x++) {
          System.out.print(colors.get(count + x) + " ");
        }
        System.out.print("| ");
        count = count + base;
      }
      System.out.println();
    }
    System.out.println();

    count = base * 5;
    for (int i = 0; i < dimension; i++) {
      for (int x = 0; x < dimension + 3; x++) {
        System.out.print("  ");
      }

      System.out.print("| ");
      for (int j = 0; j < dimension; j++) {
        System.out.print(colors.get(count) + " ");
        count++;
      }
      System.out.print("| ");
      System.out.println();
    }
    System.out.println("___________________________________________________________________");

  }

  // prints cube in square format (the numbers) cross format
  public void printCubeNums() {
    System.out.println("___________________________________________________________________");
    System.out.println();
    int base = dimension * dimension;

    int count = 0;
    for (int i = 0; i < dimension; i++) {
      for (int x = 0; x < dimension + 2; x++) {
        System.out.print("   ");
      }

      System.out.print("| ");
      for (int j = 0; j < dimension; j++) {
        int a = faces.get(count);
        if (a < 10) {
          System.out.print("0" + a + " ");
        } else {
          System.out.print(a + " ");
        }
        count++;
      }
      System.out.print("| ");
      System.out.println();
    }

    System.out.println();

    for (int i = 0; i < dimension; i++) {
      count = base + dimension * i;

      for (int j = 0; j < 4; j++) {

        System.out.print(" | ");
        for (int x = 0; x < dimension; x++) {
          int a = faces.get(count + x);
          if (a < 10) {
            System.out.print("0" + a + " ");
          } else {
            System.out.print(a + " ");
          }
        }
        System.out.print("| ");
        count = count + base;
      }
      System.out.println();
    }
    System.out.println();

    count = base * 5;
    for (int i = 0; i < dimension; i++) {
      for (int x = 0; x < dimension + 2; x++) {
        System.out.print("   ");
      }

      System.out.print("| ");
      for (int j = 0; j < dimension; j++) {
        int a = faces.get(count);
        if (a < 10) {
          System.out.print("0" + a + " ");
        } else {
          System.out.print(a + " ");
        }
        count++;
      }
      System.out.print("| ");
      System.out.println();
    }
    System.out.println("___________________________________________________________________");
  }

  // A few testing methods - use these to showcase what every move does
  public static void testRotations() {

    // Change dimension to try different size cubes
    Cube testCube3 = new Cube(3);
    System.out.println("Origional Cube shown as array");
    System.out.println(testCube3.faces);

    System.out.println("Origional Cube");
    testCube3.printCube();
    testCube3.printCubeNums();
    // Change 1 to -1 to rotate counter clockwise

    System.out.println("Back Face Clockwise");
    // rotates back layer clockwise (when front is in front)
    testCube3.rotateFrontFace(1, 0);
    testCube3.printCubeNums();
    testCube3.rotateFrontFace(-1, 0);

    System.out.println("Front Face Clockwise");
    // rotates front face clockwise (when front is in front)
    testCube3.rotateFrontFace(1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();
    testCube3.rotateFrontFace(-1, testCube3.getDimension() - 1);

    System.out.println("Left Face Clockwise");
    // rotates left side clockwise (when right is in front)
    testCube3.rotateSide(1, 0);
    testCube3.printCubeNums();
    testCube3.rotateSide(-1, 0);

    System.out.println("Right Face Clockwise");
    // rotates right side clockwise (when right is in front)
    testCube3.rotateSide(1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();
    testCube3.rotateSide(-1, testCube3.getDimension() - 1);

    System.out.println("Top Layer Clockwise");
    // rotates top layer clockwise (when looking down on the cube)
    testCube3.rotateLayer(1, 0);
    testCube3.printCubeNums();
    testCube3.rotateLayer(-1, 0);

    System.out.println("Bottom Layer Clockwise");
    // rotates bottom layer clockwise (when looking down on the cube)
    testCube3.rotateLayer(1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();
    testCube3.rotateLayer(1, testCube3.getDimension() - 1);

  }

  public static void main(String[] args) {
    testRotations();
    Cube myCube = new Cube(3);
  }

}
