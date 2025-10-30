package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.AttendanceCommand.AttendanceStatus;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    private static Logger logger = LogsCenter.getLogger(Person.class);

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Birthday birthday;
    private final Note note;

    // Data fields
    private final Class studentClass;
    private final Set<Tag> tags = new HashSet<>();
    private final Attendance attendance;

    // Extra fields
    private final Favourite favourite;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Class studentClass,
                  Birthday birthday, Note note, Set<Tag> tags, Attendance attendance, Favourite favourite) {
        requireAllNonNull(name, phone, email, address, studentClass, birthday, note, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentClass = studentClass;
        this.birthday = birthday;
        this.note = note;
        this.tags.addAll(tags);

        // only add attendance for student
        if (tags.contains(new Tag("student"))) {
            this.attendance = (attendance != null) ? attendance : new Attendance();
        } else {
            this.attendance = null;
        }

        // favourite could potentially be null in which case set it to default false
        if (favourite == null) {
            this.favourite = new Favourite(false);
            logger.info("favourite was null for " + name + "so we set it to false");
        } else {
            this.favourite = favourite;
            logger.info("favourite was available for " + name + " so we set it to"
                    + favourite.getIsFavouriteBoolean());
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

    public Birthday getBirthday() {
        return birthday;
    }

    public Note getNote() {
        return note;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public Favourite getFavouriteStatus() {
        return favourite;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Marks the attendance of this person object.
     *
     * @param date When does this attendance apply.
     * @param status What is the status of this attendance.
     * @return False if date is not a valid attendance date, true otherwise.
     */
    public Boolean markAttendance(LocalDate date, AttendanceStatus status)
            throws CommandException {
        assert date != null;
        assert status != null;

        if (!validAttendanceDate(date) || attendance == null) {
            return false;
        }

        attendance.markAttendance(date, status);
        return true;
    }

    /**
     * Unmarks the attendance of this person object.
     *
     * @param date When does this attendance apply.
     * @return False if date is not a valid attendance date, true otherwise.
     */
    public Boolean unmarkAttendance(LocalDate date) {
        assert date != null;

        if (!validAttendanceDate(date) || attendance == null) {
            return false;
        }

        attendance.unmarkAttendance(date);
        return true;
    }

    /**
     * Checks whether the given date is a valid attendance date.
     * A valid attendance date must be within person's born date and today's date.
     *
     * @param date
     * @return False if date before born date or after born date.
     */
    private boolean validAttendanceDate(LocalDate date) {
        int afterToday = date.compareTo(LocalDate.now());

        if (birthday.isBeforeBirthday(date) || afterToday > 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns the attendance record of caller if they are
     * a student (attendance != null).
     *
     * @return The attendance record of caller.
     */
    public Map<LocalDate, AttendanceStatus> getAttendanceRecords() {
        return (attendance != null) ? attendance.getAttendanceRecords() : Collections.emptyMap();
    }

    /**
     * Updates the favourite status of this person.
     *
     * @param value Can be true or false. If true, it indicates that this
     *              person is a favourite contact.
     */
    public void updateFavourite(Boolean value) {
        assert value != null;
        favourite.updateFavourite(value);
    }

    /**
     * Retrieves the boolean value of favourite attribute.
     *
     * @return Whether Person is in favourites or not.
     */
    public boolean getIsFavBoolean() {
        return favourite.getIsFavouriteBoolean();
    }

    /**
     * Retrieves the BooleanProperty value of favourite.
     *
     * @return BooleanProperty The favourite value.
     */
    public BooleanProperty getFavBooleanProperty() {
        return favourite.getFavBooleanProperty();
    }

    /**
     * Returns true if both persons are considered the same based on their contact type.
     * Contacts with different tags (student vs colleague) are never considered duplicates.
     * For colleagues: Same phone number OR same email address (different names allowed)
     * For students: Same name AND phone number (allows different names with same phone for emergency contacts)
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }

        // Different contact types are never considered duplicates
        // This allows a student and colleague to have the same information
        if (this.isStudent() != otherPerson.isStudent()
                || this.isColleague() != otherPerson.isColleague()) {
            return false;
        }

        // For colleagues: Check phone number conflict and email conflict only
        // Colleagues can have the same name but must have unique phone numbers and emails
        if (this.isColleague() && otherPerson.isColleague()) {
            // Same phone (not allowed for colleagues - each should have unique phone)
            boolean samePhone = otherPerson.getPhone().equals(getPhone());

            // Same email (not allowed for colleagues - each should have unique email)
            boolean sameEmail = otherPerson.getEmail().equals(getEmail());

            return samePhone || sameEmail;
        }

        // For students: Only check name and phone (allows different names with same phone)
        if (this.isStudent() && otherPerson.isStudent()) {
            return otherPerson.getName().getNormalizedName().equals(getName().getNormalizedName())
                    && otherPerson.getPhone().equals(getPhone());
        }

        // Should not reach here, but return false as safe default
        return false;
    }

    /**
     * Returns true if person has a student tag.
     */
    public boolean isStudent() {
        return tags.stream().anyMatch(tag -> tag.isStudent());
    }

    /**
     * Returns true if person has a colleague tag.
     */
    public boolean isColleague() {
        return tags.stream().anyMatch(tag -> tag.isColleague());
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
                && birthday.equals(otherPerson.birthday)
                && note.equals(otherPerson.note)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, studentClass, birthday, note, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("class", studentClass)
                .add("birthday", birthday)
                .add("note", note)
                .add("tags", tags)
                .toString();
    }

}
