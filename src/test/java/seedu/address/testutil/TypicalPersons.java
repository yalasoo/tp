package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withClass("K1A").withBirthday("15-03-2018")
            .withNote("She likes aardvarks.").withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withClass("K1B").withEmail("johnd@example.com")
            .withPhone("98765432").withBirthday("24-12-2017")
            .withNote("He can't take beer!").withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street")
            .withClass("K1C").withBirthday("08-05-2018").withNote("").withTags("colleague").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withClass("K2A")
            .withBirthday("30-01-2017").withNote("").withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822240")
            .withEmail("werner@example.com").withAddress("michegan ave")
            .withClass("K2B").withBirthday("14-07-2017").withNote("").withTags("student").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824270")
            .withEmail("lydia@example.com").withAddress("little tokyo")
            .withClass("K2C").withBirthday("03-11-2018").withNote("").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824420")
            .withEmail("anna@example.com").withAddress("4th street")
            .withClass("Nursery").withBirthday("19-09-2019").withNote("").build();
    public static final Person GEORGE_DUPLICATE = new PersonBuilder().withName("George Best").withPhone("94824888")
            .withEmail("annaa@example.com").withAddress("4th street")
            .withClass("Nursery").withBirthday("19-09-2019").withNote("").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84824240")
            .withEmail("stefan@example.com").withAddress("little india")
            .withClass("Pre-K").withBirthday("22-04-2020").withNote("").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84821310")
            .withEmail("hans@example.com").withAddress("chicago ave")
            .withClass("K1A").withBirthday("05-12-2018").withNote("").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withClass(VALID_CLASS_AMY).withBirthday(VALID_BIRTHDAY_AMY)
            .withTags(VALID_TAG_FRIEND).withNote("").build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withClass(VALID_CLASS_BOB).withBirthday(VALID_BIRTHDAY_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withNote("").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE, GEORGE_DUPLICATE));
    }
}
