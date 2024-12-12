import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PacManGameSingleFile extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;  // Size of each grid cell
    private final int GRID_SIZE = 25; // Number of rows and columns
    private final int DELAY = 150;    // Game speed in milliseconds

    private Timer timer;
    private int pacManX, pacManY;     // Pac-Man's position
    private int directionX, directionY; // Direction of movement
    private boolean[][] pellets;      // Pellet grid

    public PacManGameSingleFile() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE));

        initGame();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    private void initGame() {
        pacManX = GRID_SIZE / 2 * TILE_SIZE;
        pacManY = GRID_SIZE / 2 * TILE_SIZE;
        directionX = 0;
        directionY = 0;

        pellets = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                pellets[i][j] = true;
            }
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                directionX = 0;
                directionY = -1;
            }
            case KeyEvent.VK_DOWN -> {
                directionX = 0;
                directionY = 1;
            }
            case KeyEvent.VK_LEFT -> {
                directionX = -1;
                directionY = 0;
            }
            case KeyEvent.VK_RIGHT -> {
                directionX = 1;
                directionY = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePacMan();
        checkPelletCollision();
        repaint();
    }

    private void movePacMan() {
        pacManX += directionX * TILE_SIZE;
        pacManY += directionY * TILE_SIZE;

        // Wrap around the screen
        if (pacManX < 0) pacManX = (GRID_SIZE - 1) * TILE_SIZE;
        if (pacManX >= GRID_SIZE * TILE_SIZE) pacManX = 0;
        if (pacManY < 0) pacManY = (GRID_SIZE - 1) * TILE_SIZE;
        if (pacManY >= GRID_SIZE * TILE_SIZE) pacManY = 0;
    }

    private void checkPelletCollision() {
        int gridX = pacManX / TILE_SIZE;
        int gridY = pacManY / TILE_SIZE;

        if (pellets[gridX][gridY]) {
            pellets[gridX][gridY] = false; // Eat the pellet
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw pellets
        g.setColor(Color.WHITE);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (pellets[i][j]) {
                    g.fillOval(i * TILE_SIZE + TILE_SIZE / 4, j * TILE_SIZE + TILE_SIZE / 4,
                               TILE_SIZE / 2, TILE_SIZE / 2);
                }
            }
        }

        // Draw Pac-Man
        g.setColor(Color.YELLOW);
        g.fillOval(pacManX, pacManY, TILE_SIZE, TILE_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pac-Man Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            PacManGameSingleFile gamePanel = new PacManGameSingleFile();
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

