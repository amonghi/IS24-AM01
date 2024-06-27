package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;

import java.util.List;

/**
 * This command allows to draw a card from a {@code DrawSource}.
 *
 * @see it.polimi.ingsw.am01.model.game.DrawSource
 */
public class DrawCardCommand extends TuiCommand {

    public DrawCardCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("draw from <deckType> / draw from face_up <faceUpNumber", "Draw card from deck/faceup cards");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {

        return SequenceBuilder
                .literal("draw")
                .validatePre(this::validateState)
                .thenWhitespace()
                .thenLiteral("from")
                .thenWhitespace()
                .endWithAlternatives(List.of(
                        SequenceBuilder
                                .root()
                                .then(new EnumParser<>("deckType", DeckLocation.class))
                                .validatePost(this::checkDeck)
                                .executes(this::drawFromDeck)
                                .end(),
                        SequenceBuilder
                                .literal("face_up")
                                .thenWhitespace()
                                .then(new IntParser("faceUpNumber"))
                                .validatePost(this::validateFaceUpNumber)
                                .executes(this::drawFromFaceUp)
                                .end()
                ));
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        boolean isCorrectState = getView().getState().equals(ClientState.IN_GAME) &&
                ((getView().getGameStatus().equals(GameStatus.PLAY))
                        || (getView().getGameStatus().equals(GameStatus.SECOND_LAST_TURN))
                        || (getView().getGameStatus().equals(GameStatus.LAST_TURN)))
                && getView().getTurnPhase().equals(TurnPhase.DRAWING) && getView().getCurrentPlayer().equals(getView().getPlayerName());

        if (!isCorrectState || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method checks if the specified deck is empty.
     *
     * @param ctx the context of the command
     * @throws ValidationException if deck is empty
     */
    private void checkDeck(CommandContext ctx) throws ValidationException {
        DeckLocation deckLocation = ctx.getEnum("deckType", DeckLocation.class);

        if (getView().isDeckEmpty(deckLocation)) {
            throw new ValidationException();
        }
    }

    /**
     * This method executes the command (first branch).
     *
     * @param ctx the context of the command
     */
    private void drawFromDeck(CommandContext ctx) {
        DeckLocation deckLocation = ctx.getEnum("deckType", DeckLocation.class);

        getView().drawCardFromDeck(deckLocation);
    }

    /**
     * This method checks if the specified {@code faceUpNumber} is valid.
     *
     * @param ctx the context of the command
     * @throws ValidationException if {@code faceUpNumber} is invalid
     */
    private void validateFaceUpNumber(CommandContext ctx) throws ValidationException {
        int faceUpNumber = ctx.getInt("faceUpNumber");

        if (0 >= faceUpNumber || faceUpNumber > getView().getFaceUpCards().size()) {
            throw new ValidationException();
        }
    }

    /**
     * This method executes the command (second branch).
     *
     * @param ctx the context of the command
     */
    private void drawFromFaceUp(CommandContext ctx) {
        int faceUpNumber = ctx.getInt("faceUpNumber");
        int cardId = getView().getFaceUpCards().get(faceUpNumber - 1);

        getView().drawCardFromFaceUpCards(cardId);
    }
}
