package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class SetObjectivesVisibilityCommand extends TuiCommand {

    public SetObjectivesVisibilityCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        CommandBuilder builder = CommandBuilder.root();

        builder.branch(
                SequenceBuilder
                        .literal("show")
                        .validatePre(this::validateState)
                        .thenWhitespace()
                        .thenLiteral("objectives")
                        .executes(this::show)
                        .end()
        );

        builder.branch(
                SequenceBuilder
                        .literal("hide")
                        .validatePre(this::validateState)
                        .thenWhitespace()
                        .thenLiteral("objectives")
                        .executes(this::hide)
                        .end()
        );

        return builder.build();
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) ||
                ((!getView().getGameStatus().equals(GameStatus.PLAY))
                        && (!getView().getGameStatus().equals(GameStatus.SECOND_LAST_TURN))
                        && (!getView().getGameStatus().equals(GameStatus.LAST_TURN))
                        && (!getView().getGameStatus().equals(GameStatus.SUSPENDED)))) {
            throw new ValidationException();
        }
    }

    private void show(CommandContext ctx) {
        getView().showObjectives();
    }

    private void hide(CommandContext ctx) {
        getView().hideObjectives();
    }
}
