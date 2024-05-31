package it.polimi.ingsw.am01.tui.keyboard;

import it.polimi.ingsw.am01.eventemitter.Event;

public sealed interface Key extends Event {
    record Character(char character) implements Key {
    }

    record Ctrl(char character) implements Key {
    }

    record Alt(char character) implements Key {
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
