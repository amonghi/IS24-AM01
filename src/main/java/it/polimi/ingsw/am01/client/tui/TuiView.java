package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.client.tui.command.WordArgumentParser;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.*;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.component.layout.Padding;
import it.polimi.ingsw.am01.client.tui.component.layout.Row;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.keyboard.Key;
import it.polimi.ingsw.am01.client.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;

import java.util.List;
import java.util.Optional;

public class TuiView extends BaseTuiView {
    private final Keyboard keyboard;
    private final CommandNode rootCmd;
    private final List<Registration> keyboardRegistrations;

    private String input = "";
    private CommandNode.Result parseResult = null;
    private String output = "";
    private Key lastKey;

    public TuiView(Terminal terminal) {
        super(terminal);
        this.keyboard = Keyboard.getInstance();

        this.rootCmd = CommandBuilder.root()
                .branch(
                        SequenceBuilder
                                .literal("ping")
                                .thenWhitespace()
                                .thenLiteral("pong")
                                .executes(commandContext -> this.output = "selected: ping pong")
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("bing")
                                .thenWhitespace()
                                .thenLiteral("bong")
                                .executes(commandContext -> this.output = "selected: ping pong")
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("hello")
                                .thenWhitespace()
                                .then(new WordArgumentParser("name"))
                                .executes(commandContext -> this.output = "selected: hello, with name=" + commandContext.getString("name"))
                                .end()
                )
                .build();

        this.keyboardRegistrations = List.of(
                keyboard.onAny(onRenderThread(key -> {
                    // TODO: delete
                    this.lastKey = key;
                    this.render();
                })),

                keyboard.on(Key.Alt.class, onRenderThread(key -> {
                    // ALT+D toggles debug
                    if (key.character() == 'd') {
                        this.toggleDebug();
                    }
                })),
                keyboard.on(Key.Ctrl.class, onRenderThread(key -> {
                    // CTRL+C or CTRL+Q or CTRL+D quits the application
                    switch (key.character()) {
                        case 'c', 'q', 'd' -> this.quitApplication();
                    }
                })),
                keyboard.on(Key.Character.class, onRenderThread(key -> this.writeChar(key.character()))),
                keyboard.on(Key.Backspace.class, onRenderThread(event -> this.eraseChar())),
                keyboard.on(Key.Del.class, onRenderThread(event -> this.eraseChar())),
                keyboard.on(Key.Tab.class, onRenderThread(key -> this.writeCompletion())),
                keyboard.on(Key.Enter.class, onRenderThread(key -> this.runCommand()))
        );
    }

    @Override
    protected void onShutdown() {
        this.keyboardRegistrations.forEach(keyboard::unregister);
        super.onShutdown();
    }

    private void quitApplication() {
        System.exit(0);
    }

    private void toggleDebug() {
        this.setDebugViewEnabled(!this.isDebugViewEnabled());
        this.render();
    }

    private void writeChar(char c) {
        this.input += c;
        this.parseResult = rootCmd.parse(this.input);
        this.render();
    }

    private void eraseChar() {
        if (!this.input.isEmpty()) {
            this.input = this.input.substring(0, this.input.length() - 1);
        }
        this.parseResult = rootCmd.parse(this.input);
        this.render();
    }

    private void writeCompletion() {
        String completion = this.parseResult.getCompletions().stream().findFirst().orElse("");
        this.input = this.input + completion;
        this.parseResult = rootCmd.parse(this.input);
        this.render();
    }

    private void runCommand() {
        Optional<Runnable> runnable = this.parseResult.getCommandRunnable();
        if (runnable.isPresent()) {
            runnable.get().run();
            this.input = "";
            this.parseResult = rootCmd.parse(this.input);
        }
        this.render();
    }

    public Component compose() {
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
                                Centered.horizontally(Padding.around(1, new Text("last key: " + this.lastKey))),
                                Centered.horizontally(Padding.around(1, new Text("last command output: " + this.output))),
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
                                        "> " + this.input
                                )),
                                new FlexChild.Fixed(new Cursor()),
                                new FlexChild.Flexible(1, new Text(
                                        GraphicalRendition.DEFAULT
                                                .withWeight(GraphicalRenditionProperty.Weight.DIM),
                                        Optional.ofNullable(this.parseResult)
                                                .flatMap(result -> result.getCompletions().stream().findFirst())
                                                .orElse("")
                                ))
                        ))
                ))
        ));
    }
}
