package bubu.task;

/**
 * Represents a task without a date or time.
 */
public class ToDo extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description task description.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
