package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.CardPlacementController;
import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.network.message.c2s.PlaceCardC2S;
import javafx.scene.control.Alert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayAreaController extends SceneController {

    //TODO: define FXML style

    private final Set<CardPlacementController> placements;
    private CardPlacementController cardPlacement;

    public PlayAreaController() {
        cardPlacement = null;
        placements = new HashSet<>();
    }

    private void setCardPlacement(CardSelectedEvent event) {
        cardPlacement = new CardPlacementController(event.id(), event.side());
    }

    private void placeCard(PositionSelectedEvent event) {
        if (cardPlacement == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You have to select which card you want to place!");
            alert.show();
            return;
        }
        cardPlacement.setPosition(event.i(), event.j());
        GUIView.getInstance().sendMessage(new PlaceCardC2S(
                cardPlacement.getId(),
                cardPlacement.getSide(),
                cardPlacement.getPosition().i(),
                cardPlacement.getPosition().j()
        ));
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

    private void renderPlacement(UpdatePlayAreaEvent event) {
        placements.add(cardPlacement);
        //TODO: define FXML file for the playarea and add the card
        cardPlacement = null;
    }

    private void invalidPlacement(InvalidPlacementEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Invalid placement!");
        alert.show();
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                GUIView.getInstance().on(CardSelectedEvent.class, this::setCardPlacement),
                GUIView.getInstance().on(PositionSelectedEvent.class, this::placeCard),
                GUIView.getInstance().on(UpdatePlayablePositionsEvent.class, this::disableInvalidPositions),
                GUIView.getInstance().on(UpdatePlayAreaEvent.class, this::renderPlacement),
                GUIView.getInstance().on(InvalidPlacementEvent.class, this::invalidPlacement)
        ));
    }

    @Override
    public String getFXMLFileName() {
        //TODO: define FXML style
        return "playarea";
    }
}
