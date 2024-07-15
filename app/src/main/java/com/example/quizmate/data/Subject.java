package com.example.quizmate.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subject")
public class Subject implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SubjectID")
    private int SubjectID;

    @NonNull
    @ColumnInfo(name = "SubjectName")
    private String SubjectName;

    public Subject(){}

    public Subject(@NonNull String subjectName) {
        SubjectName = subjectName;
    }

    public int getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(int subjectID) {
        SubjectID = subjectID;
    }

    @NonNull
    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(@NonNull String subjectName) {
        SubjectName = subjectName;
    }

    protected Subject(Parcel in) {
        SubjectID = in.readInt();
        SubjectName = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(SubjectID);
        parcel.writeString(SubjectName);
    }
}