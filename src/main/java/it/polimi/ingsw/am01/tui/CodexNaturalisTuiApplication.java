package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.elements.CardComponent;
import it.polimi.ingsw.am01.tui.component.elements.Text;
import it.polimi.ingsw.am01.tui.component.layout.Centered;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.SpaceAround;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexRow;
import it.polimi.ingsw.am01.tui.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class CodexNaturalisTuiApplication extends TuiApplication<CodexNaturalisTuiApplication.State> {
    private static final char EXIT_CHAR = 'q';
    private static final char DEBUG_CHAR = 'd';

    public CodexNaturalisTuiApplication(Terminal terminal) {
        super(terminal, new State());
        KeyboardReader keyboardReader = new KeyboardReader(System.in);

        Thread keyboardListenerThread = new Thread(() -> {
            while (true) {
                try {
                    Key key = keyboardReader.nextSupportedKey();

                    boolean quit = false;
                    boolean toggleDebug = false;

                    String keyString = switch (key) {
                        case Key.Character(char c) -> {
                            switch (c) {
                                case EXIT_CHAR -> quit = true;
                                case DEBUG_CHAR -> toggleDebug = true;
                            }
                            yield String.valueOf(c);
                        }
                        case Key.Arrow(Key.Arrow.Direction dir) -> switch (dir) {
                            case UP -> "⬆️";
                            case DOWN -> "⬇️";
                            case LEFT -> "⬅️";
                            case RIGHT -> "➡️";
                        };
                    };

                    if (quit) {
                        this.shutdown();
                        return;
                    }

                    boolean finalToggleDebug = toggleDebug;
                    this.updateState(state -> {
                        if (finalToggleDebug) {
                            state.debugEnabled = !state.debugEnabled;
                        }

                        state.lastPressed = keyString;
                    });
                } catch (IOException e) {
                    this.shutdown();
                    return;
                }
            }
        });
        keyboardListenerThread.start();
    }

    @Override
    Component compose(State state) {
        return Centered.vertically(
                new Column(List.of(
                        new FlexRow(List.of(
                                new FlexChild.Flexible(1, Centered.horizontally(
                                        new CardComponent()
                                )),
                                new FlexChild.Flexible(1, Centered.horizontally(
                                        new CardComponent()
                                ))
                        )),
                        new FlexRow(List.of(
                                new FlexChild.Flexible(1, Centered.horizontally(
                                        new SpaceAround(1, 1, 1, 1,
                                                new Text("Last pressed: " + state.lastPressed)
                                        )
                                ))
                        ))
                ))
        );
    }

    public static class State extends TuiApplication.State {
        public String lastPressed;
    }
}
