package seedu.address.logic.commands.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.util.AttendanceCsvUtil.generateClassDailyAttendanceReport;
import static seedu.address.logic.commands.util.AttendanceCsvUtil.generateClassMonthlyAttendanceReport;
import static seedu.address.logic.commands.util.AttendanceCsvUtil.generateStudentsMonthlyAttendanceReport;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Class;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AttendanceCsvUtilTest {
    private Model model;

    @BeforeEach
    void setUp() {
        model = new ModelManager();
    }

    @Test
     void generateStudentsMonthlyAttendanceReport_validStudents_generatesCsv() {
        Person student = new PersonBuilder().withTags("student").build();
        Person colleague = new PersonBuilder().withName("Bob").withTags("colleague").build();
        model.addPerson(student);
        model.addPerson(colleague);

        Set<Index> indexes = Set.of(Index.fromOneBased(1), Index.fromOneBased(2));
        YearMonth month = YearMonth.of(2025, 1);

        String result = generateStudentsMonthlyAttendanceReport(model, indexes, month);

        assertTrue(result.contains("Name,Class")); // Header present
        assertTrue(result.contains(student.getName().toString()));
        assertFalse(result.contains(colleague.getName().toString())); // Should skip colleague
    }

    @Test
     void generateStudentsMonthlyAttendanceReport_nonStudents_notSaved() {
        Person colleague = new PersonBuilder().withTags("colleague").build();
        model.addPerson(colleague);

        Set<Index> indexes = Set.of(Index.fromOneBased(1));
        YearMonth month = YearMonth.of(2025, 1);

        String result = generateStudentsMonthlyAttendanceReport(model, indexes, month);

        assertTrue(result.isEmpty());
    }

    @Test
     void generateClassDailyAttendanceReport_validClass_generatesCsv() {
        Class studentClass = new Class("K1A");
        LocalDate date = LocalDate.of(2024, 12, 29);

        Person student1 = new PersonBuilder().withName("Bob").withTags("student").withClass("K1A").build();
        Person student2 = new PersonBuilder().withName("Tim").withTags("student").withClass("K1A").build();
        Person student3 = new PersonBuilder().withName("Lee").withTags("student").withClass("K1B").build();

        model.addPerson(student1);
        model.addPerson(student2);
        model.addPerson(student3);


        try {
            student1.markAttendance(date, AttendanceStatus.PRESENT);
            student2.markAttendance(date, AttendanceStatus.LATE);
            student2.markAttendance(date.plusDays(1), AttendanceStatus.SICK);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }

        String result = generateClassDailyAttendanceReport(model, studentClass, date);

        assertTrue(result.contains("Name,Status")); // Header present
        assertTrue(result.contains(student1.getName().toString()));
        assertTrue(result.contains(student2.getName().toString()));
        assertFalse(result.contains(student3.getName().toString()));

        assertTrue(result.contains(AttendanceStatus.PRESENT.toString()));
        assertTrue(result.contains(AttendanceStatus.LATE.toString()));
        assertFalse(result.contains(AttendanceStatus.SICK.toString()));
    }

    @Test
     void generateClassMonthlyAttendanceReport_validClass_generateCsv() {
        Class studentClass = new Class("K1A");
        YearMonth month = YearMonth.of(2024, 12);
        LocalDate date = LocalDate.of(2024, 12, 29);

        Person student1 = new PersonBuilder().withName("Bob").withTags("student").withClass("K1A").build();
        Person student2 = new PersonBuilder().withName("Tim").withTags("student").withClass("K1A").build();
        Person student3 = new PersonBuilder().withName("Lee").withTags("student").withClass("K1B").build();

        model.addPerson(student1);
        model.addPerson(student2);
        model.addPerson(student3);

        try {
            student1.markAttendance(date, AttendanceStatus.PRESENT);
            student2.markAttendance(date, AttendanceStatus.LATE);
            student1.markAttendance(date.plusDays(1), AttendanceStatus.SICK);
            student2.markAttendance(date.minusDays(10), AttendanceStatus.ABSENT);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }

        String result = generateClassMonthlyAttendanceReport(model, studentClass, month);

        assertTrue(result.contains("Name,Class")); // Header present
        assertTrue(result.contains(student1.getName().toString()));
        assertTrue(result.contains(student2.getName().toString()));
        assertFalse(result.contains(student3.getName().toString()));

        assertTrue(result.contains(AttendanceStatus.PRESENT.toString()));
        assertTrue(result.contains(AttendanceStatus.LATE.toString()));
        assertTrue(result.contains(AttendanceStatus.SICK.toString()));
        assertTrue(result.contains(AttendanceStatus.ABSENT.toString()));
    }
}
