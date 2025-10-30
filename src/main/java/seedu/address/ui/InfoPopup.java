package seedu.address.ui;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A popup window that displays error message when a user executes the delete contact command.
 * This is used for invalid indexes or when no matching contacts are found.
 */
public class InfoPopup extends UiPart<Stage> {

    private static final String FXML = "InfoPopup.fxml";

    @FXML
    private Label msgLabel;

    private boolean isClosed = false;

    /**
     * Creates a new Information popup window.
     *
     * @param root Stage to use as the root of the Information popup window.
     */
    public InfoPopup(Stage root) {
        super(FXML, root);
        setUpKeyboardHandlers();
        root.setWidth(300);
        root.setHeight(200);
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
     * @param message indicates the result of the invalid delete command.
     */
    public void show(String message) {
        msgLabel.setText(message);
        isClosed = false;
        getRoot().centerOnScreen();
        getRoot().show();
    }

    /**
     * Defines how keyboard inputs interact with the information popup window.
     * Pressing the ENTER key allows the user to proceed and close the popup window.
     */
    private void setUpKeyboardHandlers() {
        getRoot().getScene().setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                handleEnter();
            }
        });
    }

    /**
     * Handles the ENTER key action by closing the popup window.
     */
    private void handleEnter() {
        isClosed = true;
        getRoot().hide();
    }
}
