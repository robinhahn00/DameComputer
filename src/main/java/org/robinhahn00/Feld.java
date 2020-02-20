package org.robinhahn00;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.*;

import java.io.File;


public class Feld extends Button {
    //Das Feld ist ein Button der gedrückt werden kann. Steine stehen auf einem Feld
    private int[] xy = new int[2];
    private boolean istWeiss = false;
    private Brett brett;
    private Stein s = null; //null=leer

    private boolean gedrueckt = false; //ist das Feld gedrueckt?


    public Feld(Brett b, boolean w) {


        brett = b;
        istWeiss = w;
        if (w) {
            this.setStyle("-fx-background-color: #000000"); //weiss
        } else {
            this.setStyle("-fx-background-color: #ffffff"); //schwarz
        }
    }

    public int[] getKoord() { //gibt Koordinaten aus
        return xy;
    }

    public void setKoord(int x, int y) { //legt die koordinaten fest
        xy[0] = x;
        xy[1] = y;
    }

    public void setStein(Stein s) { //setzt Stein s auf ein feld
        this.s = s;
    }

    public Stein getStein() { //welcher Stein ist auf dem Feld? Wenn null returnt wird ist das Feld leer
        return s;
    }

    public boolean getColour() { //true= weiss, false=schwarz
        return istWeiss;
    }

    public boolean getGedrueckt() { //ist das Feld aktuell gedrueckt?
        return gedrueckt;
    }

    public void setGedrueckt(boolean b) { //markiere das Feld als gedrueckt
        gedrueckt = b;
    }

    public Brett getBrettItsOn() { //auf welchem Brett ist das Feld?
        return brett;
    }
}
