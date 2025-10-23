package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.ui.DeletePopupHandler;
import seedu.address.ui.PopupHandler;
import seedu.address.ui.TestDeletePopupHandler;
import seedu.address.ui.TestInfoPopupHandler;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private PopupHandler testInfoHandler;
    private DeletePopupHandler testDeleteHandler;
    private DeleteCommandParser parser;

    @BeforeEach
    public void setUp() {
        testInfoHandler = new TestInfoPopupHandler();
        testDeleteHandler = new TestDeletePopupHandler();
        parser = new DeleteCommandParser(testInfoHandler, testDeleteHandler);
    }

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        DeleteCommand expectedCommand =
                new DeleteCommand(INDEX_FIRST_PERSON, testInfoHandler, testDeleteHandler);
        assertParseSuccess(parser, "1", expectedCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validName_returnsDeleteCommand() {
        DeleteCommand expectedCommand =
                new DeleteCommand("Charlotte Oliveiro", testInfoHandler, testDeleteHandler);
        assertParseSuccess(parser, "n/Charlotte Oliveiro", expectedCommand);
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        assertParseFailure(parser, "n/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
