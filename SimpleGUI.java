import javax.swing.JFrame;
import javax.swing.JLabel;

public class SimpleGUI {
    public static void main(String[] args) {
        // Create a new frame (window)
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        
        // Create a label with some text
        JLabel label = new JLabel("Hello, World!", JLabel.CENTER);
        
        // Add the label to the frame
        frame.add(label);
        
        // Make the frame visible
        frame.setVisible(true);
    }
}