package com.dailytask.manager.data;

public class CompletedTaskClass {
    private String title,Time;
    private String id;

    public CompletedTaskClass() {
    }

    public CompletedTaskClass(String title, String time) {
        this.title = title;
        Time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
