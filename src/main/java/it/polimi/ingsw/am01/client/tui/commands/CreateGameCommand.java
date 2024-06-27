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
 * This command allows players to create a game.
 */
public class CreateGameCommand extends TuiCommand {
    public CreateGameCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("create_game <maxPlayers>", "Create a new game with a capacity of 'maxPlayers'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("create_game")
                .validatePre(this::validateState)
                .thenWhitespace()
                .then(new IntParser("maxPlayers"))
                .validatePost(this::validate)
                .executes(this::execute)
                .end();
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        int maxPlayers = ctx.getInt("maxPlayers");
        getView().createGameAndJoin(maxPlayers);
    }

    /**
     * This method checks the {@code maxPlayers} field
     *
     * @param ctx the context of the command
     * @throws ValidationException if {@code maxPlayers} is incorrect
     * @see it.polimi.ingsw.am01.model.game.Game#Game(int, int)
     */
    private void validate(CommandContext ctx) throws ValidationException {
        int maxPlayers = ctx.getInt("maxPlayers");

        if (2 > maxPlayers || maxPlayers > 4) {
            throw new ValidationException("maxPlayers must be between 2 and 4");
        }
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
