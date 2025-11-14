package tuitionapp.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * Payment class represents a payment transaction in the tuition management system.
 * This class holds all the data related to a payment made by a student.
 */
public class Payment {
    // Private fields (encapsulation)
    private String paymentId;
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;
    private double amount;
    private LocalDate paymentDate;
    private String paymentMethod; // Cash, Card, Bank Transfer, Online
    private String status; // Completed, Pending, Failed, Refunded
    private String remarks;

    // Date formatter for file operations
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Constructor with all fields
    public Payment(String paymentId, String studentId, String studentName, String courseId,
                   String courseName, double amount, LocalDate paymentDate, String paymentMethod,
                   String status, String remarks) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.remarks = remarks;
    }

    // Constructor without remarks (optional field)
    public Payment(String paymentId, String studentId, String studentName, String courseId,
                   String courseName, double amount, LocalDate paymentDate, String paymentMethod,
                   String status) {
        this(paymentId, studentId, studentName, courseId, courseName, amount,
                paymentDate, paymentMethod, status, "");
    }

    // Constructor for new payment (uses current date, default status)
    public Payment(String paymentId, String studentId, String studentName, String courseId,
                   String courseName, double amount, String paymentMethod) {
        this(paymentId, studentId, studentName, courseId, courseName, amount,
                LocalDate.now(), paymentMethod, "Completed", "");
    }

    // Getters
    public String getPaymentId() {
        return paymentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    // Setters
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Business methods
    public boolean isCompleted() {
        return "Completed".equalsIgnoreCase(status);
    }

    public boolean isPending() {
        return "Pending".equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return "Failed".equalsIgnoreCase(status);
    }

    public boolean isRefunded() {
        return "Refunded".equalsIgnoreCase(status);
    }

    public void markAsCompleted() {
        this.status = "Completed";
    }

    public void markAsPending() {
        this.status = "Pending";
    }

    public void markAsFailed() {
        this.status = "Failed";
    }

    public void markAsRefunded() {
        this.status = "Refunded";
    }

    public String getFormattedDate() {
        return paymentDate.format(DATE_FORMATTER);
    }

    public int getYear() {
        return paymentDate.getYear();
    }

    public int getMonth() {
        return paymentDate.getMonthValue();
    }

    // toString method for easy display
    @Override
    public String toString() {
        return String.format(
                "Payment ID: %s\n" +
                        "Student ID: %s\n" +
                        "Student Name: %s\n" +
                        "Course ID: %s\n" +
                        "Course Name: %s\n" +
                        "Amount: Rs. %.2f\n" +
                        "Payment Date: %s\n" +
                        "Payment Method: %s\n" +
                        "Status: %s\n" +
                        "Remarks: %s",
                paymentId, studentId, studentName, courseId, courseName,
                amount, getFormattedDate(), paymentMethod, status,
                remarks.isEmpty() ? "N/A" : remarks
        );
    }

    // Method to convert payment to a file-friendly format (CSV)
    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s,%.2f,%s,%s,%s,%s",
                paymentId,
                studentId,
                studentName.replace(",", ";"),
                courseId,
                courseName.replace(",", ";"),
                amount,
                getFormattedDate(),
                paymentMethod.replace(",", ";"),
                status,
                remarks.replace(",", ";"));
    }

    // Static method to create Payment from file string (CSV)
    public static Payment fromFileString(String fileString) {
        String[] parts = fileString.split(",", -1); // -1 keeps empty strings
        if (parts.length != 10) {
            throw new IllegalArgumentException("Invalid file format. Expected 10 fields, got " + parts.length);
        }

        return new Payment(
                parts[0].trim(),                                    // paymentId
                parts[1].trim(),                                    // studentId
                parts[2].trim().replace(";", ","),                  // studentName
                parts[3].trim(),                                    // courseId
                parts[4].trim().replace(";", ","),                  // courseName
                Double.parseDouble(parts[5].trim()),                // amount
                LocalDate.parse(parts[6].trim(), DATE_FORMATTER),   // paymentDate
                parts[7].trim().replace(";", ","),                  // paymentMethod
                parts[8].trim(),                                    // status
                parts[9].trim().replace(";", ",")                   // remarks
        );
    }

    // Method for displaying payment in a compact list format
    public String toListString() {
        return String.format("%-12s | %-20s | %-15s | Rs. %10.2f | %s | %s",
                paymentId, studentName, courseName, amount, getFormattedDate(), status);
    }

    // Method to get payment receipt format
    public String toReceiptString() {
        return String.format(
                "================== PAYMENT RECEIPT ==================\n" +
                        "Payment ID      : %s\n" +
                        "Date            : %s\n" +
                        "---------------------------------------------------\n" +
                        "Student ID      : %s\n" +
                        "Student Name    : %s\n" +
                        "Course          : %s (%s)\n" +
                        "---------------------------------------------------\n" +
                        "Amount Paid     : Rs. %.2f\n" +
                        "Payment Method  : %s\n" +
                        "Status          : %s\n" +
                        "---------------------------------------------------\n" +
                        "Remarks         : %s\n" +
                        "====================================================",
                paymentId, getFormattedDate(), studentId, studentName,
                courseName, courseId, amount, paymentMethod, status,
                remarks.isEmpty() ? "None" : remarks
        );
    }
}
