import exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Parser {
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);

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

    /**
     * Creates a command object for the read-only and exit command types moved
     * to the command hierarchy in this iteration.
     *
     * @param commandType parsed command type
     * @return executable command object
     * @throws IllegalArgumentException if the type has not yet been extracted
     */
    public static Command createSimpleCommand(CommandType commandType) {
        return switch (commandType) {
            case LIST -> new ListCommand();
            case BYE -> new ExitCommand();
            default -> throw new IllegalArgumentException(
                    "Command type has not yet been extracted: " + commandType);
        };
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

    /**
     * Parses a date-time or date-only value. A date-only value receives the
     * supplied default time.
     *
     * @param input date in yyyy-MM-dd or date-time in yyyy-MM-dd HHmm format
     * @param defaultTime time to use when the input contains only a date
     * @return the parsed date and time
     * @throws InvalidDateTimeException if neither accepted format matches
     */
    public static LocalDateTime parseDateTime(String input, LocalTime defaultTime) throws InvalidDateTimeException {
        try {
            return LocalDateTime.parse(input, DATE_TIME_FORMAT);
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDate.parse(input).atTime(defaultTime);
            } catch (DateTimeParseException e) {
                throw new InvalidDateTimeException();
            }
        }
    }
}
