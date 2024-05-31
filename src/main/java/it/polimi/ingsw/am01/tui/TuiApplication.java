package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.*;
import it.polimi.ingsw.am01.tui.rendering.ansi.Ansi;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawBuffer;
import it.polimi.ingsw.am01.tui.terminal.ResizeEvent;
import it.polimi.ingsw.am01.tui.terminal.Terminal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public abstract class TuiApplication<S extends TuiApplication.State> {
    private final Terminal terminal;
    private final S state;
    private final ExecutorService renderService;
    private final EventEmitter.Registration registration;

    public TuiApplication(Terminal terminal, S initialState) {
        this.terminal = terminal;
        this.state = initialState;
        this.renderService = Executors.newSingleThreadExecutor();

        this.terminal.enableRawMode();

        this.registration = this.terminal.on(ResizeEvent.class, event -> {
            renderService.submit(this::render);
        });

        // first render
        this.renderService.submit(this::render);
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
    }

    private void onShutdown() {
        this.terminal.unregister(registration);
        this.terminal.disableRawMode();
        this.renderService.shutdown();

        // clean up the terminal screen
        System.out.println(Ansi.setCursorPosition(0, 0) + Ansi.eraseInDisplay);
        System.out.flush();
    }

    public void updateState(Consumer<S> stateUpdater) {
        this.renderService.submit(() -> {
            stateUpdater.accept(this.state);
            this.render();
        });
    }

    private void render() {
        Dimensions terminalDimensions = this.terminal.getDimensions();
        Dimensions drawDimensions = this.state.debugEnabled
                ? terminalDimensions.shrink(0, 3)
                : terminalDimensions;

        long t0 = System.nanoTime(); // start time

        Component rootComponent = this.compose(this.state);

        long t1 = System.nanoTime(); // done composing, begin layout

        SizedPositioned layoutRoot = rootComponent
                .layout(Constraint.max(drawDimensions))
                .placeAt(Position.ZERO);

        long t2 = System.nanoTime(); // done layout, begin drawing

        RenderingContext rootRenderingContext = new RenderingContext(
                new RenderingContext.Global(drawDimensions),
                new RenderingContext.Local(Position.ZERO)
        );
        DrawBuffer drawBuffer = new DrawBuffer(drawDimensions, ' ');
        layoutRoot.draw(rootRenderingContext, drawBuffer.getDrawArea());

        long t3 = System.nanoTime(); // done drawing

        // print
        StringBuilder builder = new StringBuilder();
        builder
                .append(Ansi.setCursorPosition(0, 0))
                .append(Ansi.eraseInDisplay)
                .append(drawBuffer);

        if (this.state.debugEnabled) {
            long compose = (t1 - t0) / 1000;
            long layout = (t2 - t1) / 1000;
            long draw = (t3 - t2) / 1000;

            builder
                    .append("\r\n")
                    .append("compose: ").append(compose).append(" ms\r\n")
                    .append("layout: ").append(layout).append(" ms\r\n")
                    .append("draw: ").append(draw);
        }

        // position cursor
        Position cursorPosition = rootRenderingContext.global().getCursorPosition();
        builder.append(Ansi.setCursorPosition(cursorPosition.y(), cursorPosition.x()));

        System.out.print(builder);
        System.out.flush();
    }

    abstract Component compose(S state);

    public static class State {
        public boolean debugEnabled = false;
    }
}
