package com.example.quizmate.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.quizmate.dao.QuizDao;
import com.example.quizmate.dao.SubjectDao;
import com.example.quizmate.dao.UserDao;


@Database(
        entities = {User.class,Quiz.class, Subject.class,Attempt.class},
        exportSchema = false,
        version = 1
)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract QuizDao quizDao();

    public  abstract SubjectDao subjectDao();
}