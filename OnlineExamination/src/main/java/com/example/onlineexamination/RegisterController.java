package com.example.onlineexamination;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

public class RegisterController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField passWord1;
    @FXML
    private TextField passWord2;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField idNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField cellNumber;

    public void initialize() {
        setTextFieldMaxLength(passWord1, 12);  // Set maximum length to 12 characters for password
        setTextFieldMaxLength(passWord2, 12);  // Set maximum length to 12 characters for password
        setTextFieldMaxLength(lastName, 30);   // Set maximum length to 30 characters for last name
        setEmailFieldRestriction(email);       // Set email field restrictions
        setTextFieldMaxLength(firstName, 30);  // Set maximum length to 30 characters for first name
        setCellNumberField(cellNumber);        // Set restrictions for cell number field
        setIdNumberField(idNumber);            // Set restrictions for ID number field
    }

    // Sets the maximum length for a TextField
    private void setTextFieldMaxLength(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= maxLength) {
                return change;
            } else {
                return null;
            }
        }));
    }

    // Sets restrictions for ID number field
    private void setIdNumberField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Check if the new text is numeric and within 13 digits
            if (newText.matches("\\d{0,13}")) {
                return change;
            } else {
                return null;
            }
        }));
    }

    // Sets restrictions for email field
    private void setEmailFieldRestriction(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            // Allow any text to be typed

            return change;
        }));
    }

    // Sets restrictions for cell number field
    private void setCellNumberField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Check if the new text is numeric and within 10 digits
            if (newText.matches("\\d{0,10}") ) {
                return change;
            } else {
                return null;
            }
        }));
    }

    // Go to the login page
    public void login(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Register the new user and store their details in the users file
    public void registerUser(ActionEvent e) {
        String userFile = "src/main/resources/users";
        SecureRandom secureRandom = new SecureRandom();
        long number = generateUniqueStudentNumber(userFile, secureRandom);

        String password = passWord2.getText();
        String fName = firstName.getText();
        String lName = lastName.getText();
        String idNumS = idNumber.getText();
        String mail = email.getText();
        String cell = cellNumber.getText();

        if (!validateInputs(password, fName, lName, idNumS, mail, cell)) {
            return;
        }

        String user = String.join(",",
                String.valueOf(number),
                fName,
                lName,
                idNumS,
                cell,
                mail,
                password);

        try {
            List<String> userLines = Files.readAllLines(Paths.get(userFile));
            for (String userLine : userLines) {
                String[] userLineDetails = userLine.split(",");
                String userIdNumber = userLineDetails[3].trim();
                String userPhoneNumber = userLineDetails[4].trim();
                String userEmail = userLineDetails[5].trim();
                Alert inputError = new Alert(Alert.AlertType.ERROR);
                inputError.setTitle("INPUT ERROR");
                if (userIdNumber.equals(idNumS)) {
                    inputError.setContentText("ID NUMBER ALREADY IN USE. Please LOG IN.");
                    inputError.show();
                    navigateToLogin(e);
                    return;
                }
                else if (userPhoneNumber.equals(cell)) {
                    inputError.setContentText("PHONE NUMBER ALREADY IN USE. Please LOG IN.");
                    inputError.show();
                    navigateToLogin(e);
                    return;
                }
                else if (userEmail.equals(mail)) {
                    inputError.setContentText("EMAIL ALREADY IN USE. Please LOG IN.");
                    inputError.show();
                    navigateToLogin(e);
                    return;
                }

            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
                writer.write(user);
                writer.newLine();
                showUserNumberAndNavigateToLogin(e, number);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("ERROR", "Failed to save user details.");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("ERROR", "Failed to read user file.");
        }
    }

    // Validate inputs before registering the user
    private boolean validateInputs(String password, String fName, String lName, String idNum, String mail, String cell) {
        if (password.isEmpty() || fName.isEmpty() || lName.isEmpty() || idNum.isEmpty() || mail.isEmpty() || cell.isEmpty()) {
            showAlert("ERROR", "All fields are required.");
            return false;
        }

        if (password.length()<=5 || idNum.length()<13 || mail.length()<6 || cell.length()<10) {
            showAlert("ERROR", "Complete all fields.");
            return false;
        }

        if (!passWord1.getText().equals(passWord2.getText())) {
            showAlert("ERROR", "Passwords do not match.");
            return false;
        }

        // Email validation regex
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!mail.matches(emailRegex)) {
            showAlert("ERROR", "Invalid email address.");
            return false;
        }

        return true;
    }

    // Show alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    // Show user number and navigate to login page
    private void showUserNumberAndNavigateToLogin(ActionEvent e, long number) throws IOException {
        Alert showNumber = new Alert(Alert.AlertType.CONFIRMATION, "YOUR USER NUMBER IS " + number);
        showNumber.setTitle("SAVE YOUR USER NUMBER");
        Optional<ButtonType> result = showNumber.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            navigateToLogin(e);
        }
    }

    // Navigate to the login page
    private void navigateToLogin(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Generate a unique student number
    private long generateUniqueStudentNumber(String userFile, SecureRandom secureRandom) {
        long number;
        try {
            List<String> userLines = Files.readAllLines(Paths.get(userFile));
            boolean isUsed;
            do {
                isUsed = false;
                number = 1000000000L + (long) (secureRandom.nextDouble() * 9000000000L);
                for (String userLine : userLines) {
                    String[] userLineDetails = userLine.split(",");
                    long userNumber = Long.parseLong(userLineDetails[0].trim());
                    if (userNumber == number) {
                        isUsed = true;
                        break;
                    }
                }
            } while (isUsed);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read user file", ex);
        }
        return number;
    }
}
