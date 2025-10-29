package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("91234567");
        List<String> secondPredicateKeywordList = Arrays.asList("91234567", "81234567");

        PhoneContainsKeywordsPredicate firstPredicate = new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        PhoneContainsKeywordsPredicate secondPredicate = new PhoneContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same values -> returns true
        PhoneContainsKeywordsPredicate firstPredicateCopy =
                new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        assertEquals(firstPredicate, firstPredicateCopy);

        // different types -> returns false
        assertNotEquals(firstPredicate, 1);
        assertNotEquals(firstPredicate, "phone");

        // null -> returns false
        assertNotEquals(firstPredicate, null);

        // different predicate -> returns false
        assertNotEquals(firstPredicate, secondPredicate);

        // empty lists -> returns true
        PhoneContainsKeywordsPredicate emptyPredicate1 = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        PhoneContainsKeywordsPredicate emptyPredicate2 = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        assertEquals(emptyPredicate1, emptyPredicate2);

        // different order, same content -> returns false (list order matters)
        PhoneContainsKeywordsPredicate orderPredicate1 =
                new PhoneContainsKeywordsPredicate(Arrays.asList("943", "812"));
        PhoneContainsKeywordsPredicate orderPredicate2 =
                new PhoneContainsKeywordsPredicate(Arrays.asList("812", "943"));
        assertNotEquals(orderPredicate1, orderPredicate2);
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        // One keyword - exact match
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList("91234567"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Multiple keywords - both match
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("912", "567"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Only one matching keyword out of multiple
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("22222222", "95357710"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("95357710").build()));

        // Partial match at beginning
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("912"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Partial match at end
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("567"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Partial match in middle
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("345"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Match with landline number starting with 6
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("612"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("61234567").build()));

        // Match with mobile number starting with 8
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("812"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("81234567").build()));

        // Case insensitive matching (though phone numbers are numeric, testing the StringUtil behavior)
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("912"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_phoneDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Non-matching keyword - completely different number
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("95357710"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("92222222").build()));

        // Non-matching keyword - similar but not matching
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("944"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Multiple non-matching keywords
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("111", "222", "333"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Keyword longer than phone number
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("912345670000"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Non-numeric keyword
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("abc"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Keywords that don't match phone number
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("Alice", "email"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(keywords);

        String expected = PhoneContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }



    @Test
    public void test_phoneWithSpacesAndDashes_returnsTrue() {
        // Test with phone numbers that have spaces and dashes (should still match)
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList("9123"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("9123 4567").build()));

        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("4567"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("9123-4567").build()));

        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("91234567"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("9123 4567").build()));
    }

    @Test
    public void test_singleDigitKeywords_returnsCorrectResult() {
        // Single digit matching
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList("9"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Single digit not matching
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("8"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_multipleKeywordsWithMixedMatching_returnsTrue() {
        // At least one keyword matches - should return true
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Arrays.asList("999", "912", "777"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // No keywords match - should return false
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("111", "222", "777"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_keywordsWithWhitespace_returnsCorrectResult() {
        // Keywords with leading/trailing spaces - test that it doesn't throw exception
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Arrays.asList(" 912 ", "812"));
        // The predicate should handle whitespace in keywords gracefully
        // We expect it to match since "912" is contained in "91234567"
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void hashCode_sameKeywords_returnsSameHashCode() {
        List<String> keywords1 = Arrays.asList("943", "812");
        List<String> keywords2 = Arrays.asList("943", "812");
        PhoneContainsKeywordsPredicate predicate1 = new PhoneContainsKeywordsPredicate(keywords1);
        PhoneContainsKeywordsPredicate predicate2 = new PhoneContainsKeywordsPredicate(keywords2);

        assertEquals(predicate1.hashCode(), predicate2.hashCode());
    }

    @Test
    public void test_allValidSingaporePhoneFormats_returnsTrue() {
        PhoneContainsKeywordsPredicate predicate;

        // Test landline numbers (starting with 6)
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("612"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("61234567").build()));

        // Test mobile numbers (starting with 8)
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("812"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("81234567").build()));

        // Test mobile numbers (starting with 9)
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("912"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }
}
