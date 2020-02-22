package org.robinhahn00;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX APP Dame Computer
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Dame");

        var layout = new BorderPane();
        var brett = new Brett(8);

        layout.setCenter(brett);
        var reset = new Button("Reset"); //noch keine funktion
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.close();
                restart();
            }
        });
        layout.setLeft(reset);
        var scene = new Scene(layout, 750, 690);
        var f = new TextField("Schlag den Computer... Wenn du es schaffst ;)");
        layout.setTop(f);

        brett.setPlayerChangedListener(new Brett.PlayerChangedListener() {
            @Override
            public void onPlayherChanged(boolean weissAnDerReihe) {
                if (weissAnDerReihe) {
                    f.setText("Spieler weiß ist dran");
                } else {
                    f.setText("Spieler schwarz ist dran");
                }
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public void restart() {
        cleanUp();
        Stage s = new Stage();
        start(s);
    }

    private void cleanUp() {
        //cleaning

    }

    public static void main(String[] args) {
        launch(args);
    }


}