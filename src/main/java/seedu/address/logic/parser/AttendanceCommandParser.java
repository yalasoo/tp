package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.util.IndexParser;

/**
 * Parses input arguments and creates a new AttendanceCommand object.
 */
public class AttendanceCommandParser implements Parser<AttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AttendanceCommand
     * and returns an AttendanceCommand object for execution.
     *
     * @throws ParseException If the user input does not conform the expected format.
     */
    @Override
    public AttendanceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STATUS, PREFIX_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_STATUS) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STATUS, PREFIX_DATE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String strIndexes = argMultimap.getPreamble();
        String strStatus = argMultimap.getValue(PREFIX_STATUS).get();
        String strDate = argMultimap.getValue(PREFIX_DATE).orElse(LocalDate.now().format(formatter));

        Set<Index> indexes = IndexParser.parseIndexes(strIndexes);

        if (strStatus.trim().isEmpty()) {
            throw new ParseException("Status cannot be empty. Use: present, late, sick, absent");
        }

        AttendanceStatus status;
        try {
            status = AttendanceStatus.valueOf(strStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid status. Valid status: present, late, sick, absent");
        }


        LocalDate date;
        try {
            date = LocalDate.parse(strDate, formatter);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date format. Please use dd-MM-yyyy (e.g. 29-12-2025).");
        }

        return new AttendanceCommand(indexes, date, status);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
