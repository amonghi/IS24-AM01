package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.commands.*;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Border;
import it.polimi.ingsw.am01.client.tui.component.elements.BorderStyle;
import it.polimi.ingsw.am01.client.tui.component.elements.Cursor;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.keyboard.Key;
import it.polimi.ingsw.am01.client.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.scenes.AuthScene;
import it.polimi.ingsw.am01.client.tui.scenes.GamesListScene;
import it.polimi.ingsw.am01.client.tui.scenes.LobbyScene;
import it.polimi.ingsw.am01.client.tui.scenes.WelcomeScene;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TuiView extends BaseTuiView {
    private static final List<Function<TuiView, TuiCommand>> CMD_CONSTRUCTORS = List.of(
            ConnectCommand::new,
            AuthenticateCommand::new,
            JoinCommand::new,
            CreateGameCommand::new,
            StartGameCommand::new,
            QuitCommand::new
    );

    private final Keyboard keyboard;
    private final CommandNode rootCmd;
    private final List<Registration> keyboardRegistrations;

    private String input = "";

    public TuiView(Terminal terminal) {
        super(terminal);
        this.keyboard = Keyboard.getInstance();

        // build the command tree
        CommandBuilder builder = CommandBuilder.root();
        for (Function<TuiView, TuiCommand> constructor : CMD_CONSTRUCTORS) {
            CommandNode commandNode = constructor.apply(this).getRootNode();
            builder.branch(commandNode);
        }
        this.rootCmd = builder.build();

        // listen to keyboard
        this.keyboardRegistrations = List.of(
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

        // start rendering
        this.runLater(this::render);
    }

    @Override
    protected void onShutdown() {
        this.keyboardRegistrations.forEach(keyboard::unregister);
        super.onShutdown();
    }

    public void quitApplication() {
        System.exit(0);
    }

    private void toggleDebug() {
        this.setDebugViewEnabled(!this.isDebugViewEnabled());
        this.render();
    }

    private void writeChar(char c) {
        this.input += c;
        this.render();
    }

    private void eraseChar() {
        if (!this.input.isEmpty()) {
            this.input = this.input.substring(0, this.input.length() - 1);
        }
        this.render();
    }

    private void writeCompletion() {
        String autocompletableString = this.parseInput().getCompletions().stream()
                .findFirst()
                .map(completion -> completion.text().substring(0, completion.autoWritableChars()))
                .orElse("");
        this.input = this.input + autocompletableString;
        this.render();
    }

    private void runCommand() {
        Optional<Runnable> runnable = this.parseInput().getCommandRunnable();
        if (runnable.isPresent()) {
            runnable.get().run();
            this.input = "";
        }
        this.render();
    }

    private CommandNode.Result parseInput() {
        return this.rootCmd.parse(this.input);
    }

    public Component compose() {
        CommandNode.Result parseResult = this.parseInput();
        String whitePart = this.input.substring(0, parseResult.getConsumed());
        String redPart = this.input.substring(parseResult.getConsumed());
        String completion = parseResult.getCompletions().stream()
                .findFirst()
                .map(Parser.Completion::text)
                .orElse("");

        return Flex.column(List.of(
                // top part of screen
                new FlexChild.Flexible(1, Centered.both(
                        switch (this.getState()) {
                            case NOT_CONNECTED -> new WelcomeScene();
                            case NOT_AUTHENTICATED -> new AuthScene();
                            case AUTHENTICATED -> new GamesListScene(this);
                            case IN_GAME -> switch (getGameStatus()) {
                                case AWAITING_PLAYERS -> new LobbyScene(this);
                                case SETUP_STARTING_CARD_SIDE -> new Text("SETUP_STARTING_CARD_SIDE");
                                case SETUP_COLOR -> new Text("SETUP_COLOR");
                                case SETUP_OBJECTIVE -> new Text("SETUP_OBJECTIVE");
                                case PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED ->
                                        new Text("PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED");
                                case FINISHED -> new Text("FINISHED");
                                case RESTORING -> new Text("RESTORING");
                            };
                        }
                )),

                // bottom input
                new FlexChild.Fixed(new Border(BorderStyle.DEFAULT,
                        Flex.row(List.of(
                                new FlexChild.Fixed(new Text(
                                        GraphicalRendition.DEFAULT
                                                .withForeground(GraphicalRenditionProperty.ForegroundColor.WHITE),
                                        "> " + whitePart
                                )),
                                new FlexChild.Fixed(new Text(
                                        GraphicalRendition.DEFAULT
                                                .withForeground(GraphicalRenditionProperty.ForegroundColor.RED),
                                        redPart
                                )),
                                new FlexChild.Fixed(new Cursor()),
                                new FlexChild.Flexible(1, new Text(
                                        GraphicalRendition.DEFAULT
                                                .withForeground(GraphicalRenditionProperty.ForegroundColor.WHITE)
                                                .withWeight(GraphicalRenditionProperty.Weight.DIM),
                                        completion
                                ))
                        ))
                ))
        ));
    }
}
