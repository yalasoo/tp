package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsSortCommand() {
        // Test name ascending
        assertParseSuccess(parser, " f/name o/asc",
                new SortCommand(SortCommand.SortField.NAME, SortCommand.SortOrder.ASC));

        // Test class descending
        assertParseSuccess(parser, " f/class o/desc",
                new SortCommand(SortCommand.SortField.CLASS, SortCommand.SortOrder.DESC));

        // Test tag ascending (default order)
        assertParseSuccess(parser, " f/tag",
                new SortCommand(SortCommand.SortField.TAG, SortCommand.SortOrder.ASC));
    }

    @Test
    public void parse_invalidField_throwsParseException() {
        assertParseFailure(parser, " f/invalid o/asc",
                "Invalid field. Valid fields: name, class, tag");
    }

    @Test
    public void parse_invalidOrder_throwsParseException() {
        assertParseFailure(parser, " f/name o/invalid",
                "Invalid order. Use 'asc' or 'desc'");
    }

    @Test
    public void parse_missingField_throwsParseException() {
        assertParseFailure(parser, " o/asc", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE
        ));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE
        ));
    }
}
