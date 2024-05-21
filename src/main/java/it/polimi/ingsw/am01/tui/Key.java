package it.polimi.ingsw.am01.tui;

public sealed interface Key {
    record Character(char character) implements Key {
    }

    record Arrow(Direction direction) implements Key {
        public enum Direction {
            UP, DOWN, LEFT, RIGHT
        }
    }
}
