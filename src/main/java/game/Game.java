//TODO: Add rotation to the obstacles
//TODO: Add a score system UI
//TODO: Add a game over screen

package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Game {
    private Marker marker;
    private ArrayList<Obstacle> obstacles;
    private Polygon polygon;
    private int centerX;
    private int centerY;
    private boolean isGameOver;
    private boolean isGameRunning;
    private double obstacleSpawnTimer;
    private Random random;
    private final double OBSTACLE_SPAWN_INTERVAL = 2.0;
    private int score;
    private double spawnRate = 1.0;
    private double gameSpeed = 1.0;

    public Game() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        centerX = (int) screenSize.getWidth() / 2;
        centerY = (int) screenSize.getHeight() / 2;

        marker = new Marker(150, 0);
        obstacles = new ArrayList<>();

        obstacles.add(new Obstacle(centerX, centerY, 700, 2, 6, 0, 0, Color.RED));
        polygon = new Polygon(centerX, centerY, 150, 6, Color.WHITE);

        isGameOver = false;
        isGameRunning = false;
        obstacleSpawnTimer = 0;
        random = new Random();
    }

    public void update(double deltaTime) {
        if (!isGameRunning || isGameOver) {
            return;
        }
        score += (int)(deltaTime * 10);

        obstacleSpawnTimer += deltaTime;
        if (obstacleSpawnTimer >= OBSTACLE_SPAWN_INTERVAL) {
            spawnNewObstacle();
            obstacleSpawnTimer = 0;
        }

        
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update(deltaTime);

            if (obstacle.getDistance() <= 150) {
                iterator.remove();
                continue;
            }

            if (checkObstacleCollision(obstacle)) {
                isGameOver = true;
                break;
            }
        }
    }

    private boolean checkObstacleCollision(Obstacle obstacle) {
        double markerDistance = marker.getDistance();
        double markerAngle = marker.getAngle();
        double obstacleDistance = obstacle.getDistance();
        double obstacleThickness = obstacle.getThickness();

        if (Math.abs(markerDistance - obstacleDistance) > obstacleThickness / 2) {
            return false;
        }

        int openSegment = obstacle.getOpenSegment();
        int sides = obstacle.getSides();
        double rotationAngle = obstacle.getRotationAngle();
        double segmentAngle = 2 * Math.PI / sides;
        double openSegmentStart = normalizeAngle(rotationAngle + segmentAngle * openSegment);
        double openSegmentEnd = normalizeAngle(rotationAngle + segmentAngle * (openSegment + 1));
        
        
        boolean isInOpenSegment;
        if (openSegmentStart > openSegmentEnd) {
            isInOpenSegment = (markerAngle >= openSegmentStart || 
                            markerAngle <= openSegmentEnd);
        } else {
            isInOpenSegment = (markerAngle >= openSegmentStart && 
                            markerAngle <= openSegmentEnd);
        }
        
        if (Math.abs(markerDistance - obstacleDistance) < 30) {
        System.out.println("Marker angle: " + markerAngle + 
                      ", Open segment: " + openSegmentStart + " to " + openSegmentEnd +
                      ", In open segment: " + isInOpenSegment);
        }
        return !isInOpenSegment;
    }
    private double normalizeAngle(double angle) {
        double normalized = angle % (2 * Math.PI);
        if (normalized < 0) {
            normalized += 2 * Math.PI;
        }
        return normalized;
    }
    
    public void render(Graphics2D g2) {
        polygon.render(g2);
        marker.render(g2);

        for (Obstacle obstacle : obstacles) {
            obstacle.render(g2);
        }
    }

    public void startGame() {
        isGameRunning = true;
        isGameOver = false;
    }

    public void stopGame() {
        isGameRunning = false;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setSpawnRate(double rate) {
        this.spawnRate = rate;
    }

    private void spawnNewObstacle() {
        double actualSpawnInterval = OBSTACLE_SPAWN_INTERVAL / spawnRate;
        double baseObstacleSpeed = 100;
        double currentObstacleSpeed = baseObstacleSpeed * gameSpeed;
        if (obstacleSpawnTimer >= actualSpawnInterval) {
            
            int segmentCount = random.nextInt(3) + 1; // 1-3 segments
            int sides = 6; 
            double rotationAngle = random.nextDouble() * Math.PI * 2; // Random rotation
            Color color = new Color(
                    random.nextInt(156) + 100, // Red component (100-255)
                    random.nextInt(156) + 100, // Green component (100-255)
                    random.nextInt(156) + 100);  // Blue component (100-255)
            obstacles.add(new Obstacle(centerX, centerY, 700, currentObstacleSpeed , sides, segmentCount, rotationAngle, color));
            obstacleSpawnTimer = 0;
        }
    }
    public int getScore() {
        return score;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void moveMarkerLeft(double deltaTime) {
        if (marker != null && isGameRunning && !isGameOver) {
            System.out.println("moveLeft"); //logs
            marker.moveLeft(deltaTime);
        }
    }

    public void moveMarkerRight(double deltaTime) {
        if (marker != null && isGameRunning && !isGameOver) {
            System.out.println("moveRight"); //logs
            marker.moveRight(deltaTime);
        }
    }

    public double getGameSpeed() {
        return gameSpeed;
    }
    public void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }
}