package seedu.address.ui;

import javafx.application.Platform;

public class InfoPopupHandler implements PopupHandler {
    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            InfoPopup popup = new InfoPopup();
            popup.show(message);
        });
    }
}
