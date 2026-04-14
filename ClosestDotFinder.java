import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ClosestDotFinder extends JFrame {

    private List<Point> dots = new ArrayList<>();
    private Point closestDot = null;

    public ClosestDotFinder() {
        setTitle("Closest Dot Finder");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DrawingPanel drawingPanel = new DrawingPanel();

        JButton findButton = new JButton("Find Closest Dot");
        findButton.addActionListener(e -> {
            if (dots.size() < 2) {
                JOptionPane.showMessageDialog(this, "Add at least two dots.");
                return;
            }
            findClosestDot();
            drawingPanel.repaint();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(findButton);

        add(drawingPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void findClosestDot() {
        double minTotalDistance = Double.MAX_VALUE;
        Point minDot = null;

        for (Point dot : dots) {
            double totalDistance = 0;
            for (Point other : dots) {
                if (dot != other) {
                    totalDistance += dot.distance(other);
                }
            }
            if (totalDistance < minTotalDistance) {
                minTotalDistance = totalDistance;
                minDot = dot;
            }
        }
        closestDot = minDot;
    }

    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Add new dot on click
                    dots.add(e.getPoint());
                    closestDot = null; // reset closest dot
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw all dots
            for (Point p : dots) {
                g.setColor(Color.BLACK);
                g.fillOval(p.x - 5, p.y - 5, 10, 10);
            }
            // Highlight closest dot if exists
            if (closestDot != null) {
                g.setColor(Color.RED);
                g.fillOval(closestDot.x - 7, closestDot.y - 7, 14, 14);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClosestDotFinder app = new ClosestDotFinder();
            app.setVisible(true);
        });
    }
}
