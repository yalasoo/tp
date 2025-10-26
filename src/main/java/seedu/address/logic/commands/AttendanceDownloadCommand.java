package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
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
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
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
            + "student or class on a specific date or month. "
            + "Only applicable to contact with student tag.\n"
            + "Parameters: INDEX(es) (must be a positive integer) "
            + "[" + PREFIX_CLASS + "CLASS] "
            + "[" + PREFIX_DATE + "DATE] (dd-MM-yyyy) "
            + "[" + PREFIX_MONTH + "MONTH] (MM-yyyy)\n"
            + "Example: " + COMMAND_WORD + " 1-5,10,13 "
            + PREFIX_DATE + "12-12-2025";

    public static final String MESSAGE_SUCCESS = "Attendance report downloaded.";

    private static final Logger logger = LogsCenter.getLogger(AttendanceCommand.class);

    private final Set<Index> indexes;
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
    public AttendanceDownloadCommand(Set<Index> indexes, List<Class> studentClass, LocalDate date, YearMonth month,
                                     Boolean userProvideDate, Boolean userProvideMonth) {
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
    public CommandResult execute(Model model) {
        requireNonNull(model);

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
            return new CommandResult("Error saving attendance report: " + e.getMessage());
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
    private String downloadClassMonthlyAttendanceReport(Model model, String filePath) throws IOException {
        String fileName;
        for (Class studentClass : studentClass) {
            String classCsv = generateClassMonthlyAttendanceReport(model, studentClass, month);
            fileName = studentClass + "_"
                    + month.format(DateTimeFormatter.ofPattern("MM-yyyy"))
                    + ".csv";

            filePath = saveAttendanceCsv(classCsv, fileName);
        }
        return filePath;
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
    private String downloadClassDailyAttendanceReport(Model model, String filePath) throws IOException {
        String fileName;
        for (Class studentClass : studentClass) {
            String classCsv = generateClassDailyAttendanceReport(model, studentClass, date);
            fileName = studentClass + "_attendance_"
                    + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    + ".csv";

            filePath = saveAttendanceCsv(classCsv, fileName);
        }
        return filePath;
    }

    /**
     * Downloads a monthly attendance report for the specified student indexes.
     * Generates a single CSV file containing attendance data for all specified students.
     *
     * @param model The model containing the person data.
     * @return The file path of the saved student attendance report.
     * @throws IOException If an error occurs during file saving.
     */
    private String downloadStudentMonthlyAttendanceReport(Model model) throws IOException {
        String fileCsv = generateStudentsMonthlyAttendanceReport(model, indexes, month);
        String fileName = "student_attendance_"
                + month.format(DateTimeFormatter.ofPattern("MM-yyyy"))
                + ".csv";

        return saveAttendanceCsv(fileCsv, fileName);
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

        // Sort indexes before comparison
        List<Index> thisSortedIndexes = (this.indexes != null)
                ? this.indexes.stream()
                .sorted((a, b) -> Integer.compare(a.getZeroBased(), b.getZeroBased()))
                .toList()
                : null;

        List<Index> otherSortedIndexes = (otherAttendanceDownloadCommand.indexes != null)
                ? otherAttendanceDownloadCommand.indexes.stream()
                .sorted((a, b) -> Integer.compare(a.getZeroBased(), b.getZeroBased()))
                .toList()
                : null;

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

        return Objects.equals(thisSortedIndexes, otherSortedIndexes)
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
