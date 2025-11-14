package tuitionapp.ui.dialogs;

import tuitionapp.model.Student;
import tuitionapp.model.Course;
import tuitionapp.manager.StudentManager;
import tuitionapp.manager.CourseManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EditStudentDialog - Dialog for editing an existing student with MULTIPLE COURSE SUPPORT
 */
public class EditStudentDialog extends JDialog {

    private StudentManager studentManager;
    private CourseManager courseManager;
    private Student student; // The student being edited
    private List<String> originalCourseIds; // Track original courses for enrollment changes
    private boolean updated; // Track if student was updated

    // Form fields
    private JTextField txtStudentId;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextArea txtAddress;
    private JList<String> courseList; // CHANGED: Multiple selection list
    private DefaultListModel<String> courseListModel;
    private JTextField txtTotalFees;
    private JTextField txtPaidAmount;
    private JLabel lblBalance;

    // Buttons
    private JButton btnUpdate;
    private JButton btnCancel;

    /**
     * Constructor
     */
    public EditStudentDialog(JFrame parent, StudentManager studentManager,
                             CourseManager courseManager, Student student) {
        super(parent, "Edit Student - " + student.getStudentId(), true);
        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.student = student;
        this.originalCourseIds = new ArrayList<>(student.getCourseIds());
        this.updated = false;

        initComponents();
        layoutComponents();
        setupEventHandlers();
        loadCourses();
        loadStudentData(); // Fill form with existing data

        // Dialog settings
        setSize(550, 750);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        // Text fields
        txtStudentId = new JTextField(15);
        txtStudentId.setEditable(false);
        txtStudentId.setBackground(new Color(240, 240, 240));

        txtName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhone = new JTextField(15);

        txtAddress = new JTextArea(3, 20);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);

        // Course list (MULTIPLE SELECTION)
        courseListModel = new DefaultListModel<>();
        courseList = new JList<>(courseListModel);
        courseList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        courseList.setVisibleRowCount(5);

        txtTotalFees = new JTextField(10);
        txtTotalFees.setEditable(false);
        txtTotalFees.setBackground(new Color(240, 240, 240));

        txtPaidAmount = new JTextField(10);

        lblBalance = new JLabel("Rs. 0.00");
        lblBalance.setFont(new Font("Arial", Font.BOLD, 14));

        // Buttons
        btnUpdate = new JButton("Update Student");
        btnCancel = new JButton("Cancel");

        // Styling
        btnUpdate.setBackground(new Color(25, 118, 210));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setOpaque(true);
        btnUpdate.setBorderPainted(false);

        btnCancel.setBackground(new Color(211, 47, 47));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true);
        btnCancel.setBorderPainted(false);
    }

    /**
     * Layout all components using GridBagLayout
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // Main panel with form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtStudentId, gbc);

        // Row 1: Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Name: *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtName, gbc);

        // Row 2: Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email: *"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        // Row 3: Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Phone Number: *"), gbc);

        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        // Row 4: Address
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Address: *"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        scrollAddress.setPreferredSize(new Dimension(200, 60));
        formPanel.add(scrollAddress, gbc);

        // Row 5: Enrolled Courses (MULTIPLE SELECTION)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Enrolled Courses: *"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JScrollPane courseScrollPane = new JScrollPane(courseList);
        courseScrollPane.setPreferredSize(new Dimension(300, 100));
        formPanel.add(courseScrollPane, gbc);

        // Row 6: Instruction label
        gbc.gridx = 1;
        gbc.gridy = 6;
        JLabel instructionLabel = new JLabel("(Hold Ctrl to select multiple courses)");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        instructionLabel.setForeground(Color.GRAY);
        formPanel.add(instructionLabel, gbc);

        // Row 7: Total Fees (AUTO-CALCULATED)
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Total Fees (Rs.):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(txtTotalFees, gbc);

        // Row 8: Paid Amount
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Paid Amount (Rs.):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(txtPaidAmount, gbc);

        // Row 9: Balance (Calculated)
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Balance:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(lblBalance, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnUpdate);

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add note at top
        JLabel lblNote = new JLabel("Fields marked with * are required | Fees calculated automatically from courses");
        lblNote.setFont(new Font("Arial", Font.ITALIC, 11));
        lblNote.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        add(lblNote, BorderLayout.NORTH);
    }

    /**
     * Setup event handlers for buttons
     */
    private void setupEventHandlers() {
        // Update button
        btnUpdate.addActionListener(e -> updateStudent());

        // Cancel button
        btnCancel.addActionListener(e -> {
            updated = false;
            dispose();
        });

        // Course selection change - auto-calculate total fees
        courseList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateTotalFees();
                    updateBalance();
                }
            }
        });

        // Paid amount change - update balance
        txtPaidAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateBalance();
            }
        });
    }

    /**
     * Load available courses into the list
     */
    private void loadCourses() {
        courseListModel.clear();

        List<Course> courses = courseManager.getAllCourses();
        for (Course course : courses) {
            courseListModel.addElement(course.getCourseId() + " - " + course.getCourseName() +
                    " (Rs. " + String.format("%.2f", course.getCourseFee()) + ")");
        }
    }

    /**
     * Load student data into form fields
     */
    private void loadStudentData() {
        txtStudentId.setText(student.getStudentId());
        txtName.setText(student.getName());
        txtEmail.setText(student.getEmail());
        txtPhone.setText(student.getPhoneNumber());
        txtAddress.setText(student.getAddress());
        txtTotalFees.setText(String.format("%.2f", student.getTotalFees()));
        txtPaidAmount.setText(String.format("%.2f", student.getPaidAmount()));

        // Select the enrolled courses in the list
        List<String> studentCourseIds = student.getCourseIds();
        List<Integer> selectedIndices = new ArrayList<>();

        for (int i = 0; i < courseListModel.size(); i++) {
            String item = courseListModel.getElementAt(i);
            String courseId = item.split(" - ")[0];

            if (studentCourseIds.contains(courseId)) {
                selectedIndices.add(i);
            }
        }

        // Set selected indices
        int[] indices = selectedIndices.stream().mapToInt(Integer::intValue).toArray();
        courseList.setSelectedIndices(indices);

        updateBalance();
    }

    /**
     * Update total fees based on selected courses (AUTO-CALCULATE)
     */
    private void updateTotalFees() {
        List<String> selectedCourses = courseList.getSelectedValuesList();
        double totalFees = 0.0;

        for (String selected : selectedCourses) {
            String courseId = selected.split(" - ")[0];
            Course course = courseManager.findCourseById(courseId);
            if (course != null) {
                totalFees += course.getCourseFee();
            }
        }

        txtTotalFees.setText(String.format("%.2f", totalFees));
    }

    /**
     * Update balance display
     */
    private void updateBalance() {
        try {
            double totalFees = Double.parseDouble(txtTotalFees.getText().trim());
            double paidAmount = Double.parseDouble(txtPaidAmount.getText().trim());
            double balance = totalFees - paidAmount;

            lblBalance.setText(String.format("Rs. %.2f", balance));

            // Color code the balance
            if (balance > 0) {
                lblBalance.setForeground(new Color(211, 47, 47)); // Red for outstanding
            } else if (balance == 0) {
                lblBalance.setForeground(new Color(46, 125, 50)); // Green for paid
            } else {
                lblBalance.setForeground(new Color(255, 152, 0)); // Orange for overpaid
            }
        } catch (NumberFormatException e) {
            lblBalance.setText("Rs. 0.00");
            lblBalance.setForeground(Color.BLACK);
        }
    }

    /**
     * Validate and update the student
     */
    private void updateStudent() {
        try {
            // Validate inputs
            String studentId = txtStudentId.getText().trim();
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            List<String> selectedCourses = courseList.getSelectedValuesList();
            String totalFeesText = txtTotalFees.getText().trim();
            String paidAmountText = txtPaidAmount.getText().trim();

            // Check required fields
            if (name.isEmpty()) {
                showError("Name is required!");
                txtName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                showError("Email is required!");
                txtEmail.requestFocus();
                return;
            }

            if (!email.contains("@")) {
                showError("Invalid email format!");
                txtEmail.requestFocus();
                return;
            }

            if (phone.isEmpty()) {
                showError("Phone number is required!");
                txtPhone.requestFocus();
                return;
            }

            if (address.isEmpty()) {
                showError("Address is required!");
                txtAddress.requestFocus();
                return;
            }

            if (selectedCourses.isEmpty()) {
                showError("Please select at least one course!");
                courseList.requestFocus();
                return;
            }

            // Extract course IDs and names
            List<String> newCourseIds = new ArrayList<>();
            StringBuilder courseNames = new StringBuilder();

            for (int i = 0; i < selectedCourses.size(); i++) {
                String selected = selectedCourses.get(i);
                String courseId = selected.split(" - ")[0];
                String courseName = selected.split(" - ")[1].split(" \\(")[0];

                newCourseIds.add(courseId);
                courseNames.append(courseName);
                if (i < selectedCourses.size() - 1) {
                    courseNames.append(", ");
                }
            }

            // Parse numeric values
            double totalFees;
            try {
                totalFees = Double.parseDouble(totalFeesText);
                if (totalFees < 0) {
                    showError("Total fees cannot be negative!");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid total fees!");
                return;
            }

            double paidAmount;
            try {
                paidAmount = Double.parseDouble(paidAmountText);
                if (paidAmount < 0) {
                    showError("Paid amount cannot be negative!");
                    txtPaidAmount.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid paid amount!");
                txtPaidAmount.requestFocus();
                return;
            }

            // Check for duplicate email (excluding current student)
            Student existingStudent = studentManager.findStudentByEmail(email);
            if (existingStudent != null && !existingStudent.getStudentId().equals(studentId)) {
                showError("A student with this email already exists!");
                txtEmail.requestFocus();
                return;
            }

            // Update the original student object
            student.setName(name);
            student.setEmail(email);
            student.setPhoneNumber(phone);
            student.setAddress(address);
            student.setEnrolledCourse(courseNames.toString());
            student.setCourseIds(newCourseIds);
            student.setTotalFees(totalFees);
            student.setPaidAmount(paidAmount);

            // Validate student
            String validationError = studentManager.validateStudent(student);
            if (validationError != null) {
                showError(validationError);
                return;
            }

            // Update student using manager
            if (studentManager.updateStudent(studentId, student)) {
                // Handle course enrollment changes

                // Find courses to unenroll from (were selected before, not selected now)
                for (String oldCourseId : originalCourseIds) {
                    if (!newCourseIds.contains(oldCourseId)) {
                        courseManager.unenrollStudent(oldCourseId);
                    }
                }

                // Find courses to enroll in (not selected before, selected now)
                for (String newCourseId : newCourseIds) {
                    if (!originalCourseIds.contains(newCourseId)) {
                        courseManager.enrollStudent(newCourseId);
                    }
                }

                updated = true;
                JOptionPane.showMessageDialog(
                        this,
                        "Student updated successfully!\n" +
                                "Enrolled in " + newCourseIds.size() + " course(s)\n" +
                                "Total Fees: Rs. " + String.format("%.2f", totalFees),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } else {
                showError("Failed to update student.");
            }

        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Check if student was updated
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Get the updated student
     */
    public Student getStudent() {
        return student;
    }
}