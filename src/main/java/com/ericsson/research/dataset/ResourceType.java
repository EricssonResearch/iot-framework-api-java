package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandilas on 25/09/14.
 */
public class ResourceType {

    private String resource_type;
    private String uuid;

    public ResourceType(String resource_type, String uuid ) {
        this.resource_type = resource_type;
        this.uuid = uuid;
    }

    public ResourceType setResourceType(String resource_type) {
        this.resource_type = resource_type;
        return this;
    }

    public ResourceType setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getResourceType() {
        return resource_type;
    }

    public String getUuid() {
        return uuid;
    }
}
