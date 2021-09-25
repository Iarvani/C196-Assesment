package com.c196project.databases;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.c196project.models.Instructor;
import java.util.List;

@Dao
public interface InstructorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInstructor(Instructor instructor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Instructor> instructors);

    @Delete
    void deleteInstructor(Instructor instructor);

    @Query("SELECT * FROM instructors WHERE id = :id")
    Instructor getInstructorById(int id);

    @Query("SELECT * FROM instructors ORDER BY name DESC")
    LiveData<List<Instructor>> getAll();

    @Query("Select * FROM instructors WHERE courseId = :courseId")
    LiveData<List<Instructor>> getInstructorsByCourse(final int courseId);

    @Query("DELETE FROM instructors")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM instructors")
    int getCount();
}
