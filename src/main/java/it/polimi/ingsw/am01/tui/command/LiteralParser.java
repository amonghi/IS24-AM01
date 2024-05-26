package it.polimi.ingsw.am01.tui.command;

import java.util.Optional;

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
    public Optional<String> complete(String partial) {
        if (literal.startsWith(partial)) {
            return Optional.of(literal.substring(partial.length()));
        }

        return Optional.empty();
    }
}
