package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class AttendanceTest {

    private final Attendance attendance = new Attendance();

    @Test
    public void constructor_initializesEmptyAttendance() {
        assertTrue(attendance.getAttendanceRecords().isEmpty());
    }

    @Test
    public void markAttendance_validInput_addsToRecords() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        attendance.markAttendance(date, AttendanceStatus.PRESENT);

        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecords();
        assertEquals(1, records.size());
        assertEquals(AttendanceStatus.PRESENT, records.get(date));
    }

    @Test
    public void markAttendance_multipleDates_addsAllToRecords() {
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 16);

        attendance.markAttendance(date1, AttendanceStatus.PRESENT);
        attendance.markAttendance(date2, AttendanceStatus.LATE);

        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecords();
        assertEquals(2, records.size());
        assertEquals(AttendanceStatus.PRESENT, records.get(date1));
        assertEquals(AttendanceStatus.LATE, records.get(date2));
    }

    @Test
    public void markAttendance_overwriteExistingDate_updatesRecord() {
        LocalDate date = LocalDate.of(2024, 1, 15);

        attendance.markAttendance(date, AttendanceStatus.PRESENT);
        attendance.markAttendance(date, AttendanceStatus.ABSENT);

        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecords();
        assertEquals(1, records.size());
        assertEquals(AttendanceStatus.ABSENT, records.get(date));
    }

    @Test
    public void getAttendanceRecords_nonEmptyAttendance_returnsCopy() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        attendance.markAttendance(date, AttendanceStatus.PRESENT);

        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecords();
        records.put(LocalDate.of(2024, 1, 16), AttendanceStatus.LATE);

        // Original should not be modified
        assertEquals(1, attendance.getAttendanceRecords().size());
    }

    @Test
    public void getAttendanceRecordsForMonth_attendanceSameMonth_returnsAllAttendance() {
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 16);
        LocalDate date3 = LocalDate.of(2024, 1, 17);
        LocalDate date4 = LocalDate.of(2024, 1, 18);

        attendance.markAttendance(date1, AttendanceStatus.PRESENT);
        attendance.markAttendance(date2, AttendanceStatus.LATE);
        attendance.markAttendance(date3, AttendanceStatus.SICK);
        attendance.markAttendance(date4, AttendanceStatus.ABSENT);

        YearMonth targetMonth = YearMonth.of(2024, 1);
        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecordsForMonth(targetMonth);

        assertEquals(4, records.size());
        assertTrue(records.containsKey(date2));
        assertTrue(records.containsKey(date3));

        assertEquals(AttendanceStatus.PRESENT, records.get(date1));
        assertEquals(AttendanceStatus.ABSENT, records.get(date4));
    }

    @Test
    public void getAttendanceRecordsForMonth_nonEmptyAttendance_returnsCopy() {
        LocalDate date1 = LocalDate.of(2024, 1, 15);

        attendance.markAttendance(date1, AttendanceStatus.PRESENT);

        YearMonth targetMonth = YearMonth.of(2024, 1);
        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecordsForMonth(targetMonth);
        records.put(LocalDate.of(2024, 1, 16), AttendanceStatus.LATE);

        // Original should not be modified
        assertEquals(1, attendance.getAttendanceRecords().size());
    }

    @Test
    public void getAttendanceRecordsForMonth_differentMonthAttendance_returnsEmpty() {
        LocalDate date1 = LocalDate.of(2024, 1, 15);

        attendance.markAttendance(date1, AttendanceStatus.PRESENT);

        YearMonth targetMonth = YearMonth.of(2024, 2);
        Map<LocalDate, AttendanceStatus> records = attendance.getAttendanceRecordsForMonth(targetMonth);

        assertTrue(records.isEmpty());
    }

    @Test
    public void formatAttendanceRecords_emptyAttendance_returnsNoRecordsMessage() {
        String expected = "No attendance records.";
        assertEquals(expected, attendance.formatAttendanceRecords());
    }


    @Test
    public void formatAttendanceRecords_nonEmptyAttendance_returnsFormattedString() {
        attendance.markAttendance(LocalDate.of(2025, 10, 21), AttendanceStatus.PRESENT);
        attendance.markAttendance(LocalDate.of(2025, 10, 22), AttendanceStatus.LATE);

        String formatted = attendance.formatAttendanceRecords();

        String expected =
                "21 Oct 2025 → present\n"
                        + "22 Oct 2025 → late";

        assertEquals(expected, formatted);
    }

    @Test
    public void formatAttendanceRecords_unsortedInput_isSortedByDate() {
        // given (add in reverse order)
        attendance.markAttendance(LocalDate.of(2025, 10, 22), AttendanceStatus.SICK);
        attendance.markAttendance(LocalDate.of(2025, 10, 21), AttendanceStatus.PRESENT);

        // when
        String formatted = attendance.formatAttendanceRecords();

        // then (should be sorted ascending by date)
        String expected =
                "21 Oct 2025 → present\n"
                        + "22 Oct 2025 → sick";

        assertEquals(expected, formatted);
    }

    @Test
    public void formatAttendanceRecordsForMonth_emptyAttendance_returnsNoRecordsMessage() {
        Attendance attendance = new Attendance();
        YearMonth targetMonth = YearMonth.of(2024, 1);

        String result = attendance.formatAttendanceRecordsForMonth(targetMonth);

        assertEquals("No attendance records.", result);
    }

    @Test
    public void formatAttendanceRecordsForMonth_recordsExistForTargetMonth_returnsFormattedRecords() {
        Attendance attendance = new Attendance();
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 10);
        YearMonth targetMonth = YearMonth.of(2024, 1);

        attendance.markAttendance(date1, AttendanceStatus.PRESENT);
        attendance.markAttendance(date2, AttendanceStatus.LATE);

        String result = attendance.formatAttendanceRecordsForMonth(targetMonth);

        String expected = "10 Jan 2024 → late\n"
                + "15 Jan 2024 → present";
        assertEquals(expected, result);
    }

    @Test
    public void formatAttendanceRecordsForMonth_mixedMonths_onlyShowsTargetMonth() {
        Attendance attendance = new Attendance();
        LocalDate janDate = LocalDate.of(2024, 1, 15);
        LocalDate febDate = LocalDate.of(2024, 2, 20);
        LocalDate marDate = LocalDate.of(2024, 3, 10);
        YearMonth targetMonth = YearMonth.of(2024, 2);

        attendance.markAttendance(janDate, AttendanceStatus.PRESENT);
        attendance.markAttendance(febDate, AttendanceStatus.LATE);
        attendance.markAttendance(marDate, AttendanceStatus.SICK);

        String result = attendance.formatAttendanceRecordsForMonth(targetMonth);

        // Should only show February record
        assertEquals("20 Feb 2024 → late", result);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(attendance.equals(attendance));
    }

    @Test
    public void equals_sameAttendance_returnsTrue() {
        Attendance otherAttendance = new Attendance();
        LocalDate date = LocalDate.of(2024, 1, 15);

        attendance.markAttendance(date, AttendanceStatus.PRESENT);
        otherAttendance.markAttendance(date, AttendanceStatus.PRESENT);

        assertTrue(attendance.equals(otherAttendance));
    }

    @Test
    public void equals_differentAttendance_returnsFalse() {
        Attendance otherAttendance = new Attendance();
        LocalDate date = LocalDate.of(2024, 1, 15);

        attendance.markAttendance(date, AttendanceStatus.PRESENT);
        otherAttendance.markAttendance(date, AttendanceStatus.LATE);

        assertFalse(attendance.equals(otherAttendance));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(attendance.equals("not an attendance"));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(attendance.equals(null));
    }

    @Test
    public void hashCode_sameAttendance_sameHashCode() {
        Attendance otherAttendance = new Attendance();
        LocalDate date = LocalDate.of(2024, 1, 15);

        attendance.markAttendance(date, AttendanceStatus.PRESENT);
        otherAttendance.markAttendance(date, AttendanceStatus.PRESENT);

        assertEquals(attendance.hashCode(), otherAttendance.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        attendance.markAttendance(date, AttendanceStatus.PRESENT);

        String expected = "{" + date + "=PRESENT}";
        assertEquals(expected, attendance.toString());
    }
}
