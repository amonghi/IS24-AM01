package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.*;

public class ConnectCommand extends TuiCommand {
    public ConnectCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("connect")
                .thenWhitespace()
                .then(new EnumParser<>("connectionType", View.ConnectionType.class))
                .thenWhitespace()
                .then(new WordArgumentParser("hostname"))
                .thenWhitespace()
                .then(new IntParser("port"))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        View.ConnectionType connectionType = ctx.getEnum("connectionType", View.ConnectionType.class);
        String hostname = ctx.getString("hostname");
        int port = ctx.getInt("port");

        this.getView().connect(connectionType, hostname, port);
    }
}
