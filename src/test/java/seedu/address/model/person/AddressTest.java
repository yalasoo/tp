package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_emptyAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void constructor_validAddress_success() {
        // Valid addresses
        Address address = new Address("123 Main Street");
        assertEquals("123 Main Street", address.value);

        address = new Address("Blk 456, Den Road, #01-355");
        assertEquals("Blk 456, Den Road, #01-355", address.value);

        // Address with leading/trailing spaces should be trimmed
        address = new Address("  123 Main Street  ");
        assertEquals("123 Main Street", address.value);

        // Multiple spaces should be normalized to single spaces
        address = new Address("123   Main    Street");
        assertEquals("123 Main Street", address.value);
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        // Invalid addresses with problematic symbols
        assertThrows(IllegalArgumentException.class, () -> new Address("Leng Inc; 1234 Market St")); // semicolon
        assertThrows(IllegalArgumentException.class, () -> new Address("123 Main St @ Center")); // @ symbol
        assertThrows(IllegalArgumentException.class, () -> new Address("123*456 Main Street")); // asterisk
        assertThrows(IllegalArgumentException.class, () -> new Address("Unit $123 Main St")); // dollar sign
        assertThrows(IllegalArgumentException.class, () -> new Address("123 Main St!")); // exclamation
        assertThrows(IllegalArgumentException.class, () -> new Address("123 Main St?")); // question mark
        assertThrows(IllegalArgumentException.class, () -> new Address("123+456 Main Road")); // plus sign
    }

    @Test
    public void constructor_spaceNormalization_success() {
        // Test various space normalization scenarios

        // Leading and trailing spaces
        Address address = new Address("  123 Main Street  ");
        assertEquals("123 Main Street", address.value);

        // Multiple spaces between words
        address = new Address("123    Main     Street");
        assertEquals("123 Main Street", address.value);

        // Mixed tabs and spaces (tabs are treated as whitespace)
        address = new Address("123\t  Main\t\tStreet");
        assertEquals("123 Main Street", address.value);

        // Complex case with multiple types of spacing issues
        address = new Address("  Blk   456,   Den    Road,   #01-355  ");
        assertEquals("Blk 456, Den Road, #01-355", address.value);

        // Single character addresses with spaces
        address = new Address("  a  ");
        assertEquals("a", address.value);

        // Address with newlines (should be normalized to spaces)
        address = new Address("123\nMain\nStreet");
        assertEquals("123 Main Street", address.value);
    }

    @Test
    public void isValidAddress() {
        // null address
        assertFalse(Address.isValidAddress(null));

        // blank address
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only
        assertFalse(Address.isValidAddress("  ")); // multiple spaces only

        // valid addresses (spaces are handled by normalization)
        assertTrue(Address.isValidAddress(" 123 Main Street ")); // leading/trailing spaces
        assertTrue(Address.isValidAddress("123 Main Street"));
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddress("a")); // minimal valid address
        assertTrue(Address.isValidAddress("123")); // numbers only
        assertTrue(Address.isValidAddress("Block 123 Ang Mo Kio Avenue 3, #12-34"));
        assertTrue(Address.isValidAddress("311, Clementi Ave 2, #02-25"));
        assertTrue(Address.isValidAddress("Singapore 123456"));
        assertTrue(Address.isValidAddress("Unit #01-01, 123 Main Street, Singapore 654321"));

        // addresses with allowed special characters
        assertTrue(Address.isValidAddress("123-456 Main Street")); // dash
        assertTrue(Address.isValidAddress("123/456 Main Road")); // slash
        assertTrue(Address.isValidAddress("Flat 4B, Block 123, Main Street (West)")); // parentheses
        assertTrue(Address.isValidAddress("123 Main St, Apt #4B")); // comma, hash
        assertTrue(Address.isValidAddress("P.O. Box 123")); // period (allowed)
        assertTrue(Address.isValidAddress("123 Main St.")); // period at end

        // invalid addresses with prohibited symbols
        assertFalse(Address.isValidAddress("Leng Inc; 1234 Market St")); // semicolon
        assertFalse(Address.isValidAddress("123 Main St @ Center")); // @ symbol
        assertFalse(Address.isValidAddress("123*456 Main Street")); // asterisk
        assertFalse(Address.isValidAddress("Unit $123 Main St")); // dollar sign
        assertFalse(Address.isValidAddress("123 Main St!")); // exclamation
        assertFalse(Address.isValidAddress("123 Main St?")); // question mark
        assertFalse(Address.isValidAddress("123+456 Main Road")); // plus sign
    }

    @Test
    public void isValidAddress_spaceHandling() {
        // Valid addresses with various spacing issues (handled by normalization)
        assertTrue(Address.isValidAddress("  123 Main Street  "));
        assertTrue(Address.isValidAddress("123    Main     Street"));
        assertTrue(Address.isValidAddress("\t123\tMain\tStreet\t"));

        // Invalid: only whitespace (even after normalization)
        assertFalse(Address.isValidAddress("   "));
        assertFalse(Address.isValidAddress("\t\t\t"));
        assertFalse(Address.isValidAddress("\n\n"));
        assertFalse(Address.isValidAddress("  \t  \n  "));
    }

    @Test
    public void equals() {
        Address address = new Address("123 Main Street");

        // same values -> returns true
        assertTrue(address.equals(new Address("123 Main Street")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("456 Other Street")));
    }

    @Test
    public void hashCode_sameAddress_sameHashCode() {
        Address address1 = new Address("123 Main Street");
        Address address2 = new Address("123 Main Street");

        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    public void hashCode_differentAddress_differentHashCode() {
        Address address1 = new Address("123 Main Street");
        Address address2 = new Address("456 Other Street");

        // While not guaranteed, different addresses should typically have different hash codes
        // This is a probabilistic test - hash codes could theoretically collide
        assertTrue(address1.hashCode() != address2.hashCode());
    }

    @Test
    public void toString_validAddress_returnsAddress() {
        Address address = new Address("123 Main Street");
        assertEquals("123 Main Street", address.toString());

        address = new Address("Blk 456, Den Road, #01-355");
        assertEquals("Blk 456, Den Road, #01-355", address.toString());
    }
}
