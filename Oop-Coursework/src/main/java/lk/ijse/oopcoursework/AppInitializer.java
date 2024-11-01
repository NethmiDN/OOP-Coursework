package lk.ijse.oopcoursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/asset/icon.jpg")));
        primaryStage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Board.fxml")))));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {

        launch(args);
    }
}