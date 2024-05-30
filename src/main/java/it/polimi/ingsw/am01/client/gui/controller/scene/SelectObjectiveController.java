package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.component.ChatBoxController;
import it.polimi.ingsw.am01.client.gui.controller.component.ObjectiveChoiceController;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.client.gui.event.UpdateSecretObjectiveChoiceEvent;
import it.polimi.ingsw.am01.network.message.c2s.SelectSecretObjectiveC2S;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SelectObjectiveController extends SceneController {

    private final static String BUTTONS_STYLE = "-fx-background-color:  CF624B";

    private final List<ObjectiveChoiceController> choiceControllers = new ArrayList<>();

    @FXML
    private Label gameId;
    @FXML
    private Label titleLabel;
    @FXML
    private VBox playersBox;
    @FXML
    private Button firstObjectiveButton;
    @FXML
    private ImageView firstObjectiveImage;
    @FXML
    private Button secondObjectiveButton;
    @FXML
    private ImageView secondObjectiveImage;
    @FXML
    private Button confirmButton;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private Button openChatButton;
    @FXML
    private Button closeChatButton;
    private ChatBoxController chatBoxController;

    private int choice;

    @FXML
    private void initialize() {
        chatPane.setVisible(false);
        chatBoxController = new ChatBoxController();
        chatPane.getChildren().add(chatBoxController);
        closeChatButton.setVisible(false);

        choiceControllers.clear();
        gameId.setText("In game #" + GUIView.getInstance().getGameId());

        firstObjectiveImage.setImage(new Image(Objects.requireNonNull(SelectObjectiveController.class.getResource(
                Constants.OBJECTIVE_PATH + GUIView.getInstance().getSecretObjectivesId().getFirst() + Constants.IMAGE_EXTENSION
        )).toString()));


        secondObjectiveImage.setImage(new Image(Objects.requireNonNull(SelectObjectiveController.class.getResource(
                Constants.OBJECTIVE_PATH + GUIView.getInstance().getSecretObjectivesId().get(1) + Constants.IMAGE_EXTENSION
        )).toString()));

        GUIView.getInstance().getPlayersInGame().forEach(player -> {
            ObjectiveChoiceController controller = new ObjectiveChoiceController(player);
            playersBox.getChildren().add(
                    controller
            );
            choiceControllers.add(controller);
        });

        choice = GUIView.getInstance().getSecretObjectivesId().getFirst();
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                GUIView.getInstance().on(UpdateSecretObjectiveChoiceEvent.class, this::updateChoices),
                GUIView.getInstance().on(PlayerListChangedEvent.class, this::updatePlayerList),
                GUIView.getInstance().on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    private void updatePlayerList(PlayerListChangedEvent event) {
        playersBox.getChildren().clear();

        choiceControllers.removeIf(controller -> !event.playerList().contains(controller.getPlayerName()));
        chatBoxController.updatePlayersList(event);

        for (ObjectiveChoiceController controller : choiceControllers) {
            playersBox.getChildren().add(
                    controller
            );
        }
    }

    private void updateChoices(UpdateSecretObjectiveChoiceEvent event) {
        for (ObjectiveChoiceController controller : choiceControllers) {
            if (event.playersChosen().contains(controller.getPlayerName())) {
                controller.setChoice();
            }
        }
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

    @FXML
    private void selectChoice(Event event) {
        if (event.getSource() == firstObjectiveButton) {
            choice = GUIView.getInstance().getSecretObjectivesId().getFirst();
            firstObjectiveButton.setStyle(BUTTONS_STYLE);
            secondObjectiveButton.setStyle("");

        } else if (event.getSource() == secondObjectiveButton) {
            choice = GUIView.getInstance().getSecretObjectivesId().get(1);

            secondObjectiveButton.setStyle(BUTTONS_STYLE);
            firstObjectiveButton.setStyle("");
        }
    }

    @FXML
    private void confirm() {
        GUIView.getInstance().setSecretObjectiveChoiceId(choice);
        GUIView.getInstance().sendMessage(new SelectSecretObjectiveC2S(
                choice
        ));

        confirmButton.setVisible(false);
        titleLabel.setText("Waiting for other players choices");
        firstObjectiveButton.setDisable(true);
        secondObjectiveButton.setDisable(true);
        for (ObjectiveChoiceController controller : choiceControllers) {
            if (controller.getPlayerName().equals(GUIView.getInstance().getPlayerName())) {
                controller.setChoice();
            }
        }
    }

    @Override
    public String getFXMLFileName() {
        return "secret_objective_choice";
    }
}
