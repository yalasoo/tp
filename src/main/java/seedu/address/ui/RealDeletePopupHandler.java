package seedu.address.ui;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * A class handles showing delete confirmation popup windows in the application.
 */
public class RealDeletePopupHandler implements DeletePopupHandler {

    @Override
    public Person showPossibleMatches(String message, List<Person> matches) throws CommandException {
        DeletePopup popup = new DeletePopup();
        popup.show(message, matches);
        if (popup.isConfirmed()) {
            return popup.getSelectedPerson();
        } else {
            throw new CommandException(Messages.MESSAGE_DELETION_CANCELLED);
        }
    }

    @Override
    public boolean confirmDeletion(Person person) {
        DeletePopup popup = new DeletePopup();
        popup.show(Messages.MESSAGE_DELETE_CONFIRMATION, List.of(person));
        return popup.isConfirmed();
    }
}
