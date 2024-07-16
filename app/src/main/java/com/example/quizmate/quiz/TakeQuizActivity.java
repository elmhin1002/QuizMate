package com.example.quizmate.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.example.quizmate.entity.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TakeQuizActivity extends AppCompatActivity {

    private TextView quizTitleTextView, quizDescriptionTextView;
    private TextView questionTextView, questionIndicatorTextView, timerIndicatorTextView;
    private Button[] answerButtons = new Button[4];
    private Button nextButton;
    private DatabaseReference quizReference;
    private List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        quizTitleTextView = findViewById(R.id.quiz_title);
        quizDescriptionTextView = findViewById(R.id.quiz_description);
        questionTextView = findViewById(R.id.question_textview);
        questionIndicatorTextView = findViewById(R.id.question_indicator_textview);
        timerIndicatorTextView = findViewById(R.id.timer_indicator_textview);

        answerButtons[0] = findViewById(R.id.btn0);
        answerButtons[1] = findViewById(R.id.btn1);
        answerButtons[2] = findViewById(R.id.btn2);
        answerButtons[3] = findViewById(R.id.btn3);

        nextButton = findViewById(R.id.next_btn);

        String quizId = getIntent().getStringExtra("QUIZ_ID");
        quizReference = FirebaseDatabase.getInstance().getReference("quizzes").child(quizId);

        fetchQuizData();

        for (int i = 0; i < answerButtons.length; i++) {
            int finalI = i;
            answerButtons[i].setOnClickListener(v -> checkAnswer(finalI));
        }

        nextButton.setOnClickListener(v -> showNextQuestion());
    }

    private void fetchQuizData() {
        quizReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String quizName = snapshot.child("name").getValue(String.class);
                String quizDescription = snapshot.child("description").getValue(String.class);
                if (quizName != null && quizDescription != null) {
                    quizTitleTextView.setText(quizName);
                    quizDescriptionTextView.setText(quizDescription);
                }

                questions = new ArrayList<>();
                for (DataSnapshot questionSnapshot : snapshot.child("questions").getChildren()) {
                    String term = questionSnapshot.child("term").getValue(String.class);
                    String definition = questionSnapshot.child("definition").getValue(String.class);
                    if (term != null && definition != null) {
                        questions.add(new QuizQuestion(term, definition));
                    }
                }
                showNextQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TakeQuizActivity.this, "Failed to load quiz data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            Toast.makeText(this, "Quiz completed! Your score: " + score, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        List<String> answers = new ArrayList<>();
        answers.add(currentQuestion.getDefinition());
        while (answers.size() < 4) {
            int randomIndex = (int) (Math.random() * questions.size());
            String randomAnswer = questions.get(randomIndex).getDefinition();
            if (!answers.contains(randomAnswer)) {
                answers.add(randomAnswer);
            }
        }
        Collections.shuffle(answers);

        questionTextView.setText(currentQuestion.getTerm());
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(answers.get(i));
        }
        questionIndicatorTextView.setText("Question " + (currentQuestionIndex + 1) + "/" + questions.size());
    }

    private void checkAnswer(int selectedAnswerIndex) {
        String selectedAnswer = answerButtons[selectedAnswerIndex].getText().toString();
        String correctAnswer = questions.get(currentQuestionIndex).getDefinition();
        if (selectedAnswer.equals(correctAnswer)) {
            score++;
        }
        currentQuestionIndex++;
        showNextQuestion();
    }

    private static class QuizQuestion {
        private final String term;
        private final String definition;

        public QuizQuestion(String term, String definition) {
            this.term = term;
            this.definition = definition;
        }

        public String getTerm() {
            return term;
        }

        public String getDefinition() {
            return definition;
        }
    }
}
