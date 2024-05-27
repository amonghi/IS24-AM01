package it.polimi.ingsw.am01.tui;

public sealed interface Key {
    record Character(char character) implements Key {
    }

    record Arrow(Direction direction) implements Key {
        public enum Direction {
            UP, DOWN, LEFT, RIGHT
        }
    }

    record Enter() implements Key {
    }

    record Backspace() implements Key {
    }

    record Tab() implements Key {
    }

    record Del() implements Key {
    }

    record Home() implements Key {
    }

    record End() implements Key {
    }

    record PageUp() implements Key {
    }

    record PageDown() implements Key {
    }
}
