package game;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Point;

class Marker {
    double centerX;
    double centerY;
    int radius;
    double angle;
    double markerx;
    double markery;


    public Marker(int radius, double angle) {
        this.radius = radius;
        this.angle = angle;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        centerX = screenWidth / 2;
        centerY = screenHeight / 2;
    }

    public void rotationalAngle() {
        this.angle += 0.001;
    }

    public void render(Graphics2D g2) {
        GeneralPath Path = new GeneralPath();
        double s1x = (Math.cos(this.angle) * this.radius) + centerX;
        double s1y = centerY - (Math.sin(this.angle) * this.radius);
        
        double s2x = centerX + (Math.cos(this.angle + 0.2) * this.radius);
        double s2y = centerY - (Math.sin(this.angle + 0.2) * this.radius);
        
        double markerx = centerX + (Math.cos(this.angle + 0.10)) * (this.radius + 20);
        double markery = centerY - (Math.sin(this.angle + 0.10)) * (this.radius + 20);

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
}