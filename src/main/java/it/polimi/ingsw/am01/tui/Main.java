package it.polimi.ingsw.am01.tui;

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

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Terminal unixTerminal = new UnixTerminal();

        unixTerminal.on(ResizeEvent.class, event -> {
            draw(unixTerminal);
        });
        draw(unixTerminal);

        // leave the process hanging to stop the JVM from shutting down
        (new Scanner(System.in)).next();
    }

    private static void draw(Terminal terminal) {
        // clear
        System.out.println("\033[H\033[2J");
        System.out.flush();

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
        System.out.print(screen);
        System.out.flush();
        System.out.println("build: " + build + " ms");
        System.out.println("layout: " + layout + " ms");
        System.out.println("draw: " + draw + " ms");
    }
}
