import Storage.Storage;
import task.Task;
import task.ToDo;
import task.Deadline;
import task.Event;

import exception.BubuException;
import exception.MissingArgumentException;
import exception.InvalidIndexException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Bubu {
    private final Storage storage = new Storage();
    private ArrayList<Task> tasks = new ArrayList<>(storage.load());
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
                        ui.showGoodbye();
                        isEnd = true;
                        break;
                    case LIST:
                        this.commandList();
                        break;
                    case MARK:
                        this.commandMark(input);
                        break;
                    case UNMARK:
                        this.commandUnmark(input);
                        break;
                    case TODO:
                        this.commandToDo(input);
                        break;
                    case DEADLINE:
                        this.commandDeadline(input);
                        break;
                    case EVENT:
                        this.commandEvent(input);
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

    private void commandList() {
        ui.showTaskList(tasks);
    }

    private void commandMark(String input) throws BubuException {
        String[] output = input.trim().split("\\s+", 2);
        if (output.length < 2) {
            throw new MissingArgumentException("mark");
        }
        try {
            int index = Integer.parseInt(output[1]) - 1;
            if (index < 0 || index >= this.tasks.size()) {
                throw new InvalidIndexException(this.tasks.size());
            }
            this.tasks.get(index).markAsDone();
            this.storage.save(this.tasks);
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
            if (index < 0 || index >= this.tasks.size()) {
                throw new InvalidIndexException(this.tasks.size());
            }
            this.tasks.get(index).markAsUndone();
            this.storage.save(this.tasks);
            ui.showTaskMarked(this.tasks.get(index), false);
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(output[1]);
        }
    }


    private void commandToDo(String input) throws BubuException {
        String description = Parser.parseArg(input);
        ToDo task = new ToDo(description);
        this.addTask(task);
    }

    private void commandDeadline(String input) throws BubuException {
        String[] info = Parser.parseDeadline(input);
        LocalDateTime deadlineDateTime = Parser.parseDateTime(info[1], LocalTime.of(23, 59));
        Deadline deadline = new Deadline(info[0].trim(), deadlineDateTime);
        this.addTask(deadline);
    }

    private void commandEvent(String input) throws BubuException {
        String[] info = Parser.parseEvent(input);
        LocalDateTime start = Parser.parseDateTime(info[1], LocalTime.MIDNIGHT);
        LocalDateTime end = Parser.parseDateTime(info[2], LocalTime.of(23, 59));
        Event event = new Event(info[0].trim(), start, end);
        this.addTask(event);
    }

    private void addTask(Task task) {
        this.tasks.add(task);
        this.storage.save(this.tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private void commandDelete(String input) throws BubuException {
        String[] output = input.trim().split("\\s+", 2);
        if (output.length < 2) {
            throw new MissingArgumentException("delete");
        }

        try {
            int index = Integer.parseInt(output[1]) - 1;
            if (index < 0 || index >= this.tasks.size()) {
                throw new InvalidIndexException(this.tasks.size());
            }

            Task task = this.tasks.remove(index);
            this.storage.save(this.tasks);
            ui.showTaskDeleted(task, tasks.size());
        } catch (NumberFormatException e) {
            throw new InvalidIndexException(output[1]);
        }
    }
}
