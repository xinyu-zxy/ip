package exception;

public class EmptyDescriptionException extends BubuException {

    public EmptyDescriptionException(String message) {
        super("Meow! The description of a "
                + message
                + " task cannot be empty.");
    }
}
