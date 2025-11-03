package seedu.address.logic.commands.exceptions;

/**
 * Represents an error for contact with missing attendance record.
 */
public class NoAttendanceRecordException extends CommandException {
    public NoAttendanceRecordException(String message) {
        super(message);
    }
}
