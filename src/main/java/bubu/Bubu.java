package bubu;

import bubu.command.Command;
import bubu.command.CommandType;
import bubu.exception.BubuException;
import bubu.exception.InvalidIndexException;
import bubu.exception.MissingArgumentException;
import bubu.parser.Parser;
import bubu.storage.Storage;
import bubu.task.Task;
import bubu.task.TaskList;
import bubu.ui.Ui;

/**
 * Coordinates command processing and the chatbot's collaborators.
 */
public class Bubu {
    private static final String REGEX_WHITESPACE = "\\s+";
    private static final int ONE_BASED_INDEX_OFFSET = 1;

    private static final String COMMAND_NAME_MARK = "mark";
    private static final String COMMAND_NAME_UNMARK = "unmark";
    private static final String COMMAND_NAME_DELETE = "delete";

    /** Saves task-list changes to disk. */
    private final Storage storage = new Storage();
    /** Stores the tasks currently managed by the chatbot. */
    private final TaskList tasks = new TaskList(storage.load());
    /** Handles all console interaction. */
    private final Ui ui = new Ui();

    /**
     * Marks the task identified by a user-provided one-based index as complete.
     *
     * @param input Full mark command.
     * @throws BubuException If the index is missing or invalid.
     */
    private void commandMark(String input) throws BubuException {
        assert input != null : "Input cannot be null";
        int index = extractValidIndex(input, COMMAND_NAME_MARK);
        Task task = this.tasks.get(index);
        task.markAsDone();
        this.storage.saveTasks(tasks.asList());
        ui.showTaskMarked(task, true);
    }

    /**
     * Marks the task identified by a user-provided one-based index as incomplete.
     *
     * @param input Full unmark command.
     * @throws BubuException If the index is missing or invalid.
     */
    private void commandUnmark(String input) throws BubuException {
        assert input != null : "Input cannot be null";
        int index = extractValidIndex(input, COMMAND_NAME_UNMARK);
        Task task = this.tasks.get(index);
        task.markAsUndone();
        this.storage.saveTasks(tasks.asList());
        ui.showTaskMarked(task, false);
    }

    /**
     * Deletes the task identified by a user-provided one-based index.
     *
     * @param input Full delete command.
     * @throws BubuException If the index is missing or invalid.
     */
    private void commandDelete(String input) throws BubuException {
        assert input != null : "Input cannot be null";
        int index = extractValidIndex(input, COMMAND_NAME_DELETE);
        Task removedTask = this.tasks.remove(index);
        this.storage.saveTasks(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Extracts and validates a zero-based task index from user input.
     *
     * @param input Full user command.
     * @param commandName Name of the command requesting the index.
     * @return Validated zero-based task index.
     * @throws BubuException If the argument is missing, non-numeric, or out of range.
     */
    private int extractValidIndex(String input, String commandName) throws BubuException {
        assert input != null : "Input cannot be null";
        assert commandName != null : "Command name cannot be null";
        String[] parts = input.trim().split(REGEX_WHITESPACE, 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new MissingArgumentException(commandName);
        }

        String rawIndex = parts[1].trim();
        try {
            int index = Integer.parseInt(rawIndex) - ONE_BASED_INDEX_OFFSET;
            if (!tasks.hasIndex(index)) {
                throw new InvalidIndexException(tasks.size());
            }
            return index;
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(rawIndex);
        }
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user's chat message.
     * @return The chatbot's response.
     */
    public String getResponse(String input) {
        assert input != null : "Input cannot be null";
        ui.clearResponse();

        try {
            CommandType commandType = Parser.parseCommandType(input);
            assert commandType != null : "CommandType must not be null after parsing";
            executeCommand(commandType, input);
        } catch (BubuException e) {
            ui.showError(e.getMessage());
        }

        return ui.getResponse();
    }

    /**
     * Dispatches command execution based on the parsed command type.
     */
    private void executeCommand(CommandType commandType, String input) throws BubuException {
        assert commandType != null : "CommandType cannot be null";
        assert input != null : "Input cannot be null";
        switch (commandType) {
            case BYE:
            case LIST:
            case TODO:
            case DEADLINE:
            case EVENT:
            case FIND:
                Command command = Parser.createCommand(commandType, input);
                assert command != null : "Parser must create a valid Command object";
                assert tasks != null : "TaskList must not be null before execution";
                assert ui != null : "Ui must not be null before execution";
                assert storage != null : "Storage must not be null before execution";
                command.execute(tasks, ui, storage);
                break;
            case MARK:
                commandMark(input);
                break;
            case UNMARK:
                commandUnmark(input);
                break;
            case DELETE:
                commandDelete(input);
                break;
            default:
                assert false : "Unhandled command type: " + commandType;
                throw new IllegalArgumentException("Unhandled command type: " + commandType);
        }
    }
}
