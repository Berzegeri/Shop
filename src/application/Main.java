package application;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	Parent root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = new FXMLLoader().load(getClass().getResource("LogXML.fxml"));
        primaryStage.setTitle("Web Shop Login Screen");
        Scene LogScene = new Scene(root, 480, 320);
        LogScene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(LogScene);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}