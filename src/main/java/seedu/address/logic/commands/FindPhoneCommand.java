package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose phone number contains any of the argument keywords.
 * Keyword matching works on partial numbers.
 */
public class FindPhoneCommand extends Command {

    public static final String COMMAND_WORD = "find-p";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose phone number contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: PHONE(s) (can be partial numeric characters only)\n"
            + "Example: " + COMMAND_WORD + " 84123 6781";

    private final PhoneContainsKeywordsPredicate predicate;

    public FindPhoneCommand(PhoneContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListCumulative(predicate);
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
        if (!(other instanceof FindPhoneCommand)) {
            return false;
        }

        FindPhoneCommand otherFindPhoneCommand = (FindPhoneCommand) other;
        return predicate.equals(otherFindPhoneCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
