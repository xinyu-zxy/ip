package exception;

public class MissingArgumentException extends BubuException {
    public MissingArgumentException(String message) {
        super("Meow! The argument for " + message + " command is missing. Meow!");
    }
}
