package it.polimi.ingsw.am01.tui.rendering;

public record RenderingContext(Global global, Local local) {

    public static class Global {
        private final Dimensions dimensions;
        private Position cursorPosition;

        public Global(Dimensions dimensions) {
            this.cursorPosition = Position.ZERO;
            this.dimensions = dimensions;
        }

        public Position getCursorPosition() {
            return cursorPosition;
        }

        public void setCursorPosition(Position cursorPosition) {
            this.cursorPosition = cursorPosition;
        }

        public Dimensions getDimensions() {
            return dimensions;
        }
    }

    public static class Local {
        private final Position offset;

        public Local(Position offset) {
            this.offset = offset;
        }

        public Position getOffset() {
            return offset;
        }
    }
}
