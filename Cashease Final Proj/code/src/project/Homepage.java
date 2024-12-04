package project;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Homepage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblCurrentBalance;
    private JLabel lblWelcomeUser;
    private HashMap<String, BigDecimal> accounts;
    private String accountNumber = "12345";
    private String userName;
    private StringBuilder transactionLog;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Homepage frame = new Homepage("DefaultUser");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Homepage(String userName) {
        this.userName = userName;
        transactionLog = new StringBuilder();

        accounts = new HashMap<>();
        accounts.put(accountNumber, new BigDecimal("1000.00"));

        setTitle("Banking App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

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

        JLabel lblBalanceText = new JLabel("Current Balance:");
        lblBalanceText.setBounds(50, 100, 200, 30);
        lblBalanceText.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(lblBalanceText);

        lblCurrentBalance = new JLabel("₱" + accounts.get(accountNumber).toString());
        lblCurrentBalance.setBounds(50, 140, 300, 40);
        lblCurrentBalance.setFont(new Font("Arial", Font.BOLD, 28));
        lblCurrentBalance.setForeground(new Color(34, 139, 34));
        contentPane.add(lblCurrentBalance);

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
        btnLogout.setBackground(new Color(255, 0, 0));
        btnLogout.setForeground(Color.WHITE);
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
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the current frame
            Login loginWindow = new Login(); // Open the login window
            loginWindow.setVisible(true);
        }
    }
}