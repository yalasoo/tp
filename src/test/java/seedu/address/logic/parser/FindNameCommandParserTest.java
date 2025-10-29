package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FindNameCommandParser.MESSAGE_CONSTRAINTS;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindNameCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindNameCommandParserTest {

    private FindNameCommandParser parser = new FindNameCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindNameCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "ani 1", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "Bob @", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "+", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "?", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsFindNameCommand() {
        // no leading and trailing whitespaces
        FindNameCommand expectedFindNameCommand =
                new FindNameCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindNameCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindNameCommand);
    }

    @Test
    public void parse_validPartialArgs_returnsFindNameCommand() {
        //partial names (lowercase)
        FindNameCommand expectedFindNameCommand =
                new FindNameCommand(new NameContainsKeywordsPredicate(Arrays.asList("ani", "ion")));
        assertParseSuccess(parser, "ani ion", expectedFindNameCommand);
    }
}
