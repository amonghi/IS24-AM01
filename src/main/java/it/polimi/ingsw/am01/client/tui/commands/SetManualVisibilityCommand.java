package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

/**
 * This command allows players to show/hide the application's manual.
 *
 * @see ManualScene
 */
public class SetManualVisibilityCommand extends TuiCommand {

    public SetManualVisibilityCommand(TuiView view) {
        super(view);
    }

    /**
     * This static method provides a textual representation of the command.
     *
     * @return a {@link ManualScene.CommandDetail} that contains the representation of the command (structure and detail)
     * @see it.polimi.ingsw.am01.client.tui.scenes.ManualScene.CommandDetail
     */
    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("show manual / hide manual", "Show/hide the manual that contains detail about available commands");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandNode buildRootNode() {
        CommandBuilder builder = CommandBuilder.root();

        builder.branch(
                SequenceBuilder
                        .literal("show")
                        .validatePre(this::validateShow)
                        .thenWhitespace()
                        .thenLiteral("manual")
                        .executes(this::show)
                        .end()
        );

        builder.branch(
                SequenceBuilder
                        .literal("hide")
                        .validatePre(this::validateHide)
                        .thenWhitespace()
                        .thenLiteral("manual")
                        .executes(this::hide)
                        .end()
        );

        return builder.build();
    }

    /**
     * This method checks if manual is already shown on the screen.
     *
     * @throws ValidationException if manual is already shown on the screen.
     */
    private void validateShow() throws ValidationException {
        if (getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method checks if manual is closed.
     *
     * @throws ValidationException if manual is already closed
     */
    private void validateHide() throws ValidationException {
        if (!getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    /**
     * This method executes the command (first branch).
     *
     * @param ctx the context of the command
     */
    private void show(CommandContext ctx) {
        getView().showManual();
    }

    /**
     * This method executes the command (second branch).
     *
     * @param ctx the context of the command
     */
    private void hide(CommandContext ctx) {
        getView().hideManual();
    }
}
