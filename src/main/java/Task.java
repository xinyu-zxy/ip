public class Task {
    private String name;
    boolean isDone;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    //getter to obtain the task description
    public String getName() {
        return this.name;
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
        return (this.isDone ? "[X] " : "[ ] ") + this.name;
    }
}
