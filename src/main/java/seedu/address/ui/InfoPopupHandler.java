package seedu.address.ui;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * An interface handles showing message popup windows in the application.
 */
public interface InfoPopupHandler {

    /**
     * Displays a popup window showing the specified message.
     *
     * @param message the message to display in the popup window.
     * @param instruction indicates the guide for the user to either proceed or cancel the command.
     */
    void showMessage(String message, String instruction) throws CommandException;
}
