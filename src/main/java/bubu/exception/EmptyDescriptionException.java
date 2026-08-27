package bubu.exception;

/**
 * Indicates that a task command does not contain a description.
 */
public class EmptyDescriptionException extends BubuException {

    /**
     * Creates an error message for a command with no description.
     *
     * @param commandName command that requires a description.
     */
    public EmptyDescriptionException(String commandName) {
        super("Meow! The description of a "
                + commandName
                + " task cannot be empty.");
    }
}
