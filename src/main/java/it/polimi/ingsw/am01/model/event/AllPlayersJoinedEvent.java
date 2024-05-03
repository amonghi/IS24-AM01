package it.polimi.ingsw.am01.model.event;

public class AllPlayersJoinedEvent implements GameEvent{
    private int startingCardId;

    public void setStartingCardId(int startingCardId) {
        this.startingCardId = startingCardId;
    }

    public int getStartingCardId() {
        return startingCardId;
    }
}
