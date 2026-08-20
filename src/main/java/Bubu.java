import java.util.ArrayList;
import java.util.Scanner;

public class Bubu {
    private ArrayList<Task> tasks = new ArrayList<>();
    private final String line = "___________________________________________________________";
    private final String meow = " meow~";

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String banner = " /\\___/\\ \n"
                + "(  o.o  )  Hello! I'm BUBU!\n";

        System.out.println(line);
        System.out.println(banner);
        System.out.println("What can I do for you? Meow!");
        System.out.println(line);

        boolean isEnd = false;
        while (!isEnd) {
            String input = scanner.nextLine();
            System.out.println(line);

            CommandType command = Parser.parse(input);
            switch(command) {
                case BYE:
                    System.out.println("Bye. Hope to see you again soon! Meow!");
                    System.out.println(line);
                    isEnd = true;
                    break;
                case LIST:
                    this.commandList();
                    break;
                case MARK:
                    this.commandMark(input);
                    break;
                case UNMARK:
                    this.commandUnmark(input);
                    break;
                case TODO:
                    this.commandToDo(input);
                    break;
                case DEADLINE:
                    this.commandDeadline(input);
                    break;
                case EVENT:
                    this.commandEvent(input);
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }

    private void commandList() {
        System.out.println("Meow! Here are the tasks in your list:");
        for (int i = 0; i < this.tasks.size(); i++) {
            System.out.println((i + 1) + ". " + this.tasks.get(i));
        }
        System.out.println(line);
    }

    private void commandMark(String input) {
        int index = Integer.parseInt(input.split(" ")[1]) - 1;
        this.tasks.get(index).markAsDone();
        System.out.println("Meow! I've marked this task as done:");
        System.out.println(this.tasks.get(index).toString());
        System.out.println(line);
    }

    private void commandUnmark(String input) {
        int index = Integer.parseInt(input.split(" ")[1]) - 1;
        this.tasks.get(index).markAsUndone();
        System.out.println("Meow! I've marked this task as not done yet:");
        System.out.println(this.tasks.get(index).toString());
        System.out.println(line);
    }

    private void commandToDo(String input) {
        String description = Parser.parseArg(input);
        ToDo task = new ToDo(description);
        this.addTask(task);
    }

    private void commandDeadline(String input) {
        String[] info = Parser.parseDeadline(input);
        Deadline deadline = new Deadline(info[0].trim(), info[1].trim());
        this.addTask(deadline);
    }

    private void commandEvent(String input) {
        String[] info = Parser.parseEvent(input);
        Event event = new Event(info[0].trim(), info[1].trim(), info[2].trim());
        this.addTask(event);
    }

    private void addTask(Task task) {
        this.tasks.add(task);
        System.out.println("Got it meow. I've added this task:");
        System.out.println("  " + task);

        if (this.tasks.size() < 2) {
            System.out.println(String.format("Now you have %d task in the list. Meow!", this.tasks.size()));
        } else {
            System.out.println(String.format("Now you have %d tasks in the list. Meow!", this.tasks.size()));
        }
        System.out.println(line);
    }
}
