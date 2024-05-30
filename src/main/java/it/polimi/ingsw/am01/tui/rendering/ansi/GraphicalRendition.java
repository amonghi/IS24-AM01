package it.polimi.ingsw.am01.tui.rendering.ansi;

import java.util.ArrayList;
import java.util.List;

public record GraphicalRendition(
        GraphicalRenditionProperty.Weight weight,
        GraphicalRenditionProperty.Italics italics,
        GraphicalRenditionProperty.Underline underline,
        GraphicalRenditionProperty.ForegroundColor foreground,
        GraphicalRenditionProperty.BackgroundColor background
) {
    public static final GraphicalRendition DEFAULT = new GraphicalRendition(
            GraphicalRenditionProperty.Weight.RESET,
            GraphicalRenditionProperty.Italics.OFF,
            GraphicalRenditionProperty.Underline.OFF,
            GraphicalRenditionProperty.ForegroundColor.DEFAULT,
            GraphicalRenditionProperty.BackgroundColor.DEFAULT);

    public GraphicalRendition withWeight(GraphicalRenditionProperty.Weight weight) {
        return new GraphicalRendition(weight, this.italics, this.underline, this.foreground, this.background);
    }

    public GraphicalRendition withItalics(GraphicalRenditionProperty.Italics italics) {
        return new GraphicalRendition(this.weight, italics, this.underline, this.foreground, this.background);
    }

    public GraphicalRendition withUnderline(GraphicalRenditionProperty.Underline underline) {
        return new GraphicalRendition(this.weight, this.italics, underline, this.foreground, this.background);
    }

    public GraphicalRendition withForeground(GraphicalRenditionProperty.ForegroundColor foreground) {
        return new GraphicalRendition(this.weight, this.italics, this.underline, foreground, this.background);
    }

    public GraphicalRendition withBackground(GraphicalRenditionProperty.BackgroundColor background) {
        return new GraphicalRendition(this.weight, this.italics, this.underline, this.foreground, background);
    }

    private List<GraphicalRenditionProperty> asList() {
        return List.of(this.weight, this.italics, this.underline, this.foreground, this.background);
    }

    public String getAnsiSequence() {
        return Ansi.SGR(this.asList());
    }

    public String getUpdateAnsiSequence(GraphicalRendition other) {
        List<GraphicalRenditionProperty> thisList = this.asList();
        List<GraphicalRenditionProperty> otherList = other.asList();

        List<GraphicalRenditionProperty> changedProperties = new ArrayList<>();
        for (int i = 0; i < thisList.size(); i++) {
            if (!thisList.get(i).equals(otherList.get(i))) {
                changedProperties.add(thisList.get(i));
            }
        }

        return Ansi.SGR(changedProperties);
    }
}
