package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

/**
 * This method allows players to flip the cards on hand on {@link it.polimi.ingsw.am01.client.tui.scenes.PlayAreaScene}.
 *
 * @see it.polimi.ingsw.am01.client.tui.scenes.PlayAreaScene
 * @see it.polimi.ingsw.am01.model.card.face.CardFace
 */
public class SetVisibleCardSideCommand extends TuiCommand {

    public SetVisibleCardSideCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("flip <cardNumber>", "Flip the specified card in player's hand");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("flip")
                .validatePre(this::validateState)
                .thenWhitespace()
                .then(new IntParser("cardNumber"))
                .validatePost(this::validateCardNumber)
                .executes(this::execute)
                .end();
    }

    /**
     * This method checks if {@code cardNumber} is valid.
     *
     * @param ctx the context of the command
     * @throws ValidationException if {@code cardNumber} is invalid
     */
    private void validateCardNumber(CommandContext ctx) throws ValidationException {
        int index = ctx.getInt("cardNumber");
        if (index < 1 || index > getView().getHand().size()) {
            throw new ValidationException();
        }
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
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        int cardNumber = ctx.getInt("cardNumber");

        getView().flipCard(cardNumber - 1);
    }
}
