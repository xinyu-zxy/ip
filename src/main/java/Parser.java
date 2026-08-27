import exception.BubuException;
import exception.MissingArgumentException;
import exception.EmptyDescriptionException;
import exception.UnknownCommandException;

public class Parser {

    /**
     * Extracts the command word after ignoring leading and repeated whitespace.
     *
     * @param input command entered by the user
     * @return the matching command type
     * @throws BubuException if the command is unknown
     */
    public static CommandType parse(String input) throws BubuException{
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new UnknownCommandException();
        }
        String command = trimmedInput.split("\\s+", 2)[0];

        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownCommandException(command);
        }
    }

    public static String parseArg(String input) throws BubuException{
        String[] args = input.trim().split("\\s+", 2);
        if (args.length < 2 || args[1].trim().isEmpty()) {
            throw new EmptyDescriptionException(args[0]);
        }

        return args[1].trim();
    }

    public static String[] parseDeadline(String input) throws BubuException{
        String args = Parser.parseArg(input);
        String[] parts = args.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new MissingArgumentException("deadline");
        }

        return new String[] {parts[0].trim(), parts[1].trim()};
    }

    public static String[] parseEvent(String input) throws BubuException{
        String args = Parser.parseArg(input);

        String[] commands = args.split(" /from ", 2);
        if (commands.length < 2 || commands[0].trim().isEmpty()) {
            throw new EmptyDescriptionException("event");
        }

        String[] timeLine = commands[1].split(" /to ", 2);
        if (timeLine.length < 2 || timeLine[0].trim().isEmpty() || timeLine[1].trim().isEmpty()) {
            throw new MissingArgumentException("event");
        }

        String[] output = new String[] {commands[0].trim(),
                timeLine[0].trim(),
                timeLine[1].trim()};
        return output;
    }
}
