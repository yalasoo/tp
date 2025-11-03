package seedu.address.ui;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * A class handles showing message popup windows for test cases.
 */
public class TestInfoInfoPopupHandler implements InfoPopupHandler {

    @Override
    public void showMessage(String message, String instruction) throws CommandException {
        System.out.println(message);
    }
}
