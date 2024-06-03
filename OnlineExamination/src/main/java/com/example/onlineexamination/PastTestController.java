package com.example.onlineexamination;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class PastTestController {
    @FXML
    public Label totalTests;
    @FXML
    public Label totalPass;
    @FXML
    public Label totalFailed;
    @FXML
    public Label summaryLabel;
    @FXML
    private Label user;
    @FXML
    private Label userID;
    @FXML
    private Polygon next;
    @FXML
    private Polygon previous;
    @FXML
    private Button logOut;
    @FXML
    private Button back;

    private List<TestResult> userTests = new ArrayList<>();
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label topicLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label scoreLabel;

    private int total = 0;
    private int fCount = 0;
    private int pCount = 0;

    private int currentResultIndex = 0;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void setStat(List<TestResult> currentUserTests) {
        userTests = currentUserTests;
        this.total = userTests.size();
        System.out.println(total);
        for(TestResult eachTest : userTests){
            String scoreStrng = eachTest.getScore();
            double score = Double.valueOf(scoreStrng.substring(0,scoreStrng.length()-1));
            if(score <= 49){
                fCount++;
            }
            else{
                pCount++;
            }
        }
        // Calculate the percentages for the slices
        if(total == 0){
            totalTests.setText(String.valueOf(total));
            totalFailed.setText(String.valueOf(fCount));
            totalPass.setText(String.valueOf(pCount));
            summaryLabel.setText("NO RECORDS FOUND");
        }
        else{
            double performanceRatio = ((double) pCount / total) * 100;
            totalTests.setText(String.valueOf(total));
            totalFailed.setText(String.valueOf(fCount));
            totalPass.setText(String.valueOf(pCount));
            if (performanceRatio <= 39) {
                summaryLabel.setText("Critical Academic Standing");
                summaryLabel.setTextFill(Color.RED);
            } else if (performanceRatio >= 40 && performanceRatio <= 49) {
                summaryLabel.setText("Bad Academic Standing");
                summaryLabel.setTextFill(Color.ORANGE);
            } else if (performanceRatio >= 50 && performanceRatio <= 59) {
                summaryLabel.setText("Average Academic Standing");
                summaryLabel.setTextFill(Color.GREEN);
            } else if (performanceRatio >= 60 && performanceRatio <= 69) {
                summaryLabel.setText("Good Academic Standing");
                summaryLabel.setTextFill(Color.BLUE);
            } else if (performanceRatio >= 70) {
                summaryLabel.setText("Excellent Academic Standing");
                summaryLabel.setTextFill(Color.PURPLE);
            }
        }

    }


    public void setLabels(TestResult currentTest) {
        dateLabel.setText(currentTest.getDate());
        timeLabel.setText(currentTest.getTime());
        topicLabel.setText(currentTest.getTestTopic());
        durationLabel.setText(currentTest.getTestDuration());
        scoreLabel.setText(currentTest.getScore());
    }


    public void setUserResults() throws IOException {
        String resultFile = "src/main/resources/testResults";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(resultFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String userIDText = userID.getText();
        System.out.println("UserID Text: " + userIDText);  // Debugging line

        if (userIDText == null || userIDText.isEmpty()) {
            System.err.println("Invalid userID: " + userIDText);
            return;
        }

        long userIDValue;
        try {
            userIDValue = Long.parseLong(userIDText);
        } catch (NumberFormatException e) {
            System.err.println("Invalid userID format: " + userIDText);
            return;
        }



        for (String singleResult : lines) {
            String[] currentResult = singleResult.split(",");
            if (currentResult.length < 7) {
                continue;  // Skip if the line doesn't have enough data
            }
            String userNum = currentResult[0];
            String userName = currentResult[1];
            String topic = currentResult[2];
            String duration = currentResult[3];
            String score = currentResult[4];
            String date = currentResult[5];
            String timeOnDay = currentResult[6];

            TestResult userResult = new TestResult(Long.parseLong(userNum), userName, topic, duration, score);
            userResult.setTime(timeOnDay);
            userResult.setDate(date);

            if (userResult.getUserID() == userIDValue) {
                userTests.add(userResult);
                System.out.println(userResult);
            }

        }

        if (!userTests.isEmpty()) {
            System.out.println(userTests.size());
            setStat(userTests);
            setLabels(userTests.get(currentResultIndex));
        }
    }


    public void setName(String fName, String id) {
        user.setText(fName);
        userID.setText(id);
    }


    @FXML
    public void initialize() throws IOException {
        setUserResults();
    }


    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void nextTest(MouseEvent touchEvent) {
        if (!userTests.isEmpty() && currentResultIndex < userTests.size() - 1) {
            currentResultIndex++;
            setLabels(userTests.get(currentResultIndex));
        }
    }


    public void previousTest(MouseEvent touchEvent) {
        if (!userTests.isEmpty() && currentResultIndex > 0) {
            currentResultIndex--;
            setLabels(userTests.get(currentResultIndex));
        }
    }


    public void back(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("updateORtest.fxml"));
        root = loader.load();
        UpdateTestController cc = loader.getController();
        cc.setName(user.getText(), userID.getText());
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
}
