package org.robinhahn00;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Bauer extends Stein {

    private boolean dame=false;
    //verschiedenen Bilder für Stein
    File fileBlank = new File("/Users/robin/Desktop/Dame/Leer.png"); //kein Stein
    public Image blank = new Image(fileBlank.toURI().toString());

    //SoundFiles
    File soundZug= new File("/Users/robin/Desktop/Dame/Zug.m4a");
    Media media= new Media(soundZug.toURI().toString());
    MediaPlayer mp3zug= new MediaPlayer(media);

    File soundSchlagen= new File("/Users/robin/Desktop/Dame/Geschlagen.m4a");
    Media schlag= new Media(soundSchlagen.toURI().toString());
    MediaPlayer mp3Schlag= new MediaPlayer(schlag);



    public Bauer(Feld f, boolean w) {
        super(f, w);
    }

    public boolean istDame() {
        return dame;
    }

    @Override
    public boolean zugGueltig(Feld start, Feld ziel, Feld[][] felder) {
        if(start!=null&&ziel!=null) {
            int deltaX = (int) Math.sqrt(Math.pow(start.getKoord()[0] - ziel.getKoord()[0], 2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
            int deltaY = (int) Math.sqrt(Math.pow(start.getKoord()[1] - ziel.getKoord()[1], 2));
            if (ziel.getStein() != null) {
                return false;
            }
            if (schlagGueltig(start, ziel, felder)) {

                return true;
            }
            if (deltaX == 1 && deltaY == 1) {
                if (felder[ziel.getKoord()[0]][ziel.getKoord()[1]].getStein() != null) { //ist feld besetzt?
                    return false;
                } else {
                    //gucken ob stein in richtige richtung geht
                    if (start.getStein().getSteinC()) { //weiss
                        if (start.getKoord()[1] < ziel.getKoord()[1]) { //weiss darf nur nach unten gehen
                            mp3zug.play();
                            return true;
                        }
                    } else { //schwarz
                        if (start.getKoord()[1] > ziel.getKoord()[1]) { //schwarz darf nur nach oben gehen
                            mp3zug.play();
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                return false;
            }
        }return false;
    }


    public boolean schlagGueltig(Feld start, Feld ziel, Feld[][] felder) {

        int deltaX= (int) Math.sqrt(Math.pow(start.getKoord()[0]-ziel.getKoord()[0],2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaY= (int) Math.sqrt(Math.pow(start.getKoord()[1]-ziel.getKoord()[1],2));
        int [] opferKoords= new int[2]; //koordinaten des zu schlagendem steins
        if(ziel.getStein()!=null){
            return false;
        }
        opferKoords[0]= start.getKoord()[0]-(start.getKoord()[0]-ziel.getKoord()[0])/2; //berechne koordinaten des zu schlagendem
        opferKoords[1]= start.getKoord()[1]-(start.getKoord()[1]-ziel.getKoord()[1])/2;

        if(deltaX==2&&deltaY==2) {
            if (felder[opferKoords[0]][opferKoords[1]].getStein() != null){
                if(start.getStein()!=null) {
                    if (felder[opferKoords[0]][opferKoords[1]].getStein().getSteinC() != start.getStein().getSteinC()) {
                        //hier wird der eigentliche schlag ausgeführt
                        mp3Schlag.play();
                        felder[opferKoords[0]][opferKoords[1]].setGraphic(new ImageView(blank));
                        felder[opferKoords[0]][opferKoords[1]].setStein(null);
                        start.getBrettItsOn().setWurdeGradeGeschlagen(true);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }else {
            return false;
        }
        return false;
    }

    @Override
    public boolean schlagMoeglich(Feld start, Feld ziel, Feld[][] f) { // =schlagGueltig, aber ohne wirklich zu schlagen
        int deltaX= (int) Math.sqrt(Math.pow(start.getKoord()[0]-ziel.getKoord()[0],2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaY= (int) Math.sqrt(Math.pow(start.getKoord()[1]-ziel.getKoord()[1],2));
        int [] opferKoords= new int[2]; //koordinaten des zu schlagendem steins
        if(ziel.getStein()!=null){
            return false;
        }
        opferKoords[0]= start.getKoord()[0]-(start.getKoord()[0]-ziel.getKoord()[0])/2; //berechne koordinaten des zu schlagendem
        opferKoords[1]= start.getKoord()[1]-(start.getKoord()[1]-ziel.getKoord()[1])/2;

        if(deltaX==2&&deltaY==2) {
            if (f[opferKoords[0]][opferKoords[1]].getStein() != null){
                if(start.getStein()!=null) {
                    if (f[opferKoords[0]][opferKoords[1]].getStein().getSteinC() != start.getStein().getSteinC()) {
                        //hier wird der eigentliche schlag nicht ausgeführt

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }else {
            return false;
        }
        return false;
    }
}
