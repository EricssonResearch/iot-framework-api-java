package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 09/10/14.
 */
public class StreamCreationResponse {

    private boolean ok;
    private String _index;
    private String _type;
    private String _id;
    private String _version;

    public String getId() {
        return _id;
    }

    public boolean getOk() {
        return ok;
    }

    public String getIndex() {
        return _index;
    }

    public String getType() {
        return _type;
    }

    public String getVersion() {
        return _version;
    }
}
