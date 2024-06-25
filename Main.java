import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JPanel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;

    private Timer timer;
    private int paddleX = 250;
    private int ballX = 100;
    private int ballY = 0;
    private int ballSpeedY = 3;
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private BufferedImage backgroundImage;

    public Main() {
        try {
            backgroundImage = ImageIO.read(new File("path/to/your/background/image.jpg")); // Path to your background image
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer = new Timer(10, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(173, 216, 230)); // Light blue background
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Game Over Screen
        if (gameOver) {
            g.setColor(new Color(255, 69, 0)); // Red color for game over text
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 150, 250);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Score: " + score, 230, 300);
            g.drawString("Press 'R' to Restart", 150, 350);
            return;
        }

        // Paddle
        g.setColor(new Color(60, 179, 113)); // Medium sea green paddle
        g.fillRoundRect(paddleX, 550, 100, 10, 10, 10);

        // Ball
        g.setColor(new Color(255, 69, 0)); // Red ball
        g.fillOval(ballX, ballY, 20, 20);

        // Score and Lives
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, 500, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        ballY += ballSpeedY;
        if (ballY >= 550 && ballX >= paddleX && ballX <= paddleX + 100) {
            ballSpeedY = -ballSpeedY;
            score += 10;
        }
        if (ballY < 0) {
            ballSpeedY = -ballSpeedY;
        }
        if (ballY > 600) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
            } else {
                ballY = 0;
                ballX = (int) (Math.random() * 580);
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            paddleX = Math.max(paddleX - 20, 0);
        }
        if (code == KeyEvent.VK_RIGHT) {
            paddleX = Math.min(paddleX + 20, 500);
        }
        if (gameOver && code == KeyEvent.VK_R) {
            restartGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    private void restartGame() {
        ballX = 100;
        ballY = 0;
        ballSpeedY = 3;
        paddleX = 250;
        score = 0;
        lives = 3;
        gameOver = false;
        requestFocus(); // Memastikan panel kembali fokus untuk menerima input dari keyboard
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Catch the Ball");
        Main game = new Main();
        frame.add(game); 
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
