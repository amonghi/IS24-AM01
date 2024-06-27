package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.Ansi;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawBuffer;
import it.polimi.ingsw.am01.client.tui.terminal.ResizeEvent;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.eventemitter.Event;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.game.GameStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base class that contains the logic for rendering the TUI.
 * It is responsible for managing the rendering thread and the terminal.
 * It also provides a debug view that shows the time taken for layout and drawing.
 */
public abstract class BaseTuiView extends View {
    private final ExecutorService executorService;
    private final Terminal terminal;
    private final EventEmitter.Registration terminalRegistration;
    private final EventEmitter.Registration selfRegistration;
    private boolean debugViewEnabled;

    /**
     * Creates a new TUI view.
     *
     * @param terminal the terminal to use
     */
    public BaseTuiView(Terminal terminal) {
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        // init properties and start rendering thread
        this.terminal = terminal;
        this.debugViewEnabled = false;
        this.executorService = Executors.newSingleThreadExecutor();

        this.terminal.enableRawMode();
        this.terminalRegistration = this.terminal.on(ResizeEvent.class, onRenderThread(event -> this.render()));

        // we don't really care what the update is because we re-render everything anyway
        this.selfRegistration = this.onAny(event -> this.render());
    }

    /**
     * @return whether the debug view is enabled
     */
    public boolean isDebugViewEnabled() {
        return debugViewEnabled;
    }

    /**
     * Sets whether the debug view is enabled.
     *
     * @param debugViewEnabled whether the debug view is enabled
     */
    public void setDebugViewEnabled(boolean debugViewEnabled) {
        this.debugViewEnabled = debugViewEnabled;
    }

    /**
     * This method is called when the application is shutting down and should be used to clean up resources.
     * It is called by the shutdown hook, so it will run in a separate thread.
     * <p>
     * This method should be overridden by subclasses to clean up resources.
     */
    protected void onShutdown() {
        this.unregister(this.selfRegistration);
        this.terminal.unregister(terminalRegistration);
        this.terminal.disableRawMode();
        this.executorService.shutdownNow();

        // clean up the terminal screen
        System.out.println(Ansi.setCursorPosition(0, 0) + Ansi.eraseInDisplay);
        System.out.flush();
    }

    /**
     * Renders the view.
     * <p>
     * This method should always be called on the rendering thread.
     * It will compose the view (by calling {@link #compose()}),
     * calculate the layout, draw it and print it to the terminal.
     * <p>
     * If the debug view is enabled, it will also print the time taken for layout and drawing
     * (at the bottom of the screen).
     */
    protected void render() {
        Dimensions terminalDimensions = this.terminal.getDimensions();
        Dimensions drawDimensions = this.debugViewEnabled
                ? terminalDimensions.shrink(0, 3)
                : terminalDimensions;

        long t0 = System.nanoTime(); // begin layout

        Component root = this.compose();
        root.layout(Constraint.max(drawDimensions));
        root.setPosition(Position.ZERO);

        long t1 = System.nanoTime(); // done layout, begin drawing

        RenderingContext rootRenderingContext = new RenderingContext(
                new RenderingContext.Global(drawDimensions),
                new RenderingContext.Local(Position.ZERO)
        );
        DrawBuffer drawBuffer = new DrawBuffer(drawDimensions, ' ');
        root.draw(rootRenderingContext, drawBuffer.getDrawArea());

        long t2 = System.nanoTime(); // done drawing

        // print
        StringBuilder builder = new StringBuilder();
        builder
                .append(Ansi.setCursorPosition(0, 0))
                .append(Ansi.eraseInDisplay)
                .append(drawBuffer);

        if (this.debugViewEnabled) {
            long layout = (t1 - t0) / 1000;
            long draw = (t2 - t1) / 1000;

            builder
                    .append("\r\n")
                    .append("layout: ").append(layout).append(" ms\r\n")
                    .append("draw: ").append(draw);
        }

        // position cursor
        Position cursorPosition = rootRenderingContext.global().getCursorPosition();
        builder.append(Ansi.setCursorPosition(cursorPosition.y(), cursorPosition.x()));

        System.out.print(builder);
        System.out.flush();
    }

    /**
     * Composes the view.
     * <p>
     * This method should be overridden by subclasses to compose the view.
     * It should return the root component of the view.
     *
     * @return the root component of the view
     */
    protected abstract Component compose();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void changeStage(ClientState state, GameStatus gameStatus) {
        this.render();
    }

    /**
     * Runs a {@link Runnable} on the rendering thread.
     *
     * @param runnable The {@link Runnable} that has to be run on the rendering thread.
     */
    @Override
    public void runLater(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    /**
     * A helper method that wraps an {@link EventListener} so that it is run on the rendering thread.
     *
     * @param listener The {@link EventListener} that has to be run on the rendering thread.
     * @param <E>      The type of the event.
     * @return The wrapped {@link EventListener}.
     */
    protected <E extends Event> EventListener<E> onRenderThread(EventListener<E> listener) {
        return event -> this.runLater(() -> listener.onEvent(event));
    }
}
