import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonCentroidApp extends JFrame {

    public PolygonCentroidApp() {
        setTitle("Polygon Centroid Calculator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DrawingPanel panel = new DrawingPanel();
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PolygonCentroidApp().setVisible(true);
        });
    }
}

class DrawingPanel extends JPanel {

    private final List<Point> points = new ArrayList<>();

    public DrawingPanel() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    points.add(e.getPoint());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    points.clear(); // reset
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw points
        g2.setColor(Color.BLUE);
        for (Point p : points) {
            g2.fillOval(p.x - 4, p.y - 4, 8, 8);
        }

        // Draw polygon if enough points
        if (points.size() > 1) {
            g2.setColor(Color.BLACK);
            for (int i = 0; i < points.size() - 1; i++) {
                g2.drawLine(points.get(i).x, points.get(i).y,
                            points.get(i + 1).x, points.get(i + 1).y);
            }
        }

        // Close polygon if at least 3 points
        if (points.size() > 2) {
            g2.drawLine(points.get(points.size() - 1).x,
                        points.get(points.size() - 1).y,
                        points.get(0).x,
                        points.get(0).y);

            // Draw centroid
            Point centroid = computeCentroid(points);
            if (centroid != null) {
                g2.setColor(Color.RED);
                g2.fillOval(centroid.x - 6, centroid.y - 6, 12, 12);
                g2.drawString("Centroid", centroid.x + 8, centroid.y - 8);
            }
        }
    }

    private Point computeCentroid(List<Point> pts) {
        double area = 0;
        double cx = 0;
        double cy = 0;

        int n = pts.size();

        for (int i = 0; i < n; i++) {
            Point p0 = pts.get(i);
            Point p1 = pts.get((i + 1) % n);

            double cross = p0.x * p1.y - p1.x * p0.y;
            area += cross;
            cx += (p0.x + p1.x) * cross;
            cy += (p0.y + p1.y) * cross;
        }

        area *= 0.5;

        if (area == 0) return null;

        cx /= (6 * area);
        cy /= (6 * area);

        return new Point((int) cx, (int) cy);
    }
}