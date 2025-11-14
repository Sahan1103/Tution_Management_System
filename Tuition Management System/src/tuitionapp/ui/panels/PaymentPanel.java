package tuitionapp.ui.panels;
import tuitionapp.model.Payment;
import tuitionapp.model.Student;
import tuitionapp.model.Course;
import tuitionapp.manager.PaymentManager;
import tuitionapp.manager.StudentManager;
import tuitionapp.manager.CourseManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * PaymentPanel - Panel for managing payments
 */
public class PaymentPanel extends JPanel{
    private PaymentManager paymentManager;
    private StudentManager studentManager;
    private CourseManager courseManager;
    private JFrame parentFrame;

    // Table components
    private JTable paymentsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    // Buttons
    private JButton btnRecordPayment;
    private JButton btnViewDetails;
    private JButton btnDeletePayment;
    private JButton btnRefresh;
    private JButton btnPrintReceipt;

    // Search components
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClearSearch;
    private JComboBox<String> cmbSearchType;

    // Filter components
    private JComboBox<String> cmbStatusFilter;
    private JButton btnApplyFilter;

    // Statistics labels
    private JLabel lblTotalPayments;
    private JLabel lblTotalCollected;
    private JLabel lblTotalPending;
    private JLabel lblTotalRefunded;

    /**
     * Constructor
     */
    public PaymentPanel(JFrame parentFrame, PaymentManager paymentManager,
                        StudentManager studentManager, CourseManager courseManager) {
        this.parentFrame = parentFrame;
        this.paymentManager = paymentManager;
        this.studentManager = studentManager;
        this.courseManager = courseManager;

        initComponents();
        layoutComponents();
        setupEventHandlers();
        loadPayments();
        updateStatistics();
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        // Table model with column names
        String[] columnNames = {
                "Payment ID", "Date", "Student ID", "Student Name",
                "Course", "Amount (Rs.)", "Method", "Status", "Remarks"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        paymentsTable = new JTable(tableModel);
        paymentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentsTable.setRowHeight(25);
        paymentsTable.getTableHeader().setReorderingAllowed(false);

        // Adjust column widths
        paymentsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Payment ID
        paymentsTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
        paymentsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Student ID
        paymentsTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Student Name
        paymentsTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Course
        paymentsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Amount
        paymentsTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Method
        paymentsTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Status
        paymentsTable.getColumnModel().getColumn(8).setPreferredWidth(150); // Remarks

        // Center align some columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        paymentsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        paymentsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        paymentsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        paymentsTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        paymentsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        paymentsTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        scrollPane = new JScrollPane(paymentsTable);

        // Buttons
        btnRecordPayment = new JButton("Record Payment");
        btnViewDetails = new JButton("View Details");
        btnDeletePayment = new JButton("Delete Payment");
        btnRefresh = new JButton("Refresh");
        btnPrintReceipt = new JButton("Print Receipt");

        // Button styling
        btnRecordPayment.setBackground(new Color(46, 125, 50));
//        btnRecordPayment.setForeground(Color.WHITE);
        btnViewDetails.setBackground(new Color(25, 118, 210));
//        btnViewDetails.setForeground(Color.WHITE);
        btnDeletePayment.setBackground(new Color(211, 47, 47));
//        btnDeletePayment.setForeground(Color.WHITE);
        btnRefresh.setBackground(new Color(117, 117, 117));
//        btnRefresh.setForeground(Color.WHITE);

        // Search components
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnClearSearch = new JButton("Clear");
        cmbSearchType = new JComboBox<>(new String[]{
                "By Student ID", "By Student Name", "By Course"
        });

        // Filter components
        cmbStatusFilter = new JComboBox<>(new String[]{
                "All Status", "Completed", "Pending", "Failed", "Refunded"
        });
        btnApplyFilter = new JButton("Apply Filter");

        // Statistics labels
        lblTotalPayments = new JLabel("Total Payments: 0");
        lblTotalCollected = new JLabel("Total Collected: Rs. 0.00");
        lblTotalPending = new JLabel("Pending: Rs. 0.00");
        lblTotalRefunded = new JLabel("Refunded: Rs. 0.00");

        // Style statistics labels
        Font statsFont = new Font("Arial", Font.BOLD, 13);
        lblTotalPayments.setFont(statsFont);
        lblTotalCollected.setFont(statsFont);
        lblTotalPending.setFont(statsFont);
        lblTotalRefunded.setFont(statsFont);

        lblTotalCollected.setForeground(new Color(46, 125, 50));
        lblTotalPending.setForeground(new Color(255, 152, 0));
        lblTotalRefunded.setForeground(new Color(211, 47, 47));
    }

    /**
     * Layout all components
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with search and statistics
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(cmbSearchType);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClearSearch);
        searchPanel.add(new JLabel("  |  Status:"));
        searchPanel.add(cmbStatusFilter);
        searchPanel.add(btnApplyFilter);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Payment Statistics"));
        statsPanel.add(lblTotalPayments);
        statsPanel.add(lblTotalCollected);
        statsPanel.add(lblTotalPending);
        statsPanel.add(lblTotalRefunded);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set button sizes for better visibility
        Dimension buttonSize = new Dimension(150, 35);
        btnRecordPayment.setPreferredSize(buttonSize);
        btnViewDetails.setPreferredSize(buttonSize);
        btnPrintReceipt.setPreferredSize(buttonSize);
        btnDeletePayment.setPreferredSize(buttonSize);
        btnRefresh.setPreferredSize(buttonSize);

        // Make buttons opaque
        btnRecordPayment.setOpaque(true);
        btnViewDetails.setOpaque(true);
        btnDeletePayment.setOpaque(true);
        btnRefresh.setOpaque(true);

        buttonPanel.add(btnRecordPayment);
        buttonPanel.add(btnViewDetails);
        buttonPanel.add(btnPrintReceipt);
        buttonPanel.add(btnDeletePayment);
        buttonPanel.add(btnRefresh);

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Record payment button
        btnRecordPayment.addActionListener(e -> recordPayment());

        // View details button
        btnViewDetails.addActionListener(e -> viewPaymentDetails());

        // Print receipt button
        btnPrintReceipt.addActionListener(e -> printReceipt());

        // Delete button
        btnDeletePayment.addActionListener(e -> deletePayment());

        // Refresh button
        btnRefresh.addActionListener(e -> {
            loadPayments();
            updateStatistics();
        });

        // Search button
        btnSearch.addActionListener(e -> searchPayments());

        // Clear search button
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("");
            cmbStatusFilter.setSelectedIndex(0);
            loadPayments();
            updateStatistics();
        });

        // Apply filter button
        btnApplyFilter.addActionListener(e -> applyStatusFilter());

        // Double-click on table row to view details
        paymentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewPaymentDetails();
                }
            }
        });

        // Enter key in search field
        txtSearch.addActionListener(e -> searchPayments());
    }

    /**
     * Load all payments into the table
     */
    private void loadPayments() {
        tableModel.setRowCount(0); // Clear table
        List<Payment> payments = paymentManager.getAllPayments();

        for (Payment payment : payments) {
            Object[] row = {
                    payment.getPaymentId(),
                    payment.getFormattedDate(),
                    payment.getStudentId(),
                    payment.getStudentName(),
                    payment.getCourseName(),
                    String.format("%.2f", payment.getAmount()),
                    payment.getPaymentMethod(),
                    payment.getStatus(),
                    payment.getRemarks()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Record a new payment
     */
    private void recordPayment() {
        // Create custom dialog for recording payment
        JDialog dialog = new JDialog(parentFrame, "Record Payment", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(parentFrame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student selection
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Student: *"), gbc);

        JComboBox<String> cmbStudent = new JComboBox<>();
        cmbStudent.addItem("-- Select Student --");
        for (Student student : studentManager.getAllStudents()) {
            cmbStudent.addItem(student.getStudentId() + " - " + student.getName());
        }
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(cmbStudent, gbc);

        // Course (auto-filled based on student)
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Course:"), gbc);

        JTextField txtCourse = new JTextField();
        txtCourse.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 1;
        dialog.add(txtCourse, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Amount (Rs.): *"), gbc);

        JTextField txtAmount = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        dialog.add(txtAmount, gbc);

        // Payment method
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Payment Method: *"), gbc);

        JComboBox<String> cmbMethod = new JComboBox<>(new String[]{
                "Cash", "Card", "Bank Transfer", "Online"
        });
        gbc.gridx = 1; gbc.gridy = 3;
        dialog.add(cmbMethod, gbc);

        // Remarks
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Remarks:"), gbc);

        JTextArea txtRemarks = new JTextArea(3, 20);
        txtRemarks.setLineWrap(true);
        JScrollPane scrollRemarks = new JScrollPane(txtRemarks);
        gbc.gridx = 1; gbc.gridy = 4;
        dialog.add(scrollRemarks, gbc);

        // Student selection handler
        cmbStudent.addActionListener(e -> {
            String selected = (String) cmbStudent.getSelectedItem();
            if (selected != null && !selected.equals("-- Select Student --")) {
                String studentId = selected.split(" - ")[0];
                Student student = studentManager.findStudentById(studentId);
                if (student != null) {
                    txtCourse.setText(student.getEnrolledCourse());
                    txtAmount.setText(String.format("%.2f", student.getRemainingBalance()));
                }
            }
        });

        // Buttons
        JButton btnSave = new JButton("Record Payment");
        JButton btnCancel = new JButton("Cancel");

        btnSave.setBackground(new Color(46, 125, 50));
//        btnSave.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(211, 47, 47));
//        btnCancel.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        // Button actions
        btnSave.addActionListener(e -> {
            String studentSelection = (String) cmbStudent.getSelectedItem();
            String amountText = txtAmount.getText().trim();
            String method = (String) cmbMethod.getSelectedItem();
            String remarks = txtRemarks.getText().trim();

            if (studentSelection == null || studentSelection.equals("-- Select Student --")) {
                JOptionPane.showMessageDialog(dialog, "Please select a student!");
                return;
            }

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter amount!");
                return;
            }

            try {
                String studentId = studentSelection.split(" - ")[0];
                Student student = studentManager.findStudentById(studentId);
                double amount = Double.parseDouble(amountText);

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Amount must be positive!");
                    return;
                }

                // Generate payment ID
                String paymentId = paymentManager.generateNextPaymentId();

                // Get course ID from the enrolled course
                String courseId = "N/A"; // Default
                String enrolledCourseName = student.getEnrolledCourse();
                for (Course course : courseManager.getAllCourses()) {
                    if (course.getCourseName().equals(enrolledCourseName)) {
                        courseId = course.getCourseId();
                        break;
                    }
                }

                // Create payment
                Payment payment = new Payment(
                        paymentId,
                        studentId,
                        student.getName(),
                        courseId, // Now has proper course ID
                        student.getEnrolledCourse(),
                        amount,
                        method
                );
                payment.setRemarks(remarks);

                // Add payment
                if (paymentManager.addPayment(payment)) {
                    // Update student's paid amount
                    studentManager.recordPayment(studentId, amount);

                    // Refresh table
                    loadPayments();
                    updateStatistics();

                    JOptionPane.showMessageDialog(dialog, "Payment recorded successfully!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to record payment!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    /**
     * View payment details
     */
    private void viewPaymentDetails() {
        int selectedRow = paymentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to view details.");
            return;
        }

        String paymentId = (String) tableModel.getValueAt(selectedRow, 0);
        Payment payment = paymentManager.findPaymentById(paymentId);

        if (payment != null) {
            String details = payment.toString();

            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Payment Details - " + paymentId,
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Print receipt
     */
    private void printReceipt() {
        int selectedRow = paymentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to print receipt.");
            return;
        }

        String paymentId = (String) tableModel.getValueAt(selectedRow, 0);
        Payment payment = paymentManager.findPaymentById(paymentId);

        if (payment != null) {
            String receipt = payment.toReceiptString();

            JTextArea textArea = new JTextArea(receipt);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Payment Receipt - " + paymentId,
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Delete payment
     */
    private void deletePayment() {
        int selectedRow = paymentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to delete.");
            return;
        }

        String paymentId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this payment?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (paymentManager.deletePayment(paymentId)) {
                tableModel.removeRow(selectedRow);
                updateStatistics();
                JOptionPane.showMessageDialog(this, "Payment deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete payment!");
            }
        }
    }

    /**
     * Search payments
     */
    private void searchPayments() {
        String searchText = txtSearch.getText().trim();
        String searchType = (String) cmbSearchType.getSelectedItem();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter search text!");
            return;
        }

        List<Payment> results = null;

        if (searchType.equals("By Student ID")) {
            results = paymentManager.getPaymentsByStudentId(searchText);
        } else if (searchType.equals("By Student Name")) {
            results = paymentManager.getPaymentsByStudentName(searchText);
        } else if (searchType.equals("By Course")) {
            results = paymentManager.getPaymentsByCourseName(searchText);
        }

        if (results != null) {
            displayPayments(results);
            updateStatistics();
        }
    }

    /**
     * Apply status filter
     */
    private void applyStatusFilter() {
        String status = (String) cmbStatusFilter.getSelectedItem();
        List<Payment> results = null;

        if (status.equals("All Status")) {
            results = paymentManager.getAllPayments();
        } else {
            results = paymentManager.getPaymentsByStatus(status);
        }

        displayPayments(results);
        updateStatistics();
    }

    /**
     * Display payments in table
     */
    private void displayPayments(List<Payment> payments) {
        tableModel.setRowCount(0);

        for (Payment payment : payments) {
            Object[] row = {
                    payment.getPaymentId(),
                    payment.getFormattedDate(),
                    payment.getStudentId(),
                    payment.getStudentName(),
                    payment.getCourseName(),
                    String.format("%.2f", payment.getAmount()),
                    payment.getPaymentMethod(),
                    payment.getStatus(),
                    payment.getRemarks()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Update statistics
     */
    private void updateStatistics() {
        lblTotalPayments.setText("Total Payments: " + paymentManager.getTotalPaymentCount());
        lblTotalCollected.setText(String.format("Total Collected: Rs. %.2f",
                paymentManager.getTotalAmountCollected()));
        lblTotalPending.setText(String.format("Pending: Rs. %.2f",
                paymentManager.getTotalPendingAmount()));
        lblTotalRefunded.setText(String.format("Refunded: Rs. %.2f",
                paymentManager.getTotalRefundedAmount()));
    }

    /**
     * Refresh the panel
     */
    public void refresh() {
        loadPayments();
        updateStatistics();
    }
}
