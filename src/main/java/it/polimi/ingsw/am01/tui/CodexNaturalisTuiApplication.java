package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.tui.command.CommandNode;
import it.polimi.ingsw.am01.tui.command.SequenceBuilder;
import it.polimi.ingsw.am01.tui.command.WordArgumentParser;
import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.component.Composition;
import it.polimi.ingsw.am01.tui.component.elements.*;
import it.polimi.ingsw.am01.tui.component.layout.Centered;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.Padding;
import it.polimi.ingsw.am01.tui.component.layout.Row;
import it.polimi.ingsw.am01.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.component.prop.DynProp;
import it.polimi.ingsw.am01.tui.component.prop.Prop;
import it.polimi.ingsw.am01.tui.keyboard.Key;
import it.polimi.ingsw.am01.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.tui.rendering.Renderer;
import it.polimi.ingsw.am01.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.tui.rendering.ansi.GraphicalRenditionProperty;

import java.util.Optional;

public class CodexNaturalisTuiApplication extends Composition {
    private final Renderer renderer;

    private final DynProp<String> input;
    private final DynProp<String> output;
    private final DynProp<Key> lastKey;
    private final Prop<CommandNode.Result> parseResult;

    public static ComponentBuilder builder() {
        return CodexNaturalisTuiApplication::new;
    }

    private CodexNaturalisTuiApplication(BuildContext ctx) {
        super(ctx);
        this.renderer = ctx.getRenderer();

        this.input = new DynProp<>("");
        this.output = new DynProp<>("");
        this.lastKey = new DynProp<>(null);

        CommandNode rootCmd = CommandBuilder.root()
                .branch(
                        SequenceBuilder
                                .literal("ping")
                                .thenWhitespace()
                                .thenLiteral("pong")
                                .executes(commandContext -> output.set("selected: ping pong"))
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("bing")
                                .thenWhitespace()
                                .thenLiteral("bong")
                                .executes(commandContext -> output.set("selected: ping pong"))
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("hello")
                                .thenWhitespace()
                                .then(new WordArgumentParser("name"))
                                .executes(commandContext -> output.set("selected: hello, with name=" + commandContext.getString("name")))
                                .end()
                )
                .build();
        this.parseResult = input.map(rootCmd::parse);

        Keyboard keyboard = Keyboard.getInstance();
        keyboard.onAny(key -> this.renderer.runOnRenderThread(() -> lastKey.set(key)));

        keyboard.on(Key.Alt.class, key -> {
            // ALT+D toggles debug
            if (key.character() == 'd') {
                this.toggleDebug();
            }
        });
        keyboard.on(Key.Ctrl.class, key -> {
            // CTRL+C or CTRL+Q or CTRL+D quits the application
            switch (key.character()) {
                case 'c', 'q', 'd' -> this.quitApplication();
            }
        });
        keyboard.on(Key.Character.class, key -> this.writeChar(key.character()));
        keyboard.on(Key.Backspace.class, event -> this.eraseChar());
        keyboard.on(Key.Del.class, event -> this.eraseChar());
        keyboard.on(Key.Tab.class, key -> this.writeCompletion());
        keyboard.on(Key.Enter.class, key -> this.runCommand());
    }

    private void quitApplication() {
        System.exit(0);
    }

    private void toggleDebug() {
        this.renderer.runOnRenderThread(() -> renderer.setDebugViewEnabled(!renderer.isDebugViewEnabled()));
    }

    private void writeChar(char c) {
        this.renderer.runOnRenderThread(() -> {
            input.compute(s -> s + c);
        });
    }

    private void eraseChar() {
        this.renderer.runOnRenderThread(() -> {
            input.compute(s -> s.isEmpty() ? s : s.substring(0, s.length() - 1));
        });
    }

    private void writeCompletion() {
        this.renderer.runOnRenderThread(() -> {
            input.compute(in -> {
                String completion = parseResult.peek().getCompletions().stream().findFirst().orElse("");
                return in + completion;
            });
        });
    }

    private void runCommand() {
        this.renderer.runOnRenderThread(() -> {
            Optional<Runnable> runnable = parseResult.peek().getCommandRunnable();
            if (runnable.isPresent()) {
                runnable.get().run();
                input.set("");
            }
        });
    }

    @Override
    protected ComponentBuilder compose() {
        return Flex.column(
                // top part of screen
                new FlexChild.Flexible(1, Centered.both(
                        Column.of(
                                Flex.row(
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                CardComponent.builder()
                                        )),
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                CardComponent.builder()
                                        ))
                                ),
                                Centered.horizontally(Padding.around(1, Text.of(lastKey.map(key -> "last key: " + key)))),
                                Centered.horizontally(Padding.around(1, Text.of(output.map(s -> "last command output: " + s)))),
                                Centered.horizontally(Padding.around(2, Row.of(
                                        Text.of("Some"),
                                        Text.of(" "),
                                        Text.of("red").withRendition(
                                                GraphicalRendition.DEFAULT
                                                        .withForeground(GraphicalRenditionProperty.ForegroundColor.RED)
                                        ),
                                        Text.of(" "),
                                        Text.of("bold").withRendition(
                                                GraphicalRendition.DEFAULT
                                                        .withForeground(GraphicalRenditionProperty.ForegroundColor.RED)
                                                        .withWeight(GraphicalRenditionProperty.Weight.BOLD)
                                        ),
                                        Text.of(" "),
                                        Text.of("text").withRendition(
                                                GraphicalRendition.DEFAULT
                                                        .withForeground(GraphicalRenditionProperty.ForegroundColor.RED)
                                                        .withWeight(GraphicalRenditionProperty.Weight.BOLD)
                                                        .withItalics(GraphicalRenditionProperty.Italics.ON)
                                        )
                                ))),
                                Centered.horizontally(Padding.around(2, KeypressCounterComponent.builder()))
                        )
                )),

                // bottom input
                new FlexChild.Fixed(Border.around(
                        Flex.row(
                                new FlexChild.Fixed(
                                        Text.of(input.map(s -> "> " + s))
                                                .withRendition(
                                                        GraphicalRendition.DEFAULT
                                                                .withWeight(GraphicalRenditionProperty.Weight.BOLD)
                                                )
                                ),
                                new FlexChild.Fixed(Cursor.here()),
                                new FlexChild.Flexible(1,
                                        Text.of(parseResult.map(result -> result.getCompletions().stream().findFirst().orElse("")))
                                                .withRendition(
                                                        GraphicalRendition.DEFAULT
                                                                .withWeight(GraphicalRenditionProperty.Weight.DIM)
                                                )
                                )
                        )
                ))
        );
    }
}
