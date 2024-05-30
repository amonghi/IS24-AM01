package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.*;
import it.polimi.ingsw.am01.client.gui.controller.popup.ShowObjectivePopupController;
import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.client.gui.model.GUIPlacement;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.c2s.DrawCardFromDeckC2S;
import it.polimi.ingsw.am01.network.message.c2s.DrawCardFromFaceUpCardsC2S;
import it.polimi.ingsw.am01.network.message.c2s.PlaceCardC2S;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class PlayAreaController extends SceneController {
    private final SortedSet<CardPlacementController> placements;
    private final List<Integer> playerObjectives;
    private boolean cardSelected;
    private int selectedId;
    private Side selectedSide;
    private String focussedPlayer;
    @FXML
    private AnchorPane playarea;
    @FXML
    private HBox board;
    @FXML
    private VBox fu_slot1;
    @FXML
    private VBox fu_slot2;
    @FXML
    private VBox deck;
    @FXML
    private HBox hand;
    @FXML
    private HBox play_status;
    @FXML
    private HBox utility_buttons;
    @FXML
    private Label gameStatusLabel;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private Button openChatButton;
    @FXML
    private Button closeChatButton;
    private ChatBoxController chatBoxController;

    public PlayAreaController() {
        cardSelected = false;
        selectedId = 0;
        selectedSide = null;
        placements = new TreeSet<>();
        playerObjectives = new ArrayList<>();
        focussedPlayer = GUIView.getInstance().getPlayerName();
    }

    @FXML
    private void initialize() {
        chatPane.setVisible(false);
        chatBoxController = new ChatBoxController();
        chatPane.getChildren().add(chatBoxController);
        closeChatButton.setVisible(false);

        Button showBoard = new Button("Show/Hide Board");
        showBoard.setOnAction(event -> {
            showHideBoard();
        });
        Button showObj = new Button("Show Objectives");
        showObj.setOnAction(event -> {
            showHideObj();
        });
        utility_buttons.getChildren().add(showBoard);
        utility_buttons.getChildren().add(showObj);
    }

    public void setCardPlacement(int id, Side side) {
        selectedId = id;
        selectedSide = side;
        cardSelected = true;
    }

    public void placeCard(int i, int j) {
        if (!cardSelected) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You have to select which card you want to place!");
            alert.show();
            return;
        }

        for (CardPlacementController cp : placements) {
            cp.getBtnPositions().values().forEach(button -> {
                button.setDisable(true);
            });
        }

        hand.setDisable(true);

        GUIView.getInstance().sendMessage(new PlaceCardC2S(selectedId, selectedSide, i, j));
    }

    private void setFaceUpCards(SetFaceUpCardsEvent event) {
        //TODO: redesign board to unify slot1 and slot2
        fu_slot1.getChildren().clear();
        fu_slot2.getChildren().clear();
        fu_slot1.getChildren().add(new FaceUpSourceController(event.faceUpCards().get(0)));
        fu_slot1.getChildren().add(new FaceUpSourceController(event.faceUpCards().get(1)));
        fu_slot2.getChildren().add(new FaceUpSourceController(event.faceUpCards().get(2)));
        fu_slot2.getChildren().add(new FaceUpSourceController(event.faceUpCards().get(3)));
    }

    private void setDeck(SetDeckEvent event) {
        deck.getChildren().clear();

        // TODO: placeholder for empty deck
        deck.getChildren().add(new DeckSourceController(event.goldenDeck().orElse(CardColor.RED), DeckLocation.GOLDEN_CARD_DECK));
        deck.getChildren().add(new DeckSourceController(event.resourceDeck().orElse(CardColor.RED), DeckLocation.RESOURCE_CARD_DECK));

        deck.getChildren().get(0).setDisable(event.goldenDeck().isEmpty());
        deck.getChildren().get(1).setDisable(event.resourceDeck().isEmpty());
    }

    private void setHand(SetHandEvent event) {
        hand.getChildren().clear();
        for (Integer cardId : event.hand()) {
            hand.getChildren().add(new HandCardController(cardId, Side.FRONT));
        }
    }

    private void setObjectives(SetObjectives event) {
        this.playerObjectives.addAll(event.objectives());
        this.playerObjectives.add(event.secretObjective());
    }

    private void disableInvalidPositions(UpdatePlayablePositionsEvent event) {
        for (CardPlacementController cp : placements) {
            cp.getBtnPositions()
                    .keySet()
                    .stream()
                    .filter(p -> !event.playablePositions().contains(p))
                    .forEach(p -> cp.getBtnPositions().get(p).setDisable(true));

            cp.getBtnPositions()
                    .keySet()
                    .stream()
                    .filter(p -> event.playablePositions().contains(p))
                    .forEach(p -> {
                        cp.getBtnPositions().get(p).setDisable(false);
                    });
        }
    }

    private void updatePlayArea(UpdatePlayAreaEvent event) {

        for (Node playerInfo : play_status.getChildren()) {
            if (((PlayerInfoController) playerInfo).getName().equals(event.playerName())) {
                ((PlayerInfoController) playerInfo).setScore(GUIView.getInstance().getScore(event.playerName()));
            }
        }

        CardPlacementController cardPlacement = new CardPlacementController(event.cardId(), event.side());
        cardPlacement.setPosition(event.i(), event.j());
        cardPlacement.setSeq(event.seq());
        if (event.playerName().equals(GUIView.getInstance().getPlayerName())) {
            placements.add(cardPlacement);
            playarea.getChildren().add(cardPlacement);
            cardSelected = false;
        } else if (event.playerName().equals(focussedPlayer)) {
            playarea.getChildren().add(cardPlacement);
        }
    }

    private void removePlacement(RemoveLastPlacementEvent event) {
        GUIView.getInstance().removeLastPlacement(event.player());
        if (event.player().equals(GUIView.getInstance().getPlayerName())) {
            playarea.getChildren().removeLast();
        }
    }


    private void invalidPlacement(InvalidPlacementEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Invalid placement!");
        alert.show();
        cardSelected = false;
        //TODO: make out why it disables everything
        hand.setDisable(false);
        setCurrentView(GUIView.getInstance().getPlayerName());
    }

    private void showHideObj() {
        openPopup(new ShowObjectivePopupController(playerObjectives));
    }

    private void showHideBoard() {
        board.setVisible(!board.isVisible());
        hand.setVisible(!hand.isVisible());
    }

    private void handleTurn(UpdateGameTurnEvent event) {
        if (!event.gameStatus().equals(GameStatus.SECOND_LAST_TURN.toString())) {
            gameStatusLabel.setText("Game status: " + event.gameStatus());
        } else {
            gameStatusLabel.setText("Game status: " + GameStatus.PLAY);
        }
        if (!event.currentPlayer().equals(event.player())) {
            //It's not my turn
            hand.setDisable(true);
            board.setDisable(true);
            playarea.setDisable(true);
            return;
        }
        if (event.turnPhase().equals("PLACING")) {
            board.setDisable(true);
            hand.setDisable(false);
            playarea.setDisable(false);
        } else {
            board.setDisable(false);
            hand.setDisable(true);
            playarea.setDisable(true);
        }
        //TODO: remove or change
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(event.player() + ": it's your turn -> " + event.turnPhase());
        alert.show();
    }

    private void updatePlayStatus(SetPlayStatusEvent event) {
        play_status.getChildren().clear();
        for (String player : event.players()) {
            play_status.getChildren().add(new PlayerInfoController(
                    player,
                    event.colors().get(player),
                    event.scores().get(player),
                    event.connections().get(player))
            );
        }
    }

    public void drawFromFaceUp(int cardId) {
        GUIView.getInstance().sendMessage(new DrawCardFromFaceUpCardsC2S(cardId));
    }

    public void drawFromDeck(DeckLocation deckLocation) {
        GUIView.getInstance().sendMessage(new DrawCardFromDeckC2S(deckLocation));
    }

    public void setCurrentView(String player) {
        if (!GUIView.getInstance().getPlayersInGame().contains(player))
            return;
        hand.setVisible(GUIView.getInstance().getPlayerName().equals(player));
        board.setVisible(GUIView.getInstance().getPlayerName().equals(player));
        playarea.getChildren().clear();
        for (GUIPlacement placement : GUIView.getInstance().getPlacements(player)) {
            CardPlacementController cp = new CardPlacementController(placement.id(), placement.side());
            cp.setPosition(placement.pos().i(), placement.pos().j());
            cp.setSeq(placement.seq());
            cp.setDisable(!GUIView.getInstance().getPlayerName().equals(player));
            playarea.getChildren().add(cp);
        }
        focussedPlayer = player;
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                GUIView.getInstance().on(UpdatePlayablePositionsEvent.class, this::disableInvalidPositions),
                GUIView.getInstance().on(UpdatePlayAreaEvent.class, this::updatePlayArea),
                GUIView.getInstance().on(InvalidPlacementEvent.class, this::invalidPlacement),
                GUIView.getInstance().on(RemoveLastPlacementEvent.class, this::removePlacement),
                GUIView.getInstance().on(SetFaceUpCardsEvent.class, this::setFaceUpCards),
                GUIView.getInstance().on(SetDeckEvent.class, this::setDeck),
                GUIView.getInstance().on(SetHandEvent.class, this::setHand),
                GUIView.getInstance().on(SetObjectives.class, this::setObjectives),
                GUIView.getInstance().on(UpdateGameTurnEvent.class, this::handleTurn),
                GUIView.getInstance().on(SetPlayStatusEvent.class, this::updatePlayStatus),
                GUIView.getInstance().on(SetPlayAreaEvent.class, this::setPlayArea),
                GUIView.getInstance().on(GamePausedEvent.class, this::pauseGame),
                GUIView.getInstance().on(GameResumedEvent.class, this::resumeGame),
                GUIView.getInstance().on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    private void setPlayArea(SetPlayAreaEvent event) {
        event.guiPlacements().forEach(placement -> {
            CardPlacementController cardPlacement = new CardPlacementController(placement.id(), placement.side());
            cardPlacement.setPosition(placement.pos().i(), placement.pos().j());
            cardPlacement.setSeq(placement.seq());
            placements.add(cardPlacement);

            playarea.getChildren().add(cardPlacement);
        });
    }

    private void pauseGame(GamePausedEvent event) {
        gameStatusLabel.setText("Game status: " + event.getGameStatus().toString());
        playarea.setDisable(true);
    }

    private void resumeGame(GameResumedEvent event) {
        playarea.setDisable(false);
    }

    @FXML
    private void openChat() {
        chatPane.setVisible(true);
        openChatButton.setVisible(false);
        closeChatButton.setVisible(true);
    }

    @FXML
    private void closeChat() {
        chatPane.setVisible(false);
        openChatButton.setVisible(true);
        closeChatButton.setVisible(false);
    }

    @Override
    public String getFXMLFileName() {
        return "playarea";
    }
}
