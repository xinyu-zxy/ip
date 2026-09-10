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
    /** Saves task-list changes to disk. */
    private final Storage storage = new Storage();
    /** Stores the tasks currently managed by the chatbot. */
    private final TaskList tasks = new TaskList(storage.load());
    /** Handles all console interaction. */
    private final Ui ui = new Ui();

    /**
     * Marks the task identified by a user-provided one-based index as complete.
     *
     * @param input full mark command.
     * @throws BubuException if the index is missing or invalid.
     */
    private void commandMark(String input) throws BubuException {
        assert input != null : "Input cannot be null";
        String[] output = input.trim().split("\\s+", 2);
        if (output.length < 2) {
            throw new MissingArgumentException("mark");
        }
        try {
            int index = Integer.parseInt(output[1]) - 1;
            if (!tasks.hasIndex(index)) {
                throw new InvalidIndexException(this.tasks.size());
            }
            this.tasks.get(index).markAsDone();
            this.storage.saveTasks(tasks.asList());
            ui.showTaskMarked(this.tasks.get(index), true);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(output[1]);
        }
    }

    /**
     * Marks the task identified by a user-provided one-based index as incomplete.
     *
     * @param input full unmark command.
     * @throws BubuException if the index is missing or invalid.
     */
    private void commandUnmark(String input) throws BubuException {
        assert input != null : "Input cannot be null";
        String[] output = input.trim().split("\\s+", 2);
        if (output.length < 2) {
            throw new MissingArgumentException("unmark");
        }
        try {
            int index = Integer.parseInt(output[1]) - 1;
            if (!tasks.hasIndex(index)) {
                throw new InvalidIndexException(this.tasks.size());
            }
            this.tasks.get(index).markAsUndone();
            this.storage.saveTasks(tasks.asList());
            ui.showTaskMarked(this.tasks.get(index), false);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(output[1]);
        }
    }
    /**
     * Deletes the task identified by a user-provided one-based index.
     *
     * @param input full delete command.
     * @throws BubuException if the index is missing or invalid.
     */
    private void commandDelete(String input) throws BubuException {
        assert input != null : "Input cannot be null";
        String[] output = input.trim().split("\\s+", 2);
        if (output.length < 2) {
            throw new MissingArgumentException("delete");
        }

        try {
            int index = Integer.parseInt(output[1]) - 1;
            if (!tasks.hasIndex(index)) {
                throw new InvalidIndexException(this.tasks.size());
            }

            Task task = this.tasks.remove(index);
            this.storage.saveTasks(tasks.asList());
            ui.showTaskDeleted(task, tasks.size());
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(output[1]);
        }
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input the user's chat message.
     * @return the chatbot's response.
     */
    public String getResponse(String input) {
        assert input != null : "Input cannot be null";
        ui.clearResponse();

        try {
            CommandType command = Parser.parseCommandType(input);
            assert command != null : "CommandType must not be null after parsing";
            switch (command) {
                case BYE:
                case LIST:
                case TODO:
                case DEADLINE:
                case EVENT:
                case FIND:
                    Command extractedCommand = Parser.createCommand(command, input);
                    assert extractedCommand != null : "Parser must create a valid Command object";
                    assert tasks != null : "TaskList must not be null before execution";
                    assert ui != null : "Ui must not be null before execution";
                    assert storage != null : "Storage must not be null before execution";
                    extractedCommand.execute(tasks, ui, storage);
                    break;
                case MARK:
                    this.commandMark(input);
                    break;
                case UNMARK:
                    this.commandUnmark(input);
                    break;
                case DELETE:
                    this.commandDelete(input);
                    break;
                default:
                    assert false : "Unhandled command type: " + command;
                    break;
            }
        } catch (BubuException e) {
            ui.showError(e.getMessage());
        }

        return ui.getResponse();
    }
}
