package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;

public class SelectObjectiveCommand extends TuiCommand {

    public SelectObjectiveCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("select")
                .thenWhitespace()
                .thenLiteral("objective")
                .thenWhitespace()
                .then(new EnumParser<>("objectiveChoice", SecretObjectiveChoice.class))
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        SecretObjectiveChoice choice = ctx.getEnum("objectiveChoice", SecretObjectiveChoice.class);

        int objectiveId = switch (choice) {
            case LEFT -> getView().getSecretObjectivesId().getFirst();
            case RIGHT -> getView().getSecretObjectivesId().getLast();
        };

        getView().selectSecretObjective(objectiveId);
    }

    private enum SecretObjectiveChoice {
        LEFT,
        RIGHT
    }
}
