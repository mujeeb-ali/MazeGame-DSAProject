package ProjectY1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MazeGUI extends JFrame {
    private int[][] maze;
    private final int cellSize = 50;
    private final Levels levels;
    private int currentLevel = 0;
    private int rows, cols;
    private Cell start, end, userPosition;
    private boolean isHumanMode = false;

    public MazeGUI(Levels levels) {
        this.levels = levels;
        loadLevel();

        // Setup the JFrame
        setTitle("Maze Solver");
        setSize(cols * cellSize + 20, rows * cellSize + 70);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add the grid panel
        MazePanel mazePanel = new MazePanel();
        add(mazePanel, BorderLayout.CENTER);

        // Add buttons for modes
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton humanModeButton = new JButton("Play as Human");
        JButton computerModeButton = new JButton("Let Computer Solve");
        buttonPanel.add(humanModeButton);
        buttonPanel.add(computerModeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        humanModeButton.addActionListener(e -> startHumanMode(mazePanel));
        computerModeButton.addActionListener(e -> solveMaze(mazePanel));

        // Add key listener for user navigation
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isHumanMode) {
                    int newRow = userPosition.row, newCol = userPosition.col;

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> newRow--;
                        case KeyEvent.VK_DOWN -> newRow++;
                        case KeyEvent.VK_LEFT -> newCol--;
                        case KeyEvent.VK_RIGHT -> newCol++;
                    }

                    if (isValid(newRow, newCol)) {
                        userPosition = new Cell(newRow, newCol, 0);
                        maze[newRow][newCol] = 2; // Mark as visited
                        mazePanel.repaint();

                        if (userPosition.equals(end)) {
                            JOptionPane.showMessageDialog(MazeGUI.this, "Congratulations! You solved the maze!");
                            isHumanMode = false;
                            loadNextLevel();
                        }
                    }
                }
            }
        });
    }

    // Load the current level
    private void loadLevel() {
        maze = levels.getLevel(currentLevel);
        if (maze == null) {
            JOptionPane.showMessageDialog(this, "Congratulations! You completed all levels!");
            System.exit(0);
        }

        rows = maze.length;
        cols = maze[0].length;
        start = new Cell(0, 0, 0);
        end = new Cell(rows - 1, cols - 1, 0);
        userPosition = new Cell(start.row, start.col, 0);
    }

    // Load the next level
    private void loadNextLevel() {
        currentLevel++;
        loadLevel();
        repaint();
    }

    // Validate if a cell is within bounds and passable
    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && (maze[row][col] == 0 || maze[row][col] == 2);
    }

    // Solve the maze
    // Solve the maze and display the solution
    private void solveMaze(MazePanel mazePanel) {
        Maze mazeObj = new Maze(maze);
        DijkstraSolver solver = new DijkstraSolver(mazeObj);

        List<Cell> path = solver.solve(start, end);
        if (path.isEmpty()) {
            // If no path is found, show the "unsolvable" message and move to the next level
            JOptionPane.showMessageDialog(this, "Sorry, this level cannot be solved.", "Level Unsolvable", JOptionPane.WARNING_MESSAGE);
            loadNextLevel(); // Automatically move to the next level
        } else {
            // If path is found, display the path and visualize it
            JOptionPane.showMessageDialog(this, "Path found! Visualizing...");
            mazePanel.drawPath(path);

            // Wait for the visualization to complete before showing the congratulations message
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Congratulations! You solved the maze!");
                loadNextLevel(); // Load the next level after displaying the message
            });
        }
    }

//    private void solveMaze(MazePanel mazePanel) {
//        Maze mazeObj = new Maze(maze);
//        DijkstraSolver solver = new DijkstraSolver(mazeObj);
//
//        List<Cell> path = solver.solve(start, end);
//        if (path.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Sorry, this level cannot be solved.", "Level Unsolvable", JOptionPane.WARNING_MESSAGE);
//            loadNextLevel();
//        } else {
//            JOptionPane.showMessageDialog(this, "Path found! Visualizing...");
//            mazePanel.drawPath(path);
//        }
//    }

    // Start human mode
    private void startHumanMode(MazePanel mazePanel) {
        JOptionPane.showMessageDialog(this, "Use arrow keys to navigate the maze. Reach the red square to win!");
        userPosition = new Cell(start.row, start.col, 0);
        maze[start.row][start.col] = 2; // Mark the start position as visited
        mazePanel.repaint();
        isHumanMode = true;
        this.requestFocus();
    }

    // Panel to draw the maze
    private class MazePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (maze[row][col] == 1) {
                        g.setColor(Color.BLACK);
                    } else if (maze[row][col] == 2) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }

            g.setColor(Color.BLUE);
            g.fillRect(start.col * cellSize, start.row * cellSize, cellSize, cellSize);
            g.setColor(Color.RED);
            g.fillRect(end.col * cellSize, end.row * cellSize, cellSize, cellSize);

            if (userPosition != null) {
                g.setColor(Color.ORANGE);
                g.fillRect(userPosition.col * cellSize + 10, userPosition.row * cellSize + 10, cellSize - 20, cellSize - 20);
            }
        }

        public void drawPath(List<Cell> path) {
            Graphics g = getGraphics();
            g.setColor(Color.GREEN);
            for (Cell cell : path) {
                g.fillRect(cell.col * cellSize + 10, cell.row * cellSize + 10, cellSize - 20, cellSize - 20);
                try {
                    Thread.sleep(100); // Slow down the visualization
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
