import java.util.ArrayList;
import java.util.List;

public class Maze {
    private final int[][] grid;
    private final int rows;
    private final int cols;

    public Maze(int[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        for (int[] dir : directions) {
            int newRow = cell.row + dir[0];
            int newCol = cell.col + dir[1];

            if (isValid(newRow, newCol)) {
                neighbors.add(new Cell(newRow, newCol, 0));
            }
        }

        return neighbors;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && grid[row][col] == 0;
    }
}
