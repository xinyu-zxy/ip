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
     * Creates an executable command for command types already moved to the
     * command hierarchy.
     *
     * @param commandType parsed command type
     * @param input full user input
     * @return executable command object
     * @throws BubuException if the command arguments are invalid
     * @throws IllegalArgumentException if the type has not yet been extracted
     */
    public static Command createCommand(CommandType commandType, String input) throws BubuException {
        return switch (commandType) {
            case LIST -> new ListCommand();
            case BYE -> new ExitCommand();
            case TODO -> new TodoCommand(parseArg(input));
            case DEADLINE -> {
                String[] info = parseDeadline(input);
                yield new DeadlineCommand(info[0], parseDateTime(info[1], LocalTime.of(23, 59)));
            }
            case EVENT -> {
                String[] info = parseEvent(input);
                LocalDateTime start = parseDateTime(info[1], LocalTime.MIDNIGHT);
                LocalDateTime end = parseDateTime(info[2], LocalTime.of(23, 59));
                yield new EventCommand(info[0], start, end);
            }
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
