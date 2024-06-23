package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class SetChatVisibilityCommand extends TuiCommand {

    public SetChatVisibilityCommand(TuiView view) {
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
                        .thenLiteral("chat")
                        .executes(this::show)
                        .end()
        );

        builder.branch(
                SequenceBuilder
                        .literal("hide")
                        .validatePre(this::validateState)
                        .thenWhitespace()
                        .thenLiteral("chat")
                        .executes(this::hide)
                        .end()
        );

        return builder.build();
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME)
                || (getView().getGameStatus().equals(GameStatus.RESTORING))
                || (getView().getGameStatus().equals(GameStatus.FINISHED))) {
            throw new ValidationException();
        }
    }

    private void show(CommandContext ctx) {
        getView().showChat();
    }

    private void hide(CommandContext ctx) {
        getView().hideChat();
    }
}
