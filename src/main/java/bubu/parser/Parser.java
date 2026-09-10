package bubu.parser;

import java.time.LocalDateTime;
import java.time.LocalTime;

import bubu.command.Command;
import bubu.command.CommandType;
import bubu.command.DeadlineCommand;
import bubu.command.EventCommand;
import bubu.command.ExitCommand;
import bubu.command.FindCommand;
import bubu.command.ListCommand;
import bubu.command.TodoCommand;
import bubu.exception.BubuException;
import bubu.exception.EmptyDescriptionException;
import bubu.exception.InvalidDateTimeException;
import bubu.exception.MissingArgumentException;
import bubu.exception.UnknownCommandException;
import bubu.util.DateTimeParser;

/**
 * Converts user input into command types, command objects, and date-time values.
 */
public class Parser {
    private static final String REGEX_WHITESPACE = "\\s+";
    private static final String DELIMITER_BY = " /by ";
    private static final String DELIMITER_FROM = " /from ";
    private static final String DELIMITER_TO = " /to ";
    private static final String COMMAND_NAME_DEADLINE = "deadline";
    private static final String COMMAND_NAME_EVENT = "event";
    private static final int ARGUMENT_SPLIT_LIMIT = 2;
    private static final int INDEX_COMMAND_WORD = 0;
    private static final int INDEX_ARGUMENT_BODY = 1;

    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(23, 59);
    private static final LocalTime DEFAULT_START_TIME = LocalTime.MIDNIGHT;

    /**
     * Extracts the command word after ignoring leading and repeated whitespace.
     *
     * @param input Command entered by the user.
     * @return The matching command type.
     * @throws BubuException If the command is unknown or input is empty.
     */
    public static CommandType parseCommandType(String input) throws BubuException {
        assert input != null : "input string cannot be null";

        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new UnknownCommandException();
        }

        String commandWord = trimmedInput.split(REGEX_WHITESPACE, ARGUMENT_SPLIT_LIMIT)[INDEX_COMMAND_WORD];

        try {
            return CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownCommandException(commandWord);
        }
    }

    /**
     * Creates an executable command object from the parsed command type.
     *
     * @param commandType Parsed command type.
     * @param input Full user input.
     * @return Executable command object.
     * @throws BubuException If command arguments are invalid.
     */
    public static Command createCommand(CommandType commandType, String input) throws BubuException {
        assert commandType != null : "CommandType cannot be null";
        assert input != null : "Input string cannot be null";

        return switch (commandType) {
            case LIST -> new ListCommand();
            case BYE -> new ExitCommand();
            case TODO -> new TodoCommand(parseArg(input));
            case FIND -> new FindCommand(parseArg(input));
            case DEADLINE -> createDeadlineCommand(input);
            case EVENT -> createEventCommand(input);
            default -> throw new IllegalArgumentException(
                    "Command type has not yet been extracted: " + commandType);
        };
    }

    /**
     * Extracts the text following a command word.
     *
     * @param input Full command entered by the user.
     * @return Trimmed command argument.
     * @throws BubuException If the argument is missing.
     */
    public static String parseArg(String input) throws BubuException {
        String[] args = input.trim().split(REGEX_WHITESPACE, ARGUMENT_SPLIT_LIMIT);
        if (args.length < ARGUMENT_SPLIT_LIMIT || args[INDEX_ARGUMENT_BODY].trim().isEmpty()) {
            throw new EmptyDescriptionException(args[INDEX_COMMAND_WORD]);
        }

        return args[INDEX_ARGUMENT_BODY].trim();
    }

    /**
     * Extracts a deadline description and its {@code /by} value.
     *
     * @param input Full deadline command.
     * @return Description at index 0 and date-time text at index 1.
     * @throws BubuException If required deadline arguments are missing.
     */
    public static String[] parseDeadline(String input) throws BubuException {
        String args = parseArg(input);
        String[] parts = args.split(DELIMITER_BY, ARGUMENT_SPLIT_LIMIT);

        if (hasMissingParts(parts)) {
            throw new MissingArgumentException(COMMAND_NAME_DEADLINE);
        }

        String[] result = new String[] {parts[0].trim(), parts[1].trim()};
        assert result.length == 2 : "Parsed deadline must yield description and date/time";
        return result;
    }

    /**
     * Extracts an event description plus its {@code /from} and {@code /to} values.
     *
     * @param input Full event command.
     * @return Description, start date-time text, and end date-time text.
     * @throws BubuException If required event arguments are missing.
     */
    public static String[] parseEvent(String input) throws BubuException {
        String args = parseArg(input);

        String[] parts = args.split(DELIMITER_FROM, ARGUMENT_SPLIT_LIMIT);
        if (parts.length < ARGUMENT_SPLIT_LIMIT || parts[0].trim().isEmpty()) {
            throw new EmptyDescriptionException(COMMAND_NAME_EVENT);
        }

        String[] timeLine = parts[1].split(DELIMITER_TO, ARGUMENT_SPLIT_LIMIT);
        if (hasMissingParts(timeLine)) {
            throw new MissingArgumentException(COMMAND_NAME_EVENT);
        }

        return new String[] {parts[0].trim(), timeLine[0].trim(), timeLine[1].trim()};
    }

    /**
     * Parses a date-time or date-only value into a {@code LocalDateTime}.
     *
     * @param input Date in yyyy-MM-dd or date-time in yyyy-MM-dd HHmm format.
     * @param defaultTime Time to use when the input contains only a date.
     * @return The parsed date and time.
     * @throws InvalidDateTimeException If neither accepted format matches.
     */
    public static LocalDateTime parseDateTime(String input, LocalTime defaultTime)
            throws InvalidDateTimeException {
        return DateTimeParser.parse(input, defaultTime);
    }

    /**
     * Creates a deadline command from the user input.
     *
     * @param input The user input string.
     * @return The created deadline command.
     * @throws BubuException If the input is invalid.
     */
    private static DeadlineCommand createDeadlineCommand(String input) throws BubuException {
        String[] info = parseDeadline(input);
        LocalDateTime dueTime = parseDateTime(info[1], DEFAULT_END_TIME);
        return new DeadlineCommand(info[0], dueTime);
    }

    /**
     * Creates an event command from the user input.
     *
     * @param input The user input string.
     * @return The created event command.
     * @throws BubuException If the input is invalid.
     */
    private static EventCommand createEventCommand(String input) throws BubuException {
        String[] info = parseEvent(input);
        LocalDateTime start = parseDateTime(info[1], DEFAULT_START_TIME);
        LocalDateTime end = parseDateTime(info[2], DEFAULT_END_TIME);
        return new EventCommand(info[0], start, end);
    }

    /**
     * Checks if the provided parts array has missing or empty elements.
     *
     * @param parts The array of strings to check.
     * @return True if any part is missing or empty, false otherwise.
     */
    private static boolean hasMissingParts(String[] parts) {
        return parts.length < ARGUMENT_SPLIT_LIMIT
                || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty();
    }
}
