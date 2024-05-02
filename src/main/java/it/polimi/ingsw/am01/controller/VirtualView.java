package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.util.Map;
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

        game.on(CardPlacedEvent.class, event -> {
            connection.send(new UpdatePlayAreaS2C(event.getPlayerName(), event.getCardPlacement().getPosition().i(),
                    event.getCardPlacement().getPosition().j(), event.getCardPlacement().getCard().id(), event.getCardPlacement().getSide(),
                    event.getCardPlacement().getSeq(), event.getCardPlacement().getPoints()));
        });

        game.on(UpdateGameStatusAndTurnEvent.class, event -> {
            connection.send(new UpdateGameStatusAndTurnS2C(event.getGameStatus(), event.getTurnPhase(), event.getCurrentPlayer().getName()));
        });

        game.on(GameFinishedEvent.class, event -> {
            connection.send(new GameFinishedS2C(event.getGameStatus(), event.getPlayerScores().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getName(), Map.Entry::getValue))));
        });
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
            try {
                message.execute(controller, connection, this);
            } catch (IllegalMoveException e) {
                throw new RuntimeException(e); // TODO: disconnect player
            }
        }
    }
}
