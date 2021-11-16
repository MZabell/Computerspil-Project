package com.company;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class PaintPanel extends JPanel {

    public static final int PANEL_WIDTH = 500;
    public static final int PANEL_HEIGHT = 500;
    private final Player player1;
    private final Player player2;
    private final Ball ball;


    public PaintPanel(Player player1, Player player2, Ball ball, Score scoreP1, Score scoreP2) {
        this.player1 = player1;
        this.player2 = player2;
        this.ball = ball;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(Player.PLAYER1_X, player1.getY(), Player.WIDTH, Player.HEIGHT);
        g.fillRect(Player.PLAYER2_X, player2.getY(), Player.WIDTH, Player.HEIGHT);
        g.fillOval(ball.getX(), ball.getY(), Ball.WIDTH, Ball.HEIGHT);2
        g.fillRect(PANEL_WIDTH / 2 - FieldStripe.WIDTH / 2, 0, FieldStripe.WIDTH, FieldStripe.HEIGHT);
        g.setFont(new Font("Roboto", 1, 100));
        g.drawString("" + player1.score, Score.xP1, Score.yP1);
        g.drawString("" + player2.score, Score.xP2, Score.yP2);
    }

}