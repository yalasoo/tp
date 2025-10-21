package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AttendanceCommand object.
 */
public class AttendanceCommandParser implements Parser<AttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AttendanceCommand
     * and returns an AttendanceCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public AttendanceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STATUS, PREFIX_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_STATUS) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AttendanceCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STATUS, PREFIX_DATE);

        String strIndexes = argMultimap.getPreamble();
        String strStatus = argMultimap.getValue(PREFIX_STATUS).get();
        String strDate = argMultimap.getValue(PREFIX_DATE).orElse(LocalDate.now().toString());

        Set<Index> indexes = parseIndexes(strIndexes);

        if (strStatus == null || strStatus.trim().isEmpty()) {
            throw new ParseException("Status cannot be empty. Use: present, late, sick, absent");
        }

        AttendanceStatus status;
        try {
            status = AttendanceStatus.valueOf(strStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid status. Valid status: present, late, sick, absent");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date;
        try {
            date = LocalDate.parse(strDate, formatter);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date format. Please use yyyy-MM-dd.");
        }

        return new AttendanceCommand(indexes, date, status);
    }

    /**
     * Parses the given string index into a {@code Set<Index>}.
     *
     * @param strIndexes the given index in string format.
     * @return parsed index(es) in the form of {@code Set<Index>}.
     * @throws ParseException
     */
    private Set<Index> parseIndexes(String strIndexes) throws ParseException {
        Set<Index> indexes = new HashSet<>();
        String[] parts = strIndexes.split(",");

        for (String part : parts) {
            part = part.trim();

            if (part.contains("-")) {
                indexes.addAll(parseRange(part));
            } else {
                indexes.add(parseSingleIndex(part));
            }
        }

        return indexes;
    }

    /**
     * Parse the given range of index.
     *
     * @param range of index in the form of "startNum-endNum".
     * @return parsed indexes in the form of {@code Set<Index>}.
     * @throws ParseException
     */
    private Set<Index> parseRange(String range) throws ParseException {
        Set<Index> indexes = new HashSet<>();
        String[] bounds = range.split("-");

        if (bounds.length != 2 || bounds[0].isEmpty() || bounds[1].isEmpty()) {
            throw new ParseException("Invalid range format: " + range);
        }

        int start = Integer.parseInt(bounds[0].trim());
        int end = Integer.parseInt(bounds[1].trim());

        for (int i = start; i <= end; i++) {
            indexes.add(Index.fromOneBased(i));
        }

        return indexes;
    }

    /**
     * Parse the given string index.
     *
     * @param strIndex one index in string format.
     * @return Index object.
     * @throws ParseException
     */
    private Index parseSingleIndex(String strIndex) throws ParseException {
        try {
            int index = Integer.parseInt(strIndex.trim());
            return Index.fromOneBased(index);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid index: " + strIndex);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
