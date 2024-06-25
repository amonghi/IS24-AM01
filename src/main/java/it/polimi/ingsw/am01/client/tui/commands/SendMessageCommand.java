package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.CommandContext;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.parser.GreedyTextArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.parser.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.client.tui.scenes.ManualScene;
import it.polimi.ingsw.am01.model.game.GameStatus;

import java.util.List;

public class SendMessageCommand extends TuiCommand {

    public SendMessageCommand(TuiView view) {
        super(view);
    }

    public static ManualScene.CommandDetail getCommandDetail() {
        return new ManualScene.CommandDetail("send direct <recipient> <content> / send broadcast <content>", "Send a direct/broadcast message");
    }

    @Override
    protected CommandNode buildRootNode() {
        return SequenceBuilder
                .literal("send")
                .validatePre(this::validateState)
                .thenWhitespace()
                .endWithAlternatives(List.of(
                        SequenceBuilder
                                .literal("direct")
                                .thenWhitespace()
                                .then(new WordArgumentParser("recipient"))
                                .validatePost(this::validateRecipient)
                                .thenWhitespace()
                                .then(new GreedyTextArgumentParser("content"))
                                .executes(this::executeDirectMessage)
                                .end(),
                        SequenceBuilder
                                .literal("broadcast")
                                .thenWhitespace()
                                .then(new GreedyTextArgumentParser("content"))
                                .executes(this::executeBroadcastMessage)
                                .end()
                ));
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME)
                || (getView().getGameStatus().equals(GameStatus.RESTORING))
                || (getView().getGameStatus().equals(GameStatus.FINISHED))
                || getView().isManualVisible()) {
            throw new ValidationException();
        }
    }

    private void executeDirectMessage(CommandContext ctx) {
        String recipient = ctx.getString("recipient");
        String content = ctx.getString("content");

        getView().sendDirectMessage(recipient, content);
    }

    private void executeBroadcastMessage(CommandContext ctx) {
        String content = ctx.getString("content");

        getView().sendBroadcastMessage(content);
    }

    private void validateRecipient(CommandContext ctx) throws ValidationException {
        String recipient = ctx.getString("recipient");

        if (!getView().getPlayersInGame().contains(recipient) || getView().getPlayerName().equals(recipient)) {
            throw new ValidationException();
        }
    }
}
