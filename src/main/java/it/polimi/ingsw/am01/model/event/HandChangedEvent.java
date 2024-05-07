package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Set;

public record HandChangedEvent(PlayerProfile playerProfile, Set<Card> currentHand) implements GameEvent {
}
