package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

/**
 * A parser that reads a word and then puts it in the {@link CommandContext}.
 * For this parser, a word is a sequence of characters that does not contain any whitespace.
 */
public class WordArgumentParser implements Parser {
    private final String name;

    /**
     * Creates a new {@link WordArgumentParser} that will parse a word
     * and put it in the {@link CommandContext} under the given name.
     *
     * @param name the name under which the word will be put in the {@link CommandContext}
     */
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
    public Completion complete(String partial) {
        if (partial.isEmpty()) {
            return Completion.nonCompletable("<" + this.name + ">");
        }

        return Completion.completable("");
    }
}
