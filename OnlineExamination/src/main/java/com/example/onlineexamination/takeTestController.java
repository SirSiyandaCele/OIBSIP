package com.example.onlineexamination;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class takeTestController {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    public Button mathematicsTest;
    @FXML
    public Button javaTest;
    @FXML
    public Button back;
    @FXML
    private Label user;

    @FXML
    private Label userID;

    public void setName(String FName, String ID){
        user.setText(FName);
        userID.setText(ID);
    }

    public void chooseJava(ActionEvent actionEvent) throws IOException {
        String filePath = "src/main/resources/java";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TestScreen.fxml"));
        root = loader.load();
        testScreenController cc = loader.getController();
        cc.readFile(filePath,"JAVA");

        cc.setName(user.getText() , userID.getText());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void chooseMathematics(ActionEvent actionEvent) throws IOException {
        String filePath = "src/main/resources/mathematics";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TestScreen.fxml"));
        root = loader.load();
        testScreenController cc = loader.getController();
        cc.readFile(filePath,"MATHEMATICS");
        cc.setName(user.getText() , userID.getText());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("updateORtest.fxml"));
        root = loader.load();
        UpdateTestController cc = loader.getController();
        cc.setName(user.getText() , userID.getText());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public void quit(ActionEvent actionEvent) throws IOException {
        Alert finished = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to end session?", ButtonType.YES, ButtonType.NO);
        finished.setTitle("End Session");
        Optional<ButtonType> result = finished.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        }
        //Platform.exit();
    }


}
