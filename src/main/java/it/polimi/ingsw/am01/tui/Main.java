package it.polimi.ingsw.am01.tui;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.elements.Border;
import it.polimi.ingsw.am01.tui.component.elements.BorderStyle;
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
                    int screenWidth = 100 + (int) ((Math.sin((0.1 * (double) time)) + 1) * 20);

                    // build screen
                    Dimensions screenDimensions = new Dimensions(screenWidth, 15);
                    CharBufferDrawArea screen = new CharBufferDrawArea(screenDimensions, ' ');

                    Component component =
                            new Border(BorderStyle.DEFAULT,
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
                                                                            new Text("Screen width: " + screenWidth)
                                                                    )
                                                            ))
                                                    ))
                                            ))
                                    )
                            );

                    Sized sized = component.layout(Constraint.max(screenDimensions));
                    sized.placeAt(Position.ZERO).draw(screen);

                    // clear and print
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
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
