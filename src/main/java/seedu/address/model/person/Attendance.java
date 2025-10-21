package seedu.address.model.person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;

/**
 * Represents a Person's attendance in the address book.
 */
public class Attendance {
    public static final String MESSAGE_CONSTRAINTS = "Attendance should only be "
            + "present/late/sick/absent.";

    public final Map<LocalDate, AttendanceStatus> attendance;

    public Attendance() {
        this.attendance = new HashMap<>();
    }

    public void markAttendance(LocalDate date, AttendanceStatus status) {
        attendance.put(date, status);
    }

    public Map<LocalDate, AttendanceStatus> getAttendanceRecords() {
        return new HashMap<>(attendance);
    }

    /**
     * Attendance formatter to be displayed in the view window.
     * @return a formatted string of the attendance record.
     */
    public String formatAttendanceRecords() {
        if (attendance.isEmpty()) {
            return "No attendance records";
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
