package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SortCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();

        Person andy = new PersonBuilder().withName("Andy").build();
        Person ben = new PersonBuilder().withName("Ben").build();
        Person craig = new PersonBuilder().withName("Craig").build();
        Person danny = new PersonBuilder().withName("Danny").build();

        model.addPerson(andy);
        model.addPerson(ben);
        model.addPerson(craig);
        model.addPerson(danny);
    }

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
    public void execute_emptyContactList_throwsCommandException() {
        Model model = new ModelManager();

        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_sortByNameAscending_success() throws Exception {
        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC);

        CommandResult result = command.execute(model);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortByNameDescending_success() throws Exception {
        SortCommand command = new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.DESC);

        CommandResult result = command.execute(model);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortByClassAscending_success() throws Exception {
        SortCommand command = new SortCommand(SortCommand.SortField.CLASS, SortCommand.SortOrder.ASC);

        CommandResult result = command.execute(model);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortByClassDescending_success() throws Exception {
        SortCommand command = new SortCommand(SortCommand.SortField.CLASS, SortCommand.SortOrder.DESC);

        CommandResult result = command.execute(model);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortByTagAscending_success() throws Exception {
        SortCommand command = new SortCommand(SortCommand.SortField.TAG, SortCommand.SortOrder.ASC);

        CommandResult result = command.execute(model);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortByTagDescending_success() throws Exception {
        SortCommand command = new SortCommand(SortCommand.SortField.TAG, SortCommand.SortOrder.DESC);

        CommandResult result = command.execute(model);

        assertEquals(SortCommand.MESSAGE_SORT_SUCCESS, result.getFeedbackToUser());
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
}
