package org.robinhahn00;

import javafx.scene.image.ImageView;

public abstract class Stein extends ImageView {
    private boolean dame;
    private Feld feld;
    private boolean istWeiss;
    private Brett brett;

    public Stein(Feld feld, boolean istWeiss) {
        this.feld = feld;
        this.istWeiss = istWeiss;
    }

    public boolean getSteinC() {
        return istWeiss;
    }

    public void setSteinC(boolean c) {
        istWeiss = c;
    }

    public boolean istDame() {
        return dame;
    }

    public void setDame() {
        dame = true;
    }

    public Feld getFeld() {
        return feld;
    }

    public void setBrett(Brett brett) {
        this.brett = brett;
    }

    public Brett getBrett() {
        return brett;
    }

    public abstract boolean zugGueltig(Feld start, Feld ziel, Feld[][] f);

    public abstract boolean schlagGueltig(Feld start, Feld ziel, Feld[][] f);

    public abstract boolean schlagMoeglich(Feld start, Feld ziel, Feld[][] f);


}
