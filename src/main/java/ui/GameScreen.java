package ui;

import java.awt.*;
import javax.swing.*;
import game.Game;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameScreen {
    private JFrame frame;
    private GamePanel mainPanel;
    private Game game;
    private boolean isRunning;
    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;

    public GameScreen(JFrame frame) {
        this.frame = frame;
        this.game = new Game();
        this.mainPanel = new GamePanel();
        this.isRunning = false;
    }

    public void setUpGameScreen() {
        frame.add(mainPanel);
        setupKeyBindings();
        startGameLoop();
    }

    private void setupKeyBindings() {
        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();

        mainPanel.addKeyListener(new KeyAdapter() {
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
                        System.exit(0);
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

        // حذف Timer و استفاده از یک thread جداگانه
        Thread gameThread = new Thread(() -> {
            long lastFrameTime = System.nanoTime();
            final double TARGET_FPS = 60.0;
            final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;

            while (isRunning) {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastFrameTime) / 1000000000.0;
                lastFrameTime = currentTime;

                // آپدیت منطق بازی
                if (!game.isGameOver()) {
                    // کنترل حرکت
                    if (leftKeyPressed) {
                        game.moveMarkerLeft();
                    }
                    if (rightKeyPressed) {
                        game.moveMarkerRight();
                    }

                    game.update(deltaTime);
                }

                // درخواست رندر جدید
                SwingUtilities.invokeLater(() -> mainPanel.repaint());

                // زمان خواب برای کنترل FPS
                try {
                    long sleepTime = (long)((lastFrameTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gameThread.setDaemon(true);
        gameThread.start();
        game.startGame();
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

    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.BLACK);
            setDoubleBuffered(true); // فعال کردن double buffering
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            // تنظیمات رندرینگ را فقط یک‌بار انجام دهید
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // رندر بازی
            if (game != null) {
                game.render(g2);
                
                if (game.isGameOver()) {
                    renderGameOverMessage(g2);
                }
            }
        }

        private void renderGameOverMessage(Graphics2D g2) {
            // جداسازی منطق رندر پیام گیم اور برای خوانایی بهتر
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g2.getFontMetrics();
            String gameOverMessage = "Game Over - Press SPACE to restart";
            int textWidth = fm.stringWidth(gameOverMessage);
            g2.drawString(gameOverMessage, getWidth() / 2 - textWidth / 2, getHeight() / 2);
        }
    }
}