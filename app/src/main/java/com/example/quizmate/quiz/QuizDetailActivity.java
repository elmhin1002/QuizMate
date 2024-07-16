package com.example.quizmate.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;
import com.example.quizmate.entity.Quiz;
import com.example.quizmate.main.UserMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizDetailActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextTime;
    private LinearLayout questionContainer;
    private DatabaseReference databaseReference;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTime = findViewById(R.id.editTextTime);
        questionContainer = findViewById(R.id.questionContainer);

        quizId = getIntent().getStringExtra("QUIZ_ID");
        databaseReference = FirebaseDatabase.getInstance().getReference("quizzes").child(quizId);

        fetchQuizDetails();

        Button btnTakeQuiz = findViewById(R.id.btn_take_quiz);
        btnTakeQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(QuizDetailActivity.this, TakeQuizActivity.class);
            intent.putExtra("QUIZ_ID", quizId);
            startActivity(intent);
        });

        Button btnEditQuiz = findViewById(R.id.btn_edit_quiz);
        btnEditQuiz.setOnClickListener(v -> {
            // Navigate to EditQuizActivity with quizId
            Intent intent = new Intent(QuizDetailActivity.this, EditQuizActivity.class);
            intent.putExtra("QUIZ_ID", quizId);
            startActivity(intent);
        });

        Button btnDeleteQuiz = findViewById(R.id.btn_delete_quiz);
        btnDeleteQuiz.setOnClickListener(v -> {
            // Show confirmation dialog before deleting
            new AlertDialog.Builder(QuizDetailActivity.this)
                    .setTitle("Delete Quiz")
                    .setMessage("Are you sure you want to delete this quiz?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteQuiz())
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    finish();
                    return true;
                } else if (id == R.id.navigation_add) {
                    // Start AddQuizActivity to add a new quiz
                    startActivity(new Intent(QuizDetailActivity.this, AddQuizActivity.class));
                    return true;
                } else if (id == R.id.navigation_user) {
                    // Start UserActivity to show user details
//                    startActivity(new Intent(UserMainActivity.this, ProfileActivity.class));
                    return true;
                } else {
                    return false;
                }
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

    private void deleteQuiz() {
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(QuizDetailActivity.this, "Quiz deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(QuizDetailActivity.this, "Failed to delete quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
