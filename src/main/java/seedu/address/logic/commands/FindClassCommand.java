package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ClassContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose class contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindClassCommand extends Command {

    public static final String COMMAND_WORD = "find-c";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose classes contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: CLASS(es) (can be partial alphanumeric and hyphen characters)\n"
            + "Example: " + COMMAND_WORD + " K1A urse ";

    private final ClassContainsKeywordsPredicate predicate;

    public FindClassCommand(ClassContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW + Messages.MESSAGE_SEE_UNFILTERED_CONTACTS,
                        model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindClassCommand)) {
            return false;
        }

        FindClassCommand otherFindClassCommand = (FindClassCommand) other;
        return predicate.equals(otherFindClassCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
