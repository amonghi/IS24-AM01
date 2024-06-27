package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

/**
 * This command allows players to start a game before reaching the maximum capacity of the game.
 */
public class StartGameCommand extends TuiCommand {

    public StartGameCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("start_game", "Start a game before the maximum player number is reached");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("start_game")
                .validatePre(this::validateState)
                .validatePost(this::validateStartGame)
                .executes(this::execute)
                .end();
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        getView().startGame();
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.AWAITING_PLAYERS) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method checks if this action is permitted.
     *
     * @throws ValidationException if it is not permitted to start the game
     */
    private void validateStartGame(CommandContext ctx) throws ValidationException {
        if (getView().getPlayersInGame().size() < 2) {
            throw new ValidationException();
        }
    }
}
