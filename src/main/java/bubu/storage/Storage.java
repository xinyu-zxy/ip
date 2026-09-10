package bubu.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import bubu.task.Deadline;
import bubu.task.Event;
import bubu.task.Task;
import bubu.task.ToDo;
import bubu.util.DateTimeParser;

/**
 * Saves the current task list in a simple text file in the project data folder.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "bubu.txt");
    private static final String DELIMITER_WRITE = " | ";
    private static final String REGEX_DELIMITER_READ = " \\| ";

    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";

    private static final String STATUS_DONE = "1";
    private static final String STATUS_NOT_DONE = "0";

    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(23, 59);
    private static final LocalTime DEFAULT_START_TIME = LocalTime.MIDNIGHT;

    /**
     * Replaces the saved task file with the current contents of the task list.
     *
     * @param tasks Tasks to save.
     */
    public void saveTasks(List<Task> tasks) {
        assert tasks != null : "Task list should not be null when saving to file.";
        List<String> lines = tasks.stream()
                .map(this::format)
                .toList();
        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.write(FILE_PATH, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to save tasks to " + FILE_PATH, e);
        }
    }

    /**
     * Converts one task to its serialized file representation.
     *
     * @param task Task to format.
     * @return Single line representing task details.
     */
    private String format(Task task) {
        String status = task.isDone() ? STATUS_DONE : STATUS_NOT_DONE;

        if (task instanceof ToDo) {
            return String.join(DELIMITER_WRITE, TYPE_TODO, status, task.getDescription());
        }
        if (task instanceof Deadline deadline) {
            return String.join(DELIMITER_WRITE,
                    TYPE_DEADLINE,
                    status,
                    deadline.getDescription(),
                    DateTimeParser.format(deadline.getDeadline()));
        }
        if (task instanceof Event event) {
            return String.join(DELIMITER_WRITE,
                    TYPE_EVENT,
                    status,
                    event.getDescription(),
                    DateTimeParser.format(event.getStart()),
                    DateTimeParser.format(event.getEnd()));
        }

        throw new IllegalArgumentException("Unsupported task type: " + task.getClass().getName() + ". Meow!");
    }

    /**
     * Loads tasks from the save file into memory.
     *
     * @return List of loaded tasks, or an empty list if the file doesn't exist yet.
     */
    public List<Task> load() {
        List<Task> loadedTasks = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            return loadedTasks;
        }

        try {
            List<String> lines = Files.readAllLines(FILE_PATH);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                loadedTasks.add(parseTask(line));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read tasks from " + FILE_PATH, e);
        }

        assert loadedTasks != null : "Loaded tasks list should not be null";
        return loadedTasks;
    }

    /**
     * Parses a line from the save file into a Task object.
     *
     * @param line Line from the save file.
     * @return Parsed task object.
     * @throws IllegalArgumentException If the line format is invalid or task type is unknown.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(REGEX_DELIMITER_READ);
        String taskType = parts[0];

        Task task = switch (taskType) {
            case TYPE_TODO -> new ToDo(parts[2]);
            case TYPE_DEADLINE -> new Deadline(parts[2],
                    DateTimeParser.parseStored(parts[3], DEFAULT_END_TIME));
            case TYPE_EVENT -> new Event(parts[2],
                    DateTimeParser.parseStored(parts[3], DEFAULT_START_TIME),
                    DateTimeParser.parseStored(parts[4], DEFAULT_END_TIME));
            default -> throw new IllegalArgumentException("Unknown type: " + taskType);
        };

        if (parts[1].equals(STATUS_DONE)) {
            task.markAsDone();
        }

        return task;
    }

}
