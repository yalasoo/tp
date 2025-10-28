package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class FavouriteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noPersonList_throwsCommandException() {
        FavouriteCommand favouriteCommand = new FavouriteCommand(List.of());

        String expectedMessage = "No contacts are available to be added to favourites.";

        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        CommandException exception = assertThrows(CommandException.class, () ->
                favouriteCommand.execute(expectedModel));
        assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    public void execute_outOfBoundsIndex_throwsCommandException() {
        int validLength = model.getFilteredPersonList().size();
        Index outOfBoundIndex = Index.fromOneBased(validLength + 1);
        List<Index> index = List.of(INDEX_FIRST_PERSON, outOfBoundIndex);

        FavouriteCommand favouriteCommand = new FavouriteCommand(index);

        String expectedMessage = "You have passed in out of bound index(es). \n"
                + "Use only positive indexes within 1 to " + validLength + " inclusive!";

        CommandException exception = assertThrows(CommandException.class, () ->
                favouriteCommand.execute(model));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void execute_personNotAlreadyFavourite_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withFavourite(false).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        List<Index> index = List.of(INDEX_FIRST_PERSON);

        FavouriteCommand favouriteCommand = new FavouriteCommand(index);
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_UPDATE_SUCCESS);

        assertCommandSuccess(favouriteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personAlreadyFavourite_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withFavourite(true).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        List<Index> index = List.of(INDEX_FIRST_PERSON);

        FavouriteCommand favouriteCommand = new FavouriteCommand(index);
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_UPDATE_SUCCESS);

        assertCommandSuccess(favouriteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleValidIndexes_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedFirstPerson = new PersonBuilder(firstPerson).withFavourite(true).build();

        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedSecondPerson = new PersonBuilder(secondPerson).withFavourite(false).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedFirstPerson);
        expectedModel.setPerson(secondPerson, editedSecondPerson);

        List<Index> indexes = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        FavouriteCommand favouriteCommand = new FavouriteCommand(indexes);
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_UPDATE_SUCCESS
                + "\nThese people were removed from favourites: \n" + firstPerson.getName() + "\n");

        assertCommandSuccess(favouriteCommand, model, expectedMessage, expectedModel);
    }
}
