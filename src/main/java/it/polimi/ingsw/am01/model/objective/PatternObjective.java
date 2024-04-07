package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.game.PlayArea.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * A type of {@link Objective} that gives you point if a specific
 * pattern of {@link Card}s is visible on the {@link PlayArea}
 *
 * @see it.polimi.ingsw.am01.model.objective.Objective
 *
 */
public class PatternObjective extends Objective {
    private final Map<PlayArea.Position, CardColor> pattern;
    private final Set<Set<PlayArea.CardPlacement>> matches;

    /**
     * Constructs a new PatternObjective with a specified pattern of {@link Card}s
     *
     * @param points The points a player earns for each match
     * @param pattern The map of relative {@link Position} and {@link CardColor} that
     *                define the pattern
     */
    public PatternObjective(int points, Map<PlayArea.Position, CardColor> pattern) {
        super(points);
        this.pattern = new HashMap<>(pattern);
        matches = new HashSet<>(0);
    }

    /**
     * @return the map of that define the pattern
     */
    public Map<Position, CardColor> getPattern() {
        return Collections.unmodifiableMap(pattern);
    }

    /**
     * Identify all the patterns on the {@link PlayArea} and calculate the points to assign to the player
     *
     * @param pa The {@link PlayArea} on which find matches
     * @return The sum of the points earned by the player
     */
    @Override
    public int getEarnedPoints(PlayArea pa) {
        matches.clear();
        PlayArea.Position origin = new PlayArea.Position(0,0);

        for(PlayArea.CardPlacement cp : pa) {
            //In this set I insert the cards that satisfy the pattern
            Set<PlayArea.CardPlacement> satisfyingCards = new HashSet<>(0);
            //The position (0,0) is always present in the Map
            if(cp.getCard().color().equals(pattern.get(origin))) {
                satisfyingCards.add(cp);
                //Relative positions to be checked
                Set<PlayArea.Position> toCheck = pattern.keySet().stream()
                                                                    .filter(pos -> !pos.equals(origin))
                                                                    .collect(Collectors.toSet());
                for(PlayArea.Position relativePos : toCheck) {
                    PlayArea.Position pos = new PlayArea.Position(relativePos.i() + cp.getPosition().i(),
                            relativePos.j() + cp.getPosition().j());
                    Optional<PlayArea.CardPlacement> card = pa.getAt(pos);
                    if(card.isPresent()) {
                        if(card.get().getCard().color().equals(pattern.get(relativePos))) {
                            satisfyingCards.add(card.get());
                        }
                        //TODO: CI VA UN ELSE CHE INTERROMPE IL CICLO
                    }
                }

                //Here I shoud have the complete set, if the objective has been satisfied
                if(satisfyingCards.size() == pattern.keySet().size())
                    matches.add(new HashSet<>(satisfyingCards));
                satisfyingCards.clear();
            }
        }

        //Here, in matches, I have all the Set that satisfy the objective
        //And I have to identify ONLY the disjoint sets.
        int matchCards = (int) matches.stream()
                                        .flatMap(Collection::stream)
                                        .distinct()
                                        .count();

        //Here I have the number of DIFFERENT cards that satisfy the objective.
        //Groups of pattern.keySet().size() cards can be used to calculate points
        return (matchCards / pattern.keySet().size())*getPointsPerMatch();
    }
}
