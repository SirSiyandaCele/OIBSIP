package com.example.brofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NumberGuessing extends Application { //The application class is the parent class that our HelloApp will inherit

    @Override
    public void start(Stage stage) throws IOException { //
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(NumberGuessing.class.getResource("hello-view.fxml")));
            stage.setTitle("Number Guessing Game");
            Scene scene = new Scene(root);
            stage.setResizable(false);
            scene.setFill(Color.LIGHTBLUE);
            stage.setScene(scene);
            stage.show(); //to show the stage

            //Scenes and adding content
            //SET GAME NAME
        /*Text gameTitle = new Text( "🔎NUMBER GUESSING GAME🔢");
        gameTitle.setX(150);
        gameTitle.setY(25);
        gameTitle.setFont(Font.font("Ariel", FontWeight.BOLD,20));
        gameTitle.setFill(Color.WHITE);

        //SET GAME WELCOME
        Text welcome = new Text( "W3LC0M3 TH3 G4M3 0F LUCK");
        welcome.setX(200);
        welcome.setY(50);
        welcome.setTextAlignment(TextAlignment.CENTER);
        welcome.setFont(Font.font("Ariel",FontWeight.BOLD, FontPosture.ITALIC,14));
        welcome.setFill(Color.WHITE);

        //SET GAME RULES*/



        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args); //static method of Application class, can be Application.launch(args)
    }
}