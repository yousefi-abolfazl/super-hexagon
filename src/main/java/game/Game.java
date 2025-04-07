package game;

import java.awt.*;

public class Game {
    private Marker marker;
    private Obstacle obstacle;
    private Polygon polygon;
    private int centerX;
    private int centerY;

    public Game() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        centerX = (int) screenSize.getWidth() / 2;
        centerY = (int) screenSize.getHeight() / 2;
        
        marker = new Marker(100, 0);
        obstacle = new Obstacle(centerX, centerY, 200, 2, 6, 0, Color.RED);
        polygon = new Polygon(centerX, centerY, 150, 6, Color.WHITE);
    }

    public void update(double deltaTime) {
        marker.rotationalAngle();
        obstacle.update(deltaTime);
    }

    public void render(Graphics2D g2) {
        marker.render(g2);
        obstacle.render(g2);
        polygon.render(g2);
    }

    public void startGame() {
        
    }

    public void stopGame() {

    }
}