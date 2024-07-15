package com.example.quizmate.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

public class QuizWithSubject implements Parcelable {
    @Embedded
    private Quiz quiz;
    @Relation(
            parentColumn = "SubjectID",
            entityColumn = "SubjectID"
    )
    private Subject subject;

    public QuizWithSubject(Quiz quiz, Subject subject) {
        this.quiz = quiz;
        this.subject = subject;
    }

    protected QuizWithSubject(Parcel in) {
        quiz = in.readParcelable(Quiz.class.getClassLoader());
        subject = in.readParcelable(Subject.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(quiz, flags);
        dest.writeParcelable(subject, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizWithSubject> CREATOR = new Creator<QuizWithSubject>() {
        @Override
        public QuizWithSubject createFromParcel(Parcel in) {
            return new QuizWithSubject(in);
        }

        @Override
        public QuizWithSubject[] newArray(int size) {
            return new QuizWithSubject[size];
        }
    };

    public Quiz getQuiz() {
        return quiz;
    }

    public Subject getSubject() {
        return subject;
    }
}