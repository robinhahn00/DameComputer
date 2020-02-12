package org.robinhahn00;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

public abstract class Stein extends ImageView {
    private boolean dame;
    private Feld feld;
    private boolean istWeiss;
    private Brett brett;

    public Stein(Feld f, boolean w){
        feld=f;
        istWeiss=w;
       // File file = new File("/Users/robin/Desktop/Dame/White.jpg");
        //Image image = new Image(file.toURI().toString());
        //ImageView iv = new ImageView(image);
    }
    public boolean getSteinC(){
        return istWeiss;
    }
    public void setSteinC(boolean c){
        istWeiss=c;
    }
    public boolean istDame(){
        return dame;
    }
    public void setDame(){
        dame=true;
    }
    public Feld getFeld(){
        return feld;
    }

    public void setBrett(Brett brett) {
        this.brett = brett;
    }
    public Brett getBrett(){
        return brett;
    }

    public abstract boolean zugGueltig(Feld start, Feld ziel, Feld [][] f);
    //public abstract boolean schlagGueltig(Feld start, Feld ziel, Feld [][] f);


}
