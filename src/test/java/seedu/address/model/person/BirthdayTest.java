package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        assertFalse(Birthday.isValidBirthday("31-04-2020")); // April only has 30 days
        assertFalse(Birthday.isValidBirthday("29-02-2021")); // 2021 is not a leap year
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

        assertFalse(Birthday.isValidBirthday("31-12-1899")); // before 1900
        assertFalse(Birthday.isValidBirthday("01-01-1800")); // before 1900

        // Get tomorrow's date for testing future date validation
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String tomorrowFormatted = tomorrow.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertFalse(Birthday.isValidBirthday(tomorrowFormatted)); // future date

        // Add these to your existing valid birthdays section:


        // valid birthdays
        assertTrue(Birthday.isValidBirthday("15-03-2018")); // valid date
        assertTrue(Birthday.isValidBirthday("24-12-2017")); // valid date
        assertTrue(Birthday.isValidBirthday("15-03-1950")); // within valid range
        // Today's date should be valid
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertTrue(Birthday.isValidBirthday(today));

        // edge cases
        assertTrue(Birthday.isValidBirthday("29-02-2020")); // leap year
        assertTrue(Birthday.isValidBirthday("31-01-2019")); // 31-day month
        assertTrue(Birthday.isValidBirthday("30-04-2019")); // 30-day month
        assertTrue(Birthday.isValidBirthday("28-02-2019")); // non-leap year February
        assertTrue(Birthday.isValidBirthday("01-01-1900")); // exactly 1900
    }

    @Test
    public void isValidBirthday_dateRangeValidation() {
        // Get today's date and format it
        LocalDate today = LocalDate.now();
        String todayFormatted = today.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));

        // Get tomorrow's date
        LocalDate tomorrow = today.plusDays(1);
        String tomorrowFormatted = tomorrow.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));

        // Get yesterday's date
        LocalDate yesterday = today.minusDays(1);
        String yesterdayFormatted = yesterday.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));

        // Get date from 1899
        String dateBefore1900 = "31-12-1899";

        // Get date from exactly 1900
        String dateExactly1900 = "01-01-1900";

        // Get date from 1901
        String dateAfter1900 = "01-01-1901";

        // Invalid: dates before 1900
        assertFalse(Birthday.isValidBirthday(dateBefore1900), "Date before 1900 should be invalid");
        assertFalse(Birthday.isValidBirthday("15-03-1899"), "Date in 1899 should be invalid");
        assertFalse(Birthday.isValidBirthday("31-12-1799"), "Date in 1799 should be invalid");

        // Invalid: dates after today (future dates)
        assertFalse(Birthday.isValidBirthday(tomorrowFormatted), "Tomorrow's date should be invalid");

        // Create a date far in the future
        LocalDate futureDate = today.plusYears(10);
        String futureDateFormatted = futureDate.format(DateTimeFormatter.ofPattern("dd-MM-yuuu"));
        assertFalse(Birthday.isValidBirthday(futureDateFormatted), "Future date should be invalid");

        // Valid: dates from 1900 onwards up to today
        assertTrue(Birthday.isValidBirthday(dateExactly1900), "January 1, 1900 should be valid");
        assertTrue(Birthday.isValidBirthday(dateAfter1900), "Date after 1900 should be valid");
        assertTrue(Birthday.isValidBirthday(yesterdayFormatted), "Yesterday's date should be valid");
        assertTrue(Birthday.isValidBirthday(todayFormatted), "Today's date should be valid");

        // Valid: various dates within the valid range
        assertTrue(Birthday.isValidBirthday("15-03-1950"), "Date in 1950 should be valid");
        assertTrue(Birthday.isValidBirthday("24-12-1999"), "Date in 1999 should be valid");
        assertTrue(Birthday.isValidBirthday("01-01-2000"), "Date in 2000 should be valid");
    }

    @Test
    public void constructor_futureDate_throwsIllegalArgumentException() {
        // Get tomorrow's date
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String tomorrowFormatted = tomorrow.format(DateTimeFormatter.ofPattern("dd-MM-yuuu"));

        assertThrows(IllegalArgumentException.class, () -> new Birthday(tomorrowFormatted));
    }

    @Test
    public void constructor_dateBefore1900_throwsIllegalArgumentException() {
        String dateBefore1900 = "31-12-1899";
        assertThrows(IllegalArgumentException.class, () -> new Birthday(dateBefore1900));
    }

    @Test
    public void edgeCases_dateRange() {
        // Edge case: exactly 1900-01-01 (should be valid)
        assertTrue(Birthday.isValidBirthday("01-01-1900"));

        // Edge case: exactly today (should be valid)
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertTrue(Birthday.isValidBirthday(today));

        // Edge case: one day after today (should be invalid)
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertFalse(Birthday.isValidBirthday(tomorrow));

        // Edge case: one day before 1900 (should be invalid)
        assertFalse(Birthday.isValidBirthday("31-12-1899"));
    }

    @Test
    public void validHistoricalDates() {
        // Test various valid historical dates
        assertTrue(Birthday.isValidBirthday("15-03-1900"));
        assertTrue(Birthday.isValidBirthday("28-02-1920")); // Leap year in 1920
        assertTrue(Birthday.isValidBirthday("06-06-1944")); // D-Day
        assertTrue(Birthday.isValidBirthday("20-07-1969")); // Moon landing
        assertTrue(Birthday.isValidBirthday("09-11-1989")); // Fall of Berlin Wall
        assertTrue(Birthday.isValidBirthday("01-01-2000")); // Y2K
    }

    @Test
    public void invalidFutureDates() {
        // Test various invalid future dates
        LocalDate nextYear = LocalDate.now().plusYears(1);
        String nextYearFormatted = nextYear.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertFalse(Birthday.isValidBirthday(nextYearFormatted));

        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        String nextMonthFormatted = nextMonth.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertFalse(Birthday.isValidBirthday(nextMonthFormatted));

        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        String nextWeekFormatted = nextWeek.format(DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        assertFalse(Birthday.isValidBirthday(nextWeekFormatted));
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
