package tuitionapp.ui.components;
import tuitionapp.model.Student;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentTableModel provides data for JTable to display students.
 * This class extends AbstractTableModel to work with Swing's JTable component.
 */
public class StudentTableModel extends AbstractTableModel {
    // Column names for the table
    private final String[] columnNames = {
            "Student ID",
            "Name",
            "Email",
            "Phone",
            "Address",
            "Course",
            "Total Fees",
            "Paid Amount",
            "Balance"
    };

    // Data storage
    private List<Student> students;

    // Constructor
    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    // Constructor with initial data
    public StudentTableModel(List<Student> students) {
        this.students = new ArrayList<>(students);
    }

    // ==================== AbstractTableModel REQUIRED METHODS ====================

    /**
     * Returns the number of rows in the table
     */
    @Override
    public int getRowCount() {
        return students.size();
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
            case 0: // Student ID
            case 1: // Name
            case 2: // Email
            case 3: // Phone
            case 4: // Address
            case 5: // Course
                return String.class;
            case 6: // Total Fees
            case 7: // Paid Amount
            case 8: // Balance
                return Double.class;
            default:
                return String.class;
        }
    }

    /**
     * Returns the value at a specific cell (row, column)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return student.getStudentId();
            case 1:
                return student.getName();
            case 2:
                return student.getEmail();
            case 3:
                return student.getPhoneNumber();
            case 4:
                return student.getAddress();
            case 5:
                return student.getEnrolledCourse();
            case 6:
                return student.getTotalFees();
            case 7:
                return student.getPaidAmount();
            case 8:
                return student.getRemainingBalance();
            default:
                return null;
        }
    }

    /**
     * Makes the table cells non-editable (read-only)
     * If you want editable cells, override this method to return true for specific columns
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Make all cells read-only
    }

    // ==================== CUSTOM METHODS FOR DATA MANIPULATION ====================

    /**
     * Get student at a specific row
     */
    public Student getStudentAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            return students.get(rowIndex);
        }
        return null;
    }

    /**
     * Add a new student to the table
     */
    public void addStudent(Student student) {
        students.add(student);
        int row = students.size() - 1;
        fireTableRowsInserted(row, row); // Notify table that a row was added
    }

    /**
     * Update a student at a specific row
     */
    public void updateStudent(int rowIndex, Student updatedStudent) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.set(rowIndex, updatedStudent);
            fireTableRowsUpdated(rowIndex, rowIndex); // Notify table that row was updated
        }
    }

    /**
     * Remove a student at a specific row
     */
    public void removeStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex); // Notify table that row was deleted
        }
    }

    /**
     * Remove a specific student object
     */
    public void removeStudent(Student student) {
        int index = students.indexOf(student);
        if (index >= 0) {
            removeStudent(index);
        }
    }

    /**
     * Clear all students from the table
     */
    public void clear() {
        int size = students.size();
        if (size > 0) {
            students.clear();
            fireTableRowsDeleted(0, size - 1); // Notify table that all rows were deleted
        }
    }

    /**
     * Set new list of students (replaces all data)
     */
    public void setStudents(List<Student> students) {
        this.students = new ArrayList<>(students);
        fireTableDataChanged(); // Notify table that all data has changed
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Get the number of students
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Check if table is empty
     */
    public boolean isEmpty() {
        return students.isEmpty();
    }

    /**
     * Find row index by student ID
     */
    public int findRowByStudentId(String studentId) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equalsIgnoreCase(studentId)) {
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
     * Get total fees of all students
     */
    public double getTotalFees() {
        return students.stream()
                .mapToDouble(Student::getTotalFees)
                .sum();
    }

    /**
     * Get total paid amount of all students
     */
    public double getTotalPaid() {
        return students.stream()
                .mapToDouble(Student::getPaidAmount)
                .sum();
    }

    /**
     * Get total outstanding balance of all students
     */
    public double getTotalBalance() {
        return students.stream()
                .mapToDouble(Student::getRemainingBalance)
                .sum();
    }
}
