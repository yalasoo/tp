package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;

public class AttendanceCommandParserTest {

    private AttendanceCommandParser parser = new AttendanceCommandParser();

    @Test
    public void parse_validArgs_returnsAttendanceCommand() throws Exception {
        // Single index with present status
        assertParseSuccess(parser, "1 s/present",
                new AttendanceCommand(Set.of(Index.fromOneBased(1)), LocalDate.now(), AttendanceStatus.PRESENT));

        // Multiple indexes with absent status
        assertParseSuccess(parser, "1,3,5 s/absent",
                new AttendanceCommand(Set.of(Index.fromOneBased(1), Index.fromOneBased(3), Index.fromOneBased(5)),
                        LocalDate.now(), AttendanceStatus.ABSENT));

        // Range with specific date
        assertParseSuccess(parser, "1-3 s/late d/23-10-2024",
                new AttendanceCommand(Set.of(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3)),
                        LocalDate.of(2024, 10, 23), AttendanceStatus.LATE));

        // Mixed range and individual indexes
        assertParseSuccess(parser, "1-3,5,7 s/sick",
                new AttendanceCommand(Set.of(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3),
                        Index.fromOneBased(5), Index.fromOneBased(7)),
                        LocalDate.now(), AttendanceStatus.SICK));

        // Mixed range and individual indexes with whitespaces
        assertParseSuccess(parser, "1     -     3,5    ,   7 s/     sick     ",
                new AttendanceCommand(Set.of(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3),
                        Index.fromOneBased(5), Index.fromOneBased(7)),
                        LocalDate.now(), AttendanceStatus.SICK));

        // Case insensitive status
        assertParseSuccess(parser, "1 s/PRESENT",
                new AttendanceCommand(Set.of(Index.fromOneBased(1)), LocalDate.now(), AttendanceStatus.PRESENT));
    }

    @Test
    public void parse_missingStatus_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyStatus_throwsParseException() {
        assertParseFailure(parser, "1 s/", "Status cannot be empty. Use: present, late, sick, absent");
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        assertParseFailure(parser, "1 s/invalid", "Invalid status. Valid status: present, late, sick, absent");
    }

    @Test
    public void parse_missingIndexes_throwsParseException() {
        assertParseFailure(parser, "s/present",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "abc s/present", "Invalid index: abc");
    }

    @Test
    public void parse_invalidRange_throwsParseException() {
        assertParseFailure(parser, "1- s/present", "Invalid range format: 1-");
        assertParseFailure(parser, "1-2-3 s/present", "Invalid range format: 1-2-3");
        assertParseFailure(parser, "-3 s/present", "Invalid range format: -3");
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser, "1 s/present d/invalid",
                "Invalid date format. Please use dd-MM-yyyy (e.g. 29-12-2025).");
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, "1 s/present s/absent",
                "Multiple values specified for the following single-valued field(s): s/");
    }
}
