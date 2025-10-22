package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Note note;

    // Data fields
    private final Class studentClass;
    private final Set<Tag> tags = new HashSet<>();
    private final Attendance attendance;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Class studentClass, Note note, Set<Tag> tags,
                  Attendance attendance) {
        requireAllNonNull(name, phone, email, address, studentClass, note, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentClass = studentClass;
        this.note = note;
        this.tags.addAll(tags);

        // only add attendance for student
        if (tags.contains(new Tag("student"))) {
            this.attendance = (attendance != null) ? attendance : new Attendance();
        } else {
            this.attendance = null;
        }
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Class getStudentClass() {
        return studentClass;
    }

    public Note getNote() {
        return note;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Marks the attendance of this person object
     * @param date when does this attendance apply
     * @param status what is the status of this attendance
     */
    public void markAttendance(LocalDate date, AttendanceStatus status) {
        assert date != null;
        assert status != null;

        if (attendance != null) {
            attendance.markAttendance(date, status);
        }
    }

    public Map<LocalDate, AttendanceStatus> getAttendanceRecords() {
        return (attendance != null) ? attendance.getAttendanceRecords() : Collections.emptyMap();
    }

    /**
     * Returns true if both persons have the same name and phone.
     * This defines duplicate detection as per MVP requirements (case-insensitive name comparison).
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().getNormalizedName().equals(getName().getNormalizedName())
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && studentClass.equals(otherPerson.studentClass)
                && note.equals(otherPerson.note)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, studentClass, note, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("class", studentClass)
                .add("note", note)
                .add("tags", tags)
                .toString();
    }

}
