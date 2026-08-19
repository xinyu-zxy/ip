public class parser {
    public static CommandType parse(String input) {
        String command = input.split(" ", 2)[0];
        return CommandType.valueOf(command.toUpperCase());
    }
}
