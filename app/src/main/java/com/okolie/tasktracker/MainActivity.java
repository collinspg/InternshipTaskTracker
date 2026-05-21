package com.okolie.tasktracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * MainActivity — the home screen of the Internship Task Tracker app.
 * Displays all saved tasks in a scrollable list using RecyclerView.
 * Shows an empty state message when no tasks exist yet.
 * Users can add new tasks via the floating button, or edit/delete existing ones.
 */
public class MainActivity extends AppCompatActivity {

    // Request codes to identify which screen is returning a result to this activity
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    // Keys used to pass task data between MainActivity and AddEditTaskActivity via Intent
    public static final String EXTRA_TITLE = "com.okolie.tasktracker.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.okolie.tasktracker.EXTRA_DESCRIPTION";
    public static final String EXTRA_DUE_DATE = "com.okolie.tasktracker.EXTRA_DUE_DATE";
    public static final String EXTRA_ID = "com.okolie.tasktracker.EXTRA_ID";
    public static final String EXTRA_COMPLETED = "com.okolie.tasktracker.EXTRA_COMPLETED";

    private TaskViewModel taskViewModel;

    // Displays a friendly message when the task list is empty
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the empty state TextView to its layout ID
        textEmpty = findViewById(R.id.text_empty);

        // Set up RecyclerView with a vertical scrolling list layout
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // improves scroll performance

        // Create the adapter and define what happens when Edit or Delete is tapped
        TaskAdapter adapter = new TaskAdapter(new TaskAdapter.OnItemClickListener() {

            @Override
            public void onEditClick(Task task) {
                // Open the form screen pre-filled with the selected task's current data
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra(EXTRA_ID, task.getId());
                intent.putExtra(EXTRA_TITLE, task.getTitle());
                intent.putExtra(EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(EXTRA_DUE_DATE, task.getDueDate());
                intent.putExtra(EXTRA_COMPLETED, task.isCompleted());
                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }

            @Override
            public void onDeleteClick(Task task) {
                // Show a confirmation dialog before permanently deleting the task
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete \"" + task.getTitle() + "\"?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            taskViewModel.delete(task);
                            Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Connect the ViewModel and observe the task list for any database changes
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            // Refresh the list every time the database changes
            adapter.setTasks(tasks);

            // Show the empty message if there are no tasks, otherwise show the list
            if (tasks == null || tasks.isEmpty()) {
                textEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                textEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Floating Action Button — opens the Add Task form when tapped
        FloatingActionButton fabAddTask = findViewById(R.id.fab_add_task);
        fabAddTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });
    }

    /**
     * Called automatically when AddEditTaskActivity finishes and returns data.
     * Reads the task fields from the returned Intent and saves to the database.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Read all task fields returned from the form screen
            String title = data.getStringExtra(EXTRA_TITLE);
            String description = data.getStringExtra(EXTRA_DESCRIPTION);
            String dueDate = data.getStringExtra(EXTRA_DUE_DATE);
            boolean completed = data.getBooleanExtra(EXTRA_COMPLETED, false);

            if (requestCode == ADD_TASK_REQUEST) {
                // Build a new Task object and insert it into the database
                Task task = new Task(title, description, dueDate, completed);
                taskViewModel.insert(task);
                Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();

            } else if (requestCode == EDIT_TASK_REQUEST) {
                // Retrieve the existing task ID so Room knows which row to update
                int id = data.getIntExtra(EXTRA_ID, -1);
                if (id == -1) {
                    Toast.makeText(this, "Error: task could not be updated", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Build updated task object and assign its ID before saving
                Task task = new Task(title, description, dueDate, completed);
                task.setId(id);
                taskViewModel.update(task);
                Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}