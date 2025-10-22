package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        // Create persons with birthdays far in the future
        Model testModel = new ModelManager();
        Person futurePerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday("01-01-2030"),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());
        testModel.addPerson(futurePerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);
        assertTrue(result.getFeedbackToUser().contains("No upcoming birthdays"));
    }

    @Test
    public void execute_todayBirthday_showsTodayBirthday() {
        // Create a person with today's birthday
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());

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
        // Create a person with birthday in 3 days
        LocalDate futureDate = LocalDate.now().plusDays(3);
        String futureBirthday = futureDate.format(DATE_FORMATTER);

        Person futurePerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(futureBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance());

        Model testModel = new ModelManager();
        testModel.addPerson(futurePerson);

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
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());

        // Create a person with birthday in 2 days
        LocalDate futureDate = LocalDate.now().plusDays(2);
        String futureBirthday = futureDate.format(DATE_FORMATTER);
        Person futurePerson = new Person(BENSON.getName(), BENSON.getPhone(), BENSON.getEmail(),
                BENSON.getAddress(), BENSON.getStudentClass(), new Birthday(futureBirthday),
                BENSON.getNote(), BENSON.getTags(), BENSON.getAttendance());

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);
        testModel.addPerson(futurePerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Check that both sections are present
        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains("Upcoming birthdays"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
        assertTrue(result.getFeedbackToUser().contains(BENSON.getName().toString()));
    }

    @Test
    public void execute_pastBirthday_doesNotShow() {
        // Create a person with birthday that passed 10 days ago
        LocalDate pastDate = LocalDate.now().minusDays(10);
        String pastBirthday = pastDate.format(DATE_FORMATTER);
        Person pastPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(pastBirthday),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());

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
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Check that tags are displayed
        assertTrue(result.getFeedbackToUser().contains("friends"));
    }

    @Test
    public void execute_personWithNote_showsCorrectly() {
        // Create a person with today's birthday and a note
        String today = LocalDate.now().format(DATE_FORMATTER);
        Person todayPerson = new Person(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getStudentClass(), new Birthday(today),
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());

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
                ALICE.getNote(), ALICE.getTags(), ALICE.getAttendance());

        Model testModel = new ModelManager();
        testModel.addPerson(todayPerson);

        RemindCommand remindCommand = new RemindCommand();
        CommandResult result = remindCommand.execute(testModel);

        // Should display without issues (student class doesn't affect birthday display)
        assertTrue(result.getFeedbackToUser().contains("Happy Birthday to these people today"));
        assertTrue(result.getFeedbackToUser().contains(ALICE.getName().toString()));
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
