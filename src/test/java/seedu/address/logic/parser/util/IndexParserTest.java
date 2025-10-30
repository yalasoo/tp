package seedu.address.logic.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;

public class IndexParserTest {

    @Test
    public void parseIndexes_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> IndexParser.parseIndexes("abc"));
    }

    @Test
    public void parseIndexes_singleIndex_returnsSet() throws Exception {
        Set<Index> result = IndexParser.parseIndexes("1");
        assertEquals(Set.of(Index.fromOneBased(1)), result);
    }

    @Test
    public void parseIndexes_commaSeparated_returnsSet() throws Exception {
        Set<Index> result = IndexParser.parseIndexes("1,2,3");
        assertEquals(3, result.size());
    }

    @Test
    public void parseIndexes_range_returnsSet() throws Exception {
        Set<Index> result = IndexParser.parseIndexes("1-3");
        assertEquals(3, result.size());
    }

    @Test
    public void parseIndexes_mixed_returnsSet() throws Exception {
        Set<Index> result = IndexParser.parseIndexes("1, 3-5, 7");
        assertEquals(5, result.size());
    }

    @Test
    public void parseRange_zeroOrNegative_throwsParseException() {
        assertThrows(ParseException.class, () -> IndexParser.parseRange("5-0"));
        assertThrows(ParseException.class, () -> IndexParser.parseRange("0-5"));
        assertThrows(ParseException.class, () -> IndexParser.parseRange("-1-5"));
        assertThrows(ParseException.class, () -> IndexParser.parseRange("1--2"));
    }

    @Test
    public void parseRange_endLessThanStart_throwsParseException() {
        assertThrows(ParseException.class, () -> IndexParser.parseRange("2-1"));
        assertThrows(ParseException.class, () -> IndexParser.parseRange("3-1"));
        assertThrows(ParseException.class, () -> IndexParser.parseRange("3-1-"));
    }

    @Test
    public void parseSingleIndex_zeroIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> IndexParser.parseSingleIndex("0"));
    }

    @Test
    public void parseSingleIndex_negativeIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> IndexParser.parseSingleIndex("-1"));
    }
}
