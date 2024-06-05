package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.exception.IllegalPlacementException;
import it.polimi.ingsw.am01.model.exception.NotUndoableOperationException;

import java.io.Serializable;
import java.util.*;

public class PlayArea implements Iterable<PlayArea.CardPlacement> {

    private final Set<Position> playablePositions;
    private final Map<Position, CardPlacement> cards;
    /**
     * It counts each type of {@link Collectible} visible on the {@link PlayArea}.
     * Until a {@link Card} with a certain type of {@link Collectible} is placed, the {@code KeySet} does not contain that {@link Collectible}.
     */
    private final Map<Collectible, Integer> collectibleCount;
    private int score;

    private int seq;

    /**
     * Create a new play area and initialize it with the first {@link CardPlacement}.
     * <p>
     * The initial card is placed at {@link Position#ORIGIN}.
     *
     * @param starterCard card for the {@link CardPlacement}, same as {@code card} in {@link #placeAt(Position, Card, Side, boolean)}
     * @param side        side for the {@link CardPlacement}, same as in {@link #placeAt(Position, Card, Side, boolean)}
     * @see #placeAt(Position, Card, Side)
     */
    public PlayArea(Card starterCard, Side side) {
        this.cards = new HashMap<>();
        this.score = 0;
        this.seq = 0;
        this.collectibleCount = new HashMap<>();
        this.playablePositions = new HashSet<>();

        this.playablePositions.add(Position.ORIGIN);
        try {
            this.placeAt(Position.ORIGIN, starterCard, side, true);
        } catch (IllegalPlacementException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The same as {{@link #placeAt(Position, Card, Side)}} but with separated i,j indices.
     *
     * @param i    the {@code i} index of the {@link Position} at which the {@code card} is to be placed.
     * @param j    the {@code j} index of the {@link Position} at which the {@code card} is to be placed.
     * @param card the card to be placed
     * @param side which side to place facing up
     * @return the created {@link CardPlacement}
     * @throws IllegalPlacementException when trying to create an illegal placement
     * @see #placeAt(Position, Card, Side)
     */
    public CardPlacement placeAt(int i, int j, Card card, Side side) throws IllegalPlacementException {
        return placeAt(new Position(i, j), card, side, false);
    }

    /**
     * Place a card at the specified location.
     * <p>
     * To be a valid placement the card must:
     * <ol>
     *     <li>touch at least on socket corner (that is, a corner where {@code isSocket() == true})</li>
     *     <li>not touch any corners that are filled in (that is, a corner where {@code isSocket() == false})</li>
     * </ol>
     *
     * @param position the position where to place the card
     * @param card     the card to be placed
     * @param side     which side to place facing up
     * @return the created {@link CardPlacement}
     * @throws IllegalPlacementException when trying to create an illegal placement
     * @see Corner#isSocket()
     */
    public CardPlacement placeAt(Position position, Card card, Side side) throws IllegalPlacementException {
        return placeAt(position, card, side, false);
    }

    private CardPlacement placeAt(Position position, Card card, Side side, boolean isFirst) throws IllegalPlacementException {
        int seqNumber = seq++;
        CardPlacement placement = new CardPlacement(position, card, side, seqNumber);

        // skip checks for first card
        if (!isFirst) {
            // check that we don't place over filled-in corners
            // also check that there is at least one socket around
            boolean foundSocket = false;
            for (CornerPosition cornerPos : CornerPosition.values()) {
                Optional<CardPlacement> relative = placement.getRelative(cornerPos);
                if (relative.isPresent()) {
                    Corner connectingCorner = relative.get().getVisibleFace().corner(cornerPos.getOpposite());
                    if (!connectingCorner.isSocket()) {
                        throw new IllegalPlacementException("attempted to place over non-socket corner");
                    }

                    foundSocket = true;
                }
            }

            if (!foundSocket) {
                throw new IllegalPlacementException("attempted to place without connecting to any other cards");
            }
        }

        //check placement constraints
        if (!card.getFace(side).getPlacementConstraint().map(pc -> pc.isSatisfied(this)).orElse(true)) {
            throw new IllegalPlacementException("attempt to place with no enough resources");
        }

        // register placement
        this.cards.put(position, placement);

        // if we find relative we can be sure that we are placing on top
        // so their resource will be covered and must be removed from the global count
        Arrays.stream(CornerPosition.values())
                .flatMap(cornerPos -> placement.getRelative(cornerPos)
                        .flatMap(relative -> relative.getVisibleFace().corner(cornerPos.getOpposite()).getCollectible())
                        .stream()
                )
                .forEach(collectible -> collectibleCount.computeIfPresent(collectible, (_key, count) -> count - 1));

        // now we add the resources that we have added by placing this card
        Arrays.stream(CornerPosition.values())
                .flatMap(cornerPos -> placement.getVisibleFace().corner(cornerPos).getCollectible().stream())
                .forEach(collectible -> collectibleCount.merge(collectible, 1, Integer::sum));

        // we also add all the center resources, if present
        card.getFace(side).getCenterResources()
                .forEach((resource, amount) -> collectibleCount.merge(resource, amount, Integer::sum));

        // update caches
        this.score += placement.getPoints();

        updatePlayablePositionsAfterPlacing(placement);

        return placement;
    }

    public CardPlacement undoPlacement() throws NotUndoableOperationException {
        if (this.seq < 2)
            throw new NotUndoableOperationException();

        //This should never be null, because the `seq` should be correct
        return this.cards.values()
                .stream()
                .filter(cp -> cp.seq == this.seq - 1)
                .findFirst()
                .map(this::removePlacement)
                .orElseThrow(NotUndoableOperationException::new);

    }

    private CardPlacement removePlacement(CardPlacement cardPlacement) {
        this.seq--;
        this.score -= cardPlacement.getPoints();

        //Remove resources and items of the last placement
        cardPlacement.getCard().getFace(cardPlacement.getSide())
                .getCenterResources()
                .forEach((resource, amount) -> collectibleCount.merge(resource, -amount, Integer::sum));

        Arrays.stream(CornerPosition.values())
                .flatMap(cornerPos -> cardPlacement.getVisibleFace().corner(cornerPos)
                        .getCollectible()
                        .stream())
                .forEach(collectible -> collectibleCount.merge(collectible, -1, Integer::sum));

        this.cards.remove(cardPlacement.getPosition());

        //Re-add collectibles of the placements covered by the one I've just removed
        cardPlacement.getCovered()
                .forEach((cornerPosition, placement) ->
                        placement.getVisibleCollectibleAtCorner(cornerPosition.getOpposite())
                                .ifPresent(collectible -> collectibleCount.merge(collectible, 1, Integer::sum))
                );

        updatePlayablePositionsAfterRemoving(cardPlacement);
        return cardPlacement;
    }

    /**
     * It updates the {@code Set} of playable {@link Position} every time a new {@link Card} is placed
     *
     * @param placement The {@link CardPlacement} returned by the {@link PlayArea#placeAt(int, int, Card, Side)} method
     * @see PlayArea#placeAt(int, int, Card, Side)
     */
    private void updatePlayablePositionsAfterPlacing(CardPlacement placement) {
        int invalidCount;
        playablePositions.remove(placement.getPosition());
        for (CornerPosition cornerPos : CornerPosition.values()) {
            Position pos = placement.getPosition().getRelative(cornerPos);
            if (getAt(pos).isEmpty() && placement.getVisibleFace().corner(cornerPos).isSocket()) {
                invalidCount = 0;
                for (CornerPosition newCornerPos : CornerPosition.values()) {
                    Position toCheck = pos.getRelative(newCornerPos);
                    if (getAt(toCheck).isPresent() && !getAt(toCheck).get().getVisibleFace().corner(newCornerPos.getOpposite()).isSocket()) {
                        invalidCount++;
                    }
                }
                if (invalidCount == 0)
                    playablePositions.add(pos);
            } else {
                playablePositions.remove(pos);
            }
        }
    }

    private void updatePlayablePositionsAfterRemoving(CardPlacement cardPlacement) {
        playablePositions.add(cardPlacement.getPosition());
        Arrays.stream(CornerPosition.values()).forEach(cornerPosition ->
                playablePositions.remove(cardPlacement.getPosition().getRelative(cornerPosition))
        );
    }

    public Map<Position, CardPlacement> getCards() {
        return Collections.unmodifiableMap(cards);
    }

    /**
     * Provides the playable {@link Position}, i.e. the {@link Position} where a new {@link Card},
     * if placed, will be connected to other {@link Card} without covering any missing {@link Corner}
     *
     * @return The {@code Set} containing all the playable {@link Position}. Set is unmodifiable
     */
    public Set<Position> getPlayablePositions() {
        return Collections.unmodifiableSet(playablePositions);
    }

    /**
     * The same as {@link #getAt(Position)} but with separated i,j indices.
     *
     * @param i the {@code i} coordinate of the {@link Position} of the {@link CardPlacement} that is to be returned
     * @param j the {@code j} coordinate of the {@link Position} of the {@link CardPlacement} that is to be returned
     * @return the {@link CardPlacement} at the specified position, if present
     * @see #getAt(Position)
     */
    public Optional<CardPlacement> getAt(int i, int j) {
        return getAt(new Position(i, j));
    }

    /**
     * @param pos the position of the {@link CardPlacement} that is to be returned
     * @return the {@link CardPlacement} at the specified position, if present
     */
    public Optional<CardPlacement> getAt(Position pos) {
        return Optional.ofNullable(this.cards.get(pos));
    }

    /**
     * @return An {@code UnmodifiableMap} of the visible {@link Collectible} on the {@link PlayArea}
     */
    public Map<Collectible, Integer> getCollectibleCount() {
        return Collections.unmodifiableMap(this.collectibleCount);
    }

    /**
     * @return total score earned by every {@link CardPlacement} in this {@link PlayArea}
     */
    public int getScore() {
        return this.score;
    }

    /**
     * @return the sequence number of the next {@link CardPlacement}
     */
    public int getSeq() {
        return seq;
    }

    @Override
    public Iterator<CardPlacement> iterator() {
        return this.cards.values().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PlayArea{" +
                "playablePositions=" + playablePositions +
                ", cards=" + cards +
                ", collectibleCount=" + collectibleCount +
                ", score=" + score +
                ", seq=" + seq +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        PlayArea playArea = (PlayArea) other;
        return score == playArea.score && seq == playArea.seq && Objects.equals(playablePositions, playArea.playablePositions)
                && Objects.equals(cards, playArea.cards) && Objects.equals(collectibleCount, playArea.collectibleCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(playablePositions, cards, collectibleCount, score, seq);
    }

    /**
     * Represent a position in the {@link PlayArea}
     * <p>
     * A position is represented by two indices {@code i} and {@code j}.
     * The indices can be positive or negative.
     *
     * @param i the first index of the position. Can be positive or negative.
     * @param j the second index of the position. Can be positive or negative.
     */
    public record Position(int i, int j) implements Serializable {
        /**
         * The origin of the {@link PlayArea}, that is {@code Position(0, 0)}.
         */
        public static final Position ORIGIN = new Position(0, 0);

        /**
         * Finds a new position by starting at the position represented by {@code this}
         * and moving in the direction of {@code CornerPosition}.
         *
         * @param cp the direction to move into
         * @return the position at which we arrive after moving
         */
        public Position getRelative(CornerPosition cp) {
            return switch (cp) {
                case TOP_RIGHT -> new Position(i + 1, j);
                case TOP_LEFT -> new Position(i, j + 1);
                case BOTTOM_LEFT -> new Position(i - 1, j);
                case BOTTOM_RIGHT -> new Position(i, j - 1);
            };
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return i == position.i && j == position.j;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Position{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    /**
     * Represent a placement of a card. It associates card, chosen side and play area of player.
     */
    public class CardPlacement implements Comparable<CardPlacement> {
        private final Position position;
        private final Card card;
        private final Side side;
        private final int seq;
        private final int points;

        /**
         * Constructs a new CardPlacement and sets all specified parameters. "points" is calculated by private method "calculatePoints()"
         *
         * @param position The position of the placement
         * @param card     The card of placement
         * @param side     The chosen side of card
         * @param seq      The sequence number of the placement into play area
         */
        private CardPlacement(Position position, Card card, Side side, int seq) {
            this.position = position;
            this.card = card;
            this.side = side;
            this.seq = seq;
            this.points = 0;
        }

        /**
         * Calculates points earned from the placement
         *
         * @return Points earned by owner of play area
         */
        private int calculatePoints() {
            return card.getFace(side).getPoints()
                    .map(s -> s.calculateScoredPoints(this))
                    .orElse(0);
        }

        /**
         * Provides the play area in which the placement is located
         *
         * @return The PlayArea of the placement
         */
        // TODO: decide if this is actually useful
        public PlayArea getPlayArea() {
            return PlayArea.this;
        }


        /**
         * @return the position of this {@link CardPlacement}
         */
        public Position getPosition() {
            return this.position;
        }

        /**
         * Provides the card placed
         *
         * @return The Card of the placement
         */
        public Card getCard() {
            return card;
        }

        /**
         * Provides the side of card placed
         *
         * @return The Side of card
         */
        public Side getSide() {
            return side;
        }

        /**
         * Provides the sequence number of this placement on {@link PlayArea}
         *
         * @return The sequence number
         */
        public int getSeq() {
            return seq;
        }

        /**
         * Provides the visible CardFace of the placed card
         *
         * @return The visible CardFace of card
         */
        public CardFace getVisibleFace() {
            return card.getFace(side);
        }

        /**
         * Provides points earned from the placement
         *
         * @return Points earned from the placement
         */
        public int getPoints() {
            return calculatePoints();
        }

        /**
         * Provides linked card placement at specified corner from play area.
         *
         * @param cp The CornerPosition of the card placement
         * @return An Optional of CardPlacement with the card placement linked to this at corner "cp"
         */
        public Optional<CardPlacement> getRelative(CornerPosition cp) {
            Position rel = this.position.getRelative(cp);
            return getAt(rel.i, rel.j);
        }

        /**
         * Provides the card placement that is on top, when looking at a certain corner of this card placement.
         *
         * @param cp the CornerPosition of the card placement
         * @return {@code this} if this card placement is on top, otherwise the same that {@link #getRelative(CornerPosition)} would have returned.
         */
        public CardPlacement getTopPlacementAtCorner(CornerPosition cp) {
            Optional<CardPlacement> optionalRelative = getRelative(cp);
            if (optionalRelative.isEmpty()) {
                return this;
            }

            CardPlacement relative = optionalRelative.get();
            return this.compareTo(relative) > 0 ? this : relative;
        }

        /**
         * Provides the collectible on a specific corner of card (visible side)
         *
         * @param cp The CornerPosition of the card placement
         * @return An Optional of Collectible with collectible at corner "cp"
         */
        public Optional<Collectible> getVisibleCollectibleAtCorner(CornerPosition cp) {
            CardPlacement topPlacement = getTopPlacementAtCorner(cp);

            // the adjacent CardPlacement covers this corner with its corner that is in the opposite position
            CornerPosition targetCorner = topPlacement == this ? cp : cp.getOpposite();

            return topPlacement.getVisibleFace().corner(targetCorner).getCollectible();
        }

        /**
         * Provides, for all CornerPosition, the card placement that is covered by this
         *
         * @return A Map that links CornerPosition to CardPlacement covered
         */
        public Map<CornerPosition, CardPlacement> getCovered() {
            Map<CornerPosition, CardPlacement> result = new EnumMap<>(CornerPosition.class);
            for (CornerPosition cp : CornerPosition.values()) {
                if (getRelative(cp).isPresent() && this.compareTo(getRelative(cp).get()) > 0) {
                    result.put(cp, getRelative(cp).get());
                }
            }
            return result;
        }

        /**
         * Compare two {@link CardPlacement}s based on the placement order.
         * <p>
         * If two card placements are adjacent, this can be used to determine which on is on top (it is the one that
         * was placed later).
         *
         * @param other The other CardPlacement
         * @return A negative number if {@code this} was placed before {@code other},
         * a positive number if {@code this} was placed after {@code other}
         * Should never return 0.
         */
        @Override
        public int compareTo(CardPlacement other) {
            return Integer.compare(this.seq, other.seq);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CardPlacement other = (CardPlacement) o;
            return seq == other.seq && position.equals(other.position) && card.equals(other.card) && side == other.side;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "CardPlacement{" +
                    "position=" + position +
                    ", card=" + card +
                    ", side=" + side +
                    ", seq=" + seq +
                    ", points=" + points +
                    '}';
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(position, card, side, seq, points);
        }
    }
}
