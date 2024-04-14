package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
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
    public PlayerProfile authenticate(String name) {
        return this.playerManager.createProfile(name);
    }

    private void ensureNotInGame(PlayerProfile player) {
        if (this.gameManager.getGameWhereIsPlaying(player).isPresent()) {
            throw new IllegalArgumentException("This player is already playing a game.");
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
    public Game createAndJoinGame(int maxPlayers, String playerName) {
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();
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
    public void joinGame(int gameId, String playerName) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();
        ensureNotInGame(player);

        game.join(player);
    }

    /**
     * Selects on which side to place the initial card of a certain player in a certain game, then places it.
     *
     * @param gameId     the ID of the game in which the player is playing
     * @param playerName the name of the player that will place the card
     * @param side       the side on which the card will be placed
     * @see Game#selectStartingCardSide(PlayerProfile, Side)
     */
    public void selectStartingCardSide(int gameId, String playerName, Side side) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

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
    public SelectionResult selectPlayerColor(int gameId, String playerName, PlayerColor color) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

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
    public void selectSecretObjective(int gameId, String playerName, int objectiveId) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

        Objective objective = game.getObjectiveOptions(player).stream()
                .filter(o -> o.getId() == objectiveId)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("The specified objective is not a valid choice for this player"));

        game.selectObjective(player, objective);
    }

    /**
     * Starts the game.
     * <p>
     * To start the game all players must have already performed the necessary choices.
     *
     * @param gameId the ID of the game to be started
     */
    public void startGame(int gameId) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        game.startGame();
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
    public DrawResult drawCardFromDeck(int gameId, String playerName, DeckLocation deckLocation) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

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
    public DrawResult drawCardFromFaceUpCards(int gameId, String playerName, int cardId) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

        FaceUpCard faceUpCard = game.getBoard().getFaceUpCards().stream()
                .filter(fuc -> fuc.getCard().map(card -> card.id() == cardId).orElse(false))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("That card is not among the cards that are facing up"));

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
    public void placeCard(int gameId, String playerName, int cardId, Side side, int i, int j) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

        Card toPlace = game.getPlayerData(player).getHand().stream()
                .filter(card -> card.id() == cardId)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("The player does not have that card in their hand"));

        game.placeCard(player, toPlace, side, i, j);
    }
}
