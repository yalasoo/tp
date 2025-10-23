package seedu.address.ui;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * A class handles showing delete confirmation popups in the application.
 */
public class DeletePopupHandle implements DeletePopupHandler {

    @Override
    public Person showDeletePopup(String message, List<Person> options) throws CommandException {
        DeletePopup popup = new DeletePopup();
        popup.show(message, options);
        if (popup.isConfirmed()) {
            return popup.getSelectedPerson();
        } else {
            throw new CommandException("Deletion cancelled.");
        }
    }

    @Override
    public boolean confirmDeletion(Person person) {
        DeletePopup popup = new DeletePopup();
        popup.show("Are you sure you want to delete this contact ("
                + person.getName() + ")?\nType INDEX and ENTER to confirm or ESC to cancel.", List.of(person));
        return popup.isConfirmed();
    }
}
