package com.wanna.models;

import java.util.ArrayList;

/**
 * Created by fede on 25/03/15.
 */
public class ActivityAll {
    //Atributos
    private String id;
    private String title;
    private String creator_name;
    private String creator;
    private String location;
    private String datetime;
    private String datetime_creation;
    private String quota;
    private String icon;
    private String claps;
    private int heart;
    private int comment;
    private ArrayList<String> participant;
    private ArrayList<String> confirmed;
    private ArrayList<String> notconfirmed;

    public ActivityAll() {

    }

    public ActivityAll(String id, String title, String creator_name, String creator, String location, String datetime_creation, String datetime, String quota, String icon, String claps, int heart, int comment, ArrayList<String> participant, ArrayList<String> confirmed, ArrayList<String> notconfirmed) {
        this.id = id;
        this.title = title;
        this.creator_name = creator_name;
        this.creator = creator;
        this.location = location;
        this.datetime_creation = datetime_creation;
        this.datetime = datetime;
        this.quota = quota;
        this.icon = icon;
        this.claps = claps;
        this.heart = heart;
        this.comment = comment;
        this.participant = participant;
        this.confirmed = confirmed;
        this.notconfirmed = notconfirmed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatetime_creation() {
        return datetime_creation;
    }

    public void setDatetime_creation(String datetime_creation) {
        this.datetime_creation = datetime_creation;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getClaps() {
        return claps;
    }

    public void setClaps(String claps) {
        this.claps = claps;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public ArrayList<String> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<String> participant) {
        this.participant = participant;
    }

    public ArrayList<String> getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(ArrayList<String> confirmed) {
        this.confirmed = confirmed;
    }

    public ArrayList<String> getNotconfirmed() {
        return notconfirmed;
    }

    public void setNotconfirmed(ArrayList<String> notconfirmed) {
        this.notconfirmed = notconfirmed;
    }
}
