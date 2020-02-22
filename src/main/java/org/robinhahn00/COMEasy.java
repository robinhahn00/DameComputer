package org.robinhahn00;

import javafx.scene.image.Image;

import java.io.File;
import java.util.LinkedList;

public class COMEasy extends COM {

    File fileBlank = new File("/Users/robin/Desktop/Dame/Leer.png"); //kein Stein
    public Image blank = new Image(fileBlank.toURI().toString());

    Feld[] zug = new Feld[2];
    Brett brett;


    LinkedList<Stein> möglich = new LinkedList<Stein>();
    Feld[][] felder;

    public COMEasy(Brett b) {
        brett = b;

    }

    public Feld[] ziehe() {
        felder = brett.getFelderArray();
        for (int i = 0; i < felder.length; i++) {
            for (int i2 = 0; i2 < felder.length; i2++) {
                Stein stein = felder[i][i2].getStein();
                if (stein != null) {
                    if (!stein.getSteinC()) { //also schwarzer stein der ggf gespielt werden kann
                        if (kannErZiehen(stein)) {
                            möglich.add(stein);
                        }
                    }
                }
            }
        }
        //aus welchen Zahlen soll zufällig gewählt werden
        int max = möglich.size();
        int min = 1;
        int range = max - min + 1;
        int rand = (int) (Math.random() * range) + min;
        zug = returnZug(möglich.get(rand - 1));
        return zug;
    }

    public boolean kannErZiehen(Stein s) { //guckt ob ein stein ziehen kann oder sogar schlagen kann

        int x = s.getFeld().getKoord()[0];
        int y = s.getFeld().getKoord()[1];

        //aktuell nur für bauern
        if (y == 0) {
            return false;
        }
        if (x - 1 >= 0) {
            if (felder[x - 1][y - 1].getStein() == null) { //y horizontal x vertikal
                return true; //zug möglich
            }
        }
        if (x + 1 < felder.length) {
            if (felder[x + 1][y - 1].getStein() == null) {
                return true;//zug möglich
            }
        }

        if (x + 2 < felder.length && y - 2 >= 0) {
            if (felder[x + 2][y - 2].getStein() == null && felder[x + 1][y - 1].getStein() != null) {
                if (felder[x + 1][y - 1].getStein().getSteinC()) { //wenn zwischen start und ziel feld ein gegner steht (ein stein des spielers)


                    return true; //schlag durchgefphrt
                }
            }
        }
        if (x - 2 >= 0 && y - 2 >= 0) {
            if (felder[x - 2][y - 2].getStein() == null && felder[x - 1][y - 1].getStein() != null) {
                if (felder[x - 1][y - 1].getStein().getSteinC()) { //wenn zwischen start und ziel feld ein gegner steht (ein stein des spielers)
                    return true; //schölag möglich
                }
            }
        }
        return false; //stein kann sich nicht bewegen!
    }

    public Feld[] returnZug(Stein s) {
        Feld[] z = new Feld[2];
        int x = s.getFeld().getKoord()[0];
        int y = s.getFeld().getKoord()[1];
        z[0] = felder[x][y];

        //aktuell nur für bauern
        if (y == 0) {
            return z;
        }
        if (x - 1 >= 0) {
            if (felder[x - 1][y - 1].getStein() == null) { //x horizontal y vertikal
                z[1] = felder[x - 1][y - 1];
            }
        }
        if (x + 1 < felder.length) {
            if (felder[x + 1][y - 1].getStein() == null) {
                z[1] = felder[x + 1][y - 1];
            }
        }

        if (x - 2 >= 0 && y - 2 >= 0) {
            if (felder[x - 2][y - 2].getStein() == null && felder[x - 1][y - 1].getStein() != null) {
                if (felder[x - 1][y - 1].getStein().getSteinC()) { //wenn zwischen start und ziel feld ein gegner steht (ein stein des spielers)
                    z[1] = felder[x - 2][y - 2];
                }
            }
        }
        if (x + 2 < felder.length && y - 2 >= 0) {
            if (felder[x + 2][y - 2].getStein() == null && felder[x + 1][y - 1].getStein() != null) {
                if (felder[x + 1][y - 1].getStein().getSteinC()) { //wenn zwischen start und ziel feld ein gegner steht (ein stein des spielers)
                    z[1] = felder[x + 2][y - 2];
                }
            }
        }
        return z;

    }


}
