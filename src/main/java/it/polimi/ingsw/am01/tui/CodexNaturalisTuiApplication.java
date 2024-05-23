package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.elements.*;
import it.polimi.ingsw.am01.tui.component.layout.Centered;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.Row;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexRow;
import it.polimi.ingsw.am01.tui.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class CodexNaturalisTuiApplication extends TuiApplication<CodexNaturalisTuiApplication.State> {
    private static final char QUIT_CHAR = 'q';
    private static final char DEBUG_CHAR = 'd';

    public CodexNaturalisTuiApplication(Terminal terminal) {
        super(terminal, new State());

        Thread keyboardListenerThread = new Thread(new Runnable() {
            private final KeyboardReader keyboardReader = new KeyboardReader(System.in);

            @Override
            public void run() {
                while (true) {
                    try {
                        Key key = keyboardReader.nextSupportedKey();

                        CodexNaturalisTuiApplication.this.updateState(state -> {
                            state.lastKey = key;
                        });

                        switch (key) {
                            case Key.Character(char c) -> {
                                switch (c) {
                                    case QUIT_CHAR -> {
                                        CodexNaturalisTuiApplication.this.shutdown();
                                        return;
                                    }
                                    case DEBUG_CHAR -> debug();
                                    default -> write(c);
                                }
                            }
                            case Key.Backspace() -> erase();
                            case Key.Del() -> erase();
                            default -> {
                            }
                        }
                    } catch (IOException e) {
                        CodexNaturalisTuiApplication.this.shutdown();
                        return;
                    }
                }
            }

            private void debug() {
                CodexNaturalisTuiApplication.this.updateState(state -> {
                    state.debugEnabled = !state.debugEnabled;
                });
            }

            private void write(char c) {
                CodexNaturalisTuiApplication.this.updateState(state -> {
                    state.input += c;
                });
            }

            private void erase() {
                CodexNaturalisTuiApplication.this.updateState(state -> {
                    if (!state.input.isEmpty()) {
                        state.input = state.input.substring(0, state.input.length() - 1);
                    }
                });
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
                                        new Text("last key: " + state.lastKey)
                                ))
                        )),
                        new Border(BorderStyle.DEFAULT,
                                new Row(List.of(
                                        new Text("Input: " + state.input),
                                        new Cursor()
                                ))
                        )
                ))
        );
    }

    public static class State extends TuiApplication.State {
        public String input = "";
        public Key lastKey;
    }
}
