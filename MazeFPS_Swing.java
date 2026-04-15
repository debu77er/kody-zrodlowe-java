import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class MazeFPS_Swing extends JPanel implements KeyListener, Runnable {

    final int WIDTH = 800;
    final int HEIGHT = 600;

    // Map (1 = wall, 0 = empty)
    int[][] map = {
            {1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,0,1},
            {1,0,1,0,1,0,0,1},
            {1,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,0,1},
            {1,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1},
    };

    double playerX = 3.5;
    double playerY = 3.5;
    double angle = 0;

    final double FOV = Math.PI / 3;

    Set<Integer> keys = new HashSet<>();

    public MazeFPS_Swing() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void update() {
        double speed = 0.05;
        double rotSpeed = 0.04;

        if (keys.contains(KeyEvent.VK_LEFT)) angle -= rotSpeed;
        if (keys.contains(KeyEvent.VK_RIGHT)) angle += rotSpeed;

        double dx = Math.cos(angle) * speed;
        double dy = Math.sin(angle) * speed;

        if (keys.contains(KeyEvent.VK_W)) move(playerX + dx, playerY + dy);
        if (keys.contains(KeyEvent.VK_S)) move(playerX - dx, playerY - dy);

        if (keys.contains(KeyEvent.VK_A)) move(playerX - dy, playerY + dx);
        if (keys.contains(KeyEvent.VK_D)) move(playerX + dy, playerY - dx);
    }

    void move(double nx, double ny) {
        if (map[(int) ny][(int) nx] == 0) {
            playerX = nx;
            playerY = ny;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Sky
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT / 2);

        // Floor
        g.setColor(Color.GRAY);
        g.fillRect(0, HEIGHT / 2, WIDTH, HEIGHT / 2);

        for (int x = 0; x < WIDTH; x++) {
            double rayAngle = angle - FOV / 2 + (x / (double) WIDTH) * FOV;

            double distance = castRay(rayAngle);

            // Fix fisheye distortion
            distance *= Math.cos(rayAngle - angle);

            double wallHeight = HEIGHT / (distance + 0.0001);

            float shade = (float) Math.max(0, 1 - distance / 8);
            g.setColor(new Color(shade, shade, shade));

            int start = (int) (HEIGHT / 2 - wallHeight / 2);
            int end = (int) (HEIGHT / 2 + wallHeight / 2);

            g.drawLine(x, start, x, end);
        }
    }

    double castRay(double angle) {
        double x = playerX;
        double y = playerY;

        double step = 0.02;

        while (true) {
            x += Math.cos(angle) * step;
            y += Math.sin(angle) * step;

            if (map[(int) y][(int) x] == 1) {
                double dx = x - playerX;
                double dy = y - playerY;
                return Math.sqrt(dx * dx + dy * dy);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Maze FPS - Pure Java");
        MazeFPS_Swing game = new MazeFPS_Swing();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}