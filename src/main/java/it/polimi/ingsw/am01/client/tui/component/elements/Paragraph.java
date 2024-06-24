package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Text;

import java.util.ArrayList;

public class Paragraph extends Component {
    private final GraphicalRendition rendition;
    private final String text;
    private ArrayList<String> lines;

    public Paragraph(String text) {
        this(GraphicalRendition.DEFAULT, text);
    }

    public Paragraph(GraphicalRendition rendition, String text) {
        this.rendition = rendition;
        this.text = text;
    }

    @Override
    public void layout(Constraint constraint) {
        int width = constraint.max().width();

        this.lines = new ArrayList<>();
        this.lines.add("");

        int wordStart = 0;
        while (wordStart < text.length()) {
            int lineLen = lines.getLast().length();

            int nextBreak = findNextBreak(wordStart);
            int len = nextBreak - wordStart + 1;
            int newLineLen = lineLen + len;

            // three cases:
            // 1. we can fit the word on the current line
            // 2. we can't fit the word on the current line, but it will fit on a new line
            // 3. we can't fit the word on the current line, and it won't fit on a new line
            if (newLineLen <= width) {
                // case 1: we simply add the word to the current line
                String word = text.substring(wordStart, nextBreak + 1);
                String updatedLastLine = lines.getLast() + word;
                lines.set(lines.size() - 1, updatedLastLine);

                wordStart = nextBreak + 1;
            } else if (len <= width) {
                // case 2: we add the word to a new line
                String word = text.substring(wordStart, nextBreak + 1);
                lines.add(word);

                wordStart = nextBreak + 1;
            } else {
                // case 3: we add as much of the word as we can to the current line, then start a new line
                int lengthThatFits = width - lineLen;
                String thePartThatFits = text.substring(wordStart, wordStart + lengthThatFits);
                String updatedLastLine = lines.getLast() + thePartThatFits;
                lines.set(lines.size() - 1, updatedLastLine);
                lines.add("");

                wordStart = wordStart + lengthThatFits;
            }
        }

        int height = lines.size();
        Dimensions d = Dimensions.constrained(constraint, width, height);
        this.setDimensions(d);
    }

    private int findNextBreak(int start) {
        int i = start;
        while (!canBreakAt(i) && i < text.length() - 1) {
            i++;
        }
        return i;
    }

    private boolean canBreakAt(int i) {
        if (i == text.length() - 1) {
            return true;
        }

        char next = text.charAt(i);
        return Character.isWhitespace(next);
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        a.setRendition(this.rendition);
        if (lines == null) {
            throw new IllegalStateException("Paragraph not laid out");
        }

        for (int i = 0; i < lines.size() && i < a.dimensions().height(); i++) {
            String line = lines.get(i);
            Text.write(a, 0, i, line);
        }
    }
}
