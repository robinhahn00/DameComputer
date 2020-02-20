package org.robinhahn00;

public abstract class COM {
    //Methode die in der Klasse Brett aufgerufen wird um den Zug des COM einzuleiten. Das Feld[] wird zurück gegeben
    // wobei hier der [0] Startpunkt und das [1] Ziel gespeichert werden
    public abstract Feld[] ziehe();
}