package bubu.exception;

/**
 * Indicates that a task index is missing, malformed, or outside the task list.
 */
public class InvalidIndexException extends BubuException {

    /**
     * Creates an error for an index outside the current task-list range.
     *
     * @param totalTasks number of tasks currently available
     */
    public InvalidIndexException(int totalTasks) {
        super("Meow! The index you provided is invalid. Please provide a valid index between 1 and "
                + totalTasks + ".");
    }

    /**
     * Creates an error for a non-numeric index argument.
     *
     * @param invalidIndex invalid index text
     */
    public InvalidIndexException(String invalidIndex) {
        super("'" + invalidIndex + "' is not a valid index meow. Please provide a valid index.");
    }
}
