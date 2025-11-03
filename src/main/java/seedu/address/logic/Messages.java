package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_SEE_UNFILTERED_CONTACTS = "\nUse list command to escape this filtered view "
            + "\nso that you can use the various commands on all contacts!" + "\nOtherwise you can continue using "
            + "find commands in this view for cumulative filtering.";
    public static final String MESSAGE_NO_MATCHES_FOUND = "No matches found. Please try again.";
    public static final String MESSAGE_INVALID_INDEX = "Invalid index. Please try again.";
    public static final String MESSAGE_DELETION_CANCELLED = "Deletion cancelled.";
    public static final String MESSAGE_DELETE_CONFIRMATION =
            "Are you sure you want to delete this contact (%s)?\nType INDEX and ENTER to confirm or ESC to cancel.";
    public static final String MESSAGE_POSSIBLE_MATCHES_FOUND =
            "Possible matches found below.\nType INDEX and ENTER to delete or ESC to cancel:";


    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Class: ")
                .append(person.getStudentClass())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
