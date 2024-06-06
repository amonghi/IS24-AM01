package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

public class LiteralParser implements Parser {
    private final String literal;

    public LiteralParser(String literal) {
        this.literal = literal;
    }

    private static int commonPrefixLength(String a, String b) {
        int i = 0;
        while (i < a.length() && i < b.length() && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return i;
    }

    @Override
    public Result parse(CommandContext context, String cmd) {
        int common = commonPrefixLength(this.literal, cmd);
        if (common == 0) {
            return new Result(0, false);
        }

        boolean fullMatch = common == this.literal.length();
        return new Result(common, fullMatch);
    }

    @Override
    public Completion complete(String partial) throws ParseException {
        if (literal.startsWith(partial)) {
            return Completion.completable(literal.substring(partial.length()));
        }

        throw new ParseException();
    }
}
