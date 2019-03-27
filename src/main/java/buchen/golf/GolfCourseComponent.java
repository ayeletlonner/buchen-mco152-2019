package buchen.golf;

import buchen.physics.Projectile;

import javax.swing.*;
import java.awt.*;

public class GolfCourseComponent extends JComponent {

    private static final int BALL_SIZE = 15;
    private static int BALL_START = 30;
    private static final int FLAG_X = 770;
    private static final int POLE_WIDTH = 5;
    private static final int POLE_HEIGHT = 175;
    private static final int FLAG_WIDTH = 45;
    private static final int FLAG_HEIGHT = 30;

    private final Image cloud = new ImageIcon("cloud.png").getImage();
    private final Image golfBall = new ImageIcon("golf_ball.png").getImage();
    Grass grass = new Grass("grass.png");
    private final Image grassImage = grass.image;
    private final Color[] groundColors = new Color[] {
            Color.GREEN,
            new Color(186, 127, 61),    // light brown
            new Color(135, 79, 16),     // medium brown
            new Color(81, 43, 0)};      // dark brown
    private int groundLevel;
    private Projectile projectile = new Projectile(0, 0);

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        groundLevel = getHeight() - (getHeight() / 3);

        drawSky(graphics);
        drawClouds(graphics);
        drawGround(graphics);
        drawGrass(graphics, 0, groundLevel - grass.grassHeight);
        drawFlagPole(graphics);
        drawFlag(graphics);
        drawBall(graphics);
        repaint();
    }

    public void goClicked(double velocity, double degrees) {
        projectile = new Projectile(velocity, degrees);
    }

    private void drawSky(Graphics graphics) {
        graphics.setColor(Color.CYAN);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawGround(Graphics graphics) {
        int[] groundY = new int[] {groundLevel, groundLevel + 60, groundLevel + 100, groundLevel + 180};
        int[] groundHeight = new int[] {getHeight(), 50, 80, getHeight()};

        for (int i = 0; i < groundColors.length; i++) {
            graphics.setColor(groundColors[i]);
            graphics.fillRect(0, groundY[i], getWidth(), groundHeight[i]);
        }
    }

    private void drawBall(Graphics graphics) {
        graphics.drawImage(golfBall,
                BALL_START + (int) projectile.getX(), groundLevel - BALL_SIZE - (int) projectile.getY(), BALL_SIZE, BALL_SIZE, null);
        if (projectile.getY() >= 0) {
            projectile.addTime(0.02);
        }
    }

    private void drawFlagPole(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        graphics.fillRect(FLAG_X, groundLevel - POLE_HEIGHT, POLE_WIDTH, POLE_HEIGHT);
    }

    private void drawFlag(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillPolygon(
                new int[] {
                        FLAG_X,
                        FLAG_X - FLAG_WIDTH,
                        FLAG_X},
                new int[] {
                        groundLevel - POLE_HEIGHT,
                        groundLevel - (POLE_HEIGHT - FLAG_HEIGHT / 2),
                        groundLevel - (POLE_HEIGHT - FLAG_HEIGHT)},
                3);
    }

    private void drawClouds(Graphics graphics) {
        Cloud[] clouds = new Clouds(groundLevel).getClouds();
        for (int i = 0; i < clouds.length; i++) {
            graphics.drawImage(cloud, (int) clouds[i].xPosition, (int) clouds[i].yPosition, clouds[i].width, clouds[i].height, null);
            clouds[i].xPosition += 0.05; // TODO figure out why this isn't working
        }
    }

    private void drawGrass(Graphics graphics, int xPosition, int yPosition) {
        graphics.drawImage(grassImage, xPosition, yPosition, grass.grassWidth, grass.grassHeight, null);
        int imageXEnd = xPosition + grass.grassWidth;
        if (imageXEnd < GolfFrame.WIDTH) {
            drawGrass(graphics, imageXEnd, yPosition);
        }
    }
}
