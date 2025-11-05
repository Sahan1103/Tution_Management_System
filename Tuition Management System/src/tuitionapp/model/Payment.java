package tuitionapp.model;

import java.time.LocalDate;
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

        // Default constructor
        public Payment() {
            this.paymentDate = LocalDate.now();
            this.status = "Pending";
        }


    // Parameterized constructor
    public Payment(String paymentId, String studentId, String studentName,
                   String courseId, String courseName, double amount,
                   LocalDate paymentDate,String paymentMethod,String status, String remarks) {
        this();
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.remarks = remarks;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Business methods
    public void markAsCompleted() {
        this.status = "Completed";
    }

    public void markAsFailed(String reason) {
        this.status = "Failed";
        this.remarks = (this.remarks != null ? this.remarks + "; " : "") + "Failed: " + reason;
    }

    public boolean isCompleted() {
        return "Completed".equals(this.status);
    }

    @Override
    public String toString() {
        return "Payment [ID: " + paymentId +
                ", Student: " + studentName + " (" + studentId + ")" +
                ", Course: " + courseName +
                ", Amount: " + String.format("%.2f", amount) +
                ", Date: " + paymentDate +
                ", Method: " + paymentMethod +
                ", Status: " + status + "]";
    }
}