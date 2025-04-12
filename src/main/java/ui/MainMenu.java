package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

public class MainMenu {
    private JPanel mainPanel;
    private JButton startButton;
    private JButton settingsButton;
    private JButton preButton;
    private JButton nextButton;
    private JButton exitButton;
    private SceneManager sceneManager;
    private int highScore = 0;
    private int screenWidth;
    private int screenHeight;


    private final Color DARK_BLUE = new Color(0, 0, 60);
    private final Color MEDIUM_BLUE = new Color(40, 40, 160);
    private final Color LIGHT_BLUE = new Color(80, 80, 255);

    public MainMenu(SceneManager manager) {
        this.sceneManager = manager;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = (int) screenSize.getWidth();
        this.screenHeight = (int) screenSize.getHeight();
        this.mainPanel = new HexagonBackgroundPanel();
        this.startButton = createHexButton("START GAME", MEDIUM_BLUE);
        this.nextButton = createArrowButton("NEXT", MEDIUM_BLUE, true);
        this.preButton = createArrowButton("PREVIOUS", MEDIUM_BLUE, false);
        this.exitButton = new JButton("EXIT");
        this.settingsButton = new JButton("SETTINGS");
    }

    public void setupMainMenu() {
        mainPanel = new HexagonBackgroundPanel();
        mainPanel.setLayout(null);
        // frame.setSize(screenWidth, screenHeight);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupPanel();
        setupButton();
        // settingsExitButton();
        JLabel logoLabel = createLogoLabel();
        mainPanel.add(logoLabel);
        
        // frame.add(mainPanel);
        // frame.setVisible(true);
    }

    private JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel("SUPER HEXAGON") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                
                int titleFontSize = (int)(screenHeight * 0.1); 
                Font titleFont = new Font("Arial", Font.BOLD, titleFontSize);
                
                int shadowOffset = titleFontSize / 16;
                
                g2.setFont(titleFont);
                g2.setColor(new Color(0, 0, 30));
                g2.drawString("SUPER", shadowOffset + 3, titleFontSize + shadowOffset);
                g2.drawString("HEXAGON", shadowOffset + 3, titleFontSize * 2 + shadowOffset);
                
                g2.setColor(Color.WHITE);
                g2.drawString("SUPER", 3, titleFontSize);
                g2.drawString("HEXAGON", 3, titleFontSize * 2);
                
                g2.dispose();
            }
        };
        
       
        int logoWidth = (int)(screenWidth * 0.5);
        int logoHeight = (int)(screenHeight * 0.25);
        
        
        int logoX = screenWidth / 2 - logoWidth / 2 + (int)(screenWidth * 0.1);
        int logoY = (int)(screenHeight * 0.15);
        
        logoLabel.setBounds(logoX, logoY, logoWidth, logoHeight);
        return logoLabel;
    }

   
    private class HexagonBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(DARK_BLUE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            int size = Math.min(getWidth(), getHeight()) - 100;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            Polygon hexagon = createHexagonShape(centerX, centerY, size);
            g2.setColor(MEDIUM_BLUE);
            g2.setStroke(new BasicStroke(4));
            g2.draw(hexagon);

            drawStripes(g2);
            
            g2.dispose();
        }
        
        private Polygon createHexagonShape(int centerX, int centerY, int size) {
            Polygon hexagon = new Polygon();
            for (int i = 0; i < 6; i++) {
                double angle = 2 * Math.PI * i / 6;
                int x = (int) (centerX + size * Math.cos(angle));
                int y = (int) (centerY + size * Math.sin(angle));
                hexagon.addPoint(x, y);
            }
            return hexagon;
        }
        
        private void drawStripes(Graphics2D g2) {
            g2.setColor(LIGHT_BLUE);
            int height = getHeight();
            int width = getWidth();
            
            for (int i = -height; i < width + height; i += 100) {
                g2.setStroke(new BasicStroke(15));
                g2.drawLine(i, 0, i + height, height);
            }
        }
    }

    public void setupPanel() {
        mainPanel.setPreferredSize(new Dimension(800, 600)); //BUG: check this
        mainPanel.setLayout(null);
    }
    
    public void setupButton() {
        int buttonWidth = (int)(screenWidth * 0.12);
        int buttonHeight = (int)(screenHeight * 0.07);
        int spacing = (int)(screenWidth * 0.02);
    
        
        int centerX = (screenWidth / 2) - (buttonWidth / 2);
        int leftX = centerX - buttonWidth - spacing;
        int rightX = centerX + buttonWidth + spacing;

        int mainRowY = (int)(screenHeight * 0.6);
        int topRowY = (int)(screenHeight * 0.1);
        
        int fontSize = (int)(buttonWidth * 0.10);
        
        Font buttonFont = new Font("Arial", Font.BOLD, fontSize);
        startButton.setFont(buttonFont);
        preButton.setFont(buttonFont);
        nextButton.setFont(buttonFont);
        settingsButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);
        
        startButton.setBounds(centerX, mainRowY, buttonWidth, buttonHeight);
        preButton.setBounds(leftX, mainRowY, buttonWidth, buttonHeight);
        nextButton.setBounds(rightX, mainRowY, buttonWidth, buttonHeight);
        
        settingsButton.setBounds(0, topRowY, buttonWidth, buttonHeight);
        exitButton.setBounds(screenWidth - buttonWidth, topRowY, buttonWidth, buttonHeight);

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        settingsButton.addActionListener(e -> {
            sceneManager.showSettings();
        });

        startButton.addActionListener(e -> {
            sceneManager.startGame("Normal");
        });
        
        mainPanel.add(startButton);
        mainPanel.add(nextButton);
        mainPanel.add(preButton);
        mainPanel.add(exitButton);
        mainPanel.add(settingsButton);
    }

    private JButton createHexButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
               
                int indent = h / 3;
                
                GeneralPath path = new GeneralPath();
                path.moveTo(indent, 0);
                path.lineTo(w - indent, 0);
                path.lineTo(w, h / 2);
                path.lineTo(w - indent, h);
                path.lineTo(indent, h);
                path.lineTo(0, h / 2);
                path.closePath();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, color.brighter(),
                    0, h, color.darker()
                );
                g2.setPaint(gradient);
                g2.fill(path);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.draw(path);
                
                g2.setColor(Color.WHITE);
                
                int adaptiveFontSize = (int)(w * 0.10);
                Font adaptiveFont = new Font("Arial", Font.BOLD, adaptiveFontSize);
                g2.setFont(adaptiveFont);
                
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getHeight();
                
                g2.drawString(text, (w - textWidth) / 2, h / 2 + textHeight / 4);
                
                g2.dispose();
            }
            
            @Override
            public boolean contains(int x, int y) {
                int w = getWidth();
                int h = getHeight();
                int indent = h / 3;
                
                GeneralPath path = new GeneralPath();
                path.moveTo(indent, 0);
                path.lineTo(w - indent, 0);
                path.lineTo(w, h / 2);
                path.lineTo(w - indent, h);
                path.lineTo(indent, h);
                path.lineTo(0, h / 2);
                path.closePath();
                
                return path.contains(x, y);
            }
        };
        
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        return button;
    }

    public void settingsExitButton() {
        
        settingsButton.setBackground(MEDIUM_BLUE);
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        exitButton.setBackground(MEDIUM_BLUE);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    private JButton createArrowButton(String text, Color color, boolean isRightArrow) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                int indent = h / 2;
                
                GeneralPath path = new GeneralPath();
                if (isRightArrow) {
                    path.moveTo(0, 0);
                    path.lineTo(w - indent, 0);
                    path.lineTo(w, h / 2);
                    path.lineTo(w - indent, h);
                    path.lineTo(0, h);
                } else {
                    path.moveTo(indent, 0);
                    path.lineTo(w, 0);
                    path.lineTo(w, h);
                    path.lineTo(indent, h);
                    path.lineTo(0, h / 2);
                }
                path.closePath();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, color.brighter(),
                    0, h, color.darker()
                );
                g2.setPaint(gradient);
                g2.fill(path);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.draw(path);
                
                g2.setColor(Color.WHITE);
                int arrowSize = h / 3;
                int arrowX = (w - arrowSize) / 2;
                int arrowY = (h - arrowSize) / 2;
                
                GeneralPath arrow = new GeneralPath();
                if (isRightArrow) {
                    arrow.moveTo(arrowX, arrowY);
                    arrow.lineTo(arrowX + arrowSize, arrowY + arrowSize / 2);
                    arrow.lineTo(arrowX, arrowY + arrowSize);
                } else {
                    arrow.moveTo(arrowX + arrowSize, arrowY);
                    arrow.lineTo(arrowX, arrowY + arrowSize / 2);
                    arrow.lineTo(arrowX + arrowSize, arrowY + arrowSize);
                }
                arrow.closePath();
                g2.fill(arrow);
                
                g2.dispose();
            }
            
            @Override
            public boolean contains(int x, int y) {
                int w = getWidth();
                int h = getHeight();
                int indent = h / 2;
                
                GeneralPath path = new GeneralPath();
                if (isRightArrow) {
                    path.moveTo(0, 0);
                    path.lineTo(w - indent, 0);
                    path.lineTo(w, h / 2);
                    path.lineTo(w - indent, h);
                    path.lineTo(0, h);
                } else {
                    path.moveTo(indent, 0);
                    path.lineTo(w, 0);
                    path.lineTo(w, h);
                    path.lineTo(indent, h);
                    path.lineTo(0, h / 2);
                }
                path.closePath();
                
                return path.contains(x, y);
            }
        };
        
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        return button;
    }
    public JPanel getPanel() {
        return mainPanel;
    }
    
    public void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            // به‌روزرسانی نمایش امتیاز بالا
            // ...
        }
    }
}
