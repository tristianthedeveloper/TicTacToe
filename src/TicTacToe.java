
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    /**
     * The actual tictactoe board.
     */
    char[][] board = {{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}};

    /**
     * The computer
     *
     * @see {TicTacToeAI}
     */
    char computer;

    /**
     * The human.
     */
    char opponent;

    public TicTacToe(boolean b) {
        computer = (new Random().nextBoolean()) ? 'x' : 'o';
        this.opponent = computer == 'o' ? 'x' : 'o';
        reset();
        if (this.computer == ('x') && !b)
            computerMove();
        // TODO Auto-generated constructor stub
    }


    /**
     * Clear the board.
     */
    public void reset() {

        this.board = new char[][]{{'_', '_', '_'}, {'_', '_', '_'}, {'_', '_', '_'}};

    }

    /**
     * @param row    The row to edit.
     * @param column The column to edit.
     * @param who    Who is moving?
     */
    public boolean move(int row, int column, char who) {
        if (!isLegalMove(row, column)) {
            return false;
        }
        this.board[row][column] = who;
        return true;
    }

    /**
     * Prints the board.
     */
    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Run through every possible option, compare different scenarios, and pick
     * a move based off that.
     *
     * @see TicTacToeAI
     */
    public void computerMove() {

        TicTacToeAI.opponent = opponent;
        TicTacToeAI.player = this.computer;
        TicTacToeAI.Move m = TicTacToeAI.findBestMove(this.board);
        if (m.row == -1 || m.col == -1) {
            return;
        }

        this.board[m.row][m.col] = computer;

        System.out.println("The computer has picked: " + (m.row + 1) + "," + (m.col + 1));

    }

    /**
     * Picks who goes first randomly.
     */
    public TicTacToe() {
        computer = (new Random().nextBoolean()) ? 'x' : 'o';
        this.opponent = computer == 'o' ? 'x' : 'o';
        reset();
        if (this.computer == ('x'))
            computerMove();
        // TODO Auto-generated constructor stub
    }

    public TicTacToe(char human, char notHuman) {
        computer = notHuman;
        opponent = human;
        reset();
        if (this.computer == ('x'))
            computerMove();
        // TODO Auto-generated constructor stub
    }

    public TicTacToe(char playerMover) {
        this.computer = playerMover == 'x' ? 'o' : 'x';
        this.opponent = playerMover;
        reset();
        if (this.computer == 'x')
            computerMove();
    }

    /**
     * @param row The row to check.
     * @param col The column to check
     * @returns True if the value at the position row,col is _ (empty)
     */
    public boolean isLegalMove(int row, int col) {
        return '_' == (this.board[row][col]);
    }

    char checkWinner() {

        if (TicTacToeAI.evaluate(this.board) == -10) {
            return this.opponent;
        } else if (TicTacToeAI.evaluate(this.board) == 10) {
            return this.computer;
        }
        if (!TicTacToeAI.canMove(this.board)) {
            return 'n'; // no more moves
        }

        return ' ';
    }

    // UNUSED OLD CLASS

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe('x', 'o');

        Scanner s = new Scanner(System.in);
        boolean flag = false;
        while (game.checkWinner() == ' ') {

            // show them the current board.
            game.printBoard();

            // pick a move.
            System.out.println("Enter row and column you want to move, split by ,");

            if (!flag) {
                // only give the tutorial once.
                System.out.println("for example: 2,2");
                System.out.println("Not java indices! Use ones like a normal human.");
                flag = true;
            }

            System.out.print("your move: ");

            String str = s.nextLine();

            int row = Integer.parseInt(str.split(",")[0]) - 1;
            int col = Integer.parseInt(str.split(",")[1]) - 1;

            if ((row + col) > 4) {
                System.out.println("That's not a legal move, try again");

            } else {

                if (game.move(row, col, game.opponent)) {
                    game.computerMove();
                } else {
                    System.out.println("Illegal move, try again.");
                }

            }

        }

        s.close();
        game.printBoard();

        if (game.checkWinner() == game.computer) {
            System.out.println("Computer wins.");

        } else if (game.checkWinner() == game.opponent) {
            System.out.println("Human wins");

        } else
            System.out.println("It's a draw.");

    }
}

