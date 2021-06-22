import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

// WIP
public class TicTacToeFrame {

    /**
     * What holds all the components.
     */
    private JFrame frame;

    /**
     * The game itself.
     */
    private TicTacToe game;

    /*
     * The list of spaces on the board.
     */

    private List<JButton> spaces = new LinkedList<>();

    // the middle of the board, used for the play again function.
    private JButton middle;

    /**
     * A multithreaded pool of executable functions.
     */
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8, r -> new Thread(r));

    /**
     * Holds all the spaces.
     */
    private HashMap<JButton, Tuple<Integer, Integer>> spaceMap = new LinkedHashMap<>();

    public TicTacToeFrame() {
        this.game = new TicTacToe(true);
        this.frame = new JFrame("Tic tac toe");
        this.middle = null;
        this.setup();
        // TODO Auto-generated constructor stub
    }

    public void setup() {

        // 3x3, 1 vecS, 1 hS
        JPanel pane = new JPanel(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton space = new JButton("_");

                space.setBackground(new Color(0x3ffabff));

                space.setFont(new Font(space.getFont().getName(), Font.BOLD, 50));

                space.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // TODO Auto-generated method stub
                        int row = spaceMap.get(e.getComponent()).left;
                        int col = spaceMap.get(e.getComponent()).right;
                        String temp = ((JButton) e.getComponent()).getText();

                        if (!game.isLegalMove(row, col)) {

                            ((JButton) e.getComponent()).setText("Illegal Action!");

                            executor.schedule(() -> {
                                ((JButton) e.getComponent()).setText(temp);
                            }, 2L, TimeUnit.SECONDS);

                            return;
                        }
                        game.board[row][col] = game.opponent;

                        ((JButton) e.getComponent()).setText("" + game.opponent);
                        // show the user the move

                        // make the computer move
                        compMove();

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // int row = spaceMap.get(e.getComponent()).left;
                        // int col = spaceMap.get(e.getComponent()).right;
                        // game.board[row][col] = game.opponent;

                        // ((JButton) e.getComponent()).setText("" +
                        // game.opponent);
                        // show the user the move

                        // make the computer move
                        // compMove();
                        // TODO remove, unused. see mousePressed(MouseEvent)

                    }
                });

                // add the space to list
                spaces.add(space);

                // set the index of the very middle of the board.
                if (i == 1 && j == 1)
                    this.middle = space;

                spaceMap.put(space, new Tuple<Integer, Integer>(i, j));

            }
            System.out.println(spaceMap.values());
        }

        // cool color!11
        pane.setBackground(new Color(0xb3deff)); // hmm

        // add everything to be rendered
        spaces.forEach(pane::add);

        // finally render our game.
        frame.setContentPane(pane);
        frame.setSize(frame.getMaximumSize());
        frame.setVisible(true);

        if (game.computer == 'x')
            compMove();
    }

    public void compMove() {

        char opponent = game.opponent;
        TicTacToeAI.opponent = opponent;
        TicTacToeAI.player = game.computer;
        TicTacToeAI.Move m = TicTacToeAI.findBestMove(game.board);

        if (m.col > -1 && m.row > -1) {

            game.board[m.row][m.col] = game.computer;
            System.out.println("row: " + m.row + ", col: " + m.col);
            // find the space at the index

            spaceMap.keySet().stream().filter(e -> spaceMap.get(e).left == m.row && spaceMap.get(e).right == m.col)
                    .findFirst().orElse(null).setText("" + game.computer);

        }
        if (game.checkWinner() != ' ') {

            if (game.checkWinner() == game.computer) {
                this.middle.setText("Computer wins!");
                System.out.println("Computer wins.");

            } else if (game.checkWinner() == game.opponent) {
                this.middle.setText("Human wins!");

            } else
                this.middle.setText("Cat's game!");

            // asynchronous play again button.
            executor.schedule(() -> {
                middle.setText("Play again?");
                middle.removeMouseListener(middle.getMouseListeners()[0]);
                middle.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                        // reset
                        frame.dispose();

                        TicTacToeFrame.main(null);

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }
                });
            }, 500L, TimeUnit.MILLISECONDS);

        }
    }

    public static class Tuple<S1, S2> {

        public S1 left;
        public S2 right;

        public Tuple(S1 left, S2 right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Tuple{left=" + left + ",right=" + right + "}";
        }
    }

    public static void main(String[] args) {
        new TicTacToeFrame();
    }

}

