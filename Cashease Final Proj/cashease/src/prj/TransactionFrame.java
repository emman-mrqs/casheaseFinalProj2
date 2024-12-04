package prj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class TransactionFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private Connection con;

    // Database connection details
    private static final String DbName = "register";
    private static final String DbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String DbUrl = "jdbc:mysql://localhost:3306/" + DbName;
    private static final String DbUsername = "root";
    private static final String DbPassword = "";

    public TransactionFrame(String accountNumber) {
        setTitle("Transaction History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null); // Center the frame

        // Custom JPanel with gradient background
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(135, 206, 250); // Light sky blue
                Color color2 = new Color(70, 130, 180); // Steel blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title label
        JLabel lblTransactionHistory = new JLabel("Transaction History");
        lblTransactionHistory.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTransactionHistory.setHorizontalAlignment(SwingConstants.CENTER);
        lblTransactionHistory.setForeground(Color.WHITE);
        lblTransactionHistory.setBounds(50, 20, 600, 50);
        contentPane.add(lblTransactionHistory);

        // Table for displaying transaction history
        String[] columnNames = {"Date", "Type", "Amount", "Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0); // Empty table model
        JTable transactionTable = new JTable(tableModel);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        transactionTable.setRowHeight(30);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        transactionTable.getTableHeader().setBackground(new Color(70, 130, 180));
        transactionTable.getTableHeader().setForeground(Color.WHITE);

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(50, 90, 600, 280);
        scrollPane.setBorder(new LineBorder(new Color(70, 130, 180), 2, true));
        contentPane.add(scrollPane);

        // Close button
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnClose.setBackground(new Color(220, 20, 60)); // Crimson red
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false); // Remove focus outline
        btnClose.setBounds(250, 400, 200, 50);
        btnClose.setBorderPainted(false); // Remove button border
        btnClose.setOpaque(true); // Make button opaque for rounded effect
        btnClose.setBorder(new LineBorder(Color.WHITE, 2, true)); // Rounded border
        btnClose.addActionListener(e -> this.dispose());
        contentPane.add(btnClose);

        // Fetch and populate transaction data
        try {
            connectToDatabase();
            populateTransactionTable(accountNumber, tableModel);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
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

    // Populate the JTable with transaction data
    private void populateTransactionTable(String accountNumber, DefaultTableModel tableModel) throws Exception {
        String query = "SELECT transaction_date, transaction_type, amount, balance FROM transactions WHERE accNumber = ? ORDER BY transaction_date DESC";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String transactionDate = rs.getString("transaction_date");
                    String transactionType = rs.getString("transaction_type");
                    double amount = rs.getDouble("amount");
                    double balance = rs.getDouble("balance");

                    // Add the transaction data to the table
                    tableModel.addRow(new Object[]{
                        transactionDate,
                        transactionType,
                        String.format("₱%.2f", amount),
                        String.format("₱%.2f", balance)
                    });
                }
            }
        }
    }
}
