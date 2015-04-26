package com.zarvedan.tagthebus;

/**
 * Created by zarvedan on 25/04/15.
 */
public class Station extends Object{
    String nomStation;
    Long lat;
    Long lng;

    public Station(String nom, String la, String ln) {
        this.nomStation = nom;
        this.lat=Long.getLong(la);
        this.lng = Long.getLong(ln);
    }
}
