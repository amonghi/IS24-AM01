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
    Game game = new Game(1, 4);
    PlayerProfile p1 = new PlayerProfile("Mattew");
    PlayerProfile p2 = new PlayerProfile("Giuly");
    PlayerProfile p3 = new PlayerProfile("David");
    PlayerProfile p4 = new PlayerProfile("George");

    @Test
    public void testMaxPlayers(){
        game.join(p1);
        assertEquals(GameStatus.AWAITING_PLAYERS, game.getStatus());
        game.join(p2);
        assertEquals(GameStatus.AWAITING_PLAYERS, game.getStatus());
        game.join(p3);
        assertEquals(GameStatus.AWAITING_PLAYERS, game.getStatus());
        game.join(p4);
        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());
        assertThrows(IllegalMoveException.class, () -> game.join(new PlayerProfile("Anthony")));
    }

    @Test
    public void testGameDoubleChoiceSide(){
        game.join(p1);
        game.join(p2);
        game.join(p3);
        game.join(p4);

        assertThrows(IllegalMoveException.class, () -> game.startGame());

        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());

        game.selectStartingCardSide(p1, Side.BACK);
        game.selectStartingCardSide(p2, Side.BACK);
        assertThrows(DoubleChoiceException.class, () -> {
            game.selectStartingCardSide(p2, Side.BACK);
        });
    }

    @Test
    public void testOneTurnPerPlayer(){
        game.join(p1);
        game.join(p2);
        game.join(p3);
        game.join(p4);

        assertThrows(IllegalMoveException.class, () -> game.startGame());

        assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());

        game.selectStartingCardSide(p1, Side.BACK);
        game.selectStartingCardSide(p2, Side.BACK);
        game.selectStartingCardSide(p3, Side.BACK);
        game.selectStartingCardSide(p4, Side.BACK);

        assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
        assertEquals(SelectionResult.OK, game.selectColor(p1, PlayerColor.RED));
        assertEquals(SelectionResult.OK, game.selectColor(p2, PlayerColor.BLUE));

        assertEquals(SelectionResult.CONTENDED, game.selectColor(p3, PlayerColor.RED));

        assertEquals(SelectionResult.OK, game.selectColor(p3, PlayerColor.YELLOW));
        assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
        assertEquals(SelectionResult.OK, game.selectColor(p4, PlayerColor.GREEN));


        assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        Objective o1 = game.getObjectiveOptions(p1).stream().findAny().orElse(null);
        game.selectObjective(p1, o1);

        Objective o2 = game.getObjectiveOptions(p2).stream().findAny().orElse(null);
        game.selectObjective(p2, o2);

        Objective o3 = game.getObjectiveOptions(p3).stream().findAny().orElse(null);
        game.selectObjective(p3, o3);

        Objective o4 = game.getObjectiveOptions(p4).stream().findAny().orElse(null);
        game.selectObjective(p4, o4);

        assertEquals(GameStatus.AWAITING_START, game.getStatus());
        game.startGame();


        // FIRST PLAYER
        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        PlayerProfile first = game.getPlayerProfiles().getFirst();
        assertEquals(first, game.getCurrentPlayer());
        Card c1 = game.getPlayerData(first).getHand().getFirst();

        game.pausedGame();
        assertEquals(GameStatus.SUSPENDED, game.getStatus());

        assertThrows(IllegalMoveException.class, () -> game.placeCard(first, c1, Side.FRONT, 1, 0));

        game.resumeGame();
        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        assertDoesNotThrow(() -> game.placeCard(first, c1, Side.FRONT, 1, 0));

        assertEquals(c1.id(), game.getPlayArea(first).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.DRAWING, game.getTurnPhase());
        assertEquals(DrawResult.OK, game.drawCard(first, game.getBoard().getFaceUpCards().stream().findAny().orElse(null)));
        assertEquals(3, game.getPlayerData(first).getHand().size());
        assertEquals(4, game.getBoard().getFaceUpCards().size());


        // SECOND PLAYER
        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        PlayerProfile second = game.getPlayerProfiles().get(1);
        assertEquals(second, game.getCurrentPlayer());
        Card c2 = game.getPlayerData(second).getHand().get(2); // golden card

        assertDoesNotThrow(() -> game.placeCard(second, c2, Side.BACK, 1, 0));

        assertEquals(c2.id(), game.getPlayArea(second).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.DRAWING, game.getTurnPhase());

        assertEquals(DrawResult.OK, game.drawCard(second, game.getBoard().getResourceCardDeck()));
        assertEquals(3, game.getPlayerData(second).getHand().size());


        // THIRD PLAYER
        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        PlayerProfile third = game.getPlayerProfiles().get(2);
        assertEquals(third, game.getCurrentPlayer());
        Card c3 = game.getPlayerData(third).getHand().getFirst(); // resource card

        assertDoesNotThrow(() -> game.placeCard(third, c3, Side.FRONT, 1, 0));

        assertEquals(c3.id(), game.getPlayArea(third).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.DRAWING, game.getTurnPhase());

        assertEquals(DrawResult.OK, game.drawCard(third, game.getBoard().getFaceUpCards().stream().filter(faceUpCard -> faceUpCard.getCard().isPresent() && !faceUpCard.getCard().orElse(null).isGold()).findAny().orElse(null)));
        assertEquals(3, game.getPlayerData(third).getHand().size());


        // FOURTH PLAYER
        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        PlayerProfile fourth = game.getPlayerProfiles().get(3);
        assertEquals(fourth, game.getCurrentPlayer());
        Card c4 = game.getPlayerData(fourth).getHand().get(1); // golden card

        assertDoesNotThrow(() -> game.placeCard(fourth, c4, Side.BACK, 1, 0));

        assertEquals(c4.id(), game.getPlayArea(fourth).getAt(1, 0).map(cp -> cp.getCard().id()).orElse(null));

        assertEquals(GameStatus.PLAY, game.getStatus());
        assertEquals(TurnPhase.DRAWING, game.getTurnPhase());

        assertEquals(DrawResult.OK, game.drawCard(fourth, game.getBoard().getGoldenCardDeck()));
        assertEquals(3, game.getPlayerData(fourth).getHand().size());

        assertEquals(first, game.getCurrentPlayer());
    }

    @Test
    public void testEndGameDeckEmpty(){
        // TODO
    }

    @Test
    public void testEndGameTwentyPoints(){
        // TODO
    }
}