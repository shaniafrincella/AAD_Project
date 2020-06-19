package com.example.aadproject;

import java.io.Serializable;

public class RestaurantDetail implements Serializable {
    private String name, vicinity, rating, pricelevel;

    public RestaurantDetail(String name, String vicinity, String rating, String pricelevel) {
        this.name = name;
        this.vicinity = vicinity;
        this.rating = rating;
        this.pricelevel = pricelevel;
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
}
