package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Class;
import seedu.address.model.person.Person;
import seedu.address.storage.AttendanceCsvStorage;
import seedu.address.testutil.PersonBuilder;

public class AttendanceDownloadCommandTest {

    @TempDir
    Path tempDir;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ModelManager();
        AttendanceCsvStorage.setDataDirectory(tempDir);
    }

    @Test
    void constructor_nullDate_throwsNullPointerException() {
        Set<Index> indexes = Set.of(Index.fromOneBased(1));
        List<Class> classes = List.of(new Class("K1A"));
        YearMonth month = YearMonth.of(2025, 1);

        assertThrows(NullPointerException.class, () ->
                new AttendanceDownloadCommand(indexes, classes, null, month,
                        true, true));
    }

    @Test
    void constructor_nullMonth_throwsNullPointerException() {
        Set<Index> indexes = Set.of(Index.fromOneBased(1));
        List<Class> classes = List.of(new Class("K1A"));
        LocalDate date = LocalDate.of(2025, 12, 29);

        assertThrows(NullPointerException.class, () ->
                new AttendanceDownloadCommand(
                        indexes, classes, date, null, true, true));
    }

    @Test
    void execute_studentMonthlyReport_success() {
        Set<Index> indexes = Set.of(Index.fromOneBased(1));
        LocalDate date = LocalDate.of(2025, 12, 29);
        YearMonth month = YearMonth.of(2025, 1);

        AttendanceDownloadCommand command = new AttendanceDownloadCommand(
                indexes, null, date, month, false, true);

        Person student = new PersonBuilder().withTags("student").build();
        model.addPerson(student);

        CommandResult result = command.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Saved to:"));
    }

    @Test
    void execute_classMonthlyReport_success() {
        List<Class> classes = List.of(new Class("K1A"));
        LocalDate date = LocalDate.of(2025, 12, 29);
        YearMonth month = YearMonth.of(2025, 1);

        // With user month
        AttendanceDownloadCommand command1 = new AttendanceDownloadCommand(
                null, classes, date, month, false, true);

        // Without user month
        AttendanceDownloadCommand command2 = new AttendanceDownloadCommand(
                null, classes, date, month, false, false);

        Person student = new PersonBuilder().withTags("student").withClass("K1A").build();
        model.addPerson(student);

        CommandResult result1 = command1.execute(model);
        CommandResult result2 = command2.execute(model);

        assertTrue(result1.getFeedbackToUser().contains("Saved to:"));
        assertTrue(result2.getFeedbackToUser().contains("Saved to:"));
    }

    @Test
    void execute_classDailyReport_success() {
        List<Class> classes = List.of(new Class("K1A"));
        LocalDate date = LocalDate.of(2025, 12, 29);
        YearMonth month = YearMonth.of(2025, 1);

        AttendanceDownloadCommand command = new AttendanceDownloadCommand(
                null, classes, date, month, true, true);

        Person student = new PersonBuilder().withTags("student").build();
        model.addPerson(student);

        CommandResult result = command.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Saved to:"));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        AttendanceDownloadCommand command = new AttendanceDownloadCommand(
                Set.of(Index.fromOneBased(1)), null, LocalDate.now(), YearMonth.now(),
                false, false);

        assertEquals(command, command);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        AttendanceDownloadCommand command = new AttendanceDownloadCommand(
                Set.of(Index.fromOneBased(1)), null, LocalDate.now(), YearMonth.now(),
                false, false);

        assertFalse(command.equals("not a command"));
        assertFalse(command.equals(null));
    }

    @Test
    public void toString_containsAllFields() {
        Set<Index> indexes = Set.of(Index.fromOneBased(1));
        List<Class> classes = List.of(new Class("K1A"));
        LocalDate date = LocalDate.of(2025, 12, 29);
        YearMonth month = YearMonth.of(2025, 1);

        AttendanceDownloadCommand command = new AttendanceDownloadCommand(
                indexes, classes, date, month, true, false);

        String result = command.toString();

        assertTrue(result.contains("indexes"));
        assertTrue(result.contains("classes"));
        assertTrue(result.contains("date"));
        assertTrue(result.contains("month"));
        assertTrue(result.contains("userProvideDate"));
        assertTrue(result.contains("userProvideMonth"));
    }
}
