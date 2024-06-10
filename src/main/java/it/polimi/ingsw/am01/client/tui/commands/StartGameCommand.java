package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class StartGameCommand extends TuiCommand {

    public StartGameCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .root()
                .validate(this::validateState)
                .thenLiteral("start_game")
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        getView().startGame();
    }

    private void validateState(CommandContext ctx) throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.AWAITING_PLAYERS)) {
            throw new ValidationException();
        }
    }
}
