package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

public class WhiteSpaceParser implements Parser {
    private final boolean required;

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
        if (partial.isEmpty()) {
            return Completion.completable(" ");
        }

        if (partial.isBlank()) {
            return Completion.completable("");
        }

        throw new ParseException();
    }
}
