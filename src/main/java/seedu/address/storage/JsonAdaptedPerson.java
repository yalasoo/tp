package seedu.address.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.model.person.Address;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Class;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String studentClass;
    private final String birthday;
    private final String note;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final Map<String, String> attendance = new HashMap<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("studentClass") String studentClass,
                             @JsonProperty("birthday") String birthday,
                             @JsonProperty("note") String note,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("attendance") Map<String, String> attendance) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentClass = studentClass;
        this.birthday = birthday;
        this.note = note;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (attendance != null) {
            this.attendance.putAll(attendance);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        studentClass = source.getStudentClass().value;
        birthday = source.getBirthday().value;
        note = source.getNote().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));

        if (source.getAttendance() != null && !source.getAttendanceRecords().isEmpty()) {
            source.getAttendanceRecords().forEach((date, status) ->
                    attendance.put(date.toString(), status.toString()));
        }
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final Attendance modelAttendance = new Attendance();
        for (Map.Entry<String, String> entry : attendance.entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey());
            AttendanceStatus status = AttendanceStatus.valueOf(entry.getValue());
            modelAttendance.markAttendance(date, status);
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (studentClass == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Class.class.getSimpleName()));
        }
        if (!Class.isValidClass(studentClass)) {
            throw new IllegalValueException(Class.MESSAGE_CONSTRAINTS);
        }
        final Class modelStudentClass = new Class(studentClass);

        if (birthday == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Birthday.class.getSimpleName()));
        }
        if (!Birthday.isValidBirthday(birthday)) {
            throw new IllegalValueException(Birthday.MESSAGE_CONSTRAINTS);
        }
        final Birthday modelBirthday = new Birthday(birthday);

        if (note == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Note.class.getSimpleName()));
        }
        if (!Note.isValidNote(note)) {
            throw new IllegalValueException(Note.MESSAGE_CONSTRAINTS);
        }
        final Note modelNote = new Note(note);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        return new Person(modelName, modelPhone, modelEmail, modelAddress,
                          modelStudentClass, modelBirthday, modelNote, modelTags, modelAttendance);
    }
}
