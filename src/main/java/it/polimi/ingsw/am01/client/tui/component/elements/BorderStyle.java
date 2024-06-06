package it.polimi.ingsw.am01.client.tui.component.elements;

public record BorderStyle(
        char t,
        char b,
        char l,
        char r,
        char tl,
        char tr,
        char bl,
        char br
) {
    public static final BorderStyle DEFAULT = new BorderStyle('─', '─', '│', '│', '┌', '┐', '└', '┘');
}
