package bubu.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ToDoTest {

    @Test
    void markAsDone_uncompletedTask_marksAsDone() {
        ToDo todo = new ToDo("read book");
        assertFalse(todo.isDone());

        todo.markAsDone();
        assertTrue(todo.isDone());
    }

    @Test
    void toString_validTask_returnsFormattedString() {
        ToDo todo = new ToDo("read book");
        assertEquals("[T][ ] read book", todo.toString());

        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }
}
