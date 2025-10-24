package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_invalidTag_throwsIllegalArgumentException() {
        // Invalid tags (not 'student' or 'colleague')
        assertThrows(IllegalArgumentException.class, () -> new Tag("friend"));
        assertThrows(IllegalArgumentException.class, () -> new Tag("family"));
        assertThrows(IllegalArgumentException.class, () -> new Tag("teacher"));
        assertThrows(IllegalArgumentException.class, () -> new Tag("parent"));
        assertThrows(IllegalArgumentException.class, () -> new Tag("random"));
    }

    @Test
    public void constructor_validStudentTag_success() {
        // Valid 'student' tag in various cases
        new Tag("student");
        new Tag("Student");
        new Tag("STUDENT");
        new Tag("sTuDeNt");
    }

    @Test
    public void constructor_validColleagueTag_success() {
        // Valid 'colleague' tag in various cases
        new Tag("colleague");
        new Tag("Colleague");
        new Tag("COLLEAGUE");
        new Tag("CoLlEaGuE");
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("friend")); // not student or colleague
        assertFalse(Tag.isValidTagName("teacher")); // not student or colleague
        assertFalse(Tag.isValidTagName("parent")); // not student or colleague

        // valid tag names (case-insensitive)
        assertTrue(Tag.isValidTagName("student"));
        assertTrue(Tag.isValidTagName("Student"));
        assertTrue(Tag.isValidTagName("STUDENT"));
        assertTrue(Tag.isValidTagName("colleague"));
        assertTrue(Tag.isValidTagName("Colleague"));
        assertTrue(Tag.isValidTagName("COLLEAGUE"));
    }
}
