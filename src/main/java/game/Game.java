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
    private final double OBSTACLE_SPAWN_INTERVAL = 2.0; // Spawn every 2 seconds

    public Game() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        centerX = (int) screenSize.getWidth() / 2;
        centerY = (int) screenSize.getHeight() / 2;

        marker = new Marker(150, 0);
        obstacles = new ArrayList<>();
        // Initial obstacle
        obstacles.add(new Obstacle(centerX, centerY, 700, 2, 6, 0, Color.RED));
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

        // Update obstacle spawn timer
        obstacleSpawnTimer += deltaTime;
        if (obstacleSpawnTimer >= OBSTACLE_SPAWN_INTERVAL) {
            spawnNewObstacle();
            obstacleSpawnTimer = 0;
        }

        // Update all obstacles
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update(deltaTime);

            // Remove obstacles that are too close to the center
            if (obstacle.getDistance() <= 150) {
                iterator.remove();
                continue;
            }

            // Check for collision with this obstacle
            if (checkObstacleCollision(obstacle)) {
                isGameOver = true;
                break;
            }
        }
    }

    private boolean checkObstacleCollision(Obstacle obstacle) {
        // Implementation of collision detection
        return obstacle.checkCollision(marker.getDistance(), marker.getAngle());
    }

    private void spawnNewObstacle() {
        // Generate random properties for the new obstacle
        int segmentCount = random.nextInt(3) + 1; // 1-3 segments
        int sides = 6; // Hexagon
        double rotationOffset = random.nextDouble() * Math.PI * 2; // Random rotation
        Color color = new Color(
                random.nextInt(156) + 100, // Red component (100-255)
                random.nextInt(156) + 100, // Green component (100-255)
                random.nextInt(156) + 100  // Blue component (100-255)
        );

        // Create a new obstacle starting from far away
        obstacles.add(new Obstacle(centerX, centerY, 700.0, 100 , sides, segmentCount, color));
    }

    public void render(Graphics2D g2) {
        polygon.render(g2);
        marker.render(g2);

        // Render all obstacles
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

    public Polygon getPolygon() {
        return polygon;
    }

    public void moveMarkerLeft() {
        if (marker != null && isGameRunning && !isGameOver) {
            marker.moveLeft();
        }
    }

    public void moveMarkerRight() {
        if (marker != null && isGameRunning && !isGameOver) {
            marker.moveRight();
        }
    }
}