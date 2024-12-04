package prj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Signup extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField usernameField; // For username
    private JPasswordField passwordField; // For password
    private JTextField emailField; // For email
    private JTextField contactField; // For contact number

    // Required for database connection
    private static final String DbName = "register";
    private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DbUrl = "jdbc:mysql://localhost:3306/" + DbName;
    private static final String DbUsername = "root";
    private static final String DbPassword = "";

    // SQL Connection
    private Connection con;
    private Statement st;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Signup frame = new Signup();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Method to establish a database connection
    private void connectToDatabase() throws SQLException {
        try {
            Class.forName(DbDriver);
            con = DriverManager.getConnection(DbUrl, DbUsername, DbPassword);
            st = con.createStatement();
            if (con != null) {
                System.out.println("Connection Successful");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public Signup() {
        try {
            connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set size with increased height
        setBounds(100, 100, 800, 600);

        // Content Pane
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255)); // Light blue background
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title Section
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBounds(260, 30, 400, 50);
        contentPane.add(titleLabel);

        // Username Section
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setBounds(150, 120, 150, 30);
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBounds(300, 120, 300, 30);
        usernameField.setToolTipText("Enter your username");
        contentPane.add(usernameField);

        // Password Section
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setBounds(150, 180, 150, 30);
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBounds(300, 180, 300, 30);
        passwordField.setToolTipText("Enter your password");
        contentPane.add(passwordField);

        // Email Section
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        emailLabel.setBounds(150, 240, 150, 30);
        contentPane.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setBounds(300, 240, 300, 30);
        emailField.setToolTipText("Enter your email address");
        contentPane.add(emailField);

        // Contact Number Section
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contactLabel.setBounds(150, 300, 150, 30);
        contentPane.add(contactLabel);

        contactField = new JTextField();
        contactField.setFont(new Font("Arial", Font.PLAIN, 16));
        contactField.setBounds(300, 300, 300, 30);
        contactField.setToolTipText("Enter your contact number");
        contentPane.add(contactField);

        // Signup Button
        JButton signupButton = new JButton("Create Account");
        signupButton.setFont(new Font("Arial", Font.BOLD, 20));
        signupButton.setBackground(new Color(50, 205, 50)); // Green button
        signupButton.setForeground(Color.WHITE);
        signupButton.setBounds(300, 400, 200, 50);
        signupButton.setFocusPainted(false);
        signupButton.addActionListener(this::handleSignup);
        contentPane.add(signupButton);

        // Back to Login Button
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(192, 192, 192)); // Gray button
        backButton.setForeground(Color.BLACK);
        backButton.setBounds(300, 470, 200, 40);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
            dispose();
        });
        contentPane.add(backButton);
    }

    /**
     * Handle the signup process with form validation.
     */
    private void handleSignup(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();

        // Validate fields as before...
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(contentPane, "All fields must be filled out.", "Signup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generate a unique account number (example logic: using current timestamp)
        String accountNumber = "ACC" + System.currentTimeMillis();

        try {
            String query = "INSERT INTO accountdetails (accUsername, accPassword, accEmail, accContact, accNumber) VALUES ('"
                    + username + "', '"
                    + password + "', '"
                    + email + "', '"
                    + contact + "', '"
                    + accountNumber + "')";
            st.executeUpdate(query);

            JOptionPane.showMessageDialog(this, "Account created successfully! Your account number is: " + accountNumber, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Redirect to the Login page
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while saving user data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

}