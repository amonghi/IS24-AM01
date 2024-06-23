package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;

public abstract class TuiCommand {
    private final TuiView view;
    private final CommandNode rootNode;

    public TuiCommand(TuiView view) {
        this.view = view;
        this.rootNode = this.buildRootNode();
    }

    protected abstract CommandNode buildRootNode();

    protected TuiView getView() {
        return view;
    }

    public CommandNode getRootNode() {
        return this.rootNode;
    }
}
