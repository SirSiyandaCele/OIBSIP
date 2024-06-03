import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserAccount {
    // Attributes of the user account
    private String userId; // User ID
    private String pin; // PIN
    private double balance; // Current balance
    private List<String[]> transactions; // List to store transaction details
    private String fullName; // Full name of the account holder
    private String bank; // Bank name associated with the account
    private String TCode; // Transaction code associated with the account

    // Constructor to initialize the user account
    public UserAccount(String fullName, String userId, String pin, double initialBalance, String bank, String TCode) {
        // Initialize account attributes with provided values
        this.fullName = fullName;
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
        this.bank = bank;
        this.TCode = TCode;
        this.transactions = new ArrayList<>(); // Initialize the transactions list
    }

    // Getter methods to access account attributes
    public String getUserId() {
        return this.userId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getBank() {
        return this.bank;
    }

    public String getTCode() {
        return this.TCode;
    }

    public String getPin() {
        return this.pin;
    }

    public double getBalance() {
        return this.balance;
    }

    // Method to deposit money into the account
    public void deposit(double amount) {
        balance += amount; // Increase balance by the deposited amount
        new atmTransaction(TCode, "Deposit", amount, balance); // Create a new transaction for the deposit
    }

    // Method to withdraw money from the account
    public void withdraw(double amount) {
        balance -= amount; // Decrease balance by the withdrawn amount
        new atmTransaction(TCode, "Withdraw", amount, balance); // Create a new transaction for the withdrawal
    }

    // Method to transfer money from this account to another account
    public void transfer(UserAccount toAccount, double amount) {
        balance-=amount;
        new atmTransaction(TCode, "Transfer to " + toAccount.getUserId(), amount, balance); // Create a new transaction for the transfer
    }

    // Method to add money received from a transfer to this account
    public void moneyInTransfer(UserAccount fromAccount, double amount) {
        balance+=amount;
        new atmTransaction(TCode, "Transfer from " + fromAccount.getUserId(), amount, balance); // Create a new transaction for the received money
    }

    // Method to retrieve transaction history associated with this account
    public List<String[]> getTransactions() {
        transactions.clear(); // Clear the transactions list before populating it with new data

        String accountFilePath = "src/userTransactions";

        try {
            List<String> transactionLines = Files.readAllLines(Paths.get(accountFilePath)); // Read all lines from the transactions file
            boolean isUserFound = false;

            // Iterating through each line to find transactions linked to the user
            int count = 0;
            for (int index = 0; index < transactionLines.size(); index++) {
                count++;
                String transactionLine = transactionLines.get(index);
                String[] transactionDetails = transactionLine.split(","); // Split transaction details by comma
                String transactionCode = transactionDetails[0].trim(); // Get the transaction code
                if (TCode.equals(transactionCode)) { // Check if the transaction code matches the user's transaction code
                    isUserFound = true;
                    transactions.add(transactionDetails); // Add transaction details to the transactions list
                }
            }
            // Print message if no transactions found for the user
            if (count == transactionLines.size() - 1 && !isUserFound) {
                System.out.println("YOU HAVE NOT TRANSACTED YET");
            }

            // Return a copy of the transactions list
            return new ArrayList<>(transactions);
        } catch (IOException e) { // Catch IOException if file reading fails
            throw new RuntimeException(e);
        }
    }
}

