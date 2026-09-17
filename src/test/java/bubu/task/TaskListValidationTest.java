package bubu.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import bubu.exception.DuplicateTaskException;

/** Tests validation performed when tasks are added to a task list. */
class TaskListValidationTest {
    @Test
    void add_duplicateTask_throwsAndKeepsOriginalTask() throws DuplicateTaskException {
        TaskList taskList = new TaskList();
        taskList.add(new Deadline("submit report", LocalDateTime.of(2026, 9, 18, 18, 0)));

        assertThrows(DuplicateTaskException.class,
                () -> taskList.add(new Deadline("submit report", LocalDateTime.of(2026, 9, 18, 18, 0))));
        assertEquals(1, taskList.size());
    }
}
