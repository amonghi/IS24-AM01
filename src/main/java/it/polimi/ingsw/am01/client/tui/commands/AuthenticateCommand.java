package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

public class AuthenticateCommand extends TuiCommand {
    public AuthenticateCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("authenticate <playerName>", "User login");
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("authenticate")
                .validatePre(this::validateState)
                .thenWhitespace()
                .then(new WordArgumentParser("playerName"))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        String playerName = ctx.getString("playerName");
        this.getView().authenticate(playerName);
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.NOT_AUTHENTICATED) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }
}
