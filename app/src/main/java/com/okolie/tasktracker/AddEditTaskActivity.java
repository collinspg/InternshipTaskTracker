package com.okolie.tasktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * AddEditTaskActivity — the second screen of the app.
 * Serves a dual purpose: adding a brand-new task OR editing an existing one.
 * If an existing task ID is passed in via Intent, all fields are pre-filled for editing.
 * If no ID is passed, the form starts empty for a new task entry.
 */
public class AddEditTaskActivity extends AppCompatActivity {

    // Form input fields
    private EditText editTitle, editDescription, editDueDate;
    private CheckBox checkboxCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Connect each variable to its corresponding view in the XML layout
        editTitle = findViewById(R.id.edit_title);
        editDescription = findViewById(R.id.edit_description);
        editDueDate = findViewById(R.id.edit_due_date);
        checkboxCompleted = findViewById(R.id.checkbox_completed_form);
        Button btnSave = findViewById(R.id.btn_save);

        // Check if we received an existing task ID — if yes, this is an Edit operation
        if (getIntent().hasExtra(MainActivity.EXTRA_ID)) {
            // Pre-fill all form fields with the existing task's current values
            setTitle("Edit Task");
            editTitle.setText(getIntent().getStringExtra(MainActivity.EXTRA_TITLE));
            editDescription.setText(getIntent().getStringExtra(MainActivity.EXTRA_DESCRIPTION));
            editDueDate.setText(getIntent().getStringExtra(MainActivity.EXTRA_DUE_DATE));
            checkboxCompleted.setChecked(getIntent().getBooleanExtra(MainActivity.EXTRA_COMPLETED, false));
        } else {
            // No ID means this is a new task — show a blank form
            setTitle("Add New Task");
        }

        // When Save is tapped, validate and return the data to MainActivity
        btnSave.setOnClickListener(v -> saveTask());
    }

    /**
     * Reads and validates the form fields, then sends the task data
     * back to MainActivity as an Intent result.
     */
    private void saveTask() {
        // Read text from each input field, trimming any extra whitespace
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String dueDate = editDueDate.getText().toString().trim();
        boolean completed = checkboxCompleted.isChecked();

        // Title is the only required field — show an error if it is empty
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show();
            return; // stop here and let the user fix the input
        }

        // Pack all task data into an Intent to send back to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.EXTRA_TITLE, title);
        resultIntent.putExtra(MainActivity.EXTRA_DESCRIPTION, description);
        resultIntent.putExtra(MainActivity.EXTRA_DUE_DATE, dueDate);
        resultIntent.putExtra(MainActivity.EXTRA_COMPLETED, completed);

        // Include the task ID if this was an edit, so MainActivity knows which task to update
        int id = getIntent().getIntExtra(MainActivity.EXTRA_ID, -1);
        if (id != -1) {
            resultIntent.putExtra(MainActivity.EXTRA_ID, id);
        }

        // Send the data back with RESULT_OK so MainActivity knows the save was successful
        setResult(RESULT_OK, resultIntent);
        finish(); // close this screen and return to MainActivity
    }
}