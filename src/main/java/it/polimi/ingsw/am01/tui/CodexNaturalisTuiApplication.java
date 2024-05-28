package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.tui.command.CommandNode;
import it.polimi.ingsw.am01.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.tui.command.WordArgumentParser;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.elements.*;
import it.polimi.ingsw.am01.tui.component.layout.Centered;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.Padding;
import it.polimi.ingsw.am01.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class CodexNaturalisTuiApplication extends TuiApplication<CodexNaturalisTuiApplication.State> {
    private static final char QUIT_CHAR = 'q';
    private static final char DEBUG_CHAR = 'd';

    public CodexNaturalisTuiApplication(Terminal terminal) {
        super(terminal, new State());

        CommandNode cmd = CommandBuilder.root()
                .branch(
                        SequenceBuilder
                                .literal("ping")
                                .thenWhitespace()
                                .thenLiteral("pong")
                                .executes(commandContext -> this.updateState(state -> state.output = "selected: ping pong"))
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("bing")
                                .thenWhitespace()
                                .thenLiteral("bong")
                                .executes(commandContext -> this.updateState(state -> state.output = "selected: ping pong"))
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("hello")
                                .thenWhitespace()
                                .then(new WordArgumentParser("name"))
                                .executes(commandContext -> this.updateState(state -> state.output = "selected: hello, with name=" + commandContext.getString("name")))
                                .end()
                )
                .build();

        Thread keyboardListenerThread = new Thread(new Runnable() {
            private final KeyboardReader keyboardReader = new KeyboardReader(System.in);
            private String input = "";
            private String completion = "";

            @Override
            public void run() {
                this.completeAndUpdateInput();

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
                            case Key.Enter() -> runCommand();
                            case Key.Tab() -> complete();
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
                this.input += c;
                completeAndUpdateInput();
            }

            private void erase() {
                if (!this.input.isEmpty()) {
                    this.input = this.input.substring(0, this.input.length() - 1);
                }
                completeAndUpdateInput();
            }

            private void runCommand() {
                cmd.parse(this.input).getCommandRunnable().ifPresent(cmd -> {
                    cmd.run();
                    this.input = "";
                });
                completeAndUpdateInput();
            }

            private void complete() {
                this.input = this.input + completion;
                this.completion = "";
                updateInput();
            }

            private void completeAndUpdateInput() {
                this.completion = cmd.parse(this.input).getCompletions().stream().findFirst().orElse("");
                updateInput();
            }

            private void updateInput() {
                CodexNaturalisTuiApplication.this.updateState(state -> {
                    state.input = this.input;
                    state.completion = this.completion;
                });
            }
        });
        keyboardListenerThread.start();
    }

    @Override
    Component compose(State state) {
        return Flex.column(List.of(
                // top part of screen
                new FlexChild.Flexible(1, Centered.both(
                        new Column(List.of(
                                Flex.row(List.of(
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new CardComponent()
                                        )),
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new CardComponent()
                                        ))
                                )),
                                Centered.horizontally(Padding.around(1, new Text("last key: " + state.lastKey))),
                                Centered.horizontally(Padding.around(1, new Text("last command output: " + state.output)))
                        )))
                ),

                // bottom input
                new FlexChild.Fixed(new Border(BorderStyle.DEFAULT,
                        Flex.row(List.of(
                                new FlexChild.Fixed(new Text("> " + state.input)),
                                new FlexChild.Fixed(new Cursor()),
                                new FlexChild.Flexible(1, new Text(state.completion))
                        ))
                ))
        ));
    }

    public static class State extends TuiApplication.State {
        public String input = "";
        public String completion = "";
        public String output = "";
        public Key lastKey;
    }
}
