package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class SelectObjectiveCommand extends TuiCommand {

    public SelectObjectiveCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("select")
                .validatePre(this::validateState)
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

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.SETUP_OBJECTIVE)) {
            throw new ValidationException();
        }
    }

    public enum SecretObjectiveChoice {
        LEFT,
        RIGHT;

        public int getObjectiveIndex() {
            return switch (this) {
                case LEFT -> 0;
                case RIGHT -> 1;
            };
        }
    }
}
