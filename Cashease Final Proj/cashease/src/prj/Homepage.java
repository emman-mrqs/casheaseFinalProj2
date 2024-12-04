package prj;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Homepage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblCurrentBalance;
    private JLabel lblWelcomeUser;
    private JLabel lblAccountNumber;
    private String accountNumber;
    private String userName;

    // Database connection details
    private static final String DbName = "register";
    private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DbUrl = "jdbc:mysql://localhost:3306/" + DbName;
    private static final String DbUsername = "root";
    private static final String DbPassword = "";

    private Connection con;

    public Homepage(String userName) {
        this.userName = userName;

        // Initialize UI
        setTitle("Banking App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Header Panel
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 784, 70);
        panel.setBackground(new Color(100, 149, 237));
        contentPane.add(panel);
        panel.setLayout(null);

        lblWelcomeUser = new JLabel("WELCOME, " + userName + "!");
        lblWelcomeUser.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcomeUser.setForeground(Color.WHITE);
        lblWelcomeUser.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcomeUser.setBounds(10, 10, 764, 50);
        panel.add(lblWelcomeUser);

        // Account Number Section
        JLabel lblAccountNumberText = new JLabel("Account Number:");
        lblAccountNumberText.setBounds(50, 81, 200, 30);
        lblAccountNumberText.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(lblAccountNumberText);

        lblAccountNumber = new JLabel("Loading...");
        lblAccountNumber.setBounds(50, 100, 300, 30);
        lblAccountNumber.setFont(new Font("Arial", Font.PLAIN, 18));
        lblAccountNumber.setForeground(new Color(70, 130, 180));
        contentPane.add(lblAccountNumber);

        // Current Balance Section
        JLabel lblBalanceText = new JLabel("Current Balance:");
        lblBalanceText.setBounds(50, 140, 200, 30);
        lblBalanceText.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(lblBalanceText);

        lblCurrentBalance = new JLabel("₱0.00");
        lblCurrentBalance.setBounds(50, 180, 300, 40);
        lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 28));
        lblCurrentBalance.setForeground(new Color(34, 139, 34));
        contentPane.add(lblCurrentBalance);

        // Buttons
        JButton btnDeposit = new JButton("Deposit");
        btnDeposit.setBounds(450, 120, 250, 60);
        btnDeposit.setFont(new Font("Arial", Font.PLAIN, 18));
        btnDeposit.setBackground(new Color(50, 205, 50));
        btnDeposit.setForeground(Color.WHITE);
        btnDeposit.addActionListener(e -> depositMoney());
        contentPane.add(btnDeposit);

        JButton btnWithdraw = new JButton("Withdraw");
        btnWithdraw.setBounds(450, 220, 250, 60);
        btnWithdraw.setFont(new Font("Arial", Font.PLAIN, 18));
        btnWithdraw.setBackground(new Color(220, 20, 60));
        btnWithdraw.setForeground(Color.WHITE);
        btnWithdraw.addActionListener(e -> withdrawMoney());
        contentPane.add(btnWithdraw);

        JButton btnTransaction = new JButton("Transaction");
        btnTransaction.setBounds(450, 320, 250, 60);
        btnTransaction.setFont(new Font("Arial", Font.PLAIN, 18));
        btnTransaction.setBackground(new Color(255, 165, 0));
        btnTransaction.setForeground(Color.WHITE);
        btnTransaction.addActionListener(e -> openTransactionFrame());
        contentPane.add(btnTransaction);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(50, 356, 250, 60);
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 18));
        btnLogout.setBackground(new Color(0, 128, 255));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> logout());
        contentPane.add(btnLogout);

        try {
            connectToDatabase();
            fetchAccountNumber(); // Fetch account details from DB
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Connect to the database
    private void connectToDatabase() throws Exception {
        try {
            Class.forName(DbDriver);
            con = DriverManager.getConnection(DbUrl, DbUsername, DbPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Database connection failed!");
        }
    }

    // Fetch account number and balance
    private void fetchAccountNumber() throws Exception {
        String query = "SELECT accNumber, balance FROM accountdetails WHERE accUsername = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accountNumber = rs.getString("accNumber");
                    double balance = rs.getDouble("balance");

                    SwingUtilities.invokeLater(() -> {
                        lblAccountNumber.setText(accountNumber);
                        lblCurrentBalance.setText("₱" + String.format("%.2f", balance));
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Deposit Money
    private void depositMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    // Fetch current balance
                    String balanceQuery = "SELECT balance FROM accountdetails WHERE accNumber = ?";
                    PreparedStatement balanceStmt = con.prepareStatement(balanceQuery);
                    balanceStmt.setString(1, accountNumber);
                    ResultSet rs = balanceStmt.executeQuery();

                    if (rs.next()) {
                        double currentBalance = rs.getDouble("balance");
                        double updatedBalance = currentBalance + amount;

                        // Update the balance in accountdetails
                        String updateQuery = "UPDATE accountdetails SET balance = ? WHERE accNumber = ?";
                        PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                        updateStmt.setDouble(1, updatedBalance);
                        updateStmt.setString(2, accountNumber);
                        updateStmt.executeUpdate();

                        // Insert the transaction into transactions
                        String insertQuery = "INSERT INTO transactions (accNumber, transaction_type, amount, balance, transaction_date) VALUES (?, 'Deposit', ?, ?, NOW())";
                        PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                        insertStmt.setString(1, accountNumber);
                        insertStmt.setDouble(2, amount);
                        insertStmt.setDouble(3, updatedBalance);
                        insertStmt.executeUpdate();

                        // Update the balance label
                        fetchAccountNumber();
                        JOptionPane.showMessageDialog(this, "Deposit Successful!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid deposit amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing deposit: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    // Withdraw Money
    private void withdrawMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);

                // Fetch current balance
                String balanceQuery = "SELECT balance FROM accountdetails WHERE accNumber = ?";
                PreparedStatement balanceStmt = con.prepareStatement(balanceQuery);
                balanceStmt.setString(1, accountNumber);
                ResultSet rs = balanceStmt.executeQuery();

                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    if (amount > 0 && amount <= currentBalance) {
                        double updatedBalance = currentBalance - amount;

                        // Update the balance in accountdetails
                        String updateQuery = "UPDATE accountdetails SET balance = ? WHERE accNumber = ?";
                        PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                        updateStmt.setDouble(1, updatedBalance);
                        updateStmt.setString(2, accountNumber);
                        updateStmt.executeUpdate();

                        // Insert the transaction into transactions
                        String insertQuery = "INSERT INTO transactions (accNumber, transaction_type, amount, balance, transaction_date) VALUES (?, 'Withdrawal', ?, ?, NOW())";
                        PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                        insertStmt.setString(1, accountNumber);
                        insertStmt.setDouble(2, amount);
                        insertStmt.setDouble(3, updatedBalance);
                        insertStmt.executeUpdate();

                        // Update the balance label
                        fetchAccountNumber();
                        JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient funds or invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing withdrawal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    // Open Transaction Frame
    private void openTransactionFrame() {
        TransactionFrame transactionFrame = new TransactionFrame(accountNumber);
        transactionFrame.setVisible(true);
    }



    // Logout
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose(); // Close the current frame
            Login loginWindow = new Login(); // Open the login window
            loginWindow.setVisible(true);
        }
    }
}
