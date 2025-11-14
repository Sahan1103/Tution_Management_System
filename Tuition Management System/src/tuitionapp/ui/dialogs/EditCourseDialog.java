package tuitionapp.ui.dialogs;
import tuitionapp.model.*;
import tuitionapp.manager.*;

import javax.swing.*;
import java.awt.*;

/**
 * EditCourseDialog - Dialog for editing an existing course
 */
public class EditCourseDialog extends JDialog{
    private CourseManager courseManager;
    private Course course; // The course being edited
    private boolean updated; // Track if course was updated

    // Form fields
    private JTextField txtCourseId;
    private JTextField txtCourseName;
    private JTextArea txtDescription;
    private JTextField txtInstructor;
    private JTextField txtCourseFee;
    private JTextField txtDuration;
    private JTextField txtSchedule;
    private JTextField txtMaxStudents;
    private JTextField txtEnrolledStudents;

    // Buttons
    private JButton btnUpdate;
    private JButton btnCancel;

    /**
     * Constructor
     */
    public EditCourseDialog(JFrame parent, CourseManager courseManager, Course course) {
        super(parent, "Edit Course", true); // true = modal dialog
        this.courseManager = courseManager;
        this.course = course;
        this.updated = false;

        initComponents();
        layoutComponents();
        setupEventHandlers();
        loadCourseData(); // Fill form with existing data

        // Dialog settings
        setSize(500, 650);
        setLocationRelativeTo(parent); // Center on parent
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        // Text fields
        txtCourseId = new JTextField(15);
        txtCourseId.setEditable(false); // Course ID cannot be changed
        txtCourseId.setBackground(new Color(240, 240, 240));
        txtCourseName = new JTextField(20);
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtInstructor = new JTextField(20);
        txtCourseFee = new JTextField(10);
        txtDuration = new JTextField(10);
        txtSchedule = new JTextField(20);
        txtMaxStudents = new JTextField(10);
        txtEnrolledStudents = new JTextField(10);
        txtEnrolledStudents.setEditable(false); // Enrollment count is managed separately
        txtEnrolledStudents.setBackground(new Color(240, 240, 240));

        // Buttons
        btnUpdate = new JButton("Update Course");
        btnCancel = new JButton("Cancel");

        // Styling
        btnUpdate.setBackground(new Color(25, 118, 210));
        btnUpdate.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(211, 47, 47));
        btnCancel.setForeground(Color.WHITE);
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
        formPanel.add(txtCourseId, gbc);

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

        // Row 8: Enrolled Students (Read-only)
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Enrolled Students:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPanel enrolledPanel = new JPanel(new BorderLayout());
        enrolledPanel.add(txtEnrolledStudents, BorderLayout.CENTER);
        JLabel lblNote = new JLabel(" (Read-only)");
        lblNote.setFont(new Font("Arial", Font.ITALIC, 10));
        lblNote.setForeground(Color.GRAY);
        enrolledPanel.add(lblNote, BorderLayout.EAST);
        formPanel.add(enrolledPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnUpdate);

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add note at top
        JLabel lblTopNote = new JLabel("Fields marked with * are required");
        lblTopNote.setFont(new Font("Arial", Font.ITALIC, 11));
        lblTopNote.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        add(lblTopNote, BorderLayout.NORTH);
    }

    /**
     * Setup event handlers for buttons
     */
    private void setupEventHandlers() {
        // Update button
        btnUpdate.addActionListener(e -> updateCourse());

        // Cancel button
        btnCancel.addActionListener(e -> {
            updated = false;
            dispose();
        });
    }

    /**
     * Load course data into form fields
     */
    private void loadCourseData() {
        txtCourseId.setText(course.getCourseId());
        txtCourseName.setText(course.getCourseName());
        txtDescription.setText(course.getDescription());
        txtInstructor.setText(course.getInstructor());
        txtCourseFee.setText(String.format("%.2f", course.getCourseFee()));
        txtDuration.setText(String.valueOf(course.getDuration()));
        txtSchedule.setText(course.getSchedule());
        txtMaxStudents.setText(String.valueOf(course.getMaxStudents()));
        txtEnrolledStudents.setText(String.valueOf(course.getEnrolledStudents()));
    }

    /**
     * Validate and update the course
     */
    private void updateCourse() {
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

                // Check if max students is less than currently enrolled
                if (maxStudents < course.getEnrolledStudents()) {
                    showError("Max students cannot be less than currently enrolled students ("
                            + course.getEnrolledStudents() + ")!");
                    txtMaxStudents.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid max students! Please enter a valid number.");
                txtMaxStudents.requestFocus();
                return;
            }

            // Create updated course object (keep enrolled students count)
            Course updatedCourse = new Course(
                    courseId,
                    courseName,
                    description.isEmpty() ? "No description" : description,
                    instructor,
                    courseFee,
                    duration,
                    schedule,
                    maxStudents,
                    course.getEnrolledStudents() // Keep current enrollment
            );

            // Validate course
            String validationError = courseManager.validateCourse(updatedCourse);
            if (validationError != null) {
                showError(validationError);
                return;
            }

            // Update course using manager
            if (courseManager.updateCourse(courseId, updatedCourse)) {
                // Update the original course object reference
                course.setCourseName(courseName);
                course.setDescription(description.isEmpty() ? "No description" : description);
                course.setInstructor(instructor);
                course.setCourseFee(courseFee);
                course.setDuration(duration);
                course.setSchedule(schedule);
                course.setMaxStudents(maxStudents);

                updated = true;
                JOptionPane.showMessageDialog(
                        this,
                        "Course updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose(); // Close dialog
            } else {
                showError("Failed to update course.");
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
     * Check if course was updated
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Get the updated course
     */
    public Course getCourse() {
        return course;
    }
}
