package com.zarvedan.tagthebus;

import java.io.File;

/**
 * Created by zarvedan on 28/04/2015.
 */

//*******************************************************
//
//    Class Photo:
//          Contient les infos indispensables:
//          - le fichierSource (son adresse complète)
//          - le titre
//          - le nom  de la station
//          - la date de la prise de vue
//          - l'heure de la prise de vue
//
//********************************************************


public class Photo {
    protected File fichierSource;
    private String titre;
    private String station;
    private String date;
    private String heure;



    public Photo(File fichierSource) {
        this.fichierSource = fichierSource;
    }

    // Méthode qui parse le nom de fichier complet pour récupérer le titre, la station où a été prise la photo
    // ainsi que la date et l'heure de la prise de vue
    public void parserInfosFichierSource(File fichierSource){
        String nomFichierComplet = fichierSource.getName();

        String delims = "[@%_*]";
        String[] partie = nomFichierComplet.split(delims);

        titre = partie[0];
        station = partie[1];

        String dateTemp = partie[2];
        dateTemp = dateTemp.substring(0, 2) + "/" + dateTemp.substring(2, 4) + "/" + dateTemp.substring(4, dateTemp.length());
        heure = partie[3];
        heure = heure.substring(0, 2) + "h" + heure.substring(2, 4) + "m";
        date = dateTemp + " - "+heure;
    }

    public File getFichierSource() {
        return fichierSource;
    }

    public void setFichierSource(File fichierSource) {
        this.fichierSource = fichierSource;
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
