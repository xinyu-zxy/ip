package bubu.task;

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
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index zero-based task index.
     * @return task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index zero-based task index.
     * @return removed task.
     */
    public Task remove(int index) {
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
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }
}
