package game;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Point;

public class Marker {
    double centerX;
    double centerY;
    int distance;
    double angle;
    double markerx;
    double markery;
    private final double ROTATION_SPEED = 0.05; // سرعت چرخش (می‌توانید تنظیم کنید)

    public Marker(int distance, double angle) {
        this.distance = distance;
        this.angle = angle;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        centerX = screenWidth / 2;
        centerY = screenHeight / 2;
    }

    // حرکت به چپ (در جهت ساعتگرد)
    public void moveLeft() {
        this.angle += ROTATION_SPEED;
    }
    
    // حرکت به راست (در جهت پادساعتگرد)
    public void moveRight() {
        this.angle -= ROTATION_SPEED;
    }

    public void render(Graphics2D g2) {
        GeneralPath Path = new GeneralPath();
        double s1x = (Math.cos(this.angle) * this.distance) + centerX;
        double s1y = centerY - (Math.sin(this.angle) * this.distance);
        
        double s2x = centerX + (Math.cos(this.angle + 0.2) * this.distance);
        double s2y = centerY - (Math.sin(this.angle + 0.2) * this.distance);
        
        markerx = centerX + (Math.cos(this.angle + 0.10)) * (this.distance + 20);
        markery = centerY - (Math.sin(this.angle + 0.10)) * (this.distance + 20);

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

    public Point getPoint() {
        return new Point((int)markerx, (int)markery);
    }
    
    // گرفتن زاویه فعلی
    public double getAngle() {
        return angle;
    }
    
    // تنظیم زاویه
    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getDistance() {
        return this.distance;
    }
}