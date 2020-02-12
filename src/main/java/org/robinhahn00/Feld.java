package org.robinhahn00;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.*;
import java.io.File;


public class Feld extends Button {

    private int [] xy= new int[2];
    private boolean istWeiss=false;
    private Brett brett;
    private Stein s=null;
    private ImageView iv;
    private boolean gedrueckt= false; //ist das Feld gedrueckt?


    public Feld(Brett b, boolean w){


        brett=b;
        istWeiss=w;
        if(w) {
            this.setStyle("-fx-background-color: #000000");
        }else{
            this.setStyle("-fx-background-color: #ffffff");
        }
    }
    public int[] getKoord(){
        return xy;
    }
    public void setKoord(int x, int y){
        xy[0]=x;
        xy[1]=y;
    }

    public void setStein( Stein s){
        this.s = s;
    }
    public Stein getStein(){
        return s;
    }
    public boolean getColour(){
        return istWeiss;
    }
    public boolean getGedrueckt(){
        return gedrueckt;
    }
    public void setGedrueckt(boolean b){
        gedrueckt=b;
    }
    public Brett getBrettItsOn(){
        return brett;
    }
}
