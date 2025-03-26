package com.wolfco.main.classes.mongoDB;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Punishment {
    @BsonProperty(value="_id")
    private ObjectId id;
    
    @BsonProperty(value="end_time")
    private Long endTime;
    @BsonProperty(value="start_time")
    private Long startTime;
    private ObjectId appeal;
    private String reason;
    private String status;
    private String type;
    private String moderator;
    private String uuid;

    public Long getEndTime() {
        return endTime;
    }

    public Punishment setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Punishment setStartTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public ObjectId getAppeal() {
        return appeal;
    }

    public Punishment setAppeal(ObjectId appeal) {
        this.appeal = appeal;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public Punishment setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Punishment setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getType() {
        return type;
    }

    public Punishment setType(String type) {
        this.type = type;
        return this;
    }

    public String getModerator() {
        return moderator;
    }

    public Punishment setModerator(String moderator) {
        this.moderator = moderator;
        return this;
    }

    public String getUUID() {
        return uuid;
    }

    public Punishment setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public ObjectId getId() {
        return id;
    }

    public Punishment setId(ObjectId id) {
        this.id = id;
        return this;
    }
}
