package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Class;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_singlePrefix_returnsCorrectMessage() {
        Prefix testPrefix = new Prefix("n/");
        String result = Messages.getErrorMessageForDuplicatePrefixes(testPrefix);

        assertTrue(result.contains(Messages.MESSAGE_DUPLICATE_FIELDS));
        assertTrue(result.contains("n/"));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_multiplePrefixes_returnsCorrectMessage() {
        Prefix namePrefix = new Prefix("n/");
        Prefix phonePrefix = new Prefix("p/");
        Prefix emailPrefix = new Prefix("e/");

        String result = Messages.getErrorMessageForDuplicatePrefixes(namePrefix, phonePrefix, emailPrefix);

        assertTrue(result.contains(Messages.MESSAGE_DUPLICATE_FIELDS));
        assertTrue(result.contains("n/"));
        assertTrue(result.contains("p/"));
        assertTrue(result.contains("e/"));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_duplicatePrefixes_removesActualDuplicates() {
        Prefix namePrefix1 = new Prefix("n/");
        Prefix namePrefix2 = new Prefix("n/");

        String result = Messages.getErrorMessageForDuplicatePrefixes(namePrefix1, namePrefix2);

        assertTrue(result.contains(Messages.MESSAGE_DUPLICATE_FIELDS));
        assertTrue(result.contains("n/"));
        // Should only contain one instance of "n/" since Set removes duplicates
        // Count occurrences of "n/" in the result
        int count = 0;
        int index = 0;
        while ((index = result.indexOf("n/", index)) != -1) {
            count++;
            index += 2; // Move past this occurrence
        }
        assertEquals(1, count, "Should only contain one instance of 'n/' due to Set deduplication");
    }

    @Test
    public void format_personWithAllFields_returnsCorrectFormat() {
        // Create a test person with all fields using valid Singapore phone number
        Name name = new Name("John Doe");
        Phone phone = new Phone("91234567"); // Valid Singapore phone number starting with 9
        Email email = new Email("john@example.com");
        Address address = new Address("123 Main Street");
        Class studentClass = new Class("Nursery"); // Valid kindergarten class format
        Birthday birthday = new Birthday("23-04-1999");
        Note note = new Note("Test note");
        Set<Tag> tags = Set.of(new Tag("student")); // Use valid tag: student or colleague

        Person person = new Person(name, phone, email, address, studentClass, birthday, note, tags, null, null);

        String result = Messages.format(person);

        // Verify all components are in the formatted string
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Phone: 91234567"));
        assertTrue(result.contains("Email: john@example.com"));
        assertTrue(result.contains("Class: NURSERY")); // Class values are stored in uppercase
        assertTrue(result.contains("Tags:"));
        assertTrue(result.contains("student"));
    }

    @Test
    public void format_personWithNoTags_returnsCorrectFormat() {
        // Create a test person with no tags using valid Singapore phone number
        Name name = new Name("Jane Doe");
        Phone phone = new Phone("87654321"); // Valid Singapore phone number starting with 8
        Email email = new Email("jane@example.com");
        Address address = new Address("456 Oak Avenue");
        Class studentClass = new Class("K1A"); // Valid kindergarten class format
        Birthday birthday = new Birthday("28-05-1998");
        Note note = new Note("");
        Set<Tag> tags = Set.of(); // Empty set of tags

        Person person = new Person(name, phone, email, address, studentClass, birthday, note, tags, null, null);

        String result = Messages.format(person);

        // Verify all components are in the formatted string
        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("Phone: 87654321"));
        assertTrue(result.contains("Email: jane@example.com"));
        assertTrue(result.contains("Class: K1A"));
        assertTrue(result.contains("Tags:"));
        // Should end with "Tags: " and nothing after for empty tags
        assertTrue(result.endsWith("Tags: "));
    }

    @Test
    public void constantMessages_haveExpectedValues() {
        // Test that message constants have expected values
        assertEquals("Unknown command", Messages.MESSAGE_UNKNOWN_COMMAND);
        assertEquals("Invalid command format! \n%1$s", Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        assertEquals("The person index provided is invalid", Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertEquals("%1$d persons listed!", Messages.MESSAGE_PERSONS_LISTED_OVERVIEW);
        assertEquals("Multiple values specified for the following single-valued field(s): ",
                Messages.MESSAGE_DUPLICATE_FIELDS);
    }
}
