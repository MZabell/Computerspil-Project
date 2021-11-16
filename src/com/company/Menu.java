package com.company;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;


public class Menu extends JPanel {

    public static JButton pvp = new JButton("Player vs Player");
    public static JButton pvAI = new JButton("Player vs AI");
    public static JButton aivAI = new JButton("AI vs AI");
    public static JButton difficulty = new JButton("Difficulty: EASY");

    public Menu() {
        setPreferredSize(new Dimension(PaintPanel.PANEL_WIDTH, PaintPanel.PANEL_HEIGHT));
        setLayout(null);
        int BUTTON_WIDTH = 200;
        int BUTTON_HEIGHT = 30;
        add(pvp).setBounds(PaintPanel.PANEL_WIDTH / 2 - BUTTON_WIDTH / 2, 200, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(pvAI).setBounds(PaintPanel.PANEL_WIDTH / 2 - BUTTON_WIDTH, 300, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(difficulty).setBounds(PaintPanel.PANEL_WIDTH / 2, 300, BUTTON_WIDTH, BUTTON_HEIGHT);
        add(aivAI).setBounds(PaintPanel.PANEL_WIDTH / 2 - BUTTON_WIDTH / 2, 400, BUTTON_WIDTH, BUTTON_HEIGHT);

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Roboto", 1, 80));
        g.drawString("Ping Pong!", 30, 100);
    }
}
