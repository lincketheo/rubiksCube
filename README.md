# rubiksCube
## A rubiks cube representer and solver
## Representation
A rubiks cube in this program is represented by a one dimensional array of integers (faces in Cube.java). For example, a cube with dimension 3 is represented as:
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 ... 54.
The meaning of these numbers is constant throughout the cube. For each face, the numbers wrap through the face, so face 1 is represented as:
0 1 2
3 4 5
6 7 8
Faces are organized in a classic unwrapped cube appearance, so:
back face
left face --- top face --- right face --- bottom face (mirrored)
front face
Run printCube() or printCubeNums() in any class to visualize this. (remember to note that the bottom face is flipped horizontally)

## Parts of the project
This project can represent, scramble (completely random) and solve any dimensional cube (except for 2 and 1 dimensions and negative). There is also a 3D GUI that goes along with the cube. This is the javascript Cube.js. This GUI only takes in a scramble and a solve algorithm, it doesn't process any of the solving methods for a cube. Solver.java has a convinient output, when you run Solver.java (with some configuring to your choice, described in the readme in src), it outputs a javascript object with a dimension, scramble and solver algorithm. So, to update algorithms in the algorithm.js file, simply run
        javac Solver.java
        java Solver > algorithm.js
This will create a new cube object in algorithm.js that Cube.js reads from. 

## Problems
The code by itself has not been seen to fail or not solve the cube as far as I'm aware. However, I've added a few checkpoints so that, if the cube is unsolved at said point, the program calls it a new scrambled cube and starts over (which doesn't lose much because it should be partially solved). These checkpoints garuntee that , as long as the code doesn't enter a stack overflow, it will be solved. Although, I have run the code a few hundred times with hundred dimension examples without a problem without the checkpoints, so these would rarely be triggered. The code is also very messy. I am a beginning computer science student, and I am not as confident as I ideally should be. There's a lot of spaghetti code and if statements. If you chose to edit my work, feel free to break it down into it's necessary parts.
