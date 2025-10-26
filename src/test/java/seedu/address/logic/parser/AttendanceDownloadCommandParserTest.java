package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceDownloadCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Class;

public class AttendanceDownloadCommandParserTest {

    private AttendanceDownloadCommandParser parser = new AttendanceDownloadCommandParser();

    @Test
    public void parse_invalidArgs_throwParseException() {
        // Empty input
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceDownloadCommand.MESSAGE_USAGE));

        // Only command word
        assertParseFailure(parser, AttendanceDownloadCommand.COMMAND_WORD,
                "Must specify either contact INDEX or c/CLASS to download attendance report.");

        // Only prefixes but no values
        assertParseFailure(parser, " d/15-10-2024 m/10-2024",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceDownloadCommand.MESSAGE_USAGE));

        // Both index and class provided
        assertParseFailure(parser, "1 c/K1A",
                "Cannot specify both INDEX and c/CLASS. Choose one.");

        // Both index and class provided
        assertParseFailure(parser, "1 d/29-12-2025",
                "Individual reports are monthly only. Use m/MM-yyyy instead of d/dd-MM-yyyy.");
    }

    @Test
    public void parse_validArgs_returnsAttendanceCommand() {
        // Single index
        assertParseSuccess(parser, "1",
                new AttendanceDownloadCommand(Set.of(Index.fromOneBased(1)), null,
                        LocalDate.now(), YearMonth.now(), false, false));

        // Multiple indexes
        assertParseSuccess(parser, "1,3,5",
                new AttendanceDownloadCommand(Set.of(Index.fromOneBased(1),
                        Index.fromOneBased(3), Index.fromOneBased(5)), null,
                        LocalDate.now(), YearMonth.now(), false, false));

        // Range indexes
        assertParseSuccess(parser, "1-3",
                new AttendanceDownloadCommand(Set.of(Index.fromOneBased(1),
                        Index.fromOneBased(2), Index.fromOneBased(3)), null,
                        LocalDate.now(), YearMonth.now(), false, false));

        // Mixed range and individual indexes
        assertParseSuccess(parser, "1-3,5,7",
                new AttendanceDownloadCommand(Set.of(Index.fromOneBased(1),
                        Index.fromOneBased(2), Index.fromOneBased(3),
                        Index.fromOneBased(5), Index.fromOneBased(7)), null,
                        LocalDate.now(), YearMonth.now(), false, false));

        // Mixed range and individual indexes with whitespaces
        assertParseSuccess(parser, "1    -   3,     5,     7   ",
                new AttendanceDownloadCommand(Set.of(Index.fromOneBased(1),
                        Index.fromOneBased(2), Index.fromOneBased(3),
                        Index.fromOneBased(5), Index.fromOneBased(7)), null,
                        LocalDate.now(), YearMonth.now(), false, false));

        // One classes
        assertParseSuccess(parser, " c/K1A",
                new AttendanceDownloadCommand(null, List.of(new Class("K1A")),
                        LocalDate.now(), YearMonth.now(), false, false));

        // Multiple classes
        assertParseSuccess(parser, " c/K1A c/K1B",
                new AttendanceDownloadCommand(null, List.of(new Class("K1A"),
                        new Class("K1B")), LocalDate.now(), YearMonth.now(),
                        false, false));

    }

    @Test
    public void parseDate_invalidUserProvidedDate_throwsParseException() {
        String invalidDateFormat = AttendanceDownloadCommand.COMMAND_WORD + "c/K1A d/2025-12-29";
        String invalidDate = AttendanceDownloadCommand.COMMAND_WORD + "c/K1A d/67-12-2025";
        String invalidMonthFormat = AttendanceDownloadCommand.COMMAND_WORD + "1 m/2025-12";
        String invalidMonth = AttendanceDownloadCommand.COMMAND_WORD + "1 m/23-2025";

        assertThrows(ParseException.class, () -> parser.parse(invalidDateFormat));
        assertThrows(ParseException.class, () -> parser.parse(invalidDate));
        assertThrows(ParseException.class, () -> parser.parse(invalidMonthFormat));
        assertThrows(ParseException.class, () -> parser.parse(invalidMonth));
    }

}
