package com.wolfco.main.classes.mongoDB;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Punishment {
    @BsonProperty(value="_id")
    private ObjectId id;
    
    @BsonProperty(value="end_time")
    private String endTime;
    @BsonProperty(value="start_time")
    private String startTime;
    private ObjectId appeal;
    private String reason;
    private String status;
    private String type;
    private String moderator;
    private String uuid;

    public long getEndTime() {
        return Long.parseLong(endTime);
    }

    public void setEndTime(long endTime) {
        this.endTime = Long.toString(endTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return Long.parseLong(startTime);
    }

    public void setStartTime(long startTime) {
        this.startTime = Long.toString(startTime);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ObjectId getAppeal() {
        return appeal;
    }

    public void setAppeal(ObjectId appeal) {
        this.appeal = appeal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
