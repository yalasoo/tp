package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class FavouriteCommandTest {

    private static Logger logger = LogsCenter.getLogger(FavouriteCommandTest.class);
    private Model model;

    /**
     * Creates an AddressBook to refer to for testing purpose which is non-static.
     * @return AddressBook for testing.
     */
    private AddressBook createFreshBook() {
        AddressBook freshBook = new AddressBook();

        Person alice = new PersonBuilder().withName("Alice Zayn")
                .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
                .withPhone("94351253").withClass("K1A").withBirthday("15-03-2018")
                .withNote("She likes aardvarks.").withTags("student").build();

        Person benson = new PersonBuilder().withName("Benson Moon")
                .withAddress("311, Clementi Ave 2, #02-25")
                .withClass("K1B").withEmail("johnd@example.com")
                .withPhone("98765432").withBirthday("24-12-2017")
                .withNote("He can't take beer!").withTags("student").build();

        freshBook.addPerson(alice);
        freshBook.addPerson(benson);
        return freshBook;
    }
    @BeforeEach
    void init() {
        model = new ModelManager(createFreshBook(), new UserPrefs());
    }


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
        logger.info("TEST 1: notfav");

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withFavourite(false).build();
        // Must also update the model itself
        model.setPerson(firstPerson, editedPerson);

        // Let's make expectedModel (the expected output model) from executing command on model
        Model expectedModel = new ModelManager(createFreshBook(), new UserPrefs());
        Person firstExpectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedFirstExpectedPerson = new PersonBuilder(firstExpectedPerson).withFavourite(true).build();
        expectedModel.setPerson(firstExpectedPerson, editedFirstExpectedPerson);

        List<Index> index = List.of(INDEX_FIRST_PERSON);

        FavouriteCommand favouriteCommand = new FavouriteCommand(index);
        // The output message we expect from executing command on model
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_UPDATE_SUCCESS
                + "\nThese people were added to favourites: \n" + firstExpectedPerson.getName() + "\n");

        assertCommandSuccess(favouriteCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_personAlreadyFavourite_success() {
        logger.info("TEST 2: alreadyFav");

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withFavourite(true).build();
        // Must also update the model itself
        model.setPerson(firstPerson, editedPerson);

        // Let's make expectedModel (the expected output model) from executing command on model
        Model expectedModel = new ModelManager(createFreshBook(), new UserPrefs());
        Person firstExpectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedFirstExpectedPerson = new PersonBuilder(firstExpectedPerson).withFavourite(false).build();
        expectedModel.setPerson(firstExpectedPerson, editedFirstExpectedPerson);

        List<Index> index = List.of(INDEX_FIRST_PERSON);

        FavouriteCommand favouriteCommand = new FavouriteCommand(index);
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_UPDATE_SUCCESS
                + "\nThese people were removed from favourites: \n" + firstExpectedPerson.getName() + "\n");

        assertCommandSuccess(favouriteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleValidIndexes_success() {
        logger.info("TEST 3: multipleindex");
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedFirstPerson = new PersonBuilder(firstPerson).withFavourite(true).build();

        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedSecondPerson = new PersonBuilder(secondPerson).withFavourite(false).build();

        // Must update the actual model as the command will be executed on actual model
        model.setPerson(firstPerson, editedFirstPerson);
        model.setPerson(secondPerson, editedSecondPerson);


        // Let's make expectedModel (the expected output model) from executing command on model
        Model expectedModel = new ModelManager(createFreshBook(), new UserPrefs());
        Person firstExpectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedFirstExpectedPerson = new PersonBuilder(firstExpectedPerson).withFavourite(false).build();
        expectedModel.setPerson(firstExpectedPerson, editedFirstExpectedPerson);

        Person secondExpectedPerson = expectedModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedSecondExpectedPerson = new PersonBuilder(secondExpectedPerson).withFavourite(true).build();
        expectedModel.setPerson(secondExpectedPerson, editedSecondExpectedPerson);

        List<Index> indexes = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        FavouriteCommand favouriteCommand = new FavouriteCommand(indexes);
        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_UPDATE_SUCCESS
                + "\nThese people were added to favourites: \n" + secondPerson.getName() + "\n"
                + "\nThese people were removed from favourites: \n" + firstPerson.getName() + "\n");

        assertCommandSuccess(favouriteCommand, model, expectedMessage, expectedModel);
    }
}
