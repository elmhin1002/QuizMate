package com.example.quizmate.entity;

public class Quiz {
    private String name;
    private String description;
    private int max_time;

    public Quiz() {
        // Default constructor required for calls to DataSnapshot.getValue(Quiz.class)
    }

    public Quiz(String name, String description, int max_time) {
        this.name = name;
        this.description = description;
        this.max_time = max_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMax_time() {
        return max_time;
    }

    public void setMax_time(int max_time) {
        this.max_time = max_time;
    }
}

//import java.util.ArrayList;
//import java.util.List;
//
//public class Quiz {
//    private String title;
//    private String description;
//    private List<Question> questions;
//
//    public Quiz() {
//        // Default constructor required for calls to DataSnapshot.getValue(Quiz.class)
//    }
//
//    public Quiz(String title, String description) {
//        this.title = title;
//        this.description = description;
//        this.questions = new ArrayList<>();
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void addQuestion(Question question) {
//        if (questions == null) {
//            questions = new ArrayList<>();
//        }
//        questions.add(question);
//    }
//
//    public static class Question {
//        private String question;
//        private String option1;
//        private String option2;
//        private String option3;
//        private String option4;
//        private String correctAnswer;
//
//        public Question() {
//            // Default constructor required for calls to DataSnapshot.getValue(Question.class)
//        }
//
//        public Question(String question, String option1, String option2, String option3, String option4, String correctAnswer) {
//            this.question = question;
//            this.option1 = option1;
//            this.option2 = option2;
//            this.option3 = option3;
//            this.option4 = option4;
//            this.correctAnswer = correctAnswer;
//        }
//
//        // Getters and Setters for Question fields
//        public String getQuestion() {
//            return question;
//        }
//
//        public void setQuestion(String question) {
//            this.question = question;
//        }
//
//        public String getOption1() {
//            return option1;
//        }
//
//        public void setOption1(String option1) {
//            this.option1 = option1;
//        }
//
//        public String getOption2() {
//            return option2;
//        }
//
//        public void setOption2(String option2) {
//            this.option2 = option2;
//        }
//
//        public String getOption3() {
//            return option3;
//        }
//
//        public void setOption3(String option3) {
//            this.option3 = option3;
//        }
//
//        public String getOption4() {
//            return option4;
//        }
//
//        public void setOption4(String option4) {
//            this.option4 = option4;
//        }
//
//        public String getCorrectAnswer() {
//            return correctAnswer;
//        }
//
//        public void setCorrectAnswer(String correctAnswer) {
//            this.correctAnswer = correctAnswer;
//        }
//    }
//}
