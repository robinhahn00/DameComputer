package org.robinhahn00;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * JavaFX APP Dame Computer
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Dame");

        BorderPane layout= new BorderPane();
        GridPane brett= new Brett(8).getBrett();

        layout.setCenter(brett);
        Button reset= new Button("Reset"); //noch keine funktion
        reset.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        stage.close();
                        restart();
                    }
        });
        layout.setLeft(reset);
        var scene = new Scene(layout, 640, 480);
        TextField f= new TextField("Schlag den Computer... Wenn du es schaffst ;)");
        layout.setTop(f);
        stage.setScene(scene);
        stage.show();
    }
    public void restart(){
               cleanUp();
               Stage s = new Stage();
               start(s);
    }
    private void cleanUp(){
        //cleaning
        
    }

    public static void main(String[] args) {
        launch(args);
    }







}