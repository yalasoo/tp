package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks the attendance of the specified person in the address book.
 * Only applicable to those with "student" tag.
 */
public class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "attendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the attendance of the specified INDEX(es) "
            + "with the specified STATUS. "
            + "Only applicable to contact with student tag.\n"
            + "Parameters: INDEX(es) (must be a positive integer) "
            + "s/STATUS (present/late/sick/absent) "
            + "[d/DATE] (yyyy-MM-dd)\n"
            + "Example: " + COMMAND_WORD + " 1-5,10,13 "
            + "s/present d/2025-12-12";

    public static final String MESSAGE_SUCCESS = "Attendance marked.";

    private static final Logger logger = LogsCenter.getLogger(AttendanceCommand.class);

    /**
     * Represents the status that can be used for marking attendance.
     */
    public enum AttendanceStatus {
        PRESENT, LATE, SICK, ABSENT, UNRECORDED
    }

    private final Set<Index> indexes;
    private final LocalDate date;
    private final AttendanceStatus status;

    /**
     * Creates a AttendanceCommand to mark attendance of the
     * specified indexes.
     *
     * @param indexes which index(es) to be mark.
     * @param date when does this marking apply.
     * @param status what is the status of the attendance.
     */
    public AttendanceCommand(Set<Index> indexes, LocalDate date, AttendanceStatus status) {
        requireNonNull(indexes);
        requireNonNull(date);
        requireNonNull(status);

        this.indexes = indexes;
        this.date = date;
        this.status = status;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Defensive check: list cannot be empty
        if (lastShownList.isEmpty()) {
            throw new CommandException("No contacts available to mark attendance.");
        }

        for (Index i : indexes) {
            int zeroBasedIndex = i.getZeroBased();

            // Check for invalid index (negative or out of range)
            if (zeroBasedIndex < 0 || zeroBasedIndex >= lastShownList.size()) {
                logger.warning("Invalid index provided: " + i.getOneBased());
                throw new CommandException(String.format("%s: %d",
                        MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, i.getOneBased()));
            }

            Person personToEdit = lastShownList.get(zeroBasedIndex);
            logger.fine("Marking attendance for " + personToEdit.getName() + " on " + date + " as " + status);

            personToEdit.markAttendance(date, status);
        }

        logger.info("Successfully marked attendance for " + indexes.size() + " students");
        return new CommandResult(MESSAGE_SUCCESS);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceCommand)) {
            return false;
        }

        AttendanceCommand otherAttendanceCommand = (AttendanceCommand) other;

        // Sort indexes before comparison
        List<Index> thisSortedIndexes = this.indexes.stream()
                .sorted((a, b) -> Integer.compare(a.getZeroBased(), b.getZeroBased()))
                .toList();
        List<Index> otherSortedIndexes = otherAttendanceCommand.indexes.stream()
                .sorted((a, b) -> Integer.compare(a.getZeroBased(), b.getZeroBased()))
                .toList();

        return thisSortedIndexes.equals(otherSortedIndexes)
                && date.equals(otherAttendanceCommand.date)
                && status.equals(otherAttendanceCommand.status);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("indexes", indexes)
                .add("date", date)
                .add("status", status)
                .toString();
    }
}
