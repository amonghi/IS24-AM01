package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.Position;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;

public class PlaceCardCommand extends TuiCommand {

    public PlaceCardCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("place card <cardNumber> <side> <i> <j>", "Place a card");
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("place")
                .validatePre(this::validateState)
                .thenWhitespace()
                .thenLiteral("card")
                .thenWhitespace()
                .then(new IntParser("cardNumber"))
                .validatePost(this::validateCardNumber)
                .thenWhitespace()
                .then(new EnumParser<>("side", Side.class))
                .thenWhitespace()
                .then(new IntParser("i"))
                .thenWhitespace()
                .then(new IntParser("j"))
                .validatePost(this::validatePosition)
                .executes(this::execute)
                .end();
    }

    private void validateState() throws ValidationException {
        boolean isCorrectState = getView().getState().equals(ClientState.IN_GAME) &&
                ((getView().getGameStatus().equals(GameStatus.PLAY))
                        || (getView().getGameStatus().equals(GameStatus.SECOND_LAST_TURN))
                        || (getView().getGameStatus().equals(GameStatus.LAST_TURN)))
                && getView().getTurnPhase().equals(TurnPhase.PLACING) && getView().getCurrentPlayer().equals(getView().getPlayerName());

        if (!isCorrectState || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    private void validateCardNumber(CommandContext ctx) throws ValidationException {
        int index = ctx.getInt("cardNumber");
        if (index < 1 || index > getView().getHand().size()) {
            throw new ValidationException();
        }
    }

    private void validatePosition(CommandContext ctx) throws ValidationException {
        int i = ctx.getInt("i");
        int j = ctx.getInt("j");

        Position pos = new Position(i, j);

        if (!getView().getPlayablePositions().contains(pos)) {
            throw new ValidationException();
        }
    }

    private void execute(CommandContext ctx) {
        int cardNumber = ctx.getInt("cardNumber");
        int cardId = getView().getHand().get(cardNumber - 1);
        Side side = ctx.getEnum("side", Side.class);
        int i = ctx.getInt("i");
        int j = ctx.getInt("j");

        getView().placeCard(cardId, side, i, j);
    }
}
