package seedu.address.ui;

import javafx.application.Platform;

/**
 * A class handles showing delete error popup windows in the application.
 */
public class RealInfoInfoPopupHandler implements InfoPopupHandler {

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            InfoPopup popup = new InfoPopup();
            popup.show(message);
        });
    }
}
