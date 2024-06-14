package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

import java.util.List;

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
        return SequenceBuilder
                .literal("focus")
                .thenWhitespace()
                .endWithAlternatives(List.of(
                        SequenceBuilder
                                .literal("reset")
                                .executes(this::resetFocus)
                                .end(),
                        SequenceBuilder
                                .literal("player")
                                .thenWhitespace()
                                .then(new WordArgumentParser("playerName"))
                                .executes(this::changeFocus)
                                .end()
                ));
    }

    private void changeFocus(CommandContext ctx) {
        getView().setFocusedPlayer(ctx.getString("playerName"));
    }

    private void resetFocus(CommandContext ctx) {
        getView().setFocusedPlayer(getView().getPlayerName());
    }

    private void validateState() throws ValidationException {
        if (!isCorrectState(this.getView())) {
            throw new ValidationException();
        }
    }

    private void validatePlayerName(CommandContext ctx) throws ValidationException {
        if (!getView().getPlayersInGame().contains(ctx.getString("playerName")))
            throw new ValidationException("Invalid player");
    }
}
