package prj;
import java.math.BigDecimal;

public class balance {

    // Store the balance as BigDecimal for better precision
    private BigDecimal balance;

    // Constructor to initialize the balance
    public balance(BigDecimal initialBalance) {
        this.balance = initialBalance;
    }

    // Method to get the current balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Method to display the current balance
    public void displayBalance() {
        System.out.println("Your current balance is: " + getBalance());
    }

    // Method to deposit money to the balance
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Method to withdraw money from the balance
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    // Main method to test the system
    public static void main(String[] args) {
        // Initialize account with a starting balance
        balance account = new balance(new BigDecimal("1000.00"));
        
        // Display current balance
        account.displayBalance();
        
        // Deposit 500.00
        account.deposit(new BigDecimal("500.00"));
        account.displayBalance();  // Balance should now be 1500.00
        
        // Withdraw 300.00
        account.withdraw(new BigDecimal("300.00"));
        account.displayBalance();  // Balance should now be 1200.00
        
        // Try to withdraw more than available balance
        account.withdraw(new BigDecimal("2000.00")); // Should display insufficient funds
        account.displayBalance();  // Balance should remain 1200.00
    }
}
