package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
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
}
