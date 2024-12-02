package ProjectY1;

import java.util.*;

public class DijkstraSolver {
    private final Maze maze;

    public DijkstraSolver(Maze maze) {
        this.maze = maze;
    }

    public List<Cell> solve(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(Comparator.comparingInt(c -> c.cost));
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Map<Cell, Integer> costSoFar = new HashMap<>();

        openList.add(start);
        costSoFar.put(start, 0);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            if (current.equals(end)) {
                return reconstructPath(cameFrom, start, end);
            }

            for (Cell neighbor : maze.getNeighbors(current)) {
                int newCost = costSoFar.get(current) + 1;

                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    neighbor.cost = newCost;
                    openList.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private List<Cell> reconstructPath(Map<Cell, Cell> cameFrom, Cell start, Cell end) {
        List<Cell> path = new ArrayList<>();
        Cell current = end;

        while (!current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }

        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
