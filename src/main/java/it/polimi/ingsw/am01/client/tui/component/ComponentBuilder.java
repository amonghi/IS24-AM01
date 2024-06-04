package it.polimi.ingsw.am01.client.tui.component;

import java.util.Arrays;
import java.util.List;

public interface ComponentBuilder {
    Component build(BuildContext ctx);

    static List<Component> buildAll(BuildContext ctx, ComponentBuilder... componentBuilders) {
        return Arrays.stream(componentBuilders)
                .map(builder -> builder.build(ctx))
                .toList();
    }
}
