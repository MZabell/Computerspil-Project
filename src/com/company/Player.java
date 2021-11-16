package com.company;

import java.awt.Rectangle;

public class Player {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 75;
    public static final int PLAYER1_X = 10;
    public static final int PLAYER2_X = PaintPanel.PANEL_WIDTH - Player.WIDTH - 10;
    public int score = 0;
    private int y = PaintPanel.PANEL_HEIGHT / 2 - HEIGHT / 2;
    private boolean upPressed;
    private boolean downPressed;
    private boolean alreadyChecked;

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    private boolean isAI;

    public boolean isAlreadyChecked() {
        return alreadyChecked;
    }

    public void setAlreadyChecked(boolean alreadyChecked) {
        this.alreadyChecked = alreadyChecked;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (y > this.y)
            this.y = Math.min(PaintPanel.PANEL_HEIGHT - HEIGHT, y);
        else if (y < this.y)
            this.y = Math.max(0, y);
    }

    public Rectangle getBoundsP1() {
        return new Rectangle(PLAYER1_X, y, WIDTH, HEIGHT);
    }

    public Rectangle getBoundsP2() {
        return new Rectangle(PLAYER2_X, y, WIDTH, HEIGHT);
    }
}
