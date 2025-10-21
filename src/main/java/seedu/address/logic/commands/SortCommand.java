package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sort contact list based on field and order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort the contact list based on FIELD and ORDER.\n"
            + "Parameters: f/FIELD (name / class / tag) "
            + "[o/ORDER] (asc / desc) default to ascending if omitted\n"
            + "Example: " + COMMAND_WORD + " f/name o/asc";

    public static final String MESSAGE_SORT_SUCCESS = "Sorted successfully";

    /**
     * Represents the fields that can be used for sorting contacts.
     */
    public enum SortField {
        NAME, CLASS, TAG
    }

    /**
     * Represents the sorting order direction.
     */
    public enum SortOrder {
        ASC, DESC
    }

    private final SortField field;
    private final SortOrder order;

    /**
     * Creates a SortCommand to sort the contact list
     * by the specified {@code field} and {@code order}.
     * @param field which field to sort by.
     * @param order what is the order to sort by.
     */
    public SortCommand(SortField field, SortOrder order) {
        requireNonNull(field);
        requireNonNull(order);

        this.field = field;
        this.order = order;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Comparator<Person> comparator = null;

        switch (field) {
        case NAME -> comparator = Comparator.comparing(p -> p.getName().toString());
        case CLASS -> comparator = Comparator.comparing(p -> p.getStudentClass().toString());
        case TAG -> comparator = Comparator.comparing(p -> p.getTags().toString());
        default -> throw new AssertionError("Unexpected sort field: " + field);
        }

        if (order == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        model.sortFilteredPersonList(comparator);
        return new CommandResult(MESSAGE_SORT_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls.
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return field.equals(otherSortCommand.field) && order.equals(otherSortCommand.order);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("field", field)
                .add("order", order)
                .toString();
    }
}
