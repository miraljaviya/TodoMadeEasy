package com.dailytask.manager.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepos {
    private TaskDao taskDao;
    private LiveData<List<MyTask>>allTasks;


    public TaskRepos(Application application)
    {
        TaskDatabase database=TaskDatabase.getInstance(application);
        taskDao=database.taskDao();
        allTasks=taskDao.getAllTasks();
    }

     public void insert(MyTask task)
    {
        new InsertTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(MyTask task)
    {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }


    public void update(MyTask task)
    {
        new updateTaskAsyncTask(taskDao).execute(task);
    }


    public void deleteAllTasks()
    {
        new DeleteAllTasksAsyncTask(taskDao).execute();
    }

    public LiveData<List<MyTask>> getAllTasks() {
        return allTasks;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<MyTask,Void,Void>
    {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao)
        {
            this.taskDao=taskDao;

        }
        @Override
        protected Void doInBackground(MyTask... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<MyTask,Void,Void>
    {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao)
        {
            this.taskDao=taskDao;
        }
        @Override
        protected Void doInBackground(MyTask... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class updateTaskAsyncTask extends AsyncTask<MyTask,Void,Void>
    {
        private TaskDao taskDao;

        private updateTaskAsyncTask(TaskDao taskDao)
        {
            this.taskDao=taskDao;

        }
        @Override
        protected Void doInBackground(MyTask... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private TaskDao taskDao;

        private DeleteAllTasksAsyncTask(TaskDao taskDao)
        {
            this.taskDao=taskDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }
}
