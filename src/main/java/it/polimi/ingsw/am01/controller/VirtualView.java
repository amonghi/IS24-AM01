package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.game.FaceUpCard;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.game.GameStatus;
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
    private final List<EventEmitter.Registration> gameRegistrations;
    private Game game;
    private PlayerProfile playerProfile;

    public VirtualView(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, GameManager gameManager) {
        this.controller = controller;
        this.connection = connection;
        this.gameManager = gameManager;
        this.game = null;
        this.playerProfile = null;
        this.gameRegistrations = new ArrayList<>();

        gameManager.on(GameCreatedEvent.class, this::gameListChanged);
        gameManager.on(GameDeletedEvent.class, this::gameListChanged);
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public Optional<Game> getGame() {
        return Optional.ofNullable(game);
    }

    public void setGame(Game game) {
        this.game = game;

        for(FaceUpCard fuc : game.getBoard().getFaceUpCards()) {
            fuc.on(FaceUpCardReplacedEvent.class, this::updateFaceUpCards);
        }

        gameRegistrations.addAll(List.of(
                game.on(PlayerJoinedEvent.class, this::updatePlayerList),
                game.on(AllPlayersChoseStartingCardSideEvent.class, this::allPlayersChoseSide),
                game.on(AllPlayersJoinedEvent.class, this::allPlayersJoined),
                game.on(CardPlacedEvent.class, this::updatePlayArea),
                game.on(UpdateGameStatusAndTurnEvent.class, this::updateGameStatusAndTurn),
                game.on(GameFinishedEvent.class, this::gameFinished),
                //game.on(FaceUpCardReplacedEvent.class, this::updateFaceUpCards), //FIXME: see Game constructor
                game.on(CardDrawnFromDeckEvent.class, this::updateDeckStatus),
                game.on(CardDrawnFromEmptySourceEvent.class, this::notifyOfEmptySource),
                game.on(SecretObjectiveChosenEvent.class, this::updateChosenObjectiveList),
                game.on(SetUpPhaseFinishedEvent.class, this::setBoardAndHand),
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
        GameStatus status = event.getGameStatus() == GameStatus.SECOND_LAST_TURN ? GameStatus.PLAY : event.getGameStatus();
        connection.send(
                new UpdateGameStatusAndTurnS2C(
                        status,
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
                        event.getPlayer().getName(),
                        event.getPlayerColor()
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
                new UpdateDeckStatusS2C(event.getResourceCardDeck().isEmpty(), event.getGoldenCardDeck().isEmpty()));
    }

    private void notifyOfEmptySource(CardDrawnFromEmptySourceEvent event) {
        connection.send(
                new EmptySourceS2C(event.getDrawSource()
                )
        );
    }

    private void updateChosenObjectiveList(SecretObjectiveChosenEvent event) {
        connection.send(
                new UpdateObjectiveSelectedS2C(Collections.unmodifiableSet(
                        event.getPlayersHaveChosen().stream()
                            .map(PlayerProfile::getName)
                                .collect(Collectors.toSet()))
                )
        );
    }

    private void setBoardAndHand(SetUpPhaseFinishedEvent event) {
        Set<Integer> commonObjectives = event.getCommonObjective().stream().map(Objective::getId).collect(Collectors.toUnmodifiableSet());
        Set<Integer> faceUpCards = event.getFaceUpCards().stream()
                .map(fuc -> Objects.requireNonNull(fuc.getCard().orElse(null)).id())
                .collect(Collectors.toUnmodifiableSet());
        Set<Integer> hand = event.getHands().get(this.playerProfile).getHand().stream().map(Card::id).collect(Collectors.toUnmodifiableSet());
        connection.send(new SetBoardAndHandS2C(commonObjectives, faceUpCards, hand));
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
}
