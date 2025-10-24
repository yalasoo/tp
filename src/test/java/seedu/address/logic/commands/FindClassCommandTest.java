package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.GEORGE_DUPLICATE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ClassContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindClassCommand}.
 */
public class FindClassCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ClassContainsKeywordsPredicate firstPredicate =
                new ClassContainsKeywordsPredicate(Collections.singletonList("first"));
        ClassContainsKeywordsPredicate secondPredicate =
                new ClassContainsKeywordsPredicate(Collections.singletonList("second"));

        FindClassCommand findFirstCommand = new FindClassCommand(firstPredicate);
        FindClassCommand findSecondCommand = new FindClassCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindClassCommand findFirstCommandCopy = new FindClassCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        ClassContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindClassCommand command = new FindClassCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        ClassContainsKeywordsPredicate predicate = preparePredicate("K1A Nursery");
        FindClassCommand command = new FindClassCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, GEORGE, GEORGE_DUPLICATE), model.getFilteredPersonList());
    }

    @Test
    public void execute_multiplePartialKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 5);
        ClassContainsKeywordsPredicate predicate = preparePredicate("K1 urs");
        FindClassCommand command = new FindClassCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, GEORGE, GEORGE_DUPLICATE), model.getFilteredPersonList());

    }

    @Test
    public void toStringMethod() {
        ClassContainsKeywordsPredicate predicate = new ClassContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindClassCommand findClassCommand = new FindClassCommand(predicate);
        String expected = FindClassCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findClassCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code ClassContainsKeywordsPredicate}.
     */
    private ClassContainsKeywordsPredicate preparePredicate(String userInput) {
        return new ClassContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
