package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.MessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.*;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.util.*;
import java.util.stream.Collectors;

public class VirtualView implements Runnable, MessageVisitor {
    private final Controller controller;
    private final Connection<S2CNetworkMessage, C2SNetworkMessage> connection;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final List<EventEmitter.Registration> gameRegistrations;
    private Game game;
    private PlayerProfile playerProfile;

    public VirtualView(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, GameManager gameManager, PlayerManager playerManager) {
        this.controller = controller;
        this.connection = connection;
        this.gameManager = gameManager;
        this.playerManager = playerManager;
        this.game = null;
        this.playerProfile = null;
        this.gameRegistrations = new ArrayList<>();

        this.gameManager.on(GameCreatedEvent.class, this::gameListChanged);
        this.gameManager.on(GameDeletedEvent.class, this::gameListChanged);
        this.gameManager.on(PlayerJoinedInGameEvent.class, this::playerJoined);
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    private void setGame(Game game) {
        this.game = game;

        gameRegistrations.addAll(List.of(
                game.on(AllPlayersChoseStartingCardSideEvent.class, this::allPlayersChoseSide),
                game.on(AllPlayersJoinedEvent.class, this::allPlayersJoined),
                game.on(CardPlacedEvent.class, this::updatePlayArea),
                game.on(UpdateGameStatusAndTurnEvent.class, this::updateGameStatusAndTurn),
                game.on(GameFinishedEvent.class, this::gameFinished),
                game.on(FaceUpCardReplacedEvent.class, this::updateFaceUpCards),
                game.on(CardDrawnFromDeckEvent.class, this::updateDeckStatus),
                game.on(CardDrawnFromEmptySourceEvent.class, this::notifyOfEmptySource),
                game.on(SecretObjectiveChosenEvent.class, this::updateChosenObjectiveList),
                game.on(SetUpPhaseFinishedEvent.class, this::setBoardAndHand),
                game.on(GameFinishedEvent.class, this::gameFinished),
                game.on(AllColorChoicesSettledEvent.class, this::updateGameStatusAndSetupObjective),
                game.on(PlayerChangedColorChoiceEvent.class, this::updatePlayerColor),
                game.on(HandChangedEvent.class, this::updatePlayerHand),
                game.on(GamePausedEvent.class, this::gamePaused),
                game.on(GameResumedEvent.class, this::gameResumed)
        ));
    }

    public Optional<PlayerProfile> getPlayerProfile() {
        return Optional.ofNullable(playerProfile);
    }

    private void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public void run() {
        while (true) {
            C2SNetworkMessage message = this.connection.receive();
            try {
                message.accept(this);
            } catch (IllegalMoveException e) {
                throw new RuntimeException(e); // TODO: disconnect player
            }
        }
    }

    private void updatePlayArea(CardPlacedEvent event) {
        connection.send(
                new UpdatePlayAreaS2C(
                        event.player().getName(),
                        event.cardPlacement().getPosition().i(),
                        event.cardPlacement().getPosition().j(),
                        event.cardPlacement().getCard().id(),
                        event.cardPlacement().getSide(),
                        event.cardPlacement().getSeq(),
                        event.cardPlacement().getPoints()
                )
        );
    }

    private void updateGameStatusAndTurn(UpdateGameStatusAndTurnEvent event) {
        GameStatus status = event.gameStatus() == GameStatus.SECOND_LAST_TURN ? GameStatus.PLAY : event.gameStatus();
        connection.send(
                new UpdateGameStatusAndTurnS2C(
                        status,
                        event.turnPhase(),
                        event.currentPlayer().getName())
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
                        event.player().getName(),
                        event.playerColor()
                )
        );
    }

    private void updateFaceUpCards(FaceUpCardReplacedEvent event) {
        connection.send(
                new UpdateFaceUpCardsS2C(
                        game.getBoard().getFaceUpCards().stream()
                                .map(fuc -> Objects.requireNonNull(fuc.getCard().orElse(null)).id())
                                .collect(Collectors.toUnmodifiableSet())
                )
        );
    }

    private void updateDeckStatus(CardDrawnFromDeckEvent event) {
        connection.send(
                new UpdateDeckStatusS2C(event.resourceCardDeck().isEmpty(), event.goldenCardDeck().isEmpty()));
    }

    private void notifyOfEmptySource(CardDrawnFromEmptySourceEvent event) {
        connection.send(
                new EmptySourceS2C(event.drawSource()
                )
        );
    }

    private void updateChosenObjectiveList(SecretObjectiveChosenEvent event) {
        connection.send(
                new UpdateObjectiveSelectedS2C(Collections.unmodifiableSet(
                        event.playersHaveChosen().stream()
                                .map(PlayerProfile::getName)
                                .collect(Collectors.toSet()))
                )
        );
    }

    private void setBoardAndHand(SetUpPhaseFinishedEvent event) {
        Set<Integer> commonObjectives = event.commonObjective().stream().map(Objective::getId).collect(Collectors.toUnmodifiableSet());
        Set<Integer> faceUpCards = event.faceUpCards().stream()
                .map(fuc -> Objects.requireNonNull(fuc.getCard().orElse(null)).id())
                .collect(Collectors.toUnmodifiableSet());
        Set<Integer> hand = event.hands().get(this.playerProfile).getHand().stream().map(Card::id).collect(Collectors.toUnmodifiableSet());
        connection.send(new SetBoardAndHandS2C(commonObjectives, faceUpCards, hand));
    }

    private void updatePlayerHand(HandChangedEvent event) {
        if (event.playerProfile().equals(playerProfile)) {
            connection.send(
                    new UpdatePlayerHandS2C(event.currentHand().stream().map(Card::id).collect(Collectors.toUnmodifiableSet()))
            );
        }
    }

    private void gameListChanged(GameManagerEvent event) {
        connection.send(new UpdateGameListS2C(
                gameManager.getGames().stream().collect(Collectors.toMap(
                        Game::getId,
                        game -> new UpdateGameListS2C.GameStat(game.getPlayerProfiles().size(), game.getMaxPlayers())
                ))
        ));
    }

    private void allPlayersChoseSide(AllPlayersChoseStartingCardSideEvent event) {
        connection.send(
                new UpdateGameStatusS2C(event.getGameStatus())
        );
    }

    private void allPlayersJoined(AllPlayersJoinedEvent event) {
        connection.send(
                new SetStartingCardS2C(game.getStartingCards().get(playerProfile).id())
        );
    }

    private void gamePaused(GamePausedEvent event) {
        connection.send(
                new SetGamePauseS2C()
        );
    }

    private void gameResumed(GameResumedEvent event) {
        connection.send(
                new SetRecoverStatusS2C(
                        event.recoverStatus()
                )
        );
    }

    private void playerJoined(PlayerJoinedInGameEvent event) {
        if (event.playerProfile().equals(playerProfile)) {
            setGame(event.game());
            connection.send(new GameJoinedS2C(event.game().getId(), GameStatus.AWAITING_PLAYERS));
        }
        if (game != null && game.equals(event.game())) {
            connection.send(
                    new UpdatePlayerListS2C(
                            game.getPlayerProfiles().stream().map(PlayerProfile::getName).collect(Collectors.toList())
                    )
            );
        } else {
            connection.send(new UpdateGameListS2C(
                    gameManager.getGames().stream().collect(Collectors.toMap(
                            Game::getId,
                            game -> new UpdateGameListS2C.GameStat(game.getPlayerProfiles().size(), game.getMaxPlayers())
                    ))
            ));
        }
    }

    @Override
    public void visit(AuthenticateC2S message) throws IllegalMoveException {
        try {
            PlayerProfile profile = controller.authenticate(message.playerName());
            setPlayerProfile(profile);
            connection.send(new SetPlayerNameS2C(profile.getName()));
            connection.send(new UpdateGameListS2C(this.getGameManager().getGames().stream().collect(Collectors.toMap(
                    Game::getId,
                    g -> new UpdateGameListS2C.GameStat(g.getPlayerProfiles().size(), g.getMaxPlayers())
            ))));
        } catch (NameAlreadyTakenException e) {
            connection.send(new NameAlreadyTakenS2C(message.playerName()));
        }
    }

    @Override
    public void visit(CreateGameAndJoinC2S message) throws IllegalMoveException {
        try {
            controller.createAndJoinGame(message.maxPlayers(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName());
        } catch (InvalidMaxPlayersException e) {
            connection.send(new InvalidMaxPlayersS2C(message.maxPlayers()));
        }
    }

    @Override
    public void visit(DrawCardFromDeckC2S message) throws IllegalMoveException {
        try {
            controller.drawCardFromDeck(this.getGame().orElseThrow(PlayerNotInGameException::new).getId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(), message.deckLocation());
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        }
    }

    @Override
    public void visit(DrawCardFromFaceUpCardsC2S message) throws IllegalMoveException {
        try {
            //FIXME: should we need to send back drawResult?
            controller.drawCardFromFaceUpCards(this.getGame().orElseThrow(PlayerNotInGameException::new).getId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(), message.cardId());
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (InvalidCardException e) {
            connection.send(new InvalidCardS2C(message.cardId()));
        }
    }

    @Override
    public void visit(JoinGameC2S message) throws IllegalMoveException {
        try {
            controller.joinGame(message.gameId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(message.gameId()));
        } catch (IllegalGameStateException e) {
            connection.send(new GameAlreadyStartedS2C(message.gameId()));
        }
    }

    @Override
    public void visit(PlaceCardC2S message) throws IllegalMoveException {
        try {
            controller.placeCard(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(),
                    message.cardId(), message.side(), message.i(), message.j()
            );
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        } catch (IllegalGameStateException e) { //TODO: maybe remove
            connection.send(new InvalidGameStateS2C());
        } catch (IllegalPlacementException e) {
            connection.send(new InvalidPlacementS2C(message.cardId(), message.side(), message.i(), message.j()));
        } catch (InvalidCardException e) {
            connection.send(new InvalidCardS2C(message.cardId()));
        }
    }

    @Override
    public void visit(SelectColorC2S message) throws IllegalMoveException {
        try {
            controller.selectPlayerColor(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(),
                    message.color()
            );
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        }
    }

    @Override
    public void visit(SelectSecretObjectiveC2S message) throws IllegalMoveException {
        try {
            controller.selectSecretObjective(this.getGame().orElseThrow(PlayerNotInGameException::new).getId(), this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(), message.objective());
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        } catch (InvalidObjectiveException e) {
            connection.send(new InvalidObjectiveSelectionS2C(message.objective()));
        } catch (DoubleChoiceException e) {
            connection.send(new DoubleChoiceS2C());
        }
    }

    @Override
    public void visit(SelectStartingCardSideC2S message) throws IllegalMoveException {
        try {
            controller.selectStartingCardSide(
                    this.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    this.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(),
                    message.side()
            );
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (DoubleChoiceException e) {
            connection.send(new DoubleSideChoiceS2C(message.side()));
        }
    }

    @Override
    public void visit(StartGameC2S message) throws IllegalMoveException {
        try {
            controller.startGame(this.getGame().orElseThrow(PlayerNotInGameException::new).getId());
        } catch (NotEnoughPlayersException e) {
            connection.send(new NotEnoughPlayersS2C(Objects.requireNonNull(this.getGame().orElse(null)).getPlayerProfiles().size()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(this.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(this.getPlayerProfile().orElse(null)).getName()));
        }
    }
}
