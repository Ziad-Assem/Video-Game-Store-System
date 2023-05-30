package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends Application {

    private static Stage stg;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("LogInForm.fxml"));
        primaryStage.setTitle("Video Game Store System");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        stg=primaryStage;




    }

    //changes the scene
    public void changeScene(String fxml)throws Exception
    {
        Parent pane=FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    //overload that changes scene
    public void changeScene(String fxmlName,Stage stg) {
        Main m = new Main();

        try {
            m.changeScene(fxmlName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) {
        launch(args);
    }
}
