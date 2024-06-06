package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;

public class JoinCommand extends TuiCommand {

    public JoinCommand(TuiView view) {
        super(view);
    }

    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("join")
                .thenWhitespace()
                .then(new IntParser("gameId"))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        int gameId = ctx.getInt("gameId");
        getView().joinGame(gameId);
    }
}
