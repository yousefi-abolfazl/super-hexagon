package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.awt.Point;


public class Polygon {
    private int centerX;
    private int centerY;
    private int size;
    private int sides;
    private Color color;
    private double rotationAngle;
    private ArrayList<Point> vertices;
    
    public Polygon(int centerX, int centerY, int size, int sides, Color color) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.size = size;
        this.sides = sides;
        this.color = color;
        this.rotationAngle = 0;
        this.vertices = new ArrayList<>();
    }
    
    public void update(double deltaTime) {
        this.rotationAngle += 1.5 * deltaTime;
        
    }
    
    
    public void render(Graphics2D g2) {
        GeneralPath path = createPolygonPath();
        
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.draw(path);
    }
    
    
    private GeneralPath createPolygonPath() {
        GeneralPath path = new GeneralPath();
        
        for (int i = 0; i < sides; i++) {
            double angle = rotationAngle + 2 * Math.PI * i / sides;
            int x = (int) (centerX + size * Math.cos(angle));
            int y = (int) (centerY + size * Math.sin(angle));
            
            if (i == 0) {
                vertices.add(new Point(x, y));
                path.moveTo(x, y);
            } else {
                vertices.add(new Point(x, y));
                path.lineTo(x, y);
            }
        }
        
        path.closePath();
        return path;
    }
    
    
    public int getCenterX() {
        return centerX;
    }
    
    public int getCenterY() {
        return centerY;
    }
    
    public int getSize() {
        return size;
    }

    public ArrayList<Point> getVertices() {
        return vertices;
    }
    
    public double getRotationAngle() {
        return rotationAngle;
    }
    
    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }
}