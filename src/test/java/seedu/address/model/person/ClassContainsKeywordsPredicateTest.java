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

public class ClassContainsKeywordsPredicateTest {

    /**
     * Parses {@code userInput} into a {@code ClassContainsKeywordsPredicate}.
     */
    private ClassContainsKeywordsPredicate preparePredicate(String userInput) {
        return new ClassContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        ClassContainsKeywordsPredicate firstPredicate = new ClassContainsKeywordsPredicate(firstPredicateKeywordList);
        ClassContainsKeywordsPredicate secondPredicate = new ClassContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same values -> returns true
        ClassContainsKeywordsPredicate firstPredicateCopy = new
                ClassContainsKeywordsPredicate(firstPredicateKeywordList);
        assertEquals(firstPredicate, firstPredicateCopy);

        // different types -> returns false
        assertNotEquals(firstPredicate, "not a predicate");

        // null -> returns false
        assertNotEquals(firstPredicate, null);

        // different person -> returns false
        assertNotEquals(firstPredicate, secondPredicate);
    }

    @Test
    public void test_classContainsKeywords_returnsTrue() {
        // One keyword
        ClassContainsKeywordsPredicate predicate = new
                ClassContainsKeywordsPredicate(Collections.singletonList("Nursery"));
        assertTrue(predicate.test(new PersonBuilder().withClass("Nursery").build()));

        // Multiple keywords
        predicate = new ClassContainsKeywordsPredicate(Arrays.asList("Nursery", "K1A"));
        assertTrue(predicate.test(new PersonBuilder().withClass("Nursery").build()));

        // Mixed-case keywords
        predicate = new ClassContainsKeywordsPredicate(Arrays.asList("nuRSEry"));
        assertTrue(predicate.test(new PersonBuilder().withClass("Nursery").build()));

        // EP: keywords match name, phone, email and class
        predicate = preparePredicate("91234567 alice@email.com Alice K1A");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("91234567")
                .withEmail("alice@email.com").withClass("K1A").build()));
    }

    @Test
    public void test_classDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        ClassContainsKeywordsPredicate predicate = new ClassContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withClass("K1B").build()));

        // Non-matching keyword
        predicate = new ClassContainsKeywordsPredicate(Collections.singletonList("Nursery"));
        assertFalse(predicate.test(new PersonBuilder().withClass("K2A").build()));

        // Keywords match phone, name, email and address, but does not match class
        predicate = new ClassContainsKeywordsPredicate(Arrays.asList("Alice", "91234567", "alice@email.com",
                "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("91234567")
                .withEmail("alice@email.com").withClass("K2B").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        ClassContainsKeywordsPredicate predicate = new ClassContainsKeywordsPredicate(keywords);

        String expected = ClassContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
