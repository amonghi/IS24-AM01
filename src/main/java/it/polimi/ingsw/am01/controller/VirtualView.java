package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.util.*;
import java.util.stream.Collectors;

public class VirtualView implements Runnable {
    private final Controller controller;
    private final Connection<S2CNetworkMessage, C2SNetworkMessage> connection;
    private final GameManager gameManager;
    private Game game;
    private PlayerProfile playerProfile;
    private final List<EventEmitter.Registration> gameRegistrations;

    public VirtualView(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, GameManager gameManager) {
        this.controller = controller;
        this.connection = connection;
        this.gameManager = gameManager;
        this.game = null;
        this.playerProfile = null;
        this.gameRegistrations = new ArrayList<>();

        gameManager.on(GameManagerEvent.class, this::updateGameList);
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    public void setGame(Game game) {
        this.game = game;

        gameRegistrations.addAll(List.of(
                game.on(PlayerJoinedEvent.class, this::updatePlayerList),
                game.on(AllPlayersChoseStartingCardSideEvent.class, this::allPlayersChoseSide),
                game.on(AllPlayersJoinedEvent.class, this::allPlayersJoined),
                game.on(CardPlacedEvent.class, this::updatePlayArea),
                game.on(UpdateGameStatusAndTurnEvent.class, this::updateGameStatusAndTurn),
                game.on(GameFinishedEvent.class, this::gameFinished),
                game.on(AllColorChoicesSettledEvent.class, this::updateGameStatusAndSetupObjective),
                game.on(PlayerChangedColorChoiceEvent.class, this::updatePlayerColor)
        ));
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

    private void updatePlayerList(PlayerJoinedEvent event) {
        connection.send(
                new UpdatePlayerListS2C(
                        event.getPlayerList().stream().map(PlayerProfile::getName).toList()
                )
        );
    }

    private void updatePlayArea(CardPlacedEvent event) {
        connection.send(
                new UpdatePlayAreaS2C(
                        event.getPlayerName(),
                        event.getCardPlacement().getPosition().i(),
                        event.getCardPlacement().getPosition().j(),
                        event.getCardPlacement().getCard().id(),
                        event.getCardPlacement().getSide(),
                        event.getCardPlacement().getSeq(),
                        event.getCardPlacement().getPoints()
                )
        );
    }

    private void updateGameStatusAndTurn(UpdateGameStatusAndTurnEvent event) {
        connection.send(
                new UpdateGameStatusAndTurnS2C(
                        event.getGameStatus(),
                        event.getTurnPhase(),
                        event.getCurrentPlayer().getName())
        );
    }

    private void gameFinished(GameFinishedEvent event) {
        connection.send(
                new GameFinishedS2C(
                        event.getGameStatus(),
                        event.getPlayerScores().entrySet().stream()
                                .collect(Collectors.toMap(e -> e.getKey().getName(), Map.Entry::getValue))
                )
        );
    }

    private void updateGameStatusAndSetupObjective(AllColorChoicesSettledEvent event) {
        List<Objective> objectiveOptions = new ArrayList<>(game.getObjectiveOptions(playerProfile));
        connection.send(
                new UpdateGameStatusAndSetupObjectiveS2C(
                        objectiveOptions.getFirst().getId(),
                        objectiveOptions.getLast().getId()
                )
        );
    }

    private void updatePlayerColor(PlayerChangedColorChoiceEvent event) {
        connection.send(
                new UpdatePlayerColorS2C(
                        playerProfile.getName(),
                        event.getPlayerColor()
                )
        );
    }

    private void updateGameList(GameManagerEvent event){
        connection.send(
                new UpdateGameListS2C(
                        event.getGamesList().stream()
                                .collect(Collectors.toMap(
                                        Game::getId,
                                        g -> List.of(g.getPlayerProfiles().size(), g.getMaxPlayers())
                                ))
                )
        );
    }

    private void allPlayersChoseSide(AllPlayersChoseStartingCardSideEvent event){
        connection.send(
                new UpdateGameStatusS2C(event.getGameStatus())
        );
    }

    private void allPlayersJoined(AllPlayersJoinedEvent event){
        connection.send(
            new SetStartingCardS2C(game.getStartingCards().get(playerProfile).id())
        );
    }
}
