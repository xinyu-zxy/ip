public class Parser {

    /* This method parse help to extract out the first word in the input
    that is being separate by " " and match with CommandType so we
    can perform the correct command.
     */
    public static CommandType parse(String input) {
        String command = input.split(" ", 2)[0];
        try {
            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommandType.UNKNOWN;
        }
    }

    public static String parseArg(String input) {
        String[] inputs = input.trim().split(" ", 2);
        return inputs.length > 1
                ? inputs[1].trim()
                : "";
    }

    public static String[] parseDeadline(String input) {
        String args = Parser.parseArg(input);
        return args.split(" /by ");
    }

    public static String[] parseEvent(String input) {
        String args = Parser.parseArg(input);
        String[] commands = args.split(" /from ", 2);
        String[] timeLine = commands[1].split(" /to ", 2);
        String[] output = new String[] {commands[0].trim(),
                timeLine[0].trim(),
                timeLine[1].trim()};
        return output;
    }
}
