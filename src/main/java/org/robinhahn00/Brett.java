package org.robinhahn00;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class Brett extends GridPane {

    public interface PlayerChangedListener {
        public void onPlayerChanged(boolean weissAnDerReihe);
    }

    private int size;
    private Feld[][] felder = new Feld[size][size];
    private Feld feldGedrueckt; // welches Feld wurde gedrückt
    private PlayerChangedListener playerChangedListener;

    private COM computer;
    private boolean wurdeGeschlagen = false; // wurde im letzten zug geschlagen?
    private boolean weissAnDerReihe = true; // die spieler müssen abwechselnd ziehen

    public Brett(int s) {
        size = s;
        computer = new COMEasy(this);
        initializeBoard();
    }

    public void setPlayerChangedListener(PlayerChangedListener listener) {
        this.playerChangedListener = listener;
    }

    private void initializeBoard() {
        setPadding(new Insets(10, 10, 10, 10));
        setHgap(8);
        setVgap(8);

        felder = getButton();
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

                            felder[i][i2].setGraphic(new ImageView(Assets.bauerWhite));

                        } else if (i2 > size / 2) { //schwarze steine
                            felder[i][i2].setStein(new Bauer(felder[i][i2], false));

                            felder[i][i2].setGraphic(new ImageView(Assets.bauerBlack));
                        } else {
                            felder[i][i2].setGraphic(new ImageView(Assets.fieldBlank));


                        }
                        enableButton(b);
                    }
                }
            }
        }
    }

    private Feld[][] getButton() {
        Feld[][] spielfeld = new Feld[8][8];
        //Raster Erzeugen
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                Feld field;
                //wenn die felder-koordinaten beide ungerde oder grade sind dann weiss
                if ((i % 2 == 0 && i2 % 2 == 0) || (i % 2 == 1 && i2 % 2 == 1)) {  //=gleich also weiß
                    field = new Feld(this, true);
                } else { //nicht gleich also schwarz
                    field = new Feld(this, false);
                }
                spielfeld[i][i2] = field;

            }
        }

        return spielfeld;
    } //erzeugen der felder in brett b

    private void enableButton(Feld feld) {
        feld.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent e) {

                // computer ist dran
                if (!weissAnDerReihe) {
                    // wird nun simuliert
                    // computerZug();
                    return;
                }

                // markiertes feld wieder freigeben
                if (feld == feldGedrueckt) {
                    releaseButton(feld);
                    return;
                }

                // markiertes feld wieder freigeben
                if (feld.getGedrueckt()) {
                    releaseButton(feld);
                    releaseButton(feldGedrueckt);
                    return;
                }

                // feld markieren
                DropShadow shadow = new DropShadow();
                feld.setEffect(shadow);
                feld.setGedrueckt(true);
                if (feldGedrueckt == null) {
                    feldGedrueckt = feld;
                    return;
                }

                // wenn auf dem feld kein stein ist, ignorieren
                if (feldGedrueckt.getStein() == null) {
                    return;
                }

                // speicher alten stein ab
                if (feldGedrueckt.getStein().getSteinC() == weissAnDerReihe) {

                    // gucken ob kein anderer stein geschlagen werden muss
                    if (!mussGeschlagenWerden(feldGedrueckt, feld)) {
                        zug(feld);
                    } else {
                        releaseButton(feldGedrueckt);
                        releaseButton(feld);
                    }

                } else {
                    releaseButton(feldGedrueckt);
                    releaseButton(feld);
                }

                // einfach
                computerZug();
            }
        });
    }

    private void computerZug() {
        if (weissAnDerReihe) {
            return;
        }

        //computer ist dran
        Feld[] comZug = computer.ziehe();
        Feld start = comZug[0];
        Feld ziel = comZug[1];
        feldGedrueckt = start;

        //  non-FX thread
        new Thread(() -> {
            try {
                // imitating guess work between 0 and 1 seconds
                Thread.sleep(new Random().nextInt(500));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Platform.runLater(() -> {
                zug(ziel);
                // computer darf ggf. öfter ziehen.

            });

        }).start();

    }

    private void releaseButton(Feld f) {
        if (f == null) {
            return;
        }

        f.setEffect(null);
        f.setGedrueckt(false);
        feldGedrueckt = null;
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


    public void zug(Feld feld) { //feld = ziel
        var originWeissAnDerReihe = weissAnDerReihe;
        if (feldGedrueckt.getStein() != null) {
            if (feldGedrueckt.getStein().zugGueltig(feldGedrueckt, feld, felder)) {


                feld.setGraphic(feldGedrueckt.getGraphic());
                //gucken ob stein zur dame wird
                boolean bauerZuDame = false; //wenn der Bauer zur Dame wird, soll er nicht den Typ Stein des Vorgänger-Feldes annehmen

                if (feldGedrueckt.getStein().getSteinC()) { //weiss

                    if (feld.getKoord()[1] == size - 1) { //ist der stein einmal über das ganze feld und wird somit zur dame?
                        //dame
                        bauerZuDame = true;
                        feld.setStein(null);
                        feld.setStein(new Dame(feld, true));
                        feld.getStein().setDame();

                        feld.setGraphic(new ImageView(Assets.dameWhite));
                    }
                } else { //schwarz

                    if (feld.getKoord()[1] == 0) {//ist der stein einmal über das ganze feld und wird somit zur dame?
                        //dame
                        bauerZuDame = true;
                        feld.setStein(null);
                        feld.setStein(new Dame(feld, false));
                        feld.getStein().setDame();

                        feld.setGraphic(new ImageView(Assets.dameBlack));
                    }
                }
                //hier mehrfach schlagen überprüfen
                if (wurdeGeschlagen) {
                    weiterZiehen(feld); //wer zieht als nächstes?
                } else {  //mit dem einem stein darf noch weiter geschlagen werden falls möglich
                    weissAnDerReihe = !weissAnDerReihe;
                }
                wurdeGeschlagen = false;


                //stein auf neuem feld erzeugen
                if (!bauerZuDame) {

                    if (feldGedrueckt.getStein().istDame()) {
                        feld.setStein(new Dame(feld, feldGedrueckt.getStein().getSteinC()));
                    } else {
                        feld.setStein(new Bauer(feld, feldGedrueckt.getStein().getSteinC()));
                    }
                }
                feldGedrueckt.setGraphic(new ImageView(Assets.fieldBlank)); //stein von altem feld löschen
                feldGedrueckt.setStein(null);
            }
        }

        if (playerChangedListener != null && weissAnDerReihe != originWeissAnDerReihe) {
            playerChangedListener.onPlayerChanged(weissAnDerReihe);
        }
        releaseButton(feldGedrueckt);
        releaseButton(feld);
    }

    private void weiterZiehen(Feld f) {
        if(f.getStein()==null){
            return;
        }
        Stein s = f.getStein(); //der stein der gezogen hat
        int x = s.getFeld().getKoord()[0];
        int y = s.getFeld().getKoord()[1];
        boolean weiter = false; //geht noch eins schlag?
        if (s.istDame()) { //dame

        } else { //bauer
            if (s.getSteinC()) {
                if (y - 2 >= 0) {
                    if (x + 2 < size) {
                        if (felder[x + 1][y - 1].getStein() != null && !felder[x + 1][y + 1].getStein().getSteinC()) {
                            if (felder[x + 2][y - 2].getStein() == null) {
                                weiter = true;
                            }
                        }
                    }
                    if (x - 2 >= 0) {
                        if (felder[x - 1][y - 1].getStein() != null && !felder[x - 1][y + 1].getStein().getSteinC()) {
                            if (felder[x - 2][y - 2].getStein() == null) {
                                weiter = true;
                            }
                        }
                    }
                }

            } else {
                if (y + 2 < size) {
                    if (x + 2 < size) {
                        if (felder[x + 1][y + 1].getStein() != null && !felder[x + 1][y + 1].getStein().getSteinC()) {
                            if (felder[x + 2][y + 2].getStein() == null) {
                                weiter = true;

                            }
                        }
                    }
                    if (x - 2 >= 0) {
                        if (felder[x - 1][y + 1].getStein() != null && !felder[x - 1][y + 1].getStein().getSteinC()) {
                            if (felder[x - 2][y + 2].getStein() == null) {
                                weiter = true;
                            }
                        }
                    }
                }

            }
            System.out.println(weiter);
            if (weiter) {
                //
            } else {
                weissAnDerReihe = !weissAnDerReihe;
            }
        }


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
        return c > 0;
    }

    public void setWurdeGradeGeschlagen(boolean b) { //wenn geschlagen wurde, und noch ein schlag möglich ist, dann muss man nochmal ziehen
        wurdeGeschlagen = b;
    }
}
