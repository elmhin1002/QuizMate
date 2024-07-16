package com.example.quizmate.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmate.R;
import com.example.quizmate.adapter.QuizAdapter;
import com.example.quizmate.entity.Quiz;
import com.example.quizmate.quiz.AddQuizActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private List<Quiz> quizList;
    private QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizList, this);
        recyclerView.setAdapter(quizAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("quizzes");

        fetchQuizzes();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    // Already on home, no action needed
                    return true;
                } else if (id == R.id.navigation_add) {
                    // Start AddQuizActivity to add a new quiz
                    startActivity(new Intent(UserMainActivity.this, AddQuizActivity.class));
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

    private void fetchQuizzes() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
                    Quiz quiz = quizSnapshot.getValue(Quiz.class);
                    if (quiz != null) {
                        quiz.setId(quizSnapshot.getKey());
                        quizList.add(quiz);
                    }
                }
                quizAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserMainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}