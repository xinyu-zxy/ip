package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private LocalDate start;
    private LocalDate end;

    public Event(String description, LocalDate start, LocalDate end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event start time entered by the user.
     *
     * @return event start time
     */
    public LocalDate getStart() {
        return this.start;
    }

    /**
     * Returns the event end time entered by the user.
     *
     * @return event end time
     */
    public LocalDate getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "[E]"
                + super.toString()
                + " (from: "
                + this.start.format(DISPLAY_FORMAT)
                + " to: "
                + this.end.format(DISPLAY_FORMAT)
                + ")";
    }
}
