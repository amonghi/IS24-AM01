package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.component.ChatBoxController;
import it.polimi.ingsw.am01.client.gui.controller.component.SideChoiceController;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.client.gui.event.UpdatePlayAreaEvent;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.am01.client.gui.controller.Utils.movePane;

public class SelectStartingCardSideController extends SceneController {

    private final static String BUTTONS_STYLE = "-fx-background-color:  BLACK; -fx-background-radius:  20";
    private final List<SideChoiceController> choiceControllers = new ArrayList<>();
    @FXML
    private Label gameId;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox playersBox;
    @FXML
    private Button frontButton;
    @FXML
    private ImageView frontImage;
    @FXML
    private Button backButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView backImage;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private ImageView openChatIcon;
    @FXML
    private ImageView closeChatIcon;
    @FXML
    private ImageView maxIcon;
    private Side choosenSide;
    private ChatBoxController chatBoxController;

    public SelectStartingCardSideController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        chatPane.setVisible(true);
        chatBoxController = new ChatBoxController(view);
        chatPane.getChildren().add(chatBoxController);
        closeChatIcon.setVisible(false);

        choiceControllers.clear();
        gameId.setText(view.getPlayerName() + " - You are in game #" + view.getGameId());
        frontImage.setImage(new Image(Objects.requireNonNull(SelectStartingCardSideController.class.getResource(
                Constants.FRONT_CARD_PATH + view.getStartingCardId() + Constants.IMAGE_EXTENSION
        )).toString()));

        backImage.setImage(new Image(Objects.requireNonNull(SelectStartingCardSideController.class.getResource(
                Constants.BACK_CARD_PATH + view.getStartingCardId() + Constants.IMAGE_EXTENSION
        )).toString()));

        view.getPlayersInGame().forEach(player -> {
            SideChoiceController controller = new SideChoiceController(player);
            playersBox.getChildren().add(
                    controller
            );
            choiceControllers.add(controller);
        });

        choosenSide = Side.FRONT;

        maxIcon.setOnMouseClicked(this::setFullScreen);
    }


    @FXML
    private void selectChoice(Event event) {
        if (event.getSource() == frontButton) {
            choosenSide = Side.FRONT;
            frontButton.setStyle(BUTTONS_STYLE);
            backButton.setStyle("-fx-background-color: transparent");

        } else if (event.getSource() == backButton) {
            choosenSide = Side.BACK;
            backButton.setStyle(BUTTONS_STYLE);
            frontButton.setStyle("-fx-background-color: transparent");
        }
    }

    @FXML
    private void confirm() {
        view.selectStartingCardSide(choosenSide);
        confirmButton.setVisible(false);
        titleLabel.setText("Waiting for other players choices");
        frontButton.setDisable(true);
        backButton.setDisable(true);
        for (SideChoiceController controller : choiceControllers) {
            if (controller.getPlayerName().equals(view.getPlayerName())) {
                controller.setChoice(view.getStartingCardId(), choosenSide);
            }
        }
    }

    @FXML
    private void openChat() {
        openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat" + Constants.IMAGE_EXTENSION)).toString()));
        movePane(0, chatPane);
        openChatIcon.setVisible(false);
        closeChatIcon.setVisible(true);
    }

    @FXML
    private void closeChat() {
        openChatIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "chat" + Constants.IMAGE_EXTENSION)).toString()));
        movePane(400, chatPane);
        openChatIcon.setVisible(true);
        closeChatIcon.setVisible(false);
    }

    private void updateChoices(UpdatePlayAreaEvent event) {
        for (SideChoiceController controller : choiceControllers) {
            if (controller.getPlayerName().equals(event.playerName())) {
                controller.setChoice(event.cardId(), event.side());
            }
        }
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                view.on(UpdatePlayAreaEvent.class, this::updateChoices),
                view.on(PlayerListChangedEvent.class, this::updatePlayersList),
                view.on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    private void updatePlayersList(PlayerListChangedEvent event) {
        playersBox.getChildren().clear();

        choiceControllers.removeIf(controller -> !event.playerList().contains(controller.getPlayerName()));
        chatBoxController.updatePlayersList(event);

        for (SideChoiceController controller : choiceControllers) {
            playersBox.getChildren().add(
                    controller
            );
        }
    }

    @Override
    public String getFXMLFileName() {
        return "starting_card_side_choice";
    }
}
