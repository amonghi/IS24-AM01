package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class SelectStartingCardSideCommand extends TuiCommand {

    public SelectStartingCardSideCommand(TuiView view) {
        super(view);
    }

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

    private void execute(CommandContext ctx) {
        Side side = ctx.getEnum("side", Side.class);
        getView().selectStartingCardSide(side);
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.SETUP_STARTING_CARD_SIDE)) {
            throw new ValidationException();
        }
    }

}
