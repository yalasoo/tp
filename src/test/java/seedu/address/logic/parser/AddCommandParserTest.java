package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.CLASS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_BIRTHDAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CLASS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NOTE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NOTE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BIRTHDAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Class;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("")
                .withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_nameNormalization_success() {
        // Multiple spaces in name should be normalized to single space
        Person expectedPerson = new PersonBuilder().withName("John Doe").withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();

        assertParseSuccess(parser, " n/John    Doe " + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));

        // Leading and trailing spaces should be trimmed
        assertParseSuccess(parser, " n/  John Doe  " + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_phoneNormalization_success() {
        // Phone with spaces should be normalized
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone("98765432")
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();

        assertParseSuccess(parser, NAME_DESC_BOB + " p/9876 5432" + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));

        // Phone with dashes should be normalized
        assertParseSuccess(parser, NAME_DESC_BOB + " p/9876-5432" + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));

        // Phone with mixed spaces and dashes should be normalized
        assertParseSuccess(parser, NAME_DESC_BOB + " p/98 76-54 32" + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_classNormalization_success() {
        // Mixed case class should be normalized to uppercase
        Person expectedNurseryPerson = new PersonBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass("NURSERY")
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/Nursery" + BIRTHDAY_DESC_BOB, new AddCommand(expectedNurseryPerson));

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/nursery" + BIRTHDAY_DESC_BOB, new AddCommand(expectedNurseryPerson));

        Person expectedK1APerson = new PersonBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass("K1A")
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/K1A" + BIRTHDAY_DESC_BOB, new AddCommand(expectedK1APerson));

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/k1a" + BIRTHDAY_DESC_BOB, new AddCommand(expectedK1APerson));

        Person expectedK2CPerson = new PersonBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass("K2C")
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/K2C" + BIRTHDAY_DESC_BOB, new AddCommand(expectedK2CPerson));

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/k2c" + BIRTHDAY_DESC_BOB, new AddCommand(expectedK2CPerson));
    }

    @Test
    public void parse_emailNormalization_success() {
        // Mixed case email should be normalized to lowercase
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail("john.doe@gmail.com").withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + " e/John.Doe@Gmail.Com"
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_validNameFormats_success() {
        // Names with hyphens
        Person expectedPerson = new PersonBuilder().withName("Mary-Jane").withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();
        assertParseSuccess(parser, " n/Mary-Jane" + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));

        // Names with apostrophes
        expectedPerson = new PersonBuilder().withName("O'Connor").withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();
        assertParseSuccess(parser, " n/O'Connor" + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));

        // Names with multiple words, hyphens, and apostrophes
        expectedPerson = new PersonBuilder().withName("Mary-Jane O'Connor Smith").withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();
        assertParseSuccess(parser, " n/Mary-Jane O'Connor Smith" + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_validPhoneNumbers_success() {
        // Phone starting with 8
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone("81234567")
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();
        assertParseSuccess(parser, NAME_DESC_BOB + " p/81234567" + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));

        // Phone starting with 9
        expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone("91234567")
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();
        assertParseSuccess(parser, NAME_DESC_BOB + " p/91234567" + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple classes
        assertParseFailure(parser, CLASS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));

        // multiple birthdays
        assertParseFailure(parser, BIRTHDAY_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BIRTHDAY));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + CLASS_DESC_AMY + BIRTHDAY_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL,
                        PREFIX_PHONE, PREFIX_CLASS, PREFIX_BIRTHDAY));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid class
        assertParseFailure(parser, INVALID_CLASS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));

        // invalid birthday
        assertParseFailure(parser, INVALID_BIRTHDAY_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BIRTHDAY));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // invalid class
        assertParseFailure(parser, validExpectedPersonString + INVALID_CLASS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));

        // invalid birthday
        assertParseFailure(parser, validExpectedPersonString + INVALID_BIRTHDAY_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BIRTHDAY));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_BOB
                        + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB, expectedMessage);

        // missing class prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + VALID_CLASS_BOB + BIRTHDAY_DESC_BOB, expectedMessage);

        // missing birthday prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + VALID_BIRTHDAY_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                + VALID_CLASS_BOB + VALID_BIRTHDAY_BOB, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid class
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + INVALID_CLASS_DESC + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND
                + TAG_DESC_FRIEND, Class.MESSAGE_CONSTRAINTS);

        // invalid birthday
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + INVALID_BIRTHDAY_DESC + TAG_DESC_HUSBAND
                + TAG_DESC_FRIEND, Birthday.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + INVALID_TAG_DESC
                + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_CLASS_DESC + BIRTHDAY_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_optionalNotePresent_success() {
        // Add with note and tags
        Person expectedPersonWithNote = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote(VALID_NOTE_BOB).withTags(VALID_TAG_FRIEND).build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB
                + NOTE_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPersonWithNote));

        // Add with note but no tags
        Person expectedPersonWithNoteNoTags = new PersonBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote(VALID_NOTE_BOB).withTags().build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB + NOTE_DESC_BOB, new AddCommand(expectedPersonWithNoteNoTags));

        // Note field at different positions (before tags)
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CLASS_DESC_BOB
                + BIRTHDAY_DESC_BOB + NOTE_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPersonWithNote));

        // Note field at different positions (after tags)
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB
                + TAG_DESC_FRIEND + NOTE_DESC_BOB, new AddCommand(expectedPersonWithNote));
    }

    @Test
    public void parse_emptyNoteField_success() {
        // Empty note field (explicit empty)
        Person expectedPersonEmptyNote = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday(VALID_BIRTHDAY_BOB).withNote("").withTags(VALID_TAG_FRIEND).build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CLASS_DESC_BOB
                + BIRTHDAY_DESC_BOB + " desc/" + TAG_DESC_FRIEND, new AddCommand(expectedPersonEmptyNote));
    }

    @Test
    public void parse_duplicateNoteFields_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + BIRTHDAY_DESC_BOB;

        // Multiple note fields
        assertParseFailure(parser, validExpectedPersonString + NOTE_DESC_AMY + NOTE_DESC_BOB,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NOTE));
    }

    @Test
    public void parse_birthdayNormalization_success() {
        // Birthday with spaces should be normalized
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withClass(VALID_CLASS_BOB)
                .withBirthday("15-03-2018").withNote("").build();

        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + " b/ 15-03-2018 ", new AddCommand(expectedPerson));
    }
}
