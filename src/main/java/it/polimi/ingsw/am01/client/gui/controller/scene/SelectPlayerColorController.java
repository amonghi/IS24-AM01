package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.ChatBoxController;
import it.polimi.ingsw.am01.client.gui.controller.component.ColorChoiceController;
import it.polimi.ingsw.am01.client.gui.event.NewMessageEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.client.gui.event.UpdatePlayerColorEvent;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am01.client.gui.controller.Utils.movePane;

public class SelectPlayerColorController extends SceneController {

    private static final int HIGHER_STROKE_WIDTH = 8;

    private static final int LOWER_STROKE_WIDTH = 1;

    private final List<ColorChoiceController> colorChoiceControllers = new ArrayList<>();
    @FXML
    private Label gameId;
    @FXML
    private Label titleLabel;
    @FXML
    private HBox playersBox;
    @FXML
    private Button yelloButton;
    @FXML
    private Button redButton;
    @FXML
    private Button blueButton;
    @FXML
    private Button greenButton;
    @FXML
    private Circle yelloCircle;
    @FXML
    private Circle redCircle;
    @FXML
    private Circle blueCircle;
    @FXML
    private Circle greenCircle;
    @FXML
    private AnchorPane chatPane;
    @FXML
    private ImageView openChatIcon;
    @FXML
    private ImageView closeChatIcon;
    @FXML
    private ImageView maxIcon;
    private ChatBoxController chatBoxController;

    private PlayerColor colorChoice;

    public SelectPlayerColorController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        chatPane.setVisible(false);
        chatBoxController = new ChatBoxController(view);
        chatPane.getChildren().add(chatBoxController);
        closeChatIcon.setVisible(false);

        colorChoiceControllers.clear();
        colorChoice = null;
        gameId.setText(view.getPlayerName() + " - You are in game #" + view.getGameId());
        view.getPlayersInGame().forEach(player -> {
            ColorChoiceController controller = new ColorChoiceController(player);
            playersBox.getChildren().add(
                    controller
            );
            colorChoiceControllers.add(controller);
        });

        maxIcon.setOnMouseClicked(this::setFullScreen);
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                view.on(UpdatePlayerColorEvent.class, this::updatePlayersColor),
                view.on(PlayerListChangedEvent.class, this::updatePlayerList),
                view.on(NewMessageEvent.class, event -> chatBoxController.updateMessages(event))
        ));
    }

    private void updatePlayerList(PlayerListChangedEvent event) {
        playersBox.getChildren().clear();

        colorChoiceControllers.removeIf(controller -> !event.playerList().contains(controller.getPlayerName()));
        chatBoxController.updatePlayersList(event);

        for (ColorChoiceController controller : colorChoiceControllers) {
            playersBox.getChildren().add(
                    controller
            );
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
    private void confirm(Event event) {
        if (event.getSource().equals(yelloButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.YELLOW)) {
                return;
            }

            setCirclesStrokes(yelloCircle);
            colorChoice = PlayerColor.YELLOW;

        } else if (event.getSource().equals(redButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.RED)) {
                return;
            }

            setCirclesStrokes(redCircle);
            colorChoice = PlayerColor.RED;

        } else if (event.getSource().equals(blueButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.BLUE)) {
                return;
            }

            setCirclesStrokes(blueCircle);
            colorChoice = PlayerColor.BLUE;

        } else if (event.getSource().equals(greenButton)) {
            if (colorChoice != null && colorChoice.equals(PlayerColor.GREEN)) {
                return;
            }

            setCirclesStrokes(greenCircle);
            colorChoice = PlayerColor.GREEN;
        }

        view.selectColor(colorChoice);
    }

    private void setCirclesStrokes(Circle selectedCircle) {
        yelloCircle.setStrokeWidth(LOWER_STROKE_WIDTH);
        blueCircle.setStrokeWidth(LOWER_STROKE_WIDTH);
        redCircle.setStrokeWidth(LOWER_STROKE_WIDTH);
        greenCircle.setStrokeWidth(LOWER_STROKE_WIDTH);

        selectedCircle.setStrokeWidth(HIGHER_STROKE_WIDTH);
    }

    private void updatePlayersColor(UpdatePlayerColorEvent event) {
        for (ColorChoiceController controller : colorChoiceControllers) {
            if (controller.getPlayerName().equals(event.playerName())) {
                controller.setChoice(event.playerColor());
            }
        }
    }

    @Override
    public String getFXMLFileName() {
        return "color_choice_phase";
    }
}
