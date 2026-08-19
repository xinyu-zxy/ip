public class Parser {
    public static CommandType parse(String input) {
        String command = input.split(" ", 2)[0];
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.UNKNOWN;
        }
    }
}
