package lk.ijse.oopcoursework.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lk.ijse.oopcoursework.service.*;

public class BoardController implements BoardUI {

    private static final int GRID_SIZE = 3;
    private String currentPlayer = "X";
    private String playerName = "Nethmi";
    private AIPlayer aiPlayer;
    private HumanPlayer humanPlayer;

    @FXML
    private JFXButton btnPlayAgain;

    @FXML
    private Group grpCols;

    @FXML
    private Label lblStatus;

    @FXML
    private Pane pneOver;

    @FXML
    private AnchorPane root;

    @FXML
    private GridPane gridBoard;

    private JFXButton[][] grid;
    private boolean isGameOver;
    private boolean isAiPlaying = false;

    @FXML
    public void initialize() {
        createGrid();
        initializeGame();
        isGameOver = false;
    }

    private void initializeGame() {
        Board board = new BoardImpl(this);
        humanPlayer = new HumanPlayer(board);
        aiPlayer = new AIPlayer(board);
    }

    private void createGrid() {
        grid = new JFXButton[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JFXButton buttonActions = new JFXButton();
                buttonActions.setMinSize(100, 100);
                buttonActions.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                buttonActions.setOnMouseClicked(this::handleButtonClick);
                gridBoard.add(buttonActions, j, i);
                grid[i][j] = buttonActions;
            }
        }
    }

    int row = -1, col = -1;

    @FXML
    private void handleButtonClick(MouseEvent event) {
        JFXButton buttonClicked = (JFXButton) event.getSource();
        row = -1;
        col = -1;

        outerLoop:
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == buttonClicked) {
                    row = i;
                    col = j;
                    break outerLoop;
                }
            }
        }

        if (!isGameOver && buttonClicked.getText().isEmpty()) {
            humanPlayer.move(row, col);
            if (!isGameOver) {
                Platform.runLater(() -> {
                    int[] bestMove = aiPlayer.getBestMove();
                    aiPlayer.move(bestMove[0], bestMove[1]);
                });
            }
        }
    }

    @Override
    public void update(int row, int col, boolean isHuman) {
        if (isGameOver) return;
        JFXButton buttonUpdate = grid[row][col];

        if (buttonUpdate.getText().isEmpty()) {
            buttonUpdate.setText(currentPlayer);
            buttonUpdate.setStyle("-fx-font-size: 36px; -fx-text-fill: black;");

            checkWinner();

            if (!isGameOver) {
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                lblStatus.setText("Current Player: " + (isHuman ? playerName : "AI"));
            }
        }
    }

    public void checkWinner() {
        String winner = null;
        for (int row = 0; row < GRID_SIZE; row++) {
            if (checkLine(grid[row][0], grid[row][1], grid[row][2])) {
                winner = grid[row][0].getText();
            }
        }

        for (int col = 0; col < GRID_SIZE; col++) {
            if (checkLine(grid[0][col], grid[1][col], grid[2][col])) {
                winner = grid[0][col].getText();
            }
        }

        if (checkLine(grid[0][0], grid[1][1], grid[2][2]) || checkLine(grid[0][2], grid[1][1], grid[2][0])) {
            winner = grid[1][1].getText();
        }

        if (winner != null) {
            notifyWinner(winner.equals("X") ? Piece.X : Piece.O);
        } else if (isBoardFull()) {
            notifyWinner(Piece.EMPTY);
        }
    }

    private boolean checkLine(JFXButton cell1, JFXButton cell2, JFXButton cell3) {
        return !cell1.getText().isEmpty() && cell1.getText().equals(cell2.getText()) && cell2.getText().equals(cell3.getText());
    }

    private boolean isBoardFull() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void notifyWinner(Piece winningPiece) {
        isGameOver = true;
        lblStatus.getStyleClass().clear();

        if (winningPiece == Piece.X) {
            lblStatus.setText(playerName + ", you have won the game!");
            lblStatus.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 24px; -fx-font-weight: bold;");
        } else if (winningPiece == Piece.O) {
            lblStatus.setText("Game over, AI has won the game!");
            lblStatus.setStyle("-fx-text-fill: #F44336; -fx-font-size: 24px; -fx-font-weight: bold;");
        } else {
            lblStatus.setText("It's a tie!");
            lblStatus.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 24px; -fx-font-weight: bold;");
        }

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), lblStatus);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        pneOver.setVisible(true);
        pneOver.toFront();
        Platform.runLater(btnPlayAgain::requestFocus);
    }

    @FXML
    void btnPlayAgainOnAction(ActionEvent event) {
        resetBoard();
    }

    private void resetBoard() {
        lblStatus.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold;");

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j].setText("");
                grid[i][j].setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 2;");
            }
        }

        currentPlayer = "X";
        isGameOver = false;

        lblStatus.setText(playerName + ", it's your turn");
        pneOver.setVisible(false);
        initializeGame();
    }
}