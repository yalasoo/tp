package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COLLEAGUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STUDENT;
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
    public void isSamePerson_colleagueDuplicateDetection() {
        // Test colleague duplicate detection logic - colleagues can have same names

        // Two colleagues with same phone -> duplicate (not allowed)
        Person colleague1 = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_COLLEAGUE).build();
        Person colleague2 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_COLLEAGUE).build();
        assertTrue(colleague1.isSamePerson(colleague2)); // Same phone, should be duplicate

        // Two colleagues with same email -> duplicate (not allowed)
        Person colleague3 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_COLLEAGUE).build();
        assertTrue(colleague1.isSamePerson(colleague3)); // Same email, should be duplicate

        // Two colleagues with same name but different phone and email -> allowed
        Person colleague4 = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_COLLEAGUE).build();
        assertFalse(colleague1.isSamePerson(colleague4)); // Same name allowed for colleagues

        // Two colleagues with different names, phones, and emails -> different persons
        Person colleague5 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_COLLEAGUE).build();
        assertFalse(colleague1.isSamePerson(colleague5));
    }

    @Test
    public void isSamePerson_studentDuplicateDetection() {
        // Test student duplicate detection logic (should allow different names with same phone)

        // Two students with same name and phone -> same person
        Person student1 = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_STUDENT).build();
        Person student2 = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_STUDENT).build();
        assertTrue(student1.isSamePerson(student2));

        // Two students with different names but same phone -> different persons (allowed for emergency contacts)
        Person student3 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_STUDENT).build();
        assertFalse(student1.isSamePerson(student3)); // Different names allowed for students

        // Two students with different names and phones -> different persons
        Person student4 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_STUDENT).build();
        assertFalse(student1.isSamePerson(student4));
    }

    @Test
    public void isSamePerson_mixedContactTypes() {
        // Test mixed contact type scenarios

        // Student and colleague with same details -> same persons (same name and phone)
        Person student = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_STUDENT).build();
        Person colleague = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_COLLEAGUE).build();
        assertTrue(student.isSamePerson(colleague)); // Same name and phone, falls back to student logic

        // Student and colleague with different names, same phone -> not duplicates (student logic applies)
        Person student2 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_STUDENT).build();
        assertFalse(student.isSamePerson(student2)); // Different names, student logic allows same phone

        // Colleague and student with different names, same phone -> not duplicates (student logic applies)
        assertFalse(colleague.isSamePerson(student2)); // Mixed types, falls back to student logic
    }

    @Test
    public void isStudent() {
        // Test isStudent method coverage
        Person student = new PersonBuilder().withTags(VALID_TAG_STUDENT).build();
        Person colleague = new PersonBuilder().withTags(VALID_TAG_COLLEAGUE).build();

        assertTrue(student.isStudent());
        assertFalse(colleague.isStudent());
    }

    @Test
    public void isColleague() {
        // Test isColleague method coverage
        Person student = new PersonBuilder().withTags(VALID_TAG_STUDENT).build();
        Person colleague = new PersonBuilder().withTags(VALID_TAG_COLLEAGUE).build();

        assertFalse(student.isColleague());
        assertTrue(colleague.isColleague());
    }

    @Test
    public void isSamePerson_edgeCases() {
        // Test edge cases for isSamePerson method
        Person person1 = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withTags(VALID_TAG_COLLEAGUE).build();

        // Same object reference -> true
        assertTrue(person1.isSamePerson(person1));

        // Null comparison -> false
        assertFalse(person1.isSamePerson(null));

        // Two colleagues with same phone, different names -> duplicate (should be true)
        Person colleague2 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_BOB).withTags(VALID_TAG_COLLEAGUE).build();
        assertTrue(person1.isSamePerson(colleague2)); // Different names but same phone - not allowed for colleagues
    }

    @Test
    public void isSamePerson_colleagueDifferentPhoneSameEmail() {
        // Test specific case: colleagues with different phones but same email
        Person colleague1 = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail("common@email.com").withTags(VALID_TAG_COLLEAGUE).build();
        Person colleague2 = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail("common@email.com").withTags(VALID_TAG_COLLEAGUE).build();

        // Should be detected as duplicate due to same email
        assertTrue(colleague1.isSamePerson(colleague2));
        assertTrue(colleague2.isSamePerson(colleague1)); // Test bidirectional
    }

    @Test
    public void markAttendance_nullDate_throwsAssertionError() {
        Person student = new PersonBuilder().withTags("student").build();
        StringBuilder contactsNotMarked = new StringBuilder();

        assertThrows(AssertionError.class, () ->
                student.markAttendance(null, AttendanceStatus.PRESENT, contactsNotMarked));
    }

    @Test
    public void markAttendance_nullStatus_throwsAssertionError() {
        Person student = new PersonBuilder().withTags("student").build();
        LocalDate date = LocalDate.of(2024, 1, 15);
        StringBuilder contactsNotMarked = new StringBuilder();

        assertThrows(AssertionError.class, () ->
                student.markAttendance(date, null, contactsNotMarked));
    }

    @Test
    public void markAttendance_validDateAndStatus_success() throws CommandException {
        LocalDate today = LocalDate.of(2025, 10, 9);
        Person person = new PersonBuilder().withTags("student").build();
        StringBuilder contactsNotMarked = new StringBuilder();

        person.markAttendance(today, AttendanceStatus.PRESENT, contactsNotMarked);

        Map<LocalDate, AttendanceStatus> records = person.getAttendanceRecords();
        assertEquals(1, records.size());
        assertEquals(AttendanceStatus.PRESENT, records.get(today));
    }

    @Test
    public void markAttendance_dateBeforeBirthday_failure() throws CommandException {
        Person person = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();
        StringBuilder contactsNotMarked = new StringBuilder();

        LocalDate date = LocalDate.of(2023, 1, 1);

        assertFalse(person.markAttendance(date, AttendanceStatus.PRESENT, contactsNotMarked));
    }

    @Test
    public void markAttendance_dateAfterToday_failure() throws CommandException {
        Person person = new PersonBuilder().withTags("student").withBirthday("01-01-2024").build();
        StringBuilder contactsNotMarked = new StringBuilder();

        LocalDate date = LocalDate.now().plusDays(1);

        assertFalse(person.markAttendance(date, AttendanceStatus.PRESENT, contactsNotMarked));
    }

    @Test
    public void markAttendance_colleague_failure() throws CommandException {
        Person person = new PersonBuilder().withTags("colleague").withBirthday("01-01-2024").build();
        StringBuilder contactsNotMarked = new StringBuilder();

        LocalDate date = LocalDate.of(2024, 1, 1);

        assertFalse(person.markAttendance(date, AttendanceStatus.PRESENT, contactsNotMarked));
    }

    @Test
    public void getAttendanceRecords_studentWithAttendance_returnsRecords() throws CommandException {
        Person student = new PersonBuilder().withTags("student").build();
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.of(2024, 1, 16);
        StringBuilder contactsNotMarked = new StringBuilder();

        student.markAttendance(date1, AttendanceStatus.PRESENT, contactsNotMarked);
        student.markAttendance(date2, AttendanceStatus.LATE, contactsNotMarked);

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
