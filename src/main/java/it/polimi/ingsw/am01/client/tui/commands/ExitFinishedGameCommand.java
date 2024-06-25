package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class ExitFinishedGameCommand extends TuiCommand {
    public ExitFinishedGameCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("back", "Go back to the games' list");
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("back")
                .validatePre(this::validateState)
                .executes(this::execute)
                .end();
    }

    private void execute(CommandContext ctx) {
        getView().exitFinishedGame();
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.FINISHED) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }
}
