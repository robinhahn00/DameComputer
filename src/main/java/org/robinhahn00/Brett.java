package org.robinhahn00;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;

public class Brett extends GridPane {
    int size; //größe des Brettes; Standartmässig 8x8
    Feld[][] felder = new Feld[size][size]; //Das Brett, bestehend aus size x size Feldern
    Brett brett;
    int anzahlWeisseSteine; //wenn einer gleich 0, dann spiel vorbei
    int anzahlSchwarzeSteine;
    boolean wurdeGeschlagen = false; //wurde im letzten zug geschlagen?
    COM computer; //der "Gegenspieler"

    boolean weissAnDerReihe = true; //die spieler müssen abwechselnd ziehen true=spieler ist dran, false=com ist dran
    Feld feldGedrueckt = null; //welches Feld wurde gedrückt


    //verschiedenen Bilder für die Steine
    File fileBlank = new File("/Users/robin/Desktop/Dame/Leer.png"); //kein Stein
    public Image blank = new Image(fileBlank.toURI().toString());

    File fileWeiss = new File("/Users/robin/Desktop/Dame/Weiss.png");
    public Image weiss = new Image(fileWeiss.toURI().toString());

    File fileSchwarz = new File("/Users/robin/Desktop/Dame/schwarz.png");
    public Image schwarz = new Image(fileSchwarz.toURI().toString());

    File fileWeissDame = new File("/Users/robin/Desktop/Dame/WeissDame.png");
    public Image weissDame = new Image(fileWeissDame.toURI().toString());

    File fileSchwarzDame = new File("/Users/robin/Desktop/Dame/SchwarzDame.png");
    public Image schwarzDame = new Image(fileSchwarzDame.toURI().toString());

    // Konstruktor
    public Brett(int s) {
        size = s;
        brett = this;


    }

    public Brett getBrett() { //Hier wird das Brett erstellt, die Felder erzeugt und die Steine zu anfang platziert
        brett = new Brett(size);
        brett.setPadding(new Insets(10, 10, 10, 10));
        brett.setHgap(8);
        brett.setVgap(8);
        //Aufurf der methode getButton die ihren Rückgabewert in "felder" speicher. Hier werden die Felder erzeugt
        felder = getButton(this); //
        for (int i = 0; i < size; i++) {
            for (int i2 = 0; i2 < size; i2++) {
                if (felder[i][i2] != null) {
                    Feld b = felder[i][i2];
                    felder[i][i2].setKoord(i, i2); //die Koordinaten des Feldes werden abgespeichert
                    GridPane.setConstraints(b, i, i2); //und das Feld wird in das Brett eingefügt
                    brett.getChildren().add(b);

                    if (!felder[i][i2].getColour()) { //weiss
                        felder[i][i2].setGraphic(new ImageView(blank)); //hier müssen gar keine Steine platziert werden
                    } else { //schwarzes feld
                        if (i2 < (size / 2) - 1) { //weiße steine //die weissen steine kommen nach oben und die schwarzen nach unten
                            felder[i][i2].setStein(new Bauer(felder[i][i2], true)); //anfangs werden natürlich nur bauern und keine damen erzeugt
                            felder[i][i2].setGraphic(new ImageView(weiss));

                        } else if (i2 > size / 2) { //schwarze steine
                            felder[i][i2].setStein(new Bauer(felder[i][i2], false));
                            felder[i][i2].setGraphic(new ImageView(schwarz));
                        } else {
                            felder[i][i2].setGraphic(new ImageView(blank)); //die schwarzen Steine in der Mitte bleiben leer


                        }
                        enableButton(b); //hier wird jeder button/jedes feld aktiviert
                    }
                }
            }
        }
        anzahlWeisseSteine = countSteine(); //wenn einer gleich 0, dann spiel vorbei
        int anzahlSchwarzeSteine = anzahlWeisseSteine;
        computer = new COMEasy(this); //hier wird der COM als spieler erstellt


        return brett;

    } //erzeugen des brettes der größe s

    /*
    In der Methode werden die Felder für das Brett b erzeugt und als 2 Dimensionales Array zurückgegeben
     */
    private Feld[][] getButton(Brett b) {

        Feld[][] spielfeld = new Feld[size][size];
        //Raster Erzeugen
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                Feld field;
                //wenn die felder-koordinaten beide ungerde oder grade sind dann weiss
                if ((i % 2 == 0 && i2 % 2 == 0) || (i % 2 == 1 && i2 % 2 == 1)) {  //=gleich also weiß
                    field = new Feld(b, true);
                } else { //nicht gleich also schwarz
                    field = new Feld(b, false);
                }
                spielfeld[i][i2] = field;

            }
        }

        return spielfeld;
    } //erzeugen der felder in brett b


    /*
    Die Methode wird aufgerufen, sobald ein Feld f gedrückt worden ist. f ist dabei das zuletzt gedrueckte feld und feldGedrueckt ist das vorher gedrueckte
     */
    private void enableButton(Feld f) {

        f.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {


                if (weissAnDerReihe) { //ist weiss an der Reihe? Die Spieler dürfen nur abwechselnd spielen
                    if (f != feldGedrueckt) { //Wurde 2 mal das gleiche Feld gedrueckt?
                        if (!f.getGedrueckt()) { //ist f aktuell gedrueckt?
                            DropShadow shadow = new DropShadow(); //Ein Effekt, sodass man sieht welches Feld gedrueckt ist
                            f.setEffect(shadow);
                            f.setGedrueckt(true);
                            if (feldGedrueckt != null) { //war vorher schon ein Feld gedrueckt oder ist das Feld f das erste was gedrueckt wird
                                //speicher alten stein ab
                                if (feldGedrueckt.getStein() != null) { //war das zuerst gedrueckte feld besetzt?
                                    if (feldGedrueckt.getStein().getSteinC() == weissAnDerReihe) { //ist der stein der gezogen werden soll auch zugehörig zu dem Spieler der an der Reihe ist?
                                        if (!mussGeschlagenWerden(feldGedrueckt, f)) { //gucken ob kein anderer stein geschlagen werden muss
                                            zug(f); //der Zug wird durchgeführt
                                        } else {//wenn ein anderer Stein geschlagen werden muss, dann.....
                                            releaseButton(feldGedrueckt); //... sollen beide Felder "losgelassen" werden
                                            releaseButton(f);
                                        }
                                    } else { //gehört der Stein nicht dem spieler der an der reihe ist?
                                        releaseButton(feldGedrueckt);
                                        releaseButton(f);
                                    }
                                }
                            } else {// war vorher kein Feld gedrueckt? Dann ist feldGedrueckt jetzt das aktuelle Feld f und somit die
                                feldGedrueckt = f;//startposition für den nächsten zug
                            }


                        } else { //ist f grade nicht als gedruekct markiert?
                            releaseButton(f);
                            releaseButton(feldGedrueckt);
                        }

                    } else { //wenn 2mal das gleiche gedrueckt wurde, dann soll dieses bitte losgelassen werden
                        releaseButton(f);

                    }
                } else { //ist weiss nicht an der Reihe? Na dann...
                    comIstDran();//... ist der Computer an der Reihe
                }
            }
        });


    }

    /*
    Methode die aufgerufen wird wenn der Computer an seinem zug ist
     */
    private void comIstDran() {
        Feld[] comZug = computer.ziehe(); //hier wird der Zug bestimmt der gespielt werden soll
        Feld start = comZug[0];
        Feld ziel = comZug[1];
        feldGedrueckt = start; //dabei ist das feldGedrueckt = start, sodass die Methode zug(feld f) hier auch einsetzbar ist
        zug(ziel); //zug des COMs vom Feld Start zum Feld Ziel

    }

    /*
    Methode zum loslassen eines Feldes
     */
    private void releaseButton(Feld f) {
        if (f != null) {
            f.setEffect(null); //Schatten wird entfernt
            f.setGedrueckt(false); //Feld ist nicht mehr als gedrueckt hinterlegt
            feldGedrueckt = null; //altes feldGedrueckt wird entfernt
        }
    }

    /*
    Methode die das Spielfeld als 2 Dimensionales Array zurück gibt
     */
    public Feld[][] getFelderArray() {
        return felder;
    }

    /*
    Methode die die Anzahl der Steine pro Farbe zählt.
     */
    private int countSteine() {
        int c = 0; //anzahl steine
        for (int i = 0; i < felder.length; i++) {
            for (int i2 = 0; i2 < felder[0].length; i2++) {
                if (felder[i][i2].getStein() != null) {
                    c++;
                }
            }
        }
        return c / 2; //dadurch dass die Methode nur zu beginn aufgerufen wird, ist c immer grade und lässt sich somit durch 2 als int teilen
    }

/*
Hier finder der eigentliche Zug von feldGedrueckt zu Feld f statt.
 */
    public void zug(Feld f) {
        if (feldGedrueckt.getStein() != null) { //ist auf dem Start-Feld ein Stein?
            if (feldGedrueckt.getStein().zugGueltig(feldGedrueckt, f, felder)) { //Ist der Zug von feldGedrueckt zu f gueltig?
                if (!wurdeGeschlagen) { //wurde grade geschlagen? Ja? Dann kann der Spieler mit dem Stein, mit dem er grade geschlagen hat, noch weiter schlagen
                    if (weissAnDerReihe) {//war weiss dran?
                        weissAnDerReihe = false;//dann ist jetzt schwarz dran!
                    } else {
                        weissAnDerReihe = true;
                    }
                } else {
                    //mit dem einem stein darf noch weiter geschlagen werden falls möglich
                }
                wurdeGeschlagen = false;

                f.setGraphic(feldGedrueckt.getGraphic()); //das Bild des Steins kommt nun vom alten Feld auf das neue
                //gucken ob stein zur dame wird
                boolean bauerZuDame = false; //wenn der Bauer zur Dame wird, soll er nicht den Typ Stein des Vorgänger-Feldes annehmen

                if (feldGedrueckt.getStein().getSteinC()) { //Ist der Stein weiss?

                    if (f.getKoord()[1] == size - 1) {//ist der Stein einmal über das ganze Brett gewandert ?
                        //Dann wird er zur dame
                        bauerZuDame = true;
                        f.setStein(null);
                        f.setStein(new Dame(f, true));
                        f.getStein().setDame();

                        f.setGraphic(new ImageView(weissDame));
                    }
                } else { //ansonsten ist der Stein schwarz

                    if (f.getKoord()[1] == 0) {
                        //dame
                        bauerZuDame = true;
                        f.setStein(null);
                        f.setStein(new Dame(f, false));
                        f.getStein().setDame();

                        f.setGraphic(new ImageView(schwarzDame));
                    }
                }


                //stein auf neuem feld erzeugen
                if (!bauerZuDame) {//wenn der Bauer in dem Zug nicht zur Dame geworden ist, dann soll er den Stein-Typen behalten

                    if (feldGedrueckt.getStein().istDame()) { //was vor dem zug eine dame war, bleibt eine dame
                        f.setStein(new Dame(f, feldGedrueckt.getStein().getSteinC()));
                    } else {    //was vor dem Zug Bauer war bleibt Bauer
                        f.setStein(new Bauer(f, feldGedrueckt.getStein().getSteinC()));
                    }
                }
                feldGedrueckt.setGraphic(new ImageView(blank)); //Bild vom stein von altem feld löschen
                feldGedrueckt.setStein(null); //der Stein wird auf dem Start feld entfernt
            }
        }
        releaseButton(feldGedrueckt); //nach einem erfolgreichem zug werden alle Felder losgelassen!
        releaseButton(f);

    }
    //Kann und Muss in dem Zug geschlagen werden?
    private boolean mussGeschlagenWerden(Feld eingabeStart, Feld eingabeZiel) { //true= anstelle des zuges muss ein anderer gemacht werden
        int c = 0;
        for (int i = 0; i < felder.length; i++) {
            for (int i2 = 0; i2 < felder[0].length; i2++) {
                if (felder[i][i2].getStein() != null && felder[i][i2].getStein().getSteinC() == weissAnDerReihe) {

                    if (!felder[i][i2].getStein().istDame()) {  //fallunterscheidung ist wichtig für die eingabe des Ziel-Feldes
                        //gucke ob er als bauer schlagen kann
                        Feld start = felder[i][i2];
                        int x = start.getKoord()[0];
                        int y = start.getKoord()[1];


                        if (start.getStein().getSteinC()) { //fallunterscheidung ist wichtig für die eingabe des Ziel-Feldes
                            //weiss
                            if ((x - 2 >= 0) && (y + 2 < size)) { //OutOfBoundsExeption verhindern
                                if (start.getStein().schlagMoeglich(start, felder[x - 2][y + 2], felder)) { //schlag in die eine richtung
                                    if ((start == eingabeStart) && (felder[x - 2][y + 2] == eingabeZiel)) {
                                        return false;
                                    }
                                    c++; //wenn c >0 ist und kein Stein im Zug geschlagen wird, dann ist der Zug ungueltig
                                    //Allerding kann es ja auch mehrere Schlag-Möglichkeiten geben und der Spieler muss
                                    //Davon ja nur eine umsetzen. Deswegen muss c erhöht werden und es kann nicht direkt false returnt werden.


                                }
                            }
                            if ((y + 2 < size) && (x + 2 < size)) {
                                if (start.getStein().schlagMoeglich(start, felder[x + 2][y + 2], felder)) { //schlag in die andere richtung
                                    if ((start == eingabeStart) && (felder[x + 2][y + 2] == eingabeZiel)) {
                                        return false;
                                    }
                                    c++;

                                }
                            }
                        } else {

                            //schwarz
                            if ((y - 2 >= 0) && (x - 2 >= 0)) {
                                if (start.getStein().schlagMoeglich(start, felder[x - 2][y - 2], felder)) { //schlag in die eine richtung
                                    if ((start == eingabeStart) && (felder[x - 2][y - 2] == eingabeZiel)) {
                                        return false;
                                    }
                                    c++;

                                }
                            }
                            if ((y - 2 >= 0) && (x + 2 < size)) {
                                if (start.getStein().schlagMoeglich(start, felder[x + 2][y - 2], felder)) { //schlag in die andere richtung
                                    if ((start == eingabeStart) && (felder[x + 2][y - 2] == eingabeZiel)) {
                                        return false;
                                    }
                                    c++;

                                }
                            }
                        }
                    } else {
                        //gucken ob als dame geschlagen werden kann

                    }
                }

            }
        }
        if (c > 0) { //gab es gelegenheit zu schlagen?
            return true;
        }
        //dann scheint wohl alles richtig gewesen zu sein und der Zug kann ohne Probleme gespielt werden
        return false;
    }

    public void setWurdeGradeGeschlagen(boolean b) { //wenn geschlagen wurde, und noch ein schlag möglich ist, dann muss man nochmal ziehen
        wurdeGeschlagen = b;
    }
}
