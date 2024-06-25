package com.example.quizmate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionList = new ArrayList<>();
        questionList.add(new Question("What is the capital of France?", "Paris", "London"));
        questionList.add(new Question("What is the capital of Germany?", "Berlin", "Munich"));
        // Add more questions as needed

        questionAdapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(questionAdapter);
    }
}
