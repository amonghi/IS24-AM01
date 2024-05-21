package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.elements.CardComponent;
import it.polimi.ingsw.am01.tui.component.elements.Text;
import it.polimi.ingsw.am01.tui.component.layout.Centered;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.SpaceAround;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexRow;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Position;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.CharBufferDrawArea;
import it.polimi.ingsw.am01.tui.terminal.ResizeEvent;
import it.polimi.ingsw.am01.tui.terminal.Terminal;
import it.polimi.ingsw.am01.tui.terminal.UnixTerminal;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TuiApplication {
    private static final char EXIT_CHAR = 'q';

    private final Terminal terminal;
    private ExecutorService renderService;

    private char lastPressed;
    private boolean alreadyShutDown;
    private EventEmitter.Registration registration;

    public TuiApplication() {
        this.terminal = new UnixTerminal();
        this.renderService = null;
        this.alreadyShutDown = false;
    }

    public void start() {
        this.terminal.enableRawMode();
        this.renderService = Executors.newSingleThreadExecutor();
        this.renderService.submit(this::draw);

        this.registration = this.terminal.on(ResizeEvent.class, event -> {
            renderService.submit(this::draw);
        });

        Thread keyboardListenerThread = new Thread(() -> {
            while (true) {
                try {
                    int c = System.in.read();
                    if (c == EXIT_CHAR) {
                        this.shutdown();
                        return;
                    }

                    renderService.submit(() -> {
                        this.lastPressed = (char) c;
                        draw();
                    });
                } catch (IOException e) {
                    this.shutdown();
                    return;
                }
            }
        });
        keyboardListenerThread.start();
    }

    public void shutdown() {
        if (this.alreadyShutDown) {
            return;
        }
        this.alreadyShutDown = true;

        if (this.registration != null) {
            this.terminal.unregister(registration);
        }
        if (this.renderService != null) {
            this.renderService.shutdown();
        }
        this.terminal.disableRawMode();
    }

    private void draw() {
        // build screen
        Dimensions screenDimensions = terminal.getDimensions();
        Dimensions drawDimensions = Dimensions.of(screenDimensions.width(), screenDimensions.height() - 4);
        CharBufferDrawArea screen = new CharBufferDrawArea(drawDimensions, ' ');

        long t0 = System.nanoTime();

        Component component =
                Centered.vertically(
                        new Column(List.of(
                                new FlexRow(List.of(
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new CardComponent()
                                        )),
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new CardComponent()
                                        ))
                                )),
                                new FlexRow(List.of(
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new SpaceAround(1, 1, 1, 1,
                                                        new Text("Screen width: " + screenDimensions.width())
                                                )
                                        ))
                                )),
                                new FlexRow(List.of(
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new SpaceAround(1, 1, 1, 1,
                                                        new Text("Screen height: " + screenDimensions.height())
                                                )
                                        ))
                                )),
                                new FlexRow(List.of(
                                        new FlexChild.Flexible(1, Centered.horizontally(
                                                new SpaceAround(1, 1, 1, 1,
                                                        new Text("Keypress: " + lastPressed)
                                                )
                                        ))
                                ))
                        ))
                );

        long t1 = System.nanoTime();

        Sized sized = component.layout(Constraint.max(drawDimensions));

        long t2 = System.nanoTime();

        sized.placeAt(Position.ZERO).draw(screen);

        long t3 = System.nanoTime();

        long build = (t1 - t0) / 1000;
        long layout = (t2 - t1) / 1000;
        long draw = (t3 - t2) / 1000;

        // print
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("\033[H\033[2J")
                .append(screen)
                .append("build: ").append(build).append(" ms\r\n")
                .append("layout: ").append(layout).append(" ms\r\n")
                .append("draw: ").append(draw).append(" ms\r\n");

        System.out.println(stringBuilder);
    }
}
