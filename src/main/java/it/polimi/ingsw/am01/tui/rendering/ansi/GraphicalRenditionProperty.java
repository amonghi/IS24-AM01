package it.polimi.ingsw.am01.tui.rendering.ansi;

public sealed interface GraphicalRenditionProperty {
    int getAnsiCode();

    enum Weight implements GraphicalRenditionProperty {
        BOLD(1),
        DIM(2),
        RESET(22);

        private final int code;

        Weight(int code) {
            this.code = code;
        }

        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    enum Italics implements GraphicalRenditionProperty {
        ON(3),
        OFF(23);

        private final int code;

        Italics(int code) {
            this.code = code;
        }

        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    enum Underline implements GraphicalRenditionProperty {
        ON(4),
        OFF(24);

        private final int code;

        Underline(int code) {
            this.code = code;
        }

        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    enum ForegroundColor implements GraphicalRenditionProperty {
        BLACK(30),
        RED(31),
        GREEN(32),
        YELLOW(33),
        BLUE(34),
        MAGENTA(35),
        CYAN(36),
        WHITE(37),
        DEFAULT(39);

        private final int code;

        ForegroundColor(int code) {
            this.code = code;
        }

        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }

    enum BackgroundColor implements GraphicalRenditionProperty {
        BLACK(40),
        RED(41),
        GREEN(42),
        YELLOW(43),
        BLUE(44),
        MAGENTA(45),
        CYAN(46),
        WHITE(47),
        DEFAULT(49);

        private final int code;

        BackgroundColor(int code) {
            this.code = code;
        }

        @Override
        public int getAnsiCode() {
            return this.code;
        }
    }
}
