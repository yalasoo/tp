package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;

public class SortCommandTest {

    @Test
    public void constructor_nullField_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new SortCommand(null, SortCommand.SortOrder.ASC));
    }

    @Test
    public void constructor_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new SortCommand(SortCommand.SortField.NAME, null));
    }

    @Test
    public void execute_sortByNameAscending_success() throws Exception {
        ModelStubWithSortTracking modelStub = new ModelStubWithSortTracking();
        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);

        CommandResult result = command.execute(modelStub);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSortMethodCalled());
        assertNotNull(modelStub.getLastComparator());
    }

    @Test
    public void execute_sortByNameDescending_success() throws Exception {
        ModelStubWithSortTracking modelStub = new ModelStubWithSortTracking();
        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.DESC);

        CommandResult result = command.execute(modelStub);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSortMethodCalled());
    }

    @Test
    public void execute_sortByClassAscending_success() throws Exception {
        ModelStubWithSortTracking modelStub = new ModelStubWithSortTracking();
        SortCommand command = new SortCommand(SortCommand.SortField.CLASS, SortCommand.SortOrder.ASC);

        CommandResult result = command.execute(modelStub);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSortMethodCalled());
    }

    @Test
    public void execute_sortByClassDescending_success() throws Exception {
        ModelStubWithSortTracking modelStub = new ModelStubWithSortTracking();
        SortCommand command = new SortCommand(SortCommand.SortField.CLASS, SortCommand.SortOrder.DESC);

        CommandResult result = command.execute(modelStub);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSortMethodCalled());
    }

    @Test
    public void execute_sortByTagAscending_success() throws Exception {
        ModelStubWithSortTracking modelStub = new ModelStubWithSortTracking();
        SortCommand command = new SortCommand(SortCommand.SortField.TAG, SortCommand.SortOrder.ASC);

        CommandResult result = command.execute(modelStub);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSortMethodCalled());
    }

    @Test
    public void execute_sortByTagDescending_success() throws Exception {
        ModelStubWithSortTracking modelStub = new ModelStubWithSortTracking();
        SortCommand command = new SortCommand(SortCommand.SortField.TAG, SortCommand.SortOrder.DESC);

        CommandResult result = command.execute(modelStub);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
        assertTrue(modelStub.isSortMethodCalled());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        SortCommand sortNameAscCommand = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);
        SortCommand sortNameDescCommand = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.DESC);
        SortCommand sortClassAscCommand = new SortCommand(SortCommand.SortField.CLASS, SortCommand.SortOrder.ASC);

        // same object -> returns true
        assertEquals(sortNameAscCommand, sortNameAscCommand);

        // same values -> returns true
        SortCommand sortNameAscCommandCopy = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);
        assertEquals(sortNameAscCommand, sortNameAscCommandCopy);

        // different types -> returns false
        assertNotEquals(1, sortNameAscCommand);

        // null -> returns false
        assertNotEquals(null, sortNameAscCommand);

        // different field -> returns false
        assertNotEquals(sortNameAscCommand, sortClassAscCommand);

        // different order -> returns false
        assertNotEquals(sortNameAscCommand, sortNameDescCommand);
    }

    @Test
    public void toStringMethod() {
        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);
        String expected = SortCommand.class.getCanonicalName() + "{field=" + SortCommand.SortField.NAME
                + ", order=" + SortCommand.SortOrder.ASC + "}";
        assertEquals(expected, command.toString());
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
     * A Model stub that tracks calls to sortFilteredPersonList method.
     */
    private class ModelStubWithSortTracking extends ModelStub {
        private boolean sortMethodCalled = false;
        private Comparator<Person> lastComparator;

        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) {
            this.sortMethodCalled = true;
            this.lastComparator = comparator;
        }

        public boolean isSortMethodCalled() {
            return sortMethodCalled;
        }

        public Comparator<Person> getLastComparator() {
            return lastComparator;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
