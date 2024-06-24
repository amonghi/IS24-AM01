package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.exception.*;
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
            assertDoesNotThrow(() -> {
                assertFalse(pm.getProfile("Alice").isPresent());

                PlayerProfile aProfile = controller.authenticate("Alice");
                assertEquals("Alice", aProfile.name());

                assertTrue(pm.getProfile("Alice").isPresent());
            });

        }

        @Test
        void cannotAuthenticateIfPlayerWithSameNameAlreadyExists() {
            assertDoesNotThrow(() -> {
                controller.authenticate("Alice");
                assertTrue(pm.getProfile("Alice").isPresent());
                assertThrows(NameAlreadyTakenException.class, () -> controller.authenticate("Alice"));
            });
        }
    }

    /**
     * Tests related to {@link Controller#createAndJoinGame(int, String)}
     */
    @Nested
    class CreateAndJoinGame {
        PlayerProfile alice;

        @BeforeEach
        void init() throws NameAlreadyTakenException {
            this.alice = controller.authenticate("Alice");
            assertTrue(pm.getProfile("Alice").isPresent());
            assertTrue(gm.getGames().isEmpty());
        }

        @Test
        void canCreateGame() throws NotAuthenticatedException, InvalidMaxPlayersException, IllegalGameStateException, PlayerAlreadyPlayingException {
            Game aGame = controller.createAndJoinGame(4, "Alice");
            assertEquals(1, gm.getGames().size());
            Optional<Game> gameWhereIsPlaying = gm.getGameWhereIsPlaying(alice);
            assertTrue(gameWhereIsPlaying.isPresent());
            assertEquals(aGame, gameWhereIsPlaying.get());
        }

        @Test
        void nonexistentPlayerCannotCreateGame() {
            assertTrue(pm.getProfile("Bob").isEmpty());
            assertThrows(NotAuthenticatedException.class, () -> controller.createAndJoinGame(3, "Bob"));
        }

        @Test
        void cannotCreateGameIfAlreadyPlaying() throws NotAuthenticatedException, InvalidMaxPlayersException, IllegalGameStateException, PlayerAlreadyPlayingException {
            controller.createAndJoinGame(3, "Alice");
            assertEquals(1, gm.getGames().size());
            assertTrue(gm.getGameWhereIsPlaying(alice).isPresent());
            assertThrows(PlayerAlreadyPlayingException.class, () -> controller.createAndJoinGame(4, "Alice"));
        }
    }

    abstract class WithUtils {
        PlayerProfile alice;
        PlayerProfile bob;
        Game game;

        void prepare_AWAITING_PLAYERS() throws NameAlreadyTakenException, NotAuthenticatedException, InvalidMaxPlayersException, IllegalGameStateException, PlayerAlreadyPlayingException {
            this.alice = controller.authenticate("Alice");
            this.bob = controller.authenticate("Bob");
            this.game = controller.createAndJoinGame(2, "Alice");
            assertEquals(1, gm.getGames().size());
            assertEquals(1, game.getPlayerProfiles().size());
            assertEquals(GameStatus.AWAITING_PLAYERS, game.getStatus());
        }

        void prepare_SETUP_STARTING_CARD_SIDE() throws NotAuthenticatedException, InvalidMaxPlayersException, NameAlreadyTakenException, IllegalGameStateException, PlayerAlreadyPlayingException, GameNotFoundException {
            this.prepare_AWAITING_PLAYERS();
            controller.joinGame(this.game.getId(), "Bob");
            assertEquals(2, game.getPlayerProfiles().size());
            assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());

        }

        void prepare_SETUP_COLOR() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException, InvalidMaxPlayersException, NameAlreadyTakenException, PlayerAlreadyPlayingException {
            this.prepare_SETUP_STARTING_CARD_SIDE();
            controller.selectStartingCardSide(this.game.getId(), "Alice", Side.FRONT);
            controller.selectStartingCardSide(this.game.getId(), "Bob", Side.BACK);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
        }

        void prepare_SETUP_OBJECTIVE() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException, InvalidMaxPlayersException, NameAlreadyTakenException, PlayerAlreadyPlayingException {
            this.prepare_SETUP_COLOR();
            controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED);
            controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.BLUE);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        }

        void prepare_PLAY_PLACING() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException, InvalidObjectiveException, InvalidMaxPlayersException, NameAlreadyTakenException, PlayerAlreadyPlayingException {
            this.prepare_SETUP_OBJECTIVE();

            controller.selectSecretObjective(game.getId(), "Alice",
                    game.getObjectiveOptions(alice).stream().findAny().orElseThrow().getId());
            controller.selectSecretObjective(game.getId(), "Bob",
                    game.getObjectiveOptions(bob).stream().findAny().orElseThrow().getId());
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.PLACING, game.getTurnPhase());
        }

        void prepare_PLAY_DRAWING() throws IllegalMoveException, PlayerNotInGameException, GameNotFoundException, InvalidObjectiveException, DoubleChoiceException, InvalidCardException, IllegalPlacementException, InvalidMaxPlayersException, NameAlreadyTakenException {
            this.prepare_PLAY_PLACING();

            // place a card so now we can draw
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).hand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            controller.placeCard(game.getId(), currentPlayer.name(), aCard.id(), Side.FRONT, position.i(), position.j());
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
        void init() throws NotAuthenticatedException, InvalidMaxPlayersException, NameAlreadyTakenException, IllegalGameStateException, PlayerAlreadyPlayingException {
            this.prepare_AWAITING_PLAYERS();
        }

        @Test
        void canJoinGame() {
            assertDoesNotThrow(() -> {
                controller.joinGame(game.getId(), "Bob");
                assertEquals(2, game.getPlayerProfiles().size());
            });
        }

        @Test
        void nonexistentPlayerCannotJoinGame() {
            assertTrue(pm.getProfile("Carlos").isEmpty());

            assertThrows(NotAuthenticatedException.class, () -> controller.joinGame(game.getId(), "Carlos"));
        }

        @Test
        void cannotJoinNonexistentGame() {
            assertThrows(GameNotFoundException.class, () -> controller.joinGame(1234, "Bob"));
        }

        @Test
        void cannotJoinGameIfAlreadyPlaying() {
            assertDoesNotThrow(() -> {
                Game game2 = controller.createAndJoinGame(4, "Bob");

                assertThrows(PlayerAlreadyPlayingException.class, () -> controller.joinGame(game2.getId(), "Alice"));
                assertThrows(PlayerAlreadyPlayingException.class, () -> controller.joinGame(game.getId(), "Bob"));
            });
        }
    }

    /**
     * Tests related to {@link Controller#selectStartingCardSide(int, String, Side)}
     */
    @Nested
    class SelectStartingCardSide extends WithUtils {

        @BeforeEach
        void init() throws NotAuthenticatedException, InvalidMaxPlayersException, NameAlreadyTakenException, IllegalGameStateException, PlayerAlreadyPlayingException, GameNotFoundException {
            this.prepare_SETUP_STARTING_CARD_SIDE();
        }

        @Test
        void canSelectStartingCardSide() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException {
            controller.selectStartingCardSide(game.getId(), "Alice", Side.FRONT);
            assertEquals(GameStatus.SETUP_STARTING_CARD_SIDE, game.getStatus());
            controller.selectStartingCardSide(game.getId(), "Bob", Side.BACK);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
        }

        @Test
        void cannotSelectStartingCardTwice() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException {
            controller.selectStartingCardSide(game.getId(), "Alice", Side.FRONT);
            assertThrows(DoubleChoiceException.class,
                    () -> controller.selectStartingCardSide(game.getId(), "Alice", Side.BACK));
        }

        @Test
        void nonexistentPlayerCannotMakeSelection() {
            assertThrows(NotAuthenticatedException.class,
                    () -> controller.selectStartingCardSide(game.getId(), "Carlos", Side.FRONT));
        }

        @Test
        void cannotSelectInNonexistentGame() {
            assertThrows(GameNotFoundException.class,
                    () -> controller.selectStartingCardSide(1234, "Alice", Side.FRONT));
        }
    }

    /**
     * Tests related to {@link Controller#selectPlayerColor(int, String, PlayerColor)}
     */
    @Nested
    class SelectPlayerColor extends WithUtils {

        @BeforeEach
        void init() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException, InvalidMaxPlayersException, NameAlreadyTakenException, PlayerAlreadyPlayingException {
            this.prepare_SETUP_COLOR();
        }

        @Test
        void canSelectColor() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException {
            controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());

            controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.BLUE);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        }

        @Test
        void canChangeColorUntilEveryoneHasChosen() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException {
            for (PlayerColor color : new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE}) {
                controller.selectPlayerColor(game.getId(), "Alice", color);
                assertEquals(GameStatus.SETUP_COLOR, game.getStatus());
            }

            controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.YELLOW);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());

            // it throws an IllegalMoveException instead of a DoubleChoiceException
            // because at this point the Game has changed state to GameStatus.SETUP_OBJECTIVE
            assertThrows(IllegalMoveException.class,
                    () -> controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED));
        }

        @Test
        void playersCanContendColor() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException {
            controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.RED);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());

            controller.selectPlayerColor(game.getId(), "Bob", PlayerColor.RED);
            assertEquals(GameStatus.SETUP_COLOR, game.getStatus());

            controller.selectPlayerColor(game.getId(), "Alice", PlayerColor.BLUE);
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());
        }

        @Test
        void nonexistentPlayerCannotMakeSelection() {
            assertThrows(NotAuthenticatedException.class,
                    () -> controller.selectPlayerColor(game.getId(), "Carlos", PlayerColor.RED));
        }

        @Test
        void cannotSelectInNonexistentGame() {
            assertThrows(GameNotFoundException.class,
                    () -> controller.selectPlayerColor(1234, "Alice", PlayerColor.RED));
        }
    }

    /**
     * Tests related to {@link Controller#selectSecretObjective(int, String, int)}
     */
    @Nested
    class SelectSecretObjective extends WithUtils {

        @BeforeEach
        void init() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, DoubleChoiceException, InvalidMaxPlayersException, NameAlreadyTakenException, PlayerAlreadyPlayingException {
            this.prepare_SETUP_OBJECTIVE();
        }

        @Test
        void canSelectSecretObjective() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, InvalidObjectiveException, DoubleChoiceException {
            Objective aObjective = game.getObjectiveOptions(alice).stream().findAny().orElseThrow();
            controller.selectSecretObjective(game.getId(), "Alice", aObjective.getId());
            assertEquals(GameStatus.SETUP_OBJECTIVE, game.getStatus());

            Objective bObjective = game.getObjectiveOptions(bob).stream().findAny().orElseThrow();
            controller.selectSecretObjective(game.getId(), "Bob", bObjective.getId());
            assertEquals(GameStatus.PLAY, game.getStatus());
        }

        @Test
        void cannotSelectSecretObjectiveTwice() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, InvalidObjectiveException, DoubleChoiceException {
            Iterator<Objective> iterator = game.getObjectiveOptions(alice).iterator();

            Objective objective1 = iterator.next();
            controller.selectSecretObjective(game.getId(), "Alice", objective1.getId());

            Objective objective2 = iterator.next();
            assertThrows(DoubleChoiceException.class, () -> controller.selectSecretObjective(game.getId(), "Alice", objective2.getId()));
        }

        @Test
        void nonexistentPlayerCannotMakeSelection() {
            Objective objective = game.getObjectiveOptions(alice).stream().findAny().orElseThrow();
            assertThrows(NotAuthenticatedException.class,
                    () -> controller.selectSecretObjective(game.getId(), "Carlos", objective.getId()));
        }

        @Test
        void cannotSelectInNonexistentGame() {
            Objective objective = game.getObjectiveOptions(alice).stream().findAny().orElseThrow();
            assertThrows(GameNotFoundException.class,
                    () -> controller.selectSecretObjective(1234, "Alice", objective.getId()));
        }

        @Test
        void cannotSelectObjectiveOutsideOfTheOptions() {
            Set<Objective> objectiveOptions = game.getObjectiveOptions(alice);
            Objective objective = getObjectiveOutside(objectiveOptions);

            assertThrows(InvalidObjectiveException.class,
                    () -> controller.selectSecretObjective(game.getId(), "Alice", objective.getId()));
        }
    }

    /**
     * Tests related to {@link Controller#placeCard(int, String, int, Side, int, int)}
     */
    @Nested
    class PlaceCard extends WithUtils {

        @BeforeEach
        void init() throws IllegalGameStateException, PlayerNotInGameException, NotAuthenticatedException, GameNotFoundException, InvalidObjectiveException, DoubleChoiceException, InvalidMaxPlayersException, NameAlreadyTakenException, PlayerAlreadyPlayingException {
            this.prepare_PLAY_PLACING();
        }

        @Test
        void canPlaceCard() throws IllegalMoveException, PlayerNotInGameException, GameNotFoundException, InvalidCardException, IllegalPlacementException {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).hand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            controller.placeCard(game.getId(), currentPlayer.name(), aCard.id(), Side.FRONT, position.i(), position.j());
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.DRAWING, game.getTurnPhase());
        }

        @Test
        void cannotPlaceCardInNonexistentGame() throws IllegalGameStateException {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).hand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            assertThrows(GameNotFoundException.class,
                    () -> controller.placeCard(1234, currentPlayer.name(), aCard.id(), Side.BACK, position.i(), position.j()));
        }

        @Test
        void nonexistentPlayerCannotPlaceCard() throws IllegalGameStateException {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            Card aCard = game.getPlayerData(currentPlayer).hand().getFirst();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();

            assertThrows(NotAuthenticatedException.class,
                    () -> controller.placeCard(game.getId(), "Carlos", aCard.id(), Side.BACK, position.i(), position.j()));
        }

        @Test
        void cannotPlaceCardThatIsNotInHand() throws IllegalGameStateException {
            PlayerProfile currentPlayer = game.getCurrentPlayer();
            List<Card> hand = game.getPlayerData(currentPlayer).hand();
            PlayArea.Position position = game.getPlayArea(currentPlayer).getPlayablePositions().stream().findAny()
                    .orElseThrow();
            Card notInHandCard = getCardOutsideOf(hand);

            assertThrows(GameNotFoundException.class,
                    () -> controller.placeCard(1234, currentPlayer.name(), notInHandCard.id(), Side.BACK, position.i(), position.j()));
        }
    }

    /**
     * Tests related to {@link Controller#drawCardFromDeck(int, String, DeckLocation)}
     * and {@link Controller#drawCardFromFaceUpCards(int, String, int)}
     */
    @Nested
    class DrawCard extends WithUtils {

        @BeforeEach
        void init() throws IllegalMoveException, PlayerNotInGameException, GameNotFoundException, InvalidObjectiveException, DoubleChoiceException, InvalidCardException, IllegalPlacementException, InvalidMaxPlayersException, NameAlreadyTakenException {
            this.prepare_PLAY_DRAWING();
        }

        @Test
        void canDrawFromDeck() throws IllegalMoveException, PlayerNotInGameException, GameNotFoundException {
            PlayerProfile player = game.getCurrentPlayer();
            controller.drawCardFromDeck(game.getId(), player.name(), DeckLocation.RESOURCE_CARD_DECK);
            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.PLACING, game.getTurnPhase());
            assertNotEquals(player, game.getCurrentPlayer());
        }

        @Test
        void nonexistentPlayerCannotDrawFromDeck() {
            assertThrows(NotAuthenticatedException.class,
                    () -> controller.drawCardFromDeck(game.getId(), "Carlos", DeckLocation.GOLDEN_CARD_DECK));
        }

        @Test
        void cannotDrawFromDeckInNonexistentGame() throws IllegalGameStateException {
            PlayerProfile player = game.getCurrentPlayer();
            assertThrows(GameNotFoundException.class,
                    () -> controller.drawCardFromDeck(1234, player.name(), DeckLocation.GOLDEN_CARD_DECK));
        }

        @Test
        void canDrawFromFaceUpCards() throws IllegalMoveException, PlayerNotInGameException, GameNotFoundException, InvalidCardException {
            PlayerProfile player = game.getCurrentPlayer();
            FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream().findAny().orElseThrow();

            controller.drawCardFromFaceUpCards(game.getId(), player.name(), faceUpCard.getCard().orElseThrow().id());

            assertEquals(GameStatus.PLAY, game.getStatus());
            assertEquals(TurnPhase.PLACING, game.getTurnPhase());
            assertNotEquals(player, game.getCurrentPlayer());
        }

        @Test
        void nonexistentPlayerCannotDrawFromFaceUpCards() {
            FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream().findAny().orElseThrow();
            assertThrows(NotAuthenticatedException.class,
                    () -> controller.drawCardFromFaceUpCards(game.getId(), "Carlos", faceUpCard.getCard().orElseThrow().id()));
        }

        @Test
        void cannotDrawFromFaceUpCardNonexistentGame() throws IllegalGameStateException {
            PlayerProfile player = game.getCurrentPlayer();
            FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream().findAny().orElseThrow();
            assertThrows(GameNotFoundException.class,
                    () -> controller.drawCardFromFaceUpCards(1234, player.name(), faceUpCard.getCard().orElseThrow().id()));
        }

        @Test
        void cannotDrawFaceUpCardThatIsNotPresent() throws IllegalGameStateException {
            PlayerProfile player = game.getCurrentPlayer();
            Set<Card> availableCards = game.getBoard().getFaceUpCards().stream()
                    .flatMap(faceUpCard -> faceUpCard.getCard().stream())
                    .collect(Collectors.toSet());
            Card notAvailable = getCardOutsideOf(availableCards);

            assertThrows(InvalidCardException.class,
                    () -> controller.drawCardFromFaceUpCards(game.getId(), player.name(), notAvailable.id()));
        }
    }
}
