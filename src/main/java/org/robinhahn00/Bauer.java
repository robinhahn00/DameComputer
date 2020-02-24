package org.robinhahn00;

import javafx.scene.image.ImageView;

public class Bauer extends Stein {


    public Bauer(Feld feld, boolean istWeiss) {
        super(feld, istWeiss);
    }

    public boolean istDame() {
        return false;
    }

    @Override
    public boolean zugGueltig(Feld start, Feld ziel, Feld[][] felder) {
        if (start != null && ziel != null) {
            int deltaX = (int) Math.sqrt(Math.pow(start.getKoord()[0] - ziel.getKoord()[0], 2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
            int deltaY = (int) Math.sqrt(Math.pow(start.getKoord()[1] - ziel.getKoord()[1], 2));
            if (ziel.getStein() != null) { //ist das Ziel-Feld leer? Falls Nein, dann ist der Zug ungueltig
                return false;
            }

            //gucken ob der stein in richtige richtung geht, denn die Bauern können sich nur nach vorne und nicht nach hinten bewegen
            if (start.getStein().getSteinC()) { //weiss
                if (start.getKoord()[1] > ziel.getKoord()[1]) { //weiss darf nur nach unten gehen. Ist der Zug nach oben, dann ist er ungueltig
                    return false;
                }
            } else { //schwarz
                if (start.getKoord()[1] < ziel.getKoord()[1]) { //schwarz darf nur nach oben gehen
                    return false;
                }
            }


            if (schlagGueltig(start, ziel, felder)) { //ist der Zug ein Schlag? Ja?
                // - Dann ist natürlich der Zug auch so Regelkonform
                return true;
            }

            if (deltaX == 1 && deltaY == 1) { //Wenn der Zug kein Schlag ist, dann darf der Zug nur ein Feld weit und nur Diagonal sein
                return true;

            }
        }
        return false;//ansonsten ist der Zug ungueltig
    }

    /*
    Diese Methode gibt True zurück, wenn ein Bauer 2 Felder Diagonal geht, das Ziel-Feld leer ist und er auf dem Weg einen gegnerischen Spieler
    überspringt. Dieser Wird dann geschlagen.
     */
    public boolean schlagGueltig(Feld start, Feld ziel, Feld[][] felder) {

        int deltaX = (int) Math.sqrt(Math.pow(start.getKoord()[0] - ziel.getKoord()[0], 2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaY = (int) Math.sqrt(Math.pow(start.getKoord()[1] - ziel.getKoord()[1], 2));
        int[] opferKoords = new int[2]; //koordinaten des zu schlagendem steins
        if (ziel.getStein() != null) { //ist das Ziel-Feld leer?
            return false;
        }
        opferKoords[0] = start.getKoord()[0] - (start.getKoord()[0] - ziel.getKoord()[0]) / 2; //berechne koordinaten des zu schlagendem
        opferKoords[1] = start.getKoord()[1] - (start.getKoord()[1] - ziel.getKoord()[1]) / 2;

        if (deltaX == 2 && deltaY == 2) { //geht der Stein genau 2 Schritte in x und y Richtung und geht er Diagonal?
            if (felder[opferKoords[0]][opferKoords[1]].getStein() != null) {
                if (start.getStein() != null) { //gibt es überhaupt einen Stein der den Weg gehen möchte? oder ist das Start-Feld leer?
                    if (felder[opferKoords[0]][opferKoords[1]].getStein().getSteinC() != start.getStein().getSteinC()) { //versucht hier jemand seinen eigenen Stein zu schlagen?
                        //hier wird der eigentliche schlag ausgeführt
                        Assets.mp3Schlag.play();
                        super.getBrett().setWurdeGradeGeschlagen(true);
                        felder[opferKoords[0]][opferKoords[1]].setGraphic(new ImageView(Assets.fieldBlank));  //das Bild wird entfernt
                        felder[opferKoords[0]][opferKoords[1]].setStein(null);// der Stein wird gelöscht
                        start.getBrettItsOn().setWurdeGradeGeschlagen(true); //dem Spiel wird gesagt, dass grade geschlagen wurde. Falls der Spieler
                        //mit dem selben Stein noch einen weiteren gegnerischen Stein schlagen kann, so muss er das auch noch tun! So sind die Regeln!
                        return true; //anschließend wird noch ausgegeben dass der schlag natürlich auch ein gültiger Zug gewesen ist
                    } else { //der eigene Stein kann nicht geschlagen werden!
                        return false; //also false
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /*
    In der Methode wird geprüft ob ein Schlag des Bauerns möglich ist. Die Methode ist dafür da, um dem Spiel zu sagen ob ein Schlag mölgich ist,
    denn wenn ein Schlag möglich ist, so muss auch geschlgen werden! So sind die Regeln. Wenn ein Schlag möglich ist, der Spieler
    versucht jedoch einen Zug zu machen ohne zu schlagen, so darf der Zug nicht ausgeführt werden.
    Deswegen diese Methode!
     */
    @Override
    public boolean schlagMoeglich(Feld start, Feld ziel, Feld[][] f) { // =schlagGueltig, aber ohne wirklich zu schlagen
        int deltaX = (int) Math.sqrt(Math.pow(start.getKoord()[0] - ziel.getKoord()[0], 2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaY = (int) Math.sqrt(Math.pow(start.getKoord()[1] - ziel.getKoord()[1], 2));
        int[] opferKoords = new int[2]; //koordinaten des zu schlagendem steins
        if (ziel.getStein() != null) {
            return false;
        }
        opferKoords[0] = start.getKoord()[0] - (start.getKoord()[0] - ziel.getKoord()[0]) / 2; //berechne koordinaten des zu schlagendem
        opferKoords[1] = start.getKoord()[1] - (start.getKoord()[1] - ziel.getKoord()[1]) / 2;

        if (deltaX == 2 && deltaY == 2) {
            if (f[opferKoords[0]][opferKoords[1]].getStein() != null) {
                if (start.getStein() != null) {
                    if (f[opferKoords[0]][opferKoords[1]].getStein().getSteinC() != start.getStein().getSteinC()) {
                        //hier wird der eigentliche schlag nicht ausgeführt

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }
}
