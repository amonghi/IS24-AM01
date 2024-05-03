package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.FaceUpCard;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Map;
import java.util.Set;

public record SetUpPhaseFinishedEvent(Set<Objective> commonObjective, Set<FaceUpCard> faceUpCards,
                                      Map<PlayerProfile, PlayerData> hands) implements GameEvent {

}
