package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.component.ChatBoxController;
import it.polimi.ingsw.am01.client.gui.controller.component.ObjectiveChoiceController;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.client.gui.event.UpdateSecretObjectiveChoiceEvent;
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


public class SelectObjectiveController extends SceneController {

    private final static String BUTTONS_STYLE = "-fx-background-color:  BLACK; -fx-background-radius:  20";

    private final List<ObjectiveChoiceController> choiceControllers = new ArrayList<>();

    @FXML
    private Label gameId;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox playersBox;
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
    private ImageView openChatIcon;
    @FXML
    private ImageView closeChatIcon;
    @FXML
    private ImageView maxIcon;
    private ChatBoxController chatBoxController;

    private int choice;

    public SelectObjectiveController(View view) {
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

        firstObjectiveImage.setImage(new Image(Objects.requireNonNull(SelectObjectiveController.class.getResource(
                Constants.OBJECTIVE_PATH + view.getSecretObjectivesId().getFirst() + Constants.IMAGE_EXTENSION
        )).toString()));


        secondObjectiveImage.setImage(new Image(Objects.requireNonNull(SelectObjectiveController.class.getResource(
                Constants.OBJECTIVE_PATH + view.getSecretObjectivesId().get(1) + Constants.IMAGE_EXTENSION
        )).toString()));

        view.getPlayersInGame().forEach(player -> {
            ObjectiveChoiceController controller = new ObjectiveChoiceController(player);
            playersBox.getChildren().add(
                    controller
            );
            choiceControllers.add(controller);
        });

        choice = view.getSecretObjectivesId().getFirst();

        maxIcon.setOnMouseClicked(this::setFullScreen);
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                view.on(UpdateSecretObjectiveChoiceEvent.class, this::updateChoices),
                view.on(PlayerListChangedEvent.class, this::updatePlayerList),
                view.on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
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
        movePane(0, chatPane);
        openChatIcon.setVisible(false);
        closeChatIcon.setVisible(true);
    }

    @FXML
    private void closeChat() {
        movePane(400, chatPane);
        openChatIcon.setVisible(true);
        closeChatIcon.setVisible(false);
    }

    @FXML
    private void selectChoice(Event event) {
        if (event.getSource() == firstObjectiveButton) {
            choice = view.getSecretObjectivesId().getFirst();
            firstObjectiveButton.setStyle(BUTTONS_STYLE);
            secondObjectiveButton.setStyle("-fx-background-color: transparent");

        } else if (event.getSource() == secondObjectiveButton) {
            choice = view.getSecretObjectivesId().get(1);
            secondObjectiveButton.setStyle(BUTTONS_STYLE);
            firstObjectiveButton.setStyle("-fx-background-color: transparent");
        }
    }

    @FXML
    private void confirm() {
        view.setSecretObjectiveChoiceId(choice);
        view.selectSecretObjective(choice);

        confirmButton.setVisible(false);
        titleLabel.setText("Waiting for other players choices");
        firstObjectiveButton.setDisable(true);
        secondObjectiveButton.setDisable(true);
        for (ObjectiveChoiceController controller : choiceControllers) {
            if (controller.getPlayerName().equals(view.getPlayerName())) {
                controller.setChoice();
            }
        }
    }

    @Override
    public String getFXMLFileName() {
        return "secret_objective_choice";
    }
}
