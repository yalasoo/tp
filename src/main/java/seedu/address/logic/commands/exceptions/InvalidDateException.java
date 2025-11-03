package seedu.address.logic.commands.exceptions;

/**
 * Thrown when an invalid date is detected.
 */
public class InvalidDateException extends CommandException {
    public InvalidDateException(String message) {
        super(message);
    }
}
