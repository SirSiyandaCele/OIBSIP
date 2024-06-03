package com.example.onlineexamination;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateTestController {

    private static final Logger logger = Logger.getLogger(UpdateTestController.class.getName());

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label user;

    @FXML
    private Label userID;

    @FXML
    private Label welcome;

    private final String textToType = "w e l c o m e   t o   J A V A & M A T H E M A T I C S   o n l i n e   q u i z z e s";
    private int charIndex = 0;


    public void setName(String FName, String ID) {
        user.setText(FName);
        userID.setText(ID);
    }


    @FXML
    public void initialize() {
        startTyping();
    }


    public void startTyping() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(textToType.length());

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), e -> {
            if (charIndex < textToType.length()) {
                welcome.setText(welcome.getText() + textToType.charAt(charIndex));
                charIndex++;
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }


    @FXML
    public void updateProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateprofile.fxml"));
            Parent root = loader.load();
            ProfileUpdateController cc = loader.getController();
            cc.setName(user.getText(), userID.getText());
            cc.loadData();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load update profile scene", e);
            showErrorDialog("Error", "Failed to load the update profile scene. Please try again.");
        }
    }


    @FXML
    public void takeTest(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("taketest.fxml"));
            Parent root = loader.load();
            takeTestController cc = loader.getController();
            cc.setName(user.getText(), userID.getText());
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load take test scene", e);
            showErrorDialog("Error", "Failed to load the take test scene. Please try again.");
        }
    }


    public void logout(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load login scene", e);
            showErrorDialog("Error", "Failed to log out. Please try again.");
        }
    }


    public void seeFormerTests(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("formerTests.fxml"));
            Parent root = loader.load();
            PastTestController cc = loader.getController();
            cc.setName(user.getText(), userID.getText());
            cc.setUserResults();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load former tests scene", e);
            showErrorDialog("Error", "Failed to load the former tests scene. Please try again.");
        }
    }

    private void showErrorDialog(String title, String message) {

        logger.severe(message);
    }
}
