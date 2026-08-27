package bubu.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToDoTest {

    @Test
    public void markAsDone_success() {
        ToDo todo = new ToDo("read book");
        assertFalse(todo.isDone());

        todo.markAsDone();
        assertTrue(todo.isDone());
    }

    @Test
    public void toString_correctFormat() {
        ToDo todo = new ToDo("read book");
        assertEquals("[T][ ] read book", todo.toString());

        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
    }
}