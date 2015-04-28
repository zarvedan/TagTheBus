package com.zarvedan.tagthebus;

import android.util.Log;

import java.io.File;

/**
 * Created by zarvedan on 28/04/2015.
 */
public class Photo {
    protected File fichierSource;
    private String titre;
    private String station;
    private String date;
    private String dateTemp;
    private String heure;

    public File getFichierSource() {
        return fichierSource;
    }

    public void setFichierSource(File fichierSource) {
        this.fichierSource = fichierSource;
    }

    public Photo(File fichierSource) {
        this.fichierSource = fichierSource;
    }

    public void parserInfosFichierSource(File fichierSource){
        String nomFichierComplet = fichierSource.getName();

        String delims = "[@%_*]";
        String[] partie = nomFichierComplet.split(delims);

        titre = partie[0];
        station = partie[1];

        dateTemp = partie[2];
        dateTemp = dateTemp.substring(0, 2) + "/" + dateTemp.substring(2, 4) + "/" + dateTemp.substring(4, dateTemp.length());
        heure = partie[3];
        heure = heure.substring(0, 2) + "h" + heure.substring(2, 4) + "m";
        date = dateTemp + " - "+heure;
    }


    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
}
