package it.polimi.ingsw.am01.client.tui;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.*;
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

public abstract class BaseTuiView extends View {
    private final ExecutorService executorService;
    private final Terminal terminal;
    private final EventEmitter.Registration terminalRegistration;
    private final EventEmitter.Registration selfRegistration;
    private boolean debugViewEnabled;

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

    public boolean isDebugViewEnabled() {
        return debugViewEnabled;
    }

    public void setDebugViewEnabled(boolean debugViewEnabled) {
        this.debugViewEnabled = debugViewEnabled;
    }

    protected void onShutdown() {
        this.unregister(this.selfRegistration);
        this.terminal.unregister(terminalRegistration);
        this.terminal.disableRawMode();
        this.executorService.shutdownNow();

        // clean up the terminal screen
        System.out.println(Ansi.setCursorPosition(0, 0) + Ansi.eraseInDisplay);
        System.out.flush();
    }

    protected void render() {
        Dimensions terminalDimensions = this.terminal.getDimensions();
        Dimensions drawDimensions = this.debugViewEnabled
                ? terminalDimensions.shrink(0, 3)
                : terminalDimensions;

        long t0 = System.nanoTime(); // begin layout

        SizedPositioned rootLayout = this.compose()
                .layout(Constraint.max(drawDimensions))
                .placeAt(Position.ZERO);

        long t1 = System.nanoTime(); // done layout, begin drawing

        RenderingContext rootRenderingContext = new RenderingContext(
                new RenderingContext.Global(drawDimensions),
                new RenderingContext.Local(Position.ZERO)
        );
        DrawBuffer drawBuffer = new DrawBuffer(drawDimensions, ' ');
        rootLayout.draw(rootRenderingContext, drawBuffer.getDrawArea());

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

    protected abstract Component compose();

    @Override
    protected void changeStage(ClientState state, GameStatus gameStatus) {
        this.render();
    }

    @Override
    protected void kickPlayer() {
        this.render();
    }

    @Override
    protected void showConnectionErrorMessage(String errorMessage) {
        this.render();
    }

    @Override
    public void runLater(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    protected <E extends Event> EventListener<E> onRenderThread(EventListener<E> listener) {
        return event -> this.runLater(() -> listener.onEvent(event));
    }
}
