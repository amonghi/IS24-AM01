package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public final class PlayerChangedColorChoiceEvent extends GameEvent {
    private final PlayerProfile player;
    private final PlayerColor playerColor;
    private final SelectionResult selectionResult;

    public PlayerChangedColorChoiceEvent(PlayerProfile player, PlayerColor playerColor, SelectionResult selectionResult) {
        this.player = player;
        this.playerColor = playerColor;
        this.selectionResult = selectionResult;
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public SelectionResult getSelectionResult() {
        return selectionResult;
    }
}
