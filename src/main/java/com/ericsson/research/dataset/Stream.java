package com.ericsson.research.dataset;

/**
 * Created by Konstantinos Vandikas on 17/09/14.
 */
public class Stream {

    private String description;
    private String location;
    private String name;
    private Boolean privacy;
    private String uuid;
    private String tags;
    private String type;
    private String unit;
    private String userId;
    private String accuracy;
    private String max_val;
    private String min_val;

    public Stream(String Uuid, String Name, String Description, String Type, String Tags, String Unit, String Location, String UserId) {
        uuid = Uuid;
        name = Name;
        description = Description;
        type = Type;
        tags = Tags;
        unit = Unit;
        location = Location;
        userId = UserId;
    }

    public Stream( String Uuid, String Name, String Description, String Location, String UserId) {
        uuid = Uuid;
        name = Name;
        description = Description;
        location = Location;
        userId = UserId;
    }

    public void setPrivacy(Boolean Privacy) {
        privacy = Privacy;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getMin_val() {
        return min_val;
    }

    public void setMin_val(String min_val) {
        this.min_val = min_val;
    }

    public String getMax_val() {
        return max_val;
    }

    public void setMax_val(String max_val) {
        this.max_val = max_val;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // remember to add min and max here
    public String toJsonString() {
        return "{\"accuracy\":\"" + accuracy + "\" \"data_type\":\"application/json\",\"description\":\"" + description + "\"," + location + "," +
                "\"name\":\"" + name + "\",\"max_val\":\"" + max_val + "\",\"min_val\":\"" + min_val + "\",\"parser\":\"\",\"polling\":false,\"polling_freq\":0,\"private\":" + privacy.toString() + "," +
                "\"resource\":{\"resource_type\":\"\",\"uuid\":\"" + uuid + "\"},\"tags\":\"" + tags + "\"," +
                "\"type\":\"" + type + "\",\"unit\":\"" + unit + "\",\"uri\":\"\",\"user_id\":\"" + userId + "\"}";
    }
}
