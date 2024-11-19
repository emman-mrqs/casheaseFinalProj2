package project;

import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.swing.*;

public class Homepage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblCurrentBalance; // Label to show current balance
    private JLabel lblWelcomeUser; // Label to show welcome message
    private HashMap<String, BigDecimal> accounts; // HashMap to store account balances
    private String accountNumber = "12345"; // Placeholder for account number
    private String userName; // Variable to hold the user's name
    private StringBuilder transactionLog; // To record transaction history

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Homepage frame = new Homepage("DefaultUser"); // Call with a default username
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Homepage(String userName) {
        this.userName = userName; // Assign the passed username
        transactionLog = new StringBuilder(); // Initialize transaction log

        // Initialize account and set initial balance
        accounts = new HashMap<>();
        accounts.put(accountNumber, new BigDecimal("1000.00")); // Start with 1000 balance

        setTitle("Banking App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255)); // Light background color
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Welcome panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(100, 149, 237)); // Cornflower blue
        panel.setBounds(0, 0, 784, 70);
        contentPane.add(panel);
        panel.setLayout(null);

        lblWelcomeUser = new JLabel("WELCOME, " + userName + "!");
        lblWelcomeUser.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcomeUser.setForeground(Color.WHITE);
        lblWelcomeUser.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcomeUser.setBounds(10, 10, 764, 50);
        panel.add(lblWelcomeUser);

        // Current balance label
        JLabel lblBalanceText = new JLabel("Current Balance:");
        lblBalanceText.setFont(new Font("Arial", Font.BOLD, 20));
        lblBalanceText.setBounds(50, 100, 200, 30);
        contentPane.add(lblBalanceText);

        lblCurrentBalance = new JLabel("₱" + accounts.get(accountNumber).toString());
        lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 28));
        lblCurrentBalance.setForeground(new Color(34, 139, 34)); // Green for positive balance
        lblCurrentBalance.setBounds(50, 140, 300, 40);
        contentPane.add(lblCurrentBalance);

        // Deposit button
        JButton btnDeposit = new JButton("Deposit");
        btnDeposit.setFont(new Font("Arial", Font.PLAIN, 18));
        btnDeposit.setBackground(new Color(50, 205, 50)); // Green background
        btnDeposit.setForeground(Color.WHITE);
        btnDeposit.setBounds(450, 120, 250, 60);
        btnDeposit.setFocusPainted(false);
        btnDeposit.setToolTipText("Click to deposit money to your account");
        btnDeposit.addActionListener(e -> depositMoney());
        contentPane.add(btnDeposit);

        // Withdraw button
        JButton btnWithdraw = new JButton("Withdraw");
        btnWithdraw.setFont(new Font("Arial", Font.PLAIN, 18));
        btnWithdraw.setBackground(new Color(220, 20, 60)); // Crimson background
        btnWithdraw.setForeground(Color.WHITE);
        btnWithdraw.setBounds(450, 220, 250, 60);
        btnWithdraw.setFocusPainted(false);
        btnWithdraw.setToolTipText("Click to withdraw money from your account");
        btnWithdraw.addActionListener(e -> withdrawMoney());
        contentPane.add(btnWithdraw);

        // Transaction button
        JButton btnTransaction = new JButton("Transaction");
        btnTransaction.setFont(new Font("Arial", Font.PLAIN, 18));
        btnTransaction.setBackground(new Color(255, 165, 0)); // Orange background
        btnTransaction.setForeground(Color.WHITE);
        btnTransaction.setBounds(450, 320, 250, 60);
        btnTransaction.setFocusPainted(false);
        btnTransaction.setToolTipText("Click to view transaction history");
        btnTransaction.addActionListener(e -> openTransactionFrame());
        contentPane.add(btnTransaction);

        // Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 18));
        btnLogout.setBackground(new Color(70, 130, 180)); // Steel blue background
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBounds(50, 400, 150, 50);
        btnLogout.setToolTipText("Click to logout and return to the login screen");
        btnLogout.addActionListener(e -> logout());
        contentPane.add(btnLogout);
    }

    private void openTransactionFrame() {
        TransactionFrame transactionFrame = new TransactionFrame(transactionLog.toString());
        transactionFrame.setVisible(true);
    }

    private void depositMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit Money", JOptionPane.PLAIN_MESSAGE);
        if (amountStr != null) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                accounts.put(accountNumber, accounts.get(accountNumber).add(amount));
                lblCurrentBalance.setText("₱" + accounts.get(accountNumber).toString());
                transactionLog.append("Deposit: ₱").append(amount).append("\n");
                JOptionPane.showMessageDialog(this, "Successfully deposited ₱" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void withdrawMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw Money", JOptionPane.PLAIN_MESSAGE);
        if (amountStr != null) {
            try {
                BigDecimal amount = new BigDecimal(amountStr);
                BigDecimal currentBalance = accounts.get(accountNumber);
                if (currentBalance.compareTo(amount) >= 0) {
                    accounts.put(accountNumber, currentBalance.subtract(amount));
                    lblCurrentBalance.setText("₱" + accounts.get(accountNumber).toString());
                    transactionLog.append("Withdraw: ₱").append(amount).append("\n");
                    JOptionPane.showMessageDialog(this, "Successfully withdrew ₱" + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logout() {
        dispose(); // Close the Homepage window
        Login loginScreen = new Login(); // Open a new Login screen
        loginScreen.setVisible(true);
    }
}
