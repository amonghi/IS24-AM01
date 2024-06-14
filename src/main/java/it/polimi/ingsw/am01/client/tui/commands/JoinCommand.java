package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;

public class JoinCommand extends TuiCommand {

    public JoinCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("join")
                .validatePre(this::validateState)
                .thenWhitespace()
                .then(new IntParser("gameId"))
                .validatePost(this::validate)
                .executes(this::execute)
                .end();
    }

    private void validate(CommandContext ctx) throws ValidationException {
        int gameId = ctx.getInt("gameId");

        if (!getView().getGames().containsKey(gameId)) {
            throw new ValidationException("gameId not valid");
        }
    }

    private void execute(CommandContext ctx) {
        int gameId = ctx.getInt("gameId");

        getView().joinGame(gameId);
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.AUTHENTICATED)) {
            throw new ValidationException();
        }
    }
}
