package tuitionapp.ui.components;
import tuitionapp.model.Course;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * CourseTableModel provides data for JTable to display courses.
 * This class extends AbstractTableModel to work with Swing's JTable component.
 */
public class CourseTableModel extends AbstractTableModel{
    // Column names for the table
    private final String[] columnNames = {
            "Course ID",
            "Course Name",
            "Description",
            "Instructor",
            "Fee (Rs.)",
            "Duration (months)",
            "Schedule",
            "Max Students",
            "Enrolled",
            "Available Seats",
            "Occupancy %"
    };

    // Data storage
    private List<Course> courses;

    // Constructor
    public CourseTableModel() {
        this.courses = new ArrayList<>();
    }

    // Constructor with initial data
    public CourseTableModel(List<Course> courses) {
        this.courses = new ArrayList<>(courses);
    }

    // ==================== AbstractTableModel REQUIRED METHODS ====================

    /**
     * Returns the number of rows in the table
     */
    @Override
    public int getRowCount() {
        return courses.size();
    }

    /**
     * Returns the number of columns in the table
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the column name for a given column index
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Returns the data type for each column (used for sorting and rendering)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // Course ID
            case 1: // Course Name
            case 2: // Description
            case 3: // Instructor
            case 6: // Schedule
                return String.class;
            case 4: // Fee
            case 10: // Occupancy %
                return Double.class;
            case 5: // Duration
            case 7: // Max Students
            case 8: // Enrolled
            case 9: // Available Seats
                return Integer.class;
            default:
                return String.class;
        }
    }

    /**
     * Returns the value at a specific cell (row, column)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Course course = courses.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return course.getCourseId();
            case 1:
                return course.getCourseName();
            case 2:
                return course.getDescription();
            case 3:
                return course.getInstructor();
            case 4:
                return course.getCourseFee();
            case 5:
                return course.getDuration();
            case 6:
                return course.getSchedule();
            case 7:
                return course.getMaxStudents();
            case 8:
                return course.getEnrolledStudents();
            case 9:
                return course.getAvailableSeats();
            case 10:
                return course.getOccupancyRate();
            default:
                return null;
        }
    }

    /**
     * Makes the table cells non-editable (read-only)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Make all cells read-only
    }

    // ==================== CUSTOM METHODS FOR DATA MANIPULATION ====================

    /**
     * Get course at a specific row
     */
    public Course getCourseAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < courses.size()) {
            return courses.get(rowIndex);
        }
        return null;
    }

    /**
     * Add a new course to the table
     */
    public void addCourse(Course course) {
        courses.add(course);
        int row = courses.size() - 1;
        fireTableRowsInserted(row, row); // Notify table that a row was added
    }

    /**
     * Update a course at a specific row
     */
    public void updateCourse(int rowIndex, Course updatedCourse) {
        if (rowIndex >= 0 && rowIndex < courses.size()) {
            courses.set(rowIndex, updatedCourse);
            fireTableRowsUpdated(rowIndex, rowIndex); // Notify table that row was updated
        }
    }

    /**
     * Remove a course at a specific row
     */
    public void removeCourse(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < courses.size()) {
            courses.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex); // Notify table that row was deleted
        }
    }

    /**
     * Remove a specific course object
     */
    public void removeCourse(Course course) {
        int index = courses.indexOf(course);
        if (index >= 0) {
            removeCourse(index);
        }
    }

    /**
     * Clear all courses from the table
     */
    public void clear() {
        int size = courses.size();
        if (size > 0) {
            courses.clear();
            fireTableRowsDeleted(0, size - 1); // Notify table that all rows were deleted
        }
    }

    /**
     * Set new list of courses (replaces all data)
     */
    public void setCourses(List<Course> courses) {
        this.courses = new ArrayList<>(courses);
        fireTableDataChanged(); // Notify table that all data has changed
    }

    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    /**
     * Get the number of courses
     */
    public int getCourseCount() {
        return courses.size();
    }

    /**
     * Check if table is empty
     */
    public boolean isEmpty() {
        return courses.isEmpty();
    }

    /**
     * Find row index by course ID
     */
    public int findRowByCourseId(String courseId) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equalsIgnoreCase(courseId)) {
                return i;
            }
        }
        return -1; // Not found
    }

    /**
     * Refresh the entire table (useful after external data changes)
     */
    public void refresh() {
        fireTableDataChanged();
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
     * Get average occupancy rate
     */
    public double getAverageOccupancyRate() {
        if (courses.isEmpty()) return 0.0;
        return courses.stream()
                .mapToDouble(Course::getOccupancyRate)
                .average()
                .orElse(0.0);
    }

    /**
     * Get courses with available seats
     */
    public List<Course> getCoursesWithAvailableSeats() {
        List<Course> available = new ArrayList<>();
        for (Course course : courses) {
            if (course.hasAvailableSeats()) {
                available.add(course);
            }
        }
        return available;
    }

    /**
     * Get full courses
     */
    public List<Course> getFullCourses() {
        List<Course> full = new ArrayList<>();
        for (Course course : courses) {
            if (course.isFull()) {
                full.add(course);
            }
        }
        return full;
    }
}
