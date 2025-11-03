package seedu.address.ui;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * A class handles showing delete error popup windows in the application.
 */
public class RealInfoInfoPopupHandler implements InfoPopupHandler {

    @Override
    public void showMessage(String message, String instruction) throws CommandException {
        InfoPopup popup = new InfoPopup();
        popup.show(message, instruction);
        if (!popup.isConfirmed()) {
            throw new CommandException("Command is cancelled.");
        }
    }
}
