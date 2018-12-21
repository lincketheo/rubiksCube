import java.util.Random;
import java.util.ArrayList;

public class Cube {
  /*Author: theolincke.com
  *
  * Date: 2018-12-14
  *
  * Description
  *
  * This is a rubiks cube class
  * A cube is a 1D array, so all opperations are done to the cube by switching array elements
  * This class does not solve the cube, it only turns faces and fixes the 1D array to coorespond with
  * a certain move 
  *
  */

  //Faces is a one dimentional array (Woah! a 3D cube is represented in 1D?)
  private ArrayList<Integer> faces;
  private int dimension;

  //creates a new cube of _dimension
  public Cube(int _dimension) {
    dimension = _dimension;
    int base = dimension * dimension;
    this.faces = new ArrayList<Integer>();
    for (int i = 0; i < 6 * base; i++) {
      this.faces.add(i);
    }
  }

  //returns faces (an array list)
  public ArrayList<Integer> getFaces() {
    return faces;
  }

  //Returns dimension
  public int getDimension() {
    return this.dimension;
  }

  // The genera formula that returns the index in the 1d array that cooresponds to
  // the coordinate triplet (face, row, col)
  public int getPosition(int face, int row, int index) {
    return dimension * dimension * face + dimension * row + index;
  }

  // rotates a given face (does not change the cubies around
  // it, only one face so this can't be used alone, it is a helper for the next
  // three)
  public void rotateFullFace(int direction, int face) {
    int[] temp1 = new int[dimension * dimension];
    int[] temp2 = new int[dimension * dimension];
    int starter = getPosition(face, 0, 0);

    for (int i = 0; i < dimension * dimension; i++) {
      temp1[i] = this.faces.get(starter + i);
    }

    for (int i = 0; i < temp1.length; i++) {

      //To anybody reading along, this line of code is a damn piece of art
      //It rotates a "square" face. Some pretty cool math here
      temp2[(i % dimension) * dimension + (dimension - 1 - (i / (dimension)))] = temp1[i];

    }

    if (direction == 1) {
      for (int i = 0; i < temp1.length; i++) {
        this.faces.set(starter + i, temp2[i]);
      }
    } else {
      int counter = 0;
      for (int i = temp2.length - 1; i >= 0; i--) {
        this.faces.set(starter + counter, temp2[i]);
        counter++;
      }
    }
  }

  // Rotate any of the collumns (imagine cube is facing you, rotate the face away
  // or towards you (collumn))
  public void rotateSide(int direction, int colNum) {

    if (colNum == 0) {
      rotateFullFace(-direction, 1);
    } else if (colNum == dimension - 1) {
      rotateFullFace(direction, 3);
    }

    if (direction == 1) {
      for (int i = 0; i < dimension; i++) {
        int temp0 = this.faces.get(getPosition(0, i, colNum));
        int temp2 = this.faces.get(getPosition(2, i, colNum));
        int temp4 = this.faces.get(getPosition(4, dimension - i - 1, dimension - colNum - 1));
        int temp5 = this.faces.get(getPosition(5, i, colNum));
        this.faces.set(getPosition(2, i, colNum), temp5);
        this.faces.set(getPosition(0, i, colNum), temp2);
        this.faces.set(getPosition(4, dimension - i - 1, dimension - colNum - 1), temp0);
        this.faces.set(getPosition(5, i, colNum), temp4);
      }

    } else {
      for (int i = 0; i < dimension; i++) {
        int temp0 = this.faces.get(getPosition(0, i, colNum));
        int temp2 = this.faces.get(getPosition(2, i, colNum));
        int temp4 = this.faces.get(getPosition(4, dimension - i - 1, dimension - colNum - 1));
        int temp5 = this.faces.get(getPosition(5, i, colNum));
        this.faces.set(getPosition(5, i, colNum), temp2);
        this.faces.set(getPosition(2, i, colNum), temp0);
        this.faces.set(getPosition(0, i, colNum), temp4);
        this.faces.set(getPosition(4, dimension - i - 1, dimension - colNum - 1), temp5);
      }
    }
  }

  // Rotate any of the layers
  // layer 0 is the top
  public void rotateLayer(int direction, int layerNum) {
    if (layerNum == 0) {
      this.rotateFullFace(direction, 2);
    } else if (layerNum == dimension - 1) {
      this.rotateFullFace(-direction, 4);
    }

    if (direction == 1) {
      for (int i = 0; i < dimension; i++) {
        int temp0 = this.faces.get(getPosition(0, dimension - layerNum - 1, i));
        int temp3 = this.faces.get(getPosition(3, i, layerNum));
        int temp5 = this.faces.get(getPosition(5, layerNum, dimension - i - 1));
        int temp1 = this.faces.get(getPosition(1, dimension - i - 1, dimension - layerNum - 1));

        this.faces.set(getPosition(3, i, layerNum), temp0);
        this.faces.set(getPosition(5, layerNum, dimension - i - 1), temp3);
        this.faces.set(getPosition(1, dimension - i - 1, dimension - layerNum - 1), temp5);
        this.faces.set(getPosition(0, dimension - layerNum - 1, i), temp1);
      }
    } else {
      for (int i = 0; i < dimension; i++) {
        int temp0 = this.faces.get(getPosition(0, dimension - layerNum - 1, i));
        int temp3 = this.faces.get(getPosition(3, i, layerNum));
        int temp5 = this.faces.get(getPosition(5, layerNum, dimension - i - 1));
        int temp1 = this.faces.get(getPosition(1, dimension - i - 1, dimension - layerNum - 1));

        this.faces.set(getPosition(3, i, layerNum), temp5);
        this.faces.set(getPosition(5, layerNum, dimension - i - 1), temp1);
        this.faces.set(getPosition(1, dimension - i - 1, dimension - layerNum - 1), temp0);
        this.faces.set(getPosition(0, dimension - layerNum - 1, i), temp3);
      }

    }

  }

  // Rotate the face that is facing you
  public void rotateFrontFace(int direction, int faceNum) {
    if (faceNum == 0) {
      this.rotateFullFace(-direction, 0);
    } else if (faceNum == dimension - 1) {
      this.rotateFullFace(direction, 5);
    }
    int temp1;
    int temp2;
    int temp3;
    int temp4;
    if (direction == 1) {
      temp1 = this.faces.get(getPosition(1, faceNum, 0));
      for (int x = 0; x < dimension; x++) {
        temp1 = this.faces.get(getPosition(1, faceNum, x));
        temp2 = this.faces.get(getPosition(2, faceNum, x));
        temp3 = this.faces.get(getPosition(3, faceNum, x));
        temp4 = this.faces.get(getPosition(4, faceNum, x));

        this.faces.set(getPosition(1, faceNum, x), temp4);
        this.faces.set(getPosition(2, faceNum, x), temp1);
        this.faces.set(getPosition(3, faceNum, x), temp2);
        this.faces.set(getPosition(4, faceNum, x), temp3);

      }

    } else {
      for (int x = 0; x < dimension; x++) {
        temp1 = this.faces.get(getPosition(1, faceNum, x));
        temp2 = this.faces.get(getPosition(2, faceNum, x));
        temp3 = this.faces.get(getPosition(3, faceNum, x));
        temp4 = this.faces.get(getPosition(4, faceNum, x));

        this.faces.set(getPosition(4, faceNum, x), temp1);
        this.faces.set(getPosition(1, faceNum, x), temp2);
        this.faces.set(getPosition(2, faceNum, x), temp3);
        this.faces.set(getPosition(3, faceNum, x), temp4);
      }
    }

  }

  // picks a bunch of random numbers and does any of the three translations to the
  // cube (takes in the number of random moves you want to perform on it)
  public void scrambleCube(int moves) {
    for (int i = 0; i < moves; i++) {
      int j = (int) (Math.random() * 3);
      int q = (int) (Math.random() * (dimension - 1));
      double m = Math.random();
      int k;
      if (m < .50) {
        k = -1;
      } else {
        k = 1;
      }

      if (j == 0) {
        this.rotateFrontFace(k, q);
      } else if (j == 1) {
        this.rotateLayer(k, q);
      } else if (j == 2) {
        this.rotateSide(k, q);
      }

    }

  }

  // **************************DEBUGGING*************************************8

  // prints cube in square format (the colors) cross format
  public void printCube() {
    System.out.println("___________________________________________________________________");
    System.out.println();
    int base = dimension * dimension;
    ArrayList<String> colors = new ArrayList();
    for (int i = 0; i < this.faces.size(); i++) {
      switch (this.faces.get(i) / base) {
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

  // prints cube in square format (the numbers) cross format (these are the
  // "origional" indices)counterclockwise
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
        int a = this.faces.get(count);
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
          int a = this.faces.get(count + x);
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
        int a = this.faces.get(count);
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

  //For debugging, I've included a few lines commented out that can help visualize the cube.
  
  public static void main(String[] args) {
    Cube testCube3 = new Cube(3);
    //Cube testCube4 = new Cube(4);

    //Doesn't really show well as a cube, but it works just fine
    //Cube testCube100 = new Cube(100);

    //Unless I haven't fixed it, I didn't care to translate to a 1 cube, so it throws an error. This is probably a small fix
    //Cube testCube1 = new Cube(1);
    /*
    for(int i =0; i < testCube3.faces.size(); i++){
      System.out.print(testCube3.faces.get(i) + " ");
    }
    System.out.println();
    


    
    testCube3.printCube();
    testCube3.printCubeNums();
    
    System.out.println();
    testCube3.scrambleCube(1000000);
    testCube3.printCube();
    testCube3.printCubeNums();
*/

    testCube3.printCubeNums();
    /*
    //rotates back counterClockwise (facing the front) clockwise (when back is in front) GOOD
    testCube3.rotateFrontFace(-1, 0);
    testCube3.printCubeNums();

    //rotates front counterClockwise (facing the front)
    testCube3.rotateFrontFace(-1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();

    //rotates left counterclockwise (when right is in front) clockwise when you're facing left (-1 is counterclockwise from the same orientation) GOOD
    testCube3.rotateSide(-1, 0);
    testCube3.printCubeNums();

    //rotates right counterclockwise (when right is in front) GOOD
    testCube3.rotateSide(-1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();

    //rotates top layer counterclockwise (looking down on cube) GOOD
    testCube3.rotateLayer(-1, 0);
    testCube3.printCubeNums();

    //rotates bottom layer counterclockwise (looking down on cube) GOOD
    testCube3.rotateLayer(-1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();
    */
    //rotates back layer clockwise (when front is in front)
    testCube3.rotateFrontFace(1, 0);
    testCube3.printCubeNums();

    //rotates front face clockwise (when front is in front)
    testCube3.rotateFrontFace(1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();

    //rotates left side clockwise (when right is in front)
    testCube3.rotateSide(1, 0);
    testCube3.printCubeNums();

    //rotates right side clockwise (when right is in front)
    testCube3.rotateSide(1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();

    //rotates top layer clockwise (when looking down on the cube)
    testCube3.rotateLayer(1, 0);
    testCube3.printCubeNums();

    //rotates bottom layer clockwise (when looking down on the cube)
    testCube3.rotateLayer(1, testCube3.getDimension() - 1);
    testCube3.printCubeNums();



    //testCube4.printCube();
    //testCube4.rotateSide();
    //testCube4.printCube();
    //testCube4.rotateLayer(1, 1);
    //testCube4.printCube();
    //1 and -1 always returns the cube to its origional state (clockwise then counterclockwise)
    //testCube4.rotateLayer(-1, 1);
    //testCube4.printCube();
    //testCube4.rotateFrontFace(1, 2);
    //testCube4.printCube();
    //testCube4.rotateSide(1, 3);
    //testCube4.printCube();
    //This isn't a legal move on a cube, it's here to show what it does though
    //testCube4.rotateFullFace();
    //testCube4.printCubeNums();


  }

}
