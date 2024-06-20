package it.polimi.ingsw.am01.client.tui.rendering.ansi;

/**
 * Represents a graphical rendition property.
 */
public sealed interface GraphicalRenditionProperty {
    /**
     * Get the ANSI code for this property.
     *
     * @return The ANSI code.
     */
    int getAnsiCode();

    /**
     * Represents the weight of the text.
     */
    enum Weight implements GraphicalRenditionProperty {
        /**
         * Shows the text in bold.
         */
        BOLD(1),
        /**
         * Shows the text dimly.
         */
        DIM(2),
        /**
         * Resets the weight to normal.
         */
        RESET(22);

        private final int code;

        Weight(int code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    /**
     * Represents whether the text is in italics.
     */
    enum Italics implements GraphicalRenditionProperty {
        /**
         * Shows the text in italics.
         */
        ON(3),
        /**
         * Resets the text to normal.
         */
        OFF(23);

        private final int code;

        Italics(int code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    /**
     * Represents whether the text is underlined.
     */
    enum Underline implements GraphicalRenditionProperty {
        /**
         * Shows the text underlined.
         */
        ON(4),
        /**
         * Resets the text to normal.
         */
        OFF(24);

        private final int code;

        Underline(int code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    /**
     * Represents the foreground color of the text.
     */
    enum ForegroundColor implements GraphicalRenditionProperty {
        BLACK(30),
        RED(31),
        GREEN(32),
        YELLOW(33),
        BLUE(34),
        MAGENTA(35),
        CYAN(36),
        WHITE(37),
        /**
         * Resets the foreground color to the default.
         */
        DEFAULT(39);

        private final int code;

        ForegroundColor(int code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    /**
     * Represents the background color of the text.
     */
    enum BackgroundColor implements GraphicalRenditionProperty {
        BLACK(40),
        RED(41),
        GREEN(42),
        YELLOW(43),
        BLUE(44),
        MAGENTA(45),
        CYAN(46),
        WHITE(47),
        /**
         * Resets the background color to the default.
         */
        DEFAULT(49);

        private final int code;

        BackgroundColor(int code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }
}
