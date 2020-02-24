package org.robinhahn00;

import javafx.scene.control.Button;


public class Feld extends Button {
    //Das Feld ist ein Button der gedrückt werden kann. Steine stehen auf einem Feld
    private int[] xy = new int[2];
    private boolean istWeiss;
    private Brett brett;
    private Stein s = null;

    private boolean gedrueckt = false; //ist das Feld gedrueckt?

    public Feld(Brett brett, boolean w) {
        this.brett = brett;
        this.istWeiss = w;

        if (w) {
            this.setStyle("-fx-background-color: #000000"); //weiss
        } else {
            this.setStyle("-fx-background-color: #ffffff"); //schwarz
        }
    }

    public int[] getKoord() {
        return xy;
    }

    public void setKoord(int x, int y) {
        xy[0] = x;
        xy[1] = y;
    }

    public void setStein(Stein s) {
        this.s = s;
    }

    public Stein getStein() {
        return s;
    }

    public boolean getColour() {
        return istWeiss;
    }

    public boolean getGedrueckt() {
        return gedrueckt;
    }

    public void setGedrueckt(boolean b) {
        gedrueckt = b;
    }

    public Brett getBrettItsOn() {
        return brett;
    }
}
