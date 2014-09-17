package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 17/09/14.
 */
public class Location {

    private String latitude;
    private String longitude;

    public Location( String Latitude, String Longitude ) {
        latitude = Latitude;
        longitude = Longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String Latitude) {
        latitude = Latitude;
    }

    public void setLongitude(String Longitude) {
        longitude = Longitude;
    }

    public String toJsonString() {
        return "\"location\":{\"lat\":" + latitude + ",\"lon\":" + longitude + "}";
    }

}
