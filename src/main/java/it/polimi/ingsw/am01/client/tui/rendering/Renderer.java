package it.polimi.ingsw.am01.client.tui.rendering;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.Ansi;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawBuffer;
import it.polimi.ingsw.am01.client.tui.terminal.ResizeEvent;
import it.polimi.ingsw.am01.client.tui.terminal.Terminal;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {
    private final Terminal terminal;
    private final EventEmitter.Registration registration;
    private final Component rootComponent;
    private SizedPositioned rootLayout;
    private final ExecutorService executorService;
    private boolean debugViewEnabled;

    public Renderer(Terminal terminal, ComponentBuilder rootComponentBuilder) {
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        // init properties and start rendering thread
        this.terminal = terminal;
        this.rootComponent = buildRootComponent(rootComponentBuilder);
        this.rootLayout = null;
        this.debugViewEnabled = false;
        this.executorService = Executors.newSingleThreadExecutor();

        // start rendering
        this.terminal.enableRawMode();
        this.registration = this.terminal.on(ResizeEvent.class, event -> this.render());
        this.render();
    }

    private Component buildRootComponent(ComponentBuilder rootComponentBuilder) {
        BuildContext buildContext = new BuildContext(this);
        return rootComponentBuilder.build(buildContext);
    }

    public boolean isDebugViewEnabled() {
        return debugViewEnabled;
    }

    public void setDebugViewEnabled(boolean debugViewEnabled) {
        this.debugViewEnabled = debugViewEnabled;
    }

    private void onShutdown() {
        this.terminal.unregister(registration);
        this.terminal.disableRawMode();
        this.executorService.shutdownNow();

        // clean up the terminal screen
        System.out.println(Ansi.setCursorPosition(0, 0) + Ansi.eraseInDisplay);
        System.out.flush();
    }

    public void runOnRenderThread(Runnable task) {
        executorService.submit(task);
    }

    public void render() {
        this.executorService.submit(this::renderTask);
    }

    private void renderTask() {
        if (rootLayout != null) {
            rootLayout.offScreen();
        }

        Dimensions terminalDimensions = this.terminal.getDimensions();
        Dimensions drawDimensions = this.debugViewEnabled
                ? terminalDimensions.shrink(0, 3)
                : terminalDimensions;

        long t0 = System.nanoTime(); // begin layout

        this.rootLayout = rootComponent
                .layout(Constraint.max(drawDimensions))
                .placeAt(Position.ZERO);

        long t1 = System.nanoTime(); // done layout, begin drawing

        RenderingContext rootRenderingContext = new RenderingContext(
                new RenderingContext.Global(drawDimensions),
                new RenderingContext.Local(Position.ZERO)
        );
        DrawBuffer drawBuffer = new DrawBuffer(drawDimensions, ' ');
        rootLayout.onScreen(rootRenderingContext, drawBuffer.getDrawArea());

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
}
