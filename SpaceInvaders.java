import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SpaceInvaders extends JPanel implements ActionListener {
    private final int PANEL_WIDTH = 500;
    private final int PANEL_HEIGHT = 600;
    private final int PLAYER_WIDTH = 50;
    private final int PLAYER_HEIGHT = 50;
    private final int BULLET_WIDTH = 5;
    private final int BULLET_HEIGHT = 10;
    private final int ENEMY_WIDTH = 30;
    private final int ENEMY_HEIGHT = 30;
    private final int ENEMY_ROWS = 3;
    private final int ENEMY_COLUMNS = 8;

    private Timer timer;
    private int playerX, playerY;   // Player's position
    private ArrayList<Rectangle> bullets;
    private ArrayList<Rectangle> enemies;
    private boolean moveLeft, moveRight;

    private Image playerImage;
    private Image enemyImage;

    public SpaceInvaders() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        loadImages();
        initGame();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });
    }

    private void loadImages() {
        playerImage = new ImageIcon("spaceship.png").getImage();
        enemyImage = new ImageIcon("alien.png").getImage();
    }

    private void initGame() {
        playerX = PANEL_WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = PANEL_HEIGHT - PLAYER_HEIGHT - 10;

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        for (int row = 0; row < ENEMY_ROWS; row++) {
            for (int col = 0; col < ENEMY_COLUMNS; col++) {
                int x = 50 + col * (ENEMY_WIDTH + 20);
                int y = 50 + row * (ENEMY_HEIGHT + 10);
                enemies.add(new Rectangle(x, y, ENEMY_WIDTH, ENEMY_HEIGHT));
            }
        }

        timer = new Timer(20, this);
        timer.start();
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moveLeft = true;
            case KeyEvent.VK_RIGHT -> moveRight = true;
            case KeyEvent.VK_SPACE -> shootBullet();
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moveLeft = false;
            case KeyEvent.VK_RIGHT -> moveRight = false;
        }
    }

    private void shootBullet() {
        bullets.add(new Rectangle(playerX + PLAYER_WIDTH / 2 - BULLET_WIDTH / 2, playerY, BULLET_WIDTH, BULLET_HEIGHT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePlayer();
        updateBullets();
        updateEnemies();
        checkCollisions();
        repaint();
    }

    private void updatePlayer() {
        if (moveLeft && playerX > 0) {
            playerX -= 5;
        }
        if (moveRight && playerX + PLAYER_WIDTH < PANEL_WIDTH) {
            playerX += 5;
        }
    }

    private void updateBullets() {
        bullets.removeIf(bullet -> bullet.y + BULLET_HEIGHT < 0);
        for (Rectangle bullet : bullets) {
            bullet.y -= 10;
        }
    }

    private void updateEnemies() {
        for (Rectangle enemy : enemies) {
            enemy.y += 1; // Enemies move downward slowly
            if (enemy.y > PANEL_HEIGHT) {
                JOptionPane.showMessageDialog(this, "Game Over! Enemies invaded the base!");
                System.exit(0);
            }
        }
    }

    private void checkCollisions() {
        ArrayList<Rectangle> bulletsToRemove = new ArrayList<>();
        ArrayList<Rectangle> enemiesToRemove = new ArrayList<>();

        for (Rectangle bullet : bullets) {
            for (Rectangle enemy : enemies) {
                if (bullet.intersects(enemy)) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                }
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);

        if (enemies.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Congratulations! You defeated all enemies!");
            System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        g.drawImage(playerImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);

        // Draw bullets
        g.setColor(Color.RED);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        // Draw enemies
        for (Rectangle enemy : enemies) {
            g.drawImage(enemyImage, enemy.x, enemy.y, ENEMY_WIDTH, ENEMY_HEIGHT, this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Space Invaders with Images");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            SpaceInvaders gamePanel = new SpaceInvaders ();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
