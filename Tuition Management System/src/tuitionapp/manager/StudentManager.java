package tuitionapp.manager;

import tuitionapp.model.*;
import tuitionapp.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StudentManager class handles all business logic related to students.
 * This includes CRUD operations (Create, Read, Update, Delete) and data management.
 */
public class StudentManager {
    private List<Student> students;
    private FileManager fileManager;
    private static final String STUDENT_FILE = "data/students.txt";
    private int nextStudentId;

    // Constructor
    public StudentManager() {
        this.students = new ArrayList<>();
        this.fileManager = new FileManager();
        this.nextStudentId = 1;
        loadStudents();
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * Add a new student to the system
     */
    public boolean addStudent(Student student) {
        try {
            // Check if student ID already exists
            if (findStudentById(student.getStudentId()) != null) {
                System.out.println("Error: Student ID already exists!");
                return false;
            }

            students.add(student);
            saveStudents();
            System.out.println("Student added successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update an existing student's information
     */
    public boolean updateStudent(String studentId, Student updatedStudent) {
        try {
            Student existingStudent = findStudentById(studentId);
            if (existingStudent == null) {
                System.out.println("Error: Student not found!");
                return false;
            }

            // Update all fields
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
            existingStudent.setAddress(updatedStudent.getAddress());
            existingStudent.setEnrolledCourse(updatedStudent.getEnrolledCourse());
            existingStudent.setTotalFees(updatedStudent.getTotalFees());
            existingStudent.setPaidAmount(updatedStudent.getPaidAmount());

            saveStudents();
            System.out.println("Student updated successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a student from the system
     */
    public boolean deleteStudent(String studentId) {
        try {
            Student student = findStudentById(studentId);
            if (student == null) {
                System.out.println("Error: Student not found!");
                return false;
            }

            students.remove(student);
            saveStudents();
            System.out.println("Student deleted successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find a student by their ID
     */
    public Student findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equalsIgnoreCase(studentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // ==================== SEARCH & FILTER OPERATIONS ====================

    /**
     * Search students by name (partial match, case-insensitive)
     */
    public List<Student> searchStudentsByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search students by course
     */
    public List<Student> searchStudentsByCourse(String courseName) {
        return students.stream()
                .filter(s -> s.getEnrolledCourse().toLowerCase().contains(courseName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get students with outstanding balance
     */
    public List<Student> getStudentsWithOutstandingBalance() {
        return students.stream()
                .filter(Student::hasOutstandingBalance)
                .collect(Collectors.toList());
    }

    /**
     * Get students who have paid in full
     */
    public List<Student> getStudentsWithFullPayment() {
        return students.stream()
                .filter(s -> !s.hasOutstandingBalance())
                .collect(Collectors.toList());
    }

    /**
     * Search students by email
     */
    public Student findStudentByEmail(String email) {
        return students.stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Search students by phone number
     */
    public Student findStudentByPhone(String phoneNumber) {
        return students.stream()
                .filter(s -> s.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(null);
    }

    // ==================== PAYMENT OPERATIONS ====================

    /**
     * Record a payment for a student
     */
    public boolean recordPayment(String studentId, double amount) {
        try {
            Student student = findStudentById(studentId);
            if (student == null) {
                System.out.println("Error: Student not found!");
                return false;
            }

            if (amount <= 0) {
                System.out.println("Error: Payment amount must be positive!");
                return false;
            }

            student.makePayment(amount);
            saveStudents();
            System.out.println("Payment recorded successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error recording payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Set total fees for a student
     */
    public boolean setStudentFees(String studentId, double totalFees) {
        try {
            Student student = findStudentById(studentId);
            if (student == null) {
                System.out.println("Error: Student not found!");
                return false;
            }

            if (totalFees < 0) {
                System.out.println("Error: Fees cannot be negative!");
                return false;
            }

            student.setTotalFees(totalFees);
            saveStudents();
            System.out.println("Fees updated successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error setting fees: " + e.getMessage());
            return false;
        }
    }

    // ==================== STATISTICS & REPORTS ====================

    /**
     * Get total number of students
     */
    public int getTotalStudentCount() {
        return students.size();
    }

    /**
     * Get total outstanding balance across all students
     */
    public double getTotalOutstandingBalance() {
        return students.stream()
                .mapToDouble(Student::getRemainingBalance)
                .sum();
    }

    /**
     * Get total fees collected
     */
    public double getTotalFeesCollected() {
        return students.stream()
                .mapToDouble(Student::getPaidAmount)
                .sum();
    }

    /**
     * Get total expected revenue
     */
    public double getTotalExpectedRevenue() {
        return students.stream()
                .mapToDouble(Student::getTotalFees)
                .sum();
    }

    /**
     * Get collection percentage
     */
    public double getCollectionPercentage() {
        double expected = getTotalExpectedRevenue();
        if (expected == 0) return 0.0;
        return (getTotalFeesCollected() / expected) * 100;
    }

    // ==================== FILE OPERATIONS ====================

    /**
     * Save all students to file
     */
    private void saveStudents() {
        try {
            List<String> lines = students.stream()
                    .map(Student::toFileString)
                    .collect(Collectors.toList());
            fileManager.writeFile(STUDENT_FILE, lines);
        } catch (Exception e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    /**
     * Load all students from file
     */
    private void loadStudents() {
        try {
            List<String> lines = fileManager.readFile(STUDENT_FILE);
            students.clear();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    try {
                        Student student = Student.fromFileString(line);
                        students.add(student);

                        // Update next ID counter
                        String idNum = student.getStudentId().replaceAll("[^0-9]", "");
                        if (!idNum.isEmpty()) {
                            int id = Integer.parseInt(idNum);
                            if (id >= nextStudentId) {
                                nextStudentId = id + 1;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing student line: " + line);
                    }
                }
            }

            System.out.println("Loaded " + students.size() + " students from file.");
        } catch (Exception e) {
            System.out.println("No existing student data found. Starting fresh.");
        }
    }

    /**
     * Reload students from file (refresh data)
     */
    public void reloadStudents() {
        loadStudents();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Generate next student ID automatically
     */
    public String generateNextStudentId() {
        return String.format("STU%04d", nextStudentId++);
    }

    /**
     * Check if student ID exists
     */
    public boolean studentIdExists(String studentId) {
        return findStudentById(studentId) != null;
    }

    /**
     * Check if email is already registered
     */
    public boolean emailExists(String email) {
        return findStudentByEmail(email) != null;
    }

    /**
     * Validate student data
     */
    public String validateStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Name cannot be empty!";
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            return "Email cannot be empty!";
        }
        if (!student.getEmail().contains("@")) {
            return "Invalid email format!";
        }
        if (student.getPhoneNumber() == null || student.getPhoneNumber().trim().isEmpty()) {
            return "Phone number cannot be empty!";
        }
        return null; // No errors
    }

    /**
     * Display all students (for console testing)
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n========== ALL STUDENTS ==========");
        for (Student student : students) {
            System.out.println(student);
            System.out.println("----------------------------------");
        }
    }
}
