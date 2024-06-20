package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

/**
 * A parser that matches white space.
 */
public class WhiteSpaceParser implements Parser {
    private final boolean required;

    /**
     * Creates a new whitespace parser.
     *
     * @param required f false, this parser will match zero or more whitespace, otherwise it will match one or more.
     */
    public WhiteSpaceParser(boolean required) {
        this.required = required;
    }

    @Override
    public Result parse(CommandContext context, String cmd) {
        int consumed = cmd.length() - cmd.stripLeading().length();
        if (this.required && consumed == 0) {
            return new Result(0, false);
        }

        return new Result(consumed, true);
    }

    @Override
    public Parser.Completion complete(String partial) throws ParseException {
        if (partial.isEmpty() && this.required) {
            return Completion.completable(" ");
        }

        if (partial.isBlank()) {
            return Completion.completable("");
        }

        throw new ParseException();
    }
}
