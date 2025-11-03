package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays message when a user executes the delete or clear command.
 */
public class InfoPopup extends UiPart<Stage> {

    private static final String FXML = "InfoPopup.fxml";

    @FXML
    private Label msgLabel;
    @FXML
    private Label instrLabel;

    private boolean isClosed = false;
    private boolean isConfirmed = false;

    /**
     * Creates a new Information popup window.
     *
     * @param root Stage to use as the root of the Information popup window.
     */
    public InfoPopup(Stage root) {
        super(FXML, root);
        setUpKeyboardHandlers();
        root.setWidth(400);
        root.setHeight(300);
        root.initModality(Modality.APPLICATION_MODAL);
        root.setAlwaysOnTop(true);
    }

    /**
     * Creates a new Information popup window.
     */
    public InfoPopup() {
        this(new Stage());
    }

    /**
     * Determines the information to be displayed in the Information popup window.
     *
     * @param message indicates the message related to the command.
     * @param instruction indicates the guide for the user to either proceed or cancel the command.
     */
    public void show(String message, String instruction) {
        msgLabel.setText(message);
        isClosed = false;
        getRoot().centerOnScreen();
        getRoot().showAndWait();
    }

    /**
     * Defines how keyboard inputs interact with the information popup window.
     */
    private void setUpKeyboardHandlers() {
        getRoot().getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case ENTER -> handleEnter();
            case ESCAPE -> handleEscape();
            default -> { }
            }
        });
    }

    /**
     * Handles the ESCAPE key action by cancelling the command and closing the window.
     */
    private void handleEscape() {
        isClosed = true;
        getRoot().hide();
    }

    private void handleEnter() {
        isConfirmed = true;
        getRoot().hide();
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}
