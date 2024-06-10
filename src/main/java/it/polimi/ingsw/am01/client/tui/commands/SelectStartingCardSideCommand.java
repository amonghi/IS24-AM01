package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.model.card.Side;

public class SelectStartingCardSideCommand extends TuiCommand {

    public SelectStartingCardSideCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("select")
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


}
