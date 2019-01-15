# rubiksCube
## A rubiks cube representer and solver
## Cube.java
Cube.java is a representation of a cube, Cube nxn and 3x3 are both extensions of Cube. A Cube has a few dedicated methods such as rotatefrontface, rotatelayer, rotateside, rotatecube clockwise, counter clockwise, up and down (the last four could be brought down to two single methods rotate cubeXPlane and YPlane, just pass a direction in each). The cube also has many finder / orientation functions that find certain pieces or orient the cube in a certain way (although it is not really necessary to orient the cube, I think it makes the 3D animation a little more visually appealing). 
These methods are described in detail in comments on the program.

## Cubenxnx.java
Cubenxn.java is an instance of Cube. An nxn cube is a cube that is greater than 3 dimensions (it can be a 3 dimensional cube, but the code just immediately turns it into a 3x3 cube). An nxn cube contains only methods to solve the front faces and the edges (that is reduction to a 3x3). Cubepieces is just a list of cubepieces that  are either in place or not. Usefull for dealling the the center face because you can immagine four pieces can fit in the same spot.

## Cube 3x3.java
Cube3x3.java is an instance of Cube. A 3x3 cube contains methods to solve a "3x3" cube. All the methods, however, assume that the edges and centers are solved, but they work with any dimensional cube. The solver method solves the first layer, then the second and finally the third. It also deals with OLL parity because there is no way of knowing that OLL parity exists until the last layer of the 3x3 stage.

## Other small classes
A big area for improvement would be in the Move class. Right now, a move is crudely defined as a main and a layer. Main is either x, y, or z +/- or XX or YY. the direction is decided by the +/- sign (which is a very crude way of doing it, a better way would be to use booleans). 
CubePiece.java is represented as a single cube piece that is either in place or not
EdgePiece contains two pieces
Cornerpiece contains three pieces
