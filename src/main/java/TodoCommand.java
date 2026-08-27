import Storage.Storage;
import task.ToDo;

/**
 * Adds a to-do task.
 */
public class TodoCommand extends AddTaskCommand {
    private final String description;

    /** Creates a to-do command for the supplied description. */
    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        addTask(new ToDo(description), tasks, ui, storage);
    }
}
