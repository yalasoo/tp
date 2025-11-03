package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.InvalidDateException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks the attendance of the specified person in the address book.
 * Only applicable to contact with "student" tag.
 */
public class AttendanceCommand extends Command {

    public static final String COMMAND_WORD = "attendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the attendance of the specified INDEX(es) "
            + "with the specified STATUS. "
            + "Only applicable to contact with student tag.\n"
            + "Parameters: INDEX(es) (must be a positive integer) "
            + PREFIX_STATUS + "STATUS (present/late/sick/absent/remove) "
            + "[" + PREFIX_DATE + "DATE] (dd-MM-yyyy) (Must be between student's born date and today's date)\n"
            + "Example: " + COMMAND_WORD + " 1-5,10,13 "
            + PREFIX_STATUS + "present "
            + PREFIX_DATE + "29-12-2025";

    public static final String MESSAGE_SUCCESS = "Modified %d out of %d contacts as %s on %s.";

    private static final Logger logger = LogsCenter.getLogger(AttendanceCommand.class);

    /**
     * Represents the status that can be used for marking attendance.
     */
    public enum AttendanceStatus {
        PRESENT, LATE, SICK, ABSENT, REMOVE
    }

    private final Set<Index> indexes;
    private final LocalDate date;
    private final AttendanceStatus status;

    private final StringBuilder studentsModified = new StringBuilder();
    private final StringBuilder contactsNotModified = new StringBuilder();

    /**
     * Creates a AttendanceCommand to mark attendance of the
     * specified indexes.
     *
     * @param indexes Which index(es) to be mark.
     * @param date When does this marking apply.
     * @param status What is the status of the attendance.
     */
    public AttendanceCommand(Set<Index> indexes, LocalDate date, AttendanceStatus status) {
        requireNonNull(indexes);
        requireNonNull(date);
        requireNonNull(status);

        this.indexes = new TreeSet<>(Comparator.comparingInt(Index::getOneBased));
        this.indexes.addAll(indexes);
        this.date = date;
        this.status = status;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Early termination when list is empty
        if (lastShownList.isEmpty()) {
            throw new CommandException("No contacts available to mark attendance.");
        }

        int totalModified = markAll(lastShownList);

        return getCommandResult(totalModified);
    }

    /**
     * Returns a CommandResult object based on the number of marked students and
     * display the details of both marked and unmarked contacts.
     *
     * @param totalModified The number of students successfully marked.
     * @return A CommandResult object.
     * @throws CommandException If an error occurs during command execution.
     */
    private CommandResult getCommandResult(int totalModified) throws CommandException {
        logger.info("Successfully modified attendance for " + totalModified + " students");
        String dateMsg = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if (!studentsModified.isEmpty()) {
            studentsModified.insert(0, "\n\nStudents with updated attendance:");
        }

        if (!contactsNotModified.isEmpty()) {
            contactsNotModified.insert(0, "\n\nBelow are the unmodified contacts:");
        }

        return new CommandResult(String.format(
                MESSAGE_SUCCESS, totalModified, indexes.size(), status, dateMsg)
                + studentsModified.append(contactsNotModified));
    }

    /**
     * Marks all specified indexes (if they are a student) and returns the
     * number of contacts actually marked.
     *
     * @return The total number of marked contacts that is a student.
     * @throws CommandException If an error occurs during command execution.
     */
    private int markAll(List<Person> lastShownList) throws CommandException {
        int totalModified = 0;

        for (Index i : indexes) {
            int zeroBasedIndex = i.getZeroBased();

            // Check for invalid index (negative or out of range)
            if (zeroBasedIndex < 0 || zeroBasedIndex >= lastShownList.size()) {
                logger.warning("Invalid index provided: " + i.getOneBased());
                throw new CommandException(String.format("%s: %d",
                        MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, i.getOneBased()));
            }

            Person personToEdit = lastShownList.get(zeroBasedIndex);

            // Ensure person is a student
            if (personToEdit.isStudent()) {
                logger.fine("Marking attendance for " + personToEdit.getName() + " on " + date + " as " + status);

                try {
                    boolean isDuplicate = false;
                    if (status.equals(AttendanceStatus.REMOVE)) {
                        personToEdit.unmarkAttendance(date);
                    } else {
                        isDuplicate = !personToEdit.markAttendance(date, status);
                    }

                    if (isDuplicate) {
                        logger.warning("Duplicate attendance: " + i.getOneBased());
                        contactsNotModified.append("\n").append(i.getOneBased()).append(". ")
                                .append(personToEdit.getName()).append(" [Status unchanged - same as previous record]");
                        continue;
                    }

                    studentsModified.append("\n").append(i.getOneBased()).append(". ").append(personToEdit.getName());
                    totalModified++;
                } catch (InvalidDateException e) {
                    logger.warning("Invalid date for attendance: " + i.getOneBased());
                    contactsNotModified.append("\n").append(i.getOneBased()).append(". ").append(personToEdit.getName())
                            .append(" [Date before birthday or today]");
                }
            } else {
                logger.warning("Contact is not a student: " + i.getOneBased());
                contactsNotModified.append("\n").append(i.getOneBased()).append(". ").append(personToEdit.getName())
                        .append(" [Not a student]");
            }
        }
        return totalModified;
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
