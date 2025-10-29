package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FindTagCommandParser.MESSAGE_CONSTRAINTS;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindTagCommandParserTest {

    private FindTagCommandParser parser = new FindTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "coll 1", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "@", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "+", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "?", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsFindTagCommand() {
        // no leading and trailing whitespaces
        FindTagCommand expectedFindTagCommand =
                new FindTagCommand(new TagContainsKeywordsPredicate(Arrays.asList("student", "coll")));
        assertParseSuccess(parser, "student coll", expectedFindTagCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n student \n \t coll  \t", expectedFindTagCommand);
    }

}
