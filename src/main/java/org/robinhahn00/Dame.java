package org.robinhahn00;

import javafx.scene.image.ImageView;

public class Dame extends Stein {

    public Dame(Feld feld, boolean istWeiss) {
        super(feld, istWeiss);
    }

    //getter Methode, ob ein Stein vom Typ Dame ist
    public boolean istDame() {
        return true;
    }

    //Methode die Prüft ob ein Zug, so wie er geplant ist, regelkonform ist
    @Override
    public boolean zugGueltig(Feld start, Feld ziel, Feld[][] felder) {
        int deltaX = (int) Math.sqrt(Math.pow(start.getKoord()[0] - ziel.getKoord()[0], 2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaY = (int) Math.sqrt(Math.pow(start.getKoord()[1] - ziel.getKoord()[1], 2));
        int delta = ziel.getKoord()[0] - start.getKoord()[0]; //

        if (deltaX == deltaY) {//damit ein zug gueltig ist, darf der stein sich nur diagonal bewegen
            if (felder[ziel.getKoord()[0]][ziel.getKoord()[1]].getStein() != null) { //ist feld besetzt?
                return false;//dann ist der Zug ungültig
            } else { //das Feld ist frei? Dann prüfe ob der Zug gueltig ist oder ob sogar ein stein geschlagen wird

                if (schlagGueltig(start, ziel, felder)) { //wird bei dem zug ein stein geschlagen oder klappt der zug überhaupt
                    return true;//gueltig
                }
                return false;//ungueltig
            }
        } else { //ist der Stein nicht diagonal gegangen? dann ist der Zug ...
            return false; //...ungueltig
        }
    }

    /*
    Methode die prüft ob bei einem Zug auch ein Spielstein geschlagen wird. Hier findet ausserdem der eigentlich Schlag statt, sprich das entfernen des alten Steines
     */
    @Override
    public boolean schlagGueltig(Feld start, Feld ziel, Feld[][] felder) { //0 = false, 1= true (zug ist richtig), 2= zug richtig und geschlagen
        int deltaXB = (int) Math.sqrt(Math.pow(start.getKoord()[0] - ziel.getKoord()[0], 2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaYB = (int) Math.sqrt(Math.pow(start.getKoord()[1] - ziel.getKoord()[1], 2));
        int deltaX = ziel.getKoord()[0] - start.getKoord()[0];
        int deltaY = ziel.getKoord()[1] - start.getKoord()[1];

        if (deltaXB == deltaYB) { //ist der Zug diagonal über die schwarzen felder?
            int[] c = wieVieleSteineDazwischen(deltaX, deltaY, start, felder); //counter [0] wie viele Steine (gegnerische) zwischen dem Weg von start zu ziel liegen und [1] an welcher x stelle und an welcher [2] y stelle

            if (c[0] == 1) { //wenn nur ein stein dazwischen liegt dann ist der schlag gültig
                Assets.mp3Schlag.play();
                int[] opferKoords = new int[2];
                opferKoords[0] = start.getKoord()[0] + c[1];
                opferKoords[1] = start.getKoord()[1] + c[2];
                felder[opferKoords[0]][opferKoords[1]].setGraphic(new ImageView(Assets.bauerWhite));//der Stein ist ab hier nicht mehr sichtbar
                felder[opferKoords[0]][opferKoords[1]].setStein(null); //und ab hier auch nicht mehr im spiel. Das Feld ist wieder frei

                return true;
            } else if (c[0] == 0) { //zug gueltig aber kein schlag
                Assets.mp3Zug.play();
                return true;
            } else { //zug nicht gueltig, da mehrere gegnerische Steine oder ein eigener zwischen start und ziel liegen
                return false;
            }
        } else { //ansonsten ist der Zug ungültig
            return false;
        }
    }

    /*
    Methode die zwischen zwei feldern, die anzahl an Steinen zählt. Dabei wird sowohl geschaut OB, Wie viele, und Wo Steine dazwischen liegen.
    c(0. Wie viele Steine sind dazwischen? / 1. X-Koordinate eines Steins der dazwischen liegt / 2. Y-Koordinate ein.... liegt)
    c[0]= -1 bedeutet, dass hier ein Stein der eigenen Farbe versucht wurde zu schlagen.
     */
    private int[] wieVieleSteineDazwischen(int deltaX, int deltaY, Feld start, Feld[][] felder) { //return c (anzahl an steinen dazwischen), wenn c=-1, dann schlag nicht gültig
        int[] c = new int[3];
        /*
        Im folgendem gibt es 4 Fälle: Das Ziel ist Links Oben von dem Ziel aus gesehen -> deltaX und deltaY (zielkoord-startkoor) negativ
                                        "          Links Unten     "                   -> deltaX Positiv und deltaY negativ
                                        "          Rechts Oben     "                   -> deltaX ist negativ und deltaY positiv
                                        "          Rechts Unten     "                  -> deltaX ist positiv und deltaY negativ
         */
        if (deltaX < 0) {
            if (deltaY < 0) {
                //x und y neg
                for (int i = -1; i > deltaX; i--) {          //eine For-Schleife, sodass einmal alle Felder zwischen start und ziel geprüft werden ob sie leer sind
                    int i2 = i;
                    Stein s = felder[start.getKoord()[0] + i][start.getKoord()[1] + i2].getStein();
                    if (s != null) { //if ein stein steht dazwischen
                        c[1] = i; //hier werden die Koordinaten des gefundenen Steines gespeichert
                        c[2] = i2;
                        if (s.getSteinC() == super.getSteinC()) {  //if der Stein hat die gleiche Farbe wie der eigene stein?
                            c[0] = -1; //dann ist c[0] = -1 und der Zug somit ungueltig
                            return c;
                        } else { //sonst ist es ein gegnerischer stein also c++
                            c[0]++; //dann wird c[0] (der Counter der Steine zwischen start und ziel) um 1 erhöht
                        }
                    }
                }
            } else { //die funktionsweise ist hier gleich wie oben
                //x neg y pos
                for (int i = -1; i > deltaX; i--) {
                    int i2 = -1 * i;
                    Stein s = felder[start.getKoord()[0] + i][start.getKoord()[1] + i2].getStein();
                    if (s != null) { //if ein stein steht dazwischen
                        c[1] = i;
                        c[2] = i2;
                        if (s.getSteinC() == super.getSteinC()) {  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0] = -1;
                            return c;
                        } else { //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            }
        } else {
            if (deltaY < 0) {
                //x pos y neg
                for (int i = 1; i < deltaX; i++) {
                    int i2 = -1 * i;
                    Stein s = felder[start.getKoord()[0] + i][start.getKoord()[1] + i2].getStein();
                    if (s != null) { //if ein stein steht dazwischen
                        c[1] = i;
                        c[2] = i2;
                        if (s.getSteinC() == super.getSteinC()) {  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0] = -1;
                            return c;
                        } else { //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            } else {
                //x und y pos
                for (int i = 1; i < deltaX; i++) {
                    int i2 = i;

                    Stein s = felder[start.getKoord()[0] + i][start.getKoord()[1] + i2].getStein();
                    if (s != null) { //if ein stein steht dazwischen
                        c[1] = i;
                        c[2] = i2;
                        if (s.getSteinC() == super.getSteinC()) {  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0] = -1;
                            return c;
                        } else { //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            }
        }
        return c;
    }

    @Override
    //die Methode soll true zurückgeben, wenn der Stein einen anderen Stein schlagen könnte und false wenn nicht.
    public boolean schlagMoeglich(Feld start, Feld ziel, Feld[][] f) {
        return false;
    }
}
