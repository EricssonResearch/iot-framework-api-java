package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 17/09/14.
 */
public class Query {

    private String queryString;

    public Query(String QueryString) {
        queryString = QueryString;
    }

    public void setQueryString(String QueryString) {
        queryString = QueryString;
    }

    public String getQueryString() {
        return queryString;
    }

    public String toJsonString() {
        return "{\"query\" : { \"query_string\" : {\"query\" : \"" + queryString + "\"}}}";
    }
}
