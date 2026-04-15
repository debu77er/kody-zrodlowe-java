
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GoGame extends JFrame {
    private final int BOARD_SIZE = 19;
    private JButton[][] boardButtons;
    private boolean blackTurn = true;

    public GoGame() {
        setTitle("Go Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setBackground(Color.LIGHT_GRAY);
                boardButtons[i][j].setOpaque(true);
                boardButtons[i][j].setBorderPainted(false);
                final int x = i;
                final int y = j;
                boardButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        placeStone(x, y);
                    }
                });
                add(boardButtons[i][j]);
            }
        }
    }

    private void placeStone(int x, int y) {
        if (boardButtons[x][y].getText().isEmpty()) {
            boardButtons[x][y].setText(blackTurn ? "B" : "W");
            boardButtons[x][y].setForeground(blackTurn ? Color.BLACK : Color.WHITE);
            blackTurn = !blackTurn;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GoGame game = new GoGame();
            game.setVisible(true);
        });
    }
}
