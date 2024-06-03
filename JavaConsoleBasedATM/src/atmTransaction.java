import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class atmTransaction {
    // Attributes of the transaction
    private String tCode; // A reference number that links each user with their transactions
    private String type; // Type of transaction (e.g., deposit, withdrawal)
    private double amount; // Amount involved in the transaction
    private double balanceAfter; // Balance after the transaction
    private LocalDateTime timestamp; // Timestamp of when the transaction occurred

    // Constructor to initialize the transaction
    public atmTransaction(String TCode, String type, double amount, double balanceAfter) {
        // Assign values to transaction attributes
        this.tCode = TCode;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now(); // Get the current timestamp

        // Format the timestamp using a specific pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = this.timestamp.format(formatter);

        // Create the transaction record as a string
        String transactionRecord = String.join(",",
                this.tCode,
                this.type,
                String.valueOf(this.amount),
                String.valueOf(this.balanceAfter),
                formattedTimestamp);

        // Write the transaction record to the "userTransactions" file
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/userTransactions", true))) { // Please modify this to your location when you are testing my project
            writer.write(transactionRecord); // Write the transaction record
            writer.newLine(); // Move to the next line for the next transaction
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception if an error occurs during writing
        }
    }

    // Override toString method to provide a string representation of the transaction
    @Override
    public String toString() {
        return "atmTransaction{" +
                "tCode='" + tCode + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", timestamp=" + timestamp +
                '}';
    }
}
