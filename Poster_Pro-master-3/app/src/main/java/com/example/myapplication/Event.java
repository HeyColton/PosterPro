package com.example.myapplication;




public class Event {

    private String title;
    private String location;
    private String time;
    private String content;
    private String poster;
    private int auid;

    public Event(){

    }

    public Event(String title, String location, String time, String content, String poster,int auid) {
        this.title = title;
        this.location = location;
        this.time = time;
        this.content = content;
        this.poster = poster;
        this.auid=auid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getAuid(){ return auid; }

    public void setAuid(){this.auid=auid;}
}