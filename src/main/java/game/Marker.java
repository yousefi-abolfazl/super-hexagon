package game;

import java.awt.*;
import java.awt.geom.*;

class Marker {
    double centerX;
    double centerY;
    int radius;
    double angle;


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
        double s1x =  (Math.cos(this.angle) * this.radius) + centerX;
        double s1y = centerY - (Math.sin(this.angle) * this.radius);
        double s2x = centerX + (Math.cos(this.angle + 0.001) * this.radius);
        double s2y = centerY - (Math.sin(this.angle + 0.001) * this.radius);
        double s3x = centerX + (Math.cos(this.angle + 0.0005)) * (this.radius + 0.1);
        double s3y = centerY - (Math.sin(this.angle + 0.0005)) * (this.radius + 0.1);

        Path.moveTo(centerX , centerY);
        Path.lineTo(s1x, s1y);
        Path.lineTo(s2x, s2y);
        Path.lineTo(s3x, s3y);
        Path.closePath();

        g2.draw(Path);
    }
}