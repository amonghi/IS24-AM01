package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Position;
import it.polimi.ingsw.am01.tui.rendering.SizedPositioned;
import it.polimi.ingsw.am01.tui.rendering.draw.CharBufferDrawArea;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;
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
    }

    public void shutdown() {
        this.terminal.unregister(registration);
        this.terminal.disableRawMode();
        this.renderService.shutdown();
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
                ? terminalDimensions
                : terminalDimensions.shrink(0, 3);
        DrawArea drawArea = new CharBufferDrawArea(drawDimensions, ' ');

        long t0 = System.nanoTime(); // start time

        Component rootComponent = this.compose(this.state);

        long t1 = System.nanoTime(); // done composing, begin layout

        SizedPositioned layoutRoot = rootComponent
                .layout(Constraint.max(drawDimensions))
                .placeAt(Position.ZERO);

        long t2 = System.nanoTime(); // done layout, begin drawing

        layoutRoot.draw(drawArea);

        long t3 = System.nanoTime(); // done drawing

        long build = (t1 - t0) / 1000;
        long layout = (t2 - t1) / 1000;
        long draw = (t3 - t2) / 1000;

        // print
        StringBuilder builder = new StringBuilder();
        builder
                .append("\033[H\033[2J") // clear screen and move cursor to 0,0
                .append(drawArea);

        if (this.state.debugEnabled) {
            builder
                    .append("build: ").append(build).append(" ms\r\n")
                    .append("layout: ").append(layout).append(" ms\r\n")
                    .append("draw: ").append(draw).append(" ms\r\n");
        }

        System.out.println(builder);
    }

    abstract Component compose(S state);

    public static class State {
        public boolean debugEnabled = false;
    }
}
