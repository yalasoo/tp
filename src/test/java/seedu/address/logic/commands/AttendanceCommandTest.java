package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class AttendanceCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndexes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AttendanceCommand(null, LocalDate.now(), AttendanceStatus.PRESENT));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        assertThrows(NullPointerException.class, () ->
                new AttendanceCommand(indexes, null, AttendanceStatus.PRESENT));
    }

    @Test
    public void constructor_nullStatus_throwsNullPointerException() {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        assertThrows(NullPointerException.class, () ->
                new AttendanceCommand(indexes, LocalDate.now(), null));
    }

    @Test
    public void execute_validSingleIndex_success() throws Exception {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate date = LocalDate.of(2025, 12, 12);
        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.PRESENT);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Marked"));
        assertTrue(result.getFeedbackToUser().contains("out of"));
        assertTrue(result.getFeedbackToUser().contains("PRESENT"));
        assertTrue(result.getFeedbackToUser().contains("12-12-2025"));
    }

    @Test
    public void execute_validMultipleIndexes_success() throws Exception {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        LocalDate date = LocalDate.of(2025, 12, 12);

        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.ABSENT);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Marked"));
        assertTrue(result.getFeedbackToUser().contains("out of"));
        assertTrue(result.getFeedbackToUser().contains("ABSENT"));
        assertTrue(result.getFeedbackToUser().contains("12-12-2025"));
    }

    // THIS IS NOT SUPPOSED TO WORK IN FUTURE IMPLEMENTATION
    // SINCE WE DON'T WANT TO ALLOW MARKING OF FUTURE DATES!
    // REMOVE AND FIX!!!
    @Test
    public void execute_validFutureDate_success() throws Exception {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate futureDate = LocalDate.of(2025, 12, 12).plusDays(1);
        AttendanceCommand command = new AttendanceCommand(indexes, futureDate, AttendanceStatus.LATE);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Marked"));
        assertTrue(result.getFeedbackToUser().contains("out of"));
        assertTrue(result.getFeedbackToUser().contains("LATE"));
        assertTrue(result.getFeedbackToUser().contains("13-12-2025"));
    }

    @Test
    public void execute_validPastDate_success() throws Exception {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate pastDate = LocalDate.now().minusDays(1);
        String pastDateStr = pastDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        AttendanceCommand command = new AttendanceCommand(indexes, pastDate, AttendanceStatus.SICK);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Marked"));
        assertTrue(result.getFeedbackToUser().contains("out of"));
        assertTrue(result.getFeedbackToUser().contains("SICK"));
        assertTrue(result.getFeedbackToUser().contains(pastDateStr));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Index> indexes = Set.of(outOfBoundIndex);
        AttendanceCommand command = new AttendanceCommand(indexes, LocalDate.now(), AttendanceStatus.PRESENT);

        assertThrows(CommandException.class,
                MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + ": "
                        + outOfBoundIndex.getOneBased(), () -> command.execute(model));
    }

    @Test
    public void execute_mixedValidAndInvalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON, outOfBoundIndex);
        AttendanceCommand command = new AttendanceCommand(indexes, LocalDate.now(), AttendanceStatus.PRESENT);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        AttendanceCommand command = new AttendanceCommand(indexes, LocalDate.now(), AttendanceStatus.PRESENT);

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_allAttendanceStatuses_success() throws Exception {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate date = LocalDate.now();
        String dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        for (AttendanceStatus status : AttendanceStatus.values()) {
            if (status != AttendanceStatus.UNRECORDED) { // Skip UNRECORDED as it's not for marking
                AttendanceCommand command = new AttendanceCommand(indexes, date, status);
                CommandResult result = command.execute(model);

                assertTrue(result.getFeedbackToUser().contains("Marked"));
                assertTrue(result.getFeedbackToUser().contains("out of"));
                assertTrue(result.getFeedbackToUser().contains(status.toString()));
                assertTrue(result.getFeedbackToUser().contains(dateStr));
            }
        }
    }

    @Test
    public void equals() {
        Set<Index> indexes1 = Set.of(INDEX_FIRST_PERSON);
        Set<Index> indexes2 = Set.of(INDEX_SECOND_PERSON);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        AttendanceCommand command1 = new AttendanceCommand(indexes1, today, AttendanceStatus.PRESENT);
        AttendanceCommand command2 = new AttendanceCommand(indexes1, today, AttendanceStatus.PRESENT);
        AttendanceCommand command3 = new AttendanceCommand(indexes2, today, AttendanceStatus.PRESENT);
        AttendanceCommand command4 = new AttendanceCommand(indexes1, tomorrow, AttendanceStatus.PRESENT);
        AttendanceCommand command5 = new AttendanceCommand(indexes1, today, AttendanceStatus.ABSENT);

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        assertTrue(command1.equals(command2));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different indexes -> returns false
        assertFalse(command1.equals(command3));

        // different date -> returns false
        assertFalse(command1.equals(command4));

        // different status -> returns false
        assertFalse(command1.equals(command5));
    }

    @Test
    public void toStringMethod() {
        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate date = LocalDate.of(2024, 10, 23);
        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.PRESENT);

        String expected = AttendanceCommand.class.getCanonicalName()
                + "{indexes=" + indexes
                + ", date=" + date
                + ", status=" + AttendanceStatus.PRESENT + "}";
        assertEquals(expected, command.toString());
    }
}
