package com.okolie.tasktracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * TaskDao (Data Access Object) — defines all database operations for the tasks table.
 * Room automatically generates the actual SQL implementation at compile time.
 * We just declare what we want to do, and Room figures out the SQL.
 */
@Dao
public interface TaskDao {

    // Insert a brand-new task into the database
    @Insert
    void insert(Task task);

    // Update an existing task — Room matches it by the task's ID
    @Update
    void update(Task task);

    // Delete a task from the database — Room matches it by the task's ID
    @Delete
    void delete(Task task);

    /**
     * Get all tasks from the database, sorted by due date (earliest first).
     * LiveData means the UI will automatically refresh whenever this data changes —
     * we never have to manually reload the list.
     */
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    LiveData<List<Task>> getAllTasks();
}