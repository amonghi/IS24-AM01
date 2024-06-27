package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

/**
 * This command allows players to quit the application.
 */
public class QuitCommand extends TuiCommand {
    public QuitCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("quit", "Quit the application");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("quit")
                .executes(this::execute)
                .end();
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        this.getView().quitApplication();
    }
}
