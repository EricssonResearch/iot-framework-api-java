package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 17/09/14.
 */
public class Datapoint {

    private float value;

    public Datapoint(float Value) {
        value = Value;
    }

    public void setValue(float Value) {
        value = Value;
    }

    public float getValue() {
        return value;
    }

    public String toJsonString() {
        return "{\"value\":" + value + "}";
    }
}
