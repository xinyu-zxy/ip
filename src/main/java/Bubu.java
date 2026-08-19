import java.util.Scanner;

public class Bubu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = "___________________________________________________________";
        String meow = " meow~";
        String banner = " /\\___/\\ \n"
                + "(  o.o  )  Hello! I'm BUBU!\n";

        System.out.println(line);
        System.out.println(banner);
        System.out.println("What can I do for you? Meow!");
        System.out.println(line);

        while (true) {
            String input = scanner.nextLine();
            System.out.println(line);

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon! Meow!");
                System.out.println(line);
                break;
            }

            System.out.println(input + meow);
            System.out.println(line);
        }

        scanner.close();
    }
}
