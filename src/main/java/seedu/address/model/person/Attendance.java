package seedu.address.model.person;

import java.time.LocalDate;
import java.util.HashMap;
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
