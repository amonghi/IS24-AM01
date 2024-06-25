package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class SetChatVisibilityCommand extends TuiCommand {

    public SetChatVisibilityCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("show chat / hide chat", "Show/hide game chat");
    }

    @Override
    protected CommandNode buildRootNode() {
        CommandBuilder builder = CommandBuilder.root();

        builder.branch(
                SequenceBuilder
                        .literal("show")
                        .validatePre(() -> {
                            this.validateState();
                            this.validateShow();
                        })
                        .thenWhitespace()
                        .thenLiteral("chat")
                        .executes(this::show)
                        .end()
        );

        builder.branch(
                SequenceBuilder
                        .literal("hide")
                        .validatePre(() -> {
                            this.validateState();
                            this.validateHide();
                        })
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
                || (getView().getGameStatus().equals(GameStatus.FINISHED))
                || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    private void validateShow() throws ValidationException {
        if (getView().isChatVisible()) {
            throw new ValidationException();
        }
    }

    private void validateHide() throws ValidationException {
        if (!getView().isChatVisible()) {
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
