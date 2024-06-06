package it.polimi.ingsw.am01.client.tui.rendering;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

import java.util.List;

public record SizedPositioned(
        Component component,
        Dimensions dimensions,
        Position position,
        List<SizedPositioned> children
) {
    public void draw(RenderingContext ctx, DrawArea parentArea) {
        RenderingContext newCtx = new RenderingContext(
                ctx.global(),
                new RenderingContext.Local(ctx.local().getOffset().add(this.position()))
        );
        DrawArea da = parentArea.getSubarea(position, dimensions);
        this.component.drawSelf(newCtx, da);

        for (SizedPositioned child : children) {
            child.draw(newCtx, da);
        }
    }
}
