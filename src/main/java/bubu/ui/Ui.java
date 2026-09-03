package bubu.ui;

import java.util.List;
import java.util.Scanner;

import bubu.task.Task;

/**
 * Handles all console input and output for the chatbot.
 */
public class Ui {
    /** Divider printed between user commands and responses. */
    private static final String LINE = "___________________________________________________________";
    /** Reads user commands from the standard input stream. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the chatbot greeting.
     */
    public void showWelcome() {
        String banner = " /\\___/\\ \n"
                + "(  o.o  )  Hello! I'm BUBU!\n";
        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("What can I do for you? Meow!");
        System.out.println(LINE);
    }

    /**
     * Reads one command entered by the user.
     *
     * @return command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a separator before or after a response.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon! Meow!");
        showLine();
    }

    /**
     * Displays an error message from command processing.
     */
    public void showError(String message) {
        System.out.println(message);
        showLine();
    }

    /**
     * Displays all tasks in their numbered list form.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Meow! Your task list is empty.");
        } else {
            System.out.println(tasks.size() == 1
                    ? "Meow! Here is the task in your list:"
                    : "Meow! Here are the tasks in your list:");
            for (int index = 0; index < tasks.size(); index++) {
                System.out.println((index + 1) + ". " + tasks.get(index));
            }
        }
        showLine();
    }

    /**
     * Displays confirmation that a task was added.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it meow. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
        showLine();
    }

    /**
     * Displays confirmation that a task's completion status changed.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        System.out.println(isDone
                ? "Meow! I've marked this task as done:"
                : "Meow! I've marked this task as not done yet:");
        System.out.println(task);
        showLine();
    }

    /**
     * Displays confirmation that a task was deleted.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Meow! I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
        showLine();
    }

    /**
     * Closes the input stream when the chatbot exits.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints the singular or plural task-count message.
     */
    private void showTaskCount(int taskCount) {
        String noun = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + noun + " in the list. Meow!");
    }

    /**
     * Displays all matchings tasks that match the search keyword.
     *
     * @param tasks list of matching tasks.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Meow! No matching tasks found.");
        } else {
            System.out.println(tasks.size() == 1
                    ? "Meow! Here is the matching task in your list:"
                    : "Meow! Here are the matching tasks in your list:");
            for (int index = 0; index < tasks.size(); index++) {
                System.out.println((index + 1) + ". " + tasks.get(index));
            }
        }
        showLine();
    }
}
