package com.okolie.tasktracker;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskAdapter — connects the task data to the RecyclerView in MainActivity.
 * Each task in the list gets its own card row on screen.
 * The adapter is responsible for creating and binding each row view.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    /**
     * Interface for handling button clicks inside each task row.
     * MainActivity implements this to handle edit and delete actions.
     */
    public interface OnItemClickListener {
        void onEditClick(Task task);    // called when user taps Edit on a task
        void onDeleteClick(Task task);  // called when user taps Delete on a task
    }

    // The current list of tasks being displayed
    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    // Constructor receives the click listener from MainActivity
    public TaskAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Called by the LiveData observer in MainActivity whenever the database changes.
     * Replaces the current list and refreshes all the rows on screen.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the XML layout for a single task row card
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Get the task for this row position
        Task currentTask = tasks.get(position);

        // Fill in the text views with the task's data
        holder.textTitle.setText(currentTask.getTitle());
        holder.textDueDate.setText("Due: " + currentTask.getDueDate());
        holder.textDescription.setText(currentTask.getDescription());

        // Set the checkbox to match whether the task is completed
        holder.checkboxCompleted.setChecked(currentTask.isCompleted());

        // Strike through the title text if the task is marked as completed
        if (currentTask.isCompleted()) {
            holder.textTitle.setPaintFlags(
                    holder.textTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // Remove strikethrough if task is not completed
            holder.textTitle.setPaintFlags(
                    holder.textTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Pass the clicked task back to MainActivity via the listener interface
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(currentTask));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(currentTask));
    }

    // Total number of task rows to display
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * ViewHolder — caches references to all the views inside one task row.
     * This avoids calling findViewById repeatedly, which improves scroll performance.
     */
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDueDate, textDescription;
        CheckBox checkboxCompleted;
        Button btnEdit, btnDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // Connect each variable to its view ID in item_task.xml
            textTitle = itemView.findViewById(R.id.text_task_title);
            textDueDate = itemView.findViewById(R.id.text_due_date);
            textDescription = itemView.findViewById(R.id.text_description);
            checkboxCompleted = itemView.findViewById(R.id.checkbox_completed);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}