package com.okolie.tasktracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * TaskDatabase — the main Room database class for this app.
 * This is the single entry point to access the underlying SQLite database.
 * Uses the Singleton pattern to ensure only one database instance exists at a time,
 * which prevents data conflicts and saves memory.
 */
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    // The DAO we use to run all database queries
    public abstract TaskDao taskDao();

    // Static instance — only one copy of the database ever exists
    private static TaskDatabase instance;

    /**
     * Returns the single shared database instance.
     * Creates it the first time, reuses it every time after.
     * "synchronized" prevents two threads from accidentally creating two instances.
     */
    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            // Build the database the first time it is needed
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TaskDatabase.class,
                            "task_database"  // this is the name of the .db file saved on the device
                    )
                    .fallbackToDestructiveMigration() // if schema changes, wipe and rebuild the db
                    .build();
        }
        return instance;
    }
}