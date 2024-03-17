package it.polimi.ingsw.am01.model.card;

import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * CardPlacement represent a placement of placeable card. It associates card, chosen side and play area of player.
 */
public class CardPlacement implements Comparable<CardPlacement> {
    private PlayArea playArea;
    private int i;
    private int j;
    private Card card;
    private Side side;
    private int seq;
    private int points;

    /**
     * Constructs a new CardPlacement and sets all specified parameters. "points" is calculated by private method "calculatePoints()"
     * @param playArea The play area of card placement
     * @param i The coordinate x's play area
     * @param j The coordinate y's play area
     * @param card The card of placement
     * @param side The chosen side of card
     * @param seq The sequence number of the placement into play area
     */
    public CardPlacement(PlayArea playArea, int i, int j, Card card, Side side, int seq) {
        this.playArea = playArea;
        this.i = i;
        this.j = j;
        this.card = card;
        this.side = side;
        this.seq = seq;
        this.points = calculatePoints();
    }

    /**
     * Calculates points earned from the placement
     * @return Points earned by owner of play area
     */
    private int calculatePoints() {
        return card.getFace(side).getPoints().
                flatMap(s -> Optional.of(s.calculateScoredPoints(this))).
                orElse(0);
    }

    /**
     * Provides the play area in which the placement is located
     * @return The PlayArea of the placement
     */
    public PlayArea getPlayArea() {
        return playArea;
    }

    /**
     * Provides x coordinate of the placement (referred to play area)
     * @return The x coordinate of the placement
     */
    public int getI() {
        return i;
    }

    /**
     * Provides y coordinate of the placement (referred to play area)
     * @return The y coordinate of the placement
     */
    public int getJ() {
        return j;
    }

    /**
     * Provides the card placed
     * @return The Card of the placement
     */
    public Card getCard() {
        return card;
    }

    /**
     * Provides the side of card placed
     * @return The Side of card
     */
    public Side getSide() {
        return side;
    }

    /**
     * Provides the visible CardFace of the placed card
     * @return The visible CardFace of card
     */
    public CardFace getVisibleFace() {
        return card.getFace(side);
    }

    /**
     * Provides points earned from the placement
     * @return Points earned from the placement
     */
    public int getPoints() {
        return points;
    }

    /**
     * Provides the collectible on a specific corner of card (visible side)
     * @param cp The CornerPosition of the card placement
     * @return An Optional of Collectible with collectible at corner "cp"
     */
    public Optional<Collectible> getCollectibleAtCorner(CornerPosition cp) {
        return card.getFace(side).corner(cp).getCollectible();
    }

    /**
     * Provides linked card placement at specified corner from play area
     * @param cp The CornerPosition of the card placement
     * @return An Optional of CardPlacement with the card placement linked to this at corner "cp"
     */
    public Optional<CardPlacement> getRelative(CornerPosition cp) {
        return switch (cp) {
            case TOP_RIGHT -> playArea.getAt(i+1, j);
            case TOP_LEFT -> playArea.getAt(i, j+1);
            case BOTTOM_LEFT -> playArea.getAt(i-1, j);
            case BOTTOM_RIGHT -> playArea.getAt(i, j-1);
        };
    }

    /**
     * Provides, for all CornerPosition, the card placement that is covered by this
     * @return A Map that links CornerPosition to CardPlacement covered
     */
    public Map<CornerPosition, CardPlacement> getCovered() {
        Map<CornerPosition, CardPlacement> result = new EnumMap<>(CornerPosition.class);
        for(CornerPosition cp : CornerPosition.values()){
            if(getRelative(cp).isPresent() && this.compareTo(getRelative(cp).get())>0){
                result.put(cp, getRelative(cp).get());
            }
        }
        return result;
    }

    /**
     * Permits to define an order relationship between card placements, based on their sequence number (seq)
     * @param other The second CardPlacement to be compared ("this" is the first one)
     * @return A negative number if "this<other", 0 if this.seq=other.seq and a positive number if "this>other"
     */
    @Override
    public int compareTo(CardPlacement other) {
        return Integer.compare(this.seq, other.seq);
    }
}