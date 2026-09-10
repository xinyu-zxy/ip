package bubu.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the chatbot's tasks and provides controlled operations on the list.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from tasks loaded from storage.
     *
     * @param tasks loaded tasks.
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Tasks cannot be null";
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        assert task != null : "Task cannot be null";
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index zero-based task index.
     * @return task at the index.
     */
    public Task get(int index) {
        assert hasIndex(index) : "No task exists at index " + index;
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index zero-based task index.
     * @return removed task.
     */
    public Task remove(int index) {
        assert hasIndex(index) : "No task exists at index " + index;
        return tasks.remove(index);
    }

    /**
     * Checks whether a zero-based index identifies a task.
     *
     * @param index zero-based task index.
     * @return whether the index is valid.
     */
    public boolean hasIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view for displaying or saving the current tasks.
     *
     * @return unmodifiable task list.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Finds tasks whose descriptions contain a given keyword.
     *
     * @param keyword the keyword to search for.
     * @return a list of matching tasks.
     */
    public List<Task> findMatchingTasks(String keyword) {
        assert keyword != null : "Keyword cannot be null";
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }

    /**
     * Finds incomplete tasks that have a scheduled time within the given window.
     *
     * @param start beginning of the time window, inclusive.
     * @param end end of the time window, inclusive.
     * @return incomplete scheduled tasks within the time window.
     */
    public List<Task> findIncompleteTasksDueBetween(LocalDateTime start, LocalDateTime end) {
        assert start != null : "Start time cannot be null";
        assert end != null : "End time cannot be null";
        assert !end.isBefore(start) : "End time cannot be before start time";

        return tasks.stream()
                .filter(task -> !task.isDone())
                .filter(task -> isDueBetween(task, start, end))
                .toList();
    }

    /**
     * Returns whether a task's reminder time is within the given inclusive window.
     *
     * @param task the task to check
     * @param start the start of the time window
     * @param end the end of the time window
     * @return true if the task's reminder time is within the window, false otherwise
     */
    private boolean isDueBetween(Task task, LocalDateTime start, LocalDateTime end) {
        return task.getReminderTime()
                .filter(reminderTime -> !reminderTime.isBefore(start))
                .filter(reminderTime -> !reminderTime.isAfter(end))
                .isPresent();
    }
}
