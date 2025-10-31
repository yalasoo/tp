package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphabetic characters, spaces, hyphens, and apostrophes. "
            + "Names must contain at least 2 alphabetic characters, cannot consist only of punctuation, "
            + "and should not have consecutive punctuation (e.g., 'Mary-Jane' ✓, 'Mary--Jane' ✗).";

    /*
     * The name must contain only letters, spaces, hyphens, and apostrophes.
     * It should not be empty after trimming.
     */
    public static final String VALIDATION_REGEX = "^[\\p{L}\\s\\-']+$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String normalizedName = normalizeName(name);
        checkArgument(isValidName(normalizedName), MESSAGE_CONSTRAINTS);
        fullName = normalizedName;
    }

    /**
     * Normalizes the name by trimming spaces and collapsing multiple spaces into one.
     */
    private static String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        if (test == null) {
            throw new NullPointerException();
        }

        if (test.trim().isEmpty()) {
            return false;
        }

        String trimmed = test.trim();

        // Check basic character set
        if (!trimmed.matches(VALIDATION_REGEX)) {
            return false;
        }

        // Check for sufficient alphabetic content (at least 2 letters)
        long letterCount = trimmed.chars()
            .filter(Character::isLetter)
            .count();
        if (letterCount < 2) {
            return false;
        }

        // Check for excessive consecutive punctuation (more than 1 consecutive hyphens or apostrophes)
        if (trimmed.matches(".*[-']{2,}.*")) {
            return false;
        }

        // Check that name doesn't consist only of punctuation and spaces
        String withoutPunctuation = trimmed.replaceAll("[\\s\\-']+", "");
        if (withoutPunctuation.isEmpty()) {
            return false;
        }

        // Check for excessive punctuation - ratio of punctuation to letters should not exceed 1:1
        long punctuationCount = trimmed.chars()
            .filter(ch -> ch == '-' || ch == '\'')
            .count();
        if (punctuationCount >= letterCount) {
            return false;
        }

        return true;
    }

    /**
     * Returns the normalized name for case-insensitive comparison.
     */
    public String getNormalizedName() {
        return fullName.toLowerCase();
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return getNormalizedName().equals(otherName.getNormalizedName());
    }

    @Override
    public int hashCode() {
        return getNormalizedName().hashCode();
    }

}
