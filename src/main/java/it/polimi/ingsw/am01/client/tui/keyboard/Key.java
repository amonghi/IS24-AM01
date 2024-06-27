package it.polimi.ingsw.am01.client.tui.keyboard;

import it.polimi.ingsw.am01.eventemitter.Event;

public sealed interface Key extends Event {
    interface WithCtrl {
        boolean isCtrl();
    }

    interface WithAlt {
        boolean isAlt();
    }

    record Character(char character, boolean isCtrl, boolean isAlt) implements Key, WithAlt, WithCtrl {
    }

    record Arrow(Direction direction, boolean isCtrl, boolean isAlt) implements Key, WithAlt, WithCtrl {
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
