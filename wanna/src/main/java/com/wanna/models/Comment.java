package com.wanna.models;

/**
 * Created by fede on 05/08/15.
 */
public class Comment {
    private String id;
    private String event_id;
    private String user_id;
    private String name;
    private String comment;
    private String time;
    private String admina;

    public Comment(String id, String event_id, String user_id, String name, String comment, String time, String admina) {
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.name = name;
        this.comment = comment;
        this.time = time;
        this.admina = admina;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdmina() {
        return admina;
    }

    public void setAdmina(String admina) {
        this.admina = admina;
    }
}
