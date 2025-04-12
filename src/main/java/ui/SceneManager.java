//TODO: Improve the UI
//BUG: Delete Arrow keys


package ui;

import javax.swing.*;

public class SceneManager {
    private JFrame mainFrame;
    private JPanel currentScene;
    private MainMenu mainMenu;
    private GameScreen gameScreen;
    private SettingsMenu settingsMenu;
    private static SceneManager instance;
    
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    private SceneManager() {}
    
    public void initialize(JFrame frame) {
        this.mainFrame = frame;
        
        mainMenu = new MainMenu(this);
        gameScreen = new GameScreen(this);
        settingsMenu = new SettingsMenu(this);
        
        showMainMenu();
    }
    
    public void showMainMenu() {
        switchScene(mainMenu.getPanel());
    }
    
    public void startGame(String difficulty) {

        gameScreen.setDifficulty(difficulty);
        gameScreen.resetGame();
        switchScene(gameScreen.getPanel());
    }
    
    public void showSettings() {
        switchScene(settingsMenu.getPanel());
    }
    
    public void gameOver(int score) {

        mainMenu.updateHighScore(score);
        switchScene(mainMenu.getPanel());
    }
    
    private void switchScene(JPanel newScene) {
        if (currentScene != null) {
            mainFrame.remove(currentScene);
        }
        currentScene = newScene;
        mainFrame.add(currentScene);
        mainFrame.revalidate();
        mainFrame.repaint();

        currentScene.requestFocusInWindow();
    }
    
    public JFrame getFrame() {
        return mainFrame;
    }
}