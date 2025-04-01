import javax.swing.*;
import ui.MainMenu;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}