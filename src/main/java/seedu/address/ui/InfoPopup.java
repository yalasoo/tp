package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * A popup window for error message when a person executes delete contact command.
 * Applicable for invalid index and no matches.
 */
public class InfoPopup extends UiPart<Stage> {

    private static final String FXML = "InfoPopup.fxml";

    @FXML
    private Label msgLabel;

    private boolean isClosed = false;

    /**
     * Creates a new Information Pop up.
     *
     * @param root Stage to use as the root of the Information Pop up.
     */
    public InfoPopup(Stage root) {
        super(FXML, root);
        setUpKeyboardHandlers();
    }

    /**
     * Creates a new Information Pop up.
     */
    public InfoPopup() {
        this(new Stage());
    }

    /**
     * Determines the information to be displayed in the Information Pop up.
     *
     * @param message indicates the result of the invalid delete command.
     */
    public void show(String message) {
        msgLabel.setText(message);
        isClosed = false;
        getRoot().centerOnScreen();
        getRoot().show();
    }

    private void setUpKeyboardHandlers() {
        getRoot().getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case ENTER -> handleEnter();
            default -> { }
            }
        });
    }

    private void handleEnter() {
        isClosed = true;
        getRoot().hide();
    }
}
