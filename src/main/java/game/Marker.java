package game;

import java.awt.*;
import java.awt.geom.*;

public class Marker {
    private double centerX;
    private double centerY;
    private int distance;
    private double angle;
    private double markerx;
    private double markery;
    private double moveSpeed = 1.0;

    public Marker(int distance, double angle) {
        this.distance = distance;
        this.angle = angle;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        centerX = screenWidth / 2;
        centerY = screenHeight / 2;
    }

    public void moveLeft(double deltaTime) {
        angle += moveSpeed * deltaTime * 2;
        normalizeAngle();
    }
    
    public void moveRight(double deltaTime) {
        angle -= moveSpeed * deltaTime * 2;
        normalizeAngle();        
    }

    public void render(Graphics2D g2) {
        GeneralPath Path = new GeneralPath();
        double s1x = (Math.cos(this.angle) * this.distance) + centerX;
        double s1y = centerY + (Math.sin(this.angle) * this.distance);
        
        double s2x = centerX + (Math.cos(this.angle + 0.2) * this.distance);
        double s2y = centerY + (Math.sin(this.angle + 0.2) * this.distance);
        
        markerx = centerX + (Math.cos(this.angle + 0.10)) * (this.distance + 20);
        markery = centerY + (Math.sin(this.angle + 0.10)) * (this.distance + 20);

        Path.moveTo(s1x, s1y);
        Path.lineTo(s2x, s2y);
        Path.lineTo(markerx, markery);
        Path.closePath();

        g2.setColor(Color.BLUE);
        g2.fill(Path);
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(Path);
    }

    public void normalizeAngle() {
        while (this.angle > 2 * Math.PI) {
            this.angle -= 2 * Math.PI;
        }
        while (this.angle < 0) {
            this.angle += 2 * Math.PI;
        }
    }

    public Point getPoint() {
        return new Point((int)markerx, (int)markery);
    }
    
    public double getAngle() {
        return angle;
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
        normalizeAngle();
    }

    public int getDistance() {
        return this.distance;
    }
}