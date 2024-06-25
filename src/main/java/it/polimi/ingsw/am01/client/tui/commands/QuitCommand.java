package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

public class QuitCommand extends TuiCommand {
    public QuitCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("quit", "Quit the application");
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
