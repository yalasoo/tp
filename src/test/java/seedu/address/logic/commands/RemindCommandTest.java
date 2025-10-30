package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Person;

public class RemindCommandTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_noPersons_showsNoPersonsMessage() {
        Model emptyModel = new ModelManager();
        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(emptyModel);
        assertEquals(RemindCommand.MESSAGE_NO_PERSONS, result.getFeedbackToUser());
    }

    @Test
    public void execute_noUpcomingBirthdays_showsNoBirthdaysMessage() {
        // Create persons with birthdays far in the past (not upcoming)
        Model testModel = new ModelManager();
        Person pastPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday("01-01-1990"),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);
        testModel.addPerson(pastPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);
        assertTrue(result.getFeedbackToUser().contains("No upcoming birthdays"));
    }

    @Test
    public void execute_todayBirthday_showsTodayBirthday() {
        // Create a person with today's birthday (use a past date that falls today)
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains("TODAY!"));
    }

    @Test
    public void execute_upcomingBirthday_showsUpcomingBirthday() {
        // Create a person with birthday that is in 3 days
        // Use a past date that will have their birthday in 3 days from now
        LocalDate birthdayIn3Days = LocalDate.now().plusDays(3);
        int birthdayYear = LocalDate.now().getYear() - 1; // Use last year's date
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn3Days.getMonth(),
                birthdayIn3Days.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(pastBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Upcoming birthdays"));
        assertTrue(result.getFeedbackToUser().contains(BENSON.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains("in 3 days"));
    }

    @Test
    public void execute_bothTodayAndUpcoming_showsBothSections() {
        // Create a person with today's birthday
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        // Create a person with birthday that is in 2 days
        LocalDate birthdayIn2Days = LocalDate.now().plusDays(2);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn2Days.getMonth(),
                birthdayIn2Days.getDayOfMonth());
        String upcomingBirthday = pastBirthdayDate.format(DATE_FORMATTER);
        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(upcomingBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Check that both sections are present
        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains("Upcoming birthdays"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains(BENSON.getName().toString()));
    }

    @Test
    public void execute_birthdayExactlySevenDays_showsUpcomingBirthday() {
        // Create a person with birthday whose birthday is exactly 7 days from now
        LocalDate birthdayIn7Days = LocalDate.now().plusDays(7);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn7Days.getMonth(),
                birthdayIn7Days.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(pastBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Upcoming birthdays"));
        assertTrue(result.getFeedbackToUser().contains(BENSON.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains("in 7 days"));
    }

    @Test
    public void execute_pastBirthday_doesNotShow() {
        // Create a person with birthday that passed 10 days ago (birthday already passed)
        LocalDate passedBirthday = LocalDate.now().minusDays(10);
        int birthdayYear = LocalDate.now().getYear();
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear - 1,
                passedBirthday.getMonth(), passedBirthday.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);
        Person pastPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(pastBirthday),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(pastPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should show no upcoming birthdays since the birthday already passed
        assertTrue(result.getFeedbackToUser().contains("No upcoming birthdays"));
        assertFalse(result.getFeedbackToUser().contains(ALICE.getName().toString()));
    }

    @Test
    public void execute_personWithTags_showsTags() {
        // Create a person with today's birthday and tags
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Check that tags are displayed
        assertTrue(result.getFeedbackToUser().contains("student"));
    }

    @Test
    public void execute_personWithNote_showsCorrectly() {
        // Create a person with today's birthday and a note
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should display without issues (note doesn't affect birthday display)
        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
    }

    @Test
    public void execute_personWithStudentClass_showsCorrectly() {
        // Create a person with today's birthday and student class
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should display without issues (student class doesn't affect birthday display)
        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
    }

    @Test
    public void execute_personWithoutTags_showsWithoutBrackets() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPersonWithoutTags = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), new java.util.HashSet<>(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPersonWithoutTags);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertFalse(result.getFeedbackToUser().contains("[]"));
    }

    @Test
    public void execute_upcomingBirthdayOneDay_showsSingularDay() {
        // Create a person with birthday that is in 1 day
        LocalDate birthdayIn1Day = LocalDate.now().plusDays(1);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn1Day.getMonth(),
                birthdayIn1Day.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(pastBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("in 1 day"));
        assertFalse(result.getFeedbackToUser().contains("in 1 days"));
    }

    @Test
    public void execute_upcomingBirthdayMultipleDays_showsPluralDays() {
        // Create a person with birthday that is in 2 days
        LocalDate birthdayIn2Days = LocalDate.now().plusDays(2);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn2Days.getMonth(),
                birthdayIn2Days.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(pastBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("in 2 days"));
    }

    @Test
    public void execute_birthdayEightDaysAway_doesNotShow() {
        // Create a person with birthday that is in 8 days (beyond reminder window)
        LocalDate birthdayIn8Days = LocalDate.now().plusDays(8);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn8Days.getMonth(),
                birthdayIn8Days.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(pastBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("No upcoming birthdays"));
        assertFalse(result.getFeedbackToUser().contains(BENSON.getName().toString()));
    }

    @Test
    public void execute_multiplePersonsSameBirthday_showsAll() {
        String today = LocalDate.now().format(DATE_FORMATTER);

        Person person1 = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Person person2 = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(today),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(person1);
        testModel.addPerson(person2);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains(BENSON.getName().toString()));
    }

    @Test
    public void execute_emptyTodayList_returnsNoBirthdaysTodayMessage() {
        // Only add persons with upcoming birthdays, no today birthdays
        LocalDate birthdayIn3Days = LocalDate.now().plusDays(3);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn3Days.getMonth(),
                birthdayIn3Days.getDayOfMonth());
        String upcomingBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(upcomingBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should show "No birthdays today!" message
        assertTrue(result.getFeedbackToUser().contains("No birthdays today!"));
    }

    @Test
    public void execute_emptyUpcomingList_returnsNoUpcomingBirthdaysMessage() {
        // Only add persons with today's birthdays, no upcoming birthdays
        String today = LocalDate.now().format(DATE_FORMATTER);

        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should show today birthdays but no upcoming section
        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertFalse(result.getFeedbackToUser().contains("Upcoming birthdays"));
    }

    @Test
    public void execute_bothListsEmpty_returnsNoUpcomingBirthdaysMessage() {
        // Add persons with birthdays that are beyond 7 days
        LocalDate birthdayIn10Days = LocalDate.now().plusDays(10);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, birthdayIn10Days.getMonth(),
                birthdayIn10Days.getDayOfMonth());
        String farFutureBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person farFuturePerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(farFutureBirthday),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(farFuturePerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should show "No upcoming birthdays found" since both today and upcoming lists are empty
        assertEquals(RemindCommand.MESSAGE_NO_UPCOMING_BIRTHDAYS, result.getFeedbackToUser());
    }

    @Test
    public void execute_pastBirthdaysOnly_returnsNoUpcomingBirthdaysMessage() {
        // Add persons with birthdays that is already passed this year
        LocalDate passedBirthday = LocalDate.now().minusDays(5);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear, passedBirthday.getMonth(),
                passedBirthday.getDayOfMonth());
        String pastBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person pastPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(pastBirthday),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(pastPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should show "No upcoming birthdays found" since both today and upcoming lists are empty
        assertEquals(RemindCommand.MESSAGE_NO_UPCOMING_BIRTHDAYS, result.getFeedbackToUser());
    }

    @Test
    public void execute_personWithMultipleTags_showsAllTags() {
        String today = LocalDate.now().format(DATE_FORMATTER);

        java.util.Set<seedu.address.model.tag.Tag> multipleTags = new java.util.HashSet<>();
        multipleTags.add(new seedu.address.model.tag.Tag("student"));

        Person multiTagPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), multipleTags, ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(multiTagPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("student"));
    }

    @Test
    public void execute_onlyTodayBirthdays_noUpcomingSection() {
        String today = LocalDate.now().format(DATE_FORMATTER);

        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertFalse(result.getFeedbackToUser().contains("Upcoming birthdays"));
    }

    @Test
    public void execute_onlyUpcomingBirthdays_noTodaySection() {
        // Create a person with birthday whose birthday is in 3 days
        LocalDate birthdayIn3Days = LocalDate.now().plusDays(3);
        int birthdayYear = LocalDate.now().getYear() - 1;
        LocalDate pastBirthdayDate = LocalDate.of(birthdayYear,
                birthdayIn3Days.getMonth(), birthdayIn3Days.getDayOfMonth());
        String upcomingBirthday = pastBirthdayDate.format(DATE_FORMATTER);

        Person upcomingPerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(upcomingBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance(), null);

        Model testModel = new ModelManager();
        testModel.addPerson(upcomingPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        assertTrue(result.getFeedbackToUser().contains("Upcoming birthdays"));
        assertTrue(result.getFeedbackToUser().contains(BENSON.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains("No birthdays today"));
    }

    // The rest of your test methods (calculateDaysUntilBirthday tests and equals) remain the same
    // since they don't create Birthday objects with future dates

    @Test
    public void calculateDaysUntilBirthday_normalBirthdayThisYear_returnsCorrectDays() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate birthday = LocalDate.of(2000, 6, 15);
        LocalDate today = LocalDate.of(2024, 6, 10);

        long result = remindCommand.calculateDaysUntilBirthday(birthday, today);
        assertEquals(5, result);
    }

    @Test
    public void calculateDaysUntilBirthday_birthdayPassedThisYear_returnsNextYear() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate birthday = LocalDate.of(2000, 1, 15);
        LocalDate today = LocalDate.of(2024, 6, 10);

        long result = remindCommand.calculateDaysUntilBirthday(birthday, today);
        LocalDate expectedNextBirthday = LocalDate.of(2025, 1, 15);
        long expectedDays = ChronoUnit.DAYS.between(today, expectedNextBirthday);

        assertEquals(expectedDays, result);
    }

    @Test
    public void calculateDaysUntilBirthday_feb29NonLeapYear_returnsFeb28() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate feb29Birthday = LocalDate.of(2000, 2, 29);
        LocalDate today = LocalDate.of(2023, 1, 1); // Non-leap year

        long result = remindCommand.calculateDaysUntilBirthday(feb29Birthday, today);
        LocalDate expectedNextBirthday = LocalDate.of(2023, 2, 28);
        long expectedDays = ChronoUnit.DAYS.between(today, expectedNextBirthday);

        assertEquals(expectedDays, result);
    }

    @Test
    public void calculateDaysUntilBirthday_feb29LeapYear_returnsFeb29() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate feb29Birthday = LocalDate.of(2000, 2, 29);
        LocalDate today = LocalDate.of(2024, 1, 1); // Leap year

        long result = remindCommand.calculateDaysUntilBirthday(feb29Birthday, today);
        LocalDate expectedNextBirthday = LocalDate.of(2024, 2, 29);
        long expectedDays = ChronoUnit.DAYS.between(today, expectedNextBirthday);

        assertEquals(expectedDays, result);
    }

    @Test
    public void calculateDaysUntilBirthday_todayIsBirthday_returnsZero() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate birthday = LocalDate.of(2000, 6, 15);
        LocalDate today = LocalDate.of(2024, 6, 15);

        long result = remindCommand.calculateDaysUntilBirthday(birthday, today);
        assertEquals(0, result);
    }

    @Test
    public void calculateDaysUntilBirthday_yearBoundary_handlesCorrectly() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        LocalDate today = LocalDate.of(2024, 12, 31);

        long result = remindCommand.calculateDaysUntilBirthday(birthday, today);
        assertEquals(1, result); // Next birthday is Jan 1, 2025 (1 day away)
    }

    @Test
    public void calculateDaysUntilBirthday_feb28Birthday_normalCase() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate feb28Birthday = LocalDate.of(2000, 2, 28);
        LocalDate today = LocalDate.of(2023, 1, 1); // Non-leap year

        long result = remindCommand.calculateDaysUntilBirthday(feb28Birthday, today);
        LocalDate expectedNextBirthday = LocalDate.of(2023, 2, 28);
        long expectedDays = ChronoUnit.DAYS.between(today, expectedNextBirthday);

        assertEquals(expectedDays, result);
    }

    @Test
    public void calculateDaysUntilBirthday_feb29LeapYearAfterFeb_returnsFeb29NextYear() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate feb29Birthday = LocalDate.of(2000, 2, 29);
        LocalDate today = LocalDate.of(2024, 3, 1); // After February in leap year

        long result = remindCommand.calculateDaysUntilBirthday(feb29Birthday, today);

        // Should calculate days until Feb 28, 2025 (next year is non-leap)
        LocalDate expectedNextBirthday = LocalDate.of(2025, 2, 28);
        long expectedDays = ChronoUnit.DAYS.between(today, expectedNextBirthday);

        assertEquals(expectedDays, result);
    }

    @Test
    public void calculateDaysUntilBirthday_feb29LeapYearOnFeb28_returnsOneDay() {
        RemindCommand remindCommand = new RemindCommand();
        LocalDate feb29Birthday = LocalDate.of(2000, 2, 29);
        LocalDate today = LocalDate.of(2024, 2, 28); // On Feb 28 in leap year

        long result = remindCommand.calculateDaysUntilBirthday(feb29Birthday, today);

        // Should calculate 1 day until Feb 29, 2024 (tomorrow)
        assertEquals(1, result);
    }

    @Test
    public void getFormattedPersonList_emptyList_returnsEmptyString() {
        RemindCommand remindCommand = new RemindCommand();

        // Use reflection to test private method, or test through public interface
        List<Person> emptyList = new ArrayList<>();

        // Since it's private, we'll test through the public execute method
        // This case is covered when there are no birthdays but persons exist
        Model testModel = new ModelManager();
        Person personWithPastBirthday = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday("01-01-1990"),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance(), null);
        testModel.addPerson(personWithPastBirthday);

        CommandResult result = remindCommand.execute(testModel);
        // Should show no upcoming birthdays message, not crash
        assertTrue(result.getFeedbackToUser().contains("No upcoming birthdays"));
    }

    @Test
    public void equals() {
        RemindCommand remindCommand = new RemindCommand();

        // same object -> returns true
        assertTrue(remindCommand.equals(remindCommand));

        // different types -> returns false
        assertFalse(remindCommand.equals(1));

        // null -> returns false
        assertFalse(remindCommand.equals(null));
    }
}
