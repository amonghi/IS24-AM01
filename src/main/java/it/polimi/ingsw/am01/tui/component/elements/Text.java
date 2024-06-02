package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.component.prop.Prop;
import it.polimi.ingsw.am01.tui.component.prop.StaticProp;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class Text extends Component {
    public static class TextComponentBuilder implements ComponentBuilder {
        private final Prop<String> text;
        private Prop<GraphicalRendition> rendition;

        public TextComponentBuilder(String text) {
            this(new StaticProp<>(text));
        }

        public TextComponentBuilder(Prop<String> text) {
            this.text = text;
            this.rendition = new StaticProp<>(GraphicalRendition.DEFAULT);
        }

        public TextComponentBuilder withRendition(GraphicalRendition rendition) {
            return withRendition(new StaticProp<>(rendition));
        }

        public TextComponentBuilder withRendition(Prop<GraphicalRendition> rendition) {
            this.rendition = rendition;
            return this;
        }

        @Override
        public Component build(BuildContext ctx) {
            return new Text(ctx, this.rendition, this.text);
        }
    }

    public static TextComponentBuilder of(String text) {
        return new TextComponentBuilder(text);
    }

    public static TextComponentBuilder of(Prop<String> text) {
        return new TextComponentBuilder(text);
    }

    private final Prop<GraphicalRendition> rendition;
    private final Prop<String> text;

    public Text(BuildContext ctx, Prop<GraphicalRendition> rendition, Prop<String> text) {
        super(ctx);
        this.rendition = rendition;
        this.text = text;
    }

    @Override
    public Sized layout(Constraint constraint) {
        return new Sized(
                this,
                Dimensions.constrained(constraint, this.text.get(this).length(), 1),
                List.of()
        );
    }

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        a.setRendition(this.rendition.get(this));

        String text = this.text.get(this);
        for (int i = 0; i < text.length(); i++) {
            a.draw(i, 0, text.charAt(i));
        }
    }
}
