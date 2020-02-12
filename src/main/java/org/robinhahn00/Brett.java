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
    int size;
    Feld[][] felder= new Feld[size][size];
    Brett brett;
    Feld feldGedrueckt=null; //welches Feld ist gedrückt


    //verschiedenen Bilder für Stein
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





    public Brett(int s){
        size=s;
        brett=this;

    }
    public Brett getBrett(){
        brett= new Brett(size);
        brett.setPadding(new Insets(10,10,10,10));
        brett.setHgap(8);
        brett.setVgap(8);
        felder= getButton(this);
        for(int i=0; i<size; i++){
            for(int i2=0; i2<size; i2++){
                if(felder[i][i2]!=null){
                    Feld b = felder[i][i2];
                    felder[i][i2].setKoord(i,i2);

                    GridPane.setConstraints(b,i,i2);
                    brett.getChildren().add(b);

                    if(!felder[i][i2].getColour()){ //weiss
                        felder[i][i2].setGraphic(new ImageView(blank));
                    }else{ //schwarzes feld
                        if(i2<(size/2)-1){ //weiße steine
                            felder[i][i2].setStein(new Bauer(felder[i][i2], true));

                            felder[i][i2].setGraphic(new ImageView(weiss));

                        }else if(i2>size/2){ //schwarze steine
                            felder[i][i2].setStein(new Bauer(felder[i][i2], false));

                            felder[i][i2].setGraphic(new ImageView(schwarz));
                        }else{
                            felder[i][i2].setGraphic(new ImageView(blank));


                        }
                        enableButton(b);
                    }
                }
            }
        }


        return brett;

    } //erzeugen des brettes der größe s
    private Feld[][] getButton(Brett b){

        Feld[][] spielfeld= new Feld[8][8];
        //Raster Erzeugen
        for(int i=0; i<8; i++){
            for(int i2=0; i2<8; i2++){
                Feld field;
                //wenn die felder-koordinaten beide ungerde oder grade sind dann weiss
                if((i%2==0 && i2%2==0)||(i%2==1 && i2%2==1)) {  //=gleich also weiß
                    field= new Feld(b, true);
                }else{ //nicht gleich also schwarz
                    field= new Feld(b, false);
                }
                spielfeld[i][i2]=field;

            }
        }

        return spielfeld;
    } //erzeugen der felder in brett b

    private void enableButton(Feld f){

        f.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                 if(f!=feldGedrueckt) {
                     if (!f.getGedrueckt()) {
                         DropShadow shadow = new DropShadow();
                         f.setEffect(shadow);
                         f.setGedrueckt(true);
                         if (feldGedrueckt != null) {
                             //speicher alten stein ab
                             if (feldGedrueckt.getStein() != null) {
                                 if (feldGedrueckt.getStein().zugGueltig(feldGedrueckt, f, felder)) {

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

                                             f.setGraphic(new ImageView(weissDame));
                                         }
                                     } else {

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
                                     if (!bauerZuDame) {

                                         if (feldGedrueckt.getStein().istDame()) {
                                             f.setStein(new Dame(f, feldGedrueckt.getStein().getSteinC()));
                                         } else {
                                             f.setStein(new Bauer(f, feldGedrueckt.getStein().getSteinC()));
                                         }
                                     }
                                     feldGedrueckt.setGraphic(new ImageView(blank)); //stein von altem feld löschen
                                     feldGedrueckt.setStein(null);
                                     releaseButton(feldGedrueckt);
                                     releaseButton(f);
                                 } else {
                                     releaseButton(feldGedrueckt);
                                     releaseButton(f);
                                 }
                             }
                         } else {
                             feldGedrueckt = f;
                         }


                     } else {
                         //releaseButton(f);
                     }

                 }else{
                     releaseButton(f);

                 }
            }
        });


    }
    private void releaseButton(Feld f){
        f.setEffect(null);
        f.setGedrueckt(false);
        feldGedrueckt=null;
    }
    public Feld[][] getFelderArray(){
        return felder;
    }




    public Brett returnBrett(){
        return brett;
    }

}
