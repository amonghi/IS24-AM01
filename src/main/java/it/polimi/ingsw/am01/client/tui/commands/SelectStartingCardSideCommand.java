package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;

/**
 * This command allows players to select the starting card side in {@link it.polimi.ingsw.am01.client.tui.scenes.SelectStartingCardSideScene}.
 *
 * @see it.polimi.ingsw.am01.client.tui.scenes.SelectStartingCardSideScene
 */
public class SelectStartingCardSideCommand extends TuiCommand {

    public SelectStartingCardSideCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("select side <side>", "Select starting card side");
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
                .thenLiteral("side")
                .thenWhitespace()
                .then(new EnumParser<>("side", Side.class))
                .executes(this::execute)
                .end();
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        Side side = ctx.getEnum("side", Side.class);
        getView().selectStartingCardSide(side);
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME)
                || !getView().getGameStatus().equals(GameStatus.SETUP_STARTING_CARD_SIDE)
                || getView().getStartingCardPlacements().containsKey(getView().getPlayerName())
                || getView().isManualVisible()) {

            throw new ValidationException();
        }
    }
}
