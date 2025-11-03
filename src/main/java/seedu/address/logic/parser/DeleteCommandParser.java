package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
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
            if (!Name.isValidName(name)) {
                throw new ParseException(Name.MESSAGE_CONSTRAINTS);
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
}
