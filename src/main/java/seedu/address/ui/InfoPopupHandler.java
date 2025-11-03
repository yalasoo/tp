package seedu.address.ui;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * An interface handles showing delete error message popup windows in the application.
 */
public interface InfoPopupHandler {

    /**
     * Displays a popup window showing the specified error message.
     *
     * @param message the error message to display in the popup window.
     */
    void showMessage(String message, String instruction) throws CommandException;
}
