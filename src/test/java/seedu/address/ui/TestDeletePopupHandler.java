package seedu.address.ui;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * A class handles showing delete confirmation popup windows for test cases.
 */
public class TestDeletePopupHandler implements DeletePopupHandler {

    private boolean confirmDeletion = true;
    private boolean showPossibleMatchesCalled = false;

    @Override
    public Person showPossibleMatches(String message, List<Person> matches) throws CommandException {
        // Simulate user selecting the first match from the list
        showPossibleMatchesCalled = true;
        return matches.get(0);
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
     * Checks whether the deletion popup window is called.
     */
    public boolean isShowPossibleMatchesCalled() {
        return showPossibleMatchesCalled;
    }
}
