package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    Game standardGame = new Game(1, 4);
    PlayerProfile p1 = new PlayerProfile("Mattew");
    PlayerProfile p2 = new PlayerProfile("Giuly");
    PlayerProfile p3 = new PlayerProfile("David");
    PlayerProfile p4 = new PlayerProfile("George");
    
    Game shortGame = new Game(2, 3, new Board(new Deck(GameAssets.getInstance().getResourceCards().stream().limit(9).toList()),
            new Deck(GameAssets.getInstance().getGoldenCards().stream().limit(6).toList())));

    @Test
    public void testMaxPlayers(){
        standardGame.join(p1);
        assertEquals(GameStatus.AWAITING_PLAYERS, standardGame.getStatus());
        standardGame.join(p2);
        assertEquals(GameStatus.AWAITING_PLAYERS, standardGame.getStatus());
        standardGame.join(p3);
        assertEquals(GameStatus.AWAITING_PLAYERS, standardGame.getStatus());
        standardGame.join(p4);
        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, standardGame.getStatus());
        assertThrows(IllegalMoveException.class, () -> standardGame.join(new PlayerProfile("Anthony")));
    }

    @Test
    public void testGameDoubleChoiceSide(){
        standardGame.join(p1);
        standardGame.join(p2);
        standardGame.join(p3);
        standardGame.join(p4);

        assertThrows(IllegalMoveException.class, () -> standardGame.startGame());

        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, standardGame.getStatus());

        standardGame.selectStartingCardSide(p1, Side.BACK);
        standardGame.selectStartingCardSide(p2, Side.BACK);
        assertThrows(DoubleChoiceException.class, () -> standardGame.selectStartingCardSide(p2, Side.BACK));
    }

    @Test
    public void testOneTurnPerPlayer(){
        standardGame.join(p1);
        standardGame.join(p2);
        standardGame.join(p3);
        standardGame.join(p4);

        assertThrows(IllegalMoveException.class, () -> standardGame.startGame());

        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, standardGame.getStatus());

        standardGame.selectStartingCardSide(p1, Side.BACK);
        standardGame.selectStartingCardSide(p2, Side.BACK);
        standardGame.selectStartingCardSide(p3, Side.BACK);
        standardGame.selectStartingCardSide(p4, Side.BACK);

        assertEquals(GameStatus.SETUP_COLOR, standardGame.getStatus());
        assertEquals(SelectionResult.OK, standardGame.selectColor(p1, PlayerColor.RED));
        assertEquals(SelectionResult.OK, standardGame.selectColor(p2, PlayerColor.BLUE));

        assertEquals(SelectionResult.CONTENDED, standardGame.selectColor(p3, PlayerColor.RED));

        assertEquals(SelectionResult.OK, standardGame.selectColor(p3, PlayerColor.YELLOW));
        assertEquals(GameStatus.SETUP_COLOR, standardGame.getStatus());
        assertEquals(SelectionResult.OK, standardGame.selectColor(p4, PlayerColor.GREEN));


        assertEquals(GameStatus.SETUP_OBJECTIVE, standardGame.getStatus());
        Objective o1 = standardGame.getObjectiveOptions(p1).stream().findAny().orElse(null);
        standardGame.selectObjective(p1, o1);

        Objective o2 = standardGame.getObjectiveOptions(p2).stream().findAny().orElse(null);
        standardGame.selectObjective(p2, o2);

        Objective o3 = standardGame.getObjectiveOptions(p3).stream().findAny().orElse(null);
        standardGame.selectObjective(p3, o3);

        Objective o4 = standardGame.getObjectiveOptions(p4).stream().findAny().orElse(null);
        standardGame.selectObjective(p4, o4);

        assertEquals(GameStatus.AWAITING_START, standardGame.getStatus());
        standardGame.startGame();


        // FIRST PLAYER
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        PlayerProfile first = standardGame.getPlayerProfiles().getFirst();
        assertEquals(first, standardGame.getCurrentPlayer());
        Card c1 = standardGame.getPlayerData(first).getHand().getFirst();

        standardGame.pausedGame();
        assertEquals(GameStatus.SUSPENDED, standardGame.getStatus());

        assertThrows(IllegalMoveException.class, () -> standardGame.placeCard(first, c1, Side.FRONT, 1, 0));

        standardGame.resumeGame();
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        assertDoesNotThrow(() -> standardGame.placeCard(first, c1, Side.FRONT, 1, 0));

        assertEquals(c1.id(), standardGame.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.DRAWING, standardGame.getTurnPhase());
        assertEquals(DrawResult.OK, standardGame.drawCard(first, standardGame.getBoard().getFaceUpCards().stream().findAny().orElse(null)));
        assertEquals(3, standardGame.getPlayerData(first).getHand().size());
        assertEquals(4, standardGame.getBoard().getFaceUpCards().size());


        // SECOND PLAYER
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        PlayerProfile second = standardGame.getPlayerProfiles().get(1);
        assertEquals(second, standardGame.getCurrentPlayer());
        Card c2 = standardGame.getPlayerData(second).getHand().get(2); // golden card

        assertDoesNotThrow(() -> standardGame.placeCard(second, c2, Side.BACK, 1, 0));

        assertEquals(c2.id(), standardGame.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.DRAWING, standardGame.getTurnPhase());

        assertEquals(DrawResult.OK, standardGame.drawCard(second, standardGame.getBoard().getResourceCardDeck()));
        assertEquals(3, standardGame.getPlayerData(second).getHand().size());


        // THIRD PLAYER
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        PlayerProfile third = standardGame.getPlayerProfiles().get(2);
        assertEquals(third, standardGame.getCurrentPlayer());
        Card c3 = standardGame.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> standardGame.placeCard(third, c3, Side.FRONT, 1, 0));

        assertEquals(c3.id(), standardGame.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.DRAWING, standardGame.getTurnPhase());

        assertEquals(DrawResult.OK, standardGame.drawCard(third, standardGame.getBoard().getFaceUpCards().stream().filter(faceUpCard -> faceUpCard.getCard().isPresent() && !faceUpCard.getCard().orElse(null).isGold()).findAny().orElse(null)));
        assertEquals(3, standardGame.getPlayerData(third).getHand().size());


        // FOURTH PLAYER
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        PlayerProfile fourth = standardGame.getPlayerProfiles().get(3);
        assertEquals(fourth, standardGame.getCurrentPlayer());
        Card c4 = standardGame.getPlayerData(fourth).getHand().get(1); // golden card

        assertDoesNotThrow(() -> standardGame.placeCard(fourth, c4, Side.BACK, 1, 0));

        assertEquals(c4.id(), standardGame.getPlayArea(fourth).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.DRAWING, standardGame.getTurnPhase());

        assertEquals(DrawResult.OK, standardGame.drawCard(fourth, standardGame.getBoard().getGoldenCardDeck()));
        assertEquals(3, standardGame.getPlayerData(fourth).getHand().size());

        assertEquals(first, standardGame.getCurrentPlayer());
    }

    @Test
    public void testEndGameDeckEmpty(){
        
        shortGame.join(p1);
        shortGame.join(p2);
        shortGame.join(p3);

        shortGame.selectStartingCardSide(p1, Side.FRONT);
        shortGame.selectStartingCardSide(p2, Side.FRONT);
        shortGame.selectStartingCardSide(p3, Side.FRONT);

        shortGame.selectColor(p1, PlayerColor.RED);
        shortGame.selectColor(p2, PlayerColor.BLUE);
        shortGame.selectColor(p3, PlayerColor.YELLOW);

        Objective o1 = shortGame.getObjectiveOptions(p1).stream().findAny().orElse(null);
        shortGame.selectObjective(p1, o1);

        Objective o2 = shortGame.getObjectiveOptions(p2).stream().findAny().orElse(null);
        shortGame.selectObjective(p2, o2);

        Objective o3 = shortGame.getObjectiveOptions(p3).stream().findAny().orElse(null);
        shortGame.selectObjective(p3, o3);


        shortGame.startGame();
        assertEquals(GameStatus.PLAY, shortGame.getStatus());
        assertEquals(TurnPhase.PLACING, shortGame.getTurnPhase());

        // -- FIRST TURNS --

        // FIRST PLAYER
        PlayerProfile first = shortGame.getPlayerProfiles().getFirst();
        assertEquals(first, shortGame.getCurrentPlayer());
        Card c1 = shortGame.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame.placeCard(first, c1, Side.FRONT, 1, 0));

        assertEquals(c1.id(), shortGame.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame.drawCard(first, shortGame.getBoard().getResourceCardDeck()));
        // remains just one card into decks (golden card on "golden deck")

        assertEquals(3, shortGame.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        PlayerProfile second = shortGame.getPlayerProfiles().get(1);
        assertEquals(second, shortGame.getCurrentPlayer());
        Card c2 = shortGame.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame.placeCard(second, c2, Side.FRONT, 1, 0));

        assertEquals(c2.id(), shortGame.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame.drawCard(second, shortGame.getBoard().getGoldenCardDeck()));
        // decks are empty --> new phase: SECOND_LAST_TURN
        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame.getStatus());
        assertEquals(3, shortGame.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        PlayerProfile third = shortGame.getPlayerProfiles().get(2);
        assertEquals(third, shortGame.getCurrentPlayer());
        Card c3 = shortGame.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame.placeCard(third, c3, Side.FRONT, 1, 0));

        assertEquals(c3.id(), shortGame.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame.drawCard(third, shortGame.getBoard().getFaceUpCards().stream().findAny().orElse(null)));
        assertEquals(3, shortGame.getPlayerData(third).getHand().size());

        // SECOND_LAST_TURN is finished --> new phase: LAST_TURN
        assertEquals(GameStatus.LAST_TURN, shortGame.getStatus());

        // -- SECOND TURNS --
        // FIRST PLAYER
        assertEquals(first, shortGame.getCurrentPlayer());
        Card c11 = shortGame.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame.placeCard(first, c11, Side.FRONT, 0, 1));

        assertEquals(c11.id(), shortGame.getPlayArea(first).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame.getTurnPhase());
        assertEquals(2, shortGame.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        assertEquals(second, shortGame.getCurrentPlayer());
        Card c21 = shortGame.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame.placeCard(second, c21, Side.FRONT, 0, 1));

        assertEquals(c21.id(), shortGame.getPlayArea(second).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame.getTurnPhase());
        assertEquals(2, shortGame.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        assertEquals(third, shortGame.getCurrentPlayer());
        Card c31 = shortGame.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame.placeCard(third, c31, Side.FRONT, 0, 1));

        assertEquals(c31.id(), shortGame.getPlayArea(third).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame.getTurnPhase());
        assertEquals(2, shortGame.getPlayerData(third).getHand().size());

        assertEquals(GameStatus.FINISHED, shortGame.getStatus());
//        System.out.println("\nScoreboard: \n");
//        for (PlayerProfile pp : shortGame.getTotalScores().keySet()){
//            System.out.println(pp.getName() + ": " + shortGame.getTotalScores().get(pp) + " punti.");
//        }
//
//        System.out.println("\n\nWinners: \n");
//        for (PlayerProfile pp : shortGame.getWinners()){
//            System.out.println(pp.getName() + ": " + shortGame.getTotalScores().get(pp) + " punti.");
//        }
        assertTrue(shortGame.getTotalScores().size() == shortGame.getMaxPlayers() && !shortGame.getWinners().isEmpty());

    }

    @Test
    public void testEndGameTwentyPoints(){
        // TODO
    }
}