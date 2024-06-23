package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

/**
 * A parser that greedily matches the entire input and puts it in the {@link CommandContext} under the given name.
 */
public class GreedyTextArgumentParser implements Parser {
    private final String name;

    /**
     * Creates a new {@link GreedyTextArgumentParser} that will parse the entire input
     * and put it in the {@link CommandContext} under the given name.
     *
     * @param name the name under which the text will be put in the {@link CommandContext}
     */
    public GreedyTextArgumentParser(String name) {
        this.name = name;
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        if (cmd.isEmpty()) {
            throw new ParseException();
        }

        context.putArgument(this.name, cmd);
        return new Result(cmd.length(), true);
    }

    @Override
    public Completion complete(String partial) {
        if (partial.isEmpty()) {
            return Completion.nonCompletable("<" + this.name + ">");
        }

        return Completion.completable("");
    }
}
