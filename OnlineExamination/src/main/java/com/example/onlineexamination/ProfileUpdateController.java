package com.example.onlineexamination;

import javafx.application.Platform;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ProfileUpdateController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField cellNumber;
    @FXML
    private TextField email;
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

    private String user = "";
    private String userID = "";
    private String fullName = "";
    private Long loginNumber;

    // Sets the user name and ID
    public void setName(String FName, String ID){
        user = FName;
        userID = ID;
        loginNumber = Long.parseLong(userID); // Get the entered number from the userId
    }

    // Load user data and set up text field restrictions
    public void loadData(){
        setTextFieldMaxLength(passWord1, 12); // Set maximum length to 12 characters for the userPassword
        setTextFieldMaxLength(passWord2, 12); // Set maximum length to 12 characters for the userPassword
        setEmailFieldRestriction(email);
        setCellNumberField(cellNumber);

        String userFile = "src/main/resources/users";
        try {
            List<String> userLines = Files.readAllLines(Paths.get(userFile));
            for (String userLine : userLines) {
                String[] userLineDetails = userLine.split(",");
                if (Long.parseLong(userLineDetails[0]) == loginNumber) {
                    fullName = userLineDetails[1] + " " + userLineDetails[2]; // Concatenate first and last names

                    // Update the contact number, email, and password
                    firstName.setText(userLineDetails[1]);
                    lastName.setText(userLineDetails[2]);
                    idNumber.setText(userLineDetails[3]);
                    cellNumber.setText(userLineDetails[4]);
                    email.setText(userLineDetails[5]);
                    passWord1.setText(userLineDetails[6]);
                    passWord2.setText(userLineDetails[6]);

                    // Make the name and ID fields uneditable
                    firstName.setEditable(false);
                    lastName.setEditable(false);
                    idNumber.setEditable(false);

                    break;
                }
            }
        } catch (IOException e) {
            // Display an error alert instead of throwing an exception
            showErrorAlert("Error loading data", "Could not load user data from the file.");
        }
    }

    public void initialize(){
        // Initialization code, if any, can go here
    }

    // Sets the maximum length for a text field
    private void setTextFieldMaxLength(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.getControlNewText().length() <= maxLength) {
                return change;
            } else {
                return null;
            }
        }));
    }

    // Sets restrictions for the email text field
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

    // Sets restrictions for the cell number text field
    private void setCellNumberField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            // Check if the new text is numeric and within 10 digits
            if (newText.matches("\\d{0,10}")) {
                return change;
            } else {
                return null;
            }
        }));
    }

    // Updates the user profile
    public void updateProfile(ActionEvent actionEvent) {
        String userFile = "src/main/resources/users";

        try {
            List<String> userLines = Files.readAllLines(Paths.get(userFile));
            List<String> updatedUserLines = new ArrayList<>();
            for (String userLine : userLines) {
                String[] userLineDetails = userLine.split(",");
                if (Long.parseLong(userLineDetails[0]) == loginNumber) {
                    // Update the contact number, email, and password
                    userLineDetails[4] = cellNumber.getText();
                    userLineDetails[5] = email.getText();
                    userLineDetails[6] = passWord2.getText();

                    // Reconstruct the line
                    String updatedUserLine = String.join(",", userLineDetails);
                    updatedUserLines.add(updatedUserLine);
                } else {
                    updatedUserLines.add(userLine);
                }
            }
            // Write the updated lines back to the file
            Files.write(Paths.get(userFile), updatedUserLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Profile updated successfully. Click OK to log in.", ButtonType.OK);
            alert.setTitle("Update Complete");
            alert.showAndWait();

            // Navigate back
            back(actionEvent);

        } catch (IOException e) {
            // Display an error alert instead of throwing an exception
            showErrorAlert("Error updating profile", "Could not update user profile.");
        }
    }

    // Navigates back to the login screen
    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        root = loader.load();
        LoginController cc = loader.getController();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    // Quits the application
    public void quit(ActionEvent actionEvent){
        Platform.exit();
    }

    // Shows an error alert with the given title and content
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
