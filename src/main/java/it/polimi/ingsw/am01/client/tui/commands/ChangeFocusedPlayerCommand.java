package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

import java.util.List;

/**
 * This command allows players to change the play area displayed in the scene {@link it.polimi.ingsw.am01.client.tui.scenes.PlayAreaScene}.
 *
 * @see it.polimi.ingsw.am01.client.tui.scenes.PlayAreaScene
 */
public class ChangeFocusedPlayerCommand extends TuiCommand {

    public ChangeFocusedPlayerCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method is used to check if client is on correct state in order to execute game's commands.
     *
     * @param view the current {@link View} class
     * @return true if client's state is correct, otherwise false
     */
    public static boolean isCorrectState(View view) {
        ClientState state = view.getState();
        GameStatus gameStatus = view.getGameStatus();

        return state.equals(ClientState.IN_GAME) &&
                (gameStatus.equals(GameStatus.PLAY) ||
                        gameStatus.equals(GameStatus.SECOND_LAST_TURN) ||
                        gameStatus.equals(GameStatus.LAST_TURN) ||
                        gameStatus.equals(GameStatus.SUSPENDED));
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("focus reset / focus player <playerName>", "Focus on other players' play areas");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("focus")
                .validatePre(this::validateState)
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
                                .validatePost(this::validatePlayerName)
                                .executes(this::changeFocus)
                                .end()
                ));
    }

    /**
     * This method executes the command (second branch).
     *
     * @param ctx the context of the command
     */
    private void changeFocus(CommandContext ctx) {
        getView().setFocusedPlayer(ctx.getString("playerName"));
    }

    /**
     * This method executes the command (first branch).
     *
     * @param ctx the context of the command
     */
    private void resetFocus(CommandContext ctx) {
        getView().setFocusedPlayer(getView().getPlayerName());
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!isCorrectState(this.getView()) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method checks if the specified {@code playerName} is valid
     *
     * @param ctx the command context
     * @throws ValidationException if player name is incorrect
     */
    private void validatePlayerName(CommandContext ctx) throws ValidationException {
        if (!getView().getPlayersInGame().contains(ctx.getString("playerName")))
            throw new ValidationException("Invalid player");
    }
}
