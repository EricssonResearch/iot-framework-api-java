package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 18/09/14.
 */
public class Resource {
    private String description;
    private String manufacturer;
    private String model;
    private String name;

    private Stream[] streams_suggest;

    public Stream[] getSuggestedStreams() {
        return streams_suggest;
    }
}
