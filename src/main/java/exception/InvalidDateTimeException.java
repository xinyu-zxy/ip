package exception;

public class InvalidDateTimeException extends BubuException {
    public InvalidDateTimeException() {
        super("Meow! Please use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2026-08-31 1800).");
    }

    public InvalidDateTimeException(String customMessage) {
        super(customMessage);
    }
}
