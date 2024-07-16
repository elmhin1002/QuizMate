package com.example.quizmate.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.example.quizmate.main.UserMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddQuizActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextTime;
    private LinearLayout questionContainer;
    private Button buttonSaveQuiz;
    private Button buttonAddQuestion;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTime = findViewById(R.id.editTextTime);
        questionContainer = findViewById(R.id.questionContainer);
        buttonSaveQuiz = findViewById(R.id.buttonSaveQuiz);
        buttonAddQuestion = findViewById(R.id.buttonAddQuestion);

        databaseReference = FirebaseDatabase.getInstance().getReference("quizzes");

        buttonSaveQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuiz();
            }
        });

        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestionField();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(AddQuizActivity.this, UserMainActivity.class));
                    return true;
                } else if (id == R.id.navigation_add) {
                    // Already on add quiz, no action needed
                    return true;
                } else if (id == R.id.navigation_user) {
                    // Handle navigation to user profile
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void addQuestionField() {
        // Create a new LinearLayout for the question
        LinearLayout questionLayout = new LinearLayout(this);
        questionLayout.setOrientation(LinearLayout.VERTICAL);
        questionLayout.setPadding(0, 16, 0, 16);

        // Create the EditText for the term
        EditText editTextTerm = new EditText(this);
        editTextTerm.setHint("Thuật ngữ");
        editTextTerm.setPadding(10, 10, 10, 10);
        editTextTerm.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        LinearLayout.LayoutParams termParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        termParams.setMargins(0, 16, 0, 8);
        editTextTerm.setLayoutParams(termParams);

        // Create the EditText for the definition
        EditText editTextDefinition = new EditText(this);
        editTextDefinition.setHint("Định nghĩa");
        editTextDefinition.setPadding(10, 10, 10, 10);
        editTextDefinition.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        LinearLayout.LayoutParams defParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        defParams.setMargins(0, 0, 0, 8);
        editTextDefinition.setLayoutParams(defParams);

        // Create the delete button
        Button deleteButton = new Button(this);
        deleteButton.setText("X");
        LinearLayout.LayoutParams delParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        delParams.setMargins(0, 0, 0, 16);
        deleteButton.setLayoutParams(delParams);

        // Set the click listener for the delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionContainer.removeView(questionLayout);
            }
        });

        // Add the term EditText, definition EditText, and delete button to the question layout
        questionLayout.addView(editTextTerm);
        questionLayout.addView(editTextDefinition);
        questionLayout.addView(deleteButton);

        // Add the question layout to the question container
        questionContainer.addView(questionLayout);
    }

    private void saveQuiz() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String timeString = editTextTime.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(timeString)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int time;
        try {
            time = Integer.parseInt(timeString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
            return;
        }

        String quizId = databaseReference.push().getKey();
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("name", title);
        quizData.put("description", description);
        quizData.put("max_time", time);

        Map<String, Object> questions = new HashMap<>();
        for (int i = 0; i < questionContainer.getChildCount(); i++) {
            LinearLayout questionLayout = (LinearLayout) questionContainer.getChildAt(i);
            EditText termEditText = (EditText) questionLayout.getChildAt(0);
            EditText definitionEditText = (EditText) questionLayout.getChildAt(1);

            String term = termEditText.getText().toString().trim();
            String definition = definitionEditText.getText().toString().trim();

            if (TextUtils.isEmpty(term) || TextUtils.isEmpty(definition)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> question = new HashMap<>();
            question.put("term", term);
            question.put("definition", definition);

            questions.put("question" + (i + 1), question);
        }

        quizData.put("questions", questions);

        databaseReference.child(quizId).setValue(quizData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddQuizActivity.this, "Quiz added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddQuizActivity.this, "Failed to add quiz", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
