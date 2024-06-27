package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.Placement;
import it.polimi.ingsw.am01.client.Position;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.client.gui.controller.component.*;
import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.*;

import static it.polimi.ingsw.am01.client.gui.controller.Utils.movePane;

/**
 * The main controller for the scene associated to these {@link GameStatus}:
 * <ul>
 *     <li> {@link GameStatus#PLAY} </li>
 *     <li> {@link GameStatus#SECOND_LAST_TURN} </li>
 *     <li> {@link GameStatus#LAST_TURN} </li>
 *     <li> {@link GameStatus#SUSPENDED} </li>
 * </ul>
 *
 * @see SceneController
 */
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
    private HBox player_list;
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
    @FXML
    private ImageView zoomInIcon;
    @FXML
    private ImageView zoomOutIcon;
    private ChatBoxController chatBoxController;

    /**
     * It constructs a new PlayAreaController initializing:
     * <ul>
     *     <li> {@code cardSelected}: it tells whether the card to be placed has been selected or not </li>
     *     <li> {@code selectedId}: the id of the card to be placed </li>
     *     <li> {@code selectedSide}: the side of the card to be placed </li>
     *     <li> {@code placements}: the set of CardPlacements, ordered by their sequence number </li>
     *     <li> {@code playerObjective}: the list of secret and common objectives </li>
     *     <li> {@code playablePositions}: the list of the positions where you can currently place a card</li>
     *     <li> {@code focussedPlayer}: the owner of the playarea currently shown</li>
     *     <li> {@code statusText}: the text to be shown in the game status bar</li>
     * </ul>
     *
     * @param view The main {@link View} class, containing the local and reduced copy of server data
     */
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
        chatBoxController = new ChatBoxController(view);
        chatPane.getChildren().add(chatBoxController);

        //Event handling
        zoomInIcon.setOnMouseClicked(this::zoomIn);
        zoomOutIcon.setOnMouseClicked(this::zoomOut);

        showBoardIcon.setOnMouseClicked(this::showHideBoard);
        maxIcon.setOnMouseClicked(this::setFullScreen);
        closeChatIcon.setVisible(false);

        positionLayer.setOnDragOver(this::dragCard);
        positionLayer.setOnDragDropped(this::dropCard);
    }

    /**
     * It opens the pane showing the chat
     */
    @FXML
    private void openChat() {
        openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat" + Constants.IMAGE_EXTENSION)).toString()));
        movePane(0, chatPane);
        openChatIcon.setVisible(false);
        closeChatIcon.setVisible(true);
    }

    /**
     * It closes the pane showing the chat
     */
    @FXML
    private void closeChat() {
        openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat" + Constants.IMAGE_EXTENSION)).toString()));
        movePane(400, chatPane);
        openChatIcon.setVisible(true);
        closeChatIcon.setVisible(false);
    }

    /**
     * It changes the scale of the main {@link AnchorPane} to zoom in the view.
     *
     * @param event The event emitted when the mouse is clicked
     */
    private void zoomIn(MouseEvent event) {
        playarea.setScaleX(Math.min(playarea.getScaleX() + Constants.ZOOM, Constants.MAX_ZOOM));
        playarea.setScaleY(Math.min(playarea.getScaleY() + Constants.ZOOM, Constants.MAX_ZOOM));
        String zoomIn = "zoom_in", zoomOut = "zoom_out";
        if (playarea.getScaleX() == Constants.MAX_ZOOM) {
            zoomIn = "zoom_in_dis";
            zoomOut = "zoom_out";
        }
        zoomInIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + zoomIn + Constants.IMAGE_EXTENSION)).toString()));
        zoomOutIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + zoomOut + Constants.IMAGE_EXTENSION)).toString()));

    }

    /**
     * It changes the scale of the main {@link AnchorPane} to zoom out the view.
     *
     * @param event The event emitted when the mouse is clicked
     */
    private void zoomOut(MouseEvent event) {
        playarea.setScaleX(Math.max(playarea.getScaleX() - Constants.ZOOM, Constants.MIN_ZOOM));
        playarea.setScaleY(Math.max(playarea.getScaleY() - Constants.ZOOM, Constants.MIN_ZOOM));
        String zoomIn = "zoom_in", zoomOut = "zoom_out";
        if (playarea.getScaleX() == Constants.MIN_ZOOM) {
            zoomIn = "zoom_in";
            zoomOut = "zoom_out_dis";
        }
        zoomInIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + zoomIn + Constants.IMAGE_EXTENSION)).toString()));
        zoomOutIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + zoomOut + Constants.IMAGE_EXTENSION)).toString()));
    }

    /**
     * It shows or hides the board, based on its current position
     *
     * @param event The event emitted when the mouse is clicked
     * @see Utils#movePane(float, Node)
     */
    private void showHideBoard(MouseEvent event) {
        if (board.getTranslateX() != 0) {
            movePane(0, board);
        } else {
            movePane(-460, board);
            board.setDisable(true);
        }
    }

    /**
     * It shows on the {@link this#playarea} all the current playable positions
     *
     * @param event The event emitted when a player starts the drag and drop gesture
     */
    private void dragCard(DragEvent event) {
        for (Position playablePosition : playablePositions) {
            positionLayer.getChildren().add(new PlayablePositionController(
                    Utils.computeXPosition(playablePosition.i(), playablePosition.j()),
                    Utils.computeYPosition(playablePosition.i(), playablePosition.j())
            ));
        }
        event.acceptTransferModes(TransferMode.ANY);
    }

    /**
     * It tries to place a card in the position where the card is dropped
     *
     * @param event The event emitted when a player drop the card in a certain position
     * @see this#placeCard(int, int)
     */
    private void dropCard(DragEvent event) {
        clearPositionLayer();
        if (isAValidPosition(event.getX(), event.getY()).isPresent()) {
            Position pos = isAValidPosition(event.getX(), event.getY()).get();
            placeCard(pos.i(), pos.j());
        } else {
            showErrorMessage("Invalid position! Please, replace the card!");
        }
    }

    /**
     * It clears the layer on which the playable positions' placeholders are shown
     */
    public void clearPositionLayer() {
        positionLayer.getChildren().clear();
    }

    /**
     * @param x the x-coordinate from the mouse position
     * @param y the y-coordinate from the mouse position
     * @return the {@link Position} associated with the coordinates of the area
     * where the card has been dropped, if valid, otherwise {@link Optional#empty()}
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

    /**
     * It sets the id and the side of the card that the player wants to place.
     *
     * @param id   The id of the card that the player wants to place
     * @param side The {@link Side} of the card that the player wants to place
     */
    public void setCardPlacement(int id, Side side) {
        selectedId = id;
        selectedSide = side;
        cardSelected = true;
    }

    /**
     * It calls the {@link View#placeCard(int, Side, int, int)} method using:
     * <ul>
     *     <li> the id and the side set by the {@link this#setCardPlacement(int, Side)} method </li>
     *     <li> the i and j parameters </li>
     * </ul>
     *
     * @param i The i-coordinate of the {@link Position} where the player wants to place the card
     * @param j The j-coordinate of the {@link Position} where the player wants to place the card
     */
    public void placeCard(int i, int j) {
        if (!cardSelected) {
            showErrorMessage("You have to select the card to place!");
            return;
        }
        hand.setDisable(true);
        view.placeCard(selectedId, selectedSide, i, j);
    }

    /**
     * It shows to the player the {@link it.polimi.ingsw.am01.model.game.FaceUpCard} received from the {@link View}
     *
     * @param event The event received from the {@link View} with the
     *              {@link it.polimi.ingsw.am01.model.game.FaceUpCard}'s ids received from the server
     */
    private void setFaceUpCards(SetFaceUpCardsEvent event) {
        face_up.getChildren().clear();
        int placed = 0;
        for (int col = 0; col < 2; col++) {
            for (int row = 0; row < 2; row++)
                if (placed < event.faceUpCards().size())
                    face_up.add(new FaceUpSourceController(event.faceUpCards().get(placed++), this), col, row);
        }
    }

    /**
     * It shows to the player the back face of the top card of the two {@link it.polimi.ingsw.am01.model.game.Deck}s
     *
     * @param event The event received from the {@link View} with the
     *              {@link it.polimi.ingsw.am01.model.game.Deck}s' colors received from the server
     */
    private void setDeck(SetDeckEvent event) {
        deck.getChildren().clear();

        deck.add(new DeckSourceController(event.goldenDeck().orElse(CardColor.RED), DeckLocation.GOLDEN_CARD_DECK, this), 0, 0);
        deck.add(new DeckSourceController(event.resourceDeck().orElse(CardColor.RED), DeckLocation.RESOURCE_CARD_DECK, this), 1, 0);

        if (event.goldenDeck().isEmpty()) {
            deck.getChildren().get(0).setDisable(true);
            deck.getChildren().get(0).setOpacity(0.5);
        }
        if (event.resourceDeck().isEmpty()) {
            deck.getChildren().get(1).setDisable(true);
            deck.getChildren().get(1).setOpacity(0.5);
        }
    }

    /**
     * It shows to the player the {@link it.polimi.ingsw.am01.model.card.Card} in his or her hand
     *
     * @param event The event received from the {@link View} with the hand
     *              {@link it.polimi.ingsw.am01.model.card.Card}s received from the server
     */
    private void setHand(SetHandEvent event) {
        hand.getChildren().clear();
        for (Integer cardId : event.hand()) {
            hand.getChildren().add(new HandCardController(cardId, Side.FRONT, this));
        }
    }

    /**
     * It shows to the player the common and secret {@link it.polimi.ingsw.am01.model.objective.Objective}s
     *
     * @param event The event received from the {@link View} with all the objectives
     */
    private void setObjectives(SetObjectives event) {
        this.playerObjectives.clear();
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

    /**
     * It updates the current playable positions' list
     *
     * @param event The event received from the {@link View} with all the current playable positions.
     */
    private void updatePlayablePositions(UpdatePlayablePositionsEvent event) {
        this.playablePositions.clear();
        this.playablePositions.addAll(event.playablePositions());
    }

    /**
     * It updates the playarea, setting and showing the new:
     * <ul>
     *     <li> Score </li>
     *     <li> CardPlacements </li>
     * </ul>
     * for each player
     *
     * @param event The event received from the {@link View}, containing the new score and the last {@code CardPlacement}
     */
    private void updatePlayArea(UpdatePlayAreaEvent event) {
        for (Node playerInfo : player_list.getChildren()) {
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

    /**
     * It updates the playarea, removing the last placement and setting the new scores
     *
     * @param event The event received from the {@link View}, containing the name of the player
     *              whose playarea needs to be updated
     */
    private void removePlacement(RemoveLastPlacementEvent event) {
        view.removeLastPlacement(event.player());
        if (event.player().equals(focussedPlayer)) {
            playarea.getChildren().removeLast();
        }

        player_list.getChildren().clear();
        for (String player : view.getPlayersInGame()) {
            player_list.getChildren().add(new PlayerInfoController(
                    player,
                    view.getPlayerColor(player),
                    view.getScore(player),
                    view.isConnected(player),
                    this
            ));
        }
    }

    /**
     * It calls the {@link this#showErrorMessage(String)} tho show the error message
     * associated with the event received from the {@link View}
     *
     * @param event The event received from the {@link View} telling that the placement is invalid
     */
    private void invalidPlacement(InvalidPlacementEvent event) {
        showErrorMessage("You don't have enough resources to place this card!");
        cardSelected = false;
        hand.setDisable(false);
    }

    /**
     * It updates the label on the game status bar with the error message
     * received as parameter
     *
     * @param error The error message to show
     */
    private void showErrorMessage(String error) {
        gameStatusLabel.setText(error);
        gameStatusLabel.setStyle("-fx-background-color: #ff0000;  -fx-background-radius: 20;");
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            gameStatusLabel.setText(statusText);
            gameStatusLabel.setStyle("-fx-background-color: " + Utils.backgroundColorHex(view.getPlayerColor(view.getCurrentPlayer())) + ";  -fx-background-radius: 20;");
        });
        delay.play();
    }

    /**
     * It updates the label on the game status bar based on the {@link GameStatus},
     * the {@link it.polimi.ingsw.am01.model.game.TurnPhase} and the current player
     *
     * @param event The event received from the {@link View}, containing the information about
     *              the {@link GameStatus} and {@link it.polimi.ingsw.am01.model.game.TurnPhase}
     */
    private void handleTurn(UpdateGameTurnEvent event) {
        statusText = "";

        if (event.gameStatus().equals(GameStatus.LAST_TURN.toString())) {
            statusText = "Last turn! ";
        }

        if (event.currentPlayer().equals(event.player())) {
            statusText += "You are " + event.turnPhase().toLowerCase();
        } else {
            statusText += event.currentPlayer() + " is " + event.turnPhase().toLowerCase();
        }

        gameStatusLabel.setText(statusText);
        gameStatusLabel.setStyle("-fx-background-color: " + Utils.backgroundColorHex(view.getPlayerColor(event.currentPlayer())) + "; -fx-background-radius: 20;");

        if (!event.currentPlayer().equals(event.player())) {
            //It's not my turn
            movePane(-460, board);
            hand.setDisable(true);
            board.setDisable(true);
            playarea.setDisable(true);
            return;
        }
        if (event.turnPhase().equals("PLACING")) {
            movePane(-460, board);
            board.setDisable(true);
            hand.setDisable(false);
            playarea.setDisable(false);
        } else {
            movePane(0, board);
            board.setDisable(false);
            hand.setDisable(true);
            playarea.setDisable(true);
        }
    }

    /**
     * It updates the player status bar, setting the information of each player:
     * <ul>
     *     <li> Player name </li>
     *     <li> Player color </li>
     *     <li> Player score </li>
     *     <li> Connection status </li>
     * </ul>
     *
     * @param event The event received from the {@link View}, containing the information of all the players in game
     */
    private void updatePlayStatus(SetPlayStatusEvent event) {
        player_list.getChildren().clear();
        for (String player : event.players()) {
            player_list.getChildren().add(new PlayerInfoController(
                    player,
                    event.colors().get(player),
                    event.scores().get(player),
                    event.connections().get(player),
                    this
            ));
        }
    }

    /**
     * It calls the {@link View#drawCardFromFaceUpCards(int)}
     *
     * @param cardId The id of the card clicked by the player
     */
    public void drawFromFaceUp(int cardId) {
        view.drawCardFromFaceUpCards(cardId);
    }

    /**
     * It calls the {@link View#drawCardFromDeck(DeckLocation)}
     *
     * @param deckLocation The deck from which the player want to draw a card
     */
    public void drawFromDeck(DeckLocation deckLocation) {
        view.drawCardFromDeck(deckLocation);
    }

    /**
     * It updates the current view to see the playarea of the {@link this#focussedPlayer}, i.e. the
     * player name received as parameter. It also disables the board and the hand, if the focussed player
     * is not the owner of the view
     *
     * @param player The name of the player whose playarea you want to watch
     */
    public void setCurrentView(String player) {
        if (!view.getPlayersInGame().contains(player))
            return;
        hand.setVisible(view.getPlayerName().equals(player));
        board.setDisable(!view.getPlayerName().equals(player));
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

        if (!focussedPlayer.equals(view.getPlayerName())) {
            gameStatusLabel.setText(statusText + " - You are viewing " + focussedPlayer);
        } else {
            gameStatusLabel.setText(statusText);
        }
    }

    /**
     * It calls the {@link ChatBoxController#updateMessages(NewMessageEvent)} method
     *
     * @param event The event received from the {@link View}, containing the new message to add to the ChatBox
     */
    private void updateMessages(NewMessageEvent event) {
        if (event.message().recipient().equals(view.getPlayerName())) {
            openChatIcon.getImage().cancel();
            openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat-msg" + Constants.IMAGE_EXTENSION)).toString()));
        }
        chatBoxController.updateMessages(event);
    }

    /**
     * It adds and shows to the playarea all the CardPlacement of the player
     *
     * @param event The event received from the {@link View}, containing the set of the placements to show
     */
    private void setPlayArea(SetPlayAreaEvent event) {
        event.placements().forEach(placement -> {
            CardPlacementController cardPlacement = new CardPlacementController(placement.id(), placement.side());
            cardPlacement.setPosition(placement.pos().i(), placement.pos().j());
            cardPlacement.setSeq(placement.seq());
            placements.add(cardPlacement);

            playarea.getChildren().add(cardPlacement);
        });
    }

    /**
     * It updates the label of the status bar, to inform the remaining player that the game is suspended.
     * It also disables the entire playarea
     *
     * @param event The event received from the {@link View}, when only one player remains in the game
     */
    private void pauseGame(GamePausedEvent event) {
        gameStatusLabel.setText("Game suspended. Waiting for players...");
        gameStatusLabel.setStyle("-fx-background-color: lightgray; -fx-background-radius: 20;");

        playarea.setDisable(true);
    }

    /**
     * It re-enables the entire playarea
     *
     * @param event The event received from the {@link View}, when the game is resumed
     */
    private void resumeGame(GameResumedEvent event) {
        playarea.setDisable(false);
    }

    /**
     * {@inheritDoc}
     */
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
                view.on(NewMessageEvent.class, this::updateMessages)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "playarea";
    }
}
