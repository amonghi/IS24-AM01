package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.model.game.*;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerData;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * The main controller
 */
public class Controller {
    private final PlayerManager playerManager;
    private final GameManager gameManager;

    public Controller(PlayerManager playerManager, GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerManager = playerManager;
    }

    /**
     * Authenticates a new player. The player must have a unique name.
     *
     * @param name the player to authenticate
     * @return the player profile
     * @see PlayerManager#createProfile(String)
     */
    public PlayerProfile authenticate(String name) throws NameAlreadyTakenException {
        return this.playerManager.createProfile(name);
    }

    private void ensureNotInGame(PlayerProfile player) throws PlayerAlreadyPlayingException {
        if (this.gameManager.getGameWhereIsPlaying(player).isPresent()) {
            throw new PlayerAlreadyPlayingException(player.getName());
        }
    }

    /**
     * Create a new game and make the creator immediately join
     *
     * @param maxPlayers the maximum amount of players that will be allowed to join the game
     * @param playerName the name of the creator of the game
     * @return the created game
     * @throws IllegalArgumentException if the specified player is already in a game
     */
    public Game createAndJoinGame(int maxPlayers, String playerName) throws PlayerAlreadyPlayingException, InvalidMaxPlayersException, IllegalGameStateException, NotAuthenticatedException {
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);
        ensureNotInGame(player);

        Game game = this.gameManager.createGame(maxPlayers);
        game.join(player);
        return game;
    }

    /**
     * Makes a player join a game
     *
     * @param gameId     the ID of the game to be joined
     * @param playerName the name of the player that will join the game
     * @see Game#join(PlayerProfile)
     */
    public void joinGame(int gameId, String playerName) throws PlayerAlreadyPlayingException, IllegalGameStateException, GameNotFoundException, NotAuthenticatedException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);
        ensureNotInGame(player);

        game.join(player);
    }

    /**
     * Starts a game, despite not having reached the maximum threshold of connected players
     *
     * @param gameId the ID of the game that have to start
     */
    public void startGame(int gameId) throws IllegalGameStateException, NotEnoughPlayersException, GameNotFoundException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        game.startGame();
    }

    /**
     * Selects on which side to place the initial card of a certain player in a certain game, then places it.
     *
     * @param gameId     the ID of the game in which the player is playing
     * @param playerName the name of the player that will place the card
     * @param side       the side on which the card will be placed
     * @see Game#selectStartingCardSide(PlayerProfile, Side)
     */
    public void selectStartingCardSide(int gameId, String playerName, Side side) throws IllegalGameStateException, PlayerNotInGameException, GameNotFoundException, NotAuthenticatedException, DoubleChoiceException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);

        game.selectStartingCardSide(player, side);
    }

    /**
     * Selects a {@link PlayerColor} for a given player in a given game
     *
     * @param gameId     the ID of the game in which the player is playing
     * @param playerName the name of the player that is making the choice
     * @param color      the color to assign to the player
     * @return {@link SelectionResult#CONTENDED} if there is some other player that also wants the same color, {@link SelectionResult#OK} otherwise
     * @see Game#selectColor(PlayerProfile, PlayerColor)
     */
    public SelectionResult selectPlayerColor(int gameId, String playerName, PlayerColor color) throws IllegalGameStateException, PlayerNotInGameException, GameNotFoundException, NotAuthenticatedException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);

        return game.selectColor(player, color);
    }

    /**
     * Selects the secret objective for a given player in a given game.
     *
     * @param gameId      the ID of the game in which the player is playing
     * @param playerName  the name of the player that is making the choice
     * @param objectiveId the id of the objective that the player has chosen.
     *                    Must be one of the objectives returned by {@link Game#getObjectiveOptions(PlayerProfile)} for the given player
     * @see Game#selectObjective(PlayerProfile, Objective)
     * @see Game#getObjectiveOptions(PlayerProfile)
     */
    public void selectSecretObjective(int gameId, String playerName, int objectiveId) throws IllegalGameStateException, PlayerNotInGameException, GameNotFoundException, NotAuthenticatedException, InvalidObjectiveException, DoubleChoiceException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);
        Objective objective = GameAssets.getInstance().getObjectiveById(objectiveId)
                .orElseThrow(InvalidObjectiveException::new);

        game.selectObjective(player, objective);
    }

    /**
     * Draws a card from the deck that is indicated by {@code deckLocation} and places it in the hand of the player
     *
     * @param gameId       the ID of the game in which the player is playing
     * @param playerName   the name of the player that is drawing the card
     * @param deckLocation which deck to draw from
     * @return {@link DrawResult#EMPTY} if the deck was empty and thus the action had no effect, {@link DrawResult#OK} otherwise
     * @see Game#drawCard(PlayerProfile, DrawSource)
     */
    public DrawResult drawCardFromDeck(int gameId, String playerName, DeckLocation deckLocation) throws IllegalTurnException, IllegalGameStateException, PlayerNotInGameException, GameNotFoundException, NotAuthenticatedException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);

        Deck deck = switch (deckLocation) {
            case RESOURCE_CARD_DECK -> game.getBoard().getResourceCardDeck();
            case GOLDEN_CARD_DECK -> game.getBoard().getGoldenCardDeck();
        };

        return game.drawCard(player, deck);
    }

    /**
     * Draws a card from the group of cards that are facing up.
     *
     * @param gameId     the ID of the game in which the player is playing
     * @param playerName the name of the player that is drawing the card
     * @param cardId     the ID of the card to draw.
     *                   Must be the ID of one of the cards contained inside a {@link FaceUpCard} contained in {@link Board#getFaceUpCards()}
     * @return always {@link DrawResult#OK} because, since the specified card must exist, it can always be put in the player's hand
     * @see Game#drawCard(PlayerProfile, DrawSource)
     */
    public DrawResult drawCardFromFaceUpCards(int gameId, String playerName, int cardId) throws IllegalTurnException, IllegalGameStateException, PlayerNotInGameException, GameNotFoundException, NotAuthenticatedException, InvalidCardException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);

        FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream()
                .filter(fuc -> fuc.getCard().map(card -> card.id() == cardId).orElse(false))
                .findAny()
                .orElseThrow(InvalidCardException::new);

        return game.drawCard(player, faceUpCard);
    }

    /**
     * Places a card
     *
     * @param gameId     the ID of the game in which the player is playing
     * @param playerName the name of the player that is placing the card
     * @param cardId     the id of the card that should be placed.
     *                   Must be the ID of one of the cards that the player has in their {@link PlayerData#getHand()}
     * @param side       specifies on which side to place the card
     * @param i          the {@code i} coordinate on which to place the card
     * @param j          the {@code j} coordinate on which to place the card
     * @see Game#placeCard(PlayerProfile, Card, Side, int, int)
     */
    public void placeCard(int gameId, String playerName, int cardId, Side side, int i, int j) throws IllegalTurnException, IllegalGameStateException, PlayerNotInGameException, GameNotFoundException, NotAuthenticatedException, InvalidCardException, IllegalPlacementException {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow(NotAuthenticatedException::new);
        Card toPlace = GameAssets.getInstance().getCardById(cardId)
                .orElseThrow(InvalidCardException::new);

        game.placeCard(player, toPlace, side, i, j);
    }
}
