package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;

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
        
        if (distance <= 100) { 
            isActive = false;
        }
    }
    
    public void render(Graphics2D g2) {
        if (!isActive) {
            return;
        }
        
        g2.setColor(color);
        g2.setStroke(new BasicStroke((float)thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

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
            
        
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
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

    
    public boolean checkCollision(double markerDistance, double markerAngle) {
        
        if (!isActive) {
            return false;
        }
        
       
        double normalizedMarkerAngle = markerAngle % (2 * Math.PI);
        if (normalizedMarkerAngle < 0) {
            normalizedMarkerAngle += 2 * Math.PI;
        }
        
        // محاسبه زاویه هر بخش
        double segmentAngle = 2 * Math.PI / sides;
        
        // بررسی فاصله (آیا مارکر در نزدیکی فاصله موانع است)
        boolean withinDistance = Math.abs(markerDistance - this.distance) < thickness / 2;
        
        if (!withinDistance) {
            return false;  // اگر مارکر در محدوده فاصله موانع نباشد، برخوردی رخ نداده است
        }
        
        // بررسی زاویه
        for (int i = 0; i < sides; i++) {
            // اگر این بخش باز است (openSegment)، آن را رد کن
            if (i == openSegment) {
                continue;
            }
            
            // محاسبه محدوده زاویه برای این بخش
            double segmentStartAngle = rotationAngle + segmentAngle * i;
            double segmentEndAngle = rotationAngle + segmentAngle * (i + 1);
            
            // نرمال‌سازی زاویه‌ها
            segmentStartAngle = segmentStartAngle % (2 * Math.PI);
            if (segmentStartAngle < 0) segmentStartAngle += 2 * Math.PI;
            
            segmentEndAngle = segmentEndAngle % (2 * Math.PI);
            if (segmentEndAngle < 0) segmentEndAngle += 2 * Math.PI;
            
            // بررسی آیا مارکر در این بخش قرار دارد
            boolean withinSegment;
            if (segmentStartAngle > segmentEndAngle) {
                withinSegment = (normalizedMarkerAngle >= segmentStartAngle || 
                                normalizedMarkerAngle <= segmentEndAngle);
            } else {
                withinSegment = (normalizedMarkerAngle >= segmentStartAngle && 
                                normalizedMarkerAngle <= segmentEndAngle);
            }
            
            if (withinSegment) {
                return true;  // برخورد تشخیص داده شد
            }
        }
        
        return false;  // برخوردی تشخیص داده نشد
    }
}