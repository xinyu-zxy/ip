package exception;

public class InvalidIndexException extends BubuException {

    public InvalidIndexException(int totalTasks) {
        super("Meow! The index you provided is invalid. Please provide a valid index between 1 and " + totalTasks + ".");
    }

    public InvalidIndexException(String message) {
        super("'" + message + "' is not a valid index meow. Please provide a valid index.");
    }
}
