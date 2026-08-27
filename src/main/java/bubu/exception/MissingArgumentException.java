package bubu.exception;

/**
 * Indicates that a command is missing a required argument.
 */
public class MissingArgumentException extends BubuException {
    /**
     * Creates an error for a command with a missing argument.
     *
     * @param commandName command that requires the argument.
     */
    public MissingArgumentException(String commandName) {
        super("Meow! The argument for " + commandName + " command is missing. Meow!");
    }
}
