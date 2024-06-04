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
    public void onScreen(RenderingContext ctx, DrawArea parentArea) {
        this.component.onScreen();

        RenderingContext newCtx = new RenderingContext(
                ctx.global(),
                new RenderingContext.Local(ctx.local().getOffset().add(this.position()))
        );
        DrawArea da = parentArea.getSubarea(position, dimensions);
        this.component.drawSelf(newCtx, da);

        for (SizedPositioned child : children) {
            child.onScreen(newCtx, da);
        }
    }

    public void offScreen() {
        for (SizedPositioned child : children) {
            child.offScreen();
        }
        this.component.offScreen();
    }

}
