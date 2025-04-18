//TODO: Improve the UI
//[ ]: Add a music system
//BUG: The game is not stopping when the marker hits the obstacle
//BUG: marker is not moving


package ui;

import java.awt.*;
import javax.swing.*;
import game.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameScreen {
    private GamePanel gamePanel;
    private Game game;
    private SceneManager sceneManager;
    private String difficulty = "NORMAL";
    private boolean isRunning;
    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;

    public GameScreen(SceneManager manager) {
        this.sceneManager = manager;
        this.game = new Game();
        this.gamePanel = new GamePanel();
        this.isRunning = false;
        setUpGameScreen();
    }

    public void setUpGameScreen() {
        setupKeyBindings();
        startGameLoop();
    }

    private void setupKeyBindings() {
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        leftKeyPressed = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightKeyPressed = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        pauseGame();
                        sceneManager.showMainMenu();
                        break;
                    case KeyEvent.VK_SPACE:
                        if (game.isGameOver()) {
                            game = new Game();
                            game.startGame();
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                System.out.println("کلید رها شد: " + KeyEvent.getKeyText(keyCode));
                
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        leftKeyPressed = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightKeyPressed = false;
                        break;
                }
            }
        });
    }

    private void startGameLoop() {
        isRunning = true;

        Thread gameThread = new Thread(() -> {
            long lastFrameTime = System.nanoTime();
            final double TARGET_FPS = 120.0;
            final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;

            while (isRunning) {
                if (game.isGameOver()) {
                    stopGame();
                    sceneManager.gameOver(game.getScore());
                    break;
                }

                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastFrameTime) / 1000000000.0;
                
                deltaTime = Math.min(deltaTime, 0.05);
                
                lastFrameTime = currentTime;
                
                if (!game.isGameOver()) {
                    if (leftKeyPressed) {
                        game.moveMarkerLeft(deltaTime);
                    }
                    if (rightKeyPressed) {
                        game.moveMarkerRight(deltaTime);
                    }

                    game.update(deltaTime);
                }
                
                SwingUtilities.invokeLater(() -> gamePanel.repaint());
                
                try {
                    long sleepTime = (long)((lastFrameTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    } else {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gameThread.setDaemon(true);
        gameThread.setPriority(Thread.MAX_PRIORITY);
        gameThread.start();
        game.startGame();
    }

    public JPanel getPanel() {
        return gamePanel;
    }

    public void pauseGame() {
        isRunning = false;
    }

    public void resumeGame() {
        isRunning = true;
    }

    public void stopGame() {
        isRunning = false;
        game.stopGame();
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void resetGame() {
        game = new Game();
        
        gamePanel.requestFocus();
        gamePanel.requestFocusInWindow();

        switch (difficulty) {
            case "EASY":
                game.setSpawnRate(1.5);
                game.setGameSpeed(1.0);
                break;
            case "NORMAL":
                game.setSpawnRate(1.0);
                game.setGameSpeed(2.0);
                break;
            case "HARD":
                game.setSpawnRate(0.7);
                game.setGameSpeed(30.0);
                break;
        }

        startGameLoop();

        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocus();
            gamePanel.requestFocusInWindow();
        
        });
    }

    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.BLACK);
            setDoubleBuffered(true);
            setFocusable(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            
            if (game != null) {
                game.render(g2);
                
                if (game.isGameOver()) {
                    renderGameOverMessage(g2);
                }
            }
        }

        private void renderGameOverMessage(Graphics2D g2) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g2.getFontMetrics();
            String gameOverMessage = "Game Over - Press SPACE to restart";
            int textWidth = fm.stringWidth(gameOverMessage);
            g2.drawString(gameOverMessage, getWidth() / 2 - textWidth / 2, getHeight() / 2);
        }
    }

    public void applySettingGame(SettingsMenu setting) {
        if (setting != null) {
            String difficultyText = setting.getDifficulty();
            setDifficulty(difficultyText);
            
            if (setting.hasMusic()) {
                setting.music();
            } else {
                setting.stopMusic();
            }
        }
    }
}