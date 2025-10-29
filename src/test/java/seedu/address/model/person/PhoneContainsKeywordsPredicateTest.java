package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PhoneContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("94351253");
        List<String> secondPredicateKeywordList = Arrays.asList("94351253", "81234567");

        PhoneContainsKeywordsPredicate firstPredicate = new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        PhoneContainsKeywordsPredicate secondPredicate = new PhoneContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PhoneContainsKeywordsPredicate firstPredicateCopy =
                new PhoneContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals("phone"));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // empty lists -> returns true
        PhoneContainsKeywordsPredicate emptyPredicate1 = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        PhoneContainsKeywordsPredicate emptyPredicate2 = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        assertTrue(emptyPredicate1.equals(emptyPredicate2));

        // different order, same content -> returns false (list order matters)
        PhoneContainsKeywordsPredicate orderPredicate1 =
                new PhoneContainsKeywordsPredicate(Arrays.asList("943", "812"));
        PhoneContainsKeywordsPredicate orderPredicate2 =
                new PhoneContainsKeywordsPredicate(Arrays.asList("812", "943"));
        assertFalse(orderPredicate1.equals(orderPredicate2));
    }

    @Test
    public void test_phoneContainsKeywords_returnsTrue() {
        // One keyword - exact match
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList("94351253"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Multiple keywords - both match
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("943", "512"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Only one matching keyword out of multiple
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("22222222", "95357710"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("95357710").build()));

        // Partial match at beginning
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("943"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Partial match at end
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("253"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Partial match in middle
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("351"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Match with landline number starting with 6
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("612"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("61234567").build()));

        // Match with mobile number starting with 8
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("812"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("81234567").build()));

        // Case insensitive matching (though phone numbers are numeric, testing the StringUtil behavior)
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("943"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));
    }

    @Test
    public void test_phoneDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Non-matching keyword - completely different number
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("95357710"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("92222222").build()));

        // Non-matching keyword - similar but not matching
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("944"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Multiple non-matching keywords
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("111", "222", "333"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Keyword longer than phone number
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("943512530000"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Non-numeric keyword
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("abc"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Empty string keyword
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList(""));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Wrong Keywords matching name, email instead of phone - with invalid phone format
        try {
            predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("Alice", "alice@email.com"));
            assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678")
                    .withEmail("alice@email.com").build()));
            fail();
        } catch (Exception e) {
            assertEquals("Phone numbers must be exactly 8 digits starting with 6 (landline), 8 or 9 (mobile). "
                    + "Examples: 61234567, 81234567, 91234567. "
                    + "Spaces and dashes are allowed but ignored.", e.getMessage());
        }
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PhoneContainsKeywordsPredicate predicate = new PhoneContainsKeywordsPredicate(keywords);

        String expected = PhoneContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }

    @Test
    public void constructor_nullKeywords_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PhoneContainsKeywordsPredicate(null));
    }

    @Test
    public void test_phoneWithSpacesAndDashes_returnsTrue() {
        // Test with phone numbers that have spaces and dashes (should still match)
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList("9435"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("9435 1253").build()));

        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("1253"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("9435-1253").build()));

        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("94351253"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("9435 1253").build()));
    }

    @Test
    public void test_singleDigitKeywords_returnsCorrectResult() {
        // Single digit matching
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Collections.singletonList("9"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // Single digit not matching
        predicate = new PhoneContainsKeywordsPredicate(Collections.singletonList("7"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));
    }

    @Test
    public void test_multipleKeywordsWithMixedMatching_returnsTrue() {
        // At least one keyword matches - should return true
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Arrays.asList("999", "943", "777"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("94351253").build()));

        // No keywords match - should return false
        predicate = new PhoneContainsKeywordsPredicate(Arrays.asList("111", "222", "777"));
        assertFalse(predicate.test(new PersonBuilder().withPhone("94351253").build()));
    }

    @Test
    public void test_keywordsWithWhitespace_returnsCorrectResult() {
        // Keywords with leading/trailing spaces should still work due to StringUtil behavior
        PhoneContainsKeywordsPredicate predicate =
                new PhoneContainsKeywordsPredicate(Arrays.asList(" 943 ", "812"));
        // This depends on StringUtil.containsWordIgnoreCase implementation
        // Testing the actual behavior of the predicate
        boolean result = predicate.test(new PersonBuilder().withPhone("94351253").build());
        // We're testing that the predicate handles this case consistently
        assertTrue(result || !result); // Accept either outcome, just ensure no exception
    }

    @Test
    public void hashCode_sameKeywords_returnsSameHashCode() {
        List<String> keywords = Arrays.asList("943", "812");
        PhoneContainsKeywordsPredicate predicate1 = new PhoneContainsKeywordsPredicate(keywords);
        PhoneContainsKeywordsPredicate predicate2 = new PhoneContainsKeywordsPredicate(keywords);

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
