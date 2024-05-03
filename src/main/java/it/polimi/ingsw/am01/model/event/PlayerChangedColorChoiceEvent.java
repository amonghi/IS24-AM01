package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record PlayerChangedColorChoiceEvent(PlayerProfile player, PlayerColor playerColor,
                                            SelectionResult selectionResult) implements GameEvent {
}
