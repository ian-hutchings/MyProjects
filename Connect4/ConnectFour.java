package Connect4;// Ian Hutchings
// 01/24/2023
// CSE 123
// C1: Abstract Strategy Games
// TA: Ben Wang

// This class is a AbstractStrategyGame.AbstractStrategyGame.ConnectFour game. It creates a board of open spaces and takes user input to
// fill up the board until one player gets 4 tokens in a row (vertically or horizontally) and 
// wins or the game results in a tie.

import java.util.*;

public class ConnectFour implements AbstractStrategyGame {
    private String[][] board;
    private boolean redTurn;
    private String[] players;


    // Behavior
    //  - This method constructs a new AbstractStrategyGame.AbstractStrategyGame.ConnectFour object. It initializes the blank board and sets
    //    the first turn to player 1 (the red player)'s turn.
    public ConnectFour() {
        String[][] board = new String[6][7];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = "⚪";
            }
        }
        this.board = board;

        this.redTurn = true;

        String[] players = {"🔴", "🟡"};
        this.players = players;
    }

    // Behavior
    //  - This method returns an explination of how the game works and the instructions for how
    //    to play.
    // Returns
    //  - String: the game's instructions
    public String instructions() {
        return "Welcome to Connect 4!" + "\n" +
        "In order to win get 4 of your tokens in a row." + "\n" +
        "The board consists of 6 rows and 7 columns." + "\n" + 
        "To play, select which column you would like to place your token in.";
    }

    // Behavior
    //  - This method returns a visualization of the board, with all of the tokens that the
    //    players have placed.
    // Returns
    //  - String: the game board
    public String toString() {
        String fullBoard = "";
        for (int row = 0; row < board.length; row++) {
            fullBoard += Arrays.toString(board[row]) + "\n";
        }
        return fullBoard;
    }

    // Behavior
    //  - This method checks if the game is over.
    // Returns
    //  - boolean: returns true if the game is over and false if it is ongoing.
    public boolean isGameOver() {
        return getWinner() >= 0;
    }

    // Behavior
    //  - This method determines the winner of the game.
    // Returns
    //  - int: returns -1 if the game is ongoing, 0 if the game ended in a tie, 1 if player one
    //    has won and 2 if player two is the winner.
    public int getWinner() {
        int winner = -1;
        boolean redWin = false;
        boolean yellowWin = false;

        for (int row = 0; row < board.length; row++) {
            int colCount = 0;
            for (int col = 0; col < board[row].length - 1; col++) {
                if (board[row][col] != "⚪") {
                    if (board[row][col] == board[row][col + 1]) {
                        if (colCount == 0) {
                            colCount++;
                        }
                        colCount++;
                    }
                    else {
                        colCount = 0;
                    }
                    if (colCount >= 4) {
                        for (int i = 0; i < players.length; i++) {
                            if (players[i] == board[row][col]) {
                                winner = i + 1;
                                if (winner == 1) {
                                    redWin = true;
                                }
                                if (winner == 2) {
                                    yellowWin = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int col = 0; col < board[0].length; col++) {
            int rowCount = 1;
            for (int row = 0; row < board.length - 1; row++) {
                if (board[row][col] != "⚪") {
                    if (board[row][col] == board[row + 1][col]) {
                        if (rowCount == 0) {
                            rowCount++;
                        }
                        rowCount++;
                    }
                    if (rowCount >= 4) {
                        for (int i = 0; i < players.length; i++) {
                            if (players[i] == board[row][col]) {
                                winner = i + 1;
                                if (winner == 1) {
                                    redWin = true;
                                }
                                if (winner == 2) {
                                    yellowWin = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (redWin && yellowWin) {
            return 0;
        }

        return winner;
    }

    // Behavior
    //  - This method returns the current player.
    // Returns
    //  - int: returns 1 or 2 if it's player one or two's turn respectively
    public int getNextPlayer() {
        if (isGameOver()) {
            return -1;
        }
        return redTurn ? 1 : 2;
    }

    // Behavior
    //  - This method allows the user to either place or remove a token by selecting the column
    //    of the board in which they would like to do so.
    // Parameters
    //  - input: the scanner that read's the user's input
    public void makeMove(Scanner input) {
        boolean end = false;
        while (end != true) {
            System.out.print("Would you like to place a token (P) or remove a token (R)? ");
            String choice = input.next();
            
            if (choice.equalsIgnoreCase("P")) {
                placeToken(input);
                end = true;
            }
            else if (choice.equalsIgnoreCase("R")) {
                removeToken(input);
                end = true;
            }
            else {
                System.out.println("Unrecognized input, please try again.");
                System.out.println();
            }
        }
    }

    // Behavior
    //  - This method places a token in the column that the user selects at the bottommost
    //    row that doesn't already have a token.
    // Parameters
    //  - input: the scanner that read's the user's input
    // Exceptions
    //  - if the user selects a column that is outside of the range of available columns (0-6)
    //    or if that column is already full, an IllegalArgumentException is thrown.
    private void placeToken(Scanner input) {
        System.out.print("Place in column (0-6): ");
        int pick = input.nextInt();

        if (pick < 0 || pick > board.length || board[0][pick] != "⚪") {
            throw new IllegalArgumentException();
        }

        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][pick].equals("⚪")) {
                board[row][pick] = players[getNextPlayer() - 1];
                row = -1;
            }
        }

        redTurn = !redTurn;
    }

    // Behavior
    //  - This method removes a token from the column that the user selects at the bottommost
    //    row if that token was previously placed by the same player.
    // Parameters
    //  - input: the scanner that read's the user's input
    // Exceptions
    //  - if the user selects a column that is outside of the range of available columns (0-6)
    //    or if the bottom token of that column is not the current player's token, an 
    //    IllegalArgumentException is thrown.
    private void removeToken(Scanner input) {
        System.out.print("Remove from column (0-6): ");
        int pick = input.nextInt();

        if (pick < 0 || pick > board.length || board[board.length - 1][pick] != 
                                                        players[getNextPlayer() - 1]) {
            throw new IllegalArgumentException();
        }

        for (int row = board.length - 1; row > 0; row--) {
            board[row][pick] = board[row - 1][pick];
        }
        board[0][pick] = "⚪";
    }
}