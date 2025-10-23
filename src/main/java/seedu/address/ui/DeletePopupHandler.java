package seedu.address.ui;

import java.util.List;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

public interface DeletePopupHandler {

    Person showDeletePopup(String message, List<Person> options) throws CommandException;

    boolean confirmDeletion(Person person);
}
