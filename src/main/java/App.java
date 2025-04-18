import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import ui.*;
import util.MusicPlayer;

public class App {
    public static void main(String[] args) {
        // Initialize our MusicPlayer instance 
        MusicPlayer.getInstance();
        System.out.println("MusicPlayer initialized successfully");
        
        SwingUtilities.invokeLater(() -> {
            JFrame gameFrame = new JFrame("Super Hexagon");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            gameFrame.setSize(screenSize.width, screenSize.height);
            //gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.initialize(gameFrame);
            
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
        });
    }
}