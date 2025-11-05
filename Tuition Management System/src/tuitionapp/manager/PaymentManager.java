package tuitionapp.manager;

import tuitionapp.model.Payment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Used for safe retrieval

public class PaymentManager {

    // Storage for all payment records
    private List<Payment> paymentRecords;
    // Simple counter for generating unique payment IDs
    private int nextPaymentId = 1001;

    public void addPayment(Payment payment) {
        paymentRecords.add(payment);
        System.out.println("Payment added: " + payment.getStudentName());
    }


    public PaymentManager() {
        this.paymentRecords = new ArrayList<>();
        // Optional: Add some initial dummy data
        addDummyData();
    }



// --- Core Methods ---

    /**
     * Records a new successful payment.
     */
    public Payment recordNewPayment(String studentId, String studentName, String courseId,
                                    String courseName, double amount, String paymentMethod) {

        // 1. Generate a unique ID
        String newId = "PAY" + nextPaymentId++;

        // 2. Create the new Payment object
        Payment newPayment = new Payment(
                newId,
                studentId,
                studentName,
                courseId,
                courseName,
                amount,
                LocalDate.now(), // Set the current date
                paymentMethod,
                "Completed", // Default status for a successful record
                "Monthly tuition fee"
        );

        // 3. Store the payment record
        paymentRecords.add(newPayment);

        System.out.println("✅ Payment successfully recorded! ID: " + newPayment.getPaymentId());
        return newPayment;
    }
    /**
     * Retrieves a payment by its ID.
     */
    public Optional<Payment> getPaymentById(String paymentId) {
        for (Payment payment : paymentRecords) {
            if (payment.getPaymentId().equals(paymentId)) {
                return Optional.of(payment); // Payment found
            }
        }
        return Optional.empty(); // Payment not found
    }
    /**
     * Retrieves all payment records.
     */
    public List<Payment> getAllPayments() {
        return new ArrayList<>(paymentRecords); // Return a copy for immutability
    }

    /**
     * Retrieves all payments for a specific student.
     */
    public List<Payment> getPaymentsByStudentId(String studentId) {
        List<Payment> studentPayments = new ArrayList<>();
        for (Payment payment : paymentRecords) {
            if (payment.getStudentId().equals(studentId)) {


                studentPayments.add(payment);
            }
        }
        return studentPayments;
    }

// --- Additional Functionality (e.g., for reporting) ---

    /**
     * Calculates the total amount collected.
     */
    public double calculateTotalRevenue() {
        double total = 0.0;
        for (Payment payment : paymentRecords) {
// Only count completed payments
            if (payment.getStatus().equals("Completed")) {
                total += payment.getAmount();
            }
        }
        return total;
    }
    // --- Helper for Initial Testing ---
    private void addDummyData() {
        recordNewPayment("S001", "Alice Smith", "C101", "Advanced Java", 5000.00, "Cash");
        recordNewPayment("S002", "Bob Johnson", "C102", "Web Development", 6500.00, "Card");
        // Decrement the counter because recordNewPayment increments it
        nextPaymentId = 1003;
        System.out.println("--- Dummy Payment Data Initialized ---");
    }
}
