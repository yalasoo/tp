package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.DeletePopupHandler;
import seedu.address.ui.InfoPopupHandler;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private final InfoPopupHandler infoPopupHandler;
    private final DeletePopupHandler deletePopupHandler;

    /**
     * Constructs a {@code DeleteCommandParser} with the specified popup handlers.
     */
    public DeleteCommandParser(InfoPopupHandler infoPopupHandler, DeletePopupHandler deletePopupHandler) {
        this.infoPopupHandler = infoPopupHandler;
        this.deletePopupHandler = deletePopupHandler;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DeleteCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);

        //delete n/Name
        String trimmedArgs = args.trim();
        if (trimmedArgs.startsWith("n/")) {
            String name = trimmedArgs.substring(2).trim();
            if (name.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            return new DeleteCommand(name, infoPopupHandler, deletePopupHandler);
        }

        //delete Index
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteCommand(index, infoPopupHandler, deletePopupHandler);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
