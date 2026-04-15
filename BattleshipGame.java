import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipGame extends JFrame {
    private final JButton[][] playerGrid;
    private final JButton[][] opponentGrid;
    private final boolean[][] playerShips;
    private final boolean[][] opponentShips;
    private boolean playerTurn = true;

    public BattleshipGame() {
        setTitle("Gra w Statki");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        playerGrid = new JButton[10][10];
        opponentGrid = new JButton[10][10];
        playerShips = new boolean[10][10];
        opponentShips = new boolean[10][10];

        JPanel playerPanel = createGrid(playerGrid, "Your Grid");
        JPanel opponentPanel = createGrid(opponentGrid, "Opponent's Grid");

        add(playerPanel);
        add(opponentPanel);

        setVisible(true);
    }

    private JPanel createGrid(JButton[][] grid, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 10));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                button.addActionListener(new GridButtonListener(i, j, grid));
                grid[i][j] = button;
                panel.add(button);
            }
        }

        // Add a row for labels
        for (int i = 0; i < 10; i++) {
            panel.add(new JLabel(String.valueOf(i)));
        }

        return panel;
    }

    private class GridButtonListener implements ActionListener {
        private final int x;
        private final int y;
        private final JButton[][] grid;

        public GridButtonListener(int x, int y, JButton[][] grid) {
            this.x = x;
            this.y = y;
            this.grid = grid;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (playerTurn && grid == opponentGrid) {
                // Player's turn to attack
                if (!opponentShips[x][y]) {
                    grid[x][y].setBackground(Color.BLUE);
                    JOptionPane.showMessageDialog(null, "Miss!");
                } else {
                    grid[x][y].setBackground(Color.RED);
                    JOptionPane.showMessageDialog(null, "Hit!");
                }
                playerTurn = false;
            } else if (!playerTurn && grid == playerGrid) {
                // Opponent's turn to attack (not implemented)
                JOptionPane.showMessageDialog(null, "It's the opponent's turn!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleshipGame::new);
    }
}