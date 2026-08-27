package task;

public class Event extends Task {
    private String day;
    private String start;
    private String end;

    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event start time entered by the user.
     *
     * @return event start time
     */
    public String getStart() {
        return this.start;
    }

    /**
     * Returns the event end time entered by the user.
     *
     * @return event end time
     */
    public String getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return "[E]"
                + super.toString()
                + " (from: "
                + this.start
                + " to: "
                + this.end
                + ")";
    }
}
