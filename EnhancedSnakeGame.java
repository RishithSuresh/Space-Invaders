import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class EnhancedSnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 25;
    private static final int WIDTH = 20, HEIGHT = 20;
    private static final int INITIAL_DELAY = 150;

    private ArrayList<Point> snake;
    private Point food;
    private int score = 0;
    private int delay = INITIAL_DELAY;
    private String direction = "RIGHT";
    private boolean running = true;

    private Timer timer;

    public EnhancedSnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        startGame();
    }

    private void startGame() {
        snake = new ArrayList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));

        spawnFood();

        timer = new Timer(delay, this);
        timer.start();
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = switch (direction) {
            case "UP" -> new Point(head.getX(), head.y - 1);
            case "DOWN" -> new Point(head.getX(), head.y + 1);
            case "LEFT" -> new Point(head.getX() - 1, head.y);
            case "RIGHT" -> new Point(head.getX() + 1, head.y);
            default -> head;
        };

        // Check collisions
        if (newHead.getX() < 0 || newHead.getX() >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            running = false;
            timer.stop();
            return;
        }

        // Move the snake
        snake.add(0, newHead);

        // Check if food is eaten
        if (newHead.equals(food)) {
            score += 10;
            delay = Math.max(50, delay - 5); // Increase speed
            timer.setDelay(delay);
            spawnFood();
        } else {
            snake.remove(snake.size() - 1); // Remove tail
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the grid
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.getX() * TILE_SIZE, p.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.getX() * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw the score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 10, 20);

        if (!running) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) direction = "UP";
        if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) direction = "DOWN";
        if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) direction = "LEFT";
        if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) direction = "RIGHT";
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enhanced Snake Game");
        EnhancedSnakeGame game = new EnhancedSnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
