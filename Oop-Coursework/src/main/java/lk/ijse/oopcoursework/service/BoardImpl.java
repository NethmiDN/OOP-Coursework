package lk.ijse.oopcoursework.service;

public class BoardImpl implements Board {
    private final BoardUI boardUI;
    private final int SIZE = 3;
    private Piece[][] board;

    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
        board = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    @Override
    public BoardUI getBoardUI() {
        return this.boardUI;
    }

    @Override
    public void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Piece.EMPTY;
            }
        }
    }

    @Override
    public boolean isLegalMove(int row, int col) {
        return board[row][col] == Piece.EMPTY;
    }

    @Override
    public void updateMove(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    public void resetBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                updateMove(row, col, Piece.EMPTY);
            }
        }
    }

    @Override
    public Winner checkWinner() {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][0] != Piece.EMPTY && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                return new Winner(board[row][0], 0, row, 1, row, 2, row);
            }
        }

        for (int col = 0; col < SIZE; col++) {
            if (board[0][col] != Piece.EMPTY && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return new Winner(board[0][col], col, 0, col, 1, col, 2);
            }
        }

        if (board[0][0] != Piece.EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return new Winner(board[0][0], 0, 0, 1, 1, 2, 2);
        }

        if (board[0][2] != Piece.EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return new Winner(board[0][2], 2, 0, 1, 1, 0, 2);
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == Piece.EMPTY) {
                    return new Winner();  // Game is still ongoing
                }
            }
        }

        return new Winner(Piece.EMPTY);
    }


    @Override
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public int[][] getState() {
        int[][] state = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == Piece.EMPTY) {
                    state[i][j] = 0;
                } else if (board[i][j] == Piece.X) {
                    state[i][j] = 1;
                } else {
                    state[i][j] = -1;
                }
            }
        }
        return state;
    }
}