package com.example.aadproject;

/**
 * This class declares the restaurant detail information using the getter method is used to
 * retrieve the data and also implements the serializable interface
 */

import java.io.Serializable;

public class RestaurantDetail implements Serializable {
    private String name;
    private String vicinity;
    private String rating;
    private String pricelevel;
    private String imagereference;

    public RestaurantDetail(String name, String vicinity, String rating, String pricelevel, String imagereference) {
        this.name = name;
        this.vicinity = vicinity;
        this.rating = rating;
        this.pricelevel = pricelevel;
        this.imagereference = imagereference;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getRating() {
        return rating;
    }

    public String getPricelevel() {
        return pricelevel;
    }

    public String getImagereference() {
        return imagereference;
    }
}
