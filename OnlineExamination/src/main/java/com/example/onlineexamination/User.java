package com.example.onlineexamination;

import java.util.HashMap;

public class User {
    private long userNumber;
    private String firstName;
    private String lastName;
    private long idNumber;
    private long cellNumber;
    private String email;
    private String password;
    private HashMap<Integer,Double> JavaScore; //Store users number of tests taken in Java and their scores
    private HashMap<Integer,Double> MathScore;

    public User(long userNumber, String firstName, String lastName, long idNumber, long cellNumber, String email, String password) {
        this.userNumber = userNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.cellNumber = cellNumber;
        this.email = email;
        this.password = password;
    }

    public long getUserNumber() {
        return userNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getIdNumber() {
        return idNumber;
    }

    public long getCellNumber() {
        return cellNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
