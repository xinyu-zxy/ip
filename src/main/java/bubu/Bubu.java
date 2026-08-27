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

public class Bubu {
    private final Storage storage = new Storage();
    private final TaskList tasks = new TaskList(storage.load());
    private final Ui ui = new Ui();

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
