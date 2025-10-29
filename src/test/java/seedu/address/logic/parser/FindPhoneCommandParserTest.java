package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FindPhoneCommandParser.MESSAGE_CONSTRAINTS;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindPhoneCommand;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;

public class FindPhoneCommandParserTest {

    private FindPhoneCommandParser parser = new FindPhoneCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindPhoneCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "ab", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "@", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "+", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "?", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "-", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsFindPhoneCommand() {
        // no leading and trailing whitespaces
        FindPhoneCommand expectedFindPhoneCommand =
                new FindPhoneCommand(new PhoneContainsKeywordsPredicate(Arrays.asList("35", "8765")));
        assertParseSuccess(parser, "35 8765", expectedFindPhoneCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n 35 \n \t 8765  \t", expectedFindPhoneCommand);
    }

}

