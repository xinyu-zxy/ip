package exception;

public class InvalidDateTimeException extends BubuException {
    public InvalidDateTimeException() {
        super("Meow! Please use the date format yyyy-MM-dd (e.g., 2026-08-31).");
    }

    public InvalidDateTimeException(String customMessage) {
        super(customMessage);
    }
}
