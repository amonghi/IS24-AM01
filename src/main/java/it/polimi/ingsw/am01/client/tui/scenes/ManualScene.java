package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.commands.*;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.*;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.model.game.GameStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * This scene contains a brief description of all application's commands and interactions.
 * It's a kind of "help" page.
 */
public class ManualScene extends Composition {
    private final TuiView view;

    public ManualScene(TuiView view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component compose() {

        List<Component> commands = new ArrayList<>();
        List<Component> components = new ArrayList<>();

        components.add(
                new Border(Line.Style.ROUNDED, Padding.hv(1, 0, new Column(
                        commands
                )))
        );

        if (view.getState().equals(ClientState.IN_GAME) && (view.getGameStatus().equals(GameStatus.PLAY) || view.getGameStatus().equals(GameStatus.SECOND_LAST_TURN) || view.getGameStatus().equals(GameStatus.LAST_TURN) || view.getGameStatus().equals(GameStatus.SUSPENDED))) {
            components.add(new Border(Line.Style.ROUNDED, Padding.hv(1, 0, new Column(List.of(
                    Padding.hv(0, 1, new Text("Interactions")),
                    Padding.hv(0, 1, new Row(List.of(
                            new Text("--> "),
                            new Text(GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.MAGENTA), "ALT + ArrowKey"),
                            new Text(": Move play area")
                    )))
            )))));
        }

        commands.add(
                Padding.hv(0, 1, new Text("Commands"))
        );

        commands.addAll(getCommandsDetails().stream()
                .map(cmdDetail -> Padding.hv(0, 1, new Row(List.of(
                        new Text("> "),
                        new Text(GraphicalRendition.DEFAULT.withForeground(GraphicalRenditionProperty.ForegroundColor.GREEN), cmdDetail.command()),
                        new Text(" : " + cmdDetail.detail())
                ))))
                .toList());


        return new Column(List.of(
                Centered.horizontally(new Text("Manual")),
                Centered.both(new Row(components))
        ));
    }

    /**
     * @return a {@link List} of command's textual representations (only available commands).
     */
    public List<CommandDetail> getCommandsDetails() {
        List<CommandDetail> commands = new ArrayList<>();

        commands.addAll(switch (view.getState()) {
            case NOT_CONNECTED -> List.of(
                    ConnectCommand.getCommandDetail()
            );
            case NOT_AUTHENTICATED -> List.of(
                    AuthenticateCommand.getCommandDetail()
            );
            case AUTHENTICATED -> List.of(
                    CreateGameCommand.getCommandDetail(),
                    JoinCommand.getCommandDetail()
            );
            case IN_GAME -> switch (view.getGameStatus()) {
                case AWAITING_PLAYERS -> List.of(
                        StartGameCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail()
                );
                case SETUP_STARTING_CARD_SIDE -> List.of(
                        SelectStartingCardSideCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail()
                );
                case SETUP_COLOR -> List.of(
                        SelectColorCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail()
                );
                case SETUP_OBJECTIVE -> List.of(
                        SelectObjectiveCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail()
                );
                case PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED -> List.of(
                        PlaceCardCommand.getCommandDetail(),
                        DrawCardCommand.getCommandDetail(),
                        ChangeFocusedPlayerCommand.getCommandDetail(),
                        SetVisibleCardSideCommand.getCommandDetail(),
                        SetObjectivesVisibilityCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail(),
                        SetBoardVisibilityCommand.getCommandDetail()
                );
                case FINISHED -> List.of(
                        ExitFinishedGameCommand.getCommandDetail()
                );
                case RESTORING -> List.of(
                        ResumeGameCommand.getCommandDetail()
                );
            };
        });

        commands.addAll(List.of(
                SetManualVisibilityCommand.getCommandDetail(),
                QuitCommand.getCommandDetail()
        ));

        return commands;
    }

    /**
     * @param command a string that represent the command tree
     * @param detail  a string that contains a brief command's detail
     * @see TuiCommand
     */
    public record CommandDetail(String command, String detail) {
    }
}
