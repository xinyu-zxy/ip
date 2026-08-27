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
     * Starts and runs the main chatbot loop.
     * Continuously accepts, parses, and executes user commands until the exit command is received.
     */
    public void run() {
        ui.showWelcome();

        boolean isEnd = false;
        while (!isEnd) {
            String input = ui.readCommand();
            ui.showLine();

            try {
                CommandType command = Parser.parse(input);
                switch(command) {
                    case BYE:
                    case LIST:
                    case TODO:
                    case DEADLINE:
                    case EVENT:
                    case FIND:
                        Command extractedCommand = Parser.createCommand(command, input);
                        extractedCommand.execute(tasks, ui, storage);
                        isEnd = extractedCommand.isExit();
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
                        break;
                }
            } catch (BubuException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Marks the task identified by a user-provided one-based index as complete.
     *
     * @param input full mark command.
     * @throws BubuException if the index is missing or invalid.
     */
    private void commandMark(String input) throws BubuException {
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
            this.storage.save(tasks.asList());
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
            this.storage.save(tasks.asList());
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
            this.storage.save(tasks.asList());
            ui.showTaskDeleted(task, tasks.size());
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(output[1]);
        }
    }
}
