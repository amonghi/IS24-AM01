package it.polimi.ingsw.am01.model.player;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.objective.Objective;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerDataTest {

    @Test
    public void testPlayerData() {
        List<Card> hand = new ArrayList<>();
        Objective objective = GameAssets.getInstance().getObjectives().getFirst();
        PlayerColor playerColor = PlayerColor.YELLOW;

        PlayerData playerData = new PlayerData(hand, objective, playerColor);

        assertEquals(hand, playerData.hand());
        assertEquals(objective, playerData.objective());
        assertEquals(playerColor, playerData.color());
    }
}