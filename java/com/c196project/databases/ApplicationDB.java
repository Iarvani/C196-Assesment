package com.c196project.databases;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.c196project.models.Assessment;
import com.c196project.models.Course;
import com.c196project.models.Instructor;
import com.c196project.models.Term;

//If the database breaks +1 to the version number to populate a new one
@Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class}, version = 11)
@TypeConverters({DataConverter.class, CourseStatusConverter.class, AssessmentTypeConverter.class})
public abstract class ApplicationDB extends RoomDatabase {
    private static final String DATABASE_NAME = "ApplicationDB.db";
    private static volatile ApplicationDB instance;
    private static final Object LOCK = new Object();

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract InstructorDao instructorDao();

    public static ApplicationDB getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ApplicationDB.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }

        return instance;
    }
}
