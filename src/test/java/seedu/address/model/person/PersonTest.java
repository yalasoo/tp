package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COLLEAGUE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void constructor_studentWithProvidedAttendance_usesProvidedAttendance() {
        Attendance existingAttendance = new Attendance();
        existingAttendance.markAttendance(LocalDate.of(2024, 1, 15), AttendanceStatus.PRESENT);

        Person student = new PersonBuilder().withTags("student").withAttendance(existingAttendance).build();

        assertEquals(1, student.getAttendanceRecords().size());
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name and phone, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB).withTags(VALID_TAG_COLLEAGUE).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different phone, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        // Note: Names are normalized, so trailing spaces are trimmed
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        // After normalization, both names should be equal, so they are the same person
        assertTrue(BOB.isSamePerson(editedBob));
        // Additionally verify that the normalized name matches
        assertEquals(VALID_NAME_BOB, editedBob.getName().fullName);
    }

    @Test
    public void markAttendance_nullDate_throwsAssertionError() {
        Person student = new PersonBuilder().withTags("student").build();

        assertThrows(AssertionError.class, () ->
                student.markAttendance(null, AttendanceStatus.PRESENT));
    }

    @Test
    public void markAttendance_nullStatus_throwsAssertionError() {
        Person student = new PersonBuilder().withTags("student").build();
        LocalDate date = LocalDate.of(2024, 1, 15);

        assertThrows(AssertionError.class, () ->
                student.markAttendance(date, null));
    }

    @Test
    public void markAttendance_validDateAndStatus_success() throws CommandException {
        LocalDate today = LocalDate.of(2025, 10, 9);
        Person person = new PersonBuilder().withTags("student").build();

        person.markAttendance(today, AttendanceStatus.PRESENT);

        Map<LocalDate, AttendanceStatus> records = person.getAttendanceRecords();
        assertEquals(1, records.size());
        assertEquals(AttendanceStatus.PRESENT, records.get(today));
    }

    @Test
    public void markAttendance_dateBeforeBirthday_failure() throws CommandException {
        Person person = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();

        LocalDate date = LocalDate.of(2023, 1, 1);

        assertFalse(person.markAttendance(date, AttendanceStatus.PRESENT));
    }

    @Test
    public void markAttendance_dateAfterToday_failure() throws CommandException {
        Person person = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();

        LocalDate date = LocalDate.now().plusDays(1);

        assertFalse(person.markAttendance(date, AttendanceStatus.PRESENT));
    }

    @Test
    public void markAttendance_colleague_failure() throws CommandException {
        Person person = new PersonBuilder().withTags("colleague").withBirthday("01-01-2024").build();

        LocalDate date = LocalDate.of(2024, 1, 1);

        assertFalse(person.markAttendance(date, AttendanceStatus.PRESENT));
    }

    @Test
    public void unmarkAttendance_nullDate_throwsAssertionError() {
        Person student = new PersonBuilder().withTags("student").build();

        assertThrows(AssertionError.class, () ->
                student.unmarkAttendance(null));
    }

    @Test
    public void unmarkAttendance_beforeBirthday_failure() {
        Person student = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();
        LocalDate date = LocalDate.of(2023, 1, 1);

        assertFalse(student.unmarkAttendance(date));
    }

    @Test
    public void unmarkAttendance_futureDate_failure() {
        Person student = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();
        LocalDate date = LocalDate.now().plusDays(1);

        assertFalse(student.unmarkAttendance(date));
    }

    @Test
    public void unmarkAttendance_colleague_failure() {
        Person student = new PersonBuilder().withTags("colleague").withBirthday("01-01-2024").build();
        LocalDate date = LocalDate.of(2024, 1, 1);

        assertFalse(student.unmarkAttendance(date));
    }

    @Test
    public void unmarkAttendance_studentValidDate_success() {
        Person student = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();
        LocalDate date = LocalDate.of(2024, 1, 1);

        assertTrue(student.unmarkAttendance(date));
    }

    @Test
    public void getAttendanceRecords_studentWithAttendance_returnsRecords() throws CommandException {
        Person student = new PersonBuilder().withTags("student").build();
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 16);

        student.markAttendance(date1, AttendanceStatus.PRESENT);
        student.markAttendance(date2, AttendanceStatus.LATE);

        Map<LocalDate, AttendanceStatus> records = student.getAttendanceRecords();
        assertEquals(2, records.size());
        assertEquals(AttendanceStatus.PRESENT, records.get(date1));
        assertEquals(AttendanceStatus.LATE, records.get(date2));
    }

    @Test
    public void getAttendanceRecords_studentWithoutAttendance_returnsEmptyMap() {
        Person student = new PersonBuilder().withTags("student").build();

        Map<LocalDate, AttendanceStatus> records = student.getAttendanceRecords();
        assertTrue(records.isEmpty());
    }

    @Test
    public void getAttendanceRecords_nonStudent_returnsEmptyMap() {
        Person nonStudent = new PersonBuilder().withTags("colleague").build();

        Map<LocalDate, AttendanceStatus> records = nonStudent.getAttendanceRecords();
        assertTrue(records.isEmpty());
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different class -> returns false
        editedAlice = new PersonBuilder(ALICE).withClass(VALID_CLASS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different birthday -> returns false
        editedAlice = new PersonBuilder(ALICE).withBirthday(VALID_BIRTHDAY_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_COLLEAGUE).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCode_test() {
        // same object -> same hashcode
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());

        // different person -> different hashcode (usually)
        // Note: This test might occasionally fail due to hash collisions, but it's very unlikely
        assertNotEquals(ALICE.hashCode(), BOB.hashCode());

        // test that equal objects have equal hashcodes
        Person alice1 = new PersonBuilder(ALICE).build();
        Person alice2 = new PersonBuilder(ALICE).build();
        assertTrue(alice1.equals(alice2));
        assertEquals(alice1.hashCode(), alice2.hashCode());

        // test that different objects have different hashcodes for different fields
        Person differentName = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertNotEquals(ALICE.hashCode(), differentName.hashCode());

        Person differentPhone = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertNotEquals(ALICE.hashCode(), differentPhone.hashCode());

        Person differentEmail = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertNotEquals(ALICE.hashCode(), differentEmail.hashCode());

        Person differentAddress = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertNotEquals(ALICE.hashCode(), differentAddress.hashCode());

        Person differentClass = new PersonBuilder(ALICE).withClass(VALID_CLASS_BOB).build();
        assertNotEquals(ALICE.hashCode(), differentClass.hashCode());

        Person differentBirthday = new PersonBuilder(ALICE).withBirthday(VALID_BIRTHDAY_BOB).build();
        assertNotEquals(ALICE.hashCode(), differentBirthday.hashCode());

        Person differentTags = new PersonBuilder(ALICE).withTags(VALID_TAG_COLLEAGUE).build();
        assertNotEquals(ALICE.hashCode(), differentTags.hashCode());
    }

    public void hashCode_sameFields_sameHashCode() {
        Person person1 = new PersonBuilder().withName("John Doe").withPhone("955-553-46")
                .withEmail("john@example.com").withAddress("Main St").withClass("K1A")
                .withNote("Test note").withTags("student").build();

        Person person2 = new PersonBuilder().withName("John Doe").withPhone("955-553-46")
                .withEmail("john@example.com").withAddress("Main St").withClass("K1A")
                .withNote("Test note").withTags("student").build();

        assertEquals(person1.hashCode(), person2.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                + ", class=" + ALICE.getStudentClass() + ", birthday=" + ALICE.getBirthday()
                + ", note=" + ALICE.getNote() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
