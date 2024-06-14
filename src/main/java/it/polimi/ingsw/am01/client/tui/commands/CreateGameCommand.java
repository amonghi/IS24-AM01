package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;

public class CreateGameCommand extends TuiCommand {
    public CreateGameCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("create_game")
                .validatePre(this::validateState)
                .thenWhitespace()
                .then(new IntParser("maxPlayers"))
                .validatePost(this::validate)
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        int maxPlayers = ctx.getInt("maxPlayers");
        getView().createGameAndJoin(maxPlayers);
    }

    private void validate(CommandContext ctx) throws ValidationException {
        int maxPlayers = ctx.getInt("maxPlayers");

        if (2 > maxPlayers || maxPlayers > 4) {
            throw new ValidationException("maxPlayers must be between 2 and 4");
        }
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.AUTHENTICATED)) {
            throw new ValidationException();
        }
    }
}
