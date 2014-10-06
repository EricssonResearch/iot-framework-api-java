package com.ericsson.research.dataset;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Vandikas on 17/09/14.
 */
public class Stream {

    private String description;
    private Location location;
    private String name;
    @SerializedName("private") private boolean privacy; // this needs to be renamed to private somehow
    private ResourceType resource;
    private String tags;
    private String type;
    private String unit;
    private String accuracy;
    private String max_val;
    private String min_val;
    private String data_type;
    private boolean polling;
    private String parser;
    private float polling_freq;
    private String user_id;

    public Stream(String Uuid, String Name, String Description, String Type, String Tags, String Unit, Location Location, String UserId) {
        data_type = "application\\json";
        resource = new ResourceType("", Uuid);
        name = Name;
        description = Description;
        type = Type;
        tags = Tags;
        unit = Unit;
        location = Location;
        user_id = UserId;
    }

    public Stream( String Uuid, String Name, String Description, Location Location, String UserId) {
        data_type = "application\\json";
        resource = new ResourceType("", Uuid);
        name = Name;
        description = Description;
        location = Location;
        user_id = UserId;
    }

    public Stream setPrivacy(Boolean Privacy) {
        privacy = Privacy;
        return this;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public String getDescription() {
        return description;
    }

    public Stream setDescription(String description) {
        this.description = description;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Stream setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getName() {
        return name;
    }

    public Stream setName(String name) {
        this.name = name;
        return this;
    }

    public String getUuid() {
        return resource.getUuid();
    }

    public Stream setUuid(String uuid) {
        resource.setUuid(uuid);
        return this;
    }

    public String getType() {
        return type;
    }

    public Stream setType(String type) {
        this.type = type;
        return this;
    }

    public String getUserId() {
        return user_id;
    }

    public Stream setUserId(String userId) {
        this.user_id = userId;
        return this;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public Stream setAccuracy(String accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public String getMin_val() {
        return min_val;
    }

    public Stream setMin_val(String min_val) {
        this.min_val = min_val; return this;
    }

    public String getMax_val() {
        return max_val;
    }

    public Stream setMax_val(String max_val) {
        this.max_val = max_val; return this;
    }

    public String getUnit() {
        return unit;
    }

    public Stream setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public Stream setTags(String tags) {
        this.tags = tags; return this;
    }

    public Stream setResourceType( ResourceType resourceType ) {
        this.resource = resourceType;
        return this;
    }

    public Stream setDataType( String data_type ) {
        this.data_type = data_type;
        return this;
    }

    /*public String toJsonString() {
        return "{\"accuracy\":\"" + accuracy + "\" \"data_type\":\"" + data_type + "\",\"description\":\"" + description + "\"," + location + "," +
                "\"name\":\"" + name + "\",\"max_val\":\"" + max_val + "\",\"min_val\":\"" + min_val + "\",\"parser\":\"" + parser + "\",\"polling\":" + Boolean.toString(polling) + ",\"polling_freq\":" + polling_freq + ",\"private\":" + Boolean.toString(privacy) + "," +
                "\"resource\":{\"resource_type\":\"" + resource_type + "\",\"uuid\":\"" + uuid + "\"},\"tags\":\"" + tags + "\"," +
                "\"type\":\"" + type + "\",\"unit\":\"" + unit + "\",\"uri\":\"\",\"user_id\":\"" + userId + "\"}";
    }*/
}
