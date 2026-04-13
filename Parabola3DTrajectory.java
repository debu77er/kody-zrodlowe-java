import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;

public class Parabola3DTrajectory extends JPanel {
    // Parameters for the parabola
    private static final double a = 0.02; // parabola coefficient
    private static final double xMin = -50;
    private static final double xMax = 50;
    private static final int numPoints = 200;

    // Camera / Viewer parameters
    private static final double cameraDistance = 200;
    private static final double viewerAngleX = Math.toRadians(30);
    private static final double viewerAngleY = Math.toRadians(30);

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Parabola Trajectory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new Parabola3DTrajectory());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Enable better rendering quality
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Clear background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw axes
        drawAxes(g2d);

        // Generate parabola points in 3D
        Point3D[] points3D = new Point3D[numPoints];
        double step = (xMax - xMin) / (numPoints - 1);
        for (int i = 0; i < numPoints; i++) {
            double x = xMin + i * step;
            double y = a * x * x;
            double z = 0; // Parabola in X-Y plane, Z=0
            points3D[i] = new Point3D(x, y, z);
        }

        // Project 3D points onto 2D screen
        Point[] projectedPoints = new Point[numPoints];
        for (int i = 0; i < numPoints; i++) {
            projectedPoints[i] = project(points3D[i]);
        }

        // Draw the parabola trajectory
        g2d.setColor(Color.RED);
        GeneralPath path = new GeneralPath();
        if (projectedPoints.length > 0) {
            path.moveTo(projectedPoints[0].x, projectedPoints[0].y);
            for (int i = 1; i < projectedPoints.length; i++) {
                path.lineTo(projectedPoints[i].x, projectedPoints[i].y);
            }
        }
        g2d.draw(path);
    }

    // Draw coordinate axes for reference
    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        int padding = 50;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        // Origin point
        Point3D origin3D = new Point3D(0, 0, 0);
        Point origin2D = project(origin3D);

        // Draw X axis
        Point3D xEnd = new Point3D(50, 0, 0);
        Point x2D = project(xEnd);
        g2d.drawLine(origin2D.x, origin2D.y, x2D.x, x2D.y);
        g2d.drawString("X", x2D.x + 5, x2D.y);

        // Draw Y axis
        Point3D yEnd = new Point3D(0, 50, 0);
        Point y2D = project(yEnd);
        g2d.drawLine(origin2D.x, origin2D.y, y2D.x, y2D.y);
        g2d.drawString("Y", y2D.x + 5, y2D.y);

        // Draw Z axis
        Point3D zEnd = new Point3D(0, 0, 50);
        Point z2D = project(zEnd);
        g2d.drawLine(origin2D.x, origin2D.y, z2D.x, z2D.y);
        g2d.drawString("Z", z2D.x + 5, z2D.y);
    }

    // Simple perspective projection from 3D to 2D
    private Point project(Point3D p) {
        // Rotate around Y-axis
        double xz = p.x * Math.cos(viewerAngleY) + p.z * Math.sin(viewerAngleY);
        double zz = -p.x * Math.sin(viewerAngleY) + p.z * Math.cos(viewerAngleY);

        // Rotate around X-axis
        double yz = p.y * Math.cos(viewerAngleX) - zz * Math.sin(viewerAngleX);
        double zy = p.y * Math.sin(viewerAngleX) + zz * Math.cos(viewerAngleX);

        // Perspective projection onto 2D plane
        double d = cameraDistance + xz; // distance along viewer's line of sight
        double scale = 500 / d; // scale factor for perspective

        int screenX = (int) (getWidth() / 2 + yz * scale);
        int screenY = (int) (getHeight() / 2 - zy * scale); // Y axis inverted for screen coords

        return new Point(screenX, screenY);
    }

    // Inner class for 3D point
    private static class Point3D {
        double x, y, z;

        Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
