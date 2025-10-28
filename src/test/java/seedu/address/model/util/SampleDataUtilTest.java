package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_validData_returnsCorrectPersons() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        // Check that we have the expected number of sample persons
        assertEquals(6, samplePersons.length);

        // Check that all persons have valid data
        for (Person person : samplePersons) {
            assertNotNull(person.getName());
            assertNotNull(person.getPhone());
            assertNotNull(person.getEmail());
            assertNotNull(person.getAddress());
            assertNotNull(person.getStudentClass());
            assertNotNull(person.getBirthday());
            assertNotNull(person.getNote());
            assertNotNull(person.getTags());
        }
    }

    @Test
    public void getSamplePersons_validClasses_returnsCorrectClasses() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();
        List<Person> personList = Arrays.asList(samplePersons);

        // Count persons by kindergarten class
        long classK1ACount = personList.stream()
                .filter(person -> "K1A".equals(person.getStudentClass().value))
                .count();
        long classK1BCount = personList.stream()
                .filter(person -> "K1B".equals(person.getStudentClass().value))
                .count();
        long classK1CCount = personList.stream()
                .filter(person -> "K1C".equals(person.getStudentClass().value))
                .count();
        long classK2ACount = personList.stream()
                .filter(person -> "K2A".equals(person.getStudentClass().value))
                .count();
        long classK2BCount = personList.stream()
                .filter(person -> "K2B".equals(person.getStudentClass().value))
                .count();
        long classK2CCount = personList.stream()
                .filter(person -> "K2C".equals(person.getStudentClass().value))
                .count();

        // Verify that we have a good distribution of kindergarten classes
        assertTrue(classK1ACount > 0 || classK1BCount > 0 || classK1CCount > 0
                || classK2ACount > 0 || classK2BCount > 0 || classK2CCount > 0);
    }

    @Test
    public void getSamplePersons_specificPersons_haveCorrectClasses() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        // Test specific persons based on their expected classes
        for (int i = 0; i < samplePersons.length; i++) {
            Person person = samplePersons[i];
            switch (i) {
            case 0:
                assertEquals("K1A", person.getStudentClass().value);
                break;
            case 1:
                assertEquals("K1B", person.getStudentClass().value);
                break;
            case 2:
                assertEquals("K1C", person.getStudentClass().value);
                break;
            case 3:
                assertEquals("K2A", person.getStudentClass().value);
                break;
            case 4:
                assertEquals("K2B", person.getStudentClass().value);
                break;
            case 5:
                assertEquals("K2C", person.getStudentClass().value);
                break;
            default:
                // Additional sample persons beyond the expected 6
                break;
            }
        }
    }
}
