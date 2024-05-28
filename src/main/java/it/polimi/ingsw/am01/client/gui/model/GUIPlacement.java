package it.polimi.ingsw.am01.client.gui.model;

import it.polimi.ingsw.am01.model.card.Side;

public record GUIPlacement(int id, Side side, Position pos, int seq, int points) implements Comparable<GUIPlacement> {

    @Override
    public int compareTo(GUIPlacement other) {
        return Integer.compare(seq, other.seq);
    }
}
