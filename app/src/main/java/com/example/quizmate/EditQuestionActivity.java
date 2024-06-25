package com.example.quizmate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmate.R;

public class EditQuestionActivity extends AppCompatActivity {

    private EditText editTextQuestion;
    private EditText editTextAnswer;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = editTextQuestion.getText().toString();
                String answer = editTextAnswer.getText().toString();


                Toast.makeText(EditQuestionActivity.this, "Question saved", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}
