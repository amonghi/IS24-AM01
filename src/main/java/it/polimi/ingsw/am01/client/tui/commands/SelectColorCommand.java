package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.EnumParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.player.PlayerColor;

/**
 * This command allows players to select color in {@link it.polimi.ingsw.am01.client.tui.scenes.SelectColorScene}.
 *
 * @see it.polimi.ingsw.am01.client.tui.scenes.SelectColorScene
 */
public class SelectColorCommand extends TuiCommand {

    public SelectColorCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("select color <color>", "Select player color");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("select")
                .validatePre(this::validateState)
                .thenWhitespace()
                .thenLiteral("color")
                .thenWhitespace()
                .then(new EnumParser<>("color", PlayerColor.class))
                .executes(this::execute)
                .end();
    }

    /**
     * This method executes the command.
     *
     * @param ctx the context of the command
     */
    private void execute(CommandContext ctx) {
        PlayerColor color = ctx.getEnum("color", PlayerColor.class);
        getView().selectColor(color);
    }

    /**
     * This method checks the client's state.
     *
     * @throws ValidationException if state is not correct. In this case the command is invalid
     */
    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || !getView().getGameStatus().equals(GameStatus.SETUP_COLOR) || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }
}
