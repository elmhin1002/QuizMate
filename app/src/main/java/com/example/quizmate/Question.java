package com.example.quizmate;

public class Question {
    private String text;
    private String answer1;
    private String answer2;

    public Question(String text, String answer1, String answer2) {
        this.text = text;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public String getText() {
        return text;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }
}