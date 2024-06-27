package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;

/**
 * This class represents a generic Tui command.
 * Command is a sequence of instructions that can be executed by the players.
 * Players use commands to interact with the application.
 */
public abstract class TuiCommand {
    private final TuiView view;
    private final CommandNode rootNode;

    public TuiCommand(TuiView view) {
        this.view = view;
        this.rootNode = this.buildRootNode();
    }

    /**
     * This method permits to build the command tree.
     *
     * @return the tree of the command (root)
     */
    protected abstract CommandNode buildRootNode();

    /**
     * @return the {@link TuiView} instance
     */
    protected TuiView getView() {
        return view;
    }

    /**
     * @return the tree of the command (root)
     */
    public CommandNode getRootNode() {
        return this.rootNode;
    }
}
