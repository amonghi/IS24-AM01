package it.polimi.ingsw.am01.model.card;

import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Map;
import java.util.Optional;

public class CardPlacement implements Comparable<CardPlacement> {
    private PlayArea playArea;
    private int i;
    private int j;
    private Card card;
    private Side side;
    private int seq;
    private int points;

    public CardPlacement(PlayArea playArea, int i, int j, Card card, Side side, int seq) {
        throw new UnsupportedOperationException("TODO");
    }

    private int calculatePoints() {
        throw new UnsupportedOperationException("TODO");
    }

    public PlayArea getPlayArea() {
        throw new UnsupportedOperationException("TODO");
    }

    public int getI() {
        throw new UnsupportedOperationException("TODO");
    }

    public int getJ() {
        throw new UnsupportedOperationException("TODO");
    }

    public Card getCard() {
        throw new UnsupportedOperationException("TODO");
    }

    public Side getSide() {
        throw new UnsupportedOperationException("TODO");
    }

    public CardFace getVisibleFace() {
        throw new UnsupportedOperationException("TODO");
    }

    public int getPoints() {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<Collectible> getCollectibleAtCorner(CornerPosition cp) {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<CardPlacement> getRelative(CornerPosition cp) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<CornerPosition, CardPlacement> getCovered() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int compareTo(CardPlacement other) {
        throw new UnsupportedOperationException("TODO");
    }
}
