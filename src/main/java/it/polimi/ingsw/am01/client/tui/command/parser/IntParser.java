package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

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
    public Completion complete(String partial) throws ParseException {
        if (partial.isEmpty()) {
            return Completion.nonCompletable("<" + this.name + ">");
        }

        if (pattern.matcher(partial).matches()) {
            return Completion.completable("");
        }

        throw new ParseException();
    }
}
