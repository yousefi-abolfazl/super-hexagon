package ui;

import javax.swing.*;
import java.awt.*;
import game.*;

public class SceneManager {
    private JFrame mainFrame;
    private JPanel currentScene;
    private MainMenu mainMenu;
    private GameScreen gameScreen;
    private SettingsMenu settingsMenu;
    private static SceneManager instance;
    
    // الگوی Singleton برای دسترسی آسان
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    private SceneManager() {}
    
    public void initialize(JFrame frame) {
        this.mainFrame = frame;
        
        // ایجاد صحنه‌ها
        mainMenu = new MainMenu(this);
        gameScreen = new GameScreen(this);
        settingsMenu = new SettingsMenu(this);
        
        // شروع با منوی اصلی
        showMainMenu();
    }
    
    public void showMainMenu() {
        switchScene(mainMenu.getPanel());
    }
    
    public void startGame(String difficulty) {
        // انتقال تنظیمات به بازی
        gameScreen.setDifficulty(difficulty);
        gameScreen.resetGame();
        switchScene(gameScreen.getPanel());
    }
    
    public void showSettings() {
        switchScene(settingsMenu.getPanel());
    }
    
    public void gameOver(int score) {
        // ارسال امتیاز به منوی اصلی
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
        // اطمینان از فوکوس صحنه جدید
        currentScene.requestFocusInWindow();
    }
    
    // متدهای دسترسی به کامپوننت‌ها
    public JFrame getFrame() {
        return mainFrame;
    }
}