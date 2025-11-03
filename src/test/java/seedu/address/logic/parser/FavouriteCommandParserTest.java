package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX_OR_MISSING_COMMAS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FavouriteCommand;

public class FavouriteCommandParserTest {

    private FavouriteCommandParser parser = new FavouriteCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FavouriteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_allInvalidArgs_throwsParseException() {
        assertParseFailure(parser, "-1, 0", String.format(MESSAGE_INVALID_INDEX_OR_MISSING_COMMAS));
    }

    @Test
    public void parse_someInvalidArgs_throwsParseException() {
        assertParseFailure(parser, "0,1,2", String.format(MESSAGE_INVALID_INDEX_OR_MISSING_COMMAS));
    }

    @Test
    public void parse_validArgs_returnsFavouriteCommand() {
        String userInput = "1, 2";

        List<Index> indexes = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        FavouriteCommand expectedCommand = new FavouriteCommand(indexes);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
