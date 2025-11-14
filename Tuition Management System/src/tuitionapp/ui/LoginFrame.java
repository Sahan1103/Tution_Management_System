package tuitionapp.ui;
import tuitionapp.manager.AdminManager;
import tuitionapp.ui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame - A JFrame that shows the login screen.
 * On successful login, it opens the MainFrame and closes itself.
 */
public class LoginFrame extends JFrame {
    private AdminManager adminManager;

    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        this.adminManager = new AdminManager();

        setTitle("Tuition Management System - Login");
        setSize(900, 500); // Larger size for better appearance
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false); // Fixed size

        // --- Set the App Icon ---
        try {
            java.net.URL iconURL = getClass().getClassLoader().getResource("resources/icon.png");
            if (iconURL != null) {
                setIconImage(new ImageIcon(iconURL).getImage());
            }
        } catch (Exception e) {
            System.out.println("Could not load icon: " + e.getMessage());
        }

        // --- Main Panel with 2 columns ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        add(mainPanel, BorderLayout.CENTER);

        // --- 1. Left Panel (Logo) ---
        mainPanel.add(createLogoPanel());

        // --- 2. Right Panel (Login Fields) ---
        mainPanel.add(createLoginPanel());
    }

    /**
     * Creates the left-side panel with the logo and title.
     */
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Gradient background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(26, 35, 46),
                        0, getHeight(), new Color(40, 55, 71)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        logoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Container for logo and text
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Load and scale logo
        JLabel logoLabel = new JLabel();
        try {
            java.net.URL logoURL = getClass().getClassLoader().getResource("resources/icon2.png");
            if (logoURL != null) {
                ImageIcon icon = new ImageIcon(logoURL);

                // Create high-quality scaled image
                int size = 220;
                Image originalImage = icon.getImage();

                // Use BufferedImage for better quality
                java.awt.image.BufferedImage scaledImage = new java.awt.image.BufferedImage(
                        size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D g2d = scaledImage.createGraphics();

                // Enable anti-aliasing for smooth edges
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.drawImage(originalImage, 0, 0, size, size, null);
                g2d.dispose();

                logoLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback if logo not found
                logoLabel.setText("📚");
                logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
                logoLabel.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            logoLabel.setText("📚");
            logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Tuition Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("System");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tagline
        JLabel taglineLabel = new JLabel("Manage Students, Courses & Payments");
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        taglineLabel.setForeground(new Color(189, 195, 199));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(taglineLabel);

        logoPanel.add(contentPanel, gbc);

        return logoPanel;
    }

    /**
     * Creates the right-side panel with login fields.
     */
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Login Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 32));
        loginTitle.setForeground(new Color(44, 62, 80));
        loginPanel.add(loginTitle, gbc);

        // Welcome message
        gbc.gridy = 1;
        JLabel welcomeLabel = new JLabel("Welcome back! Please login to your account");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        welcomeLabel.setForeground(new Color(127, 140, 141));
        loginPanel.add(welcomeLabel, gbc);

        // Space
        gbc.gridy = 2;
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(userLabel, gbc);

        // Username Field
        gbc.gridy = 4;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(280, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridy = 5;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        loginPanel.add(passLabel, gbc);

        // Password Field
        gbc.gridy = 6;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(280, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(280, 40));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginPanel.add(loginButton, gbc);

        // --- Action Listeners ---
        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        return loginPanel;
    }

    /**
     * Handles the login logic when the button is pressed.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (adminManager.validateLogin(username, password)) {
            // Login SUCCESS
            SwingUtilities.invokeLater(() -> {
                MainFrame mainApp = new MainFrame();
                mainApp.setVisible(true);
            });
            this.dispose();
        } else {
            // Login FAILED
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
