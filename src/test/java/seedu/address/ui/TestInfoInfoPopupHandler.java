package seedu.address.ui;

/**
 * A class handles showing delete error popup windows for test cases.
 */
public class TestInfoInfoPopupHandler implements InfoPopupHandler {

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
