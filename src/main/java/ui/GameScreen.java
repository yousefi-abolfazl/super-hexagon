//TODO: Improve the UI
//[ ]: Add a music system
//BUG: The game is not stopping when the marker hits the obstacle
//BUG: marker is not moving


package ui;

import java.awt.*;
import javax.swing.*;
import game.*;
import java.awt.geom.Path2D;
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
                    System.out.println(game.getScore());
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
        game.setGameStartTime(System.nanoTime());
        game.setCurrentTime(game.getGameStartTime());
        game.setSurvivalTimeSeconds(0.0);
        game.setIsTimerRunning(true);

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

        private final Color BACKGROUND_COLOR = new Color(0, 0, 0);  
        private final Color TEXT_COLOR = new Color(255, 255, 255);
        private final Color HIGHLIGHT_COLOR = new Color(180, 0, 0); 
        private final Color LINE_COLOR_1 = new Color(180, 0, 0);
        private final Color LINE_COLOR_2 = new Color(0, 0, 0);
        
        private double pulseEffect = 0;
        private double backgroundRotation = 0;
        
        private Font titleFont = new Font("Arial", Font.BOLD, 36);
        private Font scoreFont = new Font("Arial", Font.PLAIN, 24);
        private Font messageFont = new Font("Arial", Font.BOLD, 28);
        
        private final int NUM_BACKGROUND_LINES = 12;
        
        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(BACKGROUND_COLOR);
            setDoubleBuffered(true);
            setFocusable(true);
            
            Timer animationTimer = new Timer(16, e -> {
                pulseEffect += 0.05;
                if (pulseEffect > Math.PI * 2) {
                    pulseEffect -= Math.PI * 2;
                }
                
                backgroundRotation += 0.002;
                if (backgroundRotation > Math.PI * 2) {
                    backgroundRotation -= Math.PI * 2;
                }
                
                repaint();
            });
            animationTimer.start();
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            drawBackground(g2);
            
            if (game != null) {
                game.render(g2);
                
                drawScore(g2);
                
                if (game.isGameOver()) {
                    renderGameOverMessage(g2);
                }
            } else {
                drawStartScreen(g2);
            }
        }
        
        private void drawBackground(Graphics2D g2) {
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            int maxRadius = (int) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()) / 2;
            double sectorAngle = Math.PI * 2 / NUM_BACKGROUND_LINES;
            
            for (int i = 0; i < NUM_BACKGROUND_LINES; i++) {
                double startAngle = i * sectorAngle + backgroundRotation;
                double endAngle = (i + 1) * sectorAngle + backgroundRotation;
                
                Path2D sector = new Path2D.Double();
                sector.moveTo(centerX, centerY);
                
                int x1 = (int) (centerX + Math.cos(startAngle) * maxRadius);
                int y1 = (int) (centerY + Math.sin(startAngle) * maxRadius);
                sector.lineTo(x1, y1);
                
                int numPoints = 10;
                for (int j = 1; j <= numPoints; j++) {
                    double angle = startAngle + (endAngle - startAngle) * j / numPoints;
                    int x = (int) (centerX + Math.cos(angle) * maxRadius);
                    int y = (int) (centerY + Math.sin(angle) * maxRadius);
                    sector.lineTo(x, y);
                }
                
                sector.closePath();
                
                if (i % 2 == 0) {
                    g2.setColor(new Color(180, 0, 0, 40));
                    g2.fill(sector);
                } else {
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fill(sector);
                }
            }
            
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 80));
            
            for (int i = 0; i < NUM_BACKGROUND_LINES; i++) {
                double angle = i * sectorAngle + backgroundRotation;
                int x = (int) (centerX + Math.cos(angle) * maxRadius);
                int y = (int) (centerY + Math.sin(angle) * maxRadius);
                
                g2.drawLine(centerX, centerY, x, y);
            }
        }
        
        private void drawScore(Graphics2D g2) {
            String scoreText = String.format("SCORE: %06d", game.getScore());
            g2.setFont(scoreFont);
            g2.setColor(TEXT_COLOR);
            
            FontMetrics metrics = g2.getFontMetrics();
            g2.drawString(scoreText, getWidth() / 2 - metrics.stringWidth(scoreText) / 2, 40);
            
            String levelText = getCurrentLevelName();
            
            float pulseValue = (float) Math.abs(Math.sin(pulseEffect));
            g2.setColor(new Color(
                HIGHLIGHT_COLOR.getRed(),
                HIGHLIGHT_COLOR.getGreen(),
                HIGHLIGHT_COLOR.getBlue(),
                (int) (180 + 75 * pulseValue)
            ));
            
            g2.setFont(titleFont);
            metrics = g2.getFontMetrics();
            g2.drawString(levelText, getWidth() / 2 - metrics.stringWidth(levelText) / 2, 80);
        }
        
        private String getCurrentLevelName() {
            int score = game.getScore();
            if (score < 500) {
                return "BEGINNER";
            } else if (score < 1500) {
                return "LINE";
            } else if (score < 3000) {
                return "TRIANGLE";
            } else if (score < 5000) {
                return "SQUARE";
            } else if (score < 8000) {
                return "PENTAGON";
            } else {
                return "HEXAGON";
            }
        }
        
        private void drawStartScreen(Graphics2D g2) {
            String title = "SUPER HEXAGON";
            String message = "PRESS ANY KEY TO START";
            
            g2.setColor(HIGHLIGHT_COLOR);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            FontMetrics titleMetrics = g2.getFontMetrics();
            g2.drawString(title, getWidth() / 2 - titleMetrics.stringWidth(title) / 2, 
                         getHeight() / 2 - 40);
            
            float pulseValue = (float) Math.abs(Math.sin(pulseEffect));
            g2.setColor(new Color(
                TEXT_COLOR.getRed(),
                TEXT_COLOR.getGreen(),
                TEXT_COLOR.getBlue(),
                (int) (180 + 75 * pulseValue)
            ));
            g2.setFont(messageFont);
            FontMetrics msgMetrics = g2.getFontMetrics();
            g2.drawString(message, getWidth() / 2 - msgMetrics.stringWidth(message) / 2, 
                         getHeight() / 2 + 40);
        }
    
        private void renderGameOverMessage(Graphics2D g2) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            String gameOverText = "GAME OVER";
            String scoreText = String.format("SCORE: %06d", game.getScore());
            String restartText = "PRESS SPACE TO RESTART";
            
            g2.setColor(HIGHLIGHT_COLOR);
            g2.setFont(titleFont);
            FontMetrics metrics = g2.getFontMetrics();
            g2.drawString(gameOverText, getWidth() / 2 - metrics.stringWidth(gameOverText) / 2, getHeight() / 2 - 50);
            
            g2.setColor(TEXT_COLOR);
            g2.setFont(scoreFont);
            metrics = g2.getFontMetrics();
            g2.drawString(scoreText, getWidth() / 2 - metrics.stringWidth(scoreText) / 2, getHeight() / 2);
            
            float pulseValue = (float) Math.abs(Math.sin(pulseEffect));
            g2.setColor(new Color(
                TEXT_COLOR.getRed(),
                TEXT_COLOR.getGreen(),
                TEXT_COLOR.getBlue(),
                (int) (180 + 75 * pulseValue)
            ));
            g2.setFont(messageFont);
            metrics = g2.getFontMetrics();
            g2.drawString(restartText, getWidth() / 2 - metrics.stringWidth(restartText) / 2, getHeight() / 2 + 50);
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