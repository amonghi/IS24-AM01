package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class CardComponent implements Component {
    @Override
    public Sized layout(Constraint constraint) {
        Dimensions d = Dimensions.constrained(constraint, 23, 9);
        return new Sized(this, d, List.of());
    }

    @Override
    public void drawSelf(DrawArea a) {
        // TODO: render a card for real
        String[] s = """
                ╭───┬────┬───┬────┬───╮
                │   │    │ 3 │    │ A │
                ├───┘    └───┘    └───┤
                │                     │
                │        A I P        │
                │                     │
                ├───┐   ┌─────┐   ┌───┤
                │ F │   │FFFFF│   │   │
                ╰───┴───┴─────┴───┴───╯
                """
                .split("\n");

        for (int y = 0; y < s.length && y < a.dimensions().height(); y++) {
            char[] line = s[y].toCharArray();
            for (int x = 0; x < line.length && x < a.dimensions().width(); x++) {
                char c = line[x];
                a.draw(x, y, c);
            }
        }
    }
}
