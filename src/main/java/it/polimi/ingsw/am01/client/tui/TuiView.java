package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.tui.command.CommandBuilder;
import it.polimi.ingsw.am01.client.tui.command.CommandNode;
import it.polimi.ingsw.am01.client.tui.command.parser.Parser;
import it.polimi.ingsw.am01.client.tui.commands.*;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Cursor;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Border;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.Flex;
import it.polimi.ingsw.am01.client.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.client.tui.keyboard.Key;
import it.polimi.ingsw.am01.client.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.client.tui.scenes.*;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.model.card.Side;

import java.util.ArrayList;
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
            ResumeGameCommand::new,
            SelectStartingCardSideCommand::new,
            SelectColorCommand::new,
            SelectObjectiveCommand::new,
            PlaceCardCommand::new,
            DrawCardCommand::new,
            SetBoardVisibilityCommand::new,
            SetObjectivesVisibilityCommand::new,
            SetVisibleCardSideCommand::new,
            ExitFinishedGameCommand::new,
            SetChatVisibilityCommand::new,
            SendMessageCommand::new,
            ChangeFocusedPlayerCommand::new,
            QuitCommand::new
    );

    private final Keyboard keyboard;
    private final CommandNode rootCmd;
    private final List<Registration> keyboardRegistrations;

    private String input = "";
    private boolean chatVisible = false;
    private boolean isBoardVisible = false;
    private boolean areObjectivesVisible = false;
    private String focusedPlayer = null;
    private int playAreaScrollX = 0;
    private int playAreaScrollY = 0;

    private final List<Side> visibleSides;

    public TuiView(Terminal terminal) {
        super(terminal);
        this.keyboard = Keyboard.getInstance();
        this.visibleSides = new ArrayList<>(List.of(
                Side.FRONT,
                Side.FRONT,
                Side.FRONT
        ));

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
                keyboard.on(Key.Enter.class, onRenderThread(key -> this.runCommand())),

                keyboard.on(Key.Arrow.class, onRenderThread(event -> this.movePlayArea(event.direction())))
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

    public Optional<String> getFocusedPlayer() {
        return Optional.ofNullable(focusedPlayer);
    }

    public void setFocusedPlayer(String playerName) {
        this.focusedPlayer = playerName;
        render();
    }

    public int getPlayAreaScrollX() {
        return playAreaScrollX;
    }

    public int getPlayAreaScrollY() {
        return playAreaScrollY;
    }

    private void movePlayArea(Key.Arrow.Direction direction) {
        if (!ChangeFocusedPlayerCommand.isCorrectState(this)) {
            return;
        }

        switch (direction) {
            case UP -> this.playAreaScrollY += 2;
            case DOWN -> this.playAreaScrollY -= 2;
            case LEFT -> this.playAreaScrollX += 2;
            case RIGHT -> this.playAreaScrollX -= 2;
        }

        this.render();
    }

    public Component compose() {
        CommandNode.Result parseResult = this.parseInput();
        String whitePart = this.input.substring(0, parseResult.getConsumed());
        String redPart = this.input.substring(parseResult.getConsumed());
        String completion = parseResult.getCompletions().stream()
                .findFirst()
                .map(Parser.Completion::text)
                .orElse("");

        List<FlexChild> children = new ArrayList<>();

        // top part of screen
        children.add(
                new FlexChild.Flexible(1,
                        switch (this.getState()) {
                            case NOT_CONNECTED -> new WelcomeScene();
                            case NOT_AUTHENTICATED -> new AuthScene();
                            case AUTHENTICATED -> new GamesListScene(this);
                            case IN_GAME -> switch (getGameStatus()) {
                                case AWAITING_PLAYERS -> new LobbyScene(this);
                                case SETUP_STARTING_CARD_SIDE -> new SelectStartingCardSideScene(this);
                                case SETUP_COLOR -> new SelectColorScene(this);
                                case SETUP_OBJECTIVE -> new SelectObjectiveScene(this);
                                case PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED -> new PlayAreaScene(this);
                                case FINISHED -> new EndingScene(this);
                                case RESTORING -> new RestoringScene(this);
                            };
                        }
                )
        );
        // bottom input
        children.add(
                new FlexChild.Fixed(new Border(Line.Style.DEFAULT,
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
        );

        return Flex.column(children);
    }

    public boolean areObjectivesVisible() {
        return areObjectivesVisible;
    }

    public boolean isBoardVisible() {
        return isBoardVisible;
    }

    public boolean isChatVisible() {
        return chatVisible;
    }

    public void showObjectives() {
        this.areObjectivesVisible = true;
        this.isBoardVisible = false;
        render();
    }

    public void hideObjectives() {
        this.areObjectivesVisible = false;
        render();
    }

    public void showBoard() {
        this.areObjectivesVisible = false;
        this.isBoardVisible = true;
        render();
    }

    public void hideBoard() {
        this.isBoardVisible = false;
        render();
    }

    public void showChat() {
        chatVisible = true;
        render();
    }

    public void hideChat() {
        chatVisible = false;
        render();
    }

    public void swapCard(int cardIndex) {
        Side newSide = switch (visibleSides.get(cardIndex)) {
            case FRONT -> Side.BACK;
            case BACK -> Side.FRONT;
        };

        visibleSides.set(cardIndex, newSide);
        render();
    }

    public Side getVisibleSideOf(int cardIndex) {
        return visibleSides.get(cardIndex);
    }

    public void clearData() {
        super.clearData();
        chatVisible = false;
        isBoardVisible = false;
        areObjectivesVisible = false;
        focusedPlayer = null;
        playAreaScrollX = 0;
        playAreaScrollY = 0;
        visibleSides.clear();
        visibleSides.addAll(List.of(
                Side.FRONT,
                Side.FRONT,
                Side.FRONT
        ));
    }
}
