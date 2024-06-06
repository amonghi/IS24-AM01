package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.WordArgumentParser;

public class AuthenticateCommand extends TuiCommand {
    public AuthenticateCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("authenticate")
                .thenWhitespace()
                .then(new WordArgumentParser("playerName"))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        String playerName = ctx.getString("playerName");
        this.getView().authenticate(playerName);
    }
}
