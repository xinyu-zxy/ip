package bubu.ui;

import java.util.List;

import bubu.task.Task;

/**
 * Formats chatbot responses for display in the graphical user interface.
 */
public class Ui {
    private static final String BANNER = " /\\___/\\ \n"
            + "(  o.o  )  Hello! I'm BUBU!\n";
    private static final String MESSAGE_WELCOME = "What can I do for you? Meow!";
    private static final String MESSAGE_GOODBYE = "Bye. Hope to see you again soon! Meow!";
    private static final String MESSAGE_EMPTY_LIST = "Meow! Your task list is empty.";
    private static final String MESSAGE_NO_MATCHES = "Meow! No matching tasks found.";
    private static final String MESSAGE_TASK_ADDED = "Got it meow. I've added this task:";
    private static final String MESSAGE_TASK_DELETED = "Meow! I've removed this task:";
    private static final String MESSAGE_TASK_DONE = "Meow! I've marked this task as done:";
    private static final String MESSAGE_TASK_NOT_DONE = "Meow! I've marked this task as not done yet:";
    private static final String TASK_ITEM_FORMAT = "  %s";
    private static final String NUMBERED_TASK_FORMAT = "%d. %s";
    private static final String MESSAGE_TASK_COUNT = "Now you have %d %s in the list. Meow!";
    private static final String NOUN_TASK_SINGULAR = "task";
    private static final String NOUN_TASK_PLURAL = "tasks";
    private static final String HEADER_LIST_SINGULAR = "Meow! Here is the task in your list:";
    private static final String HEADER_LIST_PLURAL = "Meow! Here are the tasks in your list:";
    private static final String HEADER_MATCH_SINGULAR = "Meow! Here is the matching task in your list:";
    private static final String HEADER_MATCH_PLURAL = "Meow! Here are the matching tasks in your list:";
    private static final int TASK_COUNT_SINGULAR = 1;



    /** Stores the response generated for the most recent user command. */
    private final StringBuilder response = new StringBuilder();

    /**
     * Appends the chatbot greeting to the current response.
     */
    public void showWelcome() {
        appendLine(BANNER.trim());
        appendLine(MESSAGE_WELCOME);
    }

    /**
     * Appends the farewell message to the current response.
     */
    public void showGoodbye() {
        appendLine(MESSAGE_GOODBYE);
    }

    /**
     * Appends an error message from command processing to the current response.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        appendLine(message);
    }

    /**
     * Appends all tasks in their numbered list form to the current response.
     *
     * @param tasks List of tasks to show.
     */
    public void showTaskList(List<Task> tasks) {
        showTasksWithHeader(tasks, MESSAGE_EMPTY_LIST, HEADER_LIST_SINGULAR, HEADER_LIST_PLURAL);
    }

    /**
     * Appends all matching tasks that match the search keyword to the current response.
     *
     * @param tasks List of matching tasks.
     */
    public void showMatchingTasks(List<Task> tasks) {
        showTasksWithHeader(tasks, MESSAGE_NO_MATCHES, HEADER_MATCH_SINGULAR, HEADER_MATCH_PLURAL);
    }

    /**
     * Appends confirmation that a task was added to the current response.
     *
     * @param task Added task.
     * @param taskCount Total number of tasks remaining.
     */
    public void showTaskAdded(Task task, int taskCount) {
        appendLine(MESSAGE_TASK_ADDED);
        appendLine(String.format(TASK_ITEM_FORMAT, task));
        showTaskCount(taskCount);
    }

    /**
     * Appends confirmation that a task's completion status changed to the current response.
     *
     * @param task Target task.
     * @param isDone True if task is marked complete, false otherwise.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        appendLine(isDone ? MESSAGE_TASK_DONE : MESSAGE_TASK_NOT_DONE);
        appendLine(String.format(TASK_ITEM_FORMAT, task));
    }

    /**
     * Appends confirmation that a task was deleted to the current response.
     *
     * @param task Removed task.
     * @param taskCount Total number of tasks remaining.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        appendLine(MESSAGE_TASK_DELETED);
        appendLine(String.format(TASK_ITEM_FORMAT, task));
        showTaskCount(taskCount);
    }

    /**
     * Renders either an empty-state message or an enumerated list of tasks with the given headers.
     */
    private void showTasksWithHeader(List<Task> tasks, String emptyMessage,
                                     String singularHeader, String pluralHeader) {
        if (tasks.isEmpty()) {
            appendLine(emptyMessage);
            return;
        }

        String header = tasks.size() == TASK_COUNT_SINGULAR ? singularHeader : pluralHeader;
        appendLine(header);
        for (int index = 0; index < tasks.size(); index++) {
            appendLine(String.format(NUMBERED_TASK_FORMAT, index + 1, tasks.get(index)));
        }
    }

    /**
     * Appends the singular or plural task-count message to the current response.
     */
    private void showTaskCount(int taskCount) {
        String noun = taskCount == TASK_COUNT_SINGULAR ? NOUN_TASK_SINGULAR : NOUN_TASK_PLURAL;
        appendLine(String.format(MESSAGE_TASK_COUNT, taskCount, noun));
    }

    /**
     * Appends a text line followed by a platform-independent line separator.
     */
    private void appendLine(String line) {
        response.append(line).append(System.lineSeparator());
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
     * @return Current response.
     */
    public String getResponse() {
        return response.toString();
    }
}
