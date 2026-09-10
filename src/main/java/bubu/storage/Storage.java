package bubu.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import bubu.task.Deadline;
import bubu.task.Event;
import bubu.task.Task;
import bubu.task.ToDo;

/**
 * Saves the current task list in a simple text file in the project data folder.
 */
public class Storage {
    /** Location of the task save file, relative to the project root. */
    private static final Path FILE_PATH = Path.of("data", "bubu.txt");
    /** Format used to persist complete date-time values. */
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Replaces the saved task file with the current contents of the task list.
     *
     * @param tasks tasks to save.
     */
    public void saveTasks(List<Task> tasks) {
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
     * Converts one task to its file representation.
     *
     * @param task task to format.
     * @return a single line to represent the task details.
     */
    private String format(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof ToDo) {
            return "T | " + status + " | " + task.getDescription();
        }
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + deadline.getDescription()
                    + " | " + deadline.getDeadline().format(DATE_TIME_FORMAT);
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + event.getDescription()
                    + " | " + event.getStart().format(DATE_TIME_FORMAT)
                    + " | " + event.getEnd().format(DATE_TIME_FORMAT);
        }
        throw new IllegalArgumentException("Unsupported task type: " + task.getClass().getName() + ". Meow!");
    }

    /**
     * Loads tasks from the save file into memory.
     *
     * @return list of loaded tasks, or an empty list if file doesn't exist yet.
     */
    public List<Task> load() {
        List<Task> loadedTasks = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            return loadedTasks;
        }

        try {
            List<String> lines = Files.readAllLines(FILE_PATH);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    loadedTasks.add(parseTask(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading storage file.");
        }

        return loadedTasks;
    }

    /**
     * Parses a line from the save file into a Task object.
     *
     * @param line line from the save file.
     * @return Task object.
     * @throws IllegalArgumentException if the line format is invalid or the task type is unknown.
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        Task task = switch (parts[0]) {
            case "T" -> new ToDo(parts[2]);
            case "D" -> new Deadline(parts[2],
                    parseStoredDateTime(parts[3], LocalTime.of(23, 59)));
            case "E" -> new Event(parts[2], parseStoredDateTime(parts[3], LocalTime.MIDNIGHT),
                    parseStoredDateTime(parts[4], LocalTime.of(23, 59)));
            default -> throw new IllegalArgumentException("Unknown type: " + parts[0]);
        };

        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Parses a new date-time value or a date-only value written by an earlier version.
     *
     * @param value stored date or date-time text.
     * @param defaultTime time used by legacy date-only entries.
     * @return parsed date and time.
     */
    private LocalDateTime parseStoredDateTime(String value, LocalTime defaultTime) {
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMAT);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(value).atTime(defaultTime);
        }
    }
}
