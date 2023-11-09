package com.example.coursework;

public class Observation {
    private int id;
    private int activityId;
    private String observationText;
    private String observationTime;
    private String comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getObservationText() {
        return observationText;
    }

    public void setObservationText(String observationText) {
        this.observationText = observationText;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Observation(int id, int activityId, String observationText, String observationTime, String comments) {
        this.id = id;
        this.activityId = activityId;
        this.observationText = observationText;
        this.observationTime = observationTime;
        this.comments = comments;
    }

}

