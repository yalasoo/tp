package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindTagCommand object
 */
public class FindTagCommandParser implements Parser<FindTagCommand> {

    public static final String MESSAGE_CONSTRAINTS =
            "Tags to find should only contain alphabetic characters"
                    + " and can be partial (case insensitive)"
                    + "\nExample: find-t stu oll ";

    /** The variable used to check against tags to ensure it contains only letters */
    public static final String VALIDATION_REGEX = "^[a-zA-Z]+$";

    /**
     * Parses the given {@code String} of arguments in the context of the FindTagCommand
     * and returns a FindTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTagCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        String[] tagKeywords = trimmedArgs.split("\\s+");

        for (String tag : tagKeywords) {
            if (!tag.matches(VALIDATION_REGEX)) {
                throw new ParseException(MESSAGE_CONSTRAINTS);
            }
        }

        return new FindTagCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)));
    }

}
