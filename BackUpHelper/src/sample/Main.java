package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("BackupHelper");
        primaryStage.setScene(new Scene(root, 670, 350));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    public Stage getStage(){
        return stage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
