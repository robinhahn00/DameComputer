package org.robinhahn00;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public abstract class Assets {

    private Assets() {
    }

    public static final Image fieldBlank = new Image(new File("assets/Leer.png").toURI().toString());
    public static final Image fieldWhite = new Image(new File("assets/Weiss.png").toURI().toString());
    public static final Image fieldBlack = new Image(new File("assets/schwarz.png").toURI().toString());
    public static final Image dameWhite = new Image(new File("assets/WeissDame.png").toURI().toString());
    public static final Image dameBlack = new Image(new File("assets/SchwarzDame.png").toURI().toString());

    public static final MediaPlayer mp3Schlag = new MediaPlayer(new Media(new File("assets/Geschlagen.m4a").toURI().toString()));
    public static final MediaPlayer mp3Zug = new MediaPlayer(new Media(new File("assets/Zug.m4a").toURI().toString()));

}
