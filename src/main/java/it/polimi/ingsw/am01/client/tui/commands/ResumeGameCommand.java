package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class ResumeGameCommand extends TuiCommand {

    public ResumeGameCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("resume")
                .validatePre(() -> {
                    this.validateState();
                    this.validateResume();
                })
                .thenWhitespace()
                .thenLiteral("game")
                .executes(this::execute)
                .end();
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.RESTORING)) {
            throw new ValidationException();
        }
    }

    private void execute(CommandContext ctx) {
        getView().resumeGame();
    }

    private void validateResume() throws ValidationException {
        int playersConnected = getView().getPlayersInGame().stream().filter(player -> getView().isConnected(player)).toList().size();

        if (playersConnected < 2) {
            throw new ValidationException();
        }
    }
}
