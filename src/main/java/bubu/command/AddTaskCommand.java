package bubu.command;

import bubu.storage.Storage;
import bubu.task.Task;
import bubu.task.TaskList;
import bubu.ui.Ui;

/**
 * Base class for commands that create and save one task.
 */
public abstract class AddTaskCommand extends Command {
    /**
     * Adds a task, saves the updated list, and shows confirmation.
     *
     * @param task task to add.
     * @param tasks task list to update.
     * @param ui user interface for confirmation.
     * @param storage storage used to save the list.
     */
    protected void addTask(Task task, TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }
}
