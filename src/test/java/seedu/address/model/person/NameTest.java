package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void constructor_nameNormalization_success() {
        // Multiple spaces should be collapsed to single space
        Name name = new Name("John    Doe");
        assertEquals("John Doe", name.fullName);

        // Leading and trailing spaces should be trimmed
        name = new Name("  Mary Jane  ");
        assertEquals("Mary Jane", name.fullName);

        // Mixed spacing issues should be handled
        name = new Name("  Peter   Paul   Mary  ");
        assertEquals("Peter Paul Mary", name.fullName);
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid names
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("   ")); // multiple spaces only
        assertFalse(Name.isValidName("John123")); // contains numbers
        assertFalse(Name.isValidName("John@Doe")); // contains invalid symbols
        assertFalse(Name.isValidName("John*")); // contains asterisk
        assertFalse(Name.isValidName("John&Doe")); // contains ampersand
        assertFalse(Name.isValidName("John#Doe")); // contains hash
        assertFalse(Name.isValidName("John$Doe")); // contains dollar sign
        assertFalse(Name.isValidName("John%Doe")); // contains percent
        assertFalse(Name.isValidName("John(Doe)")); // contains parentheses
        assertFalse(Name.isValidName("John+Doe")); // contains plus
        assertFalse(Name.isValidName("John=Doe")); // contains equals

        // valid names - alphabetic characters only
        assertTrue(Name.isValidName("John")); // single word
        assertTrue(Name.isValidName("John Doe")); // two words
        assertTrue(Name.isValidName("Mary Jane Smith")); // three words

        // single characters are now invalid (need at least 2 letters)
        assertFalse(Name.isValidName("a")); // single character
        assertFalse(Name.isValidName("Z")); // single uppercase character

        // valid names - with hyphens
        assertTrue(Name.isValidName("Mary-Jane")); // hyphenated first name
        assertTrue(Name.isValidName("Smith-Jones")); // hyphenated last name
        assertTrue(Name.isValidName("Mary-Jane Smith-Jones")); // multiple hyphens
        assertTrue(Name.isValidName("Jean-Claude Van Damme")); // mixed hyphens and spaces

        // valid names - with apostrophes
        assertTrue(Name.isValidName("O'Connor")); // name with apostrophe
        assertTrue(Name.isValidName("D'Angelo")); // name with apostrophe
        assertTrue(Name.isValidName("Mary O'Brien")); // first and last name with apostrophe
        assertTrue(Name.isValidName("Jean-Luc D'Artagnan")); // mixed hyphens and apostrophes

        // valid names - mixed case
        assertTrue(Name.isValidName("john")); // all lowercase
        assertTrue(Name.isValidName("JOHN")); // all uppercase
        assertTrue(Name.isValidName("John")); // proper case
        assertTrue(Name.isValidName("mCdonald")); // mixed case

        // valid names - complex combinations
        assertTrue(Name.isValidName("Mary-Jane O'Connor Smith")); // all special characters
        assertTrue(Name.isValidName("Jean-Baptiste D'Artagnan")); // French-style name
        assertTrue(Name.isValidName("Anna-Maria José-Carlos")); // Spanish-style name
    }

    @Test
    public void isValidName_problematicPunctuation_invalid() {
        // Names consisting only of punctuation should be invalid
        assertFalse(Name.isValidName("-"));
        assertFalse(Name.isValidName("'"));
        assertFalse(Name.isValidName("--"));
        assertFalse(Name.isValidName("''"));
        assertFalse(Name.isValidName("-'"));
        assertFalse(Name.isValidName("'-"));
        assertFalse(Name.isValidName("---"));
        assertFalse(Name.isValidName("'''"));
        assertFalse(Name.isValidName("----"));
        assertFalse(Name.isValidName("''''"));
        assertFalse(Name.isValidName("-'-'-'"));
        assertFalse(Name.isValidName("'-'-'-"));

        // Names with only punctuation and spaces should be invalid
        assertFalse(Name.isValidName(" - "));
        assertFalse(Name.isValidName(" ' "));
        assertFalse(Name.isValidName(" -- "));
        assertFalse(Name.isValidName(" '' "));
        assertFalse(Name.isValidName(" - ' - ' "));

        // Names with insufficient alphabetic content (less than 2 letters) should be invalid
        assertFalse(Name.isValidName("a")); // Only 1 letter
        assertFalse(Name.isValidName("a-")); // 1 letter + punctuation
        assertFalse(Name.isValidName("a'")); // 1 letter + punctuation
        assertFalse(Name.isValidName("-a")); // punctuation + 1 letter
        assertFalse(Name.isValidName("'a")); // punctuation + 1 letter
        assertFalse(Name.isValidName("-a-")); // punctuation + 1 letter + punctuation
        assertFalse(Name.isValidName("'a'")); // punctuation + 1 letter + punctuation

        // Names with consecutive punctuation (2+ consecutive) should be invalid
        assertFalse(Name.isValidName("abc--def")); // 2 consecutive hyphens
        assertFalse(Name.isValidName("abc''def")); // 2 consecutive apostrophes
        assertFalse(Name.isValidName("abc---def")); // 3 consecutive hyphens
        assertFalse(Name.isValidName("abc'''def")); // 3 consecutive apostrophes
        assertFalse(Name.isValidName("--abc")); // leading double punctuation
        assertFalse(Name.isValidName("abc--")); // trailing double punctuation
        assertFalse(Name.isValidName("---abc")); // leading excessive punctuation
        assertFalse(Name.isValidName("abc---")); // trailing excessive punctuation
        assertFalse(Name.isValidName("''abc''")); // double punctuation on both ends
        assertFalse(Name.isValidName("'''abc'''")); // excessive punctuation on both ends
        assertFalse(Name.isValidName("abc----def")); // 4 consecutive hyphens
        assertFalse(Name.isValidName("abc''''def")); // 4 consecutive apostrophes
        assertFalse(Name.isValidName("a-'-'-'-'-'-'-'-'b")); // excessive mixed punctuation

        // Names with predominantly punctuation should be invalid
        assertFalse(Name.isValidName("ab-'-'-'-'-'-'-'-'")); // too much punctuation
        assertFalse(Name.isValidName("a'b'c'd'e'f'g'h'i'j'k'l'm'n'o'p'q'r's't'u'v'w'x'y'z'")); // excessive apostrophes
    }

    @Test
    public void isValidName_acceptablePunctuation_valid() {
        // Now only single punctuation is allowed
        assertTrue(Name.isValidName("Mary-Jane")); // single hyphen
        assertTrue(Name.isValidName("O'Connor")); // single apostrophe
        assertTrue(Name.isValidName("Jean-Luc D'Artagnan")); // realistic complex name

        // Names with minimum 2 letters should be valid
        assertTrue(Name.isValidName("Jo")); // exactly 2 letters
        assertTrue(Name.isValidName("Li")); // exactly 2 letters
        assertTrue(Name.isValidName("Wu")); // exactly 2 letters
        assertTrue(Name.isValidName("Al-Mahmoud")); // 2+ letters with punctuation

        // Realistic names should remain valid
        assertTrue(Name.isValidName("Mary-Jane Smith"));
        assertTrue(Name.isValidName("Jean-Claude Van Damme"));
        assertTrue(Name.isValidName("Anna-Maria José-Carlos"));
        assertTrue(Name.isValidName("O'Brien-McConnell"));

        // Double punctuation should now be invalid
        assertFalse(Name.isValidName("Smith--Jones")); // double hyphen now invalid
        assertFalse(Name.isValidName("O''Connor")); // double apostrophe now invalid
    }

    @Test
    public void getNormalizedName_caseInsensitive_success() {
        Name name1 = new Name("John Doe");
        Name name2 = new Name("JOHN DOE");
        Name name3 = new Name("john doe");
        Name name4 = new Name("John DOE");

        // All variations should have the same normalized name
        assertEquals("john doe", name1.getNormalizedName());
        assertEquals("john doe", name2.getNormalizedName());
        assertEquals("john doe", name3.getNormalizedName());
        assertEquals("john doe", name4.getNormalizedName());

        // Test with special characters
        Name name5 = new Name("Mary-Jane O'Connor");
        Name name6 = new Name("MARY-JANE O'CONNOR");
        assertEquals("mary-jane o'connor", name5.getNormalizedName());
        assertEquals("mary-jane o'connor", name6.getNormalizedName());
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different type -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));

        // Note: equals uses case-insensitive comparison
        assertTrue(name.equals(new Name("valid name"))); // same name, different case
        assertTrue(name.equals(new Name("Valid  Name"))); // different spacing gets normalized
    }

    @Test
    public void toString_returnsFullName() {
        Name name = new Name("John Doe");
        assertEquals("John Doe", name.toString());

        // Test with normalized input
        Name nameWithExtraSpaces = new Name("  John   Doe  ");
        assertEquals("John Doe", nameWithExtraSpaces.toString());

        // Test with special characters
        Name nameWithSpecialChars = new Name("Mary-Jane O'Connor");
        assertEquals("Mary-Jane O'Connor", nameWithSpecialChars.toString());
    }

    @Test
    public void hashCode_consistency() {
        Name name1 = new Name("John Doe");
        Name name2 = new Name("John Doe");
        Name name3 = new Name("Jane Doe");

        // Same names should have same hash code
        assertEquals(name1.hashCode(), name2.hashCode());

        // Different names may have different hash codes (not guaranteed, but likely)
        // We just test that hashCode() doesn't throw exceptions
        name3.hashCode();
    }
}
