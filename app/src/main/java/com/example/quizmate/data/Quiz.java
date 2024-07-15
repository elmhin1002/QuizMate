package com.example.quizmate.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "SubjectID",
                childColumns = "SubjectID",
                onDelete = ForeignKey.CASCADE
        )
)
public class Quiz implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "QuizID")
    private int QuizID;
    @NonNull
    @ColumnInfo(name = "Question")
    private String Question;
    @NonNull
    @ColumnInfo(name = "Answer1")
    private String Answer1;
    @NonNull
    @ColumnInfo(name = "Answer2")
    private String Answer2;
    @NonNull
    @ColumnInfo(name = "Answer3")
    private String Answer3;
    @NonNull
    @ColumnInfo(name = "Answer4")
    private String Answer4;
    @NonNull
    @ColumnInfo(name = "CorrectAnswer")
    private String CorrectAnswer;
    @ColumnInfo(name = "SubjectID")
    private int SubjectID;
    public Quiz(@NonNull String question, @NonNull String answer1, @NonNull String answer2, @NonNull String answer3, @NonNull String answer4, @NonNull String correctAnswer, int subjectID) {
        Question = question;
        Answer1 = answer1;
        Answer2 = answer2;
        Answer3 = answer3;
        Answer4 = answer4;
        CorrectAnswer = correctAnswer;
        SubjectID = subjectID;
    }
    public Quiz(){}
    public int getQuizID() {
        return QuizID;
    }
    @NonNull
    public String getQuestion() {
        return Question;
    }
    @NonNull
    public String getAnswer1() {
        return Answer1;
    }
    @NonNull
    public String getAnswer2() {
        return Answer2;
    }
    @NonNull
    public String getAnswer3() {
        return Answer3;
    }
    @NonNull
    public String getAnswer4() {
        return Answer4;
    }
    @NonNull
    public String getCorrectAnswer() {
        return CorrectAnswer;
    }
    public int getSubjectID() {
        return SubjectID;
    }
    public void setQuizID(int quizID) {
        QuizID = quizID;
    }
    public void setQuestion(@NonNull String question) {
        Question = question;
    }
    public void setAnswer1(@NonNull String answer1) {
        Answer1 = answer1;
    }
    public void setAnswer2(@NonNull String answer2) {
        Answer2 = answer2;
    }
    public void setAnswer3(@NonNull String answer3) {
        Answer3 = answer3;
    }
    public void setAnswer4(@NonNull String answer4) {
        Answer4 = answer4;
    }
    public void setCorrectAnswer(@NonNull String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }
    public void setSubjectID(int subjectID) {
        SubjectID = subjectID;
    }
    public  static Creator<Quiz> getCreator(){return CREATOR; }
    protected Quiz(Parcel in) {
        QuizID = in.readInt();
        Question = in.readString();
        Answer1 = in.readString();
        Answer2 = in.readString();
        Answer3 = in.readString();
        Answer4 = in.readString();
        CorrectAnswer = in.readString();
        SubjectID = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(QuizID);
        dest.writeString(Question);
        dest.writeString(Answer1);
        dest.writeString(Answer2);
        dest.writeString(Answer3);
        dest.writeString(Answer4);
        dest.writeString(CorrectAnswer);
        dest.writeInt(SubjectID);
    }
    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
}