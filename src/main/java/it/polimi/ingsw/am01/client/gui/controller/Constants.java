package it.polimi.ingsw.am01.client.gui.controller;

public class Constants {
    public static final String RESOURCES_PATH = "/it/polimi/ingsw/am01/";

    public static final String FXML_PATH = RESOURCES_PATH + "fxml/";
    public static final String BACK_CARD_PATH = RESOURCES_PATH + "cards/back/";
    public static final String FRONT_CARD_PATH = RESOURCES_PATH + "cards/front/";
    public static final String OBJECTIVE_PATH = RESOURCES_PATH + "objectives/";
    public static final String ICONS_PATH = RESOURCES_PATH + "icons/";

    public static final String FXML_EXTENSION = ".fxml";
    public static final String IMAGE_EXTENSION = ".png";

    //Size for positioning card in the playarea
    public static final int OFFSET_X = 224;
    public static final int OFFSET_Y = 111;
    public static final int BASE_X = 5000;
    public static final int BASE_Y = 5000;
    public static final double X_RATIO = 0.253;
    public static final double Y_RATIO = 0.445;

    //Card size
    public static final int HAND_CARD_WIDTH = 200;
    public static final int HAND_CARD_HEIGHT = 133;
    public static final int BOARD_CARD_WIDTH = 200;
    public static final int BOARD_CARD_HEIGHT = 133;
    public static final int CARD_PLACEMENT_WIDTH = 300;
    public static final int CARD_PLACEMENT_HEIGHT = 200;


    public static final double SCENE_WIDTH = 812;
    public static final double SCENE_HEIGHT = 484;
    public static final String WINDOW_TITLE = "Codex Naturalis";
    public static final double POPUP_WIDTH = 400;
    public static final double POPUP_HEIGHT = 250;


    //Connection
    public static final String DEFAULT_HOSTNAME = "0.0.0.0";
    public static final int DEFAULT_TCP_PORT = 8888;
    public static final int DEFAULT_RMI_PORT = 7777;
}
