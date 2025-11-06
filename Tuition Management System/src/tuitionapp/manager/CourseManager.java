package tuitionapp.manager;

import tuitionapp.model.Course;
import tuitionapp.util.FileManager;

import java.util.*;

/**
 * CourseManager class handles all logic related to managing courses.
 * It communicates with FileManager to store and retrieve data from a file.
 */
public class CourseManager {

    // File path for storing course data
    private static final String COURSE_FILE_PATH = "data/courses.txt";

    // In-memory storage of all courses (courseId → Course object)
    private Map<String, Course> courseDatabase;

    // Reference to FileManager for reading/writing files
    private FileManager fileManager;

    /**
     * Constructor — initializes CourseManager with a FileManager.
     * Loads existing courses from the file if available.
     */
    public CourseManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.courseDatabase = new HashMap<>();
        loadData(); // Load courses when the manager starts
    }

    /**
     * Loads course data from the file into the HashMap.
     */
    public void loadData() {
        List<String> lines = fileManager.readFile(COURSE_FILE_PATH);
        courseDatabase.clear();

        for (String line : lines) {
            try {
                Course course = Course.fromFileString(line);
                courseDatabase.put(course.getCourseId(), course);
            } catch (Exception e) {
                System.out.println("⚠️ Skipping invalid course record: " + line);
            }
        }
    }

    /**
     * Saves all current courses to the file.
     */
    public void saveData() {
        List<String> lines = new ArrayList<>();
        for (Course course : courseDatabase.values()) {
            lines.add(course.toFileString());
        }
        fileManager.writeFile(COURSE_FILE_PATH, lines);
    }

    /**
     * Adds a new course to the system.
     * @param course The Course object to add
     * @return true if added, false if course ID already exists
     */
    public boolean addCourse(Course course) {
        if (courseDatabase.containsKey(course.getCourseId())) {
            System.out.println("⚠️ Course ID already exists: " + course.getCourseId());
            return false;
        }

        courseDatabase.put(course.getCourseId(), course);
        saveData();
        System.out.println("✅ Course added successfully!");
        return true;
    }

    /**
     * Finds a course by its ID.
     * @param courseId The ID to search for
     * @return The Course if found, else null
     */
    public Course findCourse(String courseId) {
        return courseDatabase.get(courseId);
    }

    /**
     * Deletes a course by its ID.
     * @param courseId The ID of the course to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteCourse(String courseId) {
        if (courseDatabase.containsKey(courseId)) {
            courseDatabase.remove(courseId);
            saveData();
            System.out.println("🗑️ Course deleted successfully!");
            return true;
        } else {
            System.out.println("⚠️ Course not found: " + courseId);
            return false;
        }
    }

    /**
     * Returns a list of all courses.
     * @return List of Course objects
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courseDatabase.values());
    }

    /**
     * Displays all courses in a readable format.
     */
    public void displayAllCourses() {
        if (courseDatabase.isEmpty()) {
            System.out.println("📭 No courses found.");
            return;
        }

        System.out.println("\n📚 --- List of Courses ---");
        for (Course c : courseDatabase.values()) {
            System.out.println(c.toListString());
        }
    }
}
