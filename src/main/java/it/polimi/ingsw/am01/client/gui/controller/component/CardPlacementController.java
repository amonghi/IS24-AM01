package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.Position;
import it.polimi.ingsw.am01.client.gui.event.PositionSelectedEvent;
import it.polimi.ingsw.am01.client.gui.event.ViewEvent;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardPlacementController implements ComponentController, EventEmitter<ViewEvent> {

    private static final int OFFSET_X = 224;
    private static final int OFFSET_Y = 111;
    private static final int BASE_X = 5000;
    private static final int BASE_Y = 5000;
    private static final int SUFFIX_CARD_ID_LENGTH = 7;
    private static final String path_front = "/cards/Fronts/";
    private static final String path_back = "/cards/Backs/";
    private final EventEmitterImpl<ViewEvent> emitter;
    private final int id;
    private final Side side;
    private final Map<Position, Button> btnPositions;
    private Position position;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;
    @FXML
    private Button TOP_LEFT;
    @FXML
    private Button TOP_RIGHT;
    @FXML
    private Button BOTTOM_LEFT;
    @FXML
    private Button BOTTOM_RIGHT;

    public CardPlacementController(int id, Side side) {
        this.btnPositions = new HashMap<>();
        this.emitter = new EventEmitterImpl<>();
        this.id = id;
        this.side = side;

        loadComponent();

        setImage();
    }

    public void setImage() {
        String path = switch (side) {
            case Side.FRONT:
                yield path_front;
            case Side.BACK:
                yield path_back;
        };
        String formattedId = "000" + id + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path + formattedId)).toString()));
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(int i, int j) {
        this.position = new Position(i, j);
        card.setLayoutX(computeXPosition(position.i(), position.j()));
        card.setLayoutY(computeYPosition(position.i(), position.j()));
        setCornerPositions();
    }

    private void setCornerPositions() {
        TOP_LEFT.setUserData(new Position(position.i(), position.j() + 1));
        TOP_RIGHT.setUserData(new Position(position.i() + 1, position.j()));
        BOTTOM_LEFT.setUserData(new Position(position.i() - 1, position.j()));
        BOTTOM_RIGHT.setUserData(new Position(position.i(), position.j() - 1));

        btnPositions.put(new Position(position.i(), position.j() + 1), TOP_LEFT);
        btnPositions.put(new Position(position.i() + 1, position.j()), TOP_RIGHT);
        btnPositions.put(new Position(position.i() - 1, position.j()), BOTTOM_LEFT);
        btnPositions.put(new Position(position.i(), position.j() - 1), BOTTOM_RIGHT);

        TOP_LEFT.setOnAction(this::placeCardRequest);
        TOP_RIGHT.setOnAction(this::placeCardRequest);
        BOTTOM_RIGHT.setOnAction(this::placeCardRequest);
        BOTTOM_LEFT.setOnAction(this::placeCardRequest);
    }

    private int computeXPosition(int i, int j) {
        int xPos;
        if (i == 0 && j == 0) {
            xPos = BASE_X;
        } else if (i == 0) {
            xPos = BASE_X - OFFSET_X * j;
        } else if (j == 0) {
            xPos = BASE_X + OFFSET_X * i;
        } else {
            xPos = BASE_X + OFFSET_X * i;
            xPos = xPos - OFFSET_X * j;
        }
        return xPos;
    }

    private int computeYPosition(int i, int j) {
        int yPos;
        if (i == 0 && j == 0) {
            yPos = BASE_Y;
        } else if (i == 0) {
            yPos = BASE_Y - OFFSET_Y * j;
        } else if (j == 0) {
            yPos = BASE_Y - OFFSET_Y * i;
        } else {
            yPos = BASE_Y - OFFSET_Y * i;
            yPos = yPos - OFFSET_Y * j;
        }
        return yPos;
    }

    public Map<Position, Button> getBtnPositions() {
        return this.btnPositions;
    }

    public int getId() {
        return this.id;
    }

    public Side getSide() {
        return this.side;
    }

    public void placeCardRequest(ActionEvent event) {
        Node node = (Node) event.getSource();
        Position position = (Position) node.getUserData();
        emitter.emit(new PositionSelectedEvent(position.i(), position.j()));
    }

    @Override
    public Registration onAny(EventListener<ViewEvent> listener) {
        return emitter.onAny(listener);
    }

    @Override
    public <T extends ViewEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }

    @Override
    public String getFXMLFileName() {
        return "card_layout";
    }
}

