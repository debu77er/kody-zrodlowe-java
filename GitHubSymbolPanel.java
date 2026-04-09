import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class GitHubSymbolPanel extends JFrame {

    public GitHubSymbolPanel() {
        setTitle("GitHub Source Code Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add GitHub logo at the top
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Load the GitHub logo from URL or resource
        try {
            // You can replace the URL with a local resource if you have one
            URL githubLogoUrl = new URL("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png");
            ImageIcon icon = new ImageIcon(githubLogoUrl);
            // Optional: resize the icon
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logoLabel.setText("GitHub Logo");
        }

        mainPanel.add(logoLabel, BorderLayout.NORTH);

        // Create JTextArea for source code display
        JTextArea codeTextArea = new JTextArea();
        codeTextArea.setEditable(false);
        codeTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Load source code into the text area
        String sourceCode = loadSourceCode(); // Implement this method
        codeTextArea.setText(sourceCode);

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(codeTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    /**
     * Loads source code from a file or resource.
     * For example purposes, we'll load the current file or a sample code.
     */
    private String loadSourceCode() {
        StringBuilder code = new StringBuilder();
        // You can load from a file or resource. Here's an example reading from a string.
        // For demonstration, we'll load a simple Java class code
        String sampleCode = 
            "public class HelloWorld {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"Hello, World!\");\n" +
            "    }\n" +
            "}\n";

        // Alternatively, read from a file:
        // try (BufferedReader br = new BufferedReader(new FileReader("YourSourceFile.java"))) {
        //     String line;
        //     while ((line = br.readLine()) != null) {
        //         code.append(line).append("\n");
        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        code.append(sampleCode);
        return code.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GitHubSymbolPanel().setVisible(true);
        });
    }
}
