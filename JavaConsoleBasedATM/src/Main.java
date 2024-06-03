import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create an instance of the ATM service
        AtmService ATMservice = new AtmService();
        // Create a scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Welcome message
        System.out.println("Welcome to the ATM");

        // Prompt user to enter their user ID
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        // Prompt user to enter their PIN
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        // Create an instance of the ATM class
        ATM currentATM = new ATM();
        // Retrieve account details for the entered user ID
        currentATM.getAccount(userId);

        // Check if the user is authenticated
        if (ATMservice.userAuthenticator(userId, pin)) {
            boolean quit = false; // Flag to control the loop

            // Main loop for the ATM menu
            while (!quit) {
                // Display ATM menu options
                System.out.println("\nATM Menu:");
                System.out.println("1. Transactions History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Check Balance");
                System.out.println("6. Quit");

                // Prompt user to choose an option
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                // Handle the user's choice
                switch (choice) {
                    case 1:
                        // Show transaction history
                        ATMservice.showTransactionHistory(userId);
                        break;
                    case 2:
                        // Withdraw money
                        System.out.print("Enter amount to withdraw: ");
                        double amount = scanner.nextDouble();
                        ATMservice.withdraw(userId, amount);
                        break;
                    case 3:
                        // Deposit money
                        System.out.print("Enter amount to deposit: ");
                        amount = scanner.nextDouble();
                        ATMservice.deposit(userId, amount);
                        break;
                    case 4:
                        // Transfer money to another user
                        System.out.print("Enter recipient User ID: ");
                        String recipientId = scanner.next();
                        System.out.print("Enter amount to transfer: ");
                        amount = scanner.nextDouble();
                        ATMservice.transfer(userId, recipientId, amount);
                        break;
                    case 5:
                        // Check account balance
                        ATMservice.balance(userId);
                        break;
                    case 6:
                        // Quit the program
                        quit = true;
                        break;
                    default:
                        // Handle invalid options
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            // If authentication fails, show an error message
            System.out.println("Invalid User ID or PIN.");
        }

        // Close the scanner
        scanner.close();
        // Thank the user for using the ATM
        System.out.println("Thank you for using the ATM.");
    }
}
