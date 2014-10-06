package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 25/09/14.
 */
public class Hits {

    private String _index;
    private String _type;
    private String _id;
    private float _score;
    private Resource _source;

    public String getId() {
        return _id;
    }

    public Resource getResource() {
        return _source;
    }
}
