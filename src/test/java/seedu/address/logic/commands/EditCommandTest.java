package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COLLEAGUE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_COLLEAGUE).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_COLLEAGUE).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_failure() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_editColleagueToCreateExactDuplicate_failure() {
        // Test editing a colleague to have identical details as another colleague
        Model freshModel = new ModelManager(new AddressBook(), new UserPrefs());

        Person colleague1 = new PersonBuilder().withName("Alice Smith").withPhone("81111111")
                .withEmail("alice@email.com").withTags("colleague").build();
        freshModel.addPerson(colleague1);

        Person colleague2 = new PersonBuilder().withName("Bob Johnson").withPhone("82222222")
                .withEmail("bob@email.com").withTags("colleague").build();
        freshModel.addPerson(colleague2);

        // Edit colleague2 to be identical to colleague1 (this should definitely fail)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Alice Smith")
                .withPhone("81111111")
                .withEmail("alice@email.com")
                .withTags("colleague").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        assertCommandFailure(editCommand, freshModel, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_editColleagueToHaveAllSameDetails_failure() {
        // Test editing to create duplicate by changing all fields to match existing colleague
        Model freshModel = new ModelManager(new AddressBook(), new UserPrefs());

        Person colleague1 = new PersonBuilder().withName("John Smith").withPhone("98765432")
                .withEmail("john@email.com").withTags("colleague").build();
        freshModel.addPerson(colleague1);

        Person colleague2 = new PersonBuilder().withName("Jane Doe").withPhone("87654321")
                .withEmail("jane@email.com").withTags("colleague").build();
        freshModel.addPerson(colleague2);

        // Edit colleague2 to have same phone and email as colleague1 (should fail due to phone conflict)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("98765432")
                .withEmail("john@email.com").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        assertCommandFailure(editCommand, freshModel, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_editColleagueToHaveSameName_success() {
        // Create a fresh model to avoid interference from typical persons
        Model freshModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Add one colleague to the model
        Person colleague1 = new PersonBuilder().withName("Alice Smith").withPhone("81111111")
                .withEmail("alice@email.com").withTags("colleague").build();
        freshModel.addPerson(colleague1);

        // Add another colleague with different details
        Person colleague2 = new PersonBuilder().withName("Bob Johnson").withPhone("82222222")
                .withEmail("bob@email.com").withTags("colleague").build();
        freshModel.addPerson(colleague2);

        // Edit colleague2 to have same name as colleague1 (should succeed)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Alice Smith").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        Person editedColleague2 = new PersonBuilder(colleague2).withName("Alice Smith").build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedColleague2));

        Model expectedModel = new ModelManager(new AddressBook(freshModel.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(colleague2, editedColleague2);

        assertCommandSuccess(editCommand, freshModel, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editStudentToHaveSamePhone_success() {
        // Add two students to the model
        Person student1 = new PersonBuilder().withName("Tommy Lee").withPhone("98765432")
                .withTags("student").build();
        Person student2 = new PersonBuilder().withName("Jimmy Lee").withPhone("87654321")
                .withTags("student").build();

        model.addPerson(student1);
        model.addPerson(student2);

        // Edit student2 to have same phone as student1 (should succeed for emergency contact scenario)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("98765432").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(model.getFilteredPersonList().size()), descriptor);

        Person editedStudent2 = new PersonBuilder(student2).withPhone("98765432").build();

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedStudent2));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(student2, editedStudent2);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    /**
     * Test that invalid index error is shown before no fields error.
     * This ensures users get the more specific error about the invalid index
     * rather than being asked to provide fields first.
     */
    @Test
    public void execute_invalidIndexNoFields_showsInvalidIndexError() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptor(); // No fields edited
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // Should show invalid index error, not "no fields" error
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Test that when index is valid but no fields are provided,
     * the appropriate "no fields" error is shown.
     */
    @Test
    public void execute_validIndexNoFields_showsNoFieldsError() {
        Index validIndex = INDEX_FIRST_PERSON;
        EditPersonDescriptor descriptor = new EditPersonDescriptor(); // No fields edited
        EditCommand editCommand = new EditCommand(validIndex, descriptor);

        // Should show no fields error
        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_NOT_EDITED);
    }

}
