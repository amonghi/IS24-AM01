package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;

public class ConnectCommand extends TuiCommand {
    public ConnectCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("connect")
                .validatePre(this::validateState)
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

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.NOT_CONNECTED)) {
            throw new ValidationException();
        }
    }
}
