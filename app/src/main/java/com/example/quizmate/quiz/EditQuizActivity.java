package com.example.quizmate.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class EditQuizActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextTime;
    private LinearLayout questionContainer;
    private DatabaseReference databaseReference;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTime = findViewById(R.id.editTextTime);
        questionContainer = findViewById(R.id.questionContainer);

        quizId = getIntent().getStringExtra("QUIZ_ID");
        databaseReference = FirebaseDatabase.getInstance().getReference("quizzes").child(quizId);

        fetchQuizDetails();

        Button btnSaveQuiz = findViewById(R.id.btn_save_quiz);
        btnSaveQuiz.setOnClickListener(v -> saveQuizDetails());

        Button btnAddQuestion = findViewById(R.id.btn_add_question);
        btnAddQuestion.setOnClickListener(v -> addQuestionView(null, null));
    }

    private void fetchQuizDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Quiz quiz = snapshot.getValue(Quiz.class);
                if (quiz != null) {
                    editTextTitle.setText(quiz.getName());
                    editTextDescription.setText(quiz.getDescription());
                    editTextTime.setText(String.valueOf(quiz.getMax_time()));

                    questionContainer.removeAllViews();
                    for (DataSnapshot questionSnapshot : snapshot.child("questions").getChildren()) {
                        String term = questionSnapshot.child("term").getValue(String.class);
                        String definition = questionSnapshot.child("definition").getValue(String.class);
                        addQuestionView(term, definition);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void addQuestionView(String term, String definition) {
        View questionView = LayoutInflater.from(this).inflate(R.layout.item_edit_question, questionContainer, false);

        EditText editTextTerm = questionView.findViewById(R.id.editTextTerm);
        EditText editTextDefinition = questionView.findViewById(R.id.editTextDefinition);

        if (term != null) editTextTerm.setText(term);
        if (definition != null) editTextDefinition.setText(definition);

        questionContainer.addView(questionView);
    }

    private void saveQuizDetails() {
        String name = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int maxTime = Integer.parseInt(editTextTime.getText().toString());

        Map<String, Object> quizData = new HashMap<>();
        quizData.put("name", name);
        quizData.put("description", description);
        quizData.put("max_time", maxTime);

        Map<String, Object> questions = new HashMap<>();
        for (int i = 0; i < questionContainer.getChildCount(); i++) {
            View questionView = questionContainer.getChildAt(i);
            EditText editTextTerm = questionView.findViewById(R.id.editTextTerm);
            EditText editTextDefinition = questionView.findViewById(R.id.editTextDefinition);

            String term = editTextTerm.getText().toString();
            String definition = editTextDefinition.getText().toString();

            if (!term.isEmpty() && !definition.isEmpty()) {
                Map<String, Object> questionData = new HashMap<>();
                questionData.put("term", term);
                questionData.put("definition", definition);
                questions.put("question" + (i + 1), questionData);
            }
        }

        quizData.put("questions", questions);

        databaseReference.updateChildren(quizData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditQuizActivity.this, "Quiz updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditQuizActivity.this, "Failed to update quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
