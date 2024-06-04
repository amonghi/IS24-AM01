package it.polimi.ingsw.am01.client.tui.command;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntParser implements Parser {
    private static final Pattern pattern = Pattern.compile("(\\d+)\\b");
    private final String name;

    public IntParser(String name) {
        this.name = name;
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.matches()) {
            String iString = matcher.group(1);
            int i = Integer.parseInt(iString);

            context.putArgument(this.name, i);
            return new Result(iString.length(), true);
        }

        throw new ParseException();
    }

    @Override
    public Optional<String> complete(String partial) {
        return Optional.of("<" + this.name + ">");
    }
}
