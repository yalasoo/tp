package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void constructor_phoneNormalization_success() {
        // Spaces should be removed
        Phone phone = new Phone("9876 5432");
        assertEquals("98765432", phone.value);

        // Dashes should be removed
        phone = new Phone("9876-5432");
        assertEquals("98765432", phone.value);

        // Mixed spaces and dashes should be removed
        phone = new Phone("98 76-54 32");
        assertEquals("98765432", phone.value);

        // Multiple spaces should be handled
        phone = new Phone("9876    5432");
        assertEquals("98765432", phone.value);
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers - wrong length
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("1234567")); // 7 digits (too short)
        assertFalse(Phone.isValidPhone("123456789")); // 9 digits (too long)

        // invalid phone numbers - wrong starting digit
        assertFalse(Phone.isValidPhone("12345678")); // starts with 1
        assertFalse(Phone.isValidPhone("23456789")); // starts with 2
        assertFalse(Phone.isValidPhone("34567890")); // starts with 3
        assertFalse(Phone.isValidPhone("45678901")); // starts with 4
        assertFalse(Phone.isValidPhone("56789012")); // starts with 5
        assertFalse(Phone.isValidPhone("78901234")); // starts with 7
        assertFalse(Phone.isValidPhone("01234567")); // starts with 0

        // invalid phone numbers - contains non-numeric characters
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("901@1234")); // special characters
        assertFalse(Phone.isValidPhone("9011#234")); // hash symbol
        assertFalse(Phone.isValidPhone("9011*234")); // asterisk
        assertFalse(Phone.isValidPhone("9011+234")); // plus sign

        // valid phone numbers - 8 digits starting with 8
        assertTrue(Phone.isValidPhone("81234567"));
        assertTrue(Phone.isValidPhone("87654321"));
        assertTrue(Phone.isValidPhone("80000000"));
        assertTrue(Phone.isValidPhone("89999999"));

        // valid phone numbers - 8 digits starting with 9
        assertTrue(Phone.isValidPhone("91234567"));
        assertTrue(Phone.isValidPhone("97654321"));
        assertTrue(Phone.isValidPhone("90000000"));
        assertTrue(Phone.isValidPhone("99999999"));

        // valid phone numbers - 8 digits starting with 6 (landline)
        assertTrue(Phone.isValidPhone("61234567"));
        assertTrue(Phone.isValidPhone("67654321"));
        assertTrue(Phone.isValidPhone("60000000"));
        assertTrue(Phone.isValidPhone("69999999"));

        // valid phone numbers with normalization (spaces and dashes ignored)
        assertTrue(Phone.isValidPhone("9876 5432")); // with space
        assertTrue(Phone.isValidPhone("9876-5432")); // with dash
        assertTrue(Phone.isValidPhone("98 76 54 32")); // multiple spaces
        assertTrue(Phone.isValidPhone("98-76-54-32")); // multiple dashes
        assertTrue(Phone.isValidPhone("98 76-54 32")); // mixed spaces and dashes
        assertTrue(Phone.isValidPhone("8123    4567")); // multiple spaces
        assertTrue(Phone.isValidPhone("8123----4567")); // multiple dashes

        // edge cases with normalization
        assertTrue(Phone.isValidPhone(" 81234567 ")); // leading/trailing spaces
        assertTrue(Phone.isValidPhone("-81234567-")); // leading/trailing dashes
        assertTrue(Phone.isValidPhone(" 8123 4567 ")); // combination
    }

    @Test
    public void equals() {
        Phone phone = new Phone("98765432");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("98765432")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different type -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("91234567")));

        // normalized values should be equal
        assertTrue(phone.equals(new Phone("9876 5432"))); // with spaces
        assertTrue(phone.equals(new Phone("9876-5432"))); // with dashes
        assertTrue(phone.equals(new Phone("98 76-54 32"))); // mixed
    }

    @Test
    public void toString_returnsNormalizedValue() {
        Phone phone = new Phone("98765432");
        assertEquals("98765432", phone.toString());

        // Test with spaces - should be normalized
        Phone phoneWithSpaces = new Phone("9876 5432");
        assertEquals("98765432", phoneWithSpaces.toString());

        // Test with dashes - should be normalized
        Phone phoneWithDashes = new Phone("9876-5432");
        assertEquals("98765432", phoneWithDashes.toString());

        // Test with mixed formatting - should be normalized
        Phone phoneWithMixed = new Phone("98 76-54 32");
        assertEquals("98765432", phoneWithMixed.toString());
    }

    @Test
    public void hashCode_consistency() {
        Phone phone1 = new Phone("98765432");
        Phone phone2 = new Phone("98765432");
        Phone phone3 = new Phone("91234567");

        // Same phones should have same hash code
        assertEquals(phone1.hashCode(), phone2.hashCode());

        // Normalized phones should have same hash code
        Phone phoneWithSpaces = new Phone("9876 5432");
        assertEquals(phone1.hashCode(), phoneWithSpaces.hashCode());

        // Different phones may have different hash codes
        // We just test that hashCode() doesn't throw exceptions
        phone3.hashCode();
    }
}
