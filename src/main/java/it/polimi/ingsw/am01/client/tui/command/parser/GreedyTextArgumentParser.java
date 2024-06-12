package it.polimi.ingsw.am01.client.tui.command.parser;

import it.polimi.ingsw.am01.client.tui.command.CommandContext;

public class GreedyTextArgumentParser implements Parser {
    private final String name;

    public GreedyTextArgumentParser(String name) {
        this.name = name;
    }

    @Override
    public Result parse(CommandContext context, String cmd) throws ParseException {
        if (cmd.isBlank()) {
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
