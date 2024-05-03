package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.FaceUpCard;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Map;
import java.util.Set;

public class SetUpPhaseFinishedEvent extends GameEvent {

    private final Set<Objective> commonObjective;
    private final Set<FaceUpCard> faceUpCards;
    private final Map<PlayerProfile, PlayerData> hands;

    public SetUpPhaseFinishedEvent(Set<Objective> commonObjective, Set<FaceUpCard> faceUpCards, Map<PlayerProfile, PlayerData> hands) {
        this.commonObjective = commonObjective;
        this.faceUpCards = faceUpCards;
        this.hands = hands;
    }

    public Set<Objective> getCommonObjective() {
        return commonObjective;
    }

    public Set<FaceUpCard> getFaceUpCards() {
        return faceUpCards;
    }

    public Map<PlayerProfile, PlayerData> getHands() {
        return hands;
    }
}
