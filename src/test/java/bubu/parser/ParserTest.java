package bubu.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import bubu.command.CommandType;
import bubu.exception.BubuException;
import bubu.exception.DuplicateArgumentException;
import bubu.exception.InvalidDateTimeException;
import bubu.exception.InvalidDateRangeException;
import bubu.exception.MissingArgumentException;
import bubu.exception.UnexpectedArgumentException;

public class ParserTest {

    @Test
    void parseDeadline_validAndInvalidInput_returnsExpectedOrThrows() throws BubuException {
        // Test normal parsing with description and date
        String[] result = Parser.parseDeadline("deadline return book /by 2026-08-30 1800");
        assertArrayEquals(new String[]{"return book", "2026-08-30 1800"}, result);

        // Test missing argument /by
        assertThrows(MissingArgumentException.class, () -> Parser.parseDeadline("deadline return book"));

        String[] flexibleSpacing = Parser.parseDeadline("deadline return book   /by   2026-08-30 1800");
        assertArrayEquals(new String[]{"return book", "2026-08-30 1800"}, flexibleSpacing);
        assertThrows(DuplicateArgumentException.class,
                () -> Parser.parseDeadline("deadline return book /by 2026-08-30 /by 1800"));
    }

    @Test
    void parseDateTime_validAndInvalidFormats_returnsExpectedOrThrows() throws Exception {
        LocalDateTime withTime = Parser.parseDateTime("2026-08-30 1800", LocalTime.of(23, 59));
        assertEquals(LocalDateTime.of(2026, 8, 30, 18, 0), withTime);

        LocalDateTime dateOnly = Parser.parseDateTime("2026-08-30", LocalTime.of(23, 59));
        assertEquals(LocalDateTime.of(2026, 8, 30, 23, 59), dateOnly);

        assertThrows(InvalidDateTimeException.class, () -> Parser.parseDateTime("invalid-date", LocalTime.MIDNIGHT));
    }

    @Test
    void parseEvent_endNotAfterStart_throws() {
        assertThrows(InvalidDateRangeException.class,
                () -> Parser.createCommand(
                        CommandType.EVENT,
                        "event meeting /from 2026-08-30 1800 /to 2026-08-30 1800"));
    }

    @Test
    void createCommand_commandWithoutArguments_throwsForExtraInput() {
        assertThrows(UnexpectedArgumentException.class,
                () -> Parser.createCommand(CommandType.LIST, "list extra"));
    }
}
