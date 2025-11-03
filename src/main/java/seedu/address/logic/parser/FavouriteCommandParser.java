package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX_OR_MISSING_COMMAS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FavouriteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindNameCommand object
 */
public class FavouriteCommandParser implements Parser<FavouriteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FavouriteCommand
     * and returns a FavouriteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public FavouriteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavouriteCommand.MESSAGE_USAGE));
        }

        String[] indexStrKeywords = trimmedArgs.split("\\s*,\\s*");
        List<Index> indexKeywords = new java.util.ArrayList<>(List.of());
        for (String s: indexStrKeywords) {
            Index i;
            try {
                i = ParserUtil.parseIndex(s);
            } catch (ParseException e) {
                throw new ParseException(MESSAGE_INVALID_INDEX_OR_MISSING_COMMAS);
            }
            indexKeywords.add(i);
        }

        return new FavouriteCommand(indexKeywords);
    }

}

