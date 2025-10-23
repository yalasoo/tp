package seedu.address.ui;

/**
 * A class handling delete error pop up for tests.
 */
public class TestInfoPopupHandler implements PopupHandler {
    @Override
    public void showMessage(String message) {
        System.out.println("[TestInfoPopup] " + message);
    }
}
