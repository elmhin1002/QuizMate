package com.example.quizmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmate.R;
import com.example.quizmate.entity.Quiz;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<Quiz> quizList;
    private Context context;

    public QuizAdapter(List<Quiz> quizList, Context context) {
        this.quizList = quizList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizTitleText.setText(quiz.getName());
        holder.quizSubtitleText.setText(quiz.getDescription());
        holder.quizTimeText.setText(String.valueOf(quiz.getMax_time()) + " min");
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitleText;
        TextView quizSubtitleText;
        TextView quizTimeText;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitleText = itemView.findViewById(R.id.quiz_title);
            quizSubtitleText = itemView.findViewById(R.id.quiz_subtitle);
            quizTimeText = itemView.findViewById(R.id.quiz_time);
        }
    }
}
