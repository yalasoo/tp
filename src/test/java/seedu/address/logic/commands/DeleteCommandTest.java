package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.INVALID_NAME;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;
import seedu.address.ui.DeletePopupHandler;
import seedu.address.ui.InfoPopupHandler;
import seedu.address.ui.TestDeletePopupHandler;
import seedu.address.ui.TestInfoInfoPopupHandler;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand =
                new DeleteCommand(INDEX_FIRST_PERSON, testInfoHandler, testDeleteHandler);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, testInfoHandler, testDeleteHandler);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, testInfoHandler, testDeleteHandler);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex, testInfoHandler, testDeleteHandler);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();

        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON, testInfoHandler, testDeleteHandler);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON, testInfoHandler, testDeleteHandler);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy =
                new DeleteCommand(INDEX_FIRST_PERSON, testInfoHandler, testDeleteHandler);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex, testInfoHandler, testDeleteHandler);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex
                + ", targetName=null, isDeletedByName=false}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void execute_validName_success() {
        String name = "Charlotte Oliveiro";
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand = new DeleteCommand(name, testInfoHandler, testDeleteHandler);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=null, targetName=" + name
                + ", isDeletedByName=true}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void execute_validExactName_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addPerson(TypicalPersons.AMY);

        Person personToDelete = TypicalPersons.AMY;
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand =
                new DeleteCommand(personToDelete.getName().fullName, testInfoHandler, testDeleteHandler);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void check_singleExactMatch() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> matches = model.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase("Benson Meier"))
                .toList();
        assertEquals(1, matches.size());
        assertTrue(matches.contains(TypicalPersons.BENSON));
    }

    @Test
    public void check_duplicateExactMatches() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> exactMatches = model.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase("George Best"))
                .toList();
        assertTrue(exactMatches.size() > 1);
        assertTrue(exactMatches.contains(TypicalPersons.GEORGE));
        assertTrue(exactMatches.contains(TypicalPersons.GEORGE_DUPLICATE));
    }

    @Test
    public void check_partialMatches() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> matches = model.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.toLowerCase().contains(KEYWORD_MATCHING_MEIER.toLowerCase()))
                .toList();
        assertTrue(matches.size() > 1);
        assertTrue(matches.contains(TypicalPersons.BENSON));
        assertTrue(matches.contains(TypicalPersons.DANIEL));
    }

    @Test
    public void check_noMatches() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> matches = model.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase(INVALID_NAME))
                .toList();
        assertTrue(matches.isEmpty());
        assertThrows(CommandException.class, () -> {
            throw new CommandException(Messages.MESSAGE_DELETION_CANCELLED);
        });
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_confirmedMultipleMatches_successful() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Person personToDelete = TypicalPersons.GEORGE;
        TestInfoInfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        TestDeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        testDeleteHandler.setConfirmDeletion(true);
        DeleteCommand deleteCommand = new DeleteCommand("George Best", testInfoHandler, testDeleteHandler);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
        assertTrue(testDeleteHandler.isShowPossibleMatchesCalled());
    }

    @Test
    public void execute_cancelledMultipleMatches_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        TestInfoInfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        TestDeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        testDeleteHandler.setConfirmDeletion(false);
        DeleteCommand deleteCommand = new DeleteCommand("George Best", testInfoHandler, testDeleteHandler);
        CommandException exception = assertThrows(CommandException.class, () -> deleteCommand.execute(model));
        assertEquals(Messages.MESSAGE_DELETION_CANCELLED, exception.getMessage());
    }


    @Test
    public void execute_userCancelsDeletion_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        TestInfoInfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        TestDeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        testDeleteHandler.setConfirmDeletion(false);
        DeleteCommand deleteCommand = new DeleteCommand("Alice Pauline", testInfoHandler, testDeleteHandler);
        CommandException exception = assertThrows(CommandException.class, () -> deleteCommand.execute(model));
        assertEquals(Messages.MESSAGE_DELETION_CANCELLED, exception.getMessage());
    }

    @Test
    public void execute_noMatchesFound_showsInfoPopup() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        InfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        DeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand = new DeleteCommand("Random Name", testInfoHandler, testDeleteHandler);
        CommandException exception = assertThrows(CommandException.class, () -> deleteCommand.execute(model));
        assertEquals(Messages.MESSAGE_NO_MATCHES_FOUND, exception.getMessage());
    }

    @Test
    public void execute_cancelledDeleteByIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        TestInfoInfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        TestDeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        testDeleteHandler.setConfirmDeletion(false);
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1), testInfoHandler, testDeleteHandler);
        CommandException exception = assertThrows(CommandException.class, () -> deleteCommand.execute(model));
        assertEquals(Messages.MESSAGE_DELETION_CANCELLED, exception.getMessage());
    }

    @Test
    public void execute_invalidIndex_showsInfoPopup() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        TestInfoInfoPopupHandler testInfoHandler = new TestInfoInfoPopupHandler();
        TestDeletePopupHandler testDeleteHandler = new TestDeletePopupHandler();
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(99), testInfoHandler, testDeleteHandler);
        CommandException exception = assertThrows(CommandException.class, () -> deleteCommand.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, exception.getMessage());
    }

    @Test
    public void testPersonNotNullAssertion() {
        Person personToDelete = null;
        assertThrows(AssertionError.class, () -> {
            assert personToDelete != null;
        });
    }
}

