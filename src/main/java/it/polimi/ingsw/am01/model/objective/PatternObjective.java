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
     * @param points  The points a player earns for each match
     * @param pattern The map of relative {@link Position} and {@link CardColor} that
     *                define the pattern
     */
    public PatternObjective(int points, Map<PlayArea.Position, CardColor> pattern) {
        super(points);
        this.pattern = new HashMap<>(pattern);
        matches = new HashSet<>(0);
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
        //PlayArea.Position origin = new PlayArea.Position(0,0);

        for (PlayArea.CardPlacement cp : pa) {
            //Extract absolute position from the pattern
            Map<Position, CardColor> absPosition = new HashMap<>();
            for (Position relPos : pattern.keySet()) {
                absPosition.put(new Position(relPos.i() + cp.getPosition().i(), relPos.j() + cp.getPosition().j()), pattern.get(relPos));
            }

            //Extract CardPlacement to check
            Set<PlayArea.CardPlacement> toCheck = absPosition.keySet().stream()
                    .map(pa::getAt)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            //Check if some CardPlacement is missing in one of the pattern's position
            if (toCheck.size() == pattern.size()) {
                //Check if the color of each card matches with the pattern
                toCheck = toCheck.stream()
                        .filter(cardPlacement -> cardPlacement.getCard().color().equals(absPosition.get(cardPlacement.getPosition())))
                        .collect(Collectors.toSet());

                if (toCheck.size() == pattern.size())
                    matches.add(new HashSet<>(toCheck));

                toCheck.clear();
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
        return (matchCards / pattern.size()) * getPointsPerMatch();
    }
}
