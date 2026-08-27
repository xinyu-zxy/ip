import task.Deadline;
import task.Event;
import task.Task;
import task.ToDo;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the current task list in a simple text file in the project data folder.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("data", "bubu.txt");

    /**
     * Replaces the saved task file with the current contents of the task list.
     *
     * @param tasks tasks to save
     */
    public void save(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(format(task));
        }

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
        String status = task.getStatus() ? "1" : "0";
        if (task instanceof ToDo) {
            return "T | " + status + " | " + task.getDescription();
        }
        if (task instanceof Deadline deadline) {
            return "D | " + status + " | " + deadline.getDescription()
                    + " | " + deadline.getDeadline();
        }
        if (task instanceof Event event) {
            return "E | " + status + " | " + event.getDescription()
                    + " | " + event.getStart() + " | " + event.getEnd();
        }
        throw new IllegalArgumentException("Unsupported task type: " + task.getClass().getName() + ". Meow!");
    }
}
