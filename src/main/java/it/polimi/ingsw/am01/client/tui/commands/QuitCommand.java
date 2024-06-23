package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;

public class QuitCommand extends TuiCommand {
    public QuitCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("quit")
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        this.getView().quitApplication();
    }
}
