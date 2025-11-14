package tuitionapp.ui.dialogs;
import tuitionapp.model.Course;
import tuitionapp.manager.CourseManager;
import javax.swing.*;
import java.awt.*;

/**
 * AddCourseDialog - Dialog for adding a new course
 */
public class AddCourseDialog extends JDialog{
    private CourseManager courseManager;
    private Course addedCourse; // Store the added course to return to caller

    // Form fields
    private JTextField txtCourseId;
    private JTextField txtCourseName;
    private JTextArea txtDescription;
    private JTextField txtInstructor;
    private JTextField txtCourseFee;
    private JTextField txtDuration;
    private JTextField txtSchedule;
    private JTextField txtMaxStudents;

    // Buttons
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnGenerateId;

    /**
     * Constructor
     */
    public AddCourseDialog(JFrame parent, CourseManager courseManager) {
        super(parent, "Add New Course", true); // true = modal dialog
        this.courseManager = courseManager;
        this.addedCourse = null;

        initComponents();
        layoutComponents();
        setupEventHandlers();

        // Auto-generate course ID
        generateCourseId();

        // Dialog settings
        setSize(500, 600);
        setLocationRelativeTo(parent); // Center on parent
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        // Text fields
        txtCourseId = new JTextField(15);
        txtCourseId.setEditable(false); // ID is auto-generated
        txtCourseName = new JTextField(20);
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtInstructor = new JTextField(20);
        txtCourseFee = new JTextField(10);
        txtDuration = new JTextField(10);
        txtSchedule = new JTextField(20);
        txtMaxStudents = new JTextField(10);

        // Buttons
        btnSave = new JButton("Save Course");
        btnCancel = new JButton("Cancel");
        btnGenerateId = new JButton("Generate New ID");

        // Styling
        btnSave.setBackground(new Color(46, 125, 50));
//        btnSave.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(211, 47, 47));
//        btnCancel.setForeground(Color.WHITE);
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

        // Row 0: Course ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Course ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPanel idPanel = new JPanel(new BorderLayout(5, 0));
        idPanel.add(txtCourseId, BorderLayout.CENTER);
        idPanel.add(btnGenerateId, BorderLayout.EAST);
        formPanel.add(idPanel, gbc);

        // Row 1: Course Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Course Name: *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtCourseName, gbc);

        // Row 2: Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.CENTER;
        JScrollPane scrollDescription = new JScrollPane(txtDescription);
        scrollDescription.setPreferredSize(new Dimension(200, 60));
        formPanel.add(scrollDescription, gbc);

        // Row 3: Instructor
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Instructor: *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtInstructor, gbc);

        // Row 4: Course Fee
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Course Fee (Rs.): *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtCourseFee, gbc);

        // Row 5: Duration
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Duration (months): *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtDuration, gbc);

        // Row 6: Schedule
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Schedule: *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtSchedule, gbc);

        // Row 7: Max Students
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Max Students: *"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtMaxStudents, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add note at top
        JLabel lblNote = new JLabel("Fields marked with * are required");
        lblNote.setFont(new Font("Arial", Font.ITALIC, 11));
        lblNote.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        add(lblNote, BorderLayout.NORTH);
    }

    /**
     * Setup event handlers for buttons
     */
    private void setupEventHandlers() {
        // Save button
        btnSave.addActionListener(e -> saveCourse());

        // Cancel button
        btnCancel.addActionListener(e -> {
            addedCourse = null;
            dispose();
        });

        // Generate ID button
        btnGenerateId.addActionListener(e -> generateCourseId());
    }

    /**
     * Generate a new course ID
     */
    private void generateCourseId() {
        String newId = courseManager.generateNextCourseId();
        txtCourseId.setText(newId);
    }

    /**
     * Validate and save the course
     */
    private void saveCourse() {
        try {
            // Validate inputs
            String courseId = txtCourseId.getText().trim();
            String courseName = txtCourseName.getText().trim();
            String description = txtDescription.getText().trim();
            String instructor = txtInstructor.getText().trim();
            String feeText = txtCourseFee.getText().trim();
            String durationText = txtDuration.getText().trim();
            String schedule = txtSchedule.getText().trim();
            String maxStudentsText = txtMaxStudents.getText().trim();

            // Check required fields
            if (courseName.isEmpty()) {
                showError("Course name is required!");
                txtCourseName.requestFocus();
                return;
            }

            if (instructor.isEmpty()) {
                showError("Instructor name is required!");
                txtInstructor.requestFocus();
                return;
            }

            if (feeText.isEmpty()) {
                showError("Course fee is required!");
                txtCourseFee.requestFocus();
                return;
            }

            if (durationText.isEmpty()) {
                showError("Duration is required!");
                txtDuration.requestFocus();
                return;
            }

            if (schedule.isEmpty()) {
                showError("Schedule is required!");
                txtSchedule.requestFocus();
                return;
            }

            if (maxStudentsText.isEmpty()) {
                showError("Max students is required!");
                txtMaxStudents.requestFocus();
                return;
            }

            // Parse numeric values
            double courseFee;
            try {
                courseFee = Double.parseDouble(feeText);
                if (courseFee < 0) {
                    showError("Course fee cannot be negative!");
                    txtCourseFee.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid course fee! Please enter a valid number.");
                txtCourseFee.requestFocus();
                return;
            }

            int duration;
            try {
                duration = Integer.parseInt(durationText);
                if (duration <= 0) {
                    showError("Duration must be positive!");
                    txtDuration.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid duration! Please enter a valid number.");
                txtDuration.requestFocus();
                return;
            }

            int maxStudents;
            try {
                maxStudents = Integer.parseInt(maxStudentsText);
                if (maxStudents <= 0) {
                    showError("Max students must be positive!");
                    txtMaxStudents.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid max students! Please enter a valid number.");
                txtMaxStudents.requestFocus();
                return;
            }

            // Create course object
            Course course = new Course(
                    courseId,
                    courseName,
                    description.isEmpty() ? "No description" : description,
                    instructor,
                    courseFee,
                    duration,
                    schedule,
                    maxStudents
            );

            // Validate course
            String validationError = courseManager.validateCourse(course);
            if (validationError != null) {
                showError(validationError);
                return;
            }

            // Add course using manager
            if (courseManager.addCourse(course)) {
                addedCourse = course;
                JOptionPane.showMessageDialog(
                        this,
                        "Course added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose(); // Close dialog
            } else {
                showError("Failed to add course. Course ID may already exist.");
            }

        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
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
     * Get the added course (null if cancelled)
     */
    public Course getAddedCourse() {
        return addedCourse;
    }
}
