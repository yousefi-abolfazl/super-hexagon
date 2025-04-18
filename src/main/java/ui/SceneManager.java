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
        
        gameScreen.applySettingGame(settingsMenu);
        
        try {
            if (settingsMenu.hasMusic()) {
                settingsMenu.music();
            }
        } catch (Exception e) {
            System.err.println("Failed to play initial music: " + e.getMessage());
        }
    }
    
    public void showMainMenu() {
        switchScene(mainMenu.getPanel());
    }
    
    public void startGame() {

        gameScreen.applySettingGame(settingsMenu);
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