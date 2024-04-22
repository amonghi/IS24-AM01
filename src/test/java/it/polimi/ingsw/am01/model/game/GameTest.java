package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game standardGame = new Game(1, 4);
    PlayerProfile p1 = new PlayerProfile("Mattew");
    PlayerProfile p2 = new PlayerProfile("Giuly");
    PlayerProfile p3 = new PlayerProfile("David");
    PlayerProfile p4 = new PlayerProfile("George");

    @Test
    public void testMaxPlayers() {
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
    public void testGameDoubleChoiceSide() {
        standardGame.join(p1);
        standardGame.join(p2);
        standardGame.join(p3);
        standardGame.join(p4);

        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, standardGame.getStatus());

        standardGame.selectStartingCardSide(p1, Side.BACK);
        standardGame.selectStartingCardSide(p2, Side.BACK);
        assertThrows(DoubleChoiceException.class, () -> standardGame.selectStartingCardSide(p2, Side.BACK));
    }

    @Test
    public void testOneTurnPerPlayerWithPause() {
        standardGame.join(p1);
        standardGame.join(p2);
        standardGame.join(p3);
        standardGame.join(p4);

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

        assertEquals(GameStatus.PLAY, standardGame.getStatus());


        // FIRST PLAYER
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        PlayerProfile first = standardGame.getPlayerProfiles().getFirst();
        assertEquals(first, standardGame.getCurrentPlayer());
        Card c1 = standardGame.getPlayerData(first).getHand().getFirst();

        standardGame.pauseGame();
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
    public void firstTestEndGameDeckEmpty() {
        Game shortGame1 = new Game(2, 3, new Board(new Deck(GameAssets.getInstance().getResourceCards().stream().limit(9).toList()),
                new Deck(GameAssets.getInstance().getGoldenCards().stream().limit(6).toList())));

        shortGame1.join(p1);
        shortGame1.join(p2);
        shortGame1.join(p3);

        shortGame1.selectStartingCardSide(p1, Side.FRONT);
        shortGame1.selectStartingCardSide(p2, Side.FRONT);
        shortGame1.selectStartingCardSide(p3, Side.FRONT);

        shortGame1.selectColor(p1, PlayerColor.RED);
        shortGame1.selectColor(p2, PlayerColor.BLUE);
        shortGame1.selectColor(p3, PlayerColor.YELLOW);

        Objective o1 = shortGame1.getObjectiveOptions(p1).stream().findAny().orElse(null);
        shortGame1.selectObjective(p1, o1);

        Objective o2 = shortGame1.getObjectiveOptions(p2).stream().findAny().orElse(null);
        shortGame1.selectObjective(p2, o2);

        Objective o3 = shortGame1.getObjectiveOptions(p3).stream().findAny().orElse(null);
        shortGame1.selectObjective(p3, o3);


        assertEquals(GameStatus.PLAY, shortGame1.getStatus());
        assertEquals(TurnPhase.PLACING, shortGame1.getTurnPhase());

        // -- FIRST TURNS --

        // FIRST PLAYER
        PlayerProfile first = shortGame1.getPlayerProfiles().getFirst();
        assertEquals(first, shortGame1.getCurrentPlayer());
        Card c1 = shortGame1.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame1.placeCard(first, c1, Side.FRONT, 1, 0));

        assertEquals(c1.id(), shortGame1.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame1.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame1.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame1.drawCard(first, shortGame1.getBoard().getResourceCardDeck()));
        // remains just one card into decks (golden card on "golden deck")

        assertEquals(3, shortGame1.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        PlayerProfile second = shortGame1.getPlayerProfiles().get(1);
        assertEquals(second, shortGame1.getCurrentPlayer());
        Card c2 = shortGame1.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame1.placeCard(second, c2, Side.FRONT, 1, 0));

        assertEquals(c2.id(), shortGame1.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame1.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame1.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame1.drawCard(second, shortGame1.getBoard().getGoldenCardDeck()));
        // decks are empty --> new phase: SECOND_LAST_TURN
        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame1.getStatus());
        assertEquals(3, shortGame1.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        PlayerProfile third = shortGame1.getPlayerProfiles().get(2);
        assertEquals(third, shortGame1.getCurrentPlayer());
        Card c3 = shortGame1.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame1.placeCard(third, c3, Side.FRONT, 1, 0));

        assertEquals(c3.id(), shortGame1.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame1.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame1.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame1.drawCard(third, shortGame1.getBoard().getFaceUpCards().stream().findAny().orElse(null)));
        assertEquals(3, shortGame1.getPlayerData(third).getHand().size());

        // SECOND_LAST_TURN is finished --> new phase: LAST_TURN
        assertEquals(GameStatus.LAST_TURN, shortGame1.getStatus());

        // -- SECOND TURNS --
        // FIRST PLAYER
        assertEquals(first, shortGame1.getCurrentPlayer());
        Card c11 = shortGame1.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame1.placeCard(first, c11, Side.FRONT, 0, 1));

        assertEquals(c11.id(), shortGame1.getPlayArea(first).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame1.getTurnPhase());
        assertEquals(2, shortGame1.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        assertEquals(second, shortGame1.getCurrentPlayer());
        Card c21 = shortGame1.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame1.placeCard(second, c21, Side.FRONT, 0, 1));

        assertEquals(c21.id(), shortGame1.getPlayArea(second).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame1.getTurnPhase());
        assertEquals(2, shortGame1.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        assertEquals(third, shortGame1.getCurrentPlayer());
        Card c31 = shortGame1.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame1.placeCard(third, c31, Side.FRONT, 0, 1));

        assertEquals(c31.id(), shortGame1.getPlayArea(third).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame1.getTurnPhase());
        assertEquals(2, shortGame1.getPlayerData(third).getHand().size());

        assertEquals(GameStatus.FINISHED, shortGame1.getStatus());
        assertTrue(shortGame1.getTotalScores().size() == shortGame1.getMaxPlayers() && !shortGame1.getWinners().isEmpty());

    }

    @Test
    public void secondTestEndGameDeckEmpty() {
        Game shortGame2 = new Game(3, 3, new Board(new Deck(GameAssets.getInstance().getResourceCards().stream().limit(10).toList()),
                new Deck(GameAssets.getInstance().getGoldenCards().stream().limit(6).toList())));

        shortGame2.join(p1);
        shortGame2.join(p2);
        shortGame2.join(p3);

        shortGame2.selectStartingCardSide(p1, Side.FRONT);
        shortGame2.selectStartingCardSide(p2, Side.FRONT);
        shortGame2.selectStartingCardSide(p3, Side.FRONT);

        shortGame2.selectColor(p1, PlayerColor.RED);
        shortGame2.selectColor(p2, PlayerColor.BLUE);
        shortGame2.selectColor(p3, PlayerColor.YELLOW);

        Objective o1 = shortGame2.getObjectiveOptions(p1).stream().findAny().orElse(null);
        shortGame2.selectObjective(p1, o1);

        Objective o2 = shortGame2.getObjectiveOptions(p2).stream().findAny().orElse(null);
        shortGame2.selectObjective(p2, o2);

        Objective o3 = shortGame2.getObjectiveOptions(p3).stream().findAny().orElse(null);
        shortGame2.selectObjective(p3, o3);


        assertEquals(GameStatus.PLAY, shortGame2.getStatus());
        assertEquals(TurnPhase.PLACING, shortGame2.getTurnPhase());

        // -- FIRST TURNS --

        // FIRST PLAYER
        PlayerProfile first = shortGame2.getPlayerProfiles().getFirst();
        assertEquals(first, shortGame2.getCurrentPlayer());
        Card c1 = shortGame2.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame2.placeCard(first, c1, Side.FRONT, 1, 0));

        assertEquals(c1.id(), shortGame2.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame2.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame2.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame2.drawCard(first, shortGame2.getBoard().getResourceCardDeck()));
        // remains two card into decks (one golden and one resource)

        assertEquals(3, shortGame2.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        PlayerProfile second = shortGame2.getPlayerProfiles().get(1);
        assertEquals(second, shortGame2.getCurrentPlayer());
        Card c2 = shortGame2.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame2.placeCard(second, c2, Side.FRONT, 1, 0));

        assertEquals(c2.id(), shortGame2.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame2.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame2.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame2.drawCard(second, shortGame2.getBoard().getGoldenCardDeck()));
        // remains just one card into decks (resource card on "golden deck")
        assertEquals(3, shortGame2.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        PlayerProfile third = shortGame2.getPlayerProfiles().get(2);
        assertEquals(third, shortGame2.getCurrentPlayer());
        Card c3 = shortGame2.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame2.placeCard(third, c3, Side.FRONT, 1, 0));

        assertEquals(c3.id(), shortGame2.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame2.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame2.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame2.drawCard(third, shortGame2.getBoard().getResourceCardDeck()));
        assertEquals(3, shortGame2.getPlayerData(third).getHand().size());

        // decks are empty and this is the last player --> new phase: LAST_TURN
        assertEquals(GameStatus.LAST_TURN, shortGame2.getStatus());

        // -- SECOND TURNS --
        // FIRST PLAYER
        assertEquals(first, shortGame2.getCurrentPlayer());
        Card c11 = shortGame2.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame2.placeCard(first, c11, Side.FRONT, 0, 1));

        assertEquals(c11.id(), shortGame2.getPlayArea(first).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame2.getTurnPhase());
        assertEquals(2, shortGame2.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        assertEquals(second, shortGame2.getCurrentPlayer());
        Card c21 = shortGame2.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame2.placeCard(second, c21, Side.FRONT, 0, 1));

        assertEquals(c21.id(), shortGame2.getPlayArea(second).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame2.getTurnPhase());
        assertEquals(2, shortGame2.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        assertEquals(third, shortGame2.getCurrentPlayer());
        Card c31 = shortGame2.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame2.placeCard(third, c31, Side.FRONT, 0, 1));

        assertEquals(c31.id(), shortGame2.getPlayArea(third).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame2.getTurnPhase());
        assertEquals(2, shortGame2.getPlayerData(third).getHand().size());

        assertEquals(GameStatus.FINISHED, shortGame2.getStatus());
        assertTrue(shortGame2.getTotalScores().size() == shortGame2.getMaxPlayers() && !shortGame2.getWinners().isEmpty());
    }

    @Test
    public void firstTestEndGameTwentyPoints() {
        List<Card> cards = new ArrayList<>();

        for (int i = 90; i < 120; i++) {
            cards.add(new Card(
                    i,
                    CardColor.RED,
                    true,
                    false,
                    new FrontCardFace(
                            Corner.empty(),
                            Corner.filled(Resource.FUNGI),
                            Corner.missing(),
                            Corner.filled(Resource.FUNGI),
                            new SimplePoints(20) // 20 "simple" points
                    ),
                    new BackCardFace(
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Map.of(Resource.FUNGI, 1)
                    )
            ));
        }
        Game shortGame3 = new Game(3, 3, new Board(new Deck(cards), new Deck(GameAssets.getInstance().getGoldenCards())));

        shortGame3.join(p1);
        shortGame3.join(p2);
        shortGame3.join(p3);

        shortGame3.selectStartingCardSide(p1, Side.FRONT);
        shortGame3.selectStartingCardSide(p2, Side.FRONT);
        shortGame3.selectStartingCardSide(p3, Side.FRONT);

        shortGame3.selectColor(p1, PlayerColor.RED);
        shortGame3.selectColor(p2, PlayerColor.BLUE);
        shortGame3.selectColor(p3, PlayerColor.YELLOW);

        Objective o1 = shortGame3.getObjectiveOptions(p1).stream().findAny().orElse(null);
        shortGame3.selectObjective(p1, o1);

        Objective o2 = shortGame3.getObjectiveOptions(p2).stream().findAny().orElse(null);
        shortGame3.selectObjective(p2, o2);

        Objective o3 = shortGame3.getObjectiveOptions(p3).stream().findAny().orElse(null);
        shortGame3.selectObjective(p3, o3);


        assertEquals(GameStatus.PLAY, shortGame3.getStatus());
        assertEquals(TurnPhase.PLACING, shortGame3.getTurnPhase());

        // -- FIRST TURNS --
        // FIRST PLAYER
        PlayerProfile first = shortGame3.getPlayerProfiles().getFirst();
        assertEquals(first, shortGame3.getCurrentPlayer());
        Card c1 = shortGame3.getPlayerData(first).getHand().get(2); // gold card

        assertDoesNotThrow(() -> shortGame3.placeCard(first, c1, Side.BACK, 1, 0));

        assertEquals(c1.id(), shortGame3.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame3.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame3.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame3.drawCard(first, shortGame3.getBoard().getResourceCardDeck()));
        assertEquals(3, shortGame3.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        PlayerProfile second = shortGame3.getPlayerProfiles().get(1);
        assertEquals(second, shortGame3.getCurrentPlayer());
        Card c2 = shortGame3.getPlayerData(second).getHand().getFirst(); // resource card --> 20 points earned --> SECOND_LAST_TURN phase

        assertDoesNotThrow(() -> shortGame3.placeCard(second, c2, Side.FRONT, 1, 0));
        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame3.getStatus());

        assertEquals(c2.id(), shortGame3.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(TurnPhase.DRAWING, shortGame3.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame3.drawCard(second, shortGame3.getBoard().getGoldenCardDeck()));
        assertEquals(3, shortGame3.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        PlayerProfile third = shortGame3.getPlayerProfiles().get(2);
        assertEquals(third, shortGame3.getCurrentPlayer());
        Card c3 = shortGame3.getPlayerData(third).getHand().get(2); // gold card

        assertDoesNotThrow(() -> shortGame3.placeCard(third, c3, Side.BACK, 1, 0));

        assertEquals(c3.id(), shortGame3.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame3.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame3.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame3.drawCard(third, shortGame3.getBoard().getResourceCardDeck()));
        assertEquals(3, shortGame3.getPlayerData(third).getHand().size());

        // a player has reached 20 points and this is the last player's turn --> LAST_TURN phase
        assertEquals(GameStatus.LAST_TURN, shortGame3.getStatus());


        // -- SECOND TURNS --
        // FIRST PLAYER
        assertEquals(first, shortGame3.getCurrentPlayer());
        Card c11 = shortGame3.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame3.placeCard(first, c11, Side.FRONT, 0, 1));

        assertEquals(c11.id(), shortGame3.getPlayArea(first).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame3.getTurnPhase());
        assertEquals(2, shortGame3.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        assertEquals(second, shortGame3.getCurrentPlayer());
        Card c21 = shortGame3.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame3.placeCard(second, c21, Side.FRONT, 0, 1));

        assertEquals(c21.id(), shortGame3.getPlayArea(second).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame3.getTurnPhase());
        assertEquals(2, shortGame3.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        assertEquals(third, shortGame3.getCurrentPlayer());
        Card c31 = shortGame3.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame3.placeCard(third, c31, Side.FRONT, 0, 1));

        assertEquals(c31.id(), shortGame3.getPlayArea(third).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame3.getTurnPhase());
        assertEquals(2, shortGame3.getPlayerData(third).getHand().size());

        assertEquals(GameStatus.FINISHED, shortGame3.getStatus());
        assertTrue(shortGame3.getTotalScores().size() == shortGame3.getMaxPlayers() && !shortGame3.getWinners().isEmpty());
    }

    @Test
    public void secondTestEndGameTwentyPoints() {
        List<Card> cards = new ArrayList<>();

        for (int i = 90; i < 120; i++) {
            cards.add(new Card(
                    i,
                    CardColor.RED,
                    true,
                    false,
                    new FrontCardFace(
                            Corner.empty(),
                            Corner.filled(Resource.FUNGI),
                            Corner.missing(),
                            Corner.filled(Resource.FUNGI),
                            new SimplePoints(20) // 20 "simple" points
                    ),
                    new BackCardFace(
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Map.of(Resource.FUNGI, 1)
                    )
            ));
        }
        Game shortGame4 = new Game(3, 3, new Board(new Deck(cards), new Deck(GameAssets.getInstance().getGoldenCards())));

        shortGame4.join(p1);
        shortGame4.join(p2);
        shortGame4.join(p3);

        shortGame4.selectStartingCardSide(p1, Side.FRONT);
        shortGame4.selectStartingCardSide(p2, Side.FRONT);
        shortGame4.selectStartingCardSide(p3, Side.FRONT);

        shortGame4.selectColor(p1, PlayerColor.RED);
        shortGame4.selectColor(p2, PlayerColor.BLUE);
        shortGame4.selectColor(p3, PlayerColor.YELLOW);

        Objective o1 = shortGame4.getObjectiveOptions(p1).stream().findAny().orElse(null);
        shortGame4.selectObjective(p1, o1);

        Objective o2 = shortGame4.getObjectiveOptions(p2).stream().findAny().orElse(null);
        shortGame4.selectObjective(p2, o2);

        Objective o3 = shortGame4.getObjectiveOptions(p3).stream().findAny().orElse(null);
        shortGame4.selectObjective(p3, o3);


        assertEquals(GameStatus.PLAY, shortGame4.getStatus());
        assertEquals(TurnPhase.PLACING, shortGame4.getTurnPhase());

        // -- FIRST TURNS --
        // FIRST PLAYER
        PlayerProfile first = shortGame4.getPlayerProfiles().getFirst();
        assertEquals(first, shortGame4.getCurrentPlayer());
        Card c1 = shortGame4.getPlayerData(first).getHand().get(2); // gold card

        assertDoesNotThrow(() -> shortGame4.placeCard(first, c1, Side.BACK, 1, 0));

        assertEquals(c1.id(), shortGame4.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, shortGame4.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame4.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame4.drawCard(first, shortGame4.getBoard().getResourceCardDeck()));
        assertEquals(3, shortGame4.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        PlayerProfile second = shortGame4.getPlayerProfiles().get(1);
        assertEquals(second, shortGame4.getCurrentPlayer());
        Card c2 = shortGame4.getPlayerData(second).getHand().get(2); // golden card

        assertDoesNotThrow(() -> shortGame4.placeCard(second, c2, Side.BACK, 1, 0));
        assertEquals(GameStatus.PLAY, shortGame4.getStatus());

        assertEquals(c2.id(), shortGame4.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(TurnPhase.DRAWING, shortGame4.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame4.drawCard(second, shortGame4.getBoard().getGoldenCardDeck()));
        assertEquals(3, shortGame4.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        PlayerProfile third = shortGame4.getPlayerProfiles().get(2);
        assertEquals(third, shortGame4.getCurrentPlayer());
        Card c3 = shortGame4.getPlayerData(third).getHand().getFirst(); // resource card --> 20 points earned and last player of rounds

        assertDoesNotThrow(() -> shortGame4.placeCard(third, c3, Side.FRONT, 1, 0));
        assertEquals(c3.id(), shortGame4.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));
        assertEquals(GameStatus.SECOND_LAST_TURN, shortGame4.getStatus());
        assertEquals(TurnPhase.DRAWING, shortGame4.getTurnPhase());

        assertEquals(DrawResult.OK, shortGame4.drawCard(third, shortGame4.getBoard().getResourceCardDeck()));
        assertEquals(GameStatus.LAST_TURN, shortGame4.getStatus());
        assertEquals(3, shortGame4.getPlayerData(third).getHand().size());


        // -- SECOND TURNS --
        // FIRST PLAYER
        assertEquals(first, shortGame4.getCurrentPlayer());
        Card c11 = shortGame4.getPlayerData(first).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame4.placeCard(first, c11, Side.FRONT, 0, 1));

        assertEquals(c11.id(), shortGame4.getPlayArea(first).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame4.getTurnPhase());
        assertEquals(2, shortGame4.getPlayerData(first).getHand().size());

        // SECOND PLAYER
        assertEquals(second, shortGame4.getCurrentPlayer());
        Card c21 = shortGame4.getPlayerData(second).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame4.placeCard(second, c21, Side.FRONT, 0, 1));

        assertEquals(c21.id(), shortGame4.getPlayArea(second).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame4.getTurnPhase());
        assertEquals(2, shortGame4.getPlayerData(second).getHand().size());

        // THIRD PLAYER
        assertEquals(third, shortGame4.getCurrentPlayer());
        Card c31 = shortGame4.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> shortGame4.placeCard(third, c31, Side.FRONT, 0, 1));

        assertEquals(c31.id(), shortGame4.getPlayArea(third).getAt(0, 1).map(cp -> cp.getCard().id()).orElse(null));


        assertEquals(TurnPhase.PLACING, shortGame4.getTurnPhase());
        assertEquals(2, shortGame4.getPlayerData(third).getHand().size());

        assertEquals(GameStatus.FINISHED, shortGame4.getStatus());
        assertTrue(shortGame4.getTotalScores().size() == shortGame4.getMaxPlayers() && !shortGame4.getWinners().isEmpty());
    }

    @Test
    public void testPlayerHasAlreadyJoined() {
        standardGame.join(p1);
        standardGame.join(p2);
        assertThrows(IllegalArgumentException.class, () -> standardGame.join(p1));

        PlayerProfile p5 = new PlayerProfile("Mattew");
        assertThrows(IllegalArgumentException.class, () -> standardGame.join(p5));
    }

    @Test
    public void testMaxPlayersNotValid() {
        assertThrows(IllegalArgumentException.class, () -> new Game(4, 1));
        assertThrows(IllegalArgumentException.class, () -> new Game(5, 5));

        Board board = new Board(new Deck(GameAssets.getInstance().getResourceCards()), new Deck(GameAssets.getInstance().getGoldenCards()));
        assertThrows(IllegalArgumentException.class, () -> new Game(6, 1, board));
        assertThrows(IllegalArgumentException.class, () -> new Game(7, 5, board));
    }

    @Test
    public void testNotPlayerTurn() {
        standardGame.join(p1);
        standardGame.join(p2);
        standardGame.join(p3);
        standardGame.join(p4);

        standardGame.selectStartingCardSide(p1, Side.BACK);
        standardGame.selectStartingCardSide(p2, Side.BACK);
        standardGame.selectStartingCardSide(p3, Side.BACK);
        standardGame.selectStartingCardSide(p4, Side.BACK);

        standardGame.selectColor(p1, PlayerColor.RED);
        standardGame.selectColor(p2, PlayerColor.BLUE);
        standardGame.selectColor(p3, PlayerColor.YELLOW);
        standardGame.selectColor(p4, PlayerColor.GREEN);

        Objective o1 = standardGame.getObjectiveOptions(p1).stream().findAny().orElse(null);
        standardGame.selectObjective(p1, o1);

        Objective o2 = standardGame.getObjectiveOptions(p2).stream().findAny().orElse(null);
        standardGame.selectObjective(p2, o2);

        Objective o3 = standardGame.getObjectiveOptions(p3).stream().findAny().orElse(null);
        standardGame.selectObjective(p3, o3);

        Objective o4 = standardGame.getObjectiveOptions(p4).stream().findAny().orElse(null);
        standardGame.selectObjective(p4, o4);


        // FIRST PLAYER
        PlayerProfile first = standardGame.getPlayerProfiles().getFirst();
        Card c1 = standardGame.getPlayerData(first).getHand().getFirst();

        standardGame.placeCard(first, c1, Side.FRONT, 1, 0);
        standardGame.drawCard(first, standardGame.getBoard().getFaceUpCards().stream().findAny().orElse(null));

        // THIRD PLAYER (wrong player!)
        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.PLACING, standardGame.getTurnPhase());
        PlayerProfile third = standardGame.getPlayerProfiles().get(2);
        Card c3 = standardGame.getPlayerData(third).getHand().get(2); // golden card

        assertThrows(IllegalTurnException.class, () -> standardGame.placeCard(third, c3, Side.BACK, 1, 0));
        PlayerProfile second = standardGame.getPlayerProfiles().get(1);
        Card c2 = standardGame.getPlayerData(second).getHand().getFirst();
        standardGame.placeCard(second, c2, Side.FRONT, 1, 0);

        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.DRAWING, standardGame.getTurnPhase());
        assertThrows(IllegalTurnException.class, () -> standardGame.drawCard(third, standardGame.getBoard().getFaceUpCards().stream().findAny().orElse(null)));
        assertEquals(standardGame.getPlayerProfiles().get(1), standardGame.getCurrentPlayer());

        assertEquals(GameStatus.PLAY, standardGame.getStatus());
        assertEquals(TurnPhase.DRAWING, standardGame.getTurnPhase());
    }

    @Test
    public void testCardNotInHand() {
        standardGame.join(p1);
        standardGame.join(p2);
        standardGame.join(p3);
        standardGame.join(p4);

        standardGame.selectStartingCardSide(p1, Side.BACK);
        standardGame.selectStartingCardSide(p2, Side.BACK);
        standardGame.selectStartingCardSide(p3, Side.BACK);
        standardGame.selectStartingCardSide(p4, Side.BACK);

        standardGame.selectColor(p1, PlayerColor.RED);
        standardGame.selectColor(p2, PlayerColor.BLUE);
        standardGame.selectColor(p3, PlayerColor.YELLOW);
        standardGame.selectColor(p4, PlayerColor.GREEN);

        Objective o1 = standardGame.getObjectiveOptions(p1).stream().findAny().orElse(null);
        standardGame.selectObjective(p1, o1);

        Objective o2 = standardGame.getObjectiveOptions(p2).stream().findAny().orElse(null);
        standardGame.selectObjective(p2, o2);

        Objective o3 = standardGame.getObjectiveOptions(p3).stream().findAny().orElse(null);
        standardGame.selectObjective(p3, o3);

        Objective o4 = standardGame.getObjectiveOptions(p4).stream().findAny().orElse(null);
        standardGame.selectObjective(p4, o4);


        // FIRST PLAYER
        PlayerProfile first = standardGame.getPlayerProfiles().getFirst();
        PlayerProfile second = standardGame.getPlayerProfiles().get(1);
        Card c2 = standardGame.getPlayerData(second).getHand().getFirst();

        assertThrows(IllegalArgumentException.class, () -> standardGame.placeCard(first, c2, Side.FRONT, 1, 0));
    }

    @Test
    public void testNotEnoughCards() {
        Game notValidGame1 = new Game(8, 2, new Board(new Deck(GameAssets.getInstance().getResourceCards().stream().limit(4).toList()),
                new Deck(GameAssets.getInstance().getGoldenCards().stream().limit(20).toList())));

        notValidGame1.join(p1);
        notValidGame1.join(p2);

        notValidGame1.selectStartingCardSide(p1, Side.BACK);
        notValidGame1.selectStartingCardSide(p2, Side.BACK);

        notValidGame1.selectColor(p1, PlayerColor.RED);
        notValidGame1.selectColor(p2, PlayerColor.BLUE);

        Objective o1 = notValidGame1.getObjectiveOptions(p1).stream().findAny().orElse(null);
        notValidGame1.selectObjective(p1, o1);

        assertThrows(NotEnoughGameResourcesException.class, () -> notValidGame1.selectObjective(p2, notValidGame1.getObjectiveOptions(p2).stream().findAny().orElse(null)));


        Game notValidGame2 = new Game(9, 2, new Board(new Deck(GameAssets.getInstance().getResourceCards().stream().limit(20).toList()),
                new Deck(GameAssets.getInstance().getGoldenCards().stream().limit(3).toList())));

        notValidGame2.join(p1);
        notValidGame2.join(p2);

        notValidGame2.selectStartingCardSide(p1, Side.BACK);
        notValidGame2.selectStartingCardSide(p2, Side.BACK);

        notValidGame2.selectColor(p1, PlayerColor.RED);
        notValidGame2.selectColor(p2, PlayerColor.BLUE);

        o1 = notValidGame2.getObjectiveOptions(p1).stream().findAny().orElse(null);
        notValidGame2.selectObjective(p1, o1);

        assertThrows(NotEnoughGameResourcesException.class, () -> notValidGame2.selectObjective(p2, notValidGame2.getObjectiveOptions(p2).stream().findAny().orElse(null)));
    }

    @Test
    public void testDecksAreEmptyOnStarting() {
        Game shortGame5 = new Game(11, 2, new Board(new Deck(GameAssets.getInstance().getResourceCards().stream().limit(6).toList()),
                new Deck(GameAssets.getInstance().getGoldenCards().stream().limit(4).toList())));

        shortGame5.join(p1);
        shortGame5.join(p2);

        shortGame5.selectStartingCardSide(p1, Side.BACK);
        shortGame5.selectStartingCardSide(p2, Side.BACK);

        shortGame5.selectColor(p1, PlayerColor.RED);
        shortGame5.selectColor(p2, PlayerColor.BLUE);

        Objective o1 = shortGame5.getObjectiveOptions(p1).stream().findAny().orElse(null);
        shortGame5.selectObjective(p1, o1);

        Objective o2 = shortGame5.getObjectiveOptions(p2).stream().findAny().orElse(null);
        shortGame5.selectObjective(p2, o2);

        assertEquals(GameStatus.LAST_TURN, shortGame5.getStatus());
    }
}