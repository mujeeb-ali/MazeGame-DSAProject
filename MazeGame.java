package ProjectY1;
import javax.swing.*;

public class MazeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Levels levels = new Levels();
            MazeGUI mazeGUI = new MazeGUI(levels);
            mazeGUI.setVisible(true);
        });
    }
}
