package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.model.Position;
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

import static it.polimi.ingsw.am01.client.gui.controller.Constants.*;

public class CardPlacementController extends CardController implements Comparable<CardPlacementController> {

    private final Map<Position, Button> btnPositions;
    private Position position;
    private int seq;
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
        super(id, side);
        loadComponent();

        this.btnPositions = new HashMap<>();
    }

    @FXML
    private void initialize() {
        String path = side.equals(Side.FRONT) ? setFrontImage() : setBackImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));
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

    public void setSeq(int seq) {
        this.seq = seq;
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

    public void placeCardRequest(ActionEvent event) {
        Node node = (Node) event.getSource();
        Position position = (Position) node.getUserData();
        GUIView.getInstance().getPlayAreaController().placeCard(position.i(), position.j());
    }

    @Override
    public String getFXMLFileName() {
        return "card_placement";
    }

    @Override
    public int compareTo(CardPlacementController other) {
        return Integer.compare(this.seq, other.seq);
    }
}

