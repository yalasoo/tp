package seedu.address.logic.parser.util;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Utility class for parsing index strings into {@code Index} objects.
 * Supports single index, comma-separated indexes, and index ranges.
 */
public class IndexParser {
    /**
     * Parses the given string index into a {@code Set<Index>}.
     *
     * @param strIndexes the given index in string format.
     * @return parsed index(es) in the form of {@code Set<Index>}.
     * @throws ParseException
     */
    public static Set<Index> parseIndexes(String strIndexes) throws ParseException {
        Set<Index> indexes = new HashSet<>();
        String[] parts = strIndexes.split(",");

        for (String part : parts) {
            part = part.trim();

            if (part.contains("-")) {
                indexes.addAll(parseRange(part));
            } else {
                indexes.add(parseSingleIndex(part));
            }
        }

        return indexes;
    }

    /**
     * Parse the given range of index.
     *
     * @param range of index in the form of "startNum-endNum".
     * @return parsed indexes in the form of {@code Set<Index>}.
     * @throws ParseException if an error occurred during parsing.
     */
    public static Set<Index> parseRange(String range) throws ParseException {
        Set<Index> indexes = new HashSet<>();
        String[] bounds = range.split("-");

        if (bounds.length != 2 || bounds[0].isEmpty() || bounds[1].isEmpty()) {
            throw new ParseException("Invalid range format: " + range);
        }

        int start = Integer.parseInt(bounds[0].trim());
        int end = Integer.parseInt(bounds[1].trim());

        if (start <= 0 || end <= 0) {
            throw new ParseException("Index range must contain positive numbers only.");
        }

        for (int i = start; i <= end; i++) {
            indexes.add(Index.fromOneBased(i));
        }

        return indexes;
    }

    /**
     * Parse the given string index.
     *
     * @param strIndex one index in string format.
     * @return Index object.
     * @throws ParseException
     */
    public static Index parseSingleIndex(String strIndex) throws ParseException {
        try {
            int index = Integer.parseInt(strIndex.trim());
            if (index <= 0) {
                throw new ParseException("Index must be a positive number (1, 2, 3, ...).");
            }
            return Index.fromOneBased(index);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid index: " + strIndex);
        }
    }
}
