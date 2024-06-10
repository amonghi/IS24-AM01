package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.model.player.PlayerColor;

public class SelectColorCommand extends TuiCommand {

    public SelectColorCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("select")
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
}
