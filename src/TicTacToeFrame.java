import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

// WIP
public class TicTacToeFrame {

    private JFrame frame;

    private TicTacToe game;

    private List<JButton> spaces = new LinkedList<>();
    private JButton middle;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(8, r -> new Thread(r));

    /**
     * Holds
     */
    private HashMap<JButton, Tuple<Integer, Integer>> spaceMap = new LinkedHashMap<>();

    public TicTacToeFrame() {
        this.game = new TicTacToe(false);
        this.frame = new JFrame("Tic tac toe");
        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
                space.setFont(new Font(space.getFont().getName(), Font.BOLD, 24));

                space.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // TODO Auto-generated method stub
                        int row = spaceMap.get(e.getComponent()).left;
                        int col = spaceMap.get(e.getComponent()).right;
                        String temp = ((JButton) e.getComponent()).getText();
                        if (!game.isLegalMove(row, col)) {
                            System.out.println("not legal");
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
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // TODO Auto-generated method stub

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

                    }
                });

                // add the space to list
                spaces.add(space);

                // set the index
                if (i == 1 && j == 1)
                    this.middle = space;
                spaceMap.put(space, new Tuple<Integer, Integer>(i, j));
            }
        }


        pane.setBackground(new Color(0xb3deff)); // hmm
        spaces.forEach(pane::add);
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
            executor.schedule(() -> {
                middle.setText("Play again?");
                middle.removeMouseListener(middle.getMouseListeners()[0]);
                middle.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        frame.dispose();
                        new TicTacToeFrame();
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // TODO Auto-generated method stub

                    }
                });
            }, 4L, TimeUnit.SECONDS);

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

    public void handlePaneClick() {

    }

    public static void main(String[] args) {
        new TicTacToeFrame();
    }

}

