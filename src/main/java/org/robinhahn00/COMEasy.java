package org.robinhahn00;

import java.util.LinkedList;
import java.util.List;

public class COMEasy extends COM {

    private Brett brett;

    private List<Stein> moeglich = new LinkedList<Stein>(); //Liste aller möglichen Zuege des Computers
    private List<Stein> schlaege = new LinkedList<Stein>(); // " , die einen gegn. Stein schlagen
    private Feld[][] felder;

    public COMEasy(Brett brett) {
        this.brett = brett;
    }

    public Feld[] ziehe() {
        felder = brett.getFelderArray();
        moeglich.clear(); //zurücksetzen der Liste, sodass nur die möglichen Züge des aktuellen Spielzuges betrachtet werden
        for (int i = 0; i < felder.length; i++) {
            for (int i2 = 0; i2 < felder.length; i2++) {
                Stein stein = felder[i][i2].getStein();
                if (stein != null) {
                    if (!stein.getSteinC()) { //also schwarzer stein der ggf gespielt werden kann
                        kannErZiehen(stein);
                    }
                }
            }
        }

        if (schlaege.size() == 0) { //wenn kein stein schlagen kann, dann:
            //aus welchen Zahlen soll zufällig gewählt werden
            int max = moeglich.size();
            int min = 1;
            int range = max - min + 1;
            int rand = (int) (Math.random() * range) + min;
            return returnZug(moeglich.get(rand - 1));
        } else { //wenn geschlagen werden kann, dann:
            //aus welchen Zahlen soll zufällig gewählt werden
            int max2 = schlaege.size();
            int min2 = 1;
            int range2 = max2 - min2 + 1;
            int rand2 = (int) (Math.random() * range2) + min2;
            return returnZug(schlaege.get(rand2 - 1));
        }


    }

    public void kannErZiehen(Stein s) { //guckt ob ein stein ziehen kann oder sogar schlagen kann

        int x = s.getFeld().getKoord()[0];
        int y = s.getFeld().getKoord()[1];


        if (!s.istDame()) {
            //aktuell nur für bauern

            if (x - 1 >= 0) {
                if (felder[x - 1][y - 1].getStein() == null) { //y horizontal x vertikal
                    moeglich.add(s); //zug möglich
                }
            }
            if (x + 1 < felder.length) {
                if (felder[x + 1][y - 1].getStein() == null) {
                    moeglich.add(s); //zug möglich
                }
            }

            if (x + 2 < felder.length && y - 2 >= 0) {
                if (felder[x + 2][y - 2].getStein() == null && felder[x + 1][y - 1].getStein() != null) {
                    if (felder[x + 1][y - 1].getStein().getSteinC()) { //wenn zwischen start und ziel feld ein gegner steht (ein stein des spielers)
                        schlaege.add(s);

                    }
                }
            }
            if (x - 2 >= 0 && y - 2 >= 0) {
                if (felder[x - 2][y - 2].getStein() == null && felder[x - 1][y - 1].getStein() != null) {
                    if (felder[x - 1][y - 1].getStein().getSteinC()) { //wenn zwischen start und ziel feld ein gegner steht (ein stein des spielers)
                        schlaege.add(s);

                    }
                }
            }

        } else {
            /*
            Sobald in einer richtung schon ein unüberwindbares hindernis (zB: Stein der eigenen Farbe, 2 Steine hintereinander...)
            soll in der richtung nicht mehr geschaut werden.
            0= Noch nichts gefunden
            1= ein gegnerischer Stein gefunden, der ggf geschlagen werden kann
            2= unüberwindbares hindernis
             */
            int lo = 0; //links oben (RICHTUNG) - -
            int lu = 0; //links unten   - +
            int ro = 0; //rechts oben   + -
            int ru = 0; //rechts unten  + +

            for (int i = 0; i < felder.length; i++) {

                if (ru < 2) { //Richtung: Rechts Unten
                    if (x + i < felder.length && y + i < felder.length) {
                        if ((felder[x + i][y + i].getStein() != null)) { //Besetzt?
                            ru++;
                        } else { //freies feld:
                            if (ru == 1) {
                                schlaege.add(s); //also: je mehr felder hinter einem zu schlagendem stein frei sind, desto höher ist die chance dass dieser stein zieht.
                            } else {
                                moeglich.add(s);
                            }
                        }
                    }
                }
                if (ro < 2) { //Richtung: Rechts Unten
                    if (x + i < felder.length && y - i >= 0) {
                        if ((felder[x + i][y - i].getStein() != null)) { //Besetzt?
                            ru++;
                        } else { //freies feld:
                            if (ru == 1) {
                                schlaege.add(s); //also: je mehr felder hinter einem zu schlagendem stein frei sind, desto höher ist die chance dass dieser stein zieht.
                            } else { //noch kein Feld gefunden?
                                moeglich.add(s); //dann ist die bahn frei und es kann normal gezogen werden
                            }
                        }
                    }
                }
                if (lo < 2) { //Richtung: Rechts Unten
                    if (x - i >= 0 && y + i >= 0) {
                        if ((felder[x - i][y - i].getStein() != null)) { //Besetzt?
                            ru++;
                        } else { //freies feld:
                            if (ru == 1) {
                                schlaege.add(s); //also: je mehr felder hinter einem zu schlagendem stein frei sind, desto höher ist die chance dass dieser stein zieht.
                            } else { //noch kein Feld gefunden?
                                moeglich.add(s); //dann ist die bahn frei und es kann normal gezogen werden
                            }
                        }
                    }
                }
                if (lu < 2) { //Richtung: Rechts Unten
                    if (x - i >= 0 && y + i < felder.length) {
                        if ((felder[x - i][y + i].getStein() != null)) { //Besetzt?
                            ru++;
                        } else { //freies feld:
                            if (ru == 1) {
                                schlaege.add(s); //also: je mehr felder hinter einem zu schlagendem stein frei sind, desto höher ist die chance dass dieser stein zieht.
                            } else { //noch kein Feld gefunden?
                                moeglich.add(s); //dann ist die bahn frei und es kann normal gezogen werden
                            }
                        }
                    }
                }


            }
        }

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
