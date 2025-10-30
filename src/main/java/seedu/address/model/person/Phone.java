package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers must be exactly 8 digits starting with 6 (landline), 8 or 9 (mobile). "
            + "Examples: 61234567, 81234567, 91234567. Spaces and dashes are allowed but ignored.";

    public static final String VALIDATION_REGEX = "^[689]\\d{7}$";

    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        String normalizedPhone = normalizePhone(phone);
        checkArgument(isValidPhone(normalizedPhone), MESSAGE_CONSTRAINTS);
        value = normalizedPhone;
    }

    /**
     * Normalizes the phone number by removing spaces and dashes.
     */
    private static String normalizePhone(String phone) {
        return phone.replaceAll("[\\s-]", "");
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        String normalized = normalizePhone(test);
        return normalized.matches(VALIDATION_REGEX);
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
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
