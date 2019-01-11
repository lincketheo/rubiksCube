# rubiksCube
## A rubiks cube representer and solver
## State of code
All algorithms are done. Main interface is solver.java, but each class has something in their main console that demonstrates what the class does. TODO: finish 2x2 and 1x1 exceptions, export algorithms to JSON for javascript program to read



Will organize all code in a JAR file, keeping everything out for debugging purposes. Program can solve a 3 x 3 cube. Working on n x n cube. Can solve first for front center pieces. (A few bugs). Future: need to solve last two faces (simply algorithm) and edges (this will need to use parity algorithms, so new checker methods needed for cube.java.

## Goals:
To be able to solve any n x n rubiks cube. I attempted this project with the mindset that I didn't want to look up any of the common computer methods to solve the cube. I wanted more of a spacial visualization exercise so I used the method that I know (the beginner cross method). This method requires a lot of intuition, so you'll notice that the code can be lengthy at times (a lot of if statements). The general strategy I am using is to solve the center and edge pieces of any n x n cube (turning it into a 3x3 cube) then solving the first second, then third layer of the sudo 3x3 cube. All the data is represented in a 1D array (faces) and all of the translations are done like the cube is 6 n x n faces.
## The cube
To see how I represented the array, you can run test() in the Cube.java document. You'll see the cube printed out in a cross format. The cross is represented like this:
###                Back face
### Left Face      Top Face      Right Face      Bottom Face (mirrored)
###                Front Face
Note that the bottom face wrapps around, so the right edge of bottom face touches the bottom edge of the left face, the left edge of bottom face touches the bottom edge of right face. printCubeNums() prints the numbers that the cube represents and printCube() prints the cube colors. All colors can be found by dividing (integers) the number found on the cube by the base (dimension squared). To find any index, you can use the quadratic equation dim^2 * face + dim * row + col. So if I wanted the 3rd row / 2nd col of the fourth face, I would run getPosition(3, 2, 1) (numbers subtract 1 because indexes start at 0).

## Cube.java
A representation of a dimension x dimension cube. This only contains the helper methods for the other classes. Rotates faces and orients cube.

## Cube3x3.java
This entire class assumes that the center pieces and the edge pieces are solved, or the cube is a 3x3 cube. It contains all the 3x3 methods to solve a "3x3 cube" where 3x3 only means the edges and center are solved.


