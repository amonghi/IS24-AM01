package it.polimi.ingsw.am01.client.tui.rendering;

/**
 * The rendering context offers information to the {@link it.polimi.ingsw.am01.client.tui.component.Component} during the drawing phase.
 *
 * @param global the global context
 * @param local  the local context
 */
public record RenderingContext(Global global, Local local) {

    /**
     * This contains information shared among all components
     */
    public static class Global {
        private final Dimensions dimensions;
        private Position cursorPosition;

        public Global(Dimensions dimensions) {
            this.cursorPosition = Position.ZERO;
            this.dimensions = dimensions;
        }

        /**
         * Gets the position of the cursor in the terminal
         *
         * @return the cursor position
         */
        public Position getCursorPosition() {
            return cursorPosition;
        }

        /**
         * Sets the position of the cursor in the terminal
         *
         * @param cursorPosition the new cursor position
         */
        public void setCursorPosition(Position cursorPosition) {
            this.cursorPosition = cursorPosition;
        }

        /**
         * Gets the dimensions of the terminal
         *
         * @return the dimensions of the terminal
         */
        public Dimensions getDimensions() {
            return dimensions;
        }
    }

    /**
     * This contains information specific to the component being drawn
     */
    public static class Local {
        private final Position offset;

        public Local(Position offset) {
            this.offset = offset;
        }

        /**
         * Gets the offset of the component being drawn, relative to the parent component
         *
         * @return the offset
         */
        public Position getOffset() {
            return offset;
        }
    }
}
