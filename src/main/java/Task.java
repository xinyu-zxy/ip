public class Task {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    //getter to obtain the task description
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
