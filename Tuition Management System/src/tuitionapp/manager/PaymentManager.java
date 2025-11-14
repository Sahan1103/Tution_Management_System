package tuitionapp.manager;

import tuitionapp.model.*;
import tuitionapp.util.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PaymentManager class handles all business logic related to payments.
 * This includes CRUD operations (Create, Read, Update, Delete) and data management.
 */
public class PaymentManager {
    private List<Payment> payments;
    private FileManager fileManager;
    private static final String PAYMENT_FILE = "data/payments.txt";
    private int nextPaymentId;

    // Constructor
    public PaymentManager() {
        this.payments = new ArrayList<>();
        this.fileManager = new FileManager();
        this.nextPaymentId = 1;
        loadPayments();
    }

    // ==================== CRUD OPERATIONS ====================

    /**
     * Add a new payment to the system
     */
    public boolean addPayment(Payment payment) {
        try {
            // Check if payment ID already exists
            if (findPaymentById(payment.getPaymentId()) != null) {
                System.out.println("Error: Payment ID already exists!");
                return false;
            }

            payments.add(payment);
            savePayments();
            System.out.println("Payment added successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error adding payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update an existing payment's information
     */
    public boolean updatePayment(String paymentId, Payment updatedPayment) {
        try {
            Payment existingPayment = findPaymentById(paymentId);
            if (existingPayment == null) {
                System.out.println("Error: Payment not found!");
                return false;
            }

            // Update fields
            existingPayment.setStudentName(updatedPayment.getStudentName());
            existingPayment.setCourseName(updatedPayment.getCourseName());
            existingPayment.setAmount(updatedPayment.getAmount());
            existingPayment.setPaymentDate(updatedPayment.getPaymentDate());
            existingPayment.setPaymentMethod(updatedPayment.getPaymentMethod());
            existingPayment.setStatus(updatedPayment.getStatus());
            existingPayment.setRemarks(updatedPayment.getRemarks());

            savePayments();
            System.out.println("Payment updated successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a payment from the system
     */
    public boolean deletePayment(String paymentId) {
        try {
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                System.out.println("Error: Payment not found!");
                return false;
            }

            payments.remove(payment);
            savePayments();
            System.out.println("Payment deleted successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find a payment by its ID
     */
    public Payment findPaymentById(String paymentId) {
        return payments.stream()
                .filter(p -> p.getPaymentId().equalsIgnoreCase(paymentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

    // ==================== SEARCH & FILTER OPERATIONS ====================

    /**
     * Get payments by student ID
     */
    public List<Payment> getPaymentsByStudentId(String studentId) {
        return payments.stream()
                .filter(p -> p.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by student name
     */
    public List<Payment> getPaymentsByStudentName(String studentName) {
        return payments.stream()
                .filter(p -> p.getStudentName().toLowerCase().contains(studentName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by course ID
     */
    public List<Payment> getPaymentsByCourseId(String courseId) {
        return payments.stream()
                .filter(p -> p.getCourseId().equalsIgnoreCase(courseId))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by course name
     */
    public List<Payment> getPaymentsByCourseName(String courseName) {
        return payments.stream()
                .filter(p -> p.getCourseName().toLowerCase().contains(courseName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by status
     */
    public List<Payment> getPaymentsByStatus(String status) {
        return payments.stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    /**
     * Get completed payments
     */
    public List<Payment> getCompletedPayments() {
        return payments.stream()
                .filter(Payment::isCompleted)
                .collect(Collectors.toList());
    }

    /**
     * Get pending payments
     */
    public List<Payment> getPendingPayments() {
        return payments.stream()
                .filter(Payment::isPending)
                .collect(Collectors.toList());
    }

    /**
     * Get failed payments
     */
    public List<Payment> getFailedPayments() {
        return payments.stream()
                .filter(Payment::isFailed)
                .collect(Collectors.toList());
    }

    /**
     * Get refunded payments
     */
    public List<Payment> getRefundedPayments() {
        return payments.stream()
                .filter(Payment::isRefunded)
                .collect(Collectors.toList());
    }

    /**
     * Get payments by date
     */
    public List<Payment> getPaymentsByDate(LocalDate date) {
        return payments.stream()
                .filter(p -> p.getPaymentDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by date range
     */
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return payments.stream()
                .filter(p -> !p.getPaymentDate().isBefore(startDate) && !p.getPaymentDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by month and year
     */
    public List<Payment> getPaymentsByMonth(int year, int month) {
        return payments.stream()
                .filter(p -> p.getYear() == year && p.getMonth() == month)
                .collect(Collectors.toList());
    }

    /**
     * Get payments by year
     */
    public List<Payment> getPaymentsByYear(int year) {
        return payments.stream()
                .filter(p -> p.getYear() == year)
                .collect(Collectors.toList());
    }

    /**
     * Get payments by payment method
     */
    public List<Payment> getPaymentsByMethod(String method) {
        return payments.stream()
                .filter(p -> p.getPaymentMethod().equalsIgnoreCase(method))
                .collect(Collectors.toList());
    }

    /**
     * Get payments by amount range
     */
    public List<Payment> getPaymentsByAmountRange(double minAmount, double maxAmount) {
        return payments.stream()
                .filter(p -> p.getAmount() >= minAmount && p.getAmount() <= maxAmount)
                .collect(Collectors.toList());
    }

    // ==================== PAYMENT STATUS OPERATIONS ====================

    /**
     * Mark payment as completed
     */
    public boolean markPaymentAsCompleted(String paymentId) {
        try {
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                System.out.println("Error: Payment not found!");
                return false;
            }

            payment.markAsCompleted();
            savePayments();
            System.out.println("Payment marked as completed!");
            return true;
        } catch (Exception e) {
            System.out.println("Error marking payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mark payment as pending
     */
    public boolean markPaymentAsPending(String paymentId) {
        try {
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                System.out.println("Error: Payment not found!");
                return false;
            }

            payment.markAsPending();
            savePayments();
            System.out.println("Payment marked as pending!");
            return true;
        } catch (Exception e) {
            System.out.println("Error marking payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mark payment as failed
     */
    public boolean markPaymentAsFailed(String paymentId) {
        try {
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                System.out.println("Error: Payment not found!");
                return false;
            }

            payment.markAsFailed();
            savePayments();
            System.out.println("Payment marked as failed!");
            return true;
        } catch (Exception e) {
            System.out.println("Error marking payment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mark payment as refunded
     */
    public boolean markPaymentAsRefunded(String paymentId) {
        try {
            Payment payment = findPaymentById(paymentId);
            if (payment == null) {
                System.out.println("Error: Payment not found!");
                return false;
            }

            payment.markAsRefunded();
            savePayments();
            System.out.println("Payment marked as refunded!");
            return true;
        } catch (Exception e) {
            System.out.println("Error marking payment: " + e.getMessage());
            return false;
        }
    }

    // ==================== STATISTICS & REPORTS ====================

    /**
     * Get total number of payments
     */
    public int getTotalPaymentCount() {
        return payments.size();
    }

    /**
     * Get total amount collected (completed payments only)
     */
    public double getTotalAmountCollected() {
        return payments.stream()
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get total pending amount
     */
    public double getTotalPendingAmount() {
        return payments.stream()
                .filter(Payment::isPending)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get total refunded amount
     */
    public double getTotalRefundedAmount() {
        return payments.stream()
                .filter(Payment::isRefunded)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get total amount for a specific student
     */
    public double getTotalAmountByStudent(String studentId) {
        return payments.stream()
                .filter(p -> p.getStudentId().equalsIgnoreCase(studentId))
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get total amount for a specific course
     */
    public double getTotalAmountByCourse(String courseId) {
        return payments.stream()
                .filter(p -> p.getCourseId().equalsIgnoreCase(courseId))
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get total amount for a specific month
     */
    public double getTotalAmountByMonth(int year, int month) {
        return payments.stream()
                .filter(p -> p.getYear() == year && p.getMonth() == month)
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get total amount for a specific year
     */
    public double getTotalAmountByYear(int year) {
        return payments.stream()
                .filter(p -> p.getYear() == year)
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    /**
     * Get average payment amount
     */
    public double getAveragePaymentAmount() {
        if (payments.isEmpty()) return 0.0;
        return payments.stream()
                .filter(Payment::isCompleted)
                .mapToDouble(Payment::getAmount)
                .average()
                .orElse(0.0);
    }

    /**
     * Get highest payment
     */
    public Payment getHighestPayment() {
        return payments.stream()
                .filter(Payment::isCompleted)
                .max((p1, p2) -> Double.compare(p1.getAmount(), p2.getAmount()))
                .orElse(null);
    }

    /**
     * Get lowest payment
     */
    public Payment getLowestPayment() {
        return payments.stream()
                .filter(Payment::isCompleted)
                .min((p1, p2) -> Double.compare(p1.getAmount(), p2.getAmount()))
                .orElse(null);
    }

    // ==================== FILE OPERATIONS ====================

    /**
     * Save all payments to file
     */
    private void savePayments() {
        try {
            List<String> lines = payments.stream()
                    .map(Payment::toFileString)
                    .collect(Collectors.toList());
            fileManager.writeFile(PAYMENT_FILE, lines);
        } catch (Exception e) {
            System.out.println("Error saving payments: " + e.getMessage());
        }
    }

    /**
     * Load all payments from file
     */
    private void loadPayments() {
        try {
            List<String> lines = fileManager.readFile(PAYMENT_FILE);
            payments.clear();

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    try {
                        Payment payment = Payment.fromFileString(line);
                        payments.add(payment);

                        // Update next ID counter
                        String idNum = payment.getPaymentId().replaceAll("[^0-9]", "");
                        if (!idNum.isEmpty()) {
                            int id = Integer.parseInt(idNum);
                            if (id >= nextPaymentId) {
                                nextPaymentId = id + 1;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing payment line: " + line);
                    }
                }
            }

            System.out.println("Loaded " + payments.size() + " payments from file.");
        } catch (Exception e) {
            System.out.println("No existing payment data found. Starting fresh.");
        }
    }

    /**
     * Reload payments from file (refresh data)
     */
    public void reloadPayments() {
        loadPayments();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Generate next payment ID automatically
     */
    public String generateNextPaymentId() {
        return String.format("PAY%05d", nextPaymentId++);
    }

    /**
     * Check if payment ID exists
     */
    public boolean paymentIdExists(String paymentId) {
        return findPaymentById(paymentId) != null;
    }

    /**
     * Validate payment data
     */
    public String validatePayment(Payment payment) {
        if (payment.getStudentId() == null || payment.getStudentId().trim().isEmpty()) {
            return "Student ID cannot be empty!";
        }
        if (payment.getCourseId() == null || payment.getCourseId().trim().isEmpty()) {
            return "Course ID cannot be empty!";
        }
        if (payment.getAmount() <= 0) {
            return "Payment amount must be positive!";
        }
        if (payment.getPaymentDate() == null) {
            return "Payment date cannot be null!";
        }
        return null; // No errors
    }

    /**
     * Display all payments (for console testing)
     */
    public void displayAllPayments() {
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        System.out.println("\n========== ALL PAYMENTS ==========");
        for (Payment payment : payments) {
            System.out.println(payment);
            System.out.println("----------------------------------");
        }
    }

    /**
     * Display payments in list format
     */
    public void displayPaymentsListFormat() {
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        System.out.println("\n========== PAYMENTS LIST ==========");
        System.out.println("Payment ID   | Student Name         | Course Name     | Amount       | Date       | Status");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        for (Payment payment : payments) {
            System.out.println(payment.toListString());
        }
    }
}
