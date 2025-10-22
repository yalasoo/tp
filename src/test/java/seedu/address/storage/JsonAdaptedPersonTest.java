package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.model.person.Class;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.testutil.PersonBuilder;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "12345678"; // Invalid: doesn't start with 8 or 9
    private static final String INVALID_CLASS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_NOTE = "Hello" + '\u0000' + "World";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_CLASS = BENSON.getStudentClass().toString();
    private static final String VALID_NOTE = BENSON.getNote().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final Map<String, String> VALID_ATTENDANCE = Map.of(
            "2024-10-23", "PRESENT",
            "2024-10-24", "ABSENT"
    );

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void constructor_personWithAttendance_populatesAttendanceMap() throws IllegalValueException {
        // Create a person with attendance records
        Person person = new PersonBuilder().withTags("student").build();

        // Add attendance records to the person
        person.markAttendance(LocalDate.of(2024, 1, 15), AttendanceStatus.PRESENT);
        person.markAttendance(LocalDate.of(2024, 1, 16), AttendanceStatus.LATE);

        JsonAdaptedPerson jsonPerson = new JsonAdaptedPerson(person);
        Person samePerson = jsonPerson.toModelType();

        assertFalse(samePerson.getAttendance().isEmpty());
        assertEquals(2, samePerson.getAttendance().size());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidClass_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, INVALID_CLASS, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = Class.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullClass_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, null, VALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Class.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullNote_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, null, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Note.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidNote_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, INVALID_NOTE, VALID_TAGS, VALID_ATTENDANCE);
        String expectedMessage = Note.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_CLASS, VALID_NOTE, invalidTags, VALID_ATTENDANCE);
        assertThrows(IllegalValueException.class, person::toModelType);
    }
}
