public class Task {
    private String description;
    boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    //getter to obtain the task description
    public String getDescription() {
        return this.description;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public boolean getStatus() {
        return isDone;
    }

    @Override
    public String toString() {
        return (this.isDone ? "[X] " : "[ ] ")
                + this.description;
    }
}
