import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AtmService {
    private ATM atm;

    public AtmService() {
        atm = new ATM();
    }

    public boolean userAuthenticator(String userId, String pin) {
        return atm.checkUser(userId, pin);
    }

    public void balance(String userId) {
        UserAccount account = getAccountFromFile(userId);
        if (account != null) {
            System.out.println("Account Holder Name :" + account.getFullName());
            System.out.println("Account Bank        :" + account.getBank());
            System.out.println("Account Balance     :R" + account.getBalance());
        } else {
            System.out.println("User not found.");
        }
    }

    public void showTransactionHistory(String userId) {
        UserAccount account = atm.getAccount(userId);
        if (account != null) {
            int columnWidth = 20;
            String[] categories = {"|User T-Code", "|Type", "|Amount", "|Balance", "|TimeStamp"};
            List<String> titles = new ArrayList<>(List.of(categories));
            List<String[]> transactions = account.getTransactions();

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (String title : titles) {
                    System.out.printf("%-" + columnWidth + "s", title);
                }
                System.out.println();

                for (String[] transaction : transactions) {
                    for (String field : transaction) {
                        System.out.printf("%-" + columnWidth + "s", "|" + field);
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }

    // Method to withdraw an amount from a user's account with a minimum balance requirement
    public void withdraw(String userId, double amount) {
        String accountFilePath = "src/userAccounts";

        try {
            List<String> accountLines = Files.readAllLines(Paths.get(accountFilePath));
            boolean isUserFound = false;
            if(amount >= 10) {
                // Iterate through each line to find the user's account
                for (int index = 0; index < accountLines.size(); index++) {
                    String accountLine = accountLines.get(index);
                    String[] accountDetails = accountLine.split(",");
                    String retrievedUserId = accountDetails[1].trim();

                    if (retrievedUserId.equals(userId)) {
                        // Retrieve account details
                        String retrievedFullName = accountDetails[0].trim();
                        String retrievedUserPin = accountDetails[2].trim();
                        double currentBalance = Double.parseDouble(accountDetails[3].trim());
                        String retrievedBank = accountDetails[4].trim();
                        String retrievedTransactions = accountDetails[5].trim();
                        UserAccount userAccount = new UserAccount(retrievedFullName, userId, retrievedUserPin, currentBalance, retrievedBank, retrievedTransactions);

                        // Check if withdrawal can be made while maintaining minimum balance
                        if (currentBalance >= amount + 10.0) {
                            userAccount.withdraw(amount);
                            // Update the balance in the file
                            accountDetails[3] = String.valueOf(userAccount.getBalance());
                            accountLines.set(index, String.join(",", accountDetails));
                            System.out.println("Withdrawal successful. New balance: R" + userAccount.getBalance());
                            isUserFound = true;
                        } else {
                            System.out.println("Insufficient funds. Minimum balance of 10.0 must be maintained.");
                        }
                        break; // Stop searching once the user is found
                    }
                }
            }
            else{
                System.out.println("Amount too low, A minimum withdrawal of 10.0 must be maintained.");
            }


            if (isUserFound) {
                // Write the updated lines back to the file
                Files.write(Paths.get(accountFilePath), accountLines);
            } else {
                System.out.println("User not found.");
            }
        } catch (IOException ioException) {
            throw new RuntimeException("IO Exception: " + ioException.getMessage());
        }
    }

    // Method to deposit an amount into a user's account
    public void deposit(String userId, double amount) {
        String accountFilePath = "src/userAccounts";

        try {
            List<String> accountLines = Files.readAllLines(Paths.get(accountFilePath));
            boolean isUserFound = false;

            if(amount >= 10) {

                // Iterate through each line to find the user's account
                for (int index = 0; index < accountLines.size(); index++) {
                    String accountLine = accountLines.get(index);
                    String[] accountDetails = accountLine.split(",");
                    String retrievedUserId = accountDetails[1].trim();

                    if (retrievedUserId.equals(userId)) {
                        // Retrieve account details
                        String retrievedFullName = accountDetails[0].trim();
                        String retrievedUserPin = accountDetails[2].trim();
                        double currentBalance = Double.parseDouble(accountDetails[3].trim());
                        String retrievedBank = accountDetails[4].trim();
                        String retrievedTransactions = accountDetails[5].trim();
                        UserAccount userAccount = new UserAccount(retrievedFullName, userId, retrievedUserPin, currentBalance, retrievedBank, retrievedTransactions);

                        // Deposit the amount
                        userAccount.deposit(amount);
                        // Update the balance in the file
                        accountDetails[3] = String.valueOf(userAccount.getBalance());
                        accountLines.set(index, String.join(",", accountDetails));
                        System.out.println("Deposit successful. New balance: R" + userAccount.getBalance());
                        isUserFound = true;
                        break; // Stop searching once the user is found
                    }
                }
            }
            else{
                System.out.println("Amount too low, A minimum deposit of 10.0 must be maintained.");
            }

            if (isUserFound) {
                // Write the updated lines back to the file
                Files.write(Paths.get(accountFilePath), accountLines);
            } else {
                System.out.println("User not found.");
            }
        } catch (IOException ioException) {
            throw new RuntimeException("IO Exception: " + ioException.getMessage());
        }
    }

    // Method to transfer an amount from one user's account to another user's account
    public void transfer(String userId, String recipientId, double amount) {
        String accountFilePath = "src/userAccounts";

        UserAccount account = atm.getAccount(userId);
        UserAccount recipientAccount = atm.getAccount(recipientId);
        if (account != null && recipientAccount != null) {
            try {
                List<String> accountLines = Files.readAllLines(Paths.get(accountFilePath));

                int userIdIndex = -1;
                int recipientIdIndex = -1;
                for (int index = 0; index < accountLines.size(); index++) {
                    String accountLine = accountLines.get(index);
                    String[] accountDetails = accountLine.split(",");
                    String retrievedUserId = accountDetails[1].trim();
                    if (retrievedUserId.equals(userId)) {
                        userIdIndex = index;
                    } else if (retrievedUserId.equals(recipientId)) {
                        recipientIdIndex = index;
                    }
                }

                if (userIdIndex != -1 && recipientIdIndex != -1) {
                    String[] userAccountDetails = accountLines.get(userIdIndex).split(",");
                    double userBalance = Double.parseDouble(userAccountDetails[3].trim());
                    if (userBalance >= amount + 10.0) {
                        userBalance -= amount;
                        userAccountDetails[3] = String.valueOf(userBalance);
                        accountLines.set(userIdIndex, String.join(",", userAccountDetails));

                        String[] recipientAccountDetails = accountLines.get(recipientIdIndex).split(",");
                        double recipientBalance = Double.parseDouble(recipientAccountDetails[3].trim());
                        recipientBalance += amount;
                        recipientAccountDetails[3] = String.valueOf(recipientBalance);
                        accountLines.set(recipientIdIndex, String.join(",", recipientAccountDetails));

                        account.transfer(recipientAccount, amount);
                        recipientAccount.moneyInTransfer(account, amount);

                        Files.write(Paths.get(accountFilePath), accountLines);
                        System.out.println("Transfer successful. New balance: R" + userBalance);
                    } else {
                        System.out.println("Insufficient funds. Minimum balance of 10.0 must be maintained.");
                    }
                } else {
                    System.out.println("User not found.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("User not found.");
        }
    }

    // Helper method to retrieve a user's account details from the file
    private UserAccount getAccountFromFile(String userId) {
        String accountFilePath = "src/userAccounts";

        try {
            List<String> accountLines = Files.readAllLines(Paths.get(accountFilePath));
            for (String accountLine : accountLines) {
                String[] accountDetails = accountLine.split(",");
                String retrievedUserId = accountDetails[1].trim();
                if (retrievedUserId.equals(userId)) {
                    String retrievedFullName = accountDetails[0].trim();
                    String retrievedUserPin = accountDetails[2].trim();
                    double currentBalance = Double.parseDouble(accountDetails[3].trim());
                    String retrievedBank = accountDetails[4].trim();
                    String retrievedTransactions = accountDetails[5].trim();
                    return new UserAccount(retrievedFullName, userId, retrievedUserPin, currentBalance, retrievedBank, retrievedTransactions);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
