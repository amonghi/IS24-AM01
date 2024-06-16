package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntParser implements Parser {
    private static final Pattern pattern = Pattern.compile("(-?\\d+)\\b");
    private final String name;
    private final IntPredicate validator;

    public IntParser(String name) {
        this(name, value -> true);
    }

    public IntParser(String name, IntPredicate validator) {
        this.name = name;
        this.validator = validator;
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.find() && matcher.start() == 0) {
            String iString = matcher.group(1);
            int i = Integer.parseInt(iString);

            if (!validator.test(i)) {
                throw new ParseException();
            }

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

        Matcher matcher = pattern.matcher(partial);
        if (matcher.find() && matcher.start() == 0) {
            return Completion.completable("");
        }

        throw new ParseException();
    }
}
