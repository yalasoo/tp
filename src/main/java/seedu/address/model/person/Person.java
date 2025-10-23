package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
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

    //Extra fields
    private final Favourite favourite;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Class studentClass, Note note, Set<Tag> tags,
                  Favourite favourite) {
        requireAllNonNull(name, phone, email, address, studentClass, note, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.studentClass = studentClass;
        this.note = note;
        this.tags.addAll(tags);
        //favourite could potentially be null in which case set it to default false
        if (favourite == null) {
            this.favourite = new Favourite(false);
        } else {
            this.favourite = favourite;
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
     * Updates the favourite status of this person.
     * @param value Can be true or false. If true means indicate this
     *              person as favourite contact.
     */
    public void updateFavourite(Boolean value) {
        assert value != null;
        favourite.updateFavourite(value);
    }

    /**
     * Retrieves the boolean value of favourite attribute.
     *
     * @return whether Person is in favourites or not.
     */
    public boolean getIsFavBoolean() {
        return favourite.getIsFavouriteBoolean();
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
