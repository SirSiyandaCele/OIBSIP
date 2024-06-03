package com.example.onlineexamination;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField userPassword;
    @FXML
    private TextField userID;


    public void initialize() {
        setTextFieldMaxLength(userID, 10); // Set maximum length to 10 characters for the userID
        setUserPasswordFieldMaxLength(userPassword,12); //// Set maximum length to 12 characters for the userPassword


    }

    private void setTextFieldMaxLength(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= maxLength) {
                return change;
            } else {
                return null;
            }
        }));
    }
    private void setUserPasswordFieldMaxLength(PasswordField passwordField, int maxLength) {
        passwordField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= maxLength) {
                return change;
            } else {
                return null;
            }
        }));
    }
    private void setIdNumberField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            // Check if the new text is numeric and within 13 digits
            if (newText.matches("\\d{0,13}")) {
                return change;
            } else {
                return null;
            }
        }));
    }
    private void setEmailFieldRestriction(TextField textField) {
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            // Regular expression to validate email addresses
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (newText.matches(emailRegex) || newText.isEmpty()) {
                return change;
            } else {
                return null;
            }
        }));
    }



    //REGISTRATION SCENE
    public void login(ActionEvent e) throws IOException {
        String userFile = "src/main/resources/users";

        // Ensure userID and userPassword are not null
        if (userID == null || userPassword == null) {
            throw new IllegalArgumentException("userID or userPassword TextField is not initialized.");
        }

        long loginNumber;
        try {
            loginNumber = Long.parseLong(userID.getText()); // Get the entered number from the userId
        } catch (NumberFormatException ex) {
            // Handle invalid number format
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid user ID format.");
            errorAlert.setTitle("Input Error");
            errorAlert.showAndWait();
            return; // Exit the method if user ID format is invalid
        }

        String loginPassword = userPassword.getText();

        try {
            List<String> userLines = Files.readAllLines(Paths.get(userFile));
            boolean isFound = false; // Has the user been found?
            String fullName = "";
            String userNum = "";

            for (String userLine : userLines) {
                String[] userLineDetails = userLine.split(",");
                long userNumber = Long.parseLong(userLineDetails[0].trim());
                String pass = userLineDetails[6].trim();
                if (userNumber == loginNumber && pass.equals(loginPassword)) {
                    fullName = userLineDetails[1] + " " + userLineDetails[2];
                    userNum = userLineDetails[0];
                    isFound = true;
                    break;
                }
            }

            if (isFound) { // Log in if the user has been found
                //System.out.println(fullName);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateORtest.fxml"));
                root = loader.load();
                UpdateTestController cc = loader.getController();
                cc.setName(fullName , userNum);
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
            } else {
                // Display an error alert if the user is not found
                Alert showNumber = new Alert(Alert.AlertType.ERROR, "USER NOT FOUND");
                showNumber.setTitle("USER NOT FOUND");
                showNumber.showAndWait();
            }
        } catch (IOException ex) {
            // Properly handle the IOException
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while reading the user file.");
            errorAlert.setTitle("File Read Error");
            errorAlert.showAndWait();
            throw ex;
        }
    }
}
