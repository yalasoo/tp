package seedu.address.ui;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * A class handling delete pop up for tests.
 */
public class TestDeletePopupHandler implements DeletePopupHandler {

    @Override
    public Person showDeletePopup(String message, List<Person> options) throws CommandException {
        // Auto-select the first person for tests
        return options.get(0);
    }

    @Override
    public boolean confirmDeletion(Person person) {
        // Auto-confirm deletion in tests
        return true;
    }
}
