package tuitionapp.ui.panels;

import tuitionapp.model.Student;
import tuitionapp.manager.StudentManager;
import tuitionapp.manager.CourseManager;
import tuitionapp.ui.components.StudentTableModel;
import tuitionapp.ui.dialogs.AddStudentDialog;
import tuitionapp.ui.dialogs.EditStudentDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * StudentPanel - Panel for managing students
 */
public class StudentPanel extends JPanel{
    private StudentManager studentManager;
    private CourseManager courseManager;
    private JFrame parentFrame;

    // Table components
    private JTable studentsTable;
    private StudentTableModel tableModel;
    private JScrollPane scrollPane;

    // Buttons
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnViewDetails;
    private JButton btnRecordPayment;

    // Search components
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClearSearch;
    private JComboBox<String> cmbSearchType;

    // Filter components
    private JComboBox<String> cmbBalanceFilter;
    private JButton btnApplyFilter;

    // Statistics labels
    private JLabel lblTotalStudents;
    private JLabel lblTotalFees;
    private JLabel lblTotalCollected;
    private JLabel lblTotalOutstanding;

    /**
     * Constructor
     */
    public StudentPanel(JFrame parentFrame, StudentManager studentManager, CourseManager courseManager) {
        this.parentFrame = parentFrame;
        this.studentManager = studentManager;
        this.courseManager = courseManager;

        initComponents();
        layoutComponents();
        setupEventHandlers();
        loadStudents();
        updateStatistics();
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        // Table model and table
        tableModel = new StudentTableModel();
        studentsTable = new JTable(tableModel);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.setRowHeight(25);
        studentsTable.getTableHeader().setReorderingAllowed(false);

        // Adjust column widths
        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Student ID
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Email
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Phone
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Address
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Course
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Total Fees
        studentsTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Paid Amount
        studentsTable.getColumnModel().getColumn(8).setPreferredWidth(100); // Balance

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentsTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        studentsTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        studentsTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

        // Color code balance column (red for outstanding)
        studentsTable.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && value instanceof Double) {
                    double balance = (Double) value;
                    if (balance > 0) {
                        c.setForeground(new Color(211, 47, 47)); // Red for outstanding
                    } else if (balance == 0) {
                        c.setForeground(new Color(46, 125, 50)); // Green for paid
                    } else {
                        c.setForeground(new Color(255, 152, 0)); // Orange for overpaid
                    }
                } else if (isSelected) {
                    c.setForeground(table.getSelectionForeground());
                }
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        scrollPane = new JScrollPane(studentsTable);

        // Buttons
        btnAdd = new JButton("Add Student");
        btnEdit = new JButton("Edit Student");
        btnDelete = new JButton("Delete Student");
        btnRefresh = new JButton("Refresh");
        btnViewDetails = new JButton("View Details");
        btnRecordPayment = new JButton("Record Payment");

        // Button styling
        btnAdd.setBackground(new Color(46, 125, 50));
//        btnAdd.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(25, 118, 210));
//        btnEdit.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(211, 47, 47));
//        btnDelete.setForeground(Color.WHITE);
        btnRefresh.setBackground(new Color(117, 117, 117));
//        btnRefresh.setForeground(Color.WHITE);
        btnRecordPayment.setBackground(new Color(255, 152, 0));
//        btnRecordPayment.setForeground(Color.WHITE);

        // Search components
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnClearSearch = new JButton("Clear");
        cmbSearchType = new JComboBox<>(new String[]{
                "By Name", "By Email", "By Phone", "By Course"
        });

        // Filter components
        cmbBalanceFilter = new JComboBox<>(new String[]{
                "All Students", "With Outstanding Balance", "Fully Paid"
        });
        btnApplyFilter = new JButton("Apply Filter");

        // Statistics labels
        lblTotalStudents = new JLabel("Total Students: 0");
        lblTotalFees = new JLabel("Total Fees: Rs. 0.00");
        lblTotalCollected = new JLabel("Collected: Rs. 0.00");
        lblTotalOutstanding = new JLabel("Outstanding: Rs. 0.00");

        // Style statistics labels
        Font statsFont = new Font("Arial", Font.BOLD, 13);
        lblTotalStudents.setFont(statsFont);
        lblTotalFees.setFont(statsFont);
        lblTotalCollected.setFont(statsFont);
        lblTotalOutstanding.setFont(statsFont);

        lblTotalCollected.setForeground(new Color(46, 125, 50));
        lblTotalOutstanding.setForeground(new Color(211, 47, 47));
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
        searchPanel.add(new JLabel("  |  Filter:"));
        searchPanel.add(cmbBalanceFilter);
        searchPanel.add(btnApplyFilter);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Student Statistics"));
        statsPanel.add(lblTotalStudents);
        statsPanel.add(lblTotalFees);
        statsPanel.add(lblTotalCollected);
        statsPanel.add(lblTotalOutstanding);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Set button sizes for better visibility
        Dimension buttonSize = new Dimension(150, 35);
        btnAdd.setPreferredSize(buttonSize);
        btnEdit.setPreferredSize(buttonSize);
        btnDelete.setPreferredSize(buttonSize);
        btnViewDetails.setPreferredSize(buttonSize);
        btnRecordPayment.setPreferredSize(buttonSize);
        btnRefresh.setPreferredSize(buttonSize);

        // Make buttons opaque
        btnAdd.setOpaque(true);
        btnEdit.setOpaque(true);
        btnDelete.setOpaque(true);
        btnRefresh.setOpaque(true);
        btnRecordPayment.setOpaque(true);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewDetails);
        buttonPanel.add(btnRecordPayment);
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
        // Add button
        btnAdd.addActionListener(e -> addStudent());

        // Edit button
        btnEdit.addActionListener(e -> editStudent());

        // Delete button
        btnDelete.addActionListener(e -> deleteStudent());

        // Refresh button
        btnRefresh.addActionListener(e -> {
            loadStudents();
            updateStatistics();
        });

        // View details button
        btnViewDetails.addActionListener(e -> viewStudentDetails());

        // Record payment button
        btnRecordPayment.addActionListener(e -> recordPayment());

        // Search button
        btnSearch.addActionListener(e -> searchStudents());

        // Clear search button
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("");
            cmbBalanceFilter.setSelectedIndex(0);
            loadStudents();
            updateStatistics();
        });

        // Apply filter button
        btnApplyFilter.addActionListener(e -> applyBalanceFilter());

        // Double-click on table row to edit
        studentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editStudent();
                }
            }
        });

        // Enter key in search field
        txtSearch.addActionListener(e -> searchStudents());
    }

    /**
     * Load all students into the table
     */
    private void loadStudents() {
        tableModel.setStudents(studentManager.getAllStudents());
    }

    /**
     * Add a new student
     */
    private void addStudent() {
        AddStudentDialog dialog = new AddStudentDialog(parentFrame, studentManager, courseManager);
        dialog.setVisible(true);

        Student newStudent = dialog.getAddedStudent();
        if (newStudent != null) {
            tableModel.addStudent(newStudent);
            updateStatistics();
        }
    }

    /**
     * Edit selected student
     */
    private void editStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Student selectedStudent = tableModel.getStudentAt(selectedRow);
        EditStudentDialog dialog = new EditStudentDialog(
                parentFrame, studentManager, courseManager, selectedStudent
        );
        dialog.setVisible(true);

        if (dialog.isUpdated()) {
            tableModel.updateStudent(selectedRow, selectedStudent);
            tableModel.refresh();
            updateStatistics();
        }
    }

    /**
     * Delete selected student
     */
    private void deleteStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Student selectedStudent = tableModel.getStudentAt(selectedRow);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete student: " + selectedStudent.getName() + "?\n" +
                        "Student ID: " + selectedStudent.getStudentId() + "\n" +
                        "Outstanding Balance: Rs. " + String.format("%.2f", selectedStudent.getRemainingBalance()),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentManager.deleteStudent(selectedStudent.getStudentId())) {
                // Unenroll from course
                String courseName = selectedStudent.getEnrolledCourse();
                for (var course : courseManager.getAllCourses()) {
                    if (course.getCourseName().equals(courseName)) {
                        courseManager.unenrollStudent(course.getCourseId());
                        break;
                    }
                }

                tableModel.removeStudent(selectedRow);
                updateStatistics();
                JOptionPane.showMessageDialog(
                        this,
                        "Student deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete student.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * View detailed information about selected student
     */
    private void viewStudentDetails() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student to view details.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Student selectedStudent = tableModel.getStudentAt(selectedRow);

        String details = selectedStudent.toString();

        JTextArea textArea = new JTextArea(details);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Student Details - " + selectedStudent.getStudentId(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Record payment for selected student
     */
    private void recordPayment() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student to record payment.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Student selectedStudent = tableModel.getStudentAt(selectedRow);

        // Create simple payment dialog
        String amountStr = JOptionPane.showInputDialog(
                this,
                "Student: " + selectedStudent.getName() + "\n" +
                        "Outstanding Balance: Rs. " + String.format("%.2f", selectedStudent.getRemainingBalance()) + "\n\n" +
                        "Enter payment amount:",
                "Record Payment",
                JOptionPane.QUESTION_MESSAGE
        );

        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr.trim());

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be positive!",
                            "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (studentManager.recordPayment(selectedStudent.getStudentId(), amount)) {
                    tableModel.updateStudent(selectedRow, selectedStudent);
                    tableModel.refresh();
                    updateStatistics();

                    JOptionPane.showMessageDialog(
                            this,
                            "Payment of Rs. " + String.format("%.2f", amount) + " recorded successfully!\n" +
                                    "New Balance: Rs. " + String.format("%.2f", selectedStudent.getRemainingBalance()),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to record payment.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid amount! Please enter a valid number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Search students based on search type
     */
    private void searchStudents() {
        String searchText = txtSearch.getText().trim();
        String searchType = (String) cmbSearchType.getSelectedItem();

        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter search text!");
            return;
        }

        if (searchType.equals("By Name")) {
            tableModel.setStudents(studentManager.searchStudentsByName(searchText));
        } else if (searchType.equals("By Email")) {
            Student student = studentManager.findStudentByEmail(searchText);
            if (student != null) {
                tableModel.setStudents(java.util.Arrays.asList(student));
            } else {
                tableModel.setStudents(new java.util.ArrayList<>());
                JOptionPane.showMessageDialog(this, "No student found with that email.");
            }
        } else if (searchType.equals("By Phone")) {
            Student student = studentManager.findStudentByPhone(searchText);
            if (student != null) {
                tableModel.setStudents(java.util.Arrays.asList(student));
            } else {
                tableModel.setStudents(new java.util.ArrayList<>());
                JOptionPane.showMessageDialog(this, "No student found with that phone number.");
            }
        } else if (searchType.equals("By Course")) {
            tableModel.setStudents(studentManager.searchStudentsByCourse(searchText));
        }

        updateStatistics();
    }

    /**
     * Apply balance filter
     */
    private void applyBalanceFilter() {
        String filter = (String) cmbBalanceFilter.getSelectedItem();

        if (filter.equals("All Students")) {
            tableModel.setStudents(studentManager.getAllStudents());
        } else if (filter.equals("With Outstanding Balance")) {
            tableModel.setStudents(studentManager.getStudentsWithOutstandingBalance());
        } else if (filter.equals("Fully Paid")) {
            tableModel.setStudents(studentManager.getStudentsWithFullPayment());
        }

        updateStatistics();
    }

    /**
     * Update statistics labels
     */
    private void updateStatistics() {
        lblTotalStudents.setText("Total Students: " + tableModel.getStudentCount());
        lblTotalFees.setText(String.format("Total Fees: Rs. %.2f", tableModel.getTotalFees()));
        lblTotalCollected.setText(String.format("Collected: Rs. %.2f", tableModel.getTotalPaid()));
        lblTotalOutstanding.setText(String.format("Outstanding: Rs. %.2f", tableModel.getTotalBalance()));
    }

    /**
     * Refresh the panel (reload data)
     */
    public void refresh() {
        loadStudents();
        updateStatistics();
    }
}
