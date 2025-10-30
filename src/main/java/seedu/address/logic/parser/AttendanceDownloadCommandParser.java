package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.logic.parser.util.IndexParser.parseIndexes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceDownloadCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Class;

/**
 * Parses input arguments and creates a new AttendanceDownloadCommand object.
 */
public class AttendanceDownloadCommandParser implements Parser<AttendanceDownloadCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AttendanceDownloadCommand
     * and returns an AttendanceDownloadCommand object for execution.
     *
     * @throws ParseException If the user input does not conform the expected format.
     */
    @Override
    public AttendanceDownloadCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASS, PREFIX_DATE, PREFIX_MONTH);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASS) && argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, AttendanceDownloadCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE, PREFIX_MONTH);

        boolean hasIndex = !argMultimap.getPreamble().isEmpty()
                && !argMultimap.getPreamble().equals(AttendanceDownloadCommand.COMMAND_WORD);
        boolean hasClass = !argMultimap.getAllValues(PREFIX_CLASS).isEmpty();
        boolean userProvidedDate = argMultimap.getValue(PREFIX_DATE).isPresent();
        boolean userProvidedMonth = argMultimap.getValue(PREFIX_MONTH).isPresent();

        validateParameterCombinations(hasIndex, hasClass, userProvidedDate, userProvidedMonth);

        Set<Index> indexes = null;
        List<Class> studentClasses = null;

        if (hasIndex) {
            indexes = parseIndexes(argMultimap.getPreamble());
        } else {
            studentClasses = new ArrayList<>();
            for (String classStr : argMultimap.getAllValues(PREFIX_CLASS)) {
                studentClasses.add(ParserUtil.parseClass(classStr));
            }
        }

        LocalDate date = parseDate(argMultimap.getValue(PREFIX_DATE), userProvidedDate);
        YearMonth month = parseMonth(argMultimap.getValue(PREFIX_MONTH), userProvidedMonth);

        return new AttendanceDownloadCommand(indexes, studentClasses, date, month, userProvidedDate, userProvidedMonth);
    }

    /**
     * Validates the parameter combinations for the attendance download command.
     *
     * @param hasIndex Whether index(es) are provided in the preamble.
     * @param hasClass Whether class prefix(es) are provided.
     * @param userProvidedDate Whether a specific date was provided by the user.
     * @param userProvidedMonth Whether a specific month was provided by the user.
     * @throws ParseException If the parameter combinations are invalid.
     */
    private void validateParameterCombinations(boolean hasIndex,
                                               boolean hasClass,
                                               boolean userProvidedDate,
                                               boolean userProvidedMonth) throws ParseException {
        if (!hasIndex && !hasClass) {
            throw new ParseException("Must specify either contact INDEX or c/CLASS to download attendance report.");
        }
        if (hasIndex && hasClass) {
            throw new ParseException("Cannot specify both INDEX and c/CLASS. Choose one.");
        }
        if (hasIndex && userProvidedDate && !userProvidedMonth) {
            throw new ParseException("Individual reports are monthly only. Use m/MM-yyyy instead of d/dd-MM-yyyy.");
        }
    }

    /**
     * Parses a date string into a {@code LocalDate} object.
     * If no date is provided, defaults to today's date.
     *
     * @param dateOpt The optional date string provided by the user.
     * @param userProvided Whether the date was explicitly provided by the user.
     * @return The parsed {@code LocalDate} object.
     * @throws ParseException If the date format is invalid when explicitly provided.
     */
    private LocalDate parseDate(Optional<String> dateOpt, boolean userProvided) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        String strDate = dateOpt.orElse(LocalDate.now().format(formatter));

        if (userProvided) {
            try {
                return LocalDate.parse(strDate, formatter);
            } catch (DateTimeParseException e) {
                throw new ParseException("Invalid date format/input. Please use dd-MM-yyyy (e.g. 29-12-2025).");
            }
        }
        return LocalDate.parse(strDate, formatter);
    }

    /**
     * Parses a month string into a {@code YearMonth} object.
     * If no month is provided, defaults to the current month.
     *
     * @param monthOpt The optional month string provided by the user.
     * @param userProvided Whether the month was explicitly provided by the user.
     * @return The parsed {@code YearMonth} object.
     * @throws ParseException If the month format is invalid when explicitly provided.
     */
    private YearMonth parseMonth(Optional<String> monthOpt, boolean userProvided) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-uuuu").withResolverStyle(ResolverStyle.STRICT);
        String strMonth = monthOpt.orElse(YearMonth.now().format(formatter));

        if (userProvided) {
            try {
                return YearMonth.parse(strMonth, formatter);
            } catch (DateTimeParseException e) {
                throw new ParseException("Invalid month format/input. Please use MM-yyyy (e.g. 12-2025).");
            }
        }
        return YearMonth.parse(strMonth, formatter);
    }


    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
