package com.example.onlineexamination;
import java.util.*;

public class Question {
    private String questionText;
    private List<String> possibleAnswers;
    private AnswerValidator validator;
    private String correctAnswer;

    public Question(QuestionBuilder builder) {
        this.questionText = builder.questionText;
        this.possibleAnswers = builder.possibleAnswers;
        this.validator = builder.validator;
        this.correctAnswer = builder.correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public boolean validateAnswer(String answer) {
        return validator.validate(answer);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public static class QuestionBuilder {
        private String questionText;
        private List<String> possibleAnswers = new ArrayList<>();
        private AnswerValidator validator;
        private String correctAnswer;

        public QuestionBuilder withText(String text) {
            this.questionText = text;
            return this;
        }

        public QuestionBuilder addAnswers(List<String> answers) {
            this.possibleAnswers = answers;
            return this;
        }

        public QuestionBuilder withValidator(AnswerValidator validator) {
            this.validator = validator;
            return this;
        }

        public QuestionBuilder withCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public Question build() {
            if (possibleAnswers.size() != 4) {
                throw new IllegalArgumentException("There must be exactly four possible answers.");
            }
            return new Question(this);
        }
    }
}
