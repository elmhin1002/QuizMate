package com.example.quizmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTextView;
        public Button answerButton1;
        public Button answerButton2;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerButton1 = itemView.findViewById(R.id.answerButton1);
            answerButton2 = itemView.findViewById(R.id.answerButton2);
        }
    }

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question currentQuestion = questionList.get(position);
        holder.questionTextView.setText(currentQuestion.getText());
        holder.answerButton1.setText(currentQuestion.getAnswer1());
        holder.answerButton2.setText(currentQuestion.getAnswer2());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}