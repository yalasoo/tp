package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs.
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void setSelectedPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setSelectedPerson(null));
    }

    @Test
    public void setSelectedPerson_validPerson_success() {
        modelManager.addPerson(ALICE);
        modelManager.setSelectedPerson(ALICE);
        assertEquals(ALICE, modelManager.getSelectedPerson());
    }

    @Test
    public void setSelectedPerson_differentPerson_success() {
        modelManager.addPerson(ALICE);
        modelManager.addPerson(BENSON);

        modelManager.setSelectedPerson(ALICE);
        assertEquals(ALICE, modelManager.getSelectedPerson());

        modelManager.setSelectedPerson(BENSON);
        assertEquals(BENSON, modelManager.getSelectedPerson());
    }

    @Test
    public void getSelectedPerson_noPersonSelected_returnsNull() {
        assertNull(modelManager.getSelectedPerson());
    }

    @Test
    public void selectedPersonProperty_notNull() {
        assertNotNull(modelManager.selectedPersonProperty());
    }

    @Test
    public void selectedPersonProperty_reflectsChanges() {
        modelManager.addPerson(ALICE);
        modelManager.setSelectedPerson(ALICE);

        assertEquals(ALICE, modelManager.selectedPersonProperty().get());

        modelManager.addPerson(BENSON);
        modelManager.setSelectedPerson(BENSON);
        assertEquals(BENSON, modelManager.selectedPersonProperty().get());
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void sortFilteredPersonList_validComparator_sortsAddressBook() {
        ModelManager modelManager = new ModelManager();
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();

        // Add in reverse order
        modelManager.addPerson(bob);
        modelManager.addPerson(alice);

        // Store original order
        List<Person> originalOrder = new ArrayList<>(modelManager.getFilteredPersonList());

        // Sort through ModelManager's public API
        modelManager.sortFilteredPersonList(Comparator.comparing(p -> p.getName().toString()));

        // Verify the public filtered list is sorted
        List<Person> sortedOrder = new ArrayList<>(modelManager.getFilteredPersonList());
        assertNotEquals(originalOrder, sortedOrder);
        assertEquals(alice, sortedOrder.get(0));
        assertEquals(bob, sortedOrder.get(1));
    }

    @Test
    public void sortFilteredPersonList_nullComparator_throwsNullPointerException() {
        ModelManager modelManager = new ModelManager();
        assertThrows(NullPointerException.class, () -> modelManager.sortFilteredPersonList(null));
    }

    @Test
    public void retrieveInitialFavList_returnsCorrectList() {
        ModelManager modelManager = new ModelManager();
        Person firstPerson = new PersonBuilder().withName("Alice").withFavourite(true).build();
        Person secondPerson = new PersonBuilder().withName("Bob").withFavourite(false).build();
        Person thirdPerson = new PersonBuilder().withName("Charles").withFavourite(true).build();

        // Add the people
        modelManager.addPerson(firstPerson);
        modelManager.addPerson(secondPerson);
        modelManager.addPerson(thirdPerson);

        ArrayList<Index> actualList = modelManager.retrieveInitialFavList();
        ArrayList<Index> expectedList = new ArrayList<>();

        // From zeroBased indexing, firstPerson will be index 1
        expectedList.add(Index.fromZeroBased(0));
        expectedList.add(Index.fromZeroBased(2));
        assertEquals(actualList, expectedList);
    }

    @Test
    public void updateFilteredPersonListCumulative_returnsCorrectList() {
        ModelManager modelManager = new ModelManager();
        Person firstPerson = new PersonBuilder().withName("Alice").withFavourite(true).build();
        Person secondPerson = new PersonBuilder().withName("Bob Doe").withFavourite(false).build();
        Person thirdPerson = new PersonBuilder().withName("Charles Doe").withFavourite(true).build();

        // Add the people
        modelManager.addPerson(firstPerson);
        modelManager.addPerson(secondPerson);
        modelManager.addPerson(thirdPerson);

        // Obtain current filteredList (should have all three persons)
        FilteredList<Person> filteredList = (FilteredList<Person>) modelManager.getFilteredPersonList();

        // Set first predicate
        filteredList.setPredicate(person -> person.getName().fullName.contains("a"));
        // Use the method for combined predicate
        modelManager.updateFilteredPersonListCumulative(person -> person.getName().fullName.contains("Doe"));

        // Expected output list
        ObservableList<Person> expectedList = FXCollections.observableArrayList(thirdPerson);
        // Actual output list
        ObservableList<Person> actualList = modelManager.getFilteredPersonList();
        assertEquals(actualList, expectedList);
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertEquals(modelManager, modelManagerCopy);

        // same object -> returns true
        assertEquals(modelManager, modelManager);

        // null -> returns false
        assertNotEquals(null, modelManager);

        // different types -> returns false
        assertNotEquals(5, modelManager);

        // different addressBook -> returns false
        assertNotEquals(modelManager, new ModelManager(differentAddressBook, userPrefs));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
