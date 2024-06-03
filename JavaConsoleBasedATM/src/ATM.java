import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ATM {
    private static Map<String, UserAccount> accounts; // Map to store user accounts, with user ID as key

    // Constructor to initialize the ATM with demo accounts
    public ATM() {
        accounts = new HashMap<>(); // Initialize the accounts map
        TextFileReader(); // Call the method to read account data from a text file
    }

    // Method to read account data from a text file
    private void TextFileReader() {
        String filePath = "src/userAccounts"; // File path // Please modify this to your location when you are testing my project
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Open a buffered reader for the file
            String line; // store each line read from the file
            while ((line = br.readLine()) != null) { // Read each line until the end of the file
                String[] accountData = line.split(","); // Split the line into account data fields
                if (accountData.length == 6) { // Check if the line format is valid (contains 6 fields)
                    // Extract account data from the line
                    String fullName = accountData[0].trim(); // Full name of the account holder
                    String userId = accountData[1].trim(); // User ID
                    String userPin = accountData[2].trim(); // PIN
                    double balance = Double.parseDouble(accountData[3].trim()); // Account balance
                    String bank = accountData[4].trim(); // Bank name
                    String transactions = accountData[5].trim(); // Transaction history

                    // Create an userAccount object with the extracted data
                    UserAccount account = new UserAccount(fullName, userId, userPin, balance, bank, transactions);

                    // Add the account to the accounts map with user ID as key
                    accounts.put(userId, account);
                } else {
                    System.out.println("Invalid line format: " + line); // Priny error message for invalid line format
                }
            }
        } catch (IOException e) { // Catch IOException if file reading fails
            e.printStackTrace(); // Print stack trace for the exception
        }
    }

    // Method to retrieve a user account by user ID
    public static UserAccount getAccount(String userId) {

        return accounts.get(userId); // Retrieve account from the accounts map using user ID as key
    }

    // Method to check if a user ID and PIN combination is valid
    public boolean checkUser(String userId, String pin) {
        UserAccount account = accounts.get(userId); // Retrieve account using user ID
        // Check if account exists and the provided PIN matches the account's PIN
        return account != null && account.getPin().equals(pin);
    }
}
