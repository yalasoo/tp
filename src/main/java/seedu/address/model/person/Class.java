package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's class in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClass(String)}
 */
public class Class {

    public static final String MESSAGE_CONSTRAINTS =
            "Class names should only contain alphanumeric characters (letters and numbers), "
            + "with no spaces, and must be between 1 and 20 characters long.";

    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9]{1,20}$";

    public final String value;

    /**
     * Constructs a {@code Class}.
     *
     * @param studentClass A valid class.
     */
    public Class(String studentClass) {
        requireNonNull(studentClass);
        String trimmedClass = studentClass.trim();
        checkArgument(isValidClass(trimmedClass), MESSAGE_CONSTRAINTS);
        value = trimmedClass;
    }

    /**
     * Returns true if a given string is a valid class.
     */
    public static boolean isValidClass(String test) {
        return test.trim().matches(VALIDATION_REGEX);
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
        if (!(other instanceof Class)) {
            return false;
        }

        Class otherClass = (Class) other;
        return value.equals(otherClass.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
