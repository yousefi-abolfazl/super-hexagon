package ui;


import javax.swing.*;
import javazoom.jl.player.Player;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SettingsMenu {
    private SceneManager sceneManager;
    private JPanel settingsPanel;    
    private JSlider volumeSlider;
    private Player player;
    private Thread musicThread;
    private JComboBox<String> difficultyComboBox;
    private JButton backButton;
    private JCheckBox musicCheckBox;
    
    private final Color DARK_BLUE = new Color(0, 0, 60);
    private final Color MEDIUM_BLUE = new Color(40, 40, 160);
    
    public SettingsMenu(SceneManager manager) {
        this.sceneManager = manager;
        this.settingsPanel = new JPanel();
        setupSettingsMenu();
    }
    
    public void setupSettingsMenu() {
        settingsPanel.setBackground(DARK_BLUE);
        settingsPanel.setLayout(null);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int)screenSize.getWidth();
        int screenHeight = (int)screenSize.getHeight();
        int spacing = (int)(screenWidth * 0.02);
        
        int labelWidth = (int)(screenWidth * 0.2);
        int componentWidth = (int)(screenWidth * 0.3);
        int height = (int)(screenHeight * 0.05);
        int margin = (int)(screenHeight * 0.02);
        
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, (int)(screenHeight * 0.06)));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(screenWidth/2 - labelWidth/2, margin*2, labelWidth*2, height*2);
        
        JLabel volumeLabel = new JLabel("Volume:");
        volumeLabel.setFont(new Font("Arial", Font.BOLD, (int)(screenHeight * 0.03)));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setBounds(screenWidth/2 - labelWidth - margin, spacing + margin*6, labelWidth, height);
        
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(DARK_BLUE);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setBounds(screenWidth/2, spacing + margin*6, componentWidth, height);
        
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, (int)(screenHeight * 0.03)));
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setBounds(screenWidth/2 - labelWidth - margin, spacing + margin*9, labelWidth, height);
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setBounds(screenWidth/2, spacing + margin*9, componentWidth, height);
        
        musicCheckBox = new JCheckBox("Music");
        musicCheckBox.setFont(new Font("Arial", Font.BOLD, (int)(screenHeight * 0.03)));
        musicCheckBox.setForeground(Color.WHITE);
        musicCheckBox.setBackground(DARK_BLUE);
        musicCheckBox.setBounds(screenWidth/2 - componentWidth/2, spacing + margin*12, componentWidth, height);
        musicCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        musicCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
        musicCheckBox.setSelected(true);
        
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, (int)(screenHeight * 0.03)));
        backButton.setBackground(MEDIUM_BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        backButton.setBounds(screenWidth/2 - componentWidth/2, spacing + margin*18, componentWidth, height*2);

        backButton.addActionListener(e -> {
            sceneManager.showMainMenu();
        });

        settingsPanel.add(titleLabel);
        settingsPanel.add(volumeLabel);
        settingsPanel.add(volumeSlider);
        settingsPanel.add(difficultyLabel);
        settingsPanel.add(difficultyComboBox);
        settingsPanel.add(musicCheckBox);
        settingsPanel.add(backButton);

        settingsPanel.setVisible(true);
    }
    
    public int getVolume() {
        return volumeSlider.getValue();
    }
    
    public String getDifficulty() {
        return (String) difficultyComboBox.getSelectedItem();
    }
    
    public boolean hasMusic() {
        return musicCheckBox.isSelected();
    }

    public JPanel getPanel() {
        return settingsPanel;
    }

    public void music() {
        //stopMusic();
        musicThread = new Thread(() -> {
            while (hasMusic()) {
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/sounds/Courtesy.mp3");
                    if (inputStream == null) {
                        System.out.println("File not founded");
                        break;
                    }
                    BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);

                    player = new Player(bufferedStream);
                    player.play();

                } catch (Exception e) {
                    if (hasMusic()) {
                        e.printStackTrace();
                        System.out.println("Failed to play music " + e.getMessage());
                    }
                    break;
                }
            }
        });
        musicThread.start();
    }
}