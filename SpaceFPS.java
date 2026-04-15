import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class SpaceFPS extends JPanel implements Runnable, KeyListener {

    final int WIDTH = 800;
    final int HEIGHT = 600;

    class Star {
        double x, y, z;
    }

    Star[] stars = new Star[2000];
    Random rand = new Random();

    double speed = 0.1;
    double yaw = 0;
    double pitch = 0;

    Set<Integer> keys = new HashSet<>();

    public SpaceFPS() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        for (int i = 0; i < stars.length; i++) {
            stars[i] = createStar();
        }

        new Thread(this).start();
    }

    Star createStar() {
        Star s = new Star();
        s.x = rand.nextDouble() * 2 - 1;
        s.y = rand.nextDouble() * 2 - 1;
        s.z = rand.nextDouble() * 5 + 0.5;
        return s;
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {}
        }
    }

    void update() {
        double rotSpeed = 0.03;

        if (keys.contains(KeyEvent.VK_LEFT)) yaw -= rotSpeed;
        if (keys.contains(KeyEvent.VK_RIGHT)) yaw += rotSpeed;
        if (keys.contains(KeyEvent.VK_UP)) pitch -= rotSpeed;
        if (keys.contains(KeyEvent.VK_DOWN)) pitch += rotSpeed;

        if (keys.contains(KeyEvent.VK_W)) speed += 0.01;
        if (keys.contains(KeyEvent.VK_S)) speed -= 0.01;

        speed = Math.max(0, Math.min(speed, 1));

        for (Star s : stars) {
            s.z -= speed;

            // rotate starfield (simulate camera rotation)
            double cosY = Math.cos(yaw);
            double sinY = Math.sin(yaw);
            double cosP = Math.cos(pitch);
            double sinP = Math.sin(pitch);

            double x = s.x;
            double y = s.y;
            double z = s.z;

            // yaw rotation
            double dx = cosY * x - sinY * z;
            double dz = sinY * x + cosY * z;

            // pitch rotation
            double dy = cosP * y - sinP * dz;
            dz = sinP * y + cosP * dz;

            s.x = dx;
            s.y = dy;
            s.z = dz;

            if (s.z <= 0.1) {
                Star newStar = createStar();
                s.x = newStar.x;
                s.y = newStar.y;
                s.z = newStar.z;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Star s : stars) {
            double scale = 300 / s.z;
            int sx = (int)(WIDTH / 2 + s.x * scale);
            int sy = (int)(HEIGHT / 2 + s.y * scale);

            if (sx < 0 || sx >= WIDTH || sy < 0 || sy >= HEIGHT) continue;

            int brightness = (int)(255 - s.z * 40);
            brightness = Math.max(50, Math.min(255, brightness));

            g.setColor(new Color(brightness, brightness, brightness));
            g.fillRect(sx, sy, 2, 2);
        }

        drawHUD(g);
    }

    void drawHUD(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawString("Speed: " + String.format("%.2f", speed), 10, 20);

        // crosshair
        g.drawLine(WIDTH/2 - 10, HEIGHT/2, WIDTH/2 + 10, HEIGHT/2);
        g.drawLine(WIDTH/2, HEIGHT/2 - 10, WIDTH/2, HEIGHT/2 + 10);
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
        JFrame frame = new JFrame("Interstellar FPS - Pure Java");
        SpaceFPS game = new SpaceFPS();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}