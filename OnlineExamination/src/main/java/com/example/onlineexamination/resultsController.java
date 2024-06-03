package com.example.onlineexamination;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class resultsController {

    TestResult testRes;
    private Image sadEmoji;
    private Image cryingEmoji;
    private Image angryEmoji;

    public Label passORfail;
    public Label timer;
    public Label topic;
    public Label marks;
    public Pane confettiPane;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label user;

    @FXML
    private Label userID;

    public void writeTestFile(TestResult currentTest) throws IOException { //write test results onto the testResults file
        setTimeDate(currentTest);

        String userFile = "src/main/resources/testResults";
        List<String> currentLine = new ArrayList<>();
        currentLine.add(String.valueOf(currentTest.getUserID()));
        currentLine.add(currentTest.getUserName());
        currentLine.add(currentTest.getTestTopic());
        currentLine.add(currentTest.getTestDuration());
        currentLine.add(currentTest.getScore());
        currentLine.add(currentTest.getDate());
        currentLine.add(currentTest.getTime());

        StringBuilder testRecord = new StringBuilder();
        for (int i = 0; i < currentLine.size(); i++) {
            if (i > 0) {
                testRecord.append(",");
            }
            testRecord.append(currentLine.get(i));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
            writer.write(testRecord.toString());
            writer.newLine();
        }

    }

    public void setTimeDate(TestResult testObject){
        LocalDateTime now = LocalDateTime.now();

        // Format the date as "dd - monthName - year"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd - MMMM - yyyy", Locale.ENGLISH);
        String formattedDate = now.format(dateFormatter);

        // Format the time as "HH : MM"
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH : mm");
        String formattedTime = now.format(timeFormatter);

        // Print the formatted date and time
        testObject.setDate(formattedDate);
        testObject.setTime(formattedTime);
    }

    public void setName(String FName, String ID){
        user.setText(FName);
        userID.setText(ID);
    }

    public void setResults(int mark, String testDuration, String testTopic) throws IOException {

        timer.setText(testDuration);
        marks.setText(String.valueOf(mark)+"%");
        topic.setText(testTopic);
        if(mark <= 49){
            passORfail.setText("T R Y  A G A I N");
            passORfail.setTextFill(Color.RED);
            showEmojis();
        }
        else{
            showConfetti();
        }
        testRes = new TestResult(Long.parseLong(userID.getText()),user.getText(),testTopic,testDuration, marks.getText());
        writeTestFile(testRes);


    }

    @FXML
    public void initialize() {
        sadEmoji = new Image("sad.png");
        cryingEmoji = new Image("crying.png");
        angryEmoji = new Image("angry.png");
    }

    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void showConfetti() {
        Random random = new Random();
        int numConfetti = 100; // Number of confetti pieces
        boolean isPositive = true;

        for (int i = 0; i < numConfetti; i++) {
            Circle confetti = new Circle(5, getRandomColor());
            int paneWidth = 424;
            int paneHeight = 205;

            confetti.setLayoutX(random.nextInt(paneWidth));
            confetti.setLayoutY(random.nextInt(paneHeight / 2));

            confettiPane.getChildren().add(confetti);

            TranslateTransition translate = new TranslateTransition(Duration.seconds(3 + random.nextInt(3)), confetti);
            translate.setByY(paneHeight);
            translate.setCycleCount(1);
            translate.setInterpolator(Interpolator.EASE_OUT);

            RotateTransition rotate = new RotateTransition(Duration.seconds(3 + random.nextInt(3)), confetti);
            rotate.setByAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition parallelTransition = new ParallelTransition(translate, rotate);
            parallelTransition.setOnFinished(event -> confettiPane.getChildren().remove(confetti));
            parallelTransition.play();
        }
    }

    private void showEmojis() {
        Random random = new Random();
        int numConfetti = 50; // Number of confetti pieces
        int paneWidth = 424;
        int paneHeight = 205;

        for (int i = 0; i < numConfetti; i++) {
            // Randomly choose an emoji image
            ImageView confetti = new ImageView(getRandomEmoji(random));
            confetti.setFitWidth(20);
            confetti.setFitHeight(20);

            confetti.setLayoutX(random.nextInt(paneWidth));
            confetti.setLayoutY(random.nextInt(paneHeight / 2));

            confettiPane.getChildren().add(confetti);

            TranslateTransition translate = new TranslateTransition(Duration.seconds(3 + random.nextInt(3)), confetti);
            translate.setByY(paneHeight);
            translate.setCycleCount(1);
            translate.setInterpolator(Interpolator.EASE_OUT);

            RotateTransition rotate = new RotateTransition(Duration.seconds(3 + random.nextInt(3)), confetti);
            rotate.setByAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition parallelTransition = new ParallelTransition(translate, rotate);
            parallelTransition.setOnFinished(event -> confettiPane.getChildren().remove(confetti));
            parallelTransition.play();
        }
    }

    private Image getRandomEmoji(Random random) {
        int emojiIndex = random.nextInt(3); // 0, 1, or 2
        switch (emojiIndex) {
            case 0:
                return sadEmoji;
            case 1:
                return cryingEmoji;
            case 2:
                return angryEmoji;
            default:
                return sadEmoji; // Default case should never be hit
        }
    }

    private Color getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
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
}
