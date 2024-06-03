package com.example.brofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Random;

public class NumberGuessingController{
    public int lowerBound = 1;
    public int upperBound = 100;
    SecureRandom secureRandom = new SecureRandom();
    public int randomNumber;

    public int points;
    public int attempts = 0;
    public int maxAttempts = 10;
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
        //System.out.println(randomNumber);  ///uncomment this to test the endpoint of the project
        guessNumber.setEditable(true);
        startBtn.setDisable(true);
        sendGuess.setDisable(false);

    }

    public void endGame(){ //when the game ends
        attempts = 0;
        points = 100; //reset the points to 100
        maxAttempts = 10; //reset available attempts
        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : "+ maxAttempts); //Show numbeer of attempts left(0)
        score.setText("SCORE : "+points); //Show player points(0)
        startBtn.setDisable(false); //Enable the start button so the user can restart the game if they want to
        sendGuess.setDisable(true); //Disable the send guess buttom to block user from sending "null"
        guessNumber.setEditable(false); //When the game ends, users are not able to edit the text field unless they start a new game
        guessNumber.clear();
    }

    public void incorrectGuess(String condition){ //For every incorrect guess, this method will be told whether the guess was LOW or HIGH
        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : "+ maxAttempts); //Show the number available attempts after the guess
        Alert s = new Alert(Alert.AlertType.INFORMATION,"YOUR GUESS IS TOO "+condition, ButtonType.OK);//Tell whetehr the guess was too low or high
        s.show(); //show the alert
        guessNumber.setEditable(true); //the textField for guessing is made visible again
        points=points-10; //the player looses ten points for making an incorrect guess.
        score.setText("SCORE : "+points); //display the player's points to them
    }

    public void setGuess(ActionEvent event) {
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

                        if(maxAttempts < 10){
                            maxAttempts = maxAttempts + 1;
                        }
                        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : "+ maxAttempts);
                    }

                    else if(guess == randomNumber){
                        Playerattempts.setText("NUMBER OF ATTEMPTS LEFT : "+ maxAttempts);
                            Alert s = new Alert(Alert.AlertType.INFORMATION,"YOU WON!!", ButtonType.OK);
                            s.setTitle("CONGRATULATION!");
                            s.show();
                            points=points-10;
                            score.setText("SCORE : "+points);
                            endGame();
                    }
                    else if(guess > randomNumber){
                        incorrectGuess("HIGH");
                    }
                    else {
                        incorrectGuess("LOW");
                    }
                }
                catch (NumberFormatException e){
                    Alert s = new Alert(Alert.AlertType.ERROR,"ENTER ONLY NUMBERS PLEASE", ButtonType.OK);
                    s.show();
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
            else{
                Alert restart = new Alert(Alert.AlertType.INFORMATION, "GAME OVER, THE NUMBER WAS "+ randomNumber, ButtonType.OK);
                restart.show();
                if(restart.getAlertType() == Alert.AlertType.INFORMATION){
                    Alert restart2 = new Alert(Alert.AlertType.INFORMATION, "YOU CAN RESTART ORE EXIT THE GAME" , ButtonType.OK);
                    restart2.show();
                }

                endGame();
            }
        }
        else {
            Alert showPop = new Alert(Alert.AlertType.ERROR, "ENTER A NUMBER", ButtonType.OK);
            showPop.show();
        }

    }
}
