import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import game.*;
import ui.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame gameFrame = new JFrame("Super Hexagon");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setSize(screenSize.width, screenSize.height);
            gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            GameScreen gameScreen = new GameScreen(gameFrame);
            gameScreen.setUpGameScreen();
            
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
        });
    }
}