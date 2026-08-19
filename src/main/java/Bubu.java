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

        System.out.println(this.line);
        System.out.println(banner);
        System.out.println("What can I do for you? Meow!");
        System.out.println(this.line);

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
                default:
                    Task task = new Task(input);
                    tasks.add(task);
                    System.out.println("added: " + input);
                    System.out.println(line);
                    break;
            }
        }
        scanner.close();
    }

    public void commandList() {
        System.out.println("Meow! Here are the tasks in your list:");
        for (int i = 0; i < this.tasks.size(); i++) {
            System.out.println((i + 1) + ". " + this.tasks.get(i));
        }
        System.out.println(line);
    }

    public void commandMark(String input) {
        int index = Integer.parseInt(input.split(" ")[1]) - 1;
        this.tasks.get(index).markAsDone();
        System.out.println("Meow! I've marked this task as done:");
        System.out.println(this.tasks.get(index).toString());
        System.out.println(line);
    }

    public void commandUnmark(String input) {
        int index = Integer.parseInt(input.split(" ")[1]) - 1;
        this.tasks.get(index).markAsUndone();
        System.out.println("Meow! I've marked this task as not done yet:");
        System.out.println(this.tasks.get(index).toString());
        System.out.println(line);
    }
}
