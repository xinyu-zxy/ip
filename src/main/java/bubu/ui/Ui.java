package bubu.ui;

import java.util.List;

import bubu.task.Task;

/**
 * Formats chatbot responses for display in the graphical user interface.
 */
public class Ui {
    /** Stores the response generated for the most recent user command. */
    private final StringBuilder response = new StringBuilder();

    /**
     * Appends the chatbot greeting to the current response.
     */
    public void showWelcome() {
        String banner = " /\\___/\\ \n"
                + "(  o.o  )  Hello! I'm BUBU!\n";
        response.append(banner)
                .append("What can I do for you? Meow!")
                .append(System.lineSeparator());
    }

    /**
     * Appends the farewell message to the current response.
     */
    public void showGoodbye() {
        response.append("Bye. Hope to see you again soon! Meow!")
                .append(System.lineSeparator());
    }

    /**
     * Appends an error message from command processing to the current response.
     */
    public void showError(String message) {
        response.append(message)
                .append(System.lineSeparator());
    }

    /**
     * Appends all tasks in their numbered list form to the current response.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            response.append("Meow! Your task list is empty.")
                    .append(System.lineSeparator());
        } else {
            response.append(tasks.size() == 1
                    ? "Meow! Here is the task in your list:"
                    : "Meow! Here are the tasks in your list:")
                    .append(System.lineSeparator());
            for (int index = 0; index < tasks.size(); index++) {
                response.append(index + 1)
                        .append(". ")
                        .append(tasks.get(index))
                        .append(System.lineSeparator());
            }
        }
    }

    /**
     * Appends confirmation that a task was added to the current response.
     */
    public void showTaskAdded(Task task, int taskCount) {
        response.append("Got it meow. I've added this task:")
                .append(System.lineSeparator())
                .append("  ")
                .append(task)
                .append(System.lineSeparator());
        showTaskCount(taskCount);
    }

    /**
     * Appends confirmation that a task's completion status changed to the current response.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        response.append(isDone
                ? "Meow! I've marked this task as done:"
                : "Meow! I've marked this task as not done yet:")
                .append(System.lineSeparator())
                .append("  ")
                .append(task)
                .append(System.lineSeparator());
    }

    /**
     * Appends confirmation that a task was deleted to the current response.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        response.append("Meow! I've removed this task:")
                .append(System.lineSeparator())
                .append("  ")
                .append(task)
                .append(System.lineSeparator());
        showTaskCount(taskCount);
    }

    /**
     * Appends the singular or plural task-count message to the current response.
     */
    private void showTaskCount(int taskCount) {
        String noun = taskCount == 1 ? "task" : "tasks";
        response.append("Now you have ")
                .append(taskCount)
                .append(" ")
                .append(noun)
                .append(" in the list. Meow!")
                .append(System.lineSeparator());
    }

    /**
     * Appends all matching tasks that match the search keyword to the current response.
     *
     * @param tasks list of matching tasks.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            response.append("Meow! No matching tasks found.")
                    .append(System.lineSeparator());
        } else {
            response.append(tasks.size() == 1
                    ? "Meow! Here is the matching task in your list:"
                    : "Meow! Here are the matching tasks in your list:")
                    .append(System.lineSeparator());
            for (int index = 0; index < tasks.size(); index++) {
                response.append(index + 1)
                        .append(". ")
                        .append(tasks.get(index))
                        .append(System.lineSeparator());
            }
        }
    }

    /**
     * Clears the response buffer to prepare for the next command.
     */
    public void clearResponse() {
        response.setLength(0);
    }

    /**
     * Returns the current response as a string.
     *
     * @return current response.
     */
    public String getResponse() {
        return response.toString();
    }
}
