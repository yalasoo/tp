package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.FindClassCommandParser.MESSAGE_CONSTRAINTS;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindClassCommand;
import seedu.address.model.person.ClassContainsKeywordsPredicate;

public class FindClassCommandParserTest {

    private FindClassCommandParser parser = new FindClassCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindClassCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "k 1 - @", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "+", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "/", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsFindClassCommand() {
        // no leading and trailing whitespaces
        FindClassCommand expectedFindClassCommand =
                new FindClassCommand(new ClassContainsKeywordsPredicate(Arrays.asList("K2A", "Nursery")));
        assertParseSuccess(parser, "K2A Nursery", expectedFindClassCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n K2A \n \t Nursery  \t", expectedFindClassCommand);
    }

    @Test
    public void parse_validPartialArgs_returnsFindClassCommand() {
        //partial names (lowercase)
        FindClassCommand expectedFindClassCommand =
                new FindClassCommand(new ClassContainsKeywordsPredicate(Arrays.asList("urse", "1A", "-", "b")));
        assertParseSuccess(parser, "urse 1A - b", expectedFindClassCommand);
    }
}
