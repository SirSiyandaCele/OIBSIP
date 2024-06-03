package com.example.onlineexamination;
import java.util.*;

public class TestClass {
    private TestTopic testTopic;
    private int testID;
    private List<Question> questions;

    private TestClass(TestBuilder builder) {
        this.testTopic = builder.testTopic;
        this.testID = builder.testID;
        this.questions = builder.questions;
    }

    public TestTopic getTestTopic() {
        return testTopic;
    }

    public int getTestID() {
        return testID;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public static class TestBuilder {
        private TestTopic testTopic;
        private int testID;
        private List<Question> questions = new ArrayList<>();

        public TestBuilder withTopic(TestTopic topic) {
            this.testTopic = topic;
            return this;
        }

        public TestBuilder withID(int id) {
            this.testID = id;
            return this;
        }

        public TestBuilder addQuestions(List<Question> questions) {

            this.questions =  questions;
            return this;
        }

        public TestClass build() {
            return new TestClass(this);
        }
    }
}
