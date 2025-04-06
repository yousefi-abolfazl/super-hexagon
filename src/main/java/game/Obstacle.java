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
    private double thickness = 20;
    private double rotationAngle;
    private double rotationSpeed = 0.1;

    public Obstacle(int centerX, int centerY, double distance, double speed, int sides, int openSegment, Color color) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.distance = distance;
        this.speed = speed;
        this.sides = sides;
        this.isActive = true;
        this.openSegment = openSegment;
        this.color = color;
        this.rotationAngle = 0;
    }
    
    public void update(double deltaTime) {
        distance -= speed * deltaTime;
        rotationAngle += rotationSpeed * deltaTime;
        
        if (distance <= 30) { 
            isActive = false;
        }
    }
    
    public void render(Graphics2D g2) {
        if (!isActive) {
            return;
        }
        
        g2.setColor(color);
        g2.setStroke(new BasicStroke((float)thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

        double anglePerSide = 2 * Math.PI / sides;
        double innerRadius = distance - thickness / 2;
        double outerRadius = distance + thickness / 2;

        if (innerRadius < 0) innerRadius = 0;
        if (outerRadius < 0) outerRadius = 0;

        for (int i = 0; i < sides; i++) {
            if (i == openSegment) {
                continue;
            }

            double startAngle = rotationAngle + i * anglePerSide;
            double endAngle = startAngle + anglePerSide;

            GeneralPath segmentPath = new GeneralPath();

            double x1 = centerX + innerRadius * Math.cos(startAngle);
            double y1 = centerY + innerRadius * Math.sin(startAngle);
            double x2 = centerX + outerRadius * Math.cos(startAngle);
            double y2 = centerY + outerRadius * Math.sin(startAngle);
            double x3 = centerX + outerRadius * Math.cos(endAngle);
            double y3 = centerY + outerRadius * Math.sin(endAngle);
            double x4 = centerX + innerRadius * Math.cos(endAngle);
            double y4 = centerY + innerRadius * Math.sin(endAngle);

            segmentPath.moveTo(x1, y1);
            segmentPath.lineTo(x2, y2);
            segmentPath.lineTo(x3, y3);
            segmentPath.lineTo(x4, y4);
            segmentPath.closePath();

            g2.draw(segmentPath);
        }
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

    public double getThickness() {
        return thickness;
    }
    
    public double getRotationAngle() {
        return rotationAngle;
    }
}
