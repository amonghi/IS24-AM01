package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.player.PlayerColor;

public class SelectColorCommand extends TuiCommand {

    public SelectColorCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .root()
                .validate(this::validateState)
                .thenLiteral("select")
                .thenWhitespace()
                .thenLiteral("color")
                .thenWhitespace()
                .then(new EnumParser<>("color", PlayerColor.class))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        PlayerColor color = ctx.getEnum("color", PlayerColor.class);
        getView().selectColor(color);
    }

    private void validateState(CommandContext ctx) throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.SETUP_COLOR)) {
            throw new ValidationException();
        }
    }
}
