package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClassContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindClassCommand object
 */
public class FindClassCommandParser implements Parser<FindClassCommand> {

    public static final String MESSAGE_CONSTRAINTS =
            "Classes to find should only contain alphanumeric characters and hyphens"
                    + " and can be partial (case insensitive)"
                    + "\nExample: find-c K1A K2B K1B ";

    /** The variable used to check against class to ensure it contains only letters, numerals and hyphens */
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9-]+$";

    /**
     * Parses the given {@code String} of arguments in the context of the FindClassCommand
     * and returns a FindClassCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindClassCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClassCommand.MESSAGE_USAGE));
        }

        String[] classKeywords = trimmedArgs.split("\\s+");

        for (String className : classKeywords) {
            if (!className.matches(VALIDATION_REGEX)) {
                throw new ParseException(MESSAGE_CONSTRAINTS);
            }
        }

        return new FindClassCommand(new ClassContainsKeywordsPredicate(Arrays.asList(classKeywords)));
    }

}
