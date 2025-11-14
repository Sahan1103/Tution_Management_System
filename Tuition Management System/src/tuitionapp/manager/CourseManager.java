package tuitionapp.manager;

import tuitionapp.model.*;
import tuitionapp.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CourseManager class handles all business logic related to courses.
 * This includes CRUD operations (Create, Read, Update, Delete) and data management.
 */
public class CourseManager {
    private List<Course> courses;
    private FileManager fileManager;
    private static final String COURSE_FILE = "data/courses.txt";
    private int nextCourseId;

    // Constructor
    public CourseManager() {
        this.courses = new ArrayList<>();
        this.fileManager = new FileManager();
        this.nextCourseId = 1;
        loadCourses();
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * Add a new course to the system
     */
    public boolean addCourse(Course course) {
        try {
            // Check if course ID already exists
            if (findCourseById(course.getCourseId()) != null) {
                System.out.println("Error: Course ID already exists!");
                return false;
            }

            courses.add(course);
            saveCourses();
            System.out.println("Course added successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update an existing course's information
     */
    public boolean updateCourse(String courseId, Course updatedCourse) {
        try {
            Course existingCourse = findCourseById(courseId);
            if (existingCourse == null) {
                System.out.println("Error: Course not found!");
                return false;
            }

            // Update all fields
            existingCourse.setCourseName(updatedCourse.getCourseName());
            existingCourse.setDescription(updatedCourse.getDescription());
            existingCourse.setInstructor(updatedCourse.getInstructor());
            existingCourse.setCourseFee(updatedCourse.getCourseFee());
            existingCourse.setDuration(updatedCourse.getDuration());
            existingCourse.setSchedule(updatedCourse.getSchedule());
            existingCourse.setMaxStudents(updatedCourse.getMaxStudents());

            saveCourses();
            System.out.println("Course updated successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error updating course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a course from the system
     */
    public boolean deleteCourse(String courseId) {
        try {
            Course course = findCourseById(courseId);
            if (course == null) {
                System.out.println("Error: Course not found!");
                return false;
            }

            // Check if students are enrolled
            if (course.getEnrolledStudents() > 0) {
                System.out.println("Warning: Course has " + course.getEnrolledStudents() + " enrolled students!");
                // You can choose to prevent deletion or allow with warning
            }

            courses.remove(course);
            saveCourses();
            System.out.println("Course deleted successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find a course by its ID
     */
    public Course findCourseById(String courseId) {
        return courses.stream()
                .filter(c -> c.getCourseId().equalsIgnoreCase(courseId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    // ==================== SEARCH & FILTER OPERATIONS ====================

    /**
     * Search courses by name (partial match, case-insensitive)
     */
    public List<Course> searchCoursesByName(String name) {
        return courses.stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search courses by instructor
     */
    public List<Course> searchCoursesByInstructor(String instructor) {
        return courses.stream()
                .filter(c -> c.getInstructor().toLowerCase().contains(instructor.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get courses with available seats
     */
    public List<Course> getCoursesWithAvailableSeats() {
        return courses.stream()
                .filter(Course::hasAvailableSeats)
                .collect(Collectors.toList());
    }

    /**
     * Get full courses (no available seats)
     */
    public List<Course> getFullCourses() {
        return courses.stream()
                .filter(Course::isFull)
                .collect(Collectors.toList());
    }

    /**
     * Get courses by fee range
     */
    public List<Course> getCoursesByFeeRange(double minFee, double maxFee) {
        return courses.stream()
                .filter(c -> c.getCourseFee() >= minFee && c.getCourseFee() <= maxFee)
                .collect(Collectors.toList());
    }

    /**
     * Get courses by duration
     */
    public List<Course> getCoursesByDuration(int duration) {
        return courses.stream()
                .filter(c -> c.getDuration() == duration)
                .collect(Collectors.toList());
    }

    // ==================== ENROLLMENT OPERATIONS ====================

    /**
     * Enroll a student in a course
     */
    public boolean enrollStudent(String courseId) {
        try {
            Course course = findCourseById(courseId);
            if (course == null) {
                System.out.println("Error: Course not found!");
                return false;
            }

            if (!course.hasAvailableSeats()) {
                System.out.println("Error: Course is full!");
                return false;
            }

            course.enrollStudent();
            saveCourses();
            System.out.println("Student enrolled successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error enrolling student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Unenroll a student from a course
     */
    public boolean unenrollStudent(String courseId) {
        try {
            Course course = findCourseById(courseId);
            if (course == null) {
                System.out.println("Error: Course not found!");
                return false;
            }

            if (course.getEnrolledStudents() == 0) {
                System.out.println("Error: No students enrolled in this course!");
                return false;
            }

            course.unenrollStudent();
            saveCourses();
            System.out.println("Student unenrolled successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error unenrolling student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Set enrollment count manually
     */
    public boolean setEnrollmentCount(String courseId, int count) {
        try {
            Course course = findCourseById(courseId);
            if (course == null) {
                System.out.println("Error: Course not found!");
                return false;
            }

            if (count < 0 || count > course.getMaxStudents()) {
                System.out.println("Error: Invalid enrollment count!");
                return false;
            }

            course.setEnrolledStudents(count);
            saveCourses();
            System.out.println("Enrollment count updated successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error setting enrollment count: " + e.getMessage());
            return false;
        }
    }

    // ==================== STATISTICS & REPORTS ====================

    /**
     * Get total number of courses
     */
    public int getTotalCourseCount() {
        return courses.size();
    }

    /**
     * Get total enrolled students across all courses
     */
    public int getTotalEnrolledStudents() {
        return courses.stream()
                .mapToInt(Course::getEnrolledStudents)
                .sum();
    }

    /**
     * Get total available seats across all courses
     */
    public int getTotalAvailableSeats() {
        return courses.stream()
                .mapToInt(Course::getAvailableSeats)
                .sum();
    }

    /**
     * Get total revenue potential (all courses at full capacity)
     */
    public double getTotalRevenuePotential() {
        return courses.stream()
                .mapToDouble(c -> c.getCourseFee() * c.getMaxStudents())
                .sum();
    }

    /**
     * Get current revenue (based on enrolled students)
     */
    public double getCurrentRevenue() {
        return courses.stream()
                .mapToDouble(c -> c.getCourseFee() * c.getEnrolledStudents())
                .sum();
    }

    /**
     * Get average course fee
     */
    public double getAverageFee() {
        if (courses.isEmpty()) return 0.0;
        return courses.stream()
                .mapToDouble(Course::getCourseFee)
                .average()
                .orElse(0.0);
    }

    /**
     * Get average occupancy rate across all courses
     */
    public double getAverageOccupancyRate() {
        if (courses.isEmpty()) return 0.0;
        return courses.stream()
                .mapToDouble(Course::getOccupancyRate)
                .average()
                .orElse(0.0);
    }

    /**
     * Get most popular course (highest enrollment)
     */
    public Course getMostPopularCourse() {
        return courses.stream()
                .max((c1, c2) -> Integer.compare(c1.getEnrolledStudents(), c2.getEnrolledStudents()))
                .orElse(null);
    }

    /**
     * Get least popular course (lowest enrollment)
     */
    public Course getLeastPopularCourse() {
        return courses.stream()
                .min((c1, c2) -> Integer.compare(c1.getEnrolledStudents(), c2.getEnrolledStudents()))
                .orElse(null);
    }

    // ==================== FILE OPERATIONS ====================

    /**
     * Save all courses to file
     */
    private void saveCourses() {
        try {
            List<String> lines = courses.stream()
                    .map(Course::toFileString)
                    .collect(Collectors.toList());
            fileManager.writeFile(COURSE_FILE, lines);
        } catch (Exception e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    /**
     * Load all courses from file
     */
    private void loadCourses() {
        try {
            List<String> lines = fileManager.readFile(COURSE_FILE);
            courses.clear();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    try {
                        Course course = Course.fromFileString(line);
                        courses.add(course);

                        // Update next ID counter
                        String idNum = course.getCourseId().replaceAll("[^0-9]", "");
                        if (!idNum.isEmpty()) {
                            int id = Integer.parseInt(idNum);
                            if (id >= nextCourseId) {
                                nextCourseId = id + 1;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing course line: " + line);
                    }
                }
            }

            System.out.println("Loaded " + courses.size() + " courses from file.");
        } catch (Exception e) {
            System.out.println("No existing course data found. Starting fresh.");
        }
    }

    /**
     * Reload courses from file (refresh data)
     */
    public void reloadCourses() {
        loadCourses();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Generate next course ID automatically
     */
    public String generateNextCourseId() {
        return String.format("CRS%04d", nextCourseId++);
    }

    /**
     * Check if course ID exists
     */
    public boolean courseIdExists(String courseId) {
        return findCourseById(courseId) != null;
    }

    /**
     * Validate course data
     */
    public String validateCourse(Course course) {
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            return "Course name cannot be empty!";
        }
        if (course.getInstructor() == null || course.getInstructor().trim().isEmpty()) {
            return "Instructor name cannot be empty!";
        }
        if (course.getCourseFee() < 0) {
            return "Course fee cannot be negative!";
        }
        if (course.getDuration() <= 0) {
            return "Duration must be positive!";
        }
        if (course.getMaxStudents() <= 0) {
            return "Max students must be positive!";
        }
        return null; // No errors
    }

    /**
     * Display all courses (for console testing)
     */
    public void displayAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\n========== ALL COURSES ==========");
        for (Course course : courses) {
            System.out.println(course);
            System.out.println("----------------------------------");
        }
    }

    /**
     * Display courses in list format
     */
    public void displayCoursesListFormat() {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\n========== COURSES LIST ==========");
        System.out.println("Course ID  | Course Name               | Fee        | Enrollment");
        System.out.println("-------------------------------------------------------------------");
        for (Course course : courses) {
            System.out.println(course.toListString());
        }
    }
}
