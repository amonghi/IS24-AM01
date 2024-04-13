package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.SelectionResult;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public class Controller {
    private final PlayerManager playerManager;
    private final GameManager gameManager;

    public Controller(PlayerManager playerManager, GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerManager = playerManager;
    }

    public PlayerProfile authenticate(String name) {
        return this.playerManager.createProfile(name);
    }

    private void ensureNotInGame(PlayerProfile player) {
        if (this.gameManager.getGameWhereIsPlaying(player).isPresent()) {
            throw new IllegalArgumentException("This player is already playing a game.");
        }
    }

    public Game createAndJoinGame(int maxPlayers, String playerName) {
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();
        ensureNotInGame(player);

        Game game = this.gameManager.createGame(maxPlayers);
        game.join(player);
        return game;
    }

    public void joinGame(int gameId, String playerName) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();
        ensureNotInGame(player);

        game.join(player);
    }

    public void selectStartingCardSide(int gameId, String playerName, Side side) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

        game.selectStartingCardSide(player, side);
    }

    public SelectionResult selectPlayerColor(int gameId, String playerName, PlayerColor color) {
        Game game = this.gameManager.getGame(gameId)
                .orElseThrow();
        PlayerProfile player = this.playerManager.getProfile(playerName)
                .orElseThrow();

        return game.selectColor(player, color);
    }

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
}
