package tuitionapp.model;

import java.io.Serializable;

/**
 * Student class represents a student in the Tuition Management System
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String enrolledCourse;
    private double totalFees;
    private double paidAmount;
    
    // Default Constructor
    public Student(String id, String name1, String contact, int grade) {
    }
    
    // Parameterized Constructor
    public Student(String studentId, String name, String email, String phoneNumber, 
                   String address, String enrolledCourse, double totalFees, double paidAmount) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.enrolledCourse = enrolledCourse;
        this.totalFees = totalFees;
        this.paidAmount = paidAmount;
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
    
    public double getTotalFees() {
        return totalFees;
    }
    
    public double getPaidAmount() {
        return paidAmount;
    }
    
    // Setters
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
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
    
    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
    }
    
    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    // Utility Methods
    
    /**
     * Calculates the remaining balance
     * @return remaining fees to be paid
     */
    public double getRemainingBalance() {
        return totalFees - paidAmount;
    }
    
    /**
     * Makes a payment and updates the paid amount
     * @param amount the payment amount
     * @return true if payment was successful, false if amount exceeds remaining balance
     */
    public boolean makePayment(double amount) {
        if (amount > 0 && amount <= getRemainingBalance()) {
            this.paidAmount += amount;
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the student has fully paid their fees
     * @return true if fees are fully paid, false otherwise
     */
    public boolean isFullyPaid() {
        return paidAmount >= totalFees;
    }
    
    /**
     * Calculates the payment completion percentage
     * @return percentage of fees paid
     */
    public double getPaymentPercentage() {
        if (totalFees == 0) return 0;
        return (paidAmount / totalFees) * 100;
    }
    
    // Override toString for better display
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", enrolledCourse='" + enrolledCourse + '\'' +
                ", totalFees=" + totalFees +
                ", paidAmount=" + paidAmount +
                ", remainingBalance=" + getRemainingBalance() +
                '}';
    }
    
    // Override equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId != null && studentId.equals(student.studentId);
    }
    
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }

    public void setContact(String newContact) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setGrade(int newGrade) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}