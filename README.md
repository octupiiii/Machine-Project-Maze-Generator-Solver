# Machine-Project-Maze-Generator-Solver
This project is a maze generator and solver built using Java and Swing for graphical representation. It includes various algorithms for solving the maze and allows user interaction to modify the maze and test different algorithms.

## Overview

The Maze Generator and Solver allows users to:

- Generate an empty maze or a randomized maze.
- Edit the maze by clicking on the cells to add or remove walls.
- Solve the maze using different algorithms (BFS, DFS, A*, Dijkstra).
- Visualize the solution process and the path found.

## Features

### Main Functionality

- **Maze Generation**
  - **Empty Maze**: Generates an empty maze with walls around the borders.
  - **Randomized Maze**: Generates a maze using randomized depth-first search (DFS).

- **Maze Editing**
  - Click on the cells to toggle walls on and off.

- **Maze Solving Algorithms**
  - **BFS**: Breadth-First Search.
  - **DFS**: Depth-First Search.
  - **A\***: A* Search Algorithm.
  - **Dijkstra**: Dijkstra's Algorithm.

### User Interface Components

- **JFrame**: Main frame to display the maze.
- **JPanel**: Panel to draw the maze.
- **JButton**: Buttons to generate mazes and select algorithms.
- **JTextArea**: Text area to display the number of visits and steps taken by the algorithm.

## How to Use

### Running the Program

1. **Input Maze Size**: Upon running the program, a prompt will ask for the maze size (between 8 and 64).
2. **Generate Maze**: Use the buttons to generate an empty maze or a randomized maze.
3. **Edit Maze**: Click on cells in the maze to toggle walls.
4. **Solve Maze**: Select an algorithm to solve the maze and visualize the process.

### Maze Size

The maze size can be set between 8 and 64. The size will determine the number of cells in the maze.

### Buttons

- **Empty**: Generates an empty maze.
- **Maze**: Generates a randomized maze.
- **BFS**: Solves the maze using Breadth-First Search.
- **DFS**: Solves the maze using Depth-First Search.
- **A\***: Solves the maze using A* Search Algorithm.
- **Dijkstra**: Solves the maze using Dijkstra's Algorithm.

## Code Structure

### Main Class (MG)

- **Variables**
  - `MAZE_SIZE`: Size of the maze.
  - `WALL_THICKNESS`: Thickness of maze walls.
  - `CELL_SIZE`: Size of each maze cell.
  - `maze`, `mazeCopy`: 2D arrays to represent the maze.
  - `stack`: Stack to keep track of visited cells.
  - `random`: Random number generator.
  - `ctr`: Counter for GUI paint.
  - `drawSize`: Size of the path to draw in GUI.
  - `switchPath`: Used to change the path to draw (visited or path found).
  - `visited`, `path`: Coordinates of visited cells and cells in the path found.
  - `algo`: Algorithms instance.
  - `botIcon`: Image for the bot icon.
  - `textArea`: Text area for the step counter.
  - `algoButton`: Buttons for algorithms.

### Methods

- **main**: Sets up the JFrame, initializes variables, and adds action listeners.
- **initializeMaze**: Initializes the maze data structures.
- **copyMaze**: Copies the maze from source to destination.
- **generateMaze**: Generates a randomized maze using DFS.
- **drawMaze**: Draws the maze and paths.
- **delayDraw**: Animates the drawing of the maze traversal.
- **switchPath**: Switches between the visited path and the found path.
- **addActionListener**: Adds action listeners to the algorithm buttons.
