package it.polimi.ingsw.am01.client.tui.commands;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.command.*;
import it.polimi.ingsw.am01.client.tui.command.parser.GreedyTextArgumentParser;
import it.polimi.ingsw.am01.client.tui.command.validator.ValidationException;
import it.polimi.ingsw.am01.model.game.GameStatus;

public class SendMessageCommand extends TuiCommand {

    public SendMessageCommand(TuiView view) {
        super(view);
    }

    @Override
    protected CommandNode buildRootNode() {
        CommandBuilder builder = CommandBuilder.root();
        //direct messages
        builder.branch(
                SequenceBuilder
                        .literal("send")
                        .validatePre(this::validateState)
                        .thenWhitespace()
                        .thenLiteral("direct")
                        .thenWhitespace()
                        .then(new WordArgumentParser("recipient"))
                        .validatePost(this::validateRecipient)
                        .thenWhitespace()
                        .then(new GreedyTextArgumentParser("content"))
                        .executes(this::executeDirectMessage)
                        .end()
        );

        //broadcast messages
        builder.branch(
                SequenceBuilder
                        .literal("send")
                        .validatePre(this::validateState)
                        .thenWhitespace()
                        .thenLiteral("broadcast")
                        .thenWhitespace()
                        .then(new GreedyTextArgumentParser("content"))
                        .executes(this::executeBroadcastMessage)
                        .end()
        );

        return builder.build();
    }

    private void validateState() throws ValidationException {
        if (!getView().getState().equals(ClientState.IN_GAME) || (getView().getGameStatus().equals(GameStatus.RESTORING))) {
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
