package com.dailytask.manager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Task table")
public class MyTask {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titleTask;
    private String Description;
    private String TaskTime;

    public MyTask() {
    }

    public MyTask(String titleTask, String description, String taskDate) {
        this.titleTask = titleTask;
        Description = description;
        TaskTime = taskDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleTask() {
        return titleTask;
    }

    public void setTitleTask(String titleTask) {
        this.titleTask = titleTask;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTaskTime() {
        return TaskTime;
    }

    public void setTaskTime(String taskTime) {
        TaskTime = taskTime;
    }
}

