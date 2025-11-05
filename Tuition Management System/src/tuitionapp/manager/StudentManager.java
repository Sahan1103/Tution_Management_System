package tuitionapp.manager;

import tuitionapp.model.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * StudentManager class is responsible for managing a collection of Student objects.
 * It handles operations such as adding new students, finding students by ID,
 * and retrieving a list of all students.
 * <p>
 * In a full application, this class would typically interact with a data layer
 * (like a database or the FileManager) to persist the data.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 * @since 2023-11-05
 */
public class StudentManager {

    /**
     * The in-memory storage for all student records.
     * Uses a List for ordered storage, but in a real app, a Map<String, Student>
     * might be better for quick ID lookup.
     */
    private final List<Student> students;

    /**
     * Constructs a new StudentManager and initializes the student storage.
     */
    public StudentManager() {
        this.students = new ArrayList<>();
        // Note: In a real system, you'd call a FileManager here to load initial data.
    }

    // --- Core Management Operations ---

    /**
     * Adds a new student to the management system.
     * <p>
     * Before adding, it checks if a student with the same ID already exists.
     * </p>
     *
     * @param student The {@link Student} object to be added.
     * @return {@code true} if the student was successfully added, {@code false} if a student with the ID already exists.
     */
    public boolean addStudent(Student student) {
        if (findStudentById(student.getStudentId()).isPresent()) {
            return false; // Student ID already exists
        }
        return students.add(student);
    }

    /**
     * Retrieves a student by their unique ID.
     *
     * @param studentId The unique ID of the student to find.
     * @return An {@link Optional} containing the {@link Student} if found,
     * or an empty Optional if no student with the given ID exists.
     */
    public Optional<Student> findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
    }

    /**
     * Removes a student from the system using their ID.
     *
     * @param studentId The ID of the student to remove.
     * @return {@code true} if the student was successfully found and removed, {@code false} otherwise.
     */
    public boolean removeStudent(String studentId) {
        Optional<Student> studentToRemove = findStudentById(studentId);
        if (studentToRemove.isPresent()) {
            return students.remove(studentToRemove.get());
        }
        return false;
    }

    /**
     * Gets an immutable list of all students currently in the system.
     *
     * @return A {@link List} of all {@link Student} objects.
     */
    public List<Student> getAllStudents() {
        // Return a copy to prevent external modification of the internal list
        return new ArrayList<>(students);
    }

    // --- Utility/Reporting Operations ---

    /**
     * Counts the total number of students currently enrolled.
     *
     * @return The count of students.
     */
    public int getStudentCount() {
        return students.size();
    }
}
