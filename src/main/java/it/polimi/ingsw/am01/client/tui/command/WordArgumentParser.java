package it.polimi.ingsw.am01.client.tui.command;

import it.polimi.ingsw.am01.client.tui.command.parser.ParseException;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;

import java.util.Optional;

public class WordArgumentParser implements Parser {
    private final String name;

    public WordArgumentParser(String name) {
        this.name = name;
    }

    private static int firstWhitespace(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                return i;
            }
        }

        return s.length();
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        int i = firstWhitespace(cmd);
        if (i == 0) {
            throw new ParseException();
        }

        String word = cmd.substring(0, i);
        context.putArgument(this.name, word);

        return new Result(i, true);
    }

    @Override
    public Optional<String> complete(String partial) {
        return Optional.of("<" + this.name + ">");
    }
}
