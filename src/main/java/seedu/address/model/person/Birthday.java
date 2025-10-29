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
        + "(i.e. birthday cannot be after today's date)";

    public static final String VALIDATION_REGEX = "^\\d{2}-\\d{2}-\\d{4}$";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);;

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
