package com.okolie.tasktracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * TaskViewModel — holds and manages the task data for the UI.
 * Survives screen rotations so the app doesn't reload data unnecessarily.
 * MainActivity observes the LiveData here instead of touching the database directly.
 */
public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    /**
     * Constructor — creates the repository and loads the task list.
     * Called automatically by Android when the ViewModel is first needed.
     */
    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    // Returns the full task list as LiveData for MainActivity to observe
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // Delegate all database operations to the repository
    public void insert(Task task) { repository.insert(task); }
    public void update(Task task) { repository.update(task); }
    public void delete(Task task) { repository.delete(task); }
}