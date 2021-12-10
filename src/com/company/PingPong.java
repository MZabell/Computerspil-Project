package com.company;

import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ThreadLocalRandom;

public class PingPong extends JFrame implements SerialConnectionHandler.SerialListener {

    private static final int REFRESH_RATE = 60;

    private final KeyManager keyManager;
    private final Player player1;
    private final Player player2;
    private final Ball ball;
    private boolean isRunning;
    private int overShoot;
    private int diffOverShoot;
    private int diffCount = 2;
    private long lastTime;

    public PingPong() {
        player1 = new Player();
        player2 = new Player();
        ball = new Ball();
        SerialConnectionHandler serialHandler = new SerialConnectionHandler(this);
        Score scoreP1 = new Score();
        Score scoreP2 = new Score();

        setTitle("Ping Pong");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (serialHandler.isConnected())
                    serialHandler.stopSerial();
                else
                    return;
            }
        });
        setVisible(true);
        Menu menu = new Menu();
        getContentPane().add(menu);
        pack();

        keyManager = new KeyManager();
        Menu.pvp.addActionListener(e -> new Thread(() -> {
            getContentPane().removeAll();
            PaintPanel paintPanel = new PaintPanel(player1, player2, ball, scoreP1, scoreP2);
            getContentPane().add(paintPanel);
            revalidate();
            paintPanel.addKeyListener(keyManager);
            paintPanel.requestFocusInWindow();
            isRunning = true;
            gameLoop();
        }).start());

        Menu.pvAI.addActionListener(e -> new Thread(() -> {
            getContentPane().removeAll();
            PaintPanel paintPanel = new PaintPanel(player1, player2, ball, scoreP1, scoreP2);
            getContentPane().add(paintPanel);
            revalidate();
            paintPanel.addKeyListener(keyManager);
            paintPanel.requestFocusInWindow();
            isRunning = true;
            player2.setAI(true);
            switch (Menu.difficulty.getText()) {
                case "Difficulty: EASY" -> setDiffOverShoot(10);
                case "Difficulty: NORMAL" -> setDiffOverShoot(5);
                case "Difficulty: HARD" -> setDiffOverShoot(4);
            }
            gameLoop();
        }).start());

        Menu.aivAI.addActionListener(e -> new Thread(() -> {
            getContentPane().removeAll();
            PaintPanel paintPanel = new PaintPanel(player1, player2, ball, scoreP1, scoreP2);
            getContentPane().add(paintPanel);
            revalidate();
            paintPanel.addKeyListener(keyManager);
            paintPanel.requestFocusInWindow();
            isRunning = true;
            player2.setAI(true);
            player1.setAI(true);
            gameLoop();
        }).start());

        Menu.difficulty.addActionListener(e -> {
            switch (getDiffCount()) {
                case 1 -> Menu.difficulty.setText("Difficulty: EASY");
                case 2 -> Menu.difficulty.setText("Difficulty: NORMAL");
                case 3 -> {
                    Menu.difficulty.setText("Difficulty: HARD");
                    setDiffCount(0);
                }
            }
            diffCount++;
        });
    }

    public static void main(String[] args) {
        new PingPong();
    }

    public int getDiffOverShoot() {
        return diffOverShoot;
    }

    public void setDiffOverShoot(int diffOverShoot) {
        this.diffOverShoot = diffOverShoot;
    }

    public int getDiffCount() {
        return diffCount;
    }

    public void setDiffCount(int diffCount) {
        this.diffCount = diffCount;
    }

    private void gameLoop() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            // Check if AI
            playerVsAI();
            aiVsAI();
            // Update positions
            updatePlayerPosition(player1);
            updatePlayerPosition(player2);
            // Update ball
            ball.updateBall();
            // Check win
            winCondition();
            // Check collision
            playerCollision();
            borderCollision();
            // Repaint screen
            repaint();
            long timeElapsed = System.currentTimeMillis() - startTime;
            if (timeElapsed < (int) (1f / REFRESH_RATE * 1000)) {
                try {
                    Thread.sleep((int) (1f / REFRESH_RATE * 1000) - timeElapsed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updatePlayerPosition(Player player) {
        if (player.isUpPressed())
            player.setY(player.getY() - 10);
        if (player.isDownPressed())
            player.setY(player.getY() + 10);
    }

    private void playerCollision() {
        Rectangle ballShape = ball.getBounds();
        Rectangle player1Shape = player1.getBoundsP1();
        Rectangle player2Shape = player2.getBoundsP2();
        if (ballShape.intersects(player1Shape)) {
            if (!player1.isAlreadyChecked()) {
                player1.setAlreadyChecked(true);
                player2.setAlreadyChecked(false);
                ball.setXDir((ball.getXDir() - 1) * (-1));
                ball.setYDir(ThreadLocalRandom.current().nextInt(-5, 5 + 1));
            }
        }
        if (ballShape.intersects(player2Shape)) {
            if (!player2.isAlreadyChecked()) {
                player2.setAlreadyChecked(true);
                player1.setAlreadyChecked(false);
                ball.setXDir((ball.getXDir() + 1) * (-1));
                ball.setYDir(ThreadLocalRandom.current().nextInt(-5, 5 + 1));
                overShoot = overShoot + getDiffOverShoot();
            }
        }
    }

    private void borderCollision() {
        int posBounce = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        int negBounce = ThreadLocalRandom.current().nextInt(-2, 0 + 1);
        if (ball.getY() <= 0)
            ball.setYDir(posBounce);
        if (ball.getY() + ball.getWidth() >= PaintPanel.PANEL_HEIGHT)
            ball.setYDir(negBounce);
    }

    private void winCondition() {
        if (ball.getX() <= 0) {
            ball.setX(PaintPanel.PANEL_WIDTH / 2 - Ball.WIDTH / 2);
            ball.setY(PaintPanel.PANEL_HEIGHT / 2 - Ball.WIDTH / 2);
            ball.setYDir(ball.ranDir);
            ball.setXDir(-2);
            player2.score++;
            overShoot = 0;
        }
        if (ball.getX() >= PaintPanel.PANEL_WIDTH) {
            ball.setX(PaintPanel.PANEL_WIDTH / 2 - Ball.WIDTH / 2);
            ball.setY(PaintPanel.PANEL_HEIGHT / 2 - Ball.WIDTH / 2);
            ball.setYDir(ball.ranDir);
            ball.setXDir(-2);
            player1.setAlreadyChecked(false);
            player1.score++;
            overShoot = 0;
        }
    }

    private void playerVsAI() {
        if (player2.isAI()) {
            if (ball.getY() + Ball.HEIGHT / 2 > player2.getY() + 22 + overShoot && ball.getY() + Ball.HEIGHT / 2 < player2.getY() + 53 + overShoot) {
                player2.setUpPressed(false);
                player2.setDownPressed(false);
            } else if (ball.getY() < player2.getY() + 22 + overShoot) {
                player2.setDownPressed(false);
                player2.setUpPressed(true);
            } else if (ball.getY() > player2.getY() + 53 + overShoot) {
                player2.setDownPressed(true);
                player2.setUpPressed(false);
            }
        }
    }

    private void aiVsAI() {
        if (player1.isAI() && player2.isAI()) {
            if (ball.getY() + Ball.HEIGHT / 2 > player1.getY() + 22 && ball.getY() + Ball.HEIGHT / 2 < player1.getY() + 53) {
                player1.setUpPressed(false);
                player1.setDownPressed(false);
            } else if (ball.getY() < player1.getY() + 22) {
                player1.setDownPressed(false);
                player1.setUpPressed(true);
            } else if (ball.getY() > player1.getY() + 53) {
                player1.setDownPressed(true);
                player1.setUpPressed(false);
            }

            if (ball.getY() + Ball.HEIGHT / 2 > player2.getY() + 22 && ball.getY() + Ball.HEIGHT / 2 < player2.getY() + 53) {
                player2.setUpPressed(false);
                player2.setDownPressed(false);
            } else if (ball.getY() < player2.getY() + 22) {
                player2.setDownPressed(false);
                player2.setUpPressed(true);
            } else if (ball.getY() > player2.getY() + 53) {
                player2.setDownPressed(true);
                player2.setUpPressed(false);
            }
        }
    }

    @Override
    public void onValueRead(float value) {
        player1.setY((int) (value * 100.0));
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - lastTime));
        lastTime = System.currentTimeMillis();
    }

    private class KeyManager implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            // ignore
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (!player2.isAI())
                        player2.setUpPressed(true);
                    break;
                case KeyEvent.VK_DOWN:
                    if (!player2.isAI())
                        player2.setDownPressed(true);
                    break;
                case KeyEvent.VK_W:
                    if (!player1.isAI())
                        player1.setUpPressed(true);
                    break;
                case KeyEvent.VK_S:
                    if (!player1.isAI())
                        player1.setDownPressed(true);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> player2.setUpPressed(false);
                case KeyEvent.VK_DOWN -> player2.setDownPressed(false);
                case KeyEvent.VK_W -> player1.setUpPressed(false);
                case KeyEvent.VK_S -> player1.setDownPressed(false);
            }
        }
    }
}
