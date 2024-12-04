package prj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField usernameField; // For username
    private JPasswordField passwordField; // For password input

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
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            }
        });
    }

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
    public Login() {
        try {
            connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255)); // Light blue background
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Logo Section
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/img/CE.png"))
                .getImage()
                .getScaledInstance(159, 150, Image.SCALE_SMOOTH)); // Scale the image to fit
        logoLabel.setIcon(logoIcon);
        logoLabel.setBounds(100, 30, 120, 120); // Adjust the position of the logo for better alignment
        contentPane.add(logoLabel);

        // Title Section
        JLabel titleLabel = new JLabel("CashEase");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(70, 130, 180)); // Steel blue color
        titleLabel.setBounds(240, 50, 300, 40); // Position the title next to the logo
        contentPane.add(titleLabel);

        JLabel taglineLabel = new JLabel("Save more than ever");
        taglineLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        taglineLabel.setForeground(Color.GRAY);
        taglineLabel.setBounds(240, 100, 200, 20); // Position tagline below the title
        contentPane.add(taglineLabel);

        // Username Section
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameLabel.setBounds(200, 180, 100, 30);
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setBounds(200, 210, 400, 40);
        usernameField.setToolTipText("Enter your username");
        contentPane.add(usernameField);

        // Password Section
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setBounds(200, 270, 100, 30);
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBounds(200, 300, 400, 40);
        passwordField.setToolTipText("Enter your password");
        contentPane.add(passwordField);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBackground(new Color(50, 205, 50)); // Green button
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(200, 370, 180, 50);
        loginButton.addActionListener(this::handleLogin);
        contentPane.add(loginButton);

        // Signup Button
        JButton signupButton = new JButton("Signup");
        signupButton.setFont(new Font("Arial", Font.BOLD, 20));
        signupButton.setBackground(new Color(30, 144, 255)); // Blue button
        signupButton.setForeground(Color.WHITE);
        signupButton.setBounds(420, 370, 180, 50);
        signupButton.addActionListener(e -> {
            Signup signupFrame = new Signup(); // Create an instance of the Signup class
            signupFrame.setVisible(true);     // Show the Signup page
            dispose();                        // Close the current Login window
        });
        contentPane.add(signupButton);
    }

    /**
     * Handle the login process.
     */
    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim(); // Trim whitespace
        String password = new String(passwordField.getPassword()).trim(); // Trim whitespace

        // SQL query to check if the username and password match in the database
        try {
            String query = "SELECT * FROM accountdetails WHERE accUsername = '" + username + "' AND accPassword = '" + password + "'";
            ResultSet rs = st.executeQuery(query);

            // Check if a user with the given username and password exists
            if (rs.next()) {
                // Open Homepage if login is successful
                Homepage homepage = new Homepage(username); // Pass username to Homepage
                homepage.setVisible(true); // Make the Homepage frame visible
                dispose(); // Close the login window
            } else {
                // Show error message for invalid credentials
                JOptionPane.showMessageDialog(contentPane, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(contentPane, "Error occurred while checking credentials: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}