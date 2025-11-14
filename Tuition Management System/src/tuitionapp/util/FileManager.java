package tuitionapp.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager class handles all file operations for the tuition management system.
 * This includes reading from and writing to text files for data persistence.
 */
public class FileManager {
    /**
     * Read all lines from a file
     * @param filePath Path to the file (e.g., "data/students.txt")
     * @return List of lines from the file
     */
    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try {
            File file = new File(filePath);

            // Create file if it doesn't exist
            if (!file.exists()) {
                createFileWithDirectory(filePath);
                return lines; // Return empty list for new file
            }

            // Read all lines from file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            System.out.println("Error message: " + e.getMessage());
        }

        return lines;
    }

    /**
     * Write lines to a file (overwrites existing content)
     * @param filePath Path to the file
     * @param lines List of lines to write
     */
    public void writeFile(String filePath, List<String> lines) {
        try {
            File file = new File(filePath);

            // Create file and directories if they don't exist
            if (!file.exists()) {
                createFileWithDirectory(filePath);
            }

            // Write all lines to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false)); // false = overwrite
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("Error writing to file: " + filePath);
            System.out.println("Error message: " + e.getMessage());
        }
    }

    /**
     * Append lines to a file (adds to existing content)
     * @param filePath Path to the file
     * @param lines List of lines to append
     */
    public void appendFile(String filePath, List<String> lines) {
        try {
            File file = new File(filePath);

            // Create file and directories if they don't exist
            if (!file.exists()) {
                createFileWithDirectory(filePath);
            }

            // Append lines to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true)); // true = append
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("Error appending to file: " + filePath);
            System.out.println("Error message: " + e.getMessage());
        }
    }

    /**
     * Append a single line to a file
     * @param filePath Path to the file
     * @param line Line to append
     */
    public void appendLine(String filePath, String line) {
        List<String> lines = new ArrayList<>();
        lines.add(line);
        appendFile(filePath, lines);
    }

    /**
     * Delete a file
     * @param filePath Path to the file
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error deleting file: " + filePath);
            System.out.println("Error message: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a file exists
     * @param filePath Path to the file
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Create a file with its parent directories
     * @param filePath Path to the file
     */
    private void createFileWithDirectory(String filePath) {
        try {
            File file = new File(filePath);

            // Create parent directories if they don't exist
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Create the file
            file.createNewFile();

        } catch (IOException e) {
            System.out.println("Error creating file: " + filePath);
            System.out.println("Error message: " + e.getMessage());
        }
    }

    /**
     * Clear all content from a file
     * @param filePath Path to the file
     */
    public void clearFile(String filePath) {
        writeFile(filePath, new ArrayList<>());
    }

    /**
     * Count lines in a file
     * @param filePath Path to the file
     * @return Number of lines in the file
     */
    public int countLines(String filePath) {
        return readFile(filePath).size();
    }

    /**
     * Read a specific line from a file (0-indexed)
     * @param filePath Path to the file
     * @param lineNumber Line number to read (0-indexed)
     * @return The line content, or null if line doesn't exist
     */
    public String readLine(String filePath, int lineNumber) {
        List<String> lines = readFile(filePath);
        if (lineNumber >= 0 && lineNumber < lines.size()) {
            return lines.get(lineNumber);
        }
        return null;
    }

    /**
     * Search for lines containing a specific text
     * @param filePath Path to the file
     * @param searchText Text to search for
     * @return List of lines containing the search text
     */
    public List<String> searchInFile(String filePath, String searchText) {
        List<String> results = new ArrayList<>();
        List<String> lines = readFile(filePath);

        for (String line : lines) {
            if (line.toLowerCase().contains(searchText.toLowerCase())) {
                results.add(line);
            }
        }

        return results;
    }

    /**
     * Create a backup of a file
     * @param filePath Path to the original file
     * @return true if backup created successfully, false otherwise
     */
    public boolean createBackup(String filePath) {
        try {
            if (!fileExists(filePath)) {
                return false;
            }

            String backupPath = filePath + ".backup";
            List<String> lines = readFile(filePath);
            writeFile(backupPath, lines);
            return true;

        } catch (Exception e) {
            System.out.println("Error creating backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get file size in bytes
     * @param filePath Path to the file
     * @return File size in bytes, or -1 if file doesn't exist
     */
    public long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        }
        return -1;
    }
}
