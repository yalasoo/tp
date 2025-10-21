package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_studentClass_correctSuccessMessage() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person studentPerson = new PersonBuilder().withClass("K1A").build();

        CommandResult commandResult = new AddCommand(studentPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(studentPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(studentPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_differentClassFormats_correctSuccessMessage() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person personK2C = new PersonBuilder().withClass("K2C").build();

        CommandResult commandResult = new AddCommand(personK2C).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(personK2C)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(personK2C), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonCaseInsensitiveName_throwsCommandException() {
        Person originalPerson = new PersonBuilder().withName("John Doe").withPhone("98765432").build();
        Person duplicatePerson = new PersonBuilder().withName("john doe").withPhone("98765432").build();
        AddCommand addCommand = new AddCommand(duplicatePerson);
        ModelStub modelStub = new ModelStubWithPerson(originalPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePersonMixedCase_throwsCommandException() {
        Person originalPerson = new PersonBuilder().withName("Mary Tan").withPhone("91234567").build();
        Person duplicatePerson = new PersonBuilder().withName("MARY TAN").withPhone("91234567").build();
        AddCommand addCommand = new AddCommand(duplicatePerson);
        ModelStub modelStub = new ModelStubWithPerson(originalPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_sameNameDifferentPhone_success() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person existingPerson = new PersonBuilder().withName("John Doe").withPhone("98765432").build();
        Person newPerson = new PersonBuilder().withName("John Doe").withPhone("91234567").build();

        // Add existing person first
        new AddCommand(existingPerson).execute(modelStub);

        // Adding person with same name but different phone should succeed
        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(newPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(existingPerson, newPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_samePhoneDifferentName_success() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person existingPerson = new PersonBuilder().withName("John Doe").withPhone("98765432").build();
        Person newPerson = new PersonBuilder().withName("Jane Doe").withPhone("98765432").build();

        // Add existing person first
        new AddCommand(existingPerson).execute(modelStub);

        // Adding person with same phone but different name should succeed
        CommandResult commandResult = new AddCommand(newPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(newPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(existingPerson, newPerson), modelStub.personsAdded);
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    @Test
    public void execute_personWithNote_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPersonWithNote = new PersonBuilder().withNote("Great student, very attentive").build();

        CommandResult commandResult = new AddCommand(validPersonWithNote).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPersonWithNote)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPersonWithNote), modelStub.personsAdded);
    }

    @Test
    public void execute_personWithEmptyNote_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPersonWithEmptyNote = new PersonBuilder().withNote("").build();

        CommandResult commandResult = new AddCommand(validPersonWithEmptyNote).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPersonWithEmptyNote)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPersonWithEmptyNote), modelStub.personsAdded);
    }

    @Test
    public void execute_studentWithNote_correctSuccessMessage() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person studentWithNote = new PersonBuilder().withClass("K2A")
                .withNote("Prefers morning classes").build();

        CommandResult commandResult = new AddCommand(studentWithNote).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(studentWithNote)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(studentWithNote), modelStub.personsAdded);
    }

    @Test
    public void execute_seniorStudentWithNote_correctSuccessMessage() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person seniorStudentWithNote = new PersonBuilder().withClass("Nursery")
                .withNote("Team lead for mobile app development").build();

        CommandResult commandResult = new AddCommand(seniorStudentWithNote).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(seniorStudentWithNote)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(seniorStudentWithNote), modelStub.personsAdded);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person getSelectedPerson() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
