import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class VennDiagram extends JFrame {

    public VennDiagram() {
        setTitle("Venn Diagram with Clickable Areas");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new VennPanel());
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VennDiagram().setVisible(true);
        });
    }
}

class VennPanel extends JPanel {

    // Circles representing sets
    private Ellipse2D circleA;
    private Ellipse2D circleB;
    private Ellipse2D circleC;

    public VennPanel() {
        // Initialize circles
        // Coordinates and sizes can be adjusted for aesthetics
        circleA = new Ellipse2D.Double(150, 150, 200, 200);
        circleB = new Ellipse2D.Double(250, 150, 200, 200);
        circleC = new Ellipse2D.Double(200, 250, 200, 200);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getPoint());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Enable better graphics
        Graphics2D g2 = (Graphics2D) g;

        // Anti-aliasing for smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circles with transparency
        g2.setColor(new Color(255, 0, 0, 100));
        g2.fill(circleA);
        g2.setColor(new Color(0, 255, 0, 100));
        g2.fill(circleB);
        g2.setColor(new Color(0, 0, 255, 100));
        g2.fill(circleC);

        // Draw borders
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.draw(circleA);
        g2.draw(circleB);
        g2.draw(circleC);
    }

    private void handleClick(Point p) {
        // Create Area objects for each region
        Area A = new Area(circleA);
        Area B = new Area(circleB);
        Area C = new Area(circleC);

        // Define all 8 regions:
        // 1. A only
        // 2. B only
        // 3. C only
        // 4. A & B only
        // 5. B & C only
        // 6. A & C only
        // 7. A & B & C
        // 8. Outside all

        // Helper method to check if point is in an area
        boolean inA = A.contains(p);
        boolean inB = B.contains(p);
        boolean inC = C.contains(p);

        if (inA && !inB && !inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 1: A only");
        } else if (!inA && inB && !inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 2: B only");
        } else if (!inA && !inB && inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 3: C only");
        } else if (inA && inB && !inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 4: A & B");
        } else if (!inA && inB && inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 5: B & C");
        } else if (inA && !inB && inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 6: A & C");
        } else if (inA && inB && inC) {
            JOptionPane.showMessageDialog(this, "Clicked on Area 7: A & B & C");
        } else {
            JOptionPane.showMessageDialog(this, "Clicked outside the diagram");
        }
    }
}
