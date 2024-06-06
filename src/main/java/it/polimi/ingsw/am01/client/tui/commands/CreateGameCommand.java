package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;

public class CreateGameCommand extends TuiCommand {
    public CreateGameCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("create_game")
                .thenWhitespace()
                .then(new IntParser("maxPlayers"))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        int maxPlayers = ctx.getInt("maxPlayers");
        getView().createGameAndJoin(maxPlayers);
    }
}
