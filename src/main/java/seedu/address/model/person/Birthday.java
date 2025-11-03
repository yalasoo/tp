package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents a Person's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBirthday(String)}.
 */
public class Birthday {

    public static final String MESSAGE_CONSTRAINTS =
            "Birthday should be in the format dd-MM-yyyy (e.g., 24-12-2005) and must be a valid date.\n"
        + "Birthday given must be between 01-01-1900 and today's date.";

    public static final String VALIDATION_REGEX = "^\\d{2}-\\d{2}-\\d{4}$";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public static final LocalDate MIN_VALID_DATE = LocalDate.of(1900, 1, 1);
    public static final LocalDate MAX_VALID_DATE = LocalDate.now();

    public final String value;
    public final LocalDate date;

    /**
     * Constructs a {@code Birthday}.
     *
     * @param birthday A valid birthday.
     */
    public Birthday(String birthday) {
        requireNonNull(birthday);
        String trimmedBirthday = birthday.trim();
        checkArgument(isValidBirthday(trimmedBirthday), MESSAGE_CONSTRAINTS);
        value = trimmedBirthday;
        this.date = LocalDate.parse(trimmedBirthday, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid birthday.
     */
    public static boolean isValidBirthday(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(test, DATE_FORMATTER);

            // Check if the date is within valid range (1900 to today)
            if (parsedDate.isBefore(MIN_VALID_DATE) || parsedDate.isAfter(MAX_VALID_DATE)) {
                return false;
            }

            String formatted = parsedDate.format(DATE_FORMATTER);
            return formatted.equals(test);

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks whether incoming date is earlier than birthday date.
     *
     * @param otherDate The other date to be compared to.
     * @return True if other date is earlier than birthday, else false.
     */
    public boolean isBeforeBirthday(LocalDate otherDate) {
        int result = otherDate.compareTo(date);

        if (result < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the age based on year difference only (as per requirement).
     * For example: born in 2024, in 2025 would be 1 year old.
     *
     * @return The age calculated by year difference.
     */
    int calculateAgeByYear() {
        return LocalDate.now().getYear() - date.getYear();
    }

    /**
     * Checks if this birthday belongs to a student.
     * Students are defined as being 3, 4, 5, or 6 years old (by year difference).
     *
     * @return true if the age is between 3 and 6 years old (inclusive), false otherwise.
     */
    public boolean isStudentBirthday() {
        int age = calculateAgeByYear();
        return age >= 3 && age <= 6;
    }

    /**
     * Checks if this birthday belongs to a colleague.
     * Colleagues are defined as being 18 years or older (by year difference).
     *
     * @return true if the age is 18 or older, false otherwise.
     */
    public boolean isColleagueBirthday() {
        int age = calculateAgeByYear();
        return age >= 18;
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Birthday)) {
            return false;
        }

        Birthday otherBirthday = (Birthday) other;
        return value.equals(otherBirthday.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
