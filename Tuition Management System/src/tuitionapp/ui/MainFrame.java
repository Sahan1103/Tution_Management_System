package tuitionapp.ui;
import tuitionapp.manager.StudentManager;
import tuitionapp.manager.CourseManager;
import tuitionapp.manager.PaymentManager;
import tuitionapp.ui.panels.CoursePanel;
import tuitionapp.ui.panels.PaymentPanel;
import tuitionapp.ui.panels.StudentPanel;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame - Main application window
 */
public class MainFrame extends JFrame {
    // Managers
    private StudentManager studentManager;
    private CourseManager courseManager;
    private PaymentManager paymentManager;

    // Panels
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private PaymentPanel paymentPanel;

    // UI Components
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;

    /**
     * Constructor
     */
    public MainFrame() {
        // Initialize managers
        studentManager = new StudentManager();
        courseManager = new CourseManager();
        paymentManager = new PaymentManager();

        // Setup frame
        setTitle("Tuition Class Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null); // Center on screen

        // Initialize components
        initComponents();
        createMenuBar();

        // Set icon (optional - you can add an icon later)
        try {
            java.net.URL iconURL = getClass().getClassLoader().getResource("resources/icon.png");
            if (iconURL != null) {
                setIconImage(new ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            System.out.println("Could not load icon: " + e.getMessage());
        }
    }

    /**
     * Initialize all components
     */
    private Icon createColoredIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillOval(x, y, 16, 16);
            }

            @Override
            public int getIconWidth() { return 18; }

            @Override
            public int getIconHeight() { return 18; }
        };
    }
    private void initComponents() {
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));

        // Create panels
        studentPanel = new StudentPanel(this, studentManager, courseManager);
        coursePanel = new CoursePanel(this, courseManager);
        paymentPanel = new PaymentPanel(this, paymentManager, studentManager, courseManager);

        // Add panels to tabbed pane with icons (you can customize icons)
        // Load icons from files
        ImageIcon studentIcon = loadIcon("resources/student.png");
        ImageIcon courseIcon = loadIcon("resources/course.png");
        ImageIcon paymentIcon = loadIcon("resources/payment.png");

// Add tabs with icons
        tabbedPane.addTab("Students", studentIcon, studentPanel, "Manage Students");
        tabbedPane.addTab("Courses", courseIcon, coursePanel, "Manage Courses");
        tabbedPane.addTab("Payments", paymentIcon, paymentPanel, "Manage Payments");
        // Add tabbed pane to frame
        add(tabbedPane, BorderLayout.CENTER);

        // Add status bar at bottom
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Create menu bar
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem refreshItem = new JMenuItem("Refresh All");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
        refreshItem.addActionListener(e -> refreshAllPanels());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("alt X"));
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        JMenuItem studentsItem = new JMenuItem("Students");
        studentsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl 1"));
        studentsItem.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        JMenuItem coursesItem = new JMenuItem("Courses");
        coursesItem.setAccelerator(KeyStroke.getKeyStroke("ctrl 2"));
        coursesItem.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        JMenuItem paymentsItem = new JMenuItem("Payments");
        paymentsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl 3"));
        paymentsItem.addActionListener(e -> tabbedPane.setSelectedIndex(2));

        viewMenu.add(studentsItem);
        viewMenu.add(coursesItem);
        viewMenu.add(paymentsItem);

        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.setMnemonic('R');

        JMenuItem studentReportItem = new JMenuItem("Student Report");
        studentReportItem.addActionListener(e -> showStudentReport());

        JMenuItem courseReportItem = new JMenuItem("Course Report");
        courseReportItem.addActionListener(e -> showCourseReport());

        JMenuItem paymentReportItem = new JMenuItem("Payment Report");
        paymentReportItem.addActionListener(e -> showPaymentReport());

        JMenuItem summaryReportItem = new JMenuItem("Summary Report");
        summaryReportItem.addActionListener(e -> showSummaryReport());

        reportsMenu.add(studentReportItem);
        reportsMenu.add(courseReportItem);
        reportsMenu.add(paymentReportItem);
        reportsMenu.addSeparator();
        reportsMenu.add(summaryReportItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());

        JMenuItem helpItem = new JMenuItem("Help");
        helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        helpItem.addActionListener(e -> showHelpDialog());

        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Create status bar
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        JLabel statusLabel = new JLabel("  Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString() + "  ");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(dateLabel, BorderLayout.EAST);

        return statusBar;
    }

    /**
     * Create icon label (simple text icon)
     */
    private Icon createIcon(String emoji) {
        JLabel label = new JLabel(emoji);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        label.setPreferredSize(new Dimension(20, 20));
        return new ImageIcon(createImageFromLabel(label));
    }

    /**
     * Create image from label
     */
    private Image createImageFromLabel(JLabel label) {
        int width = 20;
        int height = 20;
        Image img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        label.setSize(width, height);
        label.paint(g);
        g.dispose();
        return img;
    }
    /**
     * Load icon from file
     */
    private ImageIcon loadIcon(String path) {
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource(path);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                // Resize to 20x20
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            System.out.println("Could not load icon: " + path);
        }
        // Return null if icon not found (will show text only)
        return null;
    }

    /**
     * Refresh all panels
     */
    private void refreshAllPanels() {
        studentPanel.refresh();
        coursePanel.refresh();
        paymentPanel.refresh();
        JOptionPane.showMessageDialog(
                this,
                "All data refreshed successfully!",
                "Refresh Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Exit application
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Show student report
     */
    private void showStudentReport() {
        StringBuilder report = new StringBuilder();
        report.append("========== STUDENT REPORT ==========\n\n");
        report.append("Total Students: ").append(studentManager.getTotalStudentCount()).append("\n");
        report.append("Total Expected Revenue: Rs. ").append(String.format("%.2f", studentManager.getTotalExpectedRevenue())).append("\n");
        report.append("Total Collected: Rs. ").append(String.format("%.2f", studentManager.getTotalFeesCollected())).append("\n");
        report.append("Total Outstanding: Rs. ").append(String.format("%.2f", studentManager.getTotalOutstandingBalance())).append("\n");
        report.append("Collection Rate: ").append(String.format("%.2f%%", studentManager.getCollectionPercentage())).append("\n");
        report.append("\nStudents with Outstanding Balance: ").append(studentManager.getStudentsWithOutstandingBalance().size()).append("\n");
        report.append("Students Fully Paid: ").append(studentManager.getStudentsWithFullPayment().size()).append("\n");

        showReportDialog("Student Report", report.toString());
    }

    /**
     * Show course report
     */
    private void showCourseReport() {
        StringBuilder report = new StringBuilder();
        report.append("========== COURSE REPORT ==========\n\n");
        report.append("Total Courses: ").append(courseManager.getTotalCourseCount()).append("\n");
        report.append("Total Enrolled Students: ").append(courseManager.getTotalEnrolledStudents()).append("\n");
        report.append("Total Available Seats: ").append(courseManager.getTotalAvailableSeats()).append("\n");
        report.append("Average Occupancy Rate: ").append(String.format("%.2f%%", courseManager.getAverageOccupancyRate())).append("\n");
        report.append("Total Revenue Potential: Rs. ").append(String.format("%.2f", courseManager.getTotalRevenuePotential())).append("\n");
        report.append("Current Revenue: Rs. ").append(String.format("%.2f", courseManager.getCurrentRevenue())).append("\n");

        var mostPopular = courseManager.getMostPopularCourse();
        if (mostPopular != null) {
            report.append("\nMost Popular Course: ").append(mostPopular.getCourseName());
            report.append(" (").append(mostPopular.getEnrolledStudents()).append(" students)\n");
        }

        showReportDialog("Course Report", report.toString());
    }

    /**
     * Show payment report
     */
    private void showPaymentReport() {
        StringBuilder report = new StringBuilder();
        report.append("========== PAYMENT REPORT ==========\n\n");
        report.append("Total Payments: ").append(paymentManager.getTotalPaymentCount()).append("\n");
        report.append("Total Collected: Rs. ").append(String.format("%.2f", paymentManager.getTotalAmountCollected())).append("\n");
        report.append("Total Pending: Rs. ").append(String.format("%.2f", paymentManager.getTotalPendingAmount())).append("\n");
        report.append("Total Refunded: Rs. ").append(String.format("%.2f", paymentManager.getTotalRefundedAmount())).append("\n");
        report.append("Average Payment: Rs. ").append(String.format("%.2f", paymentManager.getAveragePaymentAmount())).append("\n");

        report.append("\nCompleted Payments: ").append(paymentManager.getCompletedPayments().size()).append("\n");
        report.append("Pending Payments: ").append(paymentManager.getPendingPayments().size()).append("\n");
        report.append("Failed Payments: ").append(paymentManager.getFailedPayments().size()).append("\n");
        report.append("Refunded Payments: ").append(paymentManager.getRefundedPayments().size()).append("\n");

        showReportDialog("Payment Report", report.toString());
    }

    /**
     * Show summary report
     */
    private void showSummaryReport() {
        StringBuilder report = new StringBuilder();
        report.append("========== TUITION MANAGEMENT SYSTEM - SUMMARY REPORT ==========\n\n");

        report.append("--- STUDENTS ---\n");
        report.append("Total Students: ").append(studentManager.getTotalStudentCount()).append("\n");
        report.append("Outstanding Balance: Rs. ").append(String.format("%.2f", studentManager.getTotalOutstandingBalance())).append("\n\n");

        report.append("--- COURSES ---\n");
        report.append("Total Courses: ").append(courseManager.getTotalCourseCount()).append("\n");
        report.append("Total Enrolled: ").append(courseManager.getTotalEnrolledStudents()).append("\n");
        report.append("Avg Occupancy: ").append(String.format("%.2f%%", courseManager.getAverageOccupancyRate())).append("\n\n");

        report.append("--- PAYMENTS ---\n");
        report.append("Total Payments: ").append(paymentManager.getTotalPaymentCount()).append("\n");
        report.append("Total Collected: Rs. ").append(String.format("%.2f", paymentManager.getTotalAmountCollected())).append("\n");
        report.append("Total Pending: Rs. ").append(String.format("%.2f", paymentManager.getTotalPendingAmount())).append("\n\n");

        report.append("--- FINANCIAL OVERVIEW ---\n");
        report.append("Expected Revenue: Rs. ").append(String.format("%.2f", studentManager.getTotalExpectedRevenue())).append("\n");
        report.append("Collected Amount: Rs. ").append(String.format("%.2f", studentManager.getTotalFeesCollected())).append("\n");
        report.append("Collection Rate: ").append(String.format("%.2f%%", studentManager.getCollectionPercentage())).append("\n");

        showReportDialog("Summary Report", report.toString());
    }

    /**
     * Show report dialog
     */
    private void showReportDialog(String title, String report) {
        JTextArea textArea = new JTextArea(report);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Show about dialog
     */
    private void showAboutDialog() {

        // --- 1. Define Your Developer Names ---
        // !!! REPLACE THESE WITH YOUR REAL NAMES !!!
        String member1 = "Harsha Maduranga";
        String member2 = "Sahan Maduranga";
        String member3 = "Kavindu Sadan";
        String member4 = "Sachini Chamodya";
        String member5 = "Tharushi Nethmini";

        // --- 2. Build the Message (using HTML for better formatting) ---
        // Using HTML lets us control line breaks and text styles
        String message = "<html>" +
                "<body>" +
                "<h2>Tuition Class Management System</h2>" +
                "<p><b>Version:</b> 1.0</p>" +
                "<p>This system provides a complete solution for managing a tuition class,<br>" +
                "including student, course, and payment tracking.</p>" +
                "<hr>" +
                "<h3>Developers:</h3>" +
                "<p>• &nbsp;" + member1 + "</p>" +
                "<p>• &nbsp;" + member2 + "</p>" +
                "<p>• &nbsp;" + member3 + "</p>" +
                "<p>• &nbsp;" + member4 + "</p>" +
                "<p>• &nbsp;" + member5 + "</p>" +
                "<br>" +
                "<p>© 2025 - University of Sri Jayewardenepura</p>" +
                "</body>" +
                "</html>";

        // --- 3. Create a JLabel to render the HTML ---
        JLabel messageLabel = new JLabel(message);

        // Optional: Set a font if you like, though default is usually fine
        // messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // --- 4. Show the dialog ---
        JOptionPane.showMessageDialog(
                this,                 // 'this' refers to the parent frame
                messageLabel,         // Use the JLabel instead of a simple string
                "About This Software",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Show help dialog
     */
    private void showHelpDialog() {
        String help = "TUITION MANAGEMENT SYSTEM - HELP\n\n" +
                "NAVIGATION:\n" +
                "• Use tabs to switch between Students, Courses, and Payments\n" +
                "• Or use Ctrl+1, Ctrl+2, Ctrl+3 keyboard shortcuts\n\n" +
                "STUDENTS:\n" +
                "• Add new students with the 'Add Student' button\n" +
                "• Double-click a row to edit\n" +
                "• Use search to find students quickly\n" +
                "• Record payments directly from the student panel\n\n" +
                "COURSES:\n" +
                "• Create and manage courses\n" +
                "• Track enrollment and available seats\n" +
                "• View occupancy rates\n\n" +
                "PAYMENTS:\n" +
                "• Record new payments\n" +
                "• View payment history\n" +
                "• Filter by status\n" +
                "• Print receipts\n\n" +
                "REPORTS:\n" +
                "• Access various reports from the Reports menu\n" +
                "• View summary statistics\n\n" +
                "KEYBOARD SHORTCUTS:\n" +
                "• F5 - Refresh all data\n" +
                "• F1 - Help\n" +
                "• Alt+X - Exit\n" +
                "• Ctrl+1/2/3 - Switch tabs";

        JTextArea textArea = new JTextArea(help);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 450));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Help",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Get managers (for external access if needed)
     */
    public StudentManager getStudentManager() {
        return studentManager;
    }

    public CourseManager getCourseManager() {
        return courseManager;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }
}
