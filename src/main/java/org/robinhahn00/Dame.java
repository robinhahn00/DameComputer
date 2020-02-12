package org.robinhahn00;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Dame extends Stein{

    File fileBlank = new File("/Users/robin/Desktop/Dame/Leer.png"); //kein Stein
    public Image blank = new Image(fileBlank.toURI().toString());

    //SoundFiles
    File soundZug= new File("/Users/robin/Desktop/Dame/Zug.m4a");
    Media media= new Media(soundZug.toURI().toString());
    MediaPlayer mp3zug= new MediaPlayer(media);
    File soundSchlagen= new File("/Users/robin/Desktop/Dame/Geschlagen.m4a");
    Media schlag= new Media(soundSchlagen.toURI().toString());
    MediaPlayer mp3Schlag= new MediaPlayer(schlag);

    private boolean dame=true;

    public Dame(Feld f, boolean w) {
        super(f, w);
    }

    public boolean istDame() {
        return dame;
    }

    @Override
    public boolean zugGueltig(Feld start, Feld ziel, Feld[][] felder) {
        int deltaX= (int) Math.sqrt(Math.pow(start.getKoord()[0]-ziel.getKoord()[0],2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaY= (int) Math.sqrt(Math.pow(start.getKoord()[1]-ziel.getKoord()[1],2));
        int delta = ziel.getKoord()[0]-start.getKoord()[0]; //
        //if(schlagGueltig(start, ziel, felder)==1){
         //   return true;
        //}
        if(deltaX==deltaY){
            if(felder[ziel.getKoord()[0]][ziel.getKoord()[1]].getStein()!=null){ //ist feld besetzt?
                return false;
            }else{

                if(schlagGueltig(start, ziel, felder)){ //wird bei dem zug ein stein geschlagen oder klappt der zug überhaupt
                    return true;
                }
                return false;
            }
        }else{
            return false;
        }
    }


    public boolean schlagGueltig(Feld start, Feld ziel, Feld[][] felder) { //0 = false, 1= true (zug ist richtig), 2= zug richtig und geschlagen
        int deltaXB= (int) Math.sqrt(Math.pow(start.getKoord()[0]-ziel.getKoord()[0],2)); //Betrag der differenz der koordinaten der 2 gedrückten felder
        int deltaYB= (int) Math.sqrt(Math.pow(start.getKoord()[1]-ziel.getKoord()[1],2));
        int deltaX = ziel.getKoord()[0]-start.getKoord()[0];
        int deltaY = ziel.getKoord()[1]-start.getKoord()[1];

        if(deltaXB==deltaYB){
            int [] c=wieVieleSteineDazwischen(deltaX,deltaY,start,felder); //counter [0] wie viele Steine (gegnerische) zwischen dem Weg von start zu ziel liegen und [1] an welcher x stelle und an welcher [2] y stelle

            if(c[0]==1){ //wenn nur ein stein dazwischen liegt dann ist der schlag gültig
                mp3Schlag.play();
                int[] opferKoords= new int[2];
                opferKoords[0]=start.getKoord()[0]+c[1];
                opferKoords[1]=start.getKoord()[1]+c[2];
                felder[opferKoords[0]][opferKoords[1]].setGraphic(new ImageView(blank));
                felder[opferKoords[0]][opferKoords[1]].setStein(null);

                return true;
            }else if(c[0]==0){ //zug gueltig aber kein schlag
                mp3zug.play();
                return true;
            }else{ //zug nicht gueltig
                return false;
            }
        }else {
            return false;
        }
    }

    private int[] wieVieleSteineDazwischen(int deltaX, int deltaY, Feld start, Feld[][] felder){ //return c (anzahl an steinen dazwischen), wenn c=-1, dann schlag nicht gültig
        int c[] = new int[3];
        if(deltaX<0){
            if(deltaY<0){
                //x und y neg
                for(int i=-1; i>deltaX; i--){
                    int i2=i;
                    Stein s=felder[start.getKoord()[0]+i][start.getKoord()[1]+i2].getStein();
                    if(s!=null){ //if ein stein steht dazwischen
                        c[1]=i;
                        c[2]=i2;
                        if(s.getSteinC()==super.getSteinC()){  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0]=-1;
                            return c;
                        }else{ //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            }else{
                //x neg y pos
                for(int i=-1; i>deltaX; i--){
                    int i2=-1*i;
                    Stein s=felder[start.getKoord()[0]+i][start.getKoord()[1]+i2].getStein();
                    if(s!=null){ //if ein stein steht dazwischen
                        c[1]=i;
                        c[2]=i2;
                        if(s.getSteinC()==super.getSteinC()){  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0]=-1;
                            return c;
                        }else{ //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            }
        }else{
            if(deltaY<0){
                //x pos y neg
                for(int i=1; i<deltaX; i++){
                    int i2=-1*i;
                    Stein s=felder[start.getKoord()[0]+i][start.getKoord()[1]+i2].getStein();
                    if(s!=null){ //if ein stein steht dazwischen
                        c[1]=i;
                        c[2]=i2;
                        if(s.getSteinC()==super.getSteinC()){  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0]=-1;
                            return c;
                        }else{ //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            }else{
                //x und y pos
                for(int i=1; i<deltaX; i++){
                    int i2=i;

                    Stein s=felder[start.getKoord()[0]+i][start.getKoord()[1]+i2].getStein();
                    if(s!=null){ //if ein stein steht dazwischen
                        c[1]=i;
                        c[2]=i2;
                        if(s.getSteinC()==super.getSteinC()){  //if der Stein hat die gleiche Farbe wie der eigene stein
                            c[0]=-1;
                            return c;
                        }else{ //sonst ist es ein gegnerischer stein also c++
                            c[0]++;
                        }
                    }
                }
            }
        }
        return c;
    }

}
