package seedu.address.testutil;

import java.time.LocalDate;

/**
 * Utility class for generating test dates that are always valid relative to current year.
 */
public class TestDateUtil {

    /**
     * Returns a birthday string for a student of the given age (based on year calculation).
     * Age is calculated as: current year - birth year
     */
    public static String getStudentBirthdayForAge(int age) {
        int currentYear = LocalDate.now().getYear();
        int birthYear = currentYear - age;
        return String.format("15-03-%04d", birthYear); // Always use 15th March for consistency
    }

    /**
     * Returns a birthday string for a colleague of the given age (based on year calculation).
     */
    public static String getColleagueBirthdayForAge(int age) {
        int currentYear = LocalDate.now().getYear();
        int birthYear = currentYear - age;
        return String.format("15-03-%04d", birthYear);
    }

    // Convenience methods for common cases
    public static String getValidStudentBirthday() {
        return getStudentBirthdayForAge(4); // 4 years old
    }

    public static String getValidColleagueBirthday() {
        return getColleagueBirthdayForAge(25); // 25 years old
    }

    /**
     * Returns a birthday string for a student at minimum valid age (3 years old).
     */
    public static String getMinStudentBirthday() {
        return getStudentBirthdayForAge(3);
    }

    /**
     * Returns a birthday string for a student at maximum valid age (6 years old).
     */
    public static String getMaxStudentBirthday() {
        return getStudentBirthdayForAge(6);
    }

    /**
     * Returns a birthday string for a colleague at minimum valid age (18 years old).
     */
    public static String getMinColleagueBirthday() {
        return getColleagueBirthdayForAge(18);
    }

    /**
     * Returns a birthday string that is too young for a student (2 years old).
     */
    public static String getTooYoungStudentBirthday() {
        return getStudentBirthdayForAge(2);
    }

    /**
     * Returns a birthday string that is too old for a student (7 years old).
     */
    public static String getTooOldStudentBirthday() {
        return getStudentBirthdayForAge(7);
    }

    /**
     * Returns a birthday string that is too young for a colleague (17 years old).
     */
    public static String getTooYoungColleagueBirthday() {
        return getColleagueBirthdayForAge(17);
    }
}
