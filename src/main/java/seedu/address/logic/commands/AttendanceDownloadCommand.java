package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.util.AttendanceCsvUtil.generateClassDailyAttendanceReport;
import static seedu.address.logic.commands.util.AttendanceCsvUtil.generateClassMonthlyAttendanceReport;
import static seedu.address.logic.commands.util.AttendanceCsvUtil.generateStudentsMonthlyAttendanceReport;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.storage.AttendanceCsvStorage.saveAttendanceCsv;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Class;

/**
 * Downloads attendance report of a specific student or class
 * on a specific date or month.
 * Only applicable to contact with "student" tag.
 */
public class AttendanceDownloadCommand extends Command {

    public static final String COMMAND_WORD = "attendanceD";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Download an attendance report of a specific "
            + "student or class on a specific date or month."
            + "\nParameters can be in one of these formats:\n"
            + "1. " + COMMAND_WORD + " INDEX(es) [m/MONTH]\n"
            + "2. " + COMMAND_WORD + " c/CLASS(es) [d/DATE]\n"
            + "3. " + COMMAND_WORD + " c/CLASS(es) [m/MONTH]\n"
            + "Examples:\n"
            + "• " + COMMAND_WORD + " 1-5,10,13 " + PREFIX_MONTH + "12-2025\n"
            + "• " + COMMAND_WORD + " " + PREFIX_CLASS + "K1A " + PREFIX_DATE + "12-12-2025\n"
            + "• " + COMMAND_WORD + " " + PREFIX_CLASS + "K1A " + PREFIX_CLASS + "K2B " + PREFIX_MONTH + "12-2025";

    public static final String MESSAGE_SUCCESS = "Attendance report(s) downloaded.";

    public static final LocalDate EARLIEST_DATE = LocalDate.of(1900, 1, 1);
    public static final YearMonth EARLIEST_MONTH = YearMonth.of(1900, 1);

    private static final Logger logger = LogsCenter.getLogger(AttendanceCommand.class);

    private final SortedSet<Index> indexes;
    private final List<Class> studentClass;
    private final LocalDate date;
    private final YearMonth month;
    private final Boolean userProvideDate;
    private final Boolean userProvideMonth;

    /**
     * Creates a AttendanceDownloadCommand to download
     * attendance report.
     *
     * @param indexes Which index(es) to download.
     * @param studentClass Which class(es) to download.
     * @param date Which date to download attendance report.
     * @param month Which month to download attendance report.
     */
    public AttendanceDownloadCommand(SortedSet<Index> indexes, List<Class> studentClass, LocalDate date,
                                     YearMonth month, Boolean userProvideDate, Boolean userProvideMonth) {
        requireNonNull(date);
        requireNonNull(month);

        this.indexes = indexes;
        this.studentClass = studentClass;
        this.date = date;
        this.month = month;
        this.userProvideDate = userProvideDate;
        this.userProvideMonth = userProvideMonth;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getFilteredPersonList().isEmpty()) {
            throw new CommandException("No contacts available to download attendance.");
        }

        if (userProvideDate) {
            checkValidDate();
        } else if (userProvideMonth) {
            checkValidMonth();
        }

        try {
            String filePath = "";

            if (indexes != null) {
                filePath = downloadStudentMonthlyAttendanceReport(model);
            } else {
                // Class attendance report default to monthly
                if (userProvideDate) {
                    filePath = downloadClassDailyAttendanceReport(model, filePath);
                } else {
                    filePath = downloadClassMonthlyAttendanceReport(model, filePath);
                }
            }

            return new CommandResult(MESSAGE_SUCCESS + " Saved to:\n" + filePath);
        } catch (IOException e) {
            logger.severe("Error saving attendance report: " + e.getMessage());
            throw new CommandException("Error saving attendance report: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            logger.warning("Index out of bound: " + e.getMessage());
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    }

    /**
     * Checks whether user's month is within 01-1900 to current month.
     *
     * @throws CommandException If an error occurs during command execution.
     */
    private void checkValidMonth() throws CommandException {
        YearMonth thisMonth = YearMonth.now();
        String thisMonthStr = thisMonth.format(DateTimeFormatter.ofPattern("MM-yyyy"));

        if (month.isBefore(EARLIEST_MONTH) || month.isAfter(YearMonth.now())) {
            logger.severe("Month provided before 01-01-1900 or after " + thisMonthStr + ".");
            throw new CommandException("Month must be within 01-1900 until " + thisMonthStr + ".");
        }
    }

    /**
     * Checks whether user's date is within 01-01-1900 to current date.
     *
     * @throws CommandException If an error occurs during command execution.
     */
    private void checkValidDate() throws CommandException {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if (date.isBefore(EARLIEST_DATE) || date.isAfter(today)) {
            logger.severe("Date provided before 01-01-1900 or after " + todayStr + ".");
            throw new CommandException("Date must be within 01-01-1900 until " + todayStr + ".");
        }
    }

    /**
     * Downloads monthly attendance reports for all specified classes.
     * Generates separate CSV files for each class and returns the path
     * of the last saved file.
     *
     * @param model The model containing the person data.
     * @param filePath The file path for saving the reports.
     * @return The file path of the last saved class attendance report.
     * @throws IOException If an error occurs during file saving.
     */
    private String downloadClassMonthlyAttendanceReport(Model model, String filePath)
            throws IOException, CommandException {
        String fileName;
        for (Class studentClass : studentClass) {
            String classCsv = generateClassMonthlyAttendanceReport(model, studentClass, month);
            fileName = studentClass + "_attendance_"
                    + month.format(DateTimeFormatter.ofPattern("MM-yyyy"))
                    + ".csv";

            filePath = classCsv.isEmpty() ? "" : saveAttendanceCsv(classCsv, fileName);
        }

        if (filePath.isEmpty()) {
            throw new CommandException("No attendance report downloaded. Class(es) provided has no students.");
        } else {
            return filePath;
        }
    }

    /**
     * Downloads daily attendance reports for all specified classes.
     * Generates separate CSV files for each class and returns the path
     * of the last saved file.
     *
     * @param model The model containing the person data.
     * @param filePath The file path for saving the reports.
     * @return The file path of the last saved class attendance report.
     * @throws IOException If an error occurs during file saving.
     */
    private String downloadClassDailyAttendanceReport(Model model, String filePath)
            throws IOException, CommandException {
        String fileName;
        for (Class studentClass : studentClass) {
            String classCsv = generateClassDailyAttendanceReport(model, studentClass, date);
            fileName = studentClass + "_attendance_"
                    + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    + ".csv";

            filePath = classCsv.isEmpty() ? "" : saveAttendanceCsv(classCsv, fileName);
        }

        if (filePath.isEmpty()) {
            throw new CommandException("No attendance report downloaded. Class(es) provided has no students.");
        } else {
            return filePath;
        }
    }

    /**
     * Downloads a monthly attendance report for the specified student indexes.
     * Generates a single CSV file containing attendance data for all specified students.
     *
     * @param model The model containing the person data.
     * @return The file path of the saved student attendance report.
     * @throws IOException If an error occurs during file saving.
     */
    private String downloadStudentMonthlyAttendanceReport(Model model)
            throws IOException, IndexOutOfBoundsException, CommandException {
        String fileCsv = generateStudentsMonthlyAttendanceReport(model, indexes, month);
        String fileName = "student_attendance_"
                + month.format(DateTimeFormatter.ofPattern("MM-yyyy"))
                + ".csv";

        if (fileCsv.isEmpty()) {
            throw new CommandException("No attendance report downloaded. No student in the index specified.");
        } else {
            return saveAttendanceCsv(fileCsv, fileName);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceDownloadCommand)) {
            return false;
        }

        AttendanceDownloadCommand otherAttendanceDownloadCommand = (AttendanceDownloadCommand) other;

        // Sort classes by their string value before comparison
        List<Class> thisSortedClass = (this.studentClass != null)
                ? this.studentClass.stream()
                .sorted(Comparator.comparing(Class::toString))
                .toList()
                : null;

        List<Class> otherSortedClass = (otherAttendanceDownloadCommand.studentClass != null)
                ? otherAttendanceDownloadCommand.studentClass.stream()
                .sorted(Comparator.comparing(Class::toString))
                .toList()
                : null;

        return Objects.equals(indexes, otherAttendanceDownloadCommand.indexes)
                && Objects.equals(thisSortedClass, otherSortedClass)
                && date.equals(otherAttendanceDownloadCommand.date)
                && month.equals(otherAttendanceDownloadCommand.month)
                && userProvideDate.equals(otherAttendanceDownloadCommand.userProvideDate)
                && userProvideMonth.equals(otherAttendanceDownloadCommand.userProvideMonth);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("indexes", indexes)
                .add("classes", studentClass)
                .add("date", date)
                .add("month", month)
                .add("userProvideDate", userProvideDate)
                .add("userProvideMonth", userProvideMonth)
                .toString();
    }
}
