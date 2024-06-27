package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.parser.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

/**
 * This command allows to connect to the server by entering network params (protocol, hostname and port).
 */
public class ConnectCommand extends TuiCommand {
    public ConnectCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("connect <connectionType> <hostname> <port>", "Connect to the server");
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        View.ConnectionType connectionType = ctx.getEnum("connectionType", View.ConnectionType.class);
        String hostname = ctx.getString("hostname");
        int port = ctx.getInt("port");

        this.getView().connect(connectionType, hostname, port);
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.NOT_CONNECTED) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }
}
