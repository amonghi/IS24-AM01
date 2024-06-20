package it.polimi.ingsw.am01.client.tui.rendering.ansi;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the set of all graphical rendition properties.
 *
 * @param weight     The weight of the text.
 * @param italics    Whether the text is in italics.
 * @param underline  Whether the text is underlined.
 * @param foreground The foreground color of the text.
 * @param background The background color of the text.
 */
public record GraphicalRendition(
        GraphicalRenditionProperty.Weight weight,
        GraphicalRenditionProperty.Italics italics,
        GraphicalRenditionProperty.Underline underline,
        GraphicalRenditionProperty.ForegroundColor foreground,
        GraphicalRenditionProperty.BackgroundColor background
) {
    /**
     * The default graphical rendition.
     */
    public static final GraphicalRendition DEFAULT = new GraphicalRendition(
            GraphicalRenditionProperty.Weight.RESET,
            GraphicalRenditionProperty.Italics.OFF,
            GraphicalRenditionProperty.Underline.OFF,
            GraphicalRenditionProperty.ForegroundColor.DEFAULT,
            GraphicalRenditionProperty.BackgroundColor.DEFAULT);

    /**
     * Create a new graphical rendition with the specified weight.
     *
     * @param weight The weight of the text.
     */
    public GraphicalRendition withWeight(GraphicalRenditionProperty.Weight weight) {
        return new GraphicalRendition(weight, this.italics, this.underline, this.foreground, this.background);
    }

    /**
     * Create a new graphical rendition with the specified italics.
     *
     * @param italics The italics of the text.
     */
    public GraphicalRendition withItalics(GraphicalRenditionProperty.Italics italics) {
        return new GraphicalRendition(this.weight, italics, this.underline, this.foreground, this.background);
    }

    /**
     * Create a new graphical rendition with the specified underline.
     *
     * @param underline The underline of the text.
     */
    public GraphicalRendition withUnderline(GraphicalRenditionProperty.Underline underline) {
        return new GraphicalRendition(this.weight, this.italics, underline, this.foreground, this.background);
    }

    /**
     * Create a new graphical rendition with the specified foreground color.
     *
     * @param foreground The foreground color of the text.
     */
    public GraphicalRendition withForeground(GraphicalRenditionProperty.ForegroundColor foreground) {
        return new GraphicalRendition(this.weight, this.italics, this.underline, foreground, this.background);
    }

    /**
     * Create a new graphical rendition with the specified background color.
     *
     * @param background The background color of the text.
     */
    public GraphicalRendition withBackground(GraphicalRenditionProperty.BackgroundColor background) {
        return new GraphicalRendition(this.weight, this.italics, this.underline, this.foreground, background);
    }

    /**
     * Convert this graphical rendition to a list of its properties.
     *
     * @return this graphical rendition as a list of its properties.
     */
    private List<GraphicalRenditionProperty> asList() {
        return List.of(this.weight, this.italics, this.underline, this.foreground, this.background);
    }

    /**
     * Get the ANSI escape sequence for this graphical rendition.
     *
     * @return The ANSI escape sequence for this graphical rendition.
     */
    public String getAnsiSequence() {
        return Ansi.SGR(this.asList());
    }

    /**
     * Get the ANSI escape sequence to update the graphical rendition in the terminal from this to another.
     *
     * @param other The other graphical rendition to update to.
     * @return The ANSI escape sequence to update the graphical rendition in the terminal from this to another.
     */
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
