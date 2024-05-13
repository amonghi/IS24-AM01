package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.elements.*;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexChild;
import it.polimi.ingsw.am01.tui.component.layout.flex.FlexRow;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Position;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.CharBufferDrawArea;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            int time = 0;

            @Override
            public void run() {
                while (true) {
                    // change the scree with time to show that it is adapting
                    time++;
                    int screenWidth = 35 + (int) ((Math.sin((0.1 * (double) time)) + 1) * 20);

                    // build screen
                    Dimensions screenDimensions = new Dimensions(screenWidth, 10);
                    CharBufferDrawArea screen = new CharBufferDrawArea(screenDimensions, '~');

                    Component component =
                            new FlexRow(List.of(
                                    new FlexChild.Flexible(1, new Container(1, BorderStyle.DEFAULT, 1, new Text("1/2 grow"))),
                                    new FlexChild.Fixed(new Border(BorderStyle.DEFAULT, new Text("Fixed"))),
                                    new FlexChild.Flexible(1, new Border(BorderStyle.DEFAULT, new Text("1/2 fixed"))),
                                    new FlexChild.Fixed(new Rectangle(3, 3, 'X'))
                            ));
                    Sized sized = component.layout(Constraint.max(screenDimensions));
                    sized.placeAt(Position.ZERO).draw(screen);

                    // clear and print
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println(screenWidth);
                    System.out.println(screen);

                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        thread.start();
    }
}
