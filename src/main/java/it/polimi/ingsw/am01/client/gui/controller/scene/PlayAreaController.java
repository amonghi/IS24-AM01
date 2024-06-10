package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.client.gui.controller.component.*;
import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.client.gui.model.Placement;
import it.polimi.ingsw.am01.client.gui.model.Position;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.*;

public class PlayAreaController extends SceneController {
    private final SortedSet<CardPlacementController> placements;
    private final List<Integer> playerObjectives;
    private final List<Position> playablePositions;
    private boolean cardSelected;
    private int selectedId;
    private Side selectedSide;
    private String focussedPlayer;
    private String statusText;
    @FXML
    private AnchorPane playarea;
    @FXML
    private AnchorPane positionLayer;
    @FXML
    private ScrollPane scrollLayer;
    @FXML
    private VBox board;
    @FXML
    private GridPane face_up;
    @FXML
    private GridPane deck;
    @FXML
    private HBox hand;
    @FXML
    private HBox common_obj;
    @FXML
    private HBox secret_obj;
    @FXML
    private HBox play_status;
    @FXML
    private Label gameStatusLabel;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private ImageView openChatIcon;
    @FXML
    private ImageView closeChatIcon;
    @FXML
    private ImageView showBoardIcon;
    @FXML
    private ImageView maxIcon;
    private ChatBoxController chatBoxController;

    public PlayAreaController(View view) {
        super(view);
        cardSelected = false;
        selectedId = 0;
        selectedSide = null;
        placements = new TreeSet<>();
        playerObjectives = new ArrayList<>();
        playablePositions = new ArrayList<>();
        focussedPlayer = view.getPlayerName();
        statusText = "";
    }

    @FXML
    private void initialize() {
        chatPane.setVisible(false);
        chatBoxController = new ChatBoxController(view);
        chatPane.getChildren().add(chatBoxController);
        showBoardIcon.setOnMouseClicked(event -> showHideBoard());
        maxIcon.setOnMouseClicked(event -> super.setFullScreen());
        closeChatIcon.setVisible(false);

        positionLayer.setOnDragOver(event -> {
            for (Position playablePosition : playablePositions) {
                positionLayer.getChildren().add(new PlayablePositionController(
                        Utils.computeXPosition(playablePosition.i(), playablePosition.j()),
                        Utils.computeYPosition(playablePosition.i(), playablePosition.j())
                ));
            }
            event.acceptTransferModes(TransferMode.ANY);
        });

        positionLayer.setOnDragDropped(event -> {
            clearPositionLayer();
            if (isAValidPosition(event.getX(), event.getY()).isPresent()) {
                Position pos = isAValidPosition(event.getX(), event.getY()).get();
                placeCard(pos.i(), pos.j());
            } else {
                showErrorMessage("Invalid position! Please, replace the card!");
            }
        });
    }

    public void clearPositionLayer() {
        positionLayer.getChildren().clear();
    }

    /**
     * @param x the x-coord from the mouse position
     * @param y the y-coord from the mouse position
     * @return the {@link Position} associated with the area in which the card has been dropped, if valid, otherwise {@link Optional#empty()}
     */
    private Optional<Position> isAValidPosition(double x, double y) {
        for (Position position : playablePositions) {
            double xCoord = Utils.computeXPosition(position.i(), position.j());
            double yCoord = Utils.computeYPosition(position.i(), position.j());
            if (x >= xCoord && x <= xCoord + 300 && y >= yCoord && y <= yCoord + 200) {
                return Optional.of(position);
            }
        }
        return Optional.empty();
    }

    public void setCardPlacement(int id, Side side) {
        selectedId = id;
        selectedSide = side;
        cardSelected = true;
    }

    public void placeCard(int i, int j) {
        if (!cardSelected) {
            showErrorMessage("You have to select the card to place!");
            return;
        }
        hand.setDisable(true);
        view.placeCard(selectedId, selectedSide, i, j);
    }

    private void setFaceUpCards(SetFaceUpCardsEvent event) {
        face_up.getChildren().clear();
        int placed = 0;
        for (int col = 0; col < 2; col ++) {
            for (int row = 0; row < 2; row ++)
                if (placed < event.faceUpCards().size())
                    face_up.add(new FaceUpSourceController(event.faceUpCards().get(placed++), this), col, row);
        }
    }

    private void setDeck(SetDeckEvent event) {
        deck.getChildren().clear();

        // TODO: placeholder for empty deck
        deck.add(new DeckSourceController(event.goldenDeck().orElse(CardColor.RED), DeckLocation.GOLDEN_CARD_DECK, this), 0, 0);
        deck.add(new DeckSourceController(event.resourceDeck().orElse(CardColor.RED), DeckLocation.RESOURCE_CARD_DECK, this), 1, 0);

        deck.getChildren().get(0).setDisable(event.goldenDeck().isEmpty());
        deck.getChildren().get(1).setDisable(event.resourceDeck().isEmpty());
    }

    private void setHand(SetHandEvent event) {
        hand.getChildren().clear();
        for (Integer cardId : event.hand()) {
            hand.getChildren().add(new HandCardController(cardId, Side.FRONT, this));
        }
    }

    private void setObjectives(SetObjectives event) {
        this.playerObjectives.addAll(event.objectives());
        this.playerObjectives.add(event.secretObjective());

        common_obj.getChildren().add(new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource(
                        Constants.OBJECTIVE_PATH + event.objectives().get(0) + Constants.IMAGE_EXTENSION)).toString(),
                        Constants.BOARD_CARD_WIDTH,
                        Constants.CARD_PLACEMENT_HEIGHT,
                        true,
                        false
                )
        ));
        common_obj.getChildren().add(new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource(
                        Constants.OBJECTIVE_PATH + event.objectives().get(1) + Constants.IMAGE_EXTENSION)).toString(),
                        Constants.BOARD_CARD_WIDTH,
                        Constants.CARD_PLACEMENT_HEIGHT,
                        true,
                        false
                )
        ));
        secret_obj.getChildren().add(new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource(
                        Constants.OBJECTIVE_PATH + event.secretObjective() + Constants.IMAGE_EXTENSION)).toString(),
                        Constants.BOARD_CARD_WIDTH,
                        Constants.CARD_PLACEMENT_HEIGHT,
                        true,
                        false
                )
        ));
    }

    private void updatePlayablePositions(UpdatePlayablePositionsEvent event) {
        this.playablePositions.clear();
        this.playablePositions.addAll(event.playablePositions());
    }

    private void updatePlayArea(UpdatePlayAreaEvent event) {
        for (Node playerInfo : play_status.getChildren()) {
            if (((PlayerInfoController) playerInfo).getName().equals(event.playerName())) {
                ((PlayerInfoController) playerInfo).setScore(view.getScore(event.playerName()));
            }
        }

        CardPlacementController cardPlacement = new CardPlacementController(event.cardId(), event.side());
        cardPlacement.setPosition(event.i(), event.j());
        cardPlacement.setSeq(event.seq());
        if (event.playerName().equals(view.getPlayerName())) {
            placements.add(cardPlacement);
            playarea.getChildren().add(cardPlacement);
            cardSelected = false;
        } else if (event.playerName().equals(focussedPlayer)) {
            playarea.getChildren().add(cardPlacement);
        }
    }

    private void removePlacement(RemoveLastPlacementEvent event) {
        view.removeLastPlacement(event.player());
        if (event.player().equals(focussedPlayer)) {
            playarea.getChildren().removeLast();
        }

        play_status.getChildren().clear();
        for (String player : view.getPlayersInGame()) {
            play_status.getChildren().add(new PlayerInfoController(
                    player,
                    view.getPlayerColor(player),
                    view.getScore(player),
                    view.isConnected(player),
                    this
            ));
        }
    }

    private void invalidPlacement(InvalidPlacementEvent event) {
        showErrorMessage("You don't have enough resources to place this card!");
        cardSelected = false;
        hand.setDisable(false);
    }

    private void showHideBoard() {
        board.setVisible(!board.isVisible());
    }

    private void showErrorMessage(String error) {
        gameStatusLabel.setText(error);
        gameStatusLabel.setStyle("-fx-background-color: #ff0000;  -fx-background-radius: 20;");
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            gameStatusLabel.setText(statusText);
            gameStatusLabel.setStyle("-fx-background-color: " + backgroundColorHex(view.getPlayerColor(view.getCurrentPlayer()))  + ";  -fx-background-radius: 20;");
        });
        delay.play();
    }

    private String backgroundColorHex(PlayerColor playerColor) {
        return switch (playerColor) {
            case RED -> "#ff8080";
            case YELLOW -> "#ffe680";
            case BLUE -> "#8080ff";
            case GREEN -> "#99e699";
        };
    }

    private void handleTurn(UpdateGameTurnEvent event) {
        statusText = "";

        if (event.gameStatus().equals(GameStatus.LAST_TURN.toString())) {
            statusText = "Last turn! ";
        }

        if (event.currentPlayer().equals(event.player())) {
             statusText += "You are " + event.turnPhase().toLowerCase();
        }
        else {
             statusText += event.currentPlayer() + " is " + event.turnPhase().toLowerCase();
        }

        gameStatusLabel.setText(statusText);
        gameStatusLabel.setStyle("-fx-background-color: " + backgroundColorHex(view.getPlayerColor(event.currentPlayer())) + "; -fx-background-radius: 20;");

        if (!event.currentPlayer().equals(event.player())) {
            //It's not my turn
            hand.setDisable(true);
            board.setDisable(true);
            board.setVisible(false);
            playarea.setDisable(true);
            return;
        }
        if (event.turnPhase().equals("PLACING")) {
            board.setVisible(false);
            board.setDisable(true);
            hand.setDisable(false);
            playarea.setDisable(false);
        } else {
            board.setVisible(true);
            board.setDisable(false);
            hand.setDisable(true);
            playarea.setDisable(true);
        }
    }

    private void updatePlayStatus(SetPlayStatusEvent event) {
        play_status.getChildren().clear();
        for (String player : event.players()) {
            play_status.getChildren().add(new PlayerInfoController(
                    player,
                    event.colors().get(player),
                    event.scores().get(player),
                    event.connections().get(player),
                    this
            ));
        }
    }

    public void drawFromFaceUp(int cardId) {
        view.drawCardFromFaceUpCards(cardId);
    }

    public void drawFromDeck(DeckLocation deckLocation) {
        view.drawCardFromDeck(deckLocation);
    }

    public void setCurrentView(String player) {
        if (!view.getPlayersInGame().contains(player))
            return;
        hand.setVisible(view.getPlayerName().equals(player));
        board.setVisible(view.getPlayerName().equals(player));
        playarea.getChildren().clear();
        playarea.getChildren().add(positionLayer);
        for (Placement placement : view.getPlacements(player)) {
            CardPlacementController cp = new CardPlacementController(placement.id(), placement.side());
            cp.setPosition(placement.pos().i(), placement.pos().j());
            cp.setSeq(placement.seq());
            cp.setDisable(!view.getPlayerName().equals(player));
            playarea.getChildren().add(cp);
        }
        focussedPlayer = player;
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                view.on(UpdatePlayablePositionsEvent.class, this::updatePlayablePositions),
                view.on(UpdatePlayAreaEvent.class, this::updatePlayArea),
                view.on(InvalidPlacementEvent.class, this::invalidPlacement),
                view.on(RemoveLastPlacementEvent.class, this::removePlacement),
                view.on(SetFaceUpCardsEvent.class, this::setFaceUpCards),
                view.on(SetDeckEvent.class, this::setDeck),
                view.on(SetHandEvent.class, this::setHand),
                view.on(SetObjectives.class, this::setObjectives),
                view.on(UpdateGameTurnEvent.class, this::handleTurn),
                view.on(SetPlayStatusEvent.class, this::updatePlayStatus),
                view.on(SetPlayAreaEvent.class, this::setPlayArea),
                view.on(GamePausedEvent.class, this::pauseGame),
                view.on(GameResumedEvent.class, this::resumeGame),
                view.on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    private void setPlayArea(SetPlayAreaEvent event) {
        event.placements().forEach(placement -> {
            CardPlacementController cardPlacement = new CardPlacementController(placement.id(), placement.side());
            cardPlacement.setPosition(placement.pos().i(), placement.pos().j());
            cardPlacement.setSeq(placement.seq());
            placements.add(cardPlacement);

            playarea.getChildren().add(cardPlacement);
        });
    }

    private void pauseGame(GamePausedEvent event) {
        gameStatusLabel.setText("Game suspended. Waiting for players...");
        gameStatusLabel.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20;");

        playarea.setDisable(true);
    }

    private void resumeGame(GameResumedEvent event) {
        playarea.setDisable(false);
    }

    @FXML
    private void openChat() {
        chatPane.setVisible(true);
        openChatIcon.setVisible(false);
        closeChatIcon.setVisible(true);
    }

    @FXML
    private void closeChat() {
        chatPane.setVisible(false);
        openChatIcon.setVisible(true);
        closeChatIcon.setVisible(false);
    }

    @Override
    public String getFXMLFileName() {
        return "playarea";
    }
}
