package exception;

public class UnknownCommandException extends BubuException {

    public UnknownCommandException(String command) {
        super("I'm sorry, but I don't know what "
                + command
                + " means. ^.^");
    }

    public UnknownCommandException() {
        super("I'm sorry, but I don't know what that means. ^.^");
    }
}
