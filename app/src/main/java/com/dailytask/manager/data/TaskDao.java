package com.dailytask.manager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

   @Insert
    void insert(MyTask myTask);

   @Delete
    void delete(MyTask myTask);

    @Update
    void update(MyTask myTask);

   @Query("DELETE FROM `Task table`")
    void deleteAllTasks();

   @Query("SELECT * FROM `Task table` ORDER BY id ASC")
   LiveData<List<MyTask>>getAllTasks();







}
