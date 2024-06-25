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

public class SetVisibleCardSideCommand extends TuiCommand {

    public SetVisibleCardSideCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("flip <cardNumber>", "Flip the specified card in player's hand");
    }

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

    private void validateCardNumber(CommandContext ctx) throws ValidationException {
        int index = ctx.getInt("cardNumber");
        if (index < 1 || index > getView().getHand().size()) {
            throw new ValidationException();
        }
    }

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

    private void execute(CommandContext ctx) {
        int cardNumber = ctx.getInt("cardNumber");

        getView().flipCard(cardNumber - 1);
    }
}
