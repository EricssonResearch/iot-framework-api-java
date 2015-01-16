package com.ericsson.research.dataset;

import com.google.gson.annotations.SerializedName;

/*
 * ##_BEGIN_LICENSE_##
 * IoT-Framework API Java
 * ----------
 * Copyright (C) 2014 Ericsson AB
 * ----------
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the Ericsson AB nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * ##_END_LICENSE_##
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
    private String _id;

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

    public Stream( String Uuid, String Name, String Description, Location Location) {
        data_type = "application\\json";
        resource = new ResourceType("", Uuid);
        name = Name;
        description = Description;
        location = Location;
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

    public String getId() {
        return _id;
    }

    public Stream setId( String Id ) {
        _id = Id;
        return this;
    }
}
