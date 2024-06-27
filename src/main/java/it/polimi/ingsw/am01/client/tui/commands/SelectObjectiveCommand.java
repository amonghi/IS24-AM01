package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

/**
 * This command allows players to select secret objective in {@link it.polimi.ingsw.am01.client.tui.scenes.SelectObjectiveScene}.
 *
 * @see it.polimi.ingsw.am01.client.tui.scenes.SelectObjectiveScene
 */
public class SelectObjectiveCommand extends TuiCommand {

    public SelectObjectiveCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("select objective <objectiveChoice>", "Select secret objective");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("select")
                .validatePre(this::validateState)
                .thenWhitespace()
                .thenLiteral("objective")
                .thenWhitespace()
                .then(new EnumParser<>("objectiveChoice", SecretObjectiveChoice.class))
                .executes(this::execute)
                .end();
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        SecretObjectiveChoice choice = ctx.getEnum("objectiveChoice", SecretObjectiveChoice.class);

        int objectiveId = switch (choice) {
            case LEFT -> getView().getSecretObjectivesId().getFirst();
            case RIGHT -> getView().getSecretObjectivesId().getLast();
        };

        getView().setSecretObjectiveChoiceId(objectiveId);
        getView().selectSecretObjective(objectiveId);
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME)
                || !getView().getGameStatus().equals(GameStatus.SETUP_OBJECTIVE)
                || getView().getPlayersHaveChosenSecretObjective().contains(getView().getPlayerName())
                || getView().isManualVisible()) {

            throw new ValidationException();
        }
    }

    /**
     * This enum represents all available player's choices during {@code SETUP_OBJECTIVE} status.
     */
    public enum SecretObjectiveChoice {
        /**
         * The objective on the left on the screen
         */
        LEFT,
        /**
         * The objective on the right on the screen
         */
        RIGHT;

        /**
         * @return an integer that represents the objective index related to the choice
         */
        public int getObjectiveIndex() {
            return switch (this) {
                case LEFT -> 0;
                case RIGHT -> 1;
            };
        }
    }
}
