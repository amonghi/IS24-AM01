package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.GUIElement;
import it.polimi.ingsw.am01.client.gui.controller.popup.PopupController;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The base controller for each scene.
 * It contains the main method to load a scene
 */
public abstract class SceneController implements GUIElement {

    protected final View view;
    private final List<EventEmitter.Registration> viewRegistrations;
    private Stage primaryStage;

    /**
     * It constructs a new SceneController, initializing an empty list to keep
     * all the {@link it.polimi.ingsw.am01.eventemitter.EventEmitter.Registration} needed in
     * a specific scene in order to listen to the events emitted by the {@link View}, specified
     * as parameter.
     *
     * @param view The reference to the main {@link View} class
     */
    public SceneController(View view) {
        this.viewRegistrations = new ArrayList<>();
        this.view = view;
    }

    /**
     * It adds to the main stage the current scene
     *
     * @param stage  The main {@link Stage} to which the scene has to be set
     * @param width  The width of the scene
     * @param height The height of the scene
     */
    public void loadScene(Stage stage, double width, double height) {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(
                Constants.FXML_PATH + getFXMLFileName() + Constants.FXML_EXTENSION
        ));

        fxmlLoader.setController(this);
        this.primaryStage = stage;

        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO: manage
        }

        registerListeners();

        stage.setTitle(Constants.WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * It set the {@link Stage#fullScreenProperty()} to:
     * <ul>
     *      <li> true, if the stage is not maximized </li>
     *      <li> false, if the stage is maximized </li>
     * </ul>
     *
     * @param event The {@link MouseEvent} emitted after clicking the mouse on the maximize/minimize icon
     */
    protected void setFullScreen(MouseEvent event) {
        primaryStage.setFullScreen(!primaryStage.isFullScreen());
    }

    /**
     * It calls the {@link PopupController#loadPopup(Stage, View)} method in order to show
     * a customized popup dialog
     *
     * @param popupController The main controller for the popup to be shown
     */
    protected void openPopup(PopupController popupController) {
        popupController.loadPopup(primaryStage, view);
    }

    /**
     * It allows you to register classes that emit events of interest and specify the method
     * to call in response to the emission of such events
     */
    protected abstract void registerListeners();

    /**
     * @return The list of {@link it.polimi.ingsw.am01.eventemitter.EventEmitter.Registration}  with the
     * classes currently listened to
     */
    public List<EventEmitter.Registration> getViewRegistrations() {
        return viewRegistrations;
    }
}
