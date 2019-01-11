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

  //private final char[] COLORFACES = { 'b', 'o', 'w', 'r', 'y', 'g' };
  private final char[] COLORFACES = { '0', '1', '2', '3', '4', '5' };

  private ArrayList<EdgeCubie> edgeCubies;
  private ArrayList<CornerCubie> cornerCubies;

  // Faces is a one dimentional array (Woah! a 3D cube is represented in 1D?)
  private ArrayList<Integer> faces;
  // a 3x3 cube has dimension 3
  private int dimension;

  public ArrayList<Move> scrambleAlgorithm = new ArrayList<>();
  public ArrayList<Move> algorithm = new ArrayList<>();

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

  public Cube(Cubenxn cube){
    dimension = cube.getDimension();
    faces = cube.getFaces();
    edgeCubies = cube.getEdgeCubies();
    cornerCubies = cube.getCornerCubies();

  }

  public Cube(Cube3x3 cube){
    dimension = cube.getDimension();
    faces = cube.getFaces();
    edgeCubies = cube.getEdgeCubies();
    cornerCubies = cube.getCornerCubies();

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

  public ArrayList<CornerCubie> getCornerCubies() {
    return cornerCubies;
  }

  public ArrayList<EdgeCubie> getEdgeCubies() {
    return edgeCubies;
  }

  // returns an array list of edgecubies that match color1 and color2
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

  // returns what row a cubie is on
  public int getRow(int cubie) {
    int base = dimension * dimension;
    return (getFaces().indexOf(cubie) % base) / dimension;
  }

  // returns what col a cubie is on
  public int getCol(int cubie) {
    return getFaces().indexOf(cubie) % getDimension();
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
    } else if (colNum == dimension - 1) {
      rotateFullFace(direction, 3);

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
      algorithm.add(new Move("Y+", colNum));
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
      algorithm.add(new Move("Y-", colNum));

    }
  }

  // Layer 0 is top layer layer dimension - 1 is bottom
  // Always rotates clockwise where top is facing you
  public void rotateLayer(int direction, int layerNum) {
    if (layerNum == 0) {
      rotateFullFace(direction, 2);
    } else if (layerNum == dimension - 1) {
      rotateFullFace(-direction, 4);
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
      algorithm.add(new Move("X+", layerNum));
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
      algorithm.add(new Move("X-", layerNum));

    }

  }

  // faceNum 0 is the back faceNym dimension - 1 is front
  // always rotates face where front is facing you
  public void rotateFrontFace(int direction, int faceNum) {
    if (faceNum == 0) {
      rotateFullFace(-direction, 0);
    } else if (faceNum == dimension - 1) {
      rotateFullFace(direction, 5);
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
      algorithm.add(new Move("Z+", faceNum));
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
      algorithm.add(new Move("Z-", faceNum));

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

  // orients the cube so that the current position of the desired faceleft / face
  // front (they can be anywhere on the cube) become left and front
  public void orientCubeFrontLeft(int faceLeft, int faceFront) {

    // store a cubie so that we can find what face this cubie lands on after we
    // execute the first translation (all translations will change the orientation
    // of the second face)
    int tempCubie = getFaces().get(getPosition(faceFront, 1, 1));

    switch (faceLeft) {
    case 0:
      rotateCubeCounterClockwise();
      break;
    case 2:
      rotateCubeDown();
      rotateCubeClockwise();
      break;
    case 3:
      rotateCubeClockwise();
      rotateCubeClockwise();
      break;
    case 4:
      rotateCubeUp();
      rotateCubeClockwise();
      break;
    case 5:
      rotateCubeClockwise();
      break;
    }

    faceFront = getFaceOrientation(tempCubie);

    switch (faceFront) {
    case 0:
      rotateCubeUp();
      rotateCubeUp();
      break;
    case 2:
      rotateCubeDown();
      break;
    case 4:
      rotateCubeUp();
      break;
    }

  }

  // Orients the cube so that facefront is the front face, face top is the top
  // face and facebottom is the bottom face color

  public void orientCube(int colorFaceFront, int colorFaceTop) {
    // If impossible orientation, only puts color face top on top
    if (dimension % 2 == 0) {
      return;
    }

    int cubieFaceTop = getCenterCubie(colorFaceTop);

    switch (getFaceOrientation(cubieFaceTop)) {
    case 0:
      rotateCubeDown();
      break;
    case 1:
      rotateCubeCounterClockwise();
      rotateCubeUp();
      break;
    case 3:
      rotateCubeClockwise();
      rotateCubeUp();
      break;
    case 4:
      rotateCubeUp();
      rotateCubeUp();
      break;
    case 5:
      rotateCubeUp();
      break;
    }

    int cubieFaceFront = getCenterCubie(colorFaceFront);

    switch (getFaceOrientation(cubieFaceFront)) {
    case 0:
      rotateCubeClockwise();
      rotateCubeClockwise();
      break;
    case 1:
      rotateCubeCounterClockwise();
      break;
    case 3:
      rotateCubeClockwise();
      break;
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

  public int getCenterCubie(int color) {
    if (color > 5 || color < 0 || getDimension() % 2 == 0) {
      return -1;
    }
    int base = dimension * dimension;
    for (int i = 0; i < 6; i++) {
      if (faces.get(getPosition(i, getDimension() / 2, getDimension() / 2)) / base == color)
        return faces.get(getPosition(i, getDimension() / 2, getDimension() / 2));
    }
    return -1;
  }

  // Only works on odd dimensions

  public int getColor(int face) {
    if (dimension % 2 == 0) {
      return -1;
    }
    return faces.get(getPosition(face, dimension / 2, dimension / 2)) / (dimension * dimension);
  }

  /****************************************************************************
   * PUTTING EVERYTHING TOGETHER - PRACTICAL
   ****************************************************************/
  // Takes in a readable list of moves (classic rubik's cube notation U R T T' U'
  // etc.) executes those moves
  public void moveSequenceNxN(ArrayList<Move> moves) {
    for (Move move : moves) {
      if (move.main.equals("X")) {
        rotateLayer(1, move.layerNo);
      } else if (move.main.equals("X'")) {
        rotateLayer(1, move.layerNo);
      } else if (move.main.equals("Y")) {
        rotateSide(1, move.layerNo);
      } else if (move.main.equals("Y'")) {
        rotateSide(-1, move.layerNo);
      } else if (move.main.equals("Z")) {
        rotateFrontFace(1, move.layerNo);
      } else if (move.main.equals("Z'")) {
        rotateFrontFace(-1, move.layerNo);
      }
    }
  }

  public void moveSequenceNxN(Move move) {

    if (move.main.equals("X")) {
      rotateLayer(1, move.layerNo);
    } else if (move.main.equals("X'")) {
      rotateLayer(1, move.layerNo);
    } else if (move.main.equals("Y")) {
      rotateSide(1, move.layerNo);
    } else if (move.main.equals("Y'")) {
      rotateSide(-1, move.layerNo);
    } else if (move.main.equals("Z")) {
      rotateFrontFace(1, move.layerNo);
    } else if (move.main.equals("Z'")) {
      rotateFrontFace(-1, move.layerNo);
    }

  }

  public void generateScramble(int moves) {
    int slice;
    int major;
    int direction;
    String majorStr = "";
    for (int i = 0; i < moves; i++) {
      slice = (int) (Math.random() * dimension);
      major = (int) (Math.random() * 3);
      direction = (int) (Math.random() * 2);
      switch (major) {
      case 0:
        majorStr = "X";
        break;
      case 1:
        majorStr = "Y";
        break;
      case 2:
        majorStr = "Z";
        break;
      }
      if(direction == 1){
        majorStr += "+";
      }else{
        majorStr += "-";
      }
      scrambleAlgorithm.add(new Move(majorStr, slice));
    }
    algorithm.clear();
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
      colors.add(String.valueOf(COLORFACES[faces.get(i) / base]));

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

    //testRotations();
    Cubenxn myCube = new Cubenxn(3);
    
    
    myCube.scrambleNxN(1000);
    myCube.printCube();

    myCube.reduceCubeto3x3();
    myCube.printCube();
    Cube3x3 myCube3x3 = new Cube3x3(myCube);
    myCube3x3.printCube();
    myCube3x3.solve();
    myCube3x3.printCube();
    /*
    for(int i = 0; i < 30; i++){
      myCube = new Cubenxn(8);
      myCube.scrambleNxN(100);
      myCube.reduceCubeto3x3();
      myCube3x3 = new Cube3x3(myCube);
      myCube3x3.solve();
      myCube3x3.printCube();
    }
    */
    
  }

}
