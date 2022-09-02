package com.dailytask.manager.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepos repos;
    private LiveData<List<MyTask>>allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repos=new TaskRepos(application);
        allTasks=repos.getAllTasks();
    }

   public void insert(MyTask task){
        repos.insert(task);
   }

   public void delete(MyTask task){
        repos.delete(task);
   }

   public void update(MyTask task){
        repos.update(task);
   }

   public void deleteAll(){
        repos.deleteAllTasks();
   }

    public LiveData<List<MyTask>> getAllTasks() {
        return allTasks;
    }
}












































































  /*  public void insert(MyTask task)
    {
        repos.insert(task);
    }
    public void delete(MyTask task)
    {
        repos.delete(task);
    }

    public void update(MyTask task)
    {
        repos.update(task);
    }

    public void deleteAll()
    {
        repos.deleteAllTasks();
    }

    public LiveData<List<MyTask>> getAllTasks() {
        return allTasks;
    }*/