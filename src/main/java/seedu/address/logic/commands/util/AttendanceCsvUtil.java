package seedu.address.logic.commands.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.model.Model;
import seedu.address.model.person.Class;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Formats attendance data into CSV file.
 */
public class AttendanceCsvUtil {

    /**
     * Generates a monthly attendance report based on the specified index(es)
     * and the specified month.
     *
     * @param model {@code Model} which the command should operate on.
     * @param indexes the index(es) to generate attendance for.
     * @param month the month to generate attendance for.
     * @return CSV string with all dates in the month as columns.
     */
    public static String generateStudentsMonthlyAttendanceReport(Model model, Set<Index> indexes, YearMonth month) {
        StringBuilder csv = new StringBuilder();

        String header = generateStudentMonthlyHeader(month);

        csv.append(header).append("\n");

        for (Index i : indexes) {
            Person person = model.getFilteredPersonList().get(i.getZeroBased());

            String row;

            if (person.getTags().contains(new Tag("student"))) {
                row = generateStudentMonthlyRow(person, month);
            } else {
                continue;
            }

            csv.append(row).append("\n");
        }

        return csv.toString();
    }

    /**
     * Generates a daily attendance report based on the specified class
     * and the specified date.
     *
     * @param model {@code Model} which the command should operate on.
     * @param studentClass the class to generate attendance for.
     * @param date the date to generate attendance for.
     * @return CSV string with students' attendance in rows.
     */
    public static String generateClassDailyAttendanceReport(Model model, Class studentClass, LocalDate date) {
        StringBuilder csv = new StringBuilder();

        csv.append("Class attendance on: ")
                .append(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .append("\n");

        String header = "Name,Status";

        csv.append(header).append("\n");

        ObservableList<Person> filteredStudent = model.getFilteredPersonList()
                .filtered(person -> person.getStudentClass().equals(studentClass));

        for (Person student : filteredStudent) {
            // TO BE REMOVED ONCE SAMPLE DATA HAS BEEN FIXED
            if (student.getTags().contains(new Tag("student"))) {
                csv.append(student.getName()).append(",");

                Map<LocalDate, AttendanceStatus> attendance = student.getAttendance().getAttendanceRecords();

                if (attendance.containsKey(date)) {
                    csv.append(attendance.get(date).toString());
                }
            } else {
                continue;
            }

            csv.append("\n");
        }

        return csv.toString();
    }

    /**
     * Generates a monthly attendance report based on the specified class
     * and the specified month.
     *
     * @param model {@code Model} which the command should operate on.
     * @param studentClass the class to generate data for.
     * @param month the month to generate attendance for.
     * @return CSV string with all dates in the month as columns.
    */
    public static String generateClassMonthlyAttendanceReport(Model model, Class studentClass, YearMonth month) {
        StringBuilder csv = new StringBuilder();

        String header = generateStudentMonthlyHeader(month);

        csv.append(header).append("\n");

        ObservableList<Person> filteredStudent = model.getFilteredPersonList()
                .filtered(person -> person.getStudentClass().equals(studentClass));


        for (Person student : filteredStudent) {
            String row;

            // TO BE REMOVED ONCE SAMPLE DATA HAS BEEN FIXED
            if (student.getTags().contains(new Tag("student"))) {
                row = generateStudentMonthlyRow(student, month);
            } else {
                continue;
            }

            csv.append(row).append("\n");
        }

        return csv.toString();
    }

    /**
     * Generates the header row for a monthly attendance report CSV.
     * Format: "Name,Class,01-10-2024,02-10-2024,...,31-10-2024".
     *
     * @param month the month to generate headers for.
     * @return CSV header string with all dates in the month as columns.
     */
    private static String generateStudentMonthlyHeader(YearMonth month) {
        LocalDate firstDay = month.atDay(1);
        LocalDate lastDay = month.atEndOfMonth();

        List<LocalDate> monthDates = firstDay.datesUntil(lastDay.plusDays(1)).toList();

        String header = "Name,Class,"
                + monthDates.stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .collect(Collectors.joining(","));

        return header;
    }

    /**
     * Generates a single student's monthly attendance data as a CSV row.
     * Format: "John Doe,K1A,PRESENT,,ABSENT,,LATE".
     * Empty cells represent dates with no attendance records.
     *
     * @param person the student to generate data for.
     * @param month the month to generate attendance for.
     * @return CSV row string with student's attendance data for the month.
     */
    private static String generateStudentMonthlyRow(Person person, YearMonth month) {
        StringBuilder row = new StringBuilder(person.getName() + "," + person.getStudentClass());
        Map<LocalDate, AttendanceStatus> attendance = person.getAttendance().getAttendanceRecords();

        LocalDate firstDay = month.atDay(1);
        LocalDate lastDay = month.atEndOfMonth();

        List<LocalDate> monthDates = firstDay.datesUntil(lastDay.plusDays(1)).toList();

        for (LocalDate date : monthDates) {
            row.append(",");
            if (attendance.containsKey(date)) {
                row.append(attendance.get(date).toString());
            }
            // else it will be an empty cell (comma)
        }

        return row.toString();
    }
}
