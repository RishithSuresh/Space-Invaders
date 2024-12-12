import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class EnhancedSpaceInvadersV2 extends JPanel implements ActionListener {
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 600;
    private final int PLAYER_WIDTH = 50;
    private final int PLAYER_HEIGHT = 50;
    private final int BULLET_WIDTH = 5;
    private final int BULLET_HEIGHT = 10;
    private final int ENEMY_WIDTH = 50;
    private final int ENEMY_HEIGHT = 50;
    private final int ENEMY_ROWS = 3;
    private final int ENEMY_COLUMNS = 8;
    private final int INITIAL_LIVES = 3;
    private final int SHIELD_WIDTH = 50;
    private final int SHIELD_HEIGHT = 50;
    private final int SHIELD_HITS = 3;

    private Timer timer;
    private int playerX, playerY;
    private int score, level;
    private int lives;
    private boolean moveLeft, moveRight;
    private ArrayList<Rectangle> playerBullets;
    private ArrayList<Rectangle> enemyBullets;
    private ArrayList<Rectangle> enemies;
    private ArrayList<Shield> shields;
    private int enemyDirection = 1; // 1 for right, -1 for left
    private int enemySpeed = 1;
    private Random random;

    private Image playerImage, enemyImage, shieldImage;
    @SuppressWarnings("unused")
    private boolean shieldActive = false; // Power-up for shield
    private boolean fasterBullets = false; // Power-up for faster bullets

    public EnhancedSpaceInvadersV2() {
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
        shieldImage = new ImageIcon("shield.png").getImage();
    }

    private void initGame() {
        playerX = PANEL_WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = PANEL_HEIGHT - PLAYER_HEIGHT - 10;
        playerBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        enemies = new ArrayList<>();
        shields = new ArrayList<>();
        score = 0;
        level = 1;
        lives = INITIAL_LIVES;
        random = new Random();

        // Initialize enemies
        for (int row = 0; row < ENEMY_ROWS; row++) {
            for (int col = 0; col < ENEMY_COLUMNS; col++) {
                int x = 50 + col * (ENEMY_WIDTH + 20);
                int y = 50 + row * (ENEMY_HEIGHT + 10);
                enemies.add(new Rectangle(x, y, ENEMY_WIDTH, ENEMY_HEIGHT));
            }
        }

        // Initialize shields
        int shieldSpacing = (PANEL_WIDTH - 3 * SHIELD_WIDTH) / 4;
        for (int i = 0; i < 3; i++) {
            int x = shieldSpacing + i * (SHIELD_WIDTH + shieldSpacing);
            int y = PANEL_HEIGHT - 150;
            shields.add(new Shield(x, y, SHIELD_WIDTH, SHIELD_HEIGHT, SHIELD_HITS));
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
        int bulletSpeed = fasterBullets ? 15 : 10;
        playerBullets.add(new Rectangle(playerX + PLAYER_WIDTH / 2 - BULLET_WIDTH / 2, playerY, BULLET_WIDTH, bulletSpeed));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePlayer();
        updateBullets();
        updateEnemies();
        updateShields();
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
        playerBullets.removeIf(bullet -> bullet.y + BULLET_HEIGHT < 0);
        enemyBullets.removeIf(bullet -> bullet.y > PANEL_HEIGHT);

        for (Rectangle bullet : playerBullets) {
            bullet.y -= 10;
        }

        for (Rectangle bullet : enemyBullets) {
            bullet.y += 5;
        }
    }

    private void updateEnemies() {
        boolean changeDirection = false;

        for (Rectangle enemy : enemies) {
            enemy.x += enemySpeed * enemyDirection;
            if (enemy.x <= 0 || enemy.x + ENEMY_WIDTH >= PANEL_WIDTH) {
                changeDirection = true;
            }
        }

        if (changeDirection) {
            enemyDirection *= -1;
            for (Rectangle enemy : enemies) {
                enemy.y += 20;
            }
        }

        // Enemies randomly shoot bullets
        if (random.nextInt(50) == 0 && !enemies.isEmpty()) {
            Rectangle shooter = enemies.get(random.nextInt(enemies.size()));
            enemyBullets.add(new Rectangle(shooter.x + ENEMY_WIDTH / 2 - BULLET_WIDTH / 2, shooter.y + ENEMY_HEIGHT, BULLET_WIDTH, BULLET_HEIGHT));
        }
    }

    private void updateShields() {
        shields.removeIf(shield -> shield.hits <= 0);
    }

    private void checkCollisions() {
        ArrayList<Rectangle> bulletsToRemove = new ArrayList<>();
        ArrayList<Rectangle> enemiesToRemove = new ArrayList<>();

        // Player bullets vs enemies
        for (Rectangle bullet : playerBullets) {
            for (Rectangle enemy : enemies) {
                if (bullet.intersects(enemy)) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    score += 100;
                }
            }
        }

        // Enemy bullets vs player
        for (Rectangle bullet : enemyBullets) {
            if (bullet.intersects(new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT))) {
                bulletsToRemove.add(bullet);
                lives--;
                if (lives <= 0) {
                    endGame("Game Over! You ran out of lives!");
                }
            }
        }

        // Enemy bullets vs shields
        for (Shield shield : shields) {
            for (Rectangle bullet : enemyBullets) {
                if (bullet.intersects(shield.getBounds())) {
                    bulletsToRemove.add(bullet);
                    shield.hits--;
                }
            }
        }

        playerBullets.removeAll(bulletsToRemove);
        enemyBullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);

        // Check if all enemies are destroyed
        if (enemies.isEmpty()) {
            enemySpeed++;
            initGame(); // Start a new level
        }
    }

    private void endGame(String message) {
        timer.stop();
        JOptionPane.showMessageDialog(this, message + " Final Score: " + score);
        System.exit(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        g.drawImage(playerImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, this);

        // Draw player bullets
        g.setColor(Color.RED);
        for (Rectangle bullet : playerBullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        // Draw enemy bullets
        g.setColor(Color.YELLOW);
        for (Rectangle bullet : enemyBullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        // Draw enemies
        for (Rectangle enemy : enemies) {
            g.drawImage(enemyImage, enemy.x, enemy.y, ENEMY_WIDTH, ENEMY_HEIGHT, this);
        }

        // Draw shields
        for (Shield shield : shields) {
            g.drawImage(shieldImage, shield.x, shield.y, shield.width, shield.height, this);
        }

        // Draw score and lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, PANEL_WIDTH - 100, 20);
        g.drawString("Level: " + level, PANEL_WIDTH / 2 - 50, 20);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Enhanced Space Invaders V2");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            EnhancedSpaceInvadersV2 gamePanel = new EnhancedSpaceInvadersV2();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Helper class for shields
    class Shield {
        int x, y, width, height, hits;

        Shield(int x, int y, int width, int height, int hits) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.hits = hits;
        }

        Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }
}

