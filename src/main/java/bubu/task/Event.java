package bubu.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);
    private LocalDateTime start;
    private LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event start time entered by the user.
     *
     * @return event start time
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * Returns the event end time entered by the user.
     *
     * @return event end time
     */
    public LocalDateTime getEnd() {
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
