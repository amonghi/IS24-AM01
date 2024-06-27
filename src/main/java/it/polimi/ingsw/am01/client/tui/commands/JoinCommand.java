package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.IntParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

/**
 * This command allows players to join a game.
 */
public class JoinCommand extends TuiCommand {

    public JoinCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("join <gameId>", "Join a game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("join")
                .validatePre(this::validateState)
                .thenWhitespace()
                .then(new IntParser("gameId"))
                .validatePost(this::validate)
                .executes(this::execute)
                .end();
    }

    /**
     * This method checks if {@code gameId} is valid.
     *
     * @param ctx the context of the command
     * @throws ValidationException if {@code gameId} is invalid
     */
    private void validate(CommandContext ctx) throws ValidationException {
        int gameId = ctx.getInt("gameId");

        if (!getView().getGames().containsKey(gameId)) {
            throw new ValidationException("gameId not valid");
        }
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        int gameId = ctx.getInt("gameId");

        getView().joinGame(gameId);
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.AUTHENTICATED) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }
}
