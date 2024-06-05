package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.event.StateChangedEvent;
import it.polimi.ingsw.am01.client.tui.command.*;
import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.client.tui.component.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Border;
import it.polimi.ingsw.am01.client.tui.component.elements.Cursor;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;
import it.polimi.ingsw.am01.client.tui.component.layout.Show;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.component.prop.DynProp;
import it.polimi.ingsw.am01.client.tui.component.prop.Prop;
import it.polimi.ingsw.am01.client.tui.keyboard.Key;
import it.polimi.ingsw.am01.client.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.client.tui.rendering.Renderer;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.screens.AuthScreen;
import it.polimi.ingsw.am01.client.tui.screens.ConnectScreen;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CodexNaturalisTuiApplication extends Composition {
    private final Renderer renderer;
    private final List<EventEmitter.Registration> registrations;

    private final DynProp<String> input;
    private final Prop<CommandNode.Result> parseResult;
    private final DynProp<View> view;

    public static ComponentBuilder builder() {
        return CodexNaturalisTuiApplication::new;
    }

    private CodexNaturalisTuiApplication(BuildContext ctx) {
        super(ctx);
        this.renderer = ctx.getRenderer();
        this.registrations = new ArrayList<>();

        this.input = new DynProp<>("");
        this.view = new DynProp<>(View.getInstance());

        CommandNode rootCmd = CommandBuilder.root()
                .branch(
                        SequenceBuilder
                                .literal("connect")
                                .thenWhitespace()
                                .then(new EnumParser<>("connectionType", View.ConnectionType.class))
                                .thenWhitespace()
                                .then(new WordArgumentParser("hostname"))
                                .thenWhitespace()
                                .then(new IntParser("port"))
                                .executes(commandContext -> {
                                    View.ConnectionType connectionType = commandContext.getEnum("connectionType", View.ConnectionType.class);
                                    String hostname = commandContext.getString("hostname");
                                    int port = commandContext.getInt("port");

                                    view.mutate(v -> v.connect(connectionType, hostname, port));
                                })
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("authenticate")
                                .thenWhitespace()
                                .then(new WordArgumentParser("playerName"))
                                .executes(commandContext -> {
                                    String playerName = commandContext.getString("playerName");

                                    view.mutate(v -> v.authenticate(playerName));
                                })
                                .end()
                )
                .branch(
                        SequenceBuilder
                                .literal("quit")
                                .executes(commandContext -> this.quitApplication())
                                .end()
                )
                .build();
        this.parseResult = input.map(rootCmd::parse);
    }

    @Override
    public void onScreen() {
        Keyboard keyboard = Keyboard.getInstance();

        this.registrations.addAll(List.of(
                keyboard.on(Key.Alt.class, key -> {
                    // ALT+D toggles debug
                    if (key.character() == 'd') {
                        this.toggleDebug();
                    }
                }),
                keyboard.on(Key.Ctrl.class, key -> {
                    // CTRL+C or CTRL+Q or CTRL+D quits the application
                    switch (key.character()) {
                        case 'c', 'q', 'd' -> this.quitApplication();
                    }
                }),
                keyboard.on(Key.Character.class, key -> this.writeChar(key.character())),
                keyboard.on(Key.Backspace.class, event -> this.eraseChar()),
                keyboard.on(Key.Del.class, event -> this.eraseChar()),
                keyboard.on(Key.Tab.class, key -> this.writeCompletion()),
                keyboard.on(Key.Enter.class, key -> this.runCommand()),

                view.peek().on(StateChangedEvent.class, event -> {
                    this.renderer.runOnRenderThread(this::update);
                })
        ));
    }

    @Override
    public void offScreen() {
        Keyboard keyboard = Keyboard.getInstance();
        this.registrations.forEach(keyboard::unregister);
        this.registrations.clear();
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
                new FlexChild.Flexible(1, Column.of(
                        Show.when(view.map(v -> v.getState().equals(ClientState.NOT_CONNECTED)), ConnectScreen.builder()),
                        Show.when(view.map(v -> v.getState().equals(ClientState.NOT_AUTHENTICATED)), AuthScreen.builder())
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
