package seedu.address.model.person;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;

/**
 * Represents a Person's attendance in the address book.
 */
public class Attendance {
    public static final String MESSAGE_CONSTRAINTS = "Attendance should only be "
            + "present/late/sick/absent.";

    public final Map<LocalDate, AttendanceStatus> attendance;

    /**
     * Constructs an empty {@code Attendance} object.
     */
    public Attendance() {
        this.attendance = new HashMap<>();
    }

    /**
     * Marks attendance for a specific date with the given status.
     * If attendance already exists for the date, it will be updated.
     *
     * @param date date the date of attendance
     * @param status date the date of attendance
     */
    public void markAttendance(LocalDate date, AttendanceStatus status) {
        attendance.put(date, status);
    }

    /**
     * Returns a copy of all attendance records.
     * The returned map contains dates mapped to their corresponding attendance status.
     *
     * @return a copy of the attendance records map
     */
    public Map<LocalDate, AttendanceStatus> getAttendanceRecords() {
        return new HashMap<>(attendance);
    }

    /**
     * Returns the attendance records within the specified month.
     *
     * @param targetMonth specified attendance month.
     * @return attendance records map at the specified month.
     */
    public Map<LocalDate, AttendanceStatus> getAttendanceRecordsForMonth(YearMonth targetMonth) {
        return attendance.entrySet()
                .stream()
                .filter(entry -> YearMonth.from(entry.getKey()).equals(targetMonth))
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue()
                ));
    }

    /**
     * Returns true if there are no attendance records.
     */
    public boolean isEmpty() {
        return attendance.isEmpty();
    }

    /**
     * Returns the number of attendance records.
     */
    public int size() {
        return attendance.size();
    }

    /**
     * Attendance formatter to be displayed in the view window.
     * @return a formatted string of the attendance record.
     */
    public String formatAttendanceRecords() {
        if (attendance.isEmpty()) {
            return "No attendance records.";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        StringBuilder formattedAttendanceRecord = new StringBuilder();

        attendance.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String formattedDate = entry.getKey().format(formatter);
                    formattedAttendanceRecord.append(formattedDate)
                            .append(" → ")
                            .append(entry.getValue().toString().toLowerCase())
                            .append("\n");
                });

        return formattedAttendanceRecord.toString().trim();
    }

    /**
     * Attendance formatter for a given month to be displayed in the view window.
     * @param targetMonth attendance month to be viewed.
     * @return a formatted string of the attendance record for the specified month.
     */
    public String formatAttendanceRecordsForMonth(YearMonth targetMonth) {
        if (attendance.isEmpty()) {
            return "No attendance records.";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        StringBuilder formattedAttendanceRecord = new StringBuilder();

        attendance.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(entry -> YearMonth.from(entry.getKey()).equals(targetMonth))
                .forEach(entry -> {
                    String formattedDate = entry.getKey().format(formatter);
                    formattedAttendanceRecord.append(formattedDate)
                            .append(" → ")
                            .append(entry.getValue().toString().toLowerCase())
                            .append("\n");
                });

        String result = formattedAttendanceRecord.toString().trim();
        return result.isEmpty() ? "No attendance records for "
                + targetMonth.format(DateTimeFormatter.ofPattern("MMM yyyy")) : result;
    }

    @Override
    public String toString() {
        return attendance.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Attendance)) {
            return false;
        }

        Attendance otherAttendance = (Attendance) other;
        return attendance.equals(otherAttendance.attendance);
    }

    @Override
    public int hashCode() {
        return attendance.hashCode();
    }

}
