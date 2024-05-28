package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.component.SideChoiceController;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.client.gui.event.UpdatePlayAreaEvent;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.c2s.SelectStartingCardSideC2S;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectStartingCardSideController extends SceneController {

    private final static String BUTTONS_STYLE = "-fx-background-color:  CF624B";
    private final List<SideChoiceController> choiceControllers = new ArrayList<>();
    @FXML
    private Label gameId;
    @FXML
    private Label titleLabel;
    @FXML
    private VBox playersBox;
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
    private Side choosenSide;

    @FXML
    private void initialize() {
        choiceControllers.clear();
        gameId.setText("In game #" + GUIView.getInstance().getGameId());
        frontImage.setImage(new Image(Objects.requireNonNull(SelectStartingCardSideController.class.getResource(
                Constants.FRONT_CARD_PATH + GUIView.getInstance().getStartingCardId() + Constants.IMAGE_EXTENSION
        )).toString()));

        backImage.setImage(new Image(Objects.requireNonNull(SelectStartingCardSideController.class.getResource(
                Constants.BACK_CARD_PATH + GUIView.getInstance().getStartingCardId() + Constants.IMAGE_EXTENSION
        )).toString()));

        GUIView.getInstance().getPlayersInGame().forEach(player -> {
            SideChoiceController controller = new SideChoiceController(player);
            playersBox.getChildren().add(
                    controller
            );
            choiceControllers.add(controller);
        });

        choosenSide = Side.FRONT;
    }

    @FXML
    private void selectChoice(Event event) {
        if (event.getSource() == frontButton) {
            choosenSide = Side.FRONT;
            frontButton.setStyle(BUTTONS_STYLE);
            backButton.setStyle("");

        } else if (event.getSource() == backButton) {
            choosenSide = Side.BACK;
            backButton.setStyle(BUTTONS_STYLE);
            frontButton.setStyle("");
        }
    }

    @FXML
    private void confirm() {
        GUIView.getInstance().sendMessage(new SelectStartingCardSideC2S(
                choosenSide
        ));
        confirmButton.setVisible(false);
        titleLabel.setText("Waiting for other players choices");
        frontButton.setDisable(true);
        backButton.setDisable(true);
        for (SideChoiceController controller : choiceControllers) {
            if (controller.getPlayerName().equals(GUIView.getInstance().getPlayerName())) {
                controller.setChoice(GUIView.getInstance().getStartingCardId(), choosenSide);
            }
        }
    }

    @FXML
    private void openChat() {
        //TODO: todo
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
                GUIView.getInstance().on(UpdatePlayAreaEvent.class, this::updateChoices),
                GUIView.getInstance().on(PlayerListChangedEvent.class, this::updatePlayersList)
        ));
    }

    private void updatePlayersList(PlayerListChangedEvent event) {
        playersBox.getChildren().clear();

        choiceControllers.removeIf(controller -> !event.playerList().contains(controller.getPlayerName()));

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
