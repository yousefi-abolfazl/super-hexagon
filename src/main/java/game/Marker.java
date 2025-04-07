package game;

import java.awt.geom.GeneralPath;
import java.awt.Graphics2D;

class Marker {
    double centerX;
    double centerY;
    int radius;
    double angle;


    public Marker(int radius, double angle) {
        this.radius = radius;
        this.angle = angle;
    }

    public void rotationalAngle() {
        this.angle += 0.001;
    }

    public void render(Graphics2D g2) {
        GeneralPath Path = new GeneralPath();
        this.centerX =  (Math.cos(this.angle) * this.radius);
        this.centerY =  (Math.sin(this.angle) * this.radius);
        double s1x = (Math.cos(this.angle + 0.001) * this.radius);
        double s1y = (Math.sin(this.angle + 0.001) * this.radius);
        double s2x = (Math.cos(this.angle + 0.0005)) * (this.radius + 0.1);
        double s2y = (Math.sin(this.angle + 0.0005)) * (this.radius + 0.1);

        Path.moveTo(centerX , centerY);
        Path.lineTo(s1x, s1y);
        Path.lineTo(s2x, s2y);
        Path.closePath();

        g2.draw(Path);
    }
}