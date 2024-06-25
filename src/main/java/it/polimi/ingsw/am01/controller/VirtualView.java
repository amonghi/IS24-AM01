package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.eventemitter.Event;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.chat.Message;
import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.model.game.*;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.CloseNetworkException;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.*;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class handles {@link it.polimi.ingsw.am01.network.message.NetworkMessage NetworkMessage} between client and server:
 * <li>send message to the client when events are emitted;</li>
 * <li>receive client message and manage them by calling {@link Controller}, {@link PlayerManager} and {@link GameManager} classes.</li>
 * To each player is associated an instance of this class.
 * This class is {@link Runnable} and it is executed by a different thread.
 *
 * @see it.polimi.ingsw.am01.network.message.NetworkMessage
 * @see Controller
 * @see PlayerManager
 */
public class VirtualView implements Runnable {
    private final Controller controller;
    private final Connection<S2CNetworkMessage, C2SNetworkMessage> connection;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final List<EventEmitter.Registration> gameRegistrations;
    private final List<EventEmitter.Registration> gameManagerRegistrations;
    private Game game;
    private PlayerProfile playerProfile;

    /**
     * Constructs a new instance of {@link VirtualView} with the given parameters.
     * It also makes this object listen to {@link GameManager} events.
     *
     * @param controller    the controller classes that permit to modify the model
     * @param connection    the connection between client and server
     * @param gameManager   the game handler
     * @param playerManager the player handler
     */
    public VirtualView(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, GameManager gameManager, PlayerManager playerManager) {
        this.controller = controller;
        this.connection = connection;
        this.gameManager = gameManager;
        this.playerManager = playerManager;
        this.game = null;
        this.playerProfile = null;
        this.gameRegistrations = new ArrayList<>();

        this.gameManagerRegistrations = List.of(
                this.gameManager.on(GameCreatedEvent.class, exceptionFilter(this::gameListChanged)),
                this.gameManager.on(GameDeletedEvent.class, exceptionFilter(this::gameListChanged)),
                this.gameManager.on(PlayerJoinedInGameEvent.class, exceptionFilter(this::playerJoined)),
                this.gameManager.on(PlayerLeftFromGameEvent.class, exceptionFilter(this::playerLeft)),
                this.gameManager.on(GameStartedEvent.class, exceptionFilter(this::gameListChanged))
        );

        startCheckingClientConnection();
    }

    /**
     * This method detects clients disconnection.
     *
     * @see Game#handleDisconnection(PlayerProfile)
     */
    private void startCheckingClientConnection() {
        Timer ping = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    connection.send(new PingS2C());
                } catch (SendNetworkException e) {
                    ping.cancel();
                    handleDisconnection();
                    disconnect();
                }
            }
        }, 0, 500);
    }

    /**
     * This method handles the disconnection of the relative client.
     * It removes the player from the game and from the playerManager.
     */
    private void handleDisconnection() {
        gameManagerRegistrations.forEach(gameManager::unregister);
        if (this.playerProfile != null) {
            //If the player is authenticated, I have to remove the player from playerManager
            playerManager.getProfile(this.playerProfile.name()).ifPresent(playerManager::removeProfile);
        }
        if (this.game != null) {
            //If the game is not null, I have to handle player re-connection
            game.handleDisconnection(this.playerProfile);
            setGame(null);
        }
        setPlayerProfile(null);
    }

    /**
     * This method handles the reconnection of a client.
     * It set the game to the one in which the player was playing before the disconnection.
     */
    private void handleReconnection() {
        gameManager.getGames().stream().filter(
                game -> game.getPlayerProfiles().contains(playerProfile) && !game.isConnected(playerProfile)
        ).findFirst().ifPresent(game -> {
            setGame(game);
            try {
                game.handleReconnection(playerProfile);
            } catch (PlayerNotInGameException | PlayerAlreadyConnectedException e) {
                // this should never happen
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @return the game manager
     */
    public GameManager getGameManager() {
        return this.gameManager;
    }

    /**
     * @return the game in which the player is in
     */
    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    /**
     * It sets the game to the given one and make the {@link VirtualView} listens to its events.
     *
     * @param game the game to set
     */
    private void setGame(Game game) {
        if (this.game != null) {
            gameRegistrations.forEach(this.game::unregister);
            gameRegistrations.clear();
        }
        this.game = game;

        if (this.game != null) {
            gameRegistrations.addAll(List.of(
                    game.on(AllPlayersChoseStartingCardSideEvent.class, exceptionFilter(this::allPlayersChoseSide)),
                    game.on(AllPlayersJoinedEvent.class, exceptionFilter(this::allPlayersJoined)),
                    game.on(CardPlacedEvent.class, exceptionFilter(this::updatePlayArea)),
                    game.on(UndoPlacementEvent.class, exceptionFilter(this::updatePlayAreaAfterUndo)),
                    game.on(UpdateGameStatusAndTurnEvent.class, exceptionFilter(this::updateGameStatusAndTurn)),
                    game.on(GameFinishedEvent.class, exceptionFilter(this::leaveGame)),
                    game.on(FaceUpCardReplacedEvent.class, exceptionFilter(this::updateFaceUpCards)),
                    game.on(CardDrawnFromDeckEvent.class, exceptionFilter(this::updateDeckStatus)),
                    game.on(CardDrawnFromEmptySourceEvent.class, exceptionFilter(this::notifyOfEmptySource)),
                    game.on(SecretObjectiveChosenEvent.class, exceptionFilter(this::updateChosenObjectiveList)),
                    game.on(SetUpPhaseFinishedEvent.class, exceptionFilter(this::setBoardAndHand)),
                    game.on(AllColorChoicesSettledEvent.class, exceptionFilter(this::updateGameStatusAndSetupObjective)),
                    game.on(PlayerChangedColorChoiceEvent.class, exceptionFilter(this::updatePlayerColor)),
                    game.on(HandChangedEvent.class, exceptionFilter(this::updatePlayerHand)),
                    game.on(GamePausedEvent.class, exceptionFilter(this::gamePaused)),
                    game.on(GameResumedEvent.class, exceptionFilter(this::gameResumed)),
                    game.on(NewMessageIncomingEvent.class, exceptionFilter(this::newMessage)),
                    game.on(PlayerDisconnectedEvent.class, exceptionFilter(this::playerDisconnected)),
                    game.on(PlayerReconnectedEvent.class, exceptionFilter(this::playerReconnected)),
                    game.on(GameAbortedEvent.class, exceptionFilter(this::kickAllPlayers)),
                    game.on(PlayerKickedEvent.class, exceptionFilter(this::kickPlayer))
            ));
        }
    }

    /**
     * @return the player profile associated to this {@link VirtualView} instance
     */
    public Optional<PlayerProfile> getPlayerProfile() {
        return Optional.ofNullable(playerProfile);
    }

    /**
     * It sets the player profile to the given one.
     *
     * @param playerProfile the player profile to set
     */
    private void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    /**
     * This method receives a message from the client and manages it.
     */
    @Override
    public void run() {
        while (true) {
            try {
                C2SNetworkMessage message = this.connection.receive();
                switch (message) {
                    case AuthenticateC2S m -> handleMessage(m);
                    case CreateGameAndJoinC2S m -> handleMessage(m);
                    case DrawCardFromDeckC2S m -> handleMessage(m);
                    case DrawCardFromFaceUpCardsC2S m -> handleMessage(m);
                    case JoinGameC2S m -> handleMessage(m);
                    case PlaceCardC2S m -> handleMessage(m);
                    case SelectColorC2S m -> handleMessage(m);
                    case SelectSecretObjectiveC2S m -> handleMessage(m);
                    case SelectStartingCardSideC2S m -> handleMessage(m);
                    case SendBroadcastMessageC2S m -> handleMessage(m);
                    case SendDirectMessageC2S m -> handleMessage(m);
                    case StartGameC2S m -> handleMessage(m);
                    case ResumeGameC2S m -> handleMessage(m);
                    default -> throw new NetworkException();
                }
            } catch (IllegalMoveException | NetworkException e) {
                disconnect();
                return;
            }
        }
    }

    /**
     * This method closes the connection
     */
    private void disconnect() {
        try {
            this.connection.close();
        } catch (CloseNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper method that wraps a {@link NetworkEventListener} in a {@link EventListener} that catches {@link NetworkException}
     * <p>
     * This is needed because event listeners cannot throw exceptions.
     *
     * @param listener the listener
     * @param <E>      the type of event
     * @return the wrapped listener
     */
    private <E extends Event> EventListener<E> exceptionFilter(NetworkEventListener<E> listener) {
        return (E event) -> {
            try {
                listener.onEvent(event);
            } catch (NetworkException e) {
                e.printStackTrace();
                disconnect();
            }
        };
    }

    /**
     * It handles the {@link NewMessageIncomingEvent}
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void newMessage(NewMessageIncomingEvent event) throws NetworkException {
        if (event.message().isRecipient(playerProfile)) {
            connection.send(new NewMessageS2C(
                    event.message().getMessageType(),
                    event.message().getTimestamp().toString(),
                    event.message().getSender().name(),
                    event.message().getContent()
            ));
        }
    }

    /**
     * It handles the {@link CardPlacedEvent}
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updatePlayArea(CardPlacedEvent event) throws NetworkException {
        connection.send(
                new UpdatePlayAreaS2C(
                        event.player().name(),
                        event.cardPlacement().getPosition().i(),
                        event.cardPlacement().getPosition().j(),
                        event.cardPlacement().getCard().id(),
                        event.cardPlacement().getSide(),
                        event.cardPlacement().getSeq(),
                        event.cardPlacement().getPoints()
                )
        );
    }

    /**
     * It handles the {@link UndoPlacementEvent}
     *
     * @param event The event received from {@link Game}
     * @throws SendNetworkException if there are connection problems
     */
    private void updatePlayAreaAfterUndo(UndoPlacementEvent event) throws SendNetworkException {
        connection.send(
                new UpdatePlayAreaAfterUndoS2C(
                        event.pp().name(),
                        event.position().i(),
                        event.position().j(),
                        event.score(),
                        event.seq()
                )
        );
    }

    /**
     * It handles the {@link UpdateGameStatusAndTurnEvent}
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updateGameStatusAndTurn(UpdateGameStatusAndTurnEvent event) throws NetworkException {
        GameStatus status = event.gameStatus() == GameStatus.SECOND_LAST_TURN ? GameStatus.PLAY : event.gameStatus();
        connection.send(
                new UpdateGameStatusAndTurnS2C(
                        status,
                        event.turnPhase(),
                        event.currentPlayer().name())
        );
        if (event.currentPlayer().equals(playerProfile) && event.turnPhase() == TurnPhase.PLACING) {
            connection.send(
                    new SetPlayablePositionsS2C(
                            game.getPlayArea(playerProfile).getPlayablePositions().stream().map(
                                    position -> new SetPlayablePositionsS2C.PlayablePosition(
                                            position.i(),
                                            position.j())
                            ).collect(Collectors.toList())
                    )
            );
        }
    }

    /**
     * It handles the {@link GameFinishedEvent}.
     * It also sets the current game to {@code null}
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void leaveGame(GameFinishedEvent event) throws NetworkException {
        connection.send(
                new GameFinishedS2C(
                        event.playersScores().entrySet().stream()
                                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue))
                )
        );

        setGame(null);
    }

    /**
     * It handles the {@link GameAbortedEvent}.
     * It also sets the current game to {@code null}
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void kickAllPlayers(GameAbortedEvent event) throws NetworkException {
        connection.send(
                new KickedFromGameS2C()
        );
        setGame(null);
    }

    /**
     * It handles the {@link PlayerKickedEvent}.
     * It also sets the current game to {@code null}, if this player is the kicked one
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void kickPlayer(PlayerKickedEvent event) throws NetworkException {
        if (event.player().equals(playerProfile)) {
            setGame(null);
        }
    }

    /**
     * It handles the {@link AllColorChoicesSettledEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updateGameStatusAndSetupObjective(AllColorChoicesSettledEvent event) throws NetworkException {
        List<Objective> objectiveOptions = new ArrayList<>(game.getObjectiveOptions(playerProfile));
        connection.send(
                new UpdateGameStatusAndSetupObjectiveS2C(
                        objectiveOptions.getFirst().getId(),
                        objectiveOptions.getLast().getId()
                )
        );
    }

    /**
     * It handles the {@link PlayerChangedColorChoiceEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updatePlayerColor(PlayerChangedColorChoiceEvent event) throws NetworkException {
        connection.send(
                new UpdatePlayerColorS2C(
                        event.player().name(),
                        event.playerColor()
                )
        );
    }

    /**
     * It handles the {@link FaceUpCardReplacedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updateFaceUpCards(FaceUpCardReplacedEvent event) throws NetworkException {
        connection.send(
                new UpdateFaceUpCardsS2C(
                        game.getBoard().getFaceUpCards().stream()
                                .filter(fuc -> fuc.getCard().isPresent())
                                .map(fuc -> fuc.getCard().get().id())
                                .collect(Collectors.toSet())
                )
        );
    }

    /**
     * It handles the {@link CardDrawnFromDeckEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updateDeckStatus(CardDrawnFromDeckEvent event) throws NetworkException {
        connection.send(
                new UpdateDeckStatusS2C(
                        event.resourceCardDeck().getVisibleColor().orElse(null),
                        event.goldenCardDeck().getVisibleColor().orElse(null)
                )
        );
    }

    /**
     * It handles the {@link CardDrawnFromEmptySourceEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void notifyOfEmptySource(CardDrawnFromEmptySourceEvent event) throws NetworkException {
        connection.send(
                new EmptySourceS2C(
                        event.drawSource()
                )
        );
    }

    /**
     * It handles the {@link SecretObjectiveChosenEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updateChosenObjectiveList(SecretObjectiveChosenEvent event) throws NetworkException {
        connection.send(
                new UpdateObjectiveSelectedS2C(
                        event.playersHaveChosen().stream()
                                .map(PlayerProfile::name)
                                .collect(Collectors.toSet())
                )
        );
    }

    /**
     * It handles the {@link SetUpPhaseFinishedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void setBoardAndHand(SetUpPhaseFinishedEvent event) throws NetworkException {
        Set<Integer> commonObjectives = event.commonObjective().stream().map(Objective::getId).collect(Collectors.toSet());
        Set<Integer> faceUpCards = event.faceUpCards().stream()
                .filter(fuc -> fuc.getCard().isPresent())
                .map(fuc -> fuc.getCard().get().id())
                .collect(Collectors.toSet());
        Set<Integer> hand = event.hands().get(this.playerProfile).hand().stream().map(Card::id).collect(Collectors.toSet());

        connection.send(
                new SetBoardAndHandS2C(
                        event.resourceDeck().getVisibleColor().orElse(null),
                        event.goldenDeck().getVisibleColor().orElse(null),
                        commonObjectives,
                        faceUpCards,
                        hand
                )
        );
    }

    /**
     * It handles the {@link HandChangedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void updatePlayerHand(HandChangedEvent event) throws NetworkException {
        if (event.playerProfile().equals(playerProfile)) {
            connection.send(
                    new UpdatePlayerHandS2C(
                            event.currentHand().stream().map(Card::id).collect(Collectors.toSet())
                    )
            );
        }
    }

    /**
     * It handles the {@link GameManagerEvent}.
     *
     * @param event The event received from {@link GameManager}
     * @throws NetworkException if there are connection problems
     */
    private void gameListChanged(GameManagerEvent event) throws NetworkException {
        if (game != null || playerProfile == null) {
            return;
        }

        connection.send(new UpdateGameListS2C(
                gameManager.getGames().stream()
                        .filter(game -> game.getStatus().equals(GameStatus.AWAITING_PLAYERS))
                        .collect(Collectors.toMap(
                                Game::getId,
                                game -> new UpdateGameListS2C.GameStat(
                                        game.getPlayerProfiles().size(),
                                        game.getMaxPlayers()
                                )
                        ))
        ));
    }

    /**
     * It handles the {@link AllPlayersChoseStartingCardSideEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void allPlayersChoseSide(AllPlayersChoseStartingCardSideEvent event) throws NetworkException {
        connection.send(
                new UpdateGameStatusS2C(
                        event.getGameStatus()
                )
        );
    }

    /**
     * It handles the {@link AllPlayersJoinedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void allPlayersJoined(AllPlayersJoinedEvent event) throws NetworkException {
        connection.send(
                new SetStartingCardS2C(
                        game.getStartingCards().get(playerProfile).id()
                )
        );
    }

    /**
     * It handles the {@link GamePausedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void gamePaused(GamePausedEvent event) throws NetworkException {
        connection.send(
                new SetGamePauseS2C()
        );
    }

    /**
     * It handles the {@link GameResumedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws NetworkException if there are connection problems
     */
    private void gameResumed(GameResumedEvent event) throws NetworkException {
        connection.send(new GameResumedS2C());
        connection.send(
                new UpdateGameStatusAndTurnS2C(
                        event.status(),
                        event.turnPhase(),
                        event.currentPlayer().name())
        );
        if (event.currentPlayer().equals(playerProfile) && event.turnPhase() == TurnPhase.PLACING) {
            connection.send(
                    new SetPlayablePositionsS2C(
                            game.getPlayArea(playerProfile).getPlayablePositions().stream().map(
                                    position -> new SetPlayablePositionsS2C.PlayablePosition(
                                            position.i(),
                                            position.j())
                            ).collect(Collectors.toList())
                    )
            );
        }
    }

    /**
     * It handles the {@link PlayerJoinedInGameEvent}.
     *
     * @param event The event received from {@link GameManager}
     * @throws NetworkException if there are connection problems
     */
    private void playerJoined(PlayerJoinedInGameEvent event) throws NetworkException {
        if (playerProfile == null) {
            return;
        }

        if (event.playerProfile().equals(playerProfile)) {
            setGame(event.game());
            connection.send(
                    new GameJoinedS2C(
                            event.game().getId(),
                            GameStatus.AWAITING_PLAYERS
                    )
            );
        }

        if (game != null) {
            if (game.equals(event.game())) {
                connection.send(
                        new UpdatePlayerListS2C(
                                game.getPlayerProfiles().stream().map(PlayerProfile::name).collect(Collectors.toList())
                        )
                );
            }
        } else {
            connection.send(new UpdateGameListS2C(
                    gameManager.getGames().stream().filter(game -> game.getStatus().equals(GameStatus.AWAITING_PLAYERS)).collect(Collectors.toMap(
                            Game::getId,
                            game -> new UpdateGameListS2C.GameStat(game.getPlayerProfiles().size(), game.getMaxPlayers())
                    ))
            ));
        }
    }

    /**
     * It handles the {@link PlayerLeftFromGameEvent}.
     *
     * @param event The event received from {@link GameManager}
     * @throws NetworkException if there are connection problems
     */
    private void playerLeft(PlayerLeftFromGameEvent event) throws NetworkException {
        if (playerProfile == null) {
            return;
        }

        if (game == null) {
            connection.send(new UpdateGameListS2C(
                    gameManager.getGames().stream().filter(game -> game.getStatus().equals(GameStatus.AWAITING_PLAYERS)).collect(Collectors.toMap(
                            Game::getId,
                            game -> new UpdateGameListS2C.GameStat(game.getPlayerProfiles().size(), game.getMaxPlayers())
                    ))
            ));
        } else if (game.equals(event.game())) {
            connection.send(
                    new UpdatePlayerListS2C(
                            game.getPlayerProfiles().stream().map(PlayerProfile::name).collect(Collectors.toList())
                    )
            );
        }
    }

    /**
     * It handles the {@link PlayerDisconnectedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws SendNetworkException if there are connection problems
     */
    private void playerDisconnected(PlayerDisconnectedEvent event) throws SendNetworkException {
        if (!event.pp().equals(playerProfile)) {
            connection.send(new PlayerDisconnectedS2C(event.pp().name()));
        }
    }

    /**
     * It handles the {@link PlayerReconnectedEvent}.
     *
     * @param event The event received from {@link Game}
     * @throws SendNetworkException if there are connection problems
     */
    private void playerReconnected(PlayerReconnectedEvent event) throws SendNetworkException {
        if (!event.pp().equals(playerProfile)) {
            connection.send(new PlayerReconnectedS2C(event.pp().name()));
        } else {
            try {
                connection.send(new SetupAfterReconnectionS2C(
                        game.getPlayerProfiles().stream()
                                .collect(Collectors.toMap(
                                                PlayerProfile::name,
                                                p -> game.getPlayArea(p).getCards().entrySet().stream()
                                                        .collect(Collectors.toMap(
                                                                        Map.Entry::getKey,
                                                                        e -> new SetupAfterReconnectionS2C.CardPlacement(
                                                                                e.getValue().getCard().id(),
                                                                                e.getValue().getSide(),
                                                                                e.getValue().getSeq(),
                                                                                e.getValue().getPoints()
                                                                        )
                                                                )
                                                        )
                                        )
                                ),
                        game.getCurrentPlayer().name(),
                        game.getTurnPhase(),
                        game.getStatus(),
                        game.getPlayerData(playerProfile).hand().stream().map(Card::id).collect(Collectors.toList()),
                        game.getPlayerProfiles().stream()
                                .collect(Collectors.toMap(
                                        PlayerProfile::name,
                                        player -> game.getPlayerData(player).color()
                                )),
                        game.getPlayerData(playerProfile).objective().getId(),
                        game.getCommonObjectives().stream().map(Objective::getId).collect(Collectors.toList()),
                        game.getBoard().getResourceCardDeck().getVisibleColor().orElse(null),
                        game.getBoard().getGoldenCardDeck().getVisibleColor().orElse(null),
                        game.getBoard().getFaceUpCards().stream()
                                .filter(fuc -> fuc.getCard().isPresent())
                                .map(fuc -> fuc.getCard().get().id())
                                .collect(Collectors.toList()),
                        game.getPlayerProfiles().stream().collect(Collectors.toMap(
                                PlayerProfile::name,
                                player -> game.isConnected(player)
                        )),
                        game.getChatManager().getMailbox(playerProfile).stream()
                                .map(message -> new SetupAfterReconnectionS2C.Message(
                                        message.getMessageType(),
                                        message.getSender().name(),
                                        message.getRecipient().map(PlayerProfile::name).orElse(null),
                                        message.getContent(),
                                        message.getTimestamp().toString()
                                )).toList()
                ));
            } catch (IllegalMoveException e) {
                // this should never happen
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method manages {@link AuthenticateC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(AuthenticateC2S message) throws IllegalMoveException, NetworkException {
        if (playerProfile != null) {
            return;
        }

        try {
            PlayerProfile profile = controller.authenticate(message.playerName());
            setPlayerProfile(profile);
            connection.send(
                    new SetPlayerNameS2C(profile.name())
            );
            connection.send(
                    new UpdateGameListS2C(this.getGameManager().getGames().stream().filter(game -> game.getStatus().equals(GameStatus.AWAITING_PLAYERS))
                            .collect(Collectors.toMap(
                                    Game::getId,
                                    g -> new UpdateGameListS2C.GameStat(g.getPlayerProfiles().size(), g.getMaxPlayers())
                            ))));
            handleReconnection();
        } catch (NameAlreadyTakenException e) {
            connection.send(new NameAlreadyTakenS2C(message.playerName()));
        }
    }

    /**
     * This method manages {@link CreateGameAndJoinC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(CreateGameAndJoinC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.createAndJoinGame(message.maxPlayers(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name());
        } catch (InvalidMaxPlayersException e) {
            connection.send(new InvalidMaxPlayersS2C(message.maxPlayers()));
        }
    }

    /**
     * This method manages {@link DrawCardFromDeckC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(DrawCardFromDeckC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.drawCardFromDeck(this.getGame().orElseThrow(PlayerNotInGameException::new).getId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(), message.deckLocation());
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        }
    }

    /**
     * This method manages {@link DrawCardFromFaceUpCardsC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(DrawCardFromFaceUpCardsC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.drawCardFromFaceUpCards(this.getGame().orElseThrow(PlayerNotInGameException::new).getId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(), message.cardId());
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (InvalidCardException e) {
            connection.send(new InvalidCardS2C(message.cardId()));
        }
    }

    /**
     * This method manages {@link JoinGameC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(JoinGameC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.joinGame(message.gameId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(message.gameId()));
        } catch (IllegalGameStateException e) {
            connection.send(new GameAlreadyStartedS2C(message.gameId()));
        }
    }

    /**
     * This method manages {@link PlaceCardC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(PlaceCardC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.placeCard(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(),
                    message.cardId(), message.side(), message.i(), message.j()
            );
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        } catch (IllegalPlacementException e) {
            connection.send(new InvalidPlacementS2C(message.cardId(), message.side(), message.i(), message.j()));
        } catch (InvalidCardException e) {
            connection.send(new InvalidCardS2C(message.cardId()));
        }
    }

    /**
     * This method manages {@link SelectColorC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(SelectColorC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.selectPlayerColor(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(),
                    message.color()
            );
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        }
    }

    /**
     * This method manages {@link SelectSecretObjectiveC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(SelectSecretObjectiveC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.selectSecretObjective(this.getGame().orElseThrow(PlayerNotInGameException::new).getId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(), message.objective());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        } catch (InvalidObjectiveException e) {
            connection.send(new InvalidObjectiveSelectionS2C(message.objective()));
        } catch (DoubleChoiceException e) {
            connection.send(new DoubleChoiceS2C());
        }
    }

    /**
     * This method manages {@link SelectStartingCardSideC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(SelectStartingCardSideC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.selectStartingCardSide(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(),
                    message.side()
            );
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (DoubleChoiceException e) {
            connection.send(new DoubleSideChoiceS2C(message.side()));
        }
    }

    /**
     * This method manages {@link StartGameC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(StartGameC2S message) throws IllegalMoveException, NetworkException {
        try {
            controller.startGame(this.getGame().orElseThrow(PlayerNotInGameException::new).getId());
        } catch (NotEnoughPlayersException e) {
            connection.send(new NotEnoughPlayersS2C(Objects.requireNonNull(this.getGame().orElse(null)).getPlayerProfiles().size()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        }
    }

    /**
     * This method manages {@link SendBroadcastMessageC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(SendBroadcastMessageC2S message) throws IllegalMoveException, NetworkException {
        try {
            Message chatMsg = controller.sendBroadcastMessage(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(),
                    message.content()
            );

            connection.send(new BroadcastMessageSentS2C(
                    chatMsg.getSender().name(),
                    chatMsg.getContent(),
                    chatMsg.getTimestamp().toString()
            ));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        }
    }

    /**
     * This method manages {@link SendDirectMessageC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(SendDirectMessageC2S message) throws IllegalMoveException, NetworkException {
        try {
            Message chatMsg = controller.sendDirectMessage(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).name(),
                    message.recipientPlayerName(),
                    message.content()
            );

            connection.send(new DirectMessageSentS2C(
                    chatMsg.getSender().name(),
                    message.recipientPlayerName(),
                    chatMsg.getContent(),
                    chatMsg.getTimestamp().toString()
            ));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (InvalidRecipientException | MessageSentToThemselvesException e) {
            connection.send(new InvalidRecipientS2C(
                    message.recipientPlayerName()
            ));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C());
        }
    }

    /**
     * This method manages {@link ResumeGameC2S} message
     *
     * @param message the message sent by the client
     * @throws IllegalMoveException if message is sent during wrong {@link GameStatus}
     * @throws NetworkException     if there are connection problems
     */
    public void handleMessage(ResumeGameC2S message) throws IllegalMoveException, NetworkException {
        if (game.getStatus() != GameStatus.RESTORING)
            throw new IllegalGameStateException();

        if (game.getPlayerProfiles().stream().filter(game::isConnected).count() > 1) {
            game.resumeGame();
        } else {
            connection.send(
                    new NotEnoughPlayersS2C(
                            (int) game.getPlayerProfiles().stream().filter(game::isConnected).count()
                    )
            );
        }
    }

    /**
     * A functional interface that represents a listener for network events.
     *
     * @param <E> the type of event
     */
    private interface NetworkEventListener<E extends Event> {
        void onEvent(E event) throws NetworkException;
    }
}
