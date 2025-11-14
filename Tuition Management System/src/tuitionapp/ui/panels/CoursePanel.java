package tuitionapp.ui.panels;
import tuitionapp.model.Course;
import tuitionapp.manager.CourseManager;
import tuitionapp.ui.components.CourseTableModel;
import tuitionapp.ui.dialogs.AddCourseDialog;
import tuitionapp.ui.dialogs.EditCourseDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * CoursePanel - Panel for managing courses
 */
public class CoursePanel extends JPanel{
    private CourseManager courseManager;
    private JFrame parentFrame;

    // Table components
    private JTable coursesTable;
    private CourseTableModel tableModel;
    private JScrollPane scrollPane;

    // Buttons
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnViewDetails;

    // Search components
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClearSearch;
    private JComboBox<String> cmbSearchType;

    // Statistics labels
    private JLabel lblTotalCourses;
    private JLabel lblTotalEnrolled;
    private JLabel lblAvailableSeats;
    private JLabel lblAverageOccupancy;

    /**
     * Constructor
     */
    public CoursePanel(JFrame parentFrame, CourseManager courseManager) {
        this.parentFrame = parentFrame;
        this.courseManager = courseManager;

        initComponents();
        layoutComponents();
        setupEventHandlers();
        loadCourses();
        updateStatistics();
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        // Table model and table
        tableModel = new CourseTableModel();
        coursesTable = new JTable(tableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.setRowHeight(25);
        coursesTable.getTableHeader().setReorderingAllowed(false);

        // Adjust column widths
        coursesTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Course ID
        coursesTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Course Name
        coursesTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Description
        coursesTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Instructor
        coursesTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Fee
        coursesTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Duration
        coursesTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Schedule
        coursesTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Max Students
        coursesTable.getColumnModel().getColumn(8).setPreferredWidth(80);  // Enrolled
        coursesTable.getColumnModel().getColumn(9).setPreferredWidth(80);  // Available
        coursesTable.getColumnModel().getColumn(10).setPreferredWidth(80); // Occupancy %

        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        coursesTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        coursesTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        coursesTable.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        coursesTable.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
        coursesTable.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        coursesTable.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);

        scrollPane = new JScrollPane(coursesTable);

        // Buttons
        btnAdd = new JButton("Add Course");
        btnEdit = new JButton("Edit Course");
        btnDelete = new JButton("Delete Course");
        btnRefresh = new JButton("Refresh");
        btnViewDetails = new JButton("View Details");

        // Button styling
        btnAdd.setBackground(new Color(46, 125, 50));
//        btnAdd.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(25, 118, 210));
//        btnEdit.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(211, 47, 47));
//        btnDelete.setForeground(Color.WHITE);
        btnRefresh.setBackground(new Color(117, 117, 117));
//        btnRefresh.setForeground(Color.WHITE);

        // Search components
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnClearSearch = new JButton("Clear");
        cmbSearchType = new JComboBox<>(new String[]{
                "By Name", "By Instructor", "With Available Seats", "Full Courses"
        });

        // Statistics labels
        lblTotalCourses = new JLabel("Total Courses: 0");
        lblTotalEnrolled = new JLabel("Total Enrolled: 0");
        lblAvailableSeats = new JLabel("Available Seats: 0");
        lblAverageOccupancy = new JLabel("Avg Occupancy: 0%");

        // Style statistics labels
        Font statsFont = new Font("Arial", Font.BOLD, 13);
        lblTotalCourses.setFont(statsFont);
        lblTotalEnrolled.setFont(statsFont);
        lblAvailableSeats.setFont(statsFont);
        lblAverageOccupancy.setFont(statsFont);
    }

    /**
     * Layout all components
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with search and statistics
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Courses"));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(cmbSearchType);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClearSearch);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Course Statistics"));
        statsPanel.add(lblTotalCourses);
        statsPanel.add(lblTotalEnrolled);
        statsPanel.add(lblAvailableSeats);
        statsPanel.add(lblAverageOccupancy);

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
        btnRefresh.setPreferredSize(buttonSize);

        // Make buttons opaque
        btnAdd.setOpaque(true);
        btnEdit.setOpaque(true);
        btnDelete.setOpaque(true);
        btnRefresh.setOpaque(true);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewDetails);
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
        btnAdd.addActionListener(e -> addCourse());

        // Edit button
        btnEdit.addActionListener(e -> editCourse());

        // Delete button
        btnDelete.addActionListener(e -> deleteCourse());

        // Refresh button
        btnRefresh.addActionListener(e -> {
            loadCourses();
            updateStatistics();
        });

        // View details button
        btnViewDetails.addActionListener(e -> viewCourseDetails());

        // Search button
        btnSearch.addActionListener(e -> searchCourses());

        // Clear search button
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("");
            loadCourses();
            updateStatistics();
        });

        // Double-click on table row to edit
        coursesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editCourse();
                }
            }
        });

        // Enter key in search field
        txtSearch.addActionListener(e -> searchCourses());
    }

    /**
     * Load all courses into the table
     */
    private void loadCourses() {
        tableModel.setCourses(courseManager.getAllCourses());
    }

    /**
     * Add a new course
     */
    private void addCourse() {
        AddCourseDialog dialog = new AddCourseDialog(parentFrame, courseManager);
        dialog.setVisible(true);

        Course newCourse = dialog.getAddedCourse();
        if (newCourse != null) {
            tableModel.addCourse(newCourse);
            updateStatistics();
        }
    }

    /**
     * Edit selected course
     */
    private void editCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a course to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Course selectedCourse = tableModel.getCourseAt(selectedRow);
        EditCourseDialog dialog = new EditCourseDialog(parentFrame, courseManager, selectedCourse);
        dialog.setVisible(true);

        if (dialog.isUpdated()) {
            tableModel.updateCourse(selectedRow, selectedCourse);
            tableModel.refresh();
            updateStatistics();
        }
    }

    /**
     * Delete selected course
     */
    private void deleteCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a course to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Course selectedCourse = tableModel.getCourseAt(selectedRow);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete course: " + selectedCourse.getCourseName() + "?\n" +
                        "Enrolled students: " + selectedCourse.getEnrolledStudents(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (courseManager.deleteCourse(selectedCourse.getCourseId())) {
                tableModel.removeCourse(selectedRow);
                updateStatistics();
                JOptionPane.showMessageDialog(
                        this,
                        "Course deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete course.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * View detailed information about selected course
     */
    private void viewCourseDetails() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a course to view details.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Course selectedCourse = tableModel.getCourseAt(selectedRow);

        String details = selectedCourse.toString();

        JTextArea textArea = new JTextArea(details);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Course Details - " + selectedCourse.getCourseId(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Search courses based on search type
     */
    private void searchCourses() {
        String searchText = txtSearch.getText().trim();
        String searchType = (String) cmbSearchType.getSelectedItem();

        if (searchType.equals("By Name")) {
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a course name to search.");
                return;
            }
            tableModel.setCourses(courseManager.searchCoursesByName(searchText));
        } else if (searchType.equals("By Instructor")) {
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an instructor name to search.");
                return;
            }
            tableModel.setCourses(courseManager.searchCoursesByInstructor(searchText));
        } else if (searchType.equals("With Available Seats")) {
            tableModel.setCourses(courseManager.getCoursesWithAvailableSeats());
        } else if (searchType.equals("Full Courses")) {
            tableModel.setCourses(courseManager.getFullCourses());
        }

        updateStatistics();
    }

    /**
     * Update statistics labels
     */
    private void updateStatistics() {
        lblTotalCourses.setText("Total Courses: " + tableModel.getCourseCount());
        lblTotalEnrolled.setText("Total Enrolled: " + tableModel.getTotalEnrolledStudents());
        lblAvailableSeats.setText("Available Seats: " + tableModel.getTotalAvailableSeats());
        lblAverageOccupancy.setText(String.format("Avg Occupancy: %.1f%%",
                tableModel.getAverageOccupancyRate()));
    }

    /**
     * Refresh the panel (reload data)
     */
    public void refresh() {
        loadCourses();
        updateStatistics();
    }
}
