import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Algorithms {

    private int[][] maze;
    private int maze_size;
    private Coordinate startCell = new Coordinate(1, 1);
    private Coordinate targetCell;
    private Map<Coordinate, Coordinate> parent;
    private ArrayList<Coordinate> visited;
    private ArrayList<Coordinate> path;
    private boolean targetReached;

    private int[] rowDirection = {-1, 1, 0, 0};
    private int[] colDirection = {0, 0, 1, -1};

    public Algorithms(int[][] maze, int maze_size) {
        this.maze = maze;
        this.maze_size = maze_size;
        targetCell = new Coordinate(maze_size-1, maze_size-1);
    }
    
    public boolean BFS( ) {

        // initialize variables
        Queue<Coordinate> queue = new LinkedList<>(); // queue used in BFS
        visited = new ArrayList<>(); 
        path = new ArrayList<>(); 
        parent = new HashMap<>();
        targetReached = false;

        queue.add(startCell); // store coordinate of first cell
        visited.add(startCell); // add start cell to visited
        maze[startCell.x][startCell.y] = 2; // mark start as visited
        
        while (!queue.isEmpty()) {

            Coordinate current = queue.poll();

            if (current.x == targetCell.x && current.y == targetCell.y) {
                
                path = reconstructPath(targetCell);
                return true;

            } else {

                exploreNeighbors(queue, current);

            }            
        }
        return false; 
    }

    public void exploreNeighbors(Queue<Coordinate> queue, Coordinate current) {

        for (int i = 0; i < 4; i++) {
            
            int newRow = current.x + rowDirection[i];
            int newCol = current.y + colDirection[i];
            Coordinate neighbor = new Coordinate(newRow, newCol);

            // Check if out of bounds, visited, or a wall
            if (neighbor.x < 0 || neighbor.x >= maze_size || 
                neighbor.y < 0 || neighbor.y >= maze_size || 
                maze[newRow][newCol] == 1 || 
                maze[newRow][newCol] == 2) 
            {
                continue;
            }

            if (neighbor.x == targetCell.x && neighbor.y == targetCell.y && !targetReached) {
                targetCell = neighbor;
                targetReached = true;
            }
            
            parent.put(neighbor, current); // store current as a parent of neighbor
            visited.add(neighbor);
            maze[neighbor.x][neighbor.y] = 2;  // mark it as visited
            queue.add(neighbor); // add neighbor to queue
        } 
    } 

    public boolean DFS() {

        // initialize variables
        Stack<Coordinate> stack = new Stack<>();  // stack used in DFS
        visited = new ArrayList<>(); 
        path = new ArrayList<>(); 
        parent = new HashMap<>();
        targetReached = false;

        stack.push(startCell);  // store coordinate of first cell
        visited.add(startCell); // add start cell to visited
        maze[startCell.x][startCell.y] = 2;  // mark first as visited

        while (!stack.isEmpty()) {

            Coordinate current = stack.pop();

            if (current.x == targetCell.x && current.y == targetCell.y) {
                
                path = reconstructPath(targetCell);
                return true;

            } else {

                exploreNeighbors(stack, current);

            }
            
        }
        return false;
    }

    public void exploreNeighbors(Stack<Coordinate> stack, Coordinate current) {

        for (int i = 0; i < 4; i++) {

            int newRow = current.x + rowDirection[i];
            int newCol = current.y + colDirection[i];
            Coordinate neighbor = new Coordinate(newRow, newCol);

            // Check if out of bounds, visited, or a wall
            if (neighbor.x < 0 || neighbor.x >= maze_size || 
                neighbor.y < 0 || neighbor.y >= maze_size || 
                maze[newRow][newCol] == 1 || 
                maze[newRow][newCol] == 2) 
            {
                continue;
            }

            if (neighbor.x == targetCell.x && neighbor.y == targetCell.y && !targetReached) {
                targetCell = neighbor;
                targetReached = true;
            }

            parent.put(neighbor, current);  // store current as a parent of neighbor
            visited.add(neighbor);
            maze[neighbor.x][neighbor.y] = 2;  // mark it as visited
            stack.push(neighbor);  // add neigbor to stack
        }
    }

    public boolean AStar() {

        Map<Coordinate, Integer> gScore = new HashMap<>();
        Map<Coordinate, Integer> fScore = new HashMap<>();

        PriorityQueue<Coordinate> openList = new PriorityQueue<>(new Comparator<Coordinate>() 
        {
            public int compare(Coordinate c1, Coordinate c2) 
            {
                return Integer.compare(fScore.get(c1), fScore.get(c2));
            }
        });

        visited = new ArrayList<>(); 
        path = new ArrayList<>(); 
        parent = new HashMap<>();
        openList.add(startCell);
        gScore.put(startCell, 0);
        fScore.put(startCell, calculateHeuristic(startCell, targetCell));

        while (!openList.isEmpty()) {
            Coordinate current = openList.poll();

            if (current.x == targetCell.x && current.y == targetCell.y) {

                path = reconstructPath(current);
                return true;

            } else {

                exploreNeighbors(openList, gScore, fScore, current);

            }
        }
        return false;
    }

    private void exploreNeighbors(PriorityQueue<Coordinate> openList, Map<Coordinate, Integer> gScore, 
                                 Map<Coordinate, Integer> fScore, Coordinate current) 
    {

        for (int i = 0; i < 4; i++) {

            int newRow = current.x + rowDirection[i];
            int newCol = current.y + colDirection[i];
            Coordinate neighbor = new Coordinate(newRow, newCol);

            // Check if out of bounds, visited, or a wall
            if (neighbor.x < 0 || neighbor.x >= maze_size || 
                neighbor.y < 0 || neighbor.y >= maze_size || 
                maze[newRow][newCol] == 1 || 
                maze[newRow][newCol] == 2) 
            {
                continue;
            }

            int tentativeGScore = gScore.get(current) + 1;
            if (!visited.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                parent.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + calculateHeuristic(neighbor, targetCell));
                openList.add(neighbor);
                visited.add(neighbor);
                maze[neighbor.x][neighbor.y] = 2;
            }
        }
    }

    public boolean Dijkstra() {

        int[][] distance = new int[maze_size][maze_size];

        PriorityQueue<Coordinate> openList = new PriorityQueue<>(new Comparator<Coordinate>() {
            public int compare(Coordinate c1, Coordinate c2) {
                int distance1 = distance[c1.x][c1.y];
                int distance2 = distance[c2.x][c2.y];

                if (distance1 < distance2)
                    return -1;
                else if (distance1 > distance2)
                    return 1; 
                else
                    return 0;  
            }
        });

        visited = new ArrayList<>(); 
        path = new ArrayList<>(); 
        parent = new HashMap<>();
        targetReached = false;

        // set all cells with initial value of infinity (max_value)
        for (int i = 0; i < maze_size; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }

        distance[startCell.x][startCell.y] = 0; // set start cell distance to 0.
        openList.add(startCell); // add startCell to queue.
        visited.add(startCell);  // add startCell to visited.

        while (!openList.isEmpty()) {

            Coordinate current = openList.poll();

            if (current.x == targetCell.x && current.y == targetCell.y) {

                path = reconstructPath(targetCell);
                return true;

            } else {

                exploreNeighbors(openList, distance, current);

            }
        }
        return false; 
    }

    public void exploreNeighbors(PriorityQueue<Coordinate> openList, int[][] distance, Coordinate current) {

        // visit each neighbor using direction vectors
        for (int i = 0; i < 4; i++) {

            int newRow = current.x + rowDirection[i];
            int newCol = current.y + colDirection[i];
            Coordinate neighbor = new Coordinate(newRow, newCol);

            // Check if out of bounds, visited, or a wall
            if (neighbor.x < 0 || neighbor.x >= maze_size || 
                neighbor.y < 0 || neighbor.y >= maze_size || 
                maze[newRow][newCol] == 1 || 
                visited.contains(neighbor)) 
            {
                continue;
            }

            if (neighbor.x == targetCell.x && neighbor.y == targetCell.y && !targetReached) {
                targetCell = neighbor;
                targetReached = true;
            }
            
            int tempDist = distance[current.x][current.y] + 10;
            if (tempDist<distance[neighbor.x][neighbor.y]) {
                distance[neighbor.x][neighbor.y] = tempDist;
                parent.put(neighbor, current);
                visited.add(neighbor);
                //maze[neighbor.x][neighbor.y] = 2;
                openList.add(neighbor);
            }
        }
    }

    private ArrayList<Coordinate> reconstructPath(Coordinate node) {

        ArrayList<Coordinate> path = new ArrayList<>();
        while (node != null) 
        {
            path.add(node);
            node = parent.get(node);
        }
        Collections.reverse(path);

        return path;
    }

    private int calculateHeuristic(Coordinate from, Coordinate to) 
    {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    public ArrayList<Coordinate> getPath() {
        return path;
    }

    public ArrayList<Coordinate> getVisited() {
        return visited;
    }

}
