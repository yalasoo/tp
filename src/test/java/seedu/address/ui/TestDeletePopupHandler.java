package seedu.address.ui;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * A class handling delete pop up for tests.
 */
public class TestDeletePopupHandler implements DeletePopupHandler {

    private boolean confirmDeletion = true;
    private boolean showDeletePopupCalled = false;

    @Override
    public Person showDeletePopup(String message, List<Person> options) throws CommandException {
        // Auto-select the first person for tests
        showDeletePopupCalled = true;
        return options.get(0);
    }

    /**
     * Sets whether the next simulated deletion should be confirmed or cancelled.
     */
    public void setConfirmDeletion(boolean confirmDeletion) {
        this.confirmDeletion = confirmDeletion;
    }

    @Override
    public boolean confirmDeletion(Person person) {
        return confirmDeletion;
    }

    /**
     * Checks whether the deletion pop up window is called.
     */
    public boolean isShowDeletePopupCalled() {
        return showDeletePopupCalled;
    }
}
