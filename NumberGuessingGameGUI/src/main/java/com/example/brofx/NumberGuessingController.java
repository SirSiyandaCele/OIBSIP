package com.example.brofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.security.SecureRandom;

<<<<<<< Updated upstream
public class NumberGuessingController{
    public int lowerBound = 1;
    public int upperBound = 100;
=======
public class NumberGuessingController {
    public final int lowerBound = 1;
    public final int upperBound = 100;

    public int maxAttempts = 10;

>>>>>>> Stashed changes
    SecureRandom secureRandom = new SecureRandom();
    public int randomNumber;

    public int points;
    public int attempts = 0;
    public int rounds = 3;
    public int guess;

    @FXML
    private TextField guessNumber;
    @FXML
    private Label score;

    @FXML
    public Button startBtn;

    @FXML
    private Label Playerattempts;

    @FXML
    private Button sendGuess;

    public void startGame(ActionEvent e) {
        points = 100;
        randomNumber = secureRandom.nextInt(upperBound - lowerBound + 1) + lowerBound;
        //System.out.println(randomNumber);
        guessNumber.setEditable(true);
        startBtn.setDisable(true);
        sendGuess.setDisable(false);
        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : " + maxAttempts);
        score.setText("SCORE : " + points);
    }

    public void endGame() {
        attempts = 0;
        points = 100;
        maxAttempts = 10;
        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : " + maxAttempts);
        score.setText("SCORE : " + points);
        startBtn.setDisable(false);
        sendGuess.setDisable(true);
        guessNumber.setEditable(false);
        guessNumber.clear();
    }

    public void incorrectGuess(String condition) {
        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : " + maxAttempts);
        Alert s = new Alert(Alert.AlertType.INFORMATION, "YOUR GUESS IS TOO " + condition, ButtonType.OK);
        s.show();
        guessNumber.setEditable(true);
        points -= 10;
        score.setText("SCORE : " + points);
    }

    public void setGuess(ActionEvent event) {
<<<<<<< Updated upstream
        maxAttempts = maxAttempts - 1;
        guessNumber.setEditable(false);
        if(guessNumber.getText() != null){
            if(maxAttempts>=1){
                try{
                    guess = Integer.parseInt(guessNumber.getText());
                    System.out.println(guess);
                    if((guess > 100) || (guess < 1)){
                        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : "+ maxAttempts);
                        guessNumber.clear();
                        Alert s = new Alert(Alert.AlertType.ERROR,"ENTER A NUMBER BETWEEN 1 AND 100", ButtonType.OK);
                        s.show();
                        guessNumber.setEditable(true);
=======
        String guessText = guessNumber.getText();
>>>>>>> Stashed changes

        if (guessText != null && !guessText.isEmpty()) {
            // Check if the input contains only numbers
            if (!guessText.matches("\\d+")) {
                Alert s = new Alert(Alert.AlertType.ERROR, "ENTER ONLY NUMBERS PLEASE", ButtonType.OK);
                s.show();
                guessNumber.setEditable(true);
                return;
            }

            guess = Integer.parseInt(guessText);

            if (guess < lowerBound || guess > upperBound) {
                Alert s = new Alert(Alert.AlertType.ERROR, "ENTER A NUMBER BETWEEN " + lowerBound + " AND " + upperBound, ButtonType.OK);
                s.show();
                guessNumber.clear();
                guessNumber.setEditable(true);
                return;
            }

            // Only decrease attempts and score for valid inputs that are not correct
            maxAttempts--;

            if (guess == randomNumber) {
                Alert s = new Alert(Alert.AlertType.INFORMATION, "YOU WON!!", ButtonType.OK);
                s.setTitle("CONGRATULATIONS!");
                s.show();
                score.setText("SCORE : " + points);
                endGame();
            } else {
                if (guess > randomNumber) {
                    incorrectGuess("HIGH");
                } else {
                    incorrectGuess("LOW");
                }

                if (maxAttempts <= 0) {
                    Alert restart = new Alert(Alert.AlertType.INFORMATION, "GAME OVER, THE NUMBER WAS " + randomNumber, ButtonType.OK);
                    restart.show();
                    endGame();
                } else {
                    Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : " + maxAttempts);
                    guessNumber.setEditable(true);
                }
            }
        } else {
            Alert showPop = new Alert(Alert.AlertType.ERROR, "ENTER A NUMBER", ButtonType.OK);
            showPop.show();
        }
    }
}
