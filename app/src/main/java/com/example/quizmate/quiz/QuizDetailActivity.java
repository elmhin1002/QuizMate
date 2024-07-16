package com.example.quizmate.quiz;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.example.quizmate.entity.Quiz;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizDetailActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextTime;
    private LinearLayout questionContainer;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTime = findViewById(R.id.editTextTime);
        questionContainer = findViewById(R.id.questionContainer);

        String quizId = getIntent().getStringExtra("QUIZ_ID");
        databaseReference = FirebaseDatabase.getInstance().getReference("quizzes").child(quizId);

        fetchQuizDetails();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                // Handle navigation to home
                finish(); // Assuming going back to home
                return true;
            } else if (id == R.id.navigation_add) {
                // Handle navigation to add quiz
                // startActivity(new Intent(QuizDetailActivity.this, AddQuizActivity.class));
                return true;
            } else if (id == R.id.navigation_user) {
                // Handle navigation to user profile
                // startActivity(new Intent(QuizDetailActivity.this, ProfileActivity.class));
                return true;
            } else {
                return false;
            }
        });
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

                        LinearLayout questionLayout = new LinearLayout(QuizDetailActivity.this);
                        questionLayout.setOrientation(LinearLayout.HORIZONTAL);
                        questionLayout.setPadding(0, 10, 0, 10);

                        EditText textViewTerm = new EditText(QuizDetailActivity.this);
                        textViewTerm.setText(term);
                        textViewTerm.setPadding(10, 10, 10, 10);
                        textViewTerm.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                        textViewTerm.setEnabled(false);
                        textViewTerm.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                        EditText textViewDefinition = new EditText(QuizDetailActivity.this);
                        textViewDefinition.setText(definition);
                        textViewDefinition.setPadding(10, 10, 10, 10);
                        textViewDefinition.setBackground(getResources().getDrawable(R.drawable.edittext_background));
                        textViewDefinition.setEnabled(false);
                        textViewDefinition.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                        questionLayout.addView(textViewTerm);
                        questionLayout.addView(textViewDefinition);

                        questionContainer.addView(questionLayout);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
