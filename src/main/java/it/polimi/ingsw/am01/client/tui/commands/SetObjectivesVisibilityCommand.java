package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

/**
 * This command allows players to show/hide the objectives' box on {@link it.polimi.ingsw.am01.client.tui.scenes.PlayAreaScene}.
 *
 * @see it.polimi.ingsw.am01.client.tui.scenes.PlayAreaScene
 * @see it.polimi.ingsw.am01.model.objective.Objective
 */
public class SetObjectivesVisibilityCommand extends TuiCommand {

    public SetObjectivesVisibilityCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("show objectives / hide objectives", "Show/hide common and secret objectives");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        CommandBuilder builder = CommandBuilder.root();

        builder.branch(
                SequenceBuilder
                        .literal("show")
                        .validatePre(() -> {
                            this.validateState();
                            this.validateShow();
                        })
                        .thenWhitespace()
                        .thenLiteral("objectives")
                        .executes(this::show)
                        .end()
        );

        builder.branch(
                SequenceBuilder
                        .literal("hide")
                        .validatePre(() -> {
                            this.validateState();
                            this.validateHide();
                        })
                        .thenWhitespace()
                        .thenLiteral("objectives")
                        .executes(this::hide)
                        .end()
        );

        return builder.build();
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) ||
                ((!getView().getGameStatus().equals(GameStatus.PLAY))
                        && (!getView().getGameStatus().equals(GameStatus.SECOND_LAST_TURN))
                        && (!getView().getGameStatus().equals(GameStatus.LAST_TURN))
                        && (!getView().getGameStatus().equals(GameStatus.SUSPENDED)))
                || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method checks if objectives' box is already shown on the screen.
     *
     * @throws ValidationException if objectives' box is already shown on the screen.
     */
    private void validateShow() throws ValidationException {
        if (getView().areObjectivesVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method checks if objectives' box is closed.
     *
     * @throws ValidationException if objectives' box is already closed
     */
    private void validateHide() throws ValidationException {
        if (!getView().areObjectivesVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method executes the command (first branch).
     *
     * @param ctx the context of the command
     */
    private void show(CommandContext ctx) {
        getView().showObjectives();
    }

    /**
     * This method executes the command (second branch).
     *
     * @param ctx the context of the command
     */
    private void hide(CommandContext ctx) {
        getView().hideObjectives();
    }
}
