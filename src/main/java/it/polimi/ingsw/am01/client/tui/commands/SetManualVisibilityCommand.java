package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;

public class SetManualVisibilityCommand extends TuiCommand {

    public SetManualVisibilityCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("show manual / hide manual", "Show/hide the manual that contains detail about available commands");
    }

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

    private void validateShow() throws ValidationException {
        if (getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    private void validateHide() throws ValidationException {
        if (!getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    private void hide(CommandContext ctx) {
        getView().hideManual();
    }

    private void show(CommandContext ctx) {
        getView().showManual();
    }
}
