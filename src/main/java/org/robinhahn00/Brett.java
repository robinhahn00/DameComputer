package org.robinhahn00;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Brett extends GridPane {

    private int size;
    private Feld[][] felder = new Feld[size][size];
    private Feld feldGedrueckt; // welches Feld wurde gedrückt

    private COM computer;
    private boolean wurdeGeschlagen = false; // wurde im letzten zug geschlagen?
    private boolean weissAnDerReihe = true; // die spieler müssen abwechselnd ziehen

    public Brett(int s) {
        size = s;
        computer = new COMEasy(this);
    }

    private void initializeBoard() {
        setPadding(new Insets(10, 10, 10, 10));
        setHgap(8);
        setVgap(8);

        felder = getButton(this);
        for (int i = 0; i < size; i++) {
            for (int i2 = 0; i2 < size; i2++) {
                if (felder[i][i2] != null) {
                    Feld b = felder[i][i2];
                    felder[i][i2].setKoord(i, i2);

                    GridPane.setConstraints(b, i, i2);
                    getChildren().add(b);

                    if (!felder[i][i2].getColour()) { //weiss
                        felder[i][i2].setGraphic(new ImageView(Assets.fieldBlank));
                    } else { //schwarzes feld
                        if (i2 < (size / 2) - 1) { //weiße steine
                            felder[i][i2].setStein(new Bauer(felder[i][i2], true));

                            felder[i][i2].setGraphic(new ImageView(Assets.fieldWhite));

                        } else if (i2 > size / 2) { //schwarze steine
                            felder[i][i2].setStein(new Bauer(felder[i][i2], false));

                            felder[i][i2].setGraphic(new ImageView(Assets.fieldBlack));
                        } else {
                            felder[i][i2].setGraphic(new ImageView(Assets.fieldBlank));


                        }
                        enableButton(b);
                    }
                }
            }
        }
    }

    private Feld[][] getButton(Brett b) {
        Feld[][] spielfeld = new Feld[8][8];
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

    private void enableButton(Feld f) {

        f.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {


                if (weissAnDerReihe) {
                    if (f != feldGedrueckt) {
                        if (!f.getGedrueckt()) {
                            DropShadow shadow = new DropShadow();
                            f.setEffect(shadow);
                            f.setGedrueckt(true);
                            if (feldGedrueckt != null) {
                                //speicher alten stein ab
                                if (feldGedrueckt.getStein() != null) {
                                    if (feldGedrueckt.getStein().getSteinC() == weissAnDerReihe) {
                                        if (!mussGeschlagenWerden(feldGedrueckt, f)) { //gucken ob kein anderer stein geschlagen werden muss


                                            zug(f);

                                        } else {
                                            releaseButton(feldGedrueckt);
                                            releaseButton(f);
                                        }
                                    } else {
                                        releaseButton(feldGedrueckt);
                                        releaseButton(f);
                                    }
                                }
                            } else {
                                feldGedrueckt = f;
                            }


                        } else {
                            releaseButton(f);
                            releaseButton(feldGedrueckt);
                        }

                    } else {
                        releaseButton(f);

                    }
                } else {
                    comIstDran();
                }
            }
        });


    }

    private void comIstDran() {
        if (!weissAnDerReihe) { //computer ist dran

            Feld[] comZug = computer.ziehe();
            Feld start = comZug[0];
            Feld ziel = comZug[1];
            feldGedrueckt = start;

            zug(ziel); //zug des COMs
        }
    }

    private void releaseButton(Feld f) {
        if (f != null) {
            f.setEffect(null);
            f.setGedrueckt(false);
            feldGedrueckt = null;
        }
    }

    public Feld[][] getFelderArray() {
        return felder;
    }

    private int countSteine() {
        int c = 0; //anzahl steine
        for (int i = 0; i < felder.length; i++) {
            for (int i2 = 0; i2 < felder[0].length; i2++) {
                if (felder[i][i2].getStein() != null) {
                    c++;
                }

            }
        }
        return c / 2;
    }


    public void zug(Feld f) {
        if (feldGedrueckt.getStein() != null) {
            if (feldGedrueckt.getStein().zugGueltig(feldGedrueckt, f, felder)) {
                if (!wurdeGeschlagen) {
                    if (weissAnDerReihe) {
                        weissAnDerReihe = false;
                    } else {
                        weissAnDerReihe = true;
                    }
                } else {
                    //mit dem einem stein darf noch weiter geschlagen werden falls möglich
                }
                wurdeGeschlagen = false;

                f.setGraphic(feldGedrueckt.getGraphic());
                //gucken ob stein zur dame wird
                boolean bauerZuDame = false; //wenn der Bauer zur Dame wird, soll er nicht den Typ Stein des Vorgänger-Feldes annehmen

                if (feldGedrueckt.getStein().getSteinC()) {

                    if (f.getKoord()[1] == size - 1) {
                        //dame
                        bauerZuDame = true;
                        f.setStein(null);
                        f.setStein(new Dame(f, true));
                        f.getStein().setDame();

                        f.setGraphic(new ImageView(Assets.dameWhite));
                    }
                } else {

                    if (f.getKoord()[1] == 0) {
                        //dame
                        bauerZuDame = true;
                        f.setStein(null);
                        f.setStein(new Dame(f, false));
                        f.getStein().setDame();

                        f.setGraphic(new ImageView(Assets.dameBlack));
                    }
                }


                //stein auf neuem feld erzeugen
                if (!bauerZuDame) {

                    if (feldGedrueckt.getStein().istDame()) {
                        f.setStein(new Dame(f, feldGedrueckt.getStein().getSteinC()));
                    } else {
                        f.setStein(new Bauer(f, feldGedrueckt.getStein().getSteinC()));
                    }
                }
                feldGedrueckt.setGraphic(new ImageView(Assets.fieldBlank)); //stein von altem feld löschen
                feldGedrueckt.setStein(null);
            }
        }
        releaseButton(feldGedrueckt);
        releaseButton(f);

    }

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
                            if ((x - 2 >= 0) && (y + 2 < size)) {
                                if (start.getStein().schlagMoeglich(start, felder[x - 2][y + 2], felder)) { //schlag in die eine richtung
                                    if ((start == eingabeStart) && (felder[x - 2][y + 2] == eingabeZiel)) {
                                        return false;
                                    }
                                    c++;

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
        if (c > 0) {
            return true;
        }
        return false;
    }

    public void setWurdeGradeGeschlagen(boolean b) { //wenn geschlagen wurde, und noch ein schlag möglich ist, dann muss man nochmal ziehen
        wurdeGeschlagen = b;
    }
}
