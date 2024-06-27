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
 * This command allows players to resume a game ({@code RESTORING} status). Command available in {@link it.polimi.ingsw.am01.client.tui.scenes.RestoringScene}.
 *
 * @see GameStatus
 * @see it.polimi.ingsw.am01.client.tui.scenes.RestoringScene
 */
public class ResumeGameCommand extends TuiCommand {

    public ResumeGameCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("resume game", "Resume a game after a server crash");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("resume")
                .validatePre(() -> {
                    this.validateState();
                    this.validateResume();
                })
                .thenWhitespace()
                .thenLiteral("game")
                .executes(this::execute)
                .end();
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.RESTORING) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        getView().resumeGame();
    }

    /**
     * This method checks if resume is permitted.
     *
     * @throws ValidationException if resume is not permitted.
     */
    private void validateResume() throws ValidationException {
        int playersConnected = getView().getPlayersInGame().stream().filter(player -> getView().isConnected(player)).toList().size();

        if (playersConnected < 2) {
            throw new ValidationException();
        }
    }
}
