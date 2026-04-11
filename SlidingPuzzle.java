import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SlidingPuzzle extends JFrame {
    private static final int SIZE = 3; // 3x3 puzzle
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private int[][] board = new int[SIZE][SIZE];
    private int emptyRow, emptyCol;

    public SlidingPuzzle() {
        setTitle("Sliding Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        initBoard();
        shuffleBoard();

        // Create panel with GridLayout
        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE, 2, 2));
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                buttons[row][col] = btn;
                final int r = row;
                final int c = col;
                btn.addActionListener(e -> moveTile(r, c));
                panel.add(btn);
            }
        }

        add(panel);
        updateButtons();

        setVisible(true);
    }

    private void initBoard() {
        int num = 1;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = num;
                num++;
            }
        }
        board[SIZE - 1][SIZE - 1] = 0; // Empty space
        emptyRow = SIZE - 1;
        emptyCol = SIZE - 1;
    }

    private void shuffleBoard() {
        // Perform random valid moves to shuffle the puzzle
        int moves = 100; // number of moves for shuffling
        for (int i = 0; i < moves; i++) {
            java.util.List<Point> neighbors = getNeighbors(emptyRow, emptyCol);
            Point p = neighbors.get((int) (Math.random() * neighbors.size()));
            moveTile(p.x, p.y);
        }
    }

    private java.util.List<Point> getNeighbors(int row, int col) {
        java.util.List<Point> list = new java.util.ArrayList<>();
        if (row > 0) list.add(new Point(row - 1, col));
        if (row < SIZE - 1) list.add(new Point(row + 1, col));
        if (col > 0) list.add(new Point(row, col - 1));
        if (col < SIZE - 1) list.add(new Point(row, col + 1));
        return list;
    }

    private void moveTile(int row, int col) {
        if ((Math.abs(emptyRow - row) == 1 && emptyCol == col) ||
            (Math.abs(emptyCol - col) == 1 && emptyRow == row)) {
            // Swap tiles
            board[emptyRow][emptyCol] = board[row][col];
            board[row][col] = 0;
            emptyRow = row;
            emptyCol = col;
            updateButtons();
            if (isSolved()) {
                JOptionPane.showMessageDialog(this, "Congratulations! You solved the puzzle!");
            }
        }
    }

    private void updateButtons() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (buttons[row][col] == null) {
                    System.err.println("Button at (" + row + "," + col + ") is null");
                    continue; // or handle as needed
                }
                int value = board[row][col];
                JButton btn = buttons[row][col];
                if (value == 0) {
                    btn.setText("");
                    btn.setBackground(Color.LIGHT_GRAY);
                } else {
                    btn.setText(String.valueOf(value));
                    btn.setBackground(Color.WHITE);
                }
            }
        }
    }


    private boolean isSolved() {
        int num = 1;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == SIZE -1 && col == SIZE -1) {
                    if (board[row][col] != 0) return false;
                } else {
                    if (board[row][col] != num) return false;
                    num++;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlidingPuzzle());
    }
}
