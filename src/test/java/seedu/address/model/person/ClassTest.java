package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ClassTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Class(null));
    }

    @Test
    public void constructor_invalidClass_throwsIllegalArgumentException() {
        String invalidClass = "K1 A"; // contains space
        assertThrows(IllegalArgumentException.class, () -> new Class(invalidClass));
    }

    @Test
    public void constructor_classNormalization_success() {
        // Case should be preserved as entered
        Class studentClass = new Class("K1A");
        assertEquals("K1A", studentClass.value);

        studentClass = new Class("k1a");
        assertEquals("k1a", studentClass.value);

        studentClass = new Class("Harmony");
        assertEquals("Harmony", studentClass.value);

        studentClass = new Class("Class123");
        assertEquals("Class123", studentClass.value);

        // Leading and trailing spaces should be trimmed
        studentClass = new Class("  K1D  ");
        assertEquals("K1D", studentClass.value);

        studentClass = new Class("  Nursery123  ");
        assertEquals("Nursery123", studentClass.value);
    }

    @Test
    public void isValidClass() {
        // null class
        assertThrows(NullPointerException.class, () -> Class.isValidClass(null));

        // invalid classes
        assertFalse(Class.isValidClass("")); // empty string
        assertFalse(Class.isValidClass(" ")); // spaces only
        assertFalse(Class.isValidClass("K1 A")); // contains space
        assertFalse(Class.isValidClass("K1-A")); // contains dash (not alphanumeric)
        assertFalse(Class.isValidClass("Class@1")); // contains special character
        assertFalse(Class.isValidClass("Pre-K")); // contains dash (not alphanumeric)
        assertFalse(Class.isValidClass("Grade 1")); // contains space
        assertFalse(Class.isValidClass("Class_A")); // contains underscore
        assertFalse(Class.isValidClass("VeryLongClassNameThatExceedsTwentyCharacters")); // exceeds 20 characters
        assertFalse(Class.isValidClass("!@#$%")); // special characters only

        // valid classes (alphanumeric only, up to 20 characters)
        assertTrue(Class.isValidClass("K1A"));
        assertTrue(Class.isValidClass("K1B"));
        assertTrue(Class.isValidClass("K1C"));
        assertTrue(Class.isValidClass("K1D")); // now valid
        assertTrue(Class.isValidClass("K2A"));
        assertTrue(Class.isValidClass("K2B"));
        assertTrue(Class.isValidClass("K2C"));
        assertTrue(Class.isValidClass("Nursery"));
        assertTrue(Class.isValidClass("PreK")); // without dash
        assertTrue(Class.isValidClass("Harmony")); // example from requirements
        assertTrue(Class.isValidClass("Class123"));
        assertTrue(Class.isValidClass("A"));
        assertTrue(Class.isValidClass("123"));
        assertTrue(Class.isValidClass("MaxLengthClass20Char")); // exactly 20 characters

        // mixed case (all valid since we accept any alphanumeric)
        assertTrue(Class.isValidClass("k1a"));
        assertTrue(Class.isValidClass("HARMONY"));
        assertTrue(Class.isValidClass("MixedCaseClass"));
    }

    @Test
    public void equals() {
        Class studentClass = new Class("K1A");

        // same values -> returns true
        assertTrue(studentClass.equals(new Class("K1A")));

        // same object -> returns true
        assertTrue(studentClass.equals(studentClass));

        // null -> returns false
        assertFalse(studentClass.equals(null));

        // different types -> returns false
        assertFalse(studentClass.equals(5.0f));

        // different values -> returns false
        assertFalse(studentClass.equals(new Class("K1D")));
        assertFalse(studentClass.equals(new Class("Harmony")));
    }

    @Test
    public void hashCode_sameClass_sameHashCode() {
        Class class1 = new Class("K1A");
        Class class2 = new Class("K1A");
        assertEquals(class1.hashCode(), class2.hashCode());
    }

    @Test
    public void toString_validClass_returnsCorrectString() {
        Class studentClass = new Class("K2C");
        assertEquals("K2C", studentClass.toString());
    }
}
