package tuitionapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Student class represents a student in the tuition management system.
 * This class holds all the data related to a student.
 * NOW SUPPORTS MULTIPLE COURSES!
 */
public class Student {
    // Private fields (encapsulation)
    private String studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String enrolledCourse; // Comma-separated course names (for display)
    private List<String> courseIds; // List of course IDs
    private double totalFees;
    private double paidAmount;

    // Constructor with all fields (including course IDs)
    public Student(String studentId, String name, String email, String phoneNumber,
                   String address, String enrolledCourse, List<String> courseIds,
                   double totalFees, double paidAmount) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.enrolledCourse = enrolledCourse;
        this.courseIds = courseIds != null ? new ArrayList<>(courseIds) : new ArrayList<>();
        this.totalFees = totalFees;
        this.paidAmount = paidAmount;
    }

    // Constructor with single course (backward compatibility)
    public Student(String studentId, String name, String email, String phoneNumber,
                   String address, String enrolledCourse, double totalFees, double paidAmount) {
        this(studentId, name, email, phoneNumber, address, enrolledCourse,
                new ArrayList<>(), totalFees, paidAmount);
    }

    // Constructor without payment details (for new students)
    public Student(String studentId, String name, String email, String phoneNumber,
                   String address, String enrolledCourse) {
        this(studentId, name, email, phoneNumber, address, enrolledCourse,
                new ArrayList<>(), 0.0, 0.0);
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEnrolledCourse() {
        return enrolledCourse;
    }

    public List<String> getCourseIds() {
        return new ArrayList<>(courseIds);
    }

    public double getTotalFees() {
        return totalFees;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEnrolledCourse(String enrolledCourse) {
        this.enrolledCourse = enrolledCourse;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds != null ? new ArrayList<>(courseIds) : new ArrayList<>();
    }

    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    // Business methods
    public double getRemainingBalance() {
        return totalFees - paidAmount;
    }

    public void makePayment(double amount) {
        this.paidAmount += amount;
    }

    public boolean hasOutstandingBalance() {
        return getRemainingBalance() > 0;
    }

    // toString method for easy display
    @Override
    public String toString() {
        return String.format(
                "Student ID: %s\n" +
                        "Name: %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n" +
                        "Address: %s\n" +
                        "Enrolled Courses: %s\n" +
                        "Total Fees: Rs. %.2f\n" +
                        "Paid Amount: Rs. %.2f\n" +
                        "Remaining Balance: Rs. %.2f",
                studentId, name, email, phoneNumber, address, enrolledCourse,
                totalFees, paidAmount, getRemainingBalance()
        );
    }

    // Method to convert student to a file-friendly format (CSV)
    public String toFileString() {
        // Convert course IDs list to pipe-separated string
        String courseIdsStr = String.join("|", courseIds);

        return String.format("%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f",
                studentId, name, email, phoneNumber, address,
                enrolledCourse, courseIdsStr, totalFees, paidAmount);
    }

    // Static method to create Student from file string (CSV)
    public static Student fromFileString(String fileString) {
        String[] parts = fileString.split(",", -1); // -1 keeps empty strings

        if (parts.length == 8) {
            // Old format (backward compatibility)
            return new Student(
                    parts[0].trim(),                    // studentId
                    parts[1].trim(),                    // name
                    parts[2].trim(),                    // email
                    parts[3].trim(),                    // phoneNumber
                    parts[4].trim(),                    // address
                    parts[5].trim(),                    // enrolledCourse
                    Double.parseDouble(parts[6].trim()), // totalFees
                    Double.parseDouble(parts[7].trim())  // paidAmount
            );
        } else if (parts.length == 9) {
            // New format with course IDs
            List<String> courseIds = new ArrayList<>();
            if (!parts[6].trim().isEmpty()) {
                String[] ids = parts[6].trim().split("\\|");
                for (String id : ids) {
                    courseIds.add(id.trim());
                }
            }

            return new Student(
                    parts[0].trim(),                    // studentId
                    parts[1].trim(),                    // name
                    parts[2].trim(),                    // email
                    parts[3].trim(),                    // phoneNumber
                    parts[4].trim(),                    // address
                    parts[5].trim(),                    // enrolledCourse
                    courseIds,                          // courseIds
                    Double.parseDouble(parts[7].trim()), // totalFees
                    Double.parseDouble(parts[8].trim())  // paidAmount
            );
        } else {
            throw new IllegalArgumentException("Invalid file format. Expected 8 or 9 fields, got " + parts.length);
        }
    }
}