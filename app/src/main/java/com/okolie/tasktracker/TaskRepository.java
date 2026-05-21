package com.okolie.tasktracker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * TaskRepository — acts as the middleman between the ViewModel and the database.
 * The UI never talks to the database directly; it always goes through the repository.
 * All database write operations (insert, update, delete) run on a background thread
 * using AsyncTask so we never freeze the main UI thread.
 */
public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    /**
     * Constructor — gets the database instance and retrieves the DAO.
     * Also loads the full task list as LiveData so the UI can observe it.
     */
    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    /**
     * Returns all tasks as LiveData.
     * The UI observes this — when data changes, the list refreshes automatically.
     */
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // Insert a new task — runs on background thread
    public void insert(Task task) {
        new InsertTaskAsync(taskDao).execute(task);
    }

    // Update an existing task — runs on background thread
    public void update(Task task) {
        new UpdateTaskAsync(taskDao).execute(task);
    }

    // Delete a task — runs on background thread
    public void delete(Task task) {
        new DeleteTaskAsync(taskDao).execute(task);
    }

    // --- AsyncTask classes for background database operations ---
    // These prevent the app from freezing while the database is being written to

    private static class InsertTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        InsertTaskAsync(TaskDao dao) { this.taskDao = dao; }

        @Override
        protected Void doInBackground(Task... tasks) {
            // This runs on a background thread — safe to do database work here
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        UpdateTaskAsync(TaskDao dao) { this.taskDao = dao; }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsync extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;
        DeleteTaskAsync(TaskDao dao) { this.taskDao = dao; }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }
}