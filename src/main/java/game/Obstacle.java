package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.GeneralPath;

public class Obstacle {
    private double distance;
    private double speed;
    private boolean isActive;
    private int centerX;
    private int centerY;
    private int sides;
    private int openSegment;
    private Color color;
    private double thickness = 40;
    private double rotationAngle;
    private double rotationSpeed = 0.1;

    public Obstacle(int centerX, int centerY, double distance, double speed, int sides, int openSegment, double rotationAngle, Color color) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.distance = distance;
        this.speed = speed;
        this.sides = sides;
        this.isActive = true;
        this.openSegment = openSegment;
        this.color = color;
        this.rotationAngle = rotationAngle;
    }
    
    public void update(double deltaTime) {
        distance -= speed * deltaTime;
        rotationAngle += rotationSpeed * deltaTime;
        
        if (distance <= 70) { 
            isActive = false;
        }
    }
    
    public void render(Graphics2D g2) {
        if (!isActive) {
            return;
        }
        
        GeneralPath path = new GeneralPath();
        
        for (int i = 0; i < sides; i++) {
            if (i == openSegment) {
                continue;
            }
            
            double angle1 = rotationAngle + 2 * Math.PI * i / sides;
            double angle2 = rotationAngle + 2 * Math.PI * ((i + 1) % sides) / sides;
            
            int x1 = (int) (centerX + distance * Math.cos(angle1));
            int y1 = (int) (centerY + distance * Math.sin(angle1));
            int x2 = (int) (centerX + distance * Math.cos(angle2));
            int y2 = (int) (centerY + distance * Math.sin(angle2));
            
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
        }
        
        g2.setColor(color);
        g2.setStroke(new BasicStroke((float)thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(path);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke((float)(thickness * 0.3), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(path);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public int getOpenSegment() {
        return openSegment;
    }
    
    public void setOpenSegment(int openSegment) {
        this.openSegment = openSegment;
    }

    public double getThickness() {
        return thickness;
    }
    
    public void setThickness(double thickness) {
        this.thickness = thickness;
    }
    
    public double getRotationAngle() {
        return rotationAngle;
    }
    
    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public int getSides() {
        return sides;
    }
}