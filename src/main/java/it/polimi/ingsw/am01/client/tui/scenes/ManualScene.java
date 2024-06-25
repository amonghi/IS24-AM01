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

public class ManualScene extends Composition {
    private final TuiView view;

    public ManualScene(TuiView view) {
        this.view = view;
    }

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

    public List<CommandDetail> getCommandsDetails() {
        return switch (view.getState()) {
            case NOT_CONNECTED -> List.of(
                    ConnectCommand.getCommandDetail(),
                    QuitCommand.getCommandDetail(),
                    SetManualVisibilityCommand.getCommandDetail()
            );
            case NOT_AUTHENTICATED -> List.of(
                    AuthenticateCommand.getCommandDetail(),
                    QuitCommand.getCommandDetail(),
                    SetManualVisibilityCommand.getCommandDetail()
            );
            case AUTHENTICATED -> List.of(
                    CreateGameCommand.getCommandDetail(),
                    JoinCommand.getCommandDetail(),
                    QuitCommand.getCommandDetail(),
                    SetManualVisibilityCommand.getCommandDetail()
            );
            case IN_GAME -> switch (view.getGameStatus()) {
                case AWAITING_PLAYERS -> List.of(
                        StartGameCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
                case SETUP_STARTING_CARD_SIDE -> List.of(
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail(),
                        SelectStartingCardSideCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
                case SETUP_COLOR -> List.of(
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail(),
                        SelectColorCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
                case SETUP_OBJECTIVE -> List.of(
                        SetChatVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail(),
                        SelectObjectiveCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
                case PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED -> List.of(
                        SetObjectivesVisibilityCommand.getCommandDetail(),
                        SetVisibleCardSideCommand.getCommandDetail(),
                        SetChatVisibilityCommand.getCommandDetail(),
                        SetBoardVisibilityCommand.getCommandDetail(),
                        SendMessageCommand.getCommandDetail(),
                        ChangeFocusedPlayerCommand.getCommandDetail(),
                        DrawCardCommand.getCommandDetail(),
                        PlaceCardCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
                case FINISHED -> List.of(
                        ExitFinishedGameCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
                case RESTORING -> List.of(
                        ResumeGameCommand.getCommandDetail(),
                        QuitCommand.getCommandDetail(),
                        SetManualVisibilityCommand.getCommandDetail()
                );
            };
        };
    }

    public record CommandDetail(String command, String detail) {
    }
}
