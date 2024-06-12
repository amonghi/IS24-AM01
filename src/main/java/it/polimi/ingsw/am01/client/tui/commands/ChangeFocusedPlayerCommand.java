package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.*;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class ChangeFocusedPlayerCommand extends TuiCommand {

    public ChangeFocusedPlayerCommand(TuiView view) {
        super(view);
    }

    public static boolean isCorrectState(View view) {
        ClientState state = view.getState();
        GameStatus gameStatus = view.getGameStatus();

        return state.equals(ClientState.IN_GAME) &&
                (gameStatus.equals(GameStatus.PLAY) ||
                        gameStatus.equals(GameStatus.SECOND_LAST_TURN) ||
                        gameStatus.equals(GameStatus.LAST_TURN));
    }

    @Override
    protected CommandNode buildRootNode() {
        CommandBuilder builder = CommandBuilder.root();

        builder.branch(
                SequenceBuilder
                        .root()
                        .validate(this::validateState)
                        .thenLiteral("focus")
                        .thenWhitespace()
                        .thenLiteral("reset")
                        .executes(this::resetFocus)
                        .end()
        );

        builder.branch(
                SequenceBuilder
                        .root()
                        .validate(this::validateState)
                        .thenLiteral("focus")
                        .thenWhitespace()
                        .thenLiteral("player")
                        .thenWhitespace()
                        .then(new WordArgumentParser("playerName"))
                        .validate(this::validatePlayerName)
                        .executes(this::changeFocus)
                        .end()
        );

        return builder.build();

    }

    private void changeFocus(CommandContext ctx) {
        getView().setFocusedPlayer(ctx.getString("playerName"));
    }

    private void resetFocus(CommandContext ctx) {
        getView().setFocusedPlayer(getView().getPlayerName());
    }

    private void validateState(CommandContext ctx) throws ValidationException {
        if (!isCorrectState(this.getView())) {
            throw new ValidationException();
        }
    }

    private void validatePlayerName(CommandContext ctx) throws ValidationException {
        if (!getView().getPlayersInGame().contains(ctx.getString("playerName")))
            throw new ValidationException("Invalid player");
    }
}
