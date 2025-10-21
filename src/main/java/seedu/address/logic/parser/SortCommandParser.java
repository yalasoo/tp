package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIELD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;

import java.util.stream.Stream;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortField;
import seedu.address.logic.commands.SortCommand.SortOrder;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public SortCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_FIELD, PREFIX_ORDER);

        if (!arePrefixesPresent(argMultimap, PREFIX_FIELD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_FIELD, PREFIX_ORDER);

        String strField = argMultimap.getValue(PREFIX_FIELD).get();
        String strOrder = argMultimap.getValue(PREFIX_ORDER).orElse("asc");

        SortField field;
        SortOrder order;

        try {
            field = SortField.valueOf(strField.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid field. Valid fields: name, class, tag");
        }

        try {
            order = SortOrder.valueOf(strOrder.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid order. Use 'asc' or 'desc'");
        }

        return new SortCommand(field, order);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
