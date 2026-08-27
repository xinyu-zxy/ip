package bubu.exception;

/**
 * Indicates that the user entered a command word the chatbot does not recognize.
 */
public class UnknownCommandException extends BubuException {

    /**
     * Creates an error that names the unrecognized command.
     *
     * @param command unrecognized command word
     */
    public UnknownCommandException(String command) {
        super("I'm sorry, but I don't know what "
                + command
                + " means. ^.^");
    }

    /** Creates an error for empty or whitespace-only input. */
    public UnknownCommandException() {
        super("I'm sorry, but I don't know what that means. ^.^");
    }
}
