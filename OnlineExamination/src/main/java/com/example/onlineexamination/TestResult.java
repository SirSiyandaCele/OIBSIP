package com.example.onlineexamination;

import java.util.ArrayList;
import java.util.List;

public class TestResult {
    private long userID;
    private String userName;
    private String testTopic;
    private String testDuration;
    private String score;
    private String time;
    private String date;
    private List<String> toShow;

    public TestResult(long userID, String userName, String testTopic, String testDuration, String score) {
        this.userID = userID;
        this.userName = userName;
        this.testTopic = testTopic;
        this.testDuration = testDuration;
        this.score = score;
    }

    public List<String> getToShow() {
        return toShow;
    }

    public void setToShow(List<String> toShow) {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(toShow.get(5));
        toReturn.add(toShow.get(6));
        toReturn.add(toShow.get(2));
        toReturn.add(toShow.get(3));
        toReturn.add(toShow.get(4));

        this.toShow = toReturn;
    }

    @Override
    public String toString() {
        return  "User Number:" + userID +"\n"+
                "Full Name  :'" + userName + "\n" +
                "Test Topic :" + testTopic + "\n"+
                "Duration   :" + testDuration + "\n"+
                "Test Score :" + score;
    }

    public long getUserID() {
        return userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getTestTopic() {
        return testTopic;
    }

    public String getTestDuration() {
        return testDuration;
    }

    public String getScore() {
        return score;
    }
}
