package com.example.onlineexamination;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class testScreenController {
    private static List<Question> questions;
    private static int currentQuestionIndex = 0;

    private HashMap<String, String> selectedAnswers = new HashMap<>();
    private Set<String> answerEntries = new LinkedHashSet<>();
    private Set<String> correctAnswers = new LinkedHashSet<>();

    private int totalMark = 0;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label timer;

    private String duration;

    private int timeSeconds = 15 * 60; // 20 minutes in seconds

    private String testTopicName;

    @FXML
    private Polygon next;

    @FXML
    private Polygon previous;

    @FXML
    private Label questionLabel;
    @FXML
    private Label user;

    @FXML
    private Label userID;

    @FXML
    private RadioButton A;
    @FXML
    private RadioButton B;
    @FXML
    private RadioButton C;
    @FXML
    private RadioButton D;

    // Capture the user's selected answer
    public void captureSolution(String entry){
        answerEntries.add(entry);
    }

    // Capture the correct answers for all questions
    public void captureCorrectAnswers(){
        for(Question q : questions){
            correctAnswers.add(q.getCorrectAnswer());
        }
    }

    // Calculate and format the duration taken to complete the test
    public void setDuration(String countDown) {
        int givenTime = 15 * 60; // Total given time in seconds (15 minutes)
        int min = Integer.parseInt(countDown.substring(0, 2));
        int sec = Integer.parseInt(countDown.substring(5));

        // Convert to seconds
        int leftoverTime = min * 60 + sec;

        // Calculate the time taken to complete the test (in seconds)
        int timeTaken = givenTime - leftoverTime;

        // Convert time taken to minutes and seconds
        int minutes = timeTaken / 60;
        int seconds = timeTaken % 60;

        // Format the time taken as "xx : yy"
        duration = String.format("%02d : %02d", minutes, seconds);
    }

    // Calculate the score based on selected answers
    public void setScore(){
        int count = 0;
        for (Map.Entry<String, String> entry : selectedAnswers.entrySet()) {
            String questionText = entry.getKey();
            String selectedAnswer = entry.getValue();
            Question question = questions.stream()
                    .filter(q -> q.getQuestionText().equals(questionText))
                    .findFirst()
                    .orElse(null);
            if (question != null && question.validateAnswer(selectedAnswer)) {
                count++;
            }
        }

        this.totalMark = (int)Math.ceil(count / 20.0 * 100); // Assuming 20 questions in total
    }

    // Read questions from a file and initialize the test
    public void readFile(String questionFile, String test) {
        testTopicName = test;
        List<Question> localQuestions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(questionFile))) {
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // Check for empty line indicating the end of a set of question, options, and answer
                if (line.trim().isEmpty()) {
                    if (lines.size() == 6) {
                        localQuestions.add(createQuestion(lines));
                    }
                    lines.clear();
                } else {
                    lines.add(line);
                }
            }
            // Process the last set if the file does not end with an empty line
            if (lines.size() == 6) {
                localQuestions.add(createQuestion(lines));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TestClass mathTest = new TestClass.TestBuilder()
                .withTopic(test.equals("MATHEMATICS") ? TestTopic.MATHEMATICS : TestTopic.JAVA)
                .withID(1)
                .addQuestions(localQuestions)
                .build();

        // Print out the test details
        System.out.println("Test ID: " + mathTest.getTestID());
        System.out.println("Test Topic: " + mathTest.getTestTopic());
        System.out.println("Total Questions: " + mathTest.getQuestions().size());

        setQuestions(mathTest.getQuestions());
        questionLabel.setText(questions.get(currentQuestionIndex).getQuestionText());
        setPossibleAnswers(questions.get(currentQuestionIndex));
        captureCorrectAnswers();

        initializeUI();
    }

    // Create a Question object from lines read from the file
    private static Question createQuestion(List<String> lines) {
        String questionText = lines.get(0);
        List<String> answers = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            answers.add(lines.get(i));
        }
        String correctAnswer = lines.get(5);

        return new Question.QuestionBuilder()
                .withText(questionText)
                .addAnswers(answers)
                .withValidator(answer -> answer.equals(correctAnswer))
                .withCorrectAnswer(correctAnswer)
                .build();
    }

    // Set the user information
    public void setName(String FName, String ID){
        user.setText(FName);
        userID.setText(ID);
    }

    // Deselect all radio buttons
    public void setRadioButtons(){
        A.setSelected(false);
        B.setSelected(false);
        C.setSelected(false);
        D.setSelected(false);
    }

    @FXML
    public void initialize(){
        // Initial setup can go here if needed
    }

    // Initialize the UI for the test screen
    public void initializeUI() {
        if (questions != null && !questions.isEmpty()) {
            // Create a ToggleGroup and assign to RadioButtons
            ToggleGroup group = new ToggleGroup();
            A.setToggleGroup(group);
            B.setToggleGroup(group);
            C.setToggleGroup(group);
            D.setToggleGroup(group);

            // Update timer label and start countdown
            startCountdown(timeSeconds);
        } else {
            System.out.println("Error: No questions available.");
        }
    }

    // Update the selected answer for the current question
    private void updateSelectedAnswer(Question currentQuestion) {
        String questionText = currentQuestion.getQuestionText();
        String selectedAnswer = selectedAnswers.get(questionText);
        if (selectedAnswer != null) {
            if(selectedAnswer.equals(A.getText())) {
                A.setSelected(true);
            } else if(selectedAnswer.equals(B.getText())) {
                B.setSelected(true);
            } else if(selectedAnswer.equals(C.getText())) {
                C.setSelected(true);
            } else if(selectedAnswer.equals(D.getText())) {
                D.setSelected(true);
            }
        }
    }

    // Set the list of questions and initialize the first question
    public void setQuestions(List<Question> quests) {
        if (quests == null || quests.isEmpty()) {
            throw new IllegalArgumentException("Question list cannot be null or empty");
        }
        if (questions == null) {
            questions = new ArrayList<>();
        }

        Collections.shuffle(quests);

        questions.clear(); // Clear any existing questions
        questions.add(quests.get(0)); // Add the first question

        // Add up to 20 unique questions from quests
        for (Question currentQuestion : quests) {
            if (!questions.contains(currentQuestion) && questions.size() < 20) {
                questions.add(currentQuestion);
            }
        }

        Collections.shuffle(questions);

        // Set the text of the first question in the questions list
        if (!questions.isEmpty()) {
            questionLabel.setText(questions.get(currentQuestionIndex).getQuestionText());
        }
    }


    // Set possible answers for a given question
    public void setPossibleAnswers(Question quest){
        List<String> options = quest.getPossibleAnswers();
        //shuffle fibe times for control
        Collections.shuffle(options);
        Collections.shuffle(options);
        Collections.shuffle(options);
        Collections.shuffle(options);
        Collections.shuffle(options);
        A.setText(options.get(0));
        B.setText(options.get(1));
        C.setText(options.get(2));
        D.setText(options.get(3));
    }

    // Navigate to the previous question
    public void previousQuestion(MouseEvent mouseEvent) {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex > 0) {
            currentQuestionIndex--;
            questionLabel.setText(questions.get(currentQuestionIndex).getQuestionText());
            setPossibleAnswers(questions.get(currentQuestionIndex));
            setRadioButtons();
            updateSelectedAnswer(questions.get(currentQuestionIndex));
        }
    }

    // Navigate to the next question
    public void nextQuestion(MouseEvent mouseEvent) {
        saveSelectedAnswer();
        if (questions != null && !questions.isEmpty() && currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            questionLabel.setText(questions.get(currentQuestionIndex).getQuestionText());
            setPossibleAnswers(questions.get(currentQuestionIndex));
            setRadioButtons();
            updateSelectedAnswer(questions.get(currentQuestionIndex));
        }
    }

    // Save the selected answer for the current question
    private void saveSelectedAnswer() {
        ToggleGroup group = A.getToggleGroup();
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedAnswer = selectedRadioButton.getText();
            String questionText = questions.get(currentQuestionIndex).getQuestionText();
            selectedAnswers.put(questionText, selectedAnswer);
        }
    }

    private Timeline timeline;

    // Start the countdown timer
    private void startCountdown(int initialTimeSeconds) {
        timeSeconds = initialTimeSeconds;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    timeSeconds--;
                    updateLabel();

                    if (timeSeconds == 0) {
                        timeline.stop();
                        loadSubmitScene();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Update the timer label
    private void updateLabel() {
        int minutes = timeSeconds / 60;
        int seconds = timeSeconds % 60;
        timer.setText(String.format("%02d : %02d", minutes, seconds));
        if (minutes <= 15) {
            timer.setTextFill(Color.RED);
            timer.setBorder(new Border(new BorderStroke(
                    Color.RED,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(5),
                    BorderWidths.DEFAULT,
                    Insets.EMPTY
            )));
        }
    }

    // Load the submission scene when the test is over
    private void loadSubmitScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TestResults.fxml"));
            Parent root = loader.load();
            resultsController cc = loader.getController();
            cc.setName(user.getText(), userID.getText());
            setDuration(timer.getText());
            setScore();

            cc.setResults(totalMark, duration, testTopicName);

            Stage stage = (Stage) timer.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save the selected answer when an option is chosen
    public void choose1(ActionEvent actionEvent) { saveSelectedAnswer(); }
    public void choose2(ActionEvent actionEvent) { saveSelectedAnswer(); }
    public void choose3(ActionEvent actionEvent) { saveSelectedAnswer(); }
    public void choose4(ActionEvent actionEvent) { saveSelectedAnswer(); }

    // Confirm and quit the test session
    public void quit(ActionEvent actionEvent) throws IOException {
        Alert finished = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to quit and end session?", ButtonType.YES, ButtonType.NO);
        finished.setTitle("End Session");
        Optional<ButtonType> result = finished.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("taketest.fxml"));
                Parent root = loader.load();
                takeTestController cc = loader.getController();
                cc.setName(user.getText(), userID.getText());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Confirm and submit the test, then show results
    public void showResults(ActionEvent actionEvent) {
        Alert finished = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to submit and end session?", ButtonType.YES, ButtonType.NO);
        finished.setTitle("Submit Test");
        Optional<ButtonType> result = finished.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("TestResults.fxml"));
                Parent root = loader.load();
                resultsController cc = loader.getController();
                cc.setName(user.getText(), userID.getText());
                setDuration(timer.getText());
                setScore();

                cc.setResults(totalMark, duration, testTopicName);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
