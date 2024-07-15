package com.example.quizmate.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.quizmate.data.Quiz;
import com.example.quizmate.data.QuizWithSubject;

import java.util.List;

@Dao
public interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void  insertQuiz(Quiz quiz);

    @Update
    void updateQuiz(Quiz quiz);

    @Delete
    void deleteQuiz(Quiz quiz);

    @Transaction
    @Query("SELECT * FROM quiz")
    List<Quiz> getAllQuiz();
    @Transaction
    @Query("SELECT * FROM quiz WHERE SubjectID = :SubjectID")
    List<Quiz> getQuizBySubject(int SubjectID);

    @Transaction
    @Query("SELECT * FROM quiz")
    List<QuizWithSubject> getAllQuizWithSubject();
    @Transaction
    @Query("SELECT * FROM quiz WHERE SubjectID = :SubjectID")
    List<QuizWithSubject> getQuizBySubjectWithSubject(int SubjectID);
    @Transaction
    @Query("SELECT * FROM quiz WHERE QuizID = :QuizID")
    QuizWithSubject getQuiz(int QuizID);

}
