package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void constructor_emailNormalization_success() {
        // Mixed case should be normalized to lowercase
        Email email = new Email("John.Doe@Gmail.Com");
        assertEquals("john.doe@gmail.com", email.value);

        // All uppercase should be normalized
        email = new Email("MARY.TAN@YAHOO.COM");
        assertEquals("mary.tan@yahoo.com", email.value);

        // Leading and trailing spaces should be trimmed
        email = new Email("  test@example.com  ");
        assertEquals("test@example.com", email.value);

        // Mixed case with spaces
        email = new Email("  John.DOE@Example.COM  ");
        assertEquals("john.doe@example.com", email.value);
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // domain without dots (should be invalid)
        assertFalse(Email.isValidEmail("user@domain")); // no dot in domain
        assertFalse(Email.isValidEmail("a@bc")); // no dot in domain
        assertFalse(Email.isValidEmail("x@xx")); // no dot in domain
        assertFalse(Email.isValidEmail("test@localhost")); // no dot in domain

        // invalid parts
        assertFalse(Email.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Email.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("-peterjack@example.com")); // local part starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack-@example.com")); // local part ends with a hyphen
        assertFalse(Email.isValidEmail("peter..jack@example.com")); // local part has two consecutive periods
        assertFalse(Email.isValidEmail("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Email.isValidEmail("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Email.isValidEmail("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Email.isValidEmail("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.com-")); // domain name ends with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.c")); // domain name too short

        // valid email
        assertTrue(Email.isValidEmail("PeterJack@example.com"));
        assertTrue(Email.isValidEmail("a@bc.de")); // minimal valid email
        assertTrue(Email.isValidEmail("test@localhost.localdomain"));
        assertTrue(Email.isValidEmail("123@145.146.147.148"));
        assertTrue(Email.isValidEmail("peter+jack@example.com")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("peter_jack@example.com")); // '_' symbol in local part
        assertTrue(Email.isValidEmail("peter.jack@example.com")); // '.' symbol in local part
        assertTrue(Email.isValidEmail("peter-jack@example-site.com")); // '-' symbol in both parts

        // Test case insensitive validation
        assertTrue(Email.isValidEmail("john.doe@gmail.com"));
        assertTrue(Email.isValidEmail("JOHN.DOE@GMAIL.COM"));
        assertTrue(Email.isValidEmail("John.Doe@Gmail.Com"));
        assertTrue(Email.isValidEmail("john.DOE@gmail.COM"));

        // Test common email providers with various cases
        assertTrue(Email.isValidEmail("user@yahoo.com"));
        assertTrue(Email.isValidEmail("User@Yahoo.Com"));
        assertTrue(Email.isValidEmail("USER@YAHOO.COM"));
        assertTrue(Email.isValidEmail("student@e.ntu.edu.sg"));
        assertTrue(Email.isValidEmail("STUDENT@E.NTU.EDU.SG"));
    }

    @Test
    public void isValidEmail_domainMustHaveDot() {
        // Test cases specifically for the domain dot requirement

        // Invalid: additional domains without dots (beyond those already tested above)
        assertFalse(Email.isValidEmail("admin@server"));
        assertFalse(Email.isValidEmail("john@company"));

        // Valid: domains with at least one dot (examples from issue)
        assertTrue(Email.isValidEmail("user@domain.com"));
        assertTrue(Email.isValidEmail("x@xx.xx")); // specifically mentioned in issue
        assertTrue(Email.isValidEmail("test@localhost.local"));
        assertTrue(Email.isValidEmail("admin@server.org"));
        assertTrue(Email.isValidEmail("john@company.co.uk"));

        // Valid: complex domains with multiple dots
        assertTrue(Email.isValidEmail("user@mail.example.com"));
        assertTrue(Email.isValidEmail("staff@dept.university.edu"));
    }


    @Test
    public void equals() {
        Email email = new Email("valid@example.com");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@example.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different type -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other@example.com")));

        // case insensitive comparison after normalization
        assertTrue(email.equals(new Email("Valid@Example.Com")));
        assertTrue(email.equals(new Email("VALID@EXAMPLE.COM")));
        assertTrue(email.equals(new Email("valid@EXAMPLE.com")));

        // with whitespace normalization
        assertTrue(email.equals(new Email("  valid@example.com  ")));
    }

    @Test
    public void toString_returnsNormalizedValue() {
        Email email = new Email("valid@example.com");
        assertEquals("valid@example.com", email.toString());

        // Test with mixed case - should be normalized
        Email emailMixedCase = new Email("Valid@Example.Com");
        assertEquals("valid@example.com", emailMixedCase.toString());

        // Test with uppercase - should be normalized
        Email emailUpperCase = new Email("VALID@EXAMPLE.COM");
        assertEquals("valid@example.com", emailUpperCase.toString());

        // Test with whitespace - should be trimmed and normalized
        Email emailWithSpaces = new Email("  Valid@Example.Com  ");
        assertEquals("valid@example.com", emailWithSpaces.toString());
    }

    @Test
    public void hashCode_consistency() {
        Email email1 = new Email("test@example.com");
        Email email2 = new Email("test@example.com");
        Email email3 = new Email("other@example.com");

        // Same emails should have same hash code
        assertEquals(email1.hashCode(), email2.hashCode());

        // Normalized emails should have same hash code
        Email emailMixedCase = new Email("Test@Example.Com");
        assertEquals(email1.hashCode(), emailMixedCase.hashCode());

        // Different emails may have different hash codes
        // We just test that hashCode() doesn't throw exceptions
        email3.hashCode();
    }
}
