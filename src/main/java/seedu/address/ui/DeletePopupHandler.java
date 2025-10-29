package seedu.address.ui;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * An interface handles showing delete confirmation popup windows in the application.
 */
public interface DeletePopupHandler {

    /**
     * Displays a popup window to user to select which {@code Person} should be deleted.
     *
     * @param message the message to display in the popup.
     * @param options a list of {@code Person} matches that the user can choose from.
     * @return the {@code Person} selected by the user for deletion.
     * @throws CommandException if the user cancels the deletion.
     */
    Person showPossibleMatches(String message, List<Person> options) throws CommandException;

    /**
     * Displays a popup window to user to confirm the deletion.
     *
     * @param person the {@code Person} selected by the user for deletion.
     * @return {@code true} if the user confirms deletion, {@code false} if the user cancels.
     */
    boolean confirmDeletion(Person person);
}
