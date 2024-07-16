package com.example.quizmate;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizHistoryActivity extends AppCompatActivity {
    private TextView tvQuizHistory;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_history);

        tvQuizHistory = findViewById(R.id.tvQuizHistory);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("QuizHistory").child(user.getUid());

        loadQuizHistory();
    }

    private void loadQuizHistory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder quizHistory = new StringBuilder();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String quiz = dataSnapshot.getValue(String.class);
                    quizHistory.append(quiz).append("\n");
                }
                tvQuizHistory.setText(quizHistory.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizHistoryActivity.this, "Failed to load quiz history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
