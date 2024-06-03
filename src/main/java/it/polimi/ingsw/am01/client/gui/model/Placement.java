package it.polimi.ingsw.am01.client.gui.model;

import it.polimi.ingsw.am01.model.card.Side;

public record Placement(int id, Side side, Position pos, int seq, int points) implements Comparable<Placement> {

    @Override
    public int compareTo(Placement other) {
        return Integer.compare(seq, other.seq);
    }
}
