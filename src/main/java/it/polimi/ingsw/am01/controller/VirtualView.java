package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.event.UpdateGameListEvent;
import it.polimi.ingsw.am01.model.event.UpdatePlayerListEvent;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.UpdateGameListS2C;
import it.polimi.ingsw.am01.network.message.s2c.UpdatePlayerListS2C;

import java.util.Optional;
import java.util.stream.Collectors;

public class VirtualView implements Runnable {
    private final Controller controller;
    private final Connection<S2CNetworkMessage, C2SNetworkMessage> connection;
    private final GameManager gameManager;
    private Game game;
    private PlayerProfile playerProfile;

    public VirtualView(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, GameManager gameManager) {
        this.controller = controller;
        this.connection = connection;
        this.gameManager = gameManager;
        this.game = null;
        this.playerProfile = null;

        gameManager.on(UpdateGameListEvent.class, event ->
                connection.send(new UpdateGameListS2C(event.getGamesList().stream().collect(Collectors.toMap(Game::getId, Game::getMaxPlayers)))));
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    public void setGame(Game game) {
        this.game = game;

        game.on(UpdatePlayerListEvent.class, event ->
                connection.send(new UpdatePlayerListS2C(event.getPlayerList().stream().map(PlayerProfile::getName).toList())));
    }

    public Optional<PlayerProfile> getPlayerProfile() {
        return Optional.ofNullable(playerProfile);
    }

    public void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public void run() {
        while (true) {
            C2SNetworkMessage message = this.connection.receive();
            message.execute(controller, connection, this);
        }
    }
}
