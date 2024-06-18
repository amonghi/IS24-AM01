package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser that reads an integer and then puts it in the {@link CommandContext}.
 */
public class IntParser implements Parser {
    private static final Pattern pattern = Pattern.compile("(-?\\d+)\\b");
    private final String name;

    /**
     * Creates a new {@link IntParser} that will parse an integer
     * and put it in the {@link CommandContext} under the given name.
     *
     * @param name the name under which the integer will be put in the {@link CommandContext}
     */
    public IntParser(String name) {
        this.name = name;
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.find() && matcher.start() == 0) {
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

        Matcher matcher = pattern.matcher(partial);
        if (matcher.find() && matcher.start() == 0) {
            return Completion.completable("");
        }

        throw new ParseException();
    }
}
