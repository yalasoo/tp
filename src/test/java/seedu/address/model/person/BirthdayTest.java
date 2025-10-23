package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class BirthdayTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Birthday(null));
    }

    @Test
    public void constructor_invalidBirthday_throwsIllegalArgumentException() {
        String invalidBirthday = "32-13-2005"; // Invalid date
        assertThrows(IllegalArgumentException.class, () -> new Birthday(invalidBirthday));
    }

    @Test
    public void isValidBirthday() {
        // null birthday
        assertThrows(NullPointerException.class, () -> Birthday.isValidBirthday(null));

        // invalid birthdays
        assertFalse(Birthday.isValidBirthday("")); // empty string
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only
        assertFalse(Birthday.isValidBirthday("32-13-2005")); // invalid date
        // assertFalse(Birthday.isValidBirthday("31-04-2020")); // April only has 30 days
        // assertFalse(Birthday.isValidBirthday("29-02-2021")); // 2021 is not a leap year
        assertFalse(Birthday.isValidBirthday("15-03-20")); // wrong year format
        assertFalse(Birthday.isValidBirthday("15/03/2020")); // wrong separator
        assertFalse(Birthday.isValidBirthday("15-3-2020")); // single digit month
        assertFalse(Birthday.isValidBirthday("5-03-2020")); // single digit day
        assertFalse(Birthday.isValidBirthday("15-03-20200")); // 5-digit year
        assertFalse(Birthday.isValidBirthday("abc-03-2020")); // non-numeric day
        assertFalse(Birthday.isValidBirthday("15-abc-2020")); // non-numeric month
        assertFalse(Birthday.isValidBirthday("15-03-abcd")); // non-numeric year
        assertFalse(Birthday.isValidBirthday("00-03-2020")); // zero day
        assertFalse(Birthday.isValidBirthday("15-00-2020")); // zero month
        assertFalse(Birthday.isValidBirthday("15-13-2020")); // month > 12
        assertFalse(Birthday.isValidBirthday("32-01-2020")); // day > 31

        // valid birthdays
        assertTrue(Birthday.isValidBirthday("15-03-2018")); // valid date
        assertTrue(Birthday.isValidBirthday("24-12-2017")); // valid date
        assertTrue(Birthday.isValidBirthday("29-02-2020")); // leap year
        assertTrue(Birthday.isValidBirthday("31-01-2019")); // 31-day month
        assertTrue(Birthday.isValidBirthday("30-04-2019")); // 30-day month
        assertTrue(Birthday.isValidBirthday("28-02-2019")); // non-leap year February
        assertTrue(Birthday.isValidBirthday("01-01-2020")); // single digit with leading zeros
    }

    @Test
    public void equals() {
        Birthday birthday = new Birthday("15-03-2018");

        // same values -> returns true
        assertTrue(birthday.equals(new Birthday("15-03-2018")));

        // same object -> returns true
        assertTrue(birthday.equals(birthday));

        // null -> returns false
        assertFalse(birthday.equals(null));

        // different types -> returns false
        assertFalse(birthday.equals(5.0f));

        // different values -> returns false
        assertFalse(birthday.equals(new Birthday("24-12-2017")));
    }

    @Test
    public void hashCode_test() {
        Birthday birthday1 = new Birthday("15-03-2018");
        Birthday birthday2 = new Birthday("15-03-2018");
        Birthday birthday3 = new Birthday("24-12-2017");

        // same values -> same hashcode
        assertTrue(birthday1.hashCode() == birthday2.hashCode());

        // different values -> different hashcode (usually)
        assertFalse(birthday1.hashCode() == birthday3.hashCode());
    }

    @Test
    public void toString_test() {
        Birthday birthday = new Birthday("15-03-2018");
        assertTrue(birthday.toString().equals("15-03-2018"));
    }

    @Test
    public void dateField_test() {
        Birthday birthday = new Birthday("15-03-2018");
        // Test that the LocalDate field is properly set
        assertTrue(birthday.date != null);
        assertTrue(birthday.date.getDayOfMonth() == 15);
        assertTrue(birthday.date.getMonthValue() == 3);
        assertTrue(birthday.date.getYear() == 2018);
    }
}
