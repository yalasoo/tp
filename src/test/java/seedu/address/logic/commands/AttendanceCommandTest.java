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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AttendanceCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

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
    public void execute_emptyContactList_failure() {
        Model newModel = new ModelManager();

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);

        AttendanceCommand command = new AttendanceCommand(indexes, LocalDate.now(), AttendanceStatus.PRESENT);

        assertThrows(CommandException.class, () -> command.execute(newModel));
    }

    @Test
    public void execute_singleStudent_success() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate date = LocalDate.of(2024, 1, 1);
        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.PRESENT);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Marked"));
        assertTrue(result.getFeedbackToUser().contains("out of"));
        assertTrue(result.getFeedbackToUser().contains("PRESENT"));
        assertTrue(result.getFeedbackToUser().contains("01-01-2024"));
    }

    @Test
    public void execute_multipleStudents_success() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        Person student2 = new PersonBuilder().withName("StuTwo").withTags("student").withBirthday("02-02-2024").build();
        model.addPerson(student1);
        model.addPerson(student2);

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        LocalDate date = LocalDate.of(2024, 1, 1);

        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.ABSENT);

        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Marked"));
        assertTrue(result.getFeedbackToUser().contains("out of"));
        assertTrue(result.getFeedbackToUser().contains("ABSENT"));
        assertTrue(result.getFeedbackToUser().contains("01-01-2024"));
    }

    @Test
    public void execute_tomorrowDate_failure() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate futureDate = LocalDate.now().plusDays(1);
        AttendanceCommand command = new AttendanceCommand(indexes, futureDate, AttendanceStatus.LATE);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_beforeBirthday_failure() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate beforeBirthday = LocalDate.of(2023, 1, 1);
        AttendanceCommand command = new AttendanceCommand(indexes, beforeBirthday, AttendanceStatus.LATE);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_birthdayBoundaryDate_failure() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-1900").build();
        model.addPerson(student1);

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate beforeBirthday = LocalDate.of(1900, 1, 1).minusDays(1);
        AttendanceCommand command = new AttendanceCommand(indexes, beforeBirthday, AttendanceStatus.LATE);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_validPastDate_success() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

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
    public void execute_allAttendanceStatuses_success() throws Exception {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

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
    public void execute_colleague_throwsCommandException() {
        Person colleague1 = new PersonBuilder().withName("ColOne").withTags("colleague")
                .withBirthday("01-01-2024").build();
        model.addPerson(colleague1);

        Set<Index> indexes = Set.of(INDEX_FIRST_PERSON);
        LocalDate date = LocalDate.now();

        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.PRESENT);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test public void execute_studentAndColleague_success() throws CommandException {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        Person colleague1 = new PersonBuilder().withName("ColOne").withTags("colleague")
                .withBirthday("01-01-2024").build();
        model.addPerson(student1);
        model.addPerson(colleague1);

        LocalDate date = LocalDate.of(2024, 1 ,1);
        Set<Index> indexes =  Set.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        AttendanceCommand command = new AttendanceCommand(indexes, date, AttendanceStatus.SICK);
        CommandResult result = command.execute(model);
        System.out.println(result.getFeedbackToUser());

        assertTrue(result.getFeedbackToUser().contains("Marked 1 out of 2 contacts as "
                + AttendanceStatus.SICK
                + " on " + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

        assertTrue(result.getFeedbackToUser().contains(colleague1.getName() + " [Not a student]"));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Index> indexes = Set.of(outOfBoundIndex);
        AttendanceCommand command = new AttendanceCommand(indexes, LocalDate.now(), AttendanceStatus.PRESENT);

        assertThrows(CommandException.class,
                MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + ": "
                        + outOfBoundIndex.getOneBased(), () -> command.execute(model));
    }

    @Test
    public void execute_mixedValidAndInvalidIndex_throwsCommandException() {
        Person student1 = new PersonBuilder().withName("StuOne").withTags("student").withBirthday("01-01-2024").build();
        model.addPerson(student1);

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
