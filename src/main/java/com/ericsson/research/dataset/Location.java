package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 17/09/14.
 */
public class Location {

    private String lat;
    private String lon;

    public Location( String Latitude, String Longitude ) {
        lat = Latitude;
        lon = Longitude;
    }

    public String getLatitude() {
        return lat;
    }

    public String getLongitude() {
        return lon;
    }

    public Location setLatitude(String Latitude) {
        lat = Latitude; return this;
    }

    public Location setLongitude(String Longitude) {
        lon = Longitude; return this;
    }

}
