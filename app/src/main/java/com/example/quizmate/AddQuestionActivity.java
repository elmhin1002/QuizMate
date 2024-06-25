package com.example.quizmate;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddQuestionActivity extends AppCompatActivity {

    private Button btnShowPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        btnShowPopup = findViewById(R.id.btn_show_popup);

        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddQuestionDialog();
            }
        });
    }

    private void showAddQuestionDialog() {
        final Dialog dialog = new Dialog(AddQuestionActivity.this);
        dialog.setContentView(R.layout.dialog_add_question);

        final EditText etQuestion = dialog.findViewById(R.id.et_dialog_question);
        final EditText etOption1 = dialog.findViewById(R.id.et_dialog_option1);
        final EditText etOption2 = dialog.findViewById(R.id.et_dialog_option2);
        final EditText etOption3 = dialog.findViewById(R.id.et_dialog_option3);
        final EditText etOption4 = dialog.findViewById(R.id.et_dialog_option4);
        final EditText etCorrectAnswer = dialog.findViewById(R.id.et_dialog_correct_answer);
        Button btnAddQuestion = dialog.findViewById(R.id.btn_dialog_add_question);

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = etQuestion.getText().toString();
                String option1 = etOption1.getText().toString();
                String option2 = etOption2.getText().toString();
                String option3 = etOption3.getText().toString();
                String option4 = etOption4.getText().toString();
                String correctAnswer = etCorrectAnswer.getText().toString();

                // TODO: Add code to save the question to your database or list

                Toast.makeText(AddQuestionActivity.this, "Question Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
