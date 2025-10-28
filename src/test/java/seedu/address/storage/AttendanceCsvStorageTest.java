package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class AttendanceCsvStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAttendanceCsv_validInput_savesFile() throws IOException {
        // Set custom data directory for testing
        AttendanceCsvStorage.setDataDirectory(tempDir);

        String csvContent = "Name,Class,Status\nJohn Doe,K1A,PRESENT";
        String fileName = "test_attendance.csv";

        String filePath = AttendanceCsvStorage.saveAttendanceCsv(csvContent, fileName);

        assertTrue(Files.exists(Path.of(filePath)));
        assertEquals(csvContent, Files.readString(Path.of(filePath)));
    }
}
