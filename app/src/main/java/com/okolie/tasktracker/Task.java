package com.okolie.tasktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Task entity — represents one internship task stored in the database.
 * Room uses this class to automatically create a "tasks" table in SQLite.
 * Each field below becomes a column in that table.
 */
@Entity(tableName = "tasks")
public class Task {

    // Auto-generated unique ID for each task (primary key, increments automatically)
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Title of the task (e.g., "Complete weekly report") — required field
    private String title;

    // Optional longer description or notes about the task
    private String description;

    // Due date entered by the user as a string (e.g., "2025-05-20")
    private String dueDate;

    // Tracks whether the task is done (true) or still pending (false)
    private boolean completed;

    /**
     * Constructor used when creating a new task before saving to the database.
     * Room requires that the parameter names match the field names exactly.
     */
    public Task(String title, String description, String dueDate, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    // --- Getters and Setters ---
    // Room needs these to read and write each field in the database

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}