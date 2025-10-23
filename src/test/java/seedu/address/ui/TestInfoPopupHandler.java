package seedu.address.ui;

public class TestInfoPopupHandler implements PopupHandler{
    @Override
    public void showMessage(String message) {
        System.out.println("[TestInfoPopup] " + message);
    }
}
