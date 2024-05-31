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
import it.polimi.ingsw.am01.tui.component.layout.Row;
import it.polimi.ingsw.am01.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.keyboard.Key;
import it.polimi.ingsw.am01.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.tui.terminal.Terminal;

import java.util.List;
import java.util.Optional;

public class CodexNaturalisTuiApplication extends TuiApplication<CodexNaturalisTuiApplication.State> {
    private static final char QUIT_CHAR = 'q';
    private static final char DEBUG_CHAR = 'd';
    private final CommandNode rootCmd;

    public CodexNaturalisTuiApplication(Terminal terminal) {
        super(terminal, new State());

        this.rootCmd = CommandBuilder.root()
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

        Keyboard keyboard = Keyboard.getInstance();
        keyboard.onAny(key -> updateState(state -> state.lastKey = key));

        keyboard.on(Key.Character.class, key -> {
            if (key.ctrl() && key.character() == QUIT_CHAR) {
                this.quitApplication();
                return;
            }

            if (key.ctrl() && key.character() == DEBUG_CHAR) {
                this.toggleDebug();
                return;
            }

            this.writeChar(key.character());
        });
        keyboard.on(Key.Backspace.class, event -> this.eraseChar());
        keyboard.on(Key.Del.class, event -> this.eraseChar());
        keyboard.on(Key.Tab.class, key -> this.writeCompletion());
        keyboard.on(Key.Enter.class, key -> this.runCommand());
    }

    private void quitApplication() {
        System.exit(0);
    }

    private void toggleDebug() {
        updateState(state -> state.debugEnabled = !state.debugEnabled);
    }

    private void writeChar(char c) {
        updateState(state -> {
            state.input += c;
            state.parseResult = rootCmd.parse(state.input);
        });
    }

    private void eraseChar() {
        updateState(state -> {
            if (!state.input.isEmpty()) {
                state.input = state.input.substring(0, state.input.length() - 1);
            }
            state.parseResult = rootCmd.parse(state.input);
        });
    }

    private void writeCompletion() {
        updateState(state -> {
            String completion = state.parseResult.getCompletions().stream().findFirst().orElse("");
            state.input = state.input + completion;
            state.parseResult = rootCmd.parse(state.input);
        });
    }

    private void runCommand() {
        updateState(state -> {
            Optional<Runnable> runnable = state.parseResult.getCommandRunnable();
            if (runnable.isPresent()) {
                runnable.get().run();
                state.input = "";
                state.parseResult = rootCmd.parse(state.input);
            }
        });
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
                                Centered.horizontally(Padding.around(1, new Text("last command output: " + state.output))),
                                Centered.horizontally(Padding.around(2, new Row(List.of(
                                        new Text("Some"),
                                        new Text(" "),
                                        new Text(GraphicalRendition.DEFAULT
                                                .withForeground(GraphicalRenditionProperty.ForegroundColor.RED),
                                                "red"),
                                        new Text(" "),
                                        new Text(GraphicalRendition.DEFAULT
                                                .withForeground(GraphicalRenditionProperty.ForegroundColor.RED)
                                                .withWeight(GraphicalRenditionProperty.Weight.BOLD),
                                                "bold"),
                                        new Text(" "),
                                        new Text(GraphicalRendition.DEFAULT
                                                .withForeground(GraphicalRenditionProperty.ForegroundColor.RED)
                                                .withWeight(GraphicalRenditionProperty.Weight.BOLD)
                                                .withItalics(GraphicalRenditionProperty.Italics.ON),
                                                "text")
                                ))))
                        )))
                ),

                // bottom input
                new FlexChild.Fixed(new Border(BorderStyle.DEFAULT,
                        Flex.row(List.of(
                                new FlexChild.Fixed(new Text(
                                        GraphicalRendition.DEFAULT
                                                .withWeight(GraphicalRenditionProperty.Weight.BOLD),
                                        "> " + state.input
                                )),
                                new FlexChild.Fixed(new Cursor()),
                                new FlexChild.Flexible(1, new Text(
                                        GraphicalRendition.DEFAULT
                                                .withWeight(GraphicalRenditionProperty.Weight.DIM),
                                        state.parseResult.getCompletions().stream().findFirst().orElse("")
                                ))
                        ))
                ))
        ));
    }

    public static class State extends TuiApplication.State {
        public String input = "";
        public CommandNode.Result parseResult = null;
        public String output = "";
        public Key lastKey;
    }
}
