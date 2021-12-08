import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PongGame extends JPanel implements Runnable, KeyListener {

    MyFrame pongFrame;
    JPanel pongPanel;
    Float tile1;
    Float tile2;
    Ball ball;
    int lastBallX;
    int collisions;
    int p1Points;
    int p1keyPressed;
    int p1keyReleased;
    int p2Points;
    int p2keyPressed;
    int p2keyReleased;
    boolean winner;
    String playerWinner;
    Thread gameLoop;



    PongGame() {
        pongPanel = new JPanel();
        this.setPreferredSize(new Dimension(1920,1080));
        this.setFocusable(true);
        pongFrame = new MyFrame();
        pongFrame.add(this);
        pongFrame.addKeyListener(this);
        pongFrame.setFocusable(true);
        p1Points = 0;
        p2Points = 0;
        tile1 = new Float(5,1080/2-100);
        tile2 = new Float(1890,1080/2-100);
        ball = new Ball();
        lastBallX = ball.X;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void update() {
        //check for winner
        if (winner) {
            gameLoop.stop();
        }
        if (p1Points == 10|| p2Points == 10) {
            if (p1Points == 10) {
                playerWinner = "1";
            }
            else {
                playerWinner = "2";
            }
            winner=true;
        }
        else {
            //check ball collisions
            if (ball.Y < 0 || ball.Y > 1080-ball.H) {
                ball.yVelocity *= -1;
            }
            if (ball.X < -600 || ball.X+ball.W > 1920 + 600) {
                if (ball.xVelocity > 0) {
                    p1Points++;
                }
                if (ball.xVelocity < 0) {
                    p2Points++;
                }
                ball = new Ball();
                tile1 = new Float(5,1080/2-100);
                tile2 = new Float(1890,1080/2-100);
                collisions=0;

            }
            else if (((ball.X > tile1.W + ball.xVelocity && ball.X < tile1.W - ball.xVelocity) && (ball.Y+ball.H > tile1.Y && ball.Y < tile1.Y + tile1.H)) || ((ball.X+ball.W < 1920 - tile2.W + ball.xVelocity && ball.X+ball.W > 1920 - tile2.W - ball.xVelocity) && (ball.Y+ball.H > tile2.Y && ball.Y < tile2.Y + tile2.H)) ){
                collisions++;
                if (collisions == 1) {
                    ball.yVelocity = Math.random() > 0.5 ? 7 : -7;
                }
                if (collisions < 10) {
                    ball.xVelocity *= -1.125;
                }
                else {
                    ball.xVelocity *= -1;
                }
            }
            lastBallX = ball.X;
            ball.Y += ball.yVelocity;
            ball.X += ball.xVelocity;

            //update player 1 tile
            if (p1keyReleased == 87 || p1keyReleased == 83) {
                p1keyPressed = 0;
            }
            if (p1keyPressed == 87) {
                if (tile1.Y > 0) {
                    tile1.Y -= 10;
                }
            }
            if (p1keyPressed == 83) {
                if (tile1.Y < 1080-tile1.H) {
                    tile1.Y += 10;
                }
            }
            p1keyReleased = 0;

            //update player 2 tile
            if (p2keyReleased == 38 || p2keyReleased == 40) {
                p2keyPressed = 0;
            }
            if (p2keyPressed == 38) {
                if (tile2.Y > 0) {
                    tile2.Y -= 10;
                }
            }
            if (p2keyPressed == 40) {
                if (tile2.Y < 1080-tile2.H) {
                    tile2.Y += 10;
                }
            }
            p2keyReleased = 0;
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        this.setBackground(Color.BLACK);
        g2D.setColor(Color.GRAY);
        if (!winner) {
            g2D.setStroke(new BasicStroke(5));
            g2D.setFont(new Font("BOLD",Font.BOLD,50));
            g2D.drawString(String.valueOf(p1Points),1920/4,100);
            g2D.drawString(String.valueOf(p2Points),1920/4 * 3,100);
            g2D.drawLine(1920/2,0,1920/2,1080);
            g2D.setColor(Color.WHITE);
            //tile 1
            g2D.fillRect(tile1.X,tile1.Y,tile1.W,tile1.H);
            //tile 2
            g2D.fillRect(tile2.X,tile2.Y,tile2.W,tile2.H);
            //ball
            g2D.fillOval(ball.X,ball.Y,ball.W,ball.H);
        }
        else {
            g2D.setColor(Color.WHITE);
            g2D.setFont(new Font("BOLD",Font.BOLD,100));
            g2D.drawString("Congratulations!", 1920/3-50,425);
            g2D.drawString("Player "+playerWinner+" won the game!", 1920/3-225, 550);
            //g2D.drawString();
        }
        g2D.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==87 || e.getKeyCode()==83) {
            p1keyPressed = e.getKeyCode();
        }
        if (e.getKeyCode()==38 || e.getKeyCode()==40) {
            p2keyPressed = e.getKeyCode();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==87 || e.getKeyCode()==83) {
            p1keyReleased = e.getKeyCode();
        }
        if (e.getKeyCode()==38 || e.getKeyCode()==40) {
            p2keyReleased = e.getKeyCode();
        }
    }

    @Override
    public void run() {
        double frameTime = 1000000000f/60;
        double nextDrawTime = System.nanoTime() + frameTime;
        while (!Thread.currentThread().isInterrupted()) {

            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += frameTime;
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
