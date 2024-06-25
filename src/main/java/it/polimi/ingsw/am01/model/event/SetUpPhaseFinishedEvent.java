package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Deck;
import it.polimi.ingsw.am01.model.game.FaceUpCard;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Map;
import java.util.Set;

/**
 * This event is emitted when the choices phase is finished. The new {@code GameStatus} will be {@code PLAY}.
 * @param resourceDeck the resource deck
 * @param goldenDeck the golden deck
 * @param commonObjective a {@link Set} that contains the commons {@link Objective}s
 * @param faceUpCards a {@link Set} that contains all the {@link FaceUpCard}s
 * @param hands a {@link Map} that contains all the {@link PlayerData} of players
 */
public record SetUpPhaseFinishedEvent(Deck resourceDeck,
                                      Deck goldenDeck,
                                      Set<Objective> commonObjective,
                                      Set<FaceUpCard> faceUpCards,
                                      Map<PlayerProfile, PlayerData> hands) implements GameEvent {
}
