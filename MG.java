import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MG {

    private static int MAZE_SIZE; // Size of the maze (number of cells)
    private static final int WALL_THICKNESS = 2; // Thickness of maze walls
    private static final int CELL_SIZE = 10; // Size of each maze cell

    private static int[][] maze; // 2D array to represent the maze
    private static int[][] mazeCopy; // 2D array to represent the maze
    private static Stack<int[]> stack; // Stack to keep track of visited cells
    private static Random random; // Random number generator
    private static int ctr = 0,  // Counter for GUI paint
                       drawSize; // Size of path to draw in GUI
    private static boolean switchPath = false; // Used to change path to draw (visited or path found)
    private static ArrayList<Coordinate> visited; // Coordinates of visited cells
    private static ArrayList<Coordinate> path;  // Coordinates of cells in the path found
    private static Algorithms algo; 
    private static Image botIcon;  
    private static JTextArea textArea;   // Text Area for step counter
    private static JButton[] algoButton;    // Buttons for algorithms
 
    public static void main(String[] args) {
        
        // Ask the max size
        String input = JOptionPane.showInputDialog("Enter maze size (between 8 and 64):");
        try {
            MAZE_SIZE = Integer.parseInt(input);
            if (MAZE_SIZE < 8 || MAZE_SIZE > 64) {
                JOptionPane.showMessageDialog(null, "Maze size must be between 8 and 64.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            return;
        }

        MAZE_SIZE += 1; //Adds borders onto the maze

        // Calculate the size of JFrame
        int frameWidth = (MAZE_SIZE + 15) * CELL_SIZE + WALL_THICKNESS * 4;
        int frameHeight = (MAZE_SIZE + 7) * CELL_SIZE + WALL_THICKNESS * 4;
        botIcon = new ImageIcon("bot_icon.png").getImage();

        // Create a JFrame for the maze visualization
        JFrame frame = new JFrame("Maze Generator");
        frame.setLayout(new BorderLayout(0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(true); 
        frame.setLocationRelativeTo(null); 

        // Create a JPanel for drawing the maze
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };

        // Create a JPanel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6,1));
        buttonPanel.setPreferredSize(new Dimension(110,1));

        // Text area for counters
        textArea = new JTextArea("Welcome!");
        textArea.setPreferredSize(new Dimension(1,35));
        textArea.setOpaque(false);
        textArea.setBorder(null);
        textArea.setEditable(false);

        // edit walls
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                copyMaze(mazeCopy, maze);
                if (maze[x][y] == 1)
                    maze[x][y] = 0;
                else
                    maze[x][y] = 1;
                copyMaze(maze, mazeCopy);
                panel.repaint();
                //printMaze();
            }
        });

        // Create a "Generate empty" button
        JButton generateEmptyButton = new JButton("empty");
        buttonPanel.add(generateEmptyButton);
        generateEmptyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeMaze(); // Initialize maze data structures
                copyMaze(maze, mazeCopy);
                panel.repaint(); // Redraw the maze
                textArea.setText("Number of visits: " + 0 + "\nNumber of steps: " + 0);
                //printMaze(); // print matrix -- VERIFICATION
            }
        });

        // Create a "Generate maze" button
        JButton generateMazeButton = new JButton("maze");
        buttonPanel.add(generateMazeButton);
        generateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initializeMaze(); // Initialize maze data structures
                generateMaze(); // Generate the maze
                copyMaze(maze, mazeCopy);
                panel.repaint(); // Redraw the maze
                textArea.setText("Number of visits: " + 0 + "\nNumber of steps: " + 0);
                //printMaze(); // print matrix -- VERIFICATION
            }
        });

        // initialize visited and path arraylist
        visited = new ArrayList<>();
        path = new ArrayList<>();

        // Array of alorithms button
        algoButton = new JButton[4];
        for (int i= 0; i < algoButton.length; i++) {
            algoButton[i] = new JButton();
            addActionListener(i, panel);
            buttonPanel.add(algoButton[i]);
        }

        // set text for algo buttons
        algoButton[0].setText("BFS");
        algoButton[1].setText("DFS");
        algoButton[2].setText("A*");
        algoButton[3].setText("Dijkstra");

        // Add components to the JFrame
        frame.add(panel);
        frame.add(textArea, BorderLayout.SOUTH);
        frame.add(buttonPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static void initializeMaze() {
        maze = new int[MAZE_SIZE][MAZE_SIZE];
        mazeCopy = new int[MAZE_SIZE][MAZE_SIZE];
        stack = new Stack<>();
        random = new Random();
    }

    private static void copyMaze(int[][] source, int[][] destination) {
        for (int i = 0; i < MAZE_SIZE; i++)
            System.arraycopy(source[i], 0, destination[i], 0, MAZE_SIZE);
    }

    private static void generateMaze() {
        initializeMaze();
    
        // Puts walls to top and leftside.
        for (int x = 0; x < MAZE_SIZE; x++) {
            for (int y = 0; y < MAZE_SIZE; y++) {
                maze[x][y] = 1;
            }
        }
    
        maze[1][1] = 0; //start point of the maze
        stack.push(new int[] { 1, 1 });
        
        maze[MAZE_SIZE-1][MAZE_SIZE-2] = 0; //cell adjacent to the endpoint
        maze[MAZE_SIZE-1][MAZE_SIZE-1] = 0; //endpoint of maze
        
        while (!stack.isEmpty()) {  // Randomized DFS
            int[] current = stack.peek();
            int cx = current[0];
            int cy = current[1];
            boolean hasUnvisitedNeighbor = false;
    
            int[] directions = { 0, 1, 2, 3 };
            for (int i = 0; i < directions.length; i++) {
                int randIndex = random.nextInt(4);
                int temp = directions[randIndex];
                directions[randIndex] = directions[i];
                directions[i] = temp;
            }
    
            for (int dir : directions) {
                int nx = cx;
                int ny = cy;
    
                if (dir == 0) {
                    ny -= 2;
                } else if (dir == 1) {
                    ny += 2;
                } else if (dir == 2) {
                    nx -= 2;
                } else if (dir == 3) {
                    nx += 2;
                }
    
                if (nx >= 0 && nx < MAZE_SIZE && ny >= 0 && ny < MAZE_SIZE && maze[nx][ny] == 1) {
                    maze[nx][ny] = 0;
                    maze[(cx + nx) / 2][(cy + ny) / 2] = 0;
                    stack.push(new int[] { nx, ny });
                    hasUnvisitedNeighbor = true;
                    break;
                }
            }
    
            if (!hasUnvisitedNeighbor) {
                stack.pop();
            }
        }
    
        int x = MAZE_SIZE - 2, y = MAZE_SIZE - 2;
        while (x > 0) {
            if (maze[x][y - 1] == 0 || maze[x][y + 1] == 0 || maze[x - 1][y] == 0 || maze[x + 1][y] == 0) {
                break;
            }
            maze[x][y] = 0;
            x--;
        }
    } 

    private static void drawMaze(Graphics g) {
        int size1 = ctr, 
            size2 = 0;

        if (maze == null)   // If maze not yet generated/initalized.
            return;

        // walls and white paths
        for (int x = 0; x < MAZE_SIZE; x++) {
            for (int y = 0; y < MAZE_SIZE; y++) {
                if (maze[x][y] == 1) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * CELL_SIZE + WALL_THICKNESS, y * CELL_SIZE + WALL_THICKNESS, CELL_SIZE, CELL_SIZE);
            }
        }

        // change path to delaydraw
        if (switchPath) {
            size1 = visited.size();
            size2 = ctr;
        }
        
        // path for visited cells
        g.setColor(Color.ORANGE);
        if (visited.size()>0) {
            for (int i = 0; i < size1; i++) {
                int x = visited.get(i).x;
                int y = visited.get(i).y;
                g.fillRect(x * CELL_SIZE + WALL_THICKNESS, y * CELL_SIZE + WALL_THICKNESS, CELL_SIZE, CELL_SIZE);
                textArea.setText("Number of visits: " + size1 + "\nNumber of steps: " + size2);
            }
        }

        // Color for start and target points
        g.setColor(Color.RED);
        g.fillRect(1 * CELL_SIZE + WALL_THICKNESS, 1 * CELL_SIZE + WALL_THICKNESS, CELL_SIZE, CELL_SIZE);

        g.setColor(Color.RED);
        g.fillRect((MAZE_SIZE - 1) * CELL_SIZE + WALL_THICKNESS, (MAZE_SIZE - 1) * CELL_SIZE + WALL_THICKNESS,
                CELL_SIZE, CELL_SIZE);

        // path for path found from start to goal
        g.setColor(Color.GREEN);
        if (switchPath) {
            for (int i = 0; i < size2; i++) {
                int x = path.get(i).x;
                int y = path.get(i).y;
                g.fillRect(x * CELL_SIZE + WALL_THICKNESS, y * CELL_SIZE + WALL_THICKNESS, CELL_SIZE, CELL_SIZE);

                if (i == ctr-1)
                    g.drawImage(botIcon, x * CELL_SIZE + WALL_THICKNESS, y * CELL_SIZE + WALL_THICKNESS, CELL_SIZE, CELL_SIZE, null);

                textArea.setText("Number of visits: " + size1 + "\nNumber of steps: " + size2);
            }
        }
        
    }

    public static void delayDraw(JPanel panel) {
        // For animation of maze traversal.
        Timer timer = new Timer(5, (ActionEvent a) -> {
            if (ctr < drawSize) {
                panel.repaint(); // redraw maze
                ctr++;
            } else {
                ((Timer) a.getSource()).stop();
                switchPath();
                ctr = 0;
            } 
        });
        timer.start();
    }

    public static void switchPath() {
        if (switchPath) {
            switchPath = false;
            drawSize = visited.size();
        } else {
            switchPath = true;
            drawSize = path.size();
        }    
    }

    private static void addActionListener(int index, JPanel panel) {

        algoButton[index].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = false;

                copyMaze(mazeCopy, maze);
                algo = new Algorithms(maze, MAZE_SIZE);

                if (index == 0)
                    result =algo.BFS();
                if (index == 1)
                    result =algo.DFS();
                if (index == 2)
                    result =algo.AStar();
                if (index == 3)
                    result =algo.Dijkstra();
                
                if (!result)
                    JOptionPane.showMessageDialog(null, "Path not found.");

                visited = algo.getVisited();
                path = algo.getPath();
                drawSize = visited.size();
                delayDraw(panel); // redraw maze with orange path
                delayDraw(panel); // redraw maze with green path
                //printMaze(); // print matrix -- VERIFICATION
            }
        }); 
    }
/* 
    private static void printMaze() {
        if (maze == null) {
            System.out.println("Maze is not generated yet.");
            return;
        }

        for (int y = 0; y < MAZE_SIZE; y++) {
            for (int x = 0; x < MAZE_SIZE; x++) {
                System.out.print(maze[x][y] + " ");
            }
            System.out.println(); // Move to the next row after each row is printed
        }
        System.out.println();
    }
*/
}
