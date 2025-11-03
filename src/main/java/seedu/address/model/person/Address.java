package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            "Addresses should contain only alphanumeric characters, spaces, and common punctuation "
            + "(comma, period, dash, hash, slash, parentheses). It should not be blank, must be at least "
            + "15 characters long, and should not contain symbols like @, *, $, !, ?, +, ;, etc.";

    /*
     * Address validation:
     * - Must not be blank or contain only whitespace
     * - Must be at least 15 characters long (after trimming)
     * - Can contain alphanumeric characters, spaces, and common address punctuation
     * - Common punctuation includes: , . - # / ( )
     * - Leading and trailing spaces are handled by trimming
     */
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9\\s,.\\-#/()]+$";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        String normalizedAddress = normalizeAddress(address);
        checkArgument(isValidAddress(normalizedAddress), MESSAGE_CONSTRAINTS);
        value = normalizedAddress;
    }

    /**
     * Normalizes the address by trimming and collapsing multiple spaces into single spaces.
     */
    private static String normalizeAddress(String address) {
        return address.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if a given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        if (test == null) {
            return false;
        }
        String trimmed = test.trim();
        return !trimmed.isEmpty() && trimmed.length() >= 15 && trimmed.matches(VALIDATION_REGEX);
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
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
