package com.example.onlineexamination;
import java.util.*;
@FunctionalInterface
public interface AnswerValidator {
    boolean validate(String providedAnswer);//just checks whether the answer is correct or no\t.
}
