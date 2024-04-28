package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.game.*;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    PlayerManager pm;
    GameManager gm;
    Controller controller;

    @BeforeEach
    void init(@TempDir Path dataDir) {
        this.pm = new PlayerManager();
        this.gm = new GameManager(dataDir);
        this.controller = new Controller(this.pm, this.gm);
    }

    /**
     * Tests related to {@link Controller#authenticate(String)}
     */
    @Nested
    class Authenticate {
        @Test
        void authenticatesPlayer() {
            assertFalse(pm.getProfile("Alice").isPresent());

            PlayerProfile aProfile = controller.authenticate("Alice");
            assertEquals("Alice", aProfile.getName());

            assertTrue(pm.getProfile("Alice").isPresent());
        }

        @Test
        void cannotAuthenticateIfPlayerWithSameNameAlreadyExists() {
            controller.authenticate("Alice");
            assertTrue(pm.getProfile("Alice").isPresent());

            assertThrows(IllegalArgumentException.class, () -> controller.authenticate("Alice"));
        }
    }

    /**
     * Tests related to {@link Controller#createAndJoinGame(int, String)}
     */
    @Nested
    class CreateAndJoinGame {
        PlayerProfile alice;

        @BeforeEach
        void init() {
            this.alice = controller.authenticate("Alice");
            assertTrue(pm.getProfile("Alice").isPresent());
            assertTrue(gm.getGames().isEmpty());
        }

        @Test
        void canCreateGame() {
            Game aGame = controller.createAndJoinGame(4, "Alice");
            assertEquals(1, gm.getGames().size());

            Optional<Game> gameWhereIsPlaying = gm.getGameWhereIsPlaying(alice);
            assertTrue(gameWhereIsPlaying.isPresent());
            assertEquals(aGame, gameWhereIsPlaying.get());
        }

        @Test
        void nonexistentPlayerCannotCreateGame() {
            assertTrue(pm.getProfile("Bob").isEmpty());
            assertThrows(NoSuchElementException.class, () -> controller.createAndJoinGame(3, "Bob"));
        }

        @Test
        void cannotCreateGameIfAlreadyPlaying() {
            controller.createAndJoinGame(3, "Alice");
            assertEquals(1, gm.getGames().size());
            assertTrue(gm.getGameWhereIsPlaying(alice).isPresent());

            assertThrows(IllegalArgumentException.class, () -> controller.createAndJoinGame(4, "Alice"));
        }
    }

    abstract class WithUtils {
        PlayerProfile alice;
        PlayerProfile bob;
        Game game;

        void prepare_AWAITING_PLAYERS() {
            this.alice = controller.authenticate("Alice");
            this.bob = controller.authenticate("Bob");
            this.game = controller.createAndJoinGame(2, "Alice");
            assertEquals(1, gm.getGames().size());
            assertEquals(1, game.getPlayerProfiles().size());
            assertEquals(GameStatus.AWAITING_PLAYERS, game.getStatus());
        }

        void prepare_SETUP_STARTING_CARD_SIDE() {
            this.prepare_AWAITING_PLAYERS();

            controller.joinGame(this.game.getId(), "Bob");
            assertEquals(2, game.getPlayerProfiles().size());
            assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());
        }

        void prepare_SETUP_COLOR() {
            this.prepare_SETUP_STARTING_CARD_SIDE();

            controller.selectStartingCardSide(this.game.getId(), "Alice", Side.FRONT);
            controller.selectStartingCardSide(this.game.getId(), "Bob", Side.BACK);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
        }

        void prepare_SETUP_OBJECTIVE() {
            this.prepare_SETUP_COLOR();

            controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED);
            controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.BLUE);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        }

        void prepare_PLAY_PLACING() {
            this.prepare_SETUP_OBJECTIVE();

            controller.selectSecretObjective(game.getId(), "Alice",
                    game.getObjectiveOptions(alice).stream().findAny().orElseThrow().getId());
            controller.selectSecretObjective(game.getId(), "Bob",
                    game.getObjectiveOptions(bob).stream().findAny().orElseThrow().getId());
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        }

        void prepare_PLAY_DRAWING() {
            this.prepare_PLAY_PLACING();

            // place a card so now we can draw
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).getHand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            controller.placeCard(game.getId(), currentPlayer.getName(), aCard.id(), Side.FRONT, position.i(), position.j());
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.DRAWING, game.getTurnPhase());
        }

        <E> E getOutside(Stream<E> universe, Collection<E> excluded) {
            return universe.filter(e -> !excluded.contains(e)).findAny().orElseThrow();
        }

        Card getCardOutsideOf(Collection<Card> excluded) {
            Stream<Card> universe = concat(
                    GameAssets.getInstance().getResourceCards().stream(),
                    GameAssets.getInstance().getGoldenCards().stream());

            return getOutside(universe, excluded);
        }

        Objective getObjectiveOutside(Collection<Objective> excluded) {
            Stream<Objective> universe = GameAssets.getInstance().getObjectives().stream();
            return getOutside(universe, excluded);
        }
    }

    /**
     * Tests related to {@link Controller#joinGame(int, String)}
     */
    @Nested
    class JoinGame extends WithUtils {

        @BeforeEach
        void init() {
            this.prepare_AWAITING_PLAYERS();
        }

        @Test
        void canJoinGame() {
            controller.joinGame(game.getId(), "Bob");
            assertEquals(2, game.getPlayerProfiles().size());
        }

        @Test
        void nonexistentPlayerCannotJoinGame() {
            assertTrue(pm.getProfile("Carlos").isEmpty());

            assertThrows(NoSuchElementException.class, () -> controller.joinGame(game.getId(), "Carlos"));
        }

        @Test
        void cannotJoinNonexistentGame() {
            assertThrows(NoSuchElementException.class, () -> controller.joinGame(1234, "Bob"));
        }

        @Test
        void cannotJoinGameIfAlreadyPlaying() {
            Game game2 = controller.createAndJoinGame(4, "Bob");

            assertThrows(IllegalArgumentException.class, () -> controller.joinGame(game2.getId(), "Alice"));
            assertThrows(IllegalArgumentException.class, () -> controller.joinGame(game.getId(), "Bob"));
        }
    }

    /**
     * Tests related to {@link Controller#selectStartingCardSide(int, String, Side)}
     */
    @Nested
    class SelectStartingCardSide extends WithUtils {

        @BeforeEach
        void init() {
            this.prepare_SETUP_STARTING_CARD_SIDE();
        }

        @Test
        void canSelectStartingCardSide() {
            controller.selectStartingCardSide(game.getId(), "Alice", Side.FRONT);
            assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());
            controller.selectStartingCardSide(game.getId(), "Bob", Side.BACK);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
        }

        @Test
        void cannotSelectStartingCardTwice() {
            controller.selectStartingCardSide(game.getId(), "Alice", Side.FRONT);
            assertThrows(DoubleChoiceException.class,
                    () -> controller.selectStartingCardSide(game.getId(), "Alice", Side.BACK));
        }

        @Test
        void nonexistentPlayerCannotMakeSelection() {
            assertThrows(NoSuchElementException.class,
                    () -> controller.selectStartingCardSide(game.getId(), "Carlos", Side.FRONT));
        }

        @Test
        void cannotSelectInNonexistentGame() {
            assertThrows(NoSuchElementException.class,
                    () -> controller.selectStartingCardSide(1234, "Alice", Side.FRONT));
        }
    }

    /**
     * Tests related to {@link Controller#selectPlayerColor(int, String, PlayerColor)}
     */
    @Nested
    class SelectPlayerColor extends WithUtils {

        @BeforeEach
        void init() {
            this.prepare_SETUP_COLOR();
        }

        @Test
        void canSelectColor() {
            SelectionResult aResult = controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED);
            assertEquals(SelectionResult.OK, aResult);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());

            SelectionResult bResult = controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.BLUE);
            assertEquals(SelectionResult.OK, bResult);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        }

        @Test
        void canChangeColorUntilEveryoneHasChosen() {
            for (PlayerColor color : new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE}) {
                SelectionResult result = controller.selectPlayerColor(game.getId(), "Alice", color);
                assertEquals(SelectionResult.OK, result);
                assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
            }

            SelectionResult bResult = controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.YELLOW);
            assertEquals(SelectionResult.OK, bResult);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());

            // it throws an IllegalMoveException instead of a DoubleChoiceException
            // because at this point the Game has changed state to GameStatus.SETUP_OBJECTIVE
            assertThrows(IllegalMoveException.class,
                    () -> controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED));
        }

        @Test
        void playersCanContendColor() {
            SelectionResult aResult = controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED);
            assertEquals(SelectionResult.OK, aResult);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());

            SelectionResult bResult = controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.RED);
            assertEquals(SelectionResult.CONTENDED, bResult);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());

            SelectionResult bResult2 = controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.BLUE);
            assertEquals(SelectionResult.OK, bResult2);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        }

        @Test
        void nonexistentPlayerCannotMakeSelection() {
            assertThrows(NoSuchElementException.class,
                    () -> controller.selectPlayerColor(game.getId(), "Carlos", PlayerColor.RED));
        }

        @Test
        void cannotSelectInNonexistentGame() {
            assertThrows(NoSuchElementException.class,
                    () -> controller.selectPlayerColor(1234, "Alice", PlayerColor.RED));
        }
    }

    /**
     * Tests related to {@link Controller#selectSecretObjective(int, String, int)}
     */
    @Nested
    class SelectSecretObjective extends WithUtils {

        @BeforeEach
        void init() {
            this.prepare_SETUP_OBJECTIVE();
        }

        @Test
        void canSelectSecretObjective() {
            Objective aObjective = game.getObjectiveOptions(alice).stream().findAny().orElseThrow();
            controller.selectSecretObjective(game.getId(), "Alice", aObjective.getId());
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());

            Objective bObjective = game.getObjectiveOptions(bob).stream().findAny().orElseThrow();
            controller.selectSecretObjective(game.getId(), "Bob", bObjective.getId());
            assertEquals(GameStatus.PLAY, game.getStatus());
        }

        @Test
        void cannotSelectSecretObjectiveTwice() {
            Iterator<Objective> iterator = game.getObjectiveOptions(alice).iterator();

            Objective objective1 = iterator.next();
            controller.selectSecretObjective(game.getId(), "Alice", objective1.getId());

            Objective objective2 = iterator.next();
            assertThrows(DoubleChoiceException.class, () -> controller.selectSecretObjective(game.getId(), "Alice", objective2.getId()));
        }

        @Test
        void nonexistentPlayerCannotMakeSelection() {
            Objective objective = game.getObjectiveOptions(alice).stream().findAny().orElseThrow();
            assertThrows(NoSuchElementException.class,
                    () -> controller.selectSecretObjective(game.getId(), "Carlos", objective.getId()));
        }

        @Test
        void cannotSelectInNonexistentGame() {
            Objective objective = game.getObjectiveOptions(alice).stream().findAny().orElseThrow();
            assertThrows(NoSuchElementException.class,
                    () -> controller.selectSecretObjective(1234, "Alice", objective.getId()));
        }

        @Test
        void cannotSelectObjectiveOutsideOfTheOptions() {
            Set<Objective> objectiveOptions = game.getObjectiveOptions(alice);
            Objective objective = getObjectiveOutside(objectiveOptions);

            assertThrows(NoSuchElementException.class,
                    () -> controller.selectSecretObjective(game.getId(), "Alice", objective.getId()));
        }
    }

    /**
     * Tests related to {@link Controller#placeCard(int, String, int, Side, int, int)}
     */
    @Nested
    class PlaceCard extends WithUtils {

        @BeforeEach
        void init() {
            this.prepare_PLAY_PLACING();
        }

        @Test
        void canPlaceCard() {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).getHand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            controller.placeCard(game.getId(), currentPlayer.getName(), aCard.id(), Side.FRONT, position.i(), position.j());
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.DRAWING, game.getTurnPhase());
        }

        @Test
        void cannotPlaceCardInNonexistentGame() {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).getHand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            assertThrows(NoSuchElementException.class,
                    () -> controller.placeCard(1234, currentPlayer.getName(), aCard.id(), Side.BACK, position.i(), position.j()));
        }

        @Test
        void nonexistentPlayerCannotPlaceCard() {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).getHand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            assertThrows(NoSuchElementException.class,
                    () -> controller.placeCard(game.getId(), "Carlos", aCard.id(), Side.BACK, position.i(), position.j()));
        }

        @Test
        void cannotPlaceCardThatIsNotInHand() {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            List<Card> hand = game.getPlayerData(currentPlayer).getHand();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();
            Card notInHandCard = getCardOutsideOf(hand);

            assertThrows(NoSuchElementException.class,
                    () -> controller.placeCard(1234, currentPlayer.getName(), notInHandCard.id(), Side.BACK, position.i(), position.j()));
        }
    }

    /**
     * Tests related to {@link Controller#drawCardFromDeck(int, String, DeckLocation)}
     * and {@link Controller#drawCardFromFaceUpCards(int, String, int)}
     */
    @Nested
    class DrawCard extends WithUtils {

        @BeforeEach
        void init() {
            this.prepare_PLAY_DRAWING();
        }

        @Test
        void canDrawFromDeck() {
            PlayerProfile player = game.getCurrentPlayer();
            controller.drawCardFromDeck(game.getId(), player.getName(), DeckLocation.RESOURCE_CARD_DECK);
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.PLACING, game.getTurnPhase());
            assertNotEquals(player, game.getCurrentPlayer());
        }

        @Test
        void nonexistentPlayerCannotDrawFromDeck() {
            assertThrows(NoSuchElementException.class,
                    () -> controller.drawCardFromDeck(game.getId(), "Carlos", DeckLocation.GOLDEN_CARD_DECK));
        }

        @Test
        void cannotDrawFromDeckInNonexistentGame() {
            PlayerProfile player = game.getCurrentPlayer();
            assertThrows(NoSuchElementException.class,
                    () -> controller.drawCardFromDeck(1234, player.getName(), DeckLocation.GOLDEN_CARD_DECK));
        }

        @Test
        void canDrawFromFaceUpCards() {
            PlayerProfile player = game.getCurrentPlayer();
            FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream().findAny().orElseThrow();

            controller.drawCardFromFaceUpCards(game.getId(), player.getName(), faceUpCard.getCard().orElseThrow().id());

            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.PLACING, game.getTurnPhase());
            assertNotEquals(player, game.getCurrentPlayer());
        }

        @Test
        void nonexistentPlayerCannotDrawFromFaceUpCards() {
            FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream().findAny().orElseThrow();
            assertThrows(NoSuchElementException.class,
                    () -> controller.drawCardFromFaceUpCards(game.getId(), "Carlos", faceUpCard.getCard().orElseThrow().id()));
        }

        @Test
        void cannotDrawFromFaceUpCardNonexistentGame() {
            PlayerProfile player = game.getCurrentPlayer();
            FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream().findAny().orElseThrow();
            assertThrows(NoSuchElementException.class,
                    () -> controller.drawCardFromFaceUpCards(1234, player.getName(), faceUpCard.getCard().orElseThrow().id()));
        }

        @Test
        void cannotDrawFaceUpCardThatIsNotPresent() {
            PlayerProfile player = game.getCurrentPlayer();
            Set<Card> availableCards = game.getBoard().getFaceUpCards().stream()
                    .flatMap(faceUpCard -> faceUpCard.getCard().stream())
                    .collect(Collectors.toSet());
            Card notAvailable = getCardOutsideOf(availableCards);

            assertThrows(IllegalArgumentException.class,
                    () -> controller.drawCardFromFaceUpCards(game.getId(), player.getName(), notAvailable.id()));
        }
    }
}
