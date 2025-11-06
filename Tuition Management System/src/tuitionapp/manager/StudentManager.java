package tuitionapp.manager;

import java.util.HashMap;
import java.util.Map;
import tuitionapp.model.Student;
import tuitionapp.util.FileManager;

public class StudentManager {

    private Map<String, Student> studentDatabase;
    private final FileManager fileManager;

    // --- Constructor (Dependency Injection) ---
    public StudentManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.studentDatabase = new HashMap<>();
    }

    // ----------------------------------------------------------------------
    // I/O METHODS (Save to File, Load from File)
    // ----------------------------------------------------------------------

    /** Loads student data from the file system using the FileManager. */
    public void loadData() {
        this.studentDatabase = fileManager.loadStudents();
        System.out.println("Loaded " + studentDatabase.size() + " students.");
    }

    /** Saves the current in-memory database to the file system. */
    public void saveData() {
        fileManager.saveStudents(this.studentDatabase);
    }

    // ----------------------------------------------------------------------
    // CRUD OPERATIONS
    // ----------------------------------------------------------------------

    /** * ADD DETAILS (Create)
     * Adds a new student to the database and saves the changes.
     */
    public void createStudent(String id, String name, String contact, int grade) {
        // Validation: Check for duplicates
        if (studentDatabase.containsKey(id)) {
            System.err.println("ERROR: Student ID " + id + " already exists. Cannot add.");
            return;
        }

        // 1. Create and add to in-memory database
        Student newStudent = new Student(id, name, contact, grade);
        studentDatabase.put(id, newStudent);

        // 2. Save the changes to the file
        saveData();
        System.out.println("SUCCESS: Student " + name + " added.");
    }

    /** * SEARCH STUDENT (Read - Single)
     * Finds and returns a Student object by ID. Returns null if not found.
     */
    public Student findStudent(String id) {
        return studentDatabase.get(id);
    }

    /** * EDIT DETAILS (Update)
     * Finds a student and updates their name, contact, and grade, then saves.
     */
    public void editStudentDetails(String id, String newName, String newContact, int newGrade) {
        Student studentToEdit = studentDatabase.get(id);

        // Validation
        if (studentToEdit == null) {
            System.err.println("ERROR: Student ID " + id + " not found. Cannot edit.");
            return;
        }

        // 1. Update the fields
        studentToEdit.setName(newName);
        studentToEdit.setContact(newContact);
        studentToEdit.setGrade(newGrade);

        // 2. Save the changes to the file
        saveData();
        System.out.println("SUCCESS: Details for Student ID " + id + " updated.");
    }

    /** * REMOVE STUDENT (Delete)
     * Removes a student by ID and saves the changes.
     */
    public void deleteStudent(String id) {
        // Validation
        if (!studentDatabase.containsKey(id)) {
            System.err.println("ERROR: Student ID " + id + " not found. Cannot delete.");
            return;
        }

        // 1. Remove from the map
        studentDatabase.remove(id);

        // 2. Save the changes to the file
        saveData();
        System.out.println("SUCCESS: Student with ID " + id + " deleted.");
    }

    /** Gets the entire map of all students. */
    public Map<String, Student> getAllStudents() {
        return this.studentDatabase;
    }
}