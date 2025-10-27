package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.commons.util.FileUtil;

/**
 * Saves Attendance CSV file into device.
 */
public class AttendanceCsvStorage {

    private static Path dataDir = Paths.get("data");

    /**
     * Saves Attendance CSV file into /data.
     *
     * @param fileCsv A CSV formatted attendance content.
     * @param fileName The name of the CSV file to be saved.
     * @return A String of path where the CSV file is saved.
     * @throws IOException If an error occurs during file saving.
     */
    public static String saveAttendanceCsv(String fileCsv, String fileName) throws IOException {
        FileUtil.createIfMissing(dataDir);

        // Create full file path with unique file name
        String uniqueFileName = getUniqueFileName(dataDir, fileName);
        Path filePath = dataDir.resolve(uniqueFileName);

        FileUtil.writeToFile(filePath, fileCsv);

        return filePath.toAbsolutePath().toString();
    }

    /**
     * Changes where attendance reports are being saved.
     *
     * @param customDataDir The new path to save the attendance reports.
     */
    public static void setDataDirectory(Path customDataDir) {
        dataDir = customDataDir;
    }

    /**
     * Generates a unique file name to prevent overwriting files
     * with the same name.
     *
     * @param directory The path to save the file.
     * @param fileName The name of the file to be saved.
     * @return A String of the unique file name.
     */
    private static String getUniqueFileName(Path directory, String fileName) {
        String baseName = fileName.replaceFirst("[.][^.]+$", ""); // Remove extension
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        Path filePath = directory.resolve(fileName);

        if (!Files.exists(filePath)) {
            return fileName;
        }

        int counter = 2;
        while (Files.exists(directory.resolve(baseName + "(" + counter + ")" + extension))) {
            counter++;
        }

        return baseName + "(" + counter + ")" + extension;
    }
}
