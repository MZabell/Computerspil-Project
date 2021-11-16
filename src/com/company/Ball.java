package com.company;

import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

public class Ball {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private int x;
    private int y;
    public int ranDir;
    private int xDir;
    private int yDir;

    public Ball() {
        x = PaintPanel.PANEL_WIDTH / 2 - Ball.WIDTH / 2;
        y = PaintPanel.PANEL_HEIGHT / 2 - Ball.WIDTH / 2;

        ranDir = ThreadLocalRandom.current().nextInt(-5, 5 + 1);
        while (ranDir == 0)
            ranDir = ThreadLocalRandom.current().nextInt(-2, 2 + 1);

        xDir = -2;
        yDir = ranDir;
    }

    public void updateBall() {
        setX(getX() + xDir);
        setY(getY() + yDir);

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXDir() {
        return xDir;
    }

    public void setXDir(int xDir) {
        this.xDir = xDir;
    }

    public int getWidth() {
        return WIDTH;
    }

    public void setYDir(int yDir) {
        this.yDir = yDir;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}
