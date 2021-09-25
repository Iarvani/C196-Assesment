package com.c196project.databases;

import android.content.Context;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import androidx.lifecycle.LiveData;
import com.c196project.models.Assessment;
import com.c196project.models.Course;
import com.c196project.models.Instructor;
import com.c196project.models.Term;
import com.c196project.utilities.SampleData;

public class ApplicationRepository {
    private static ApplicationRepository ourInstance;
    public LiveData<List<Term>> terms;
    public LiveData<List<Course>> courses;
    public LiveData<List<Assessment>> assessments;
    public LiveData<List<Instructor>> instructors;

    private ApplicationDB mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static ApplicationRepository getInstance(Context context) {
        if(ourInstance == null) {
            ourInstance = new ApplicationRepository(context);
        }
        return ourInstance;
    }

    private ApplicationRepository(Context context) {
        mDb = ApplicationDB.getInstance(context);
        terms = getAllTerms();
        courses = getAllCourses();
        assessments = getAllAssessments();
        instructors = getAllInstructors();
    }

    public void addSampleData() {
        executor.execute(() -> mDb.termDao().insertAll(SampleData.getTerms()));
        executor.execute(() -> mDb.courseDao().insertAll(SampleData.getCourses()));
        executor.execute(() -> mDb.assessmentDao().insertAll(SampleData.getAssessments()));
        executor.execute(() -> mDb.instructorDao().insertAll(SampleData.getInstructors()));
    }

    public LiveData<List<Term>> getAllTerms() {
        return mDb.termDao().getAll();
    }

    public void deleteAllData() {
        executor.execute(() -> mDb.termDao().deleteAll());
        executor.execute(() -> mDb.courseDao().deleteAll());
        executor.execute(() -> mDb.assessmentDao().deleteAll());
        executor.execute(() -> mDb.instructorDao().deleteAll());
    }

    public Term getTermById(int termId) {
        return mDb.termDao().getTermById(termId);
    }

    public void insertTerm(final Term term) {
        executor.execute(() -> mDb.termDao().insertTerm(term));
    }

    public void deleteTerm(final Term term) {
        executor.execute(() -> mDb.termDao().deleteTerm(term));
    }

    public LiveData<List<Course>> getAllCourses() {
        return mDb.courseDao().getAll();
    }

    public Course getCourseById(int courseId) {
        return mDb.courseDao().getCourseById(courseId);
    }

    public LiveData<List<Course>> getCoursesByTerm(final int termId) {
        return mDb.courseDao().getCoursesByTerm(termId);
    }

    public void insertCourse(final Course course) {
        executor.execute(() -> mDb.courseDao().insertCourse(course));
    }

    public void deleteCourse(final Course course) {
        executor.execute(() -> mDb.courseDao().deleteCourse(course));
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return mDb.assessmentDao().getAll();
    }

    public Assessment getAssessmentById(int assessmentId) {
        return mDb.assessmentDao().getAssessmentById(assessmentId);
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(final int courseId) {
        return mDb.assessmentDao().getAssessmentsByCourse(courseId);
    }

    public LiveData<List<Instructor>> getInstructorsByCourse(final int courseId) {
        return mDb.instructorDao().getInstructorsByCourse(courseId);
    }

    public void insertAssessment(final Assessment assessment) {
        executor.execute(() -> mDb.assessmentDao().insertAssessment(assessment));
    }

    public void deleteAssessment(final Assessment assessment) {
        executor.execute(() -> mDb.assessmentDao().deleteAssessment(assessment));
    }

    public LiveData<List<Instructor>> getAllInstructors() {
        return mDb.instructorDao().getAll();
    }

    public Instructor getInstructorById(int instructorId) {
        return mDb.instructorDao().getInstructorById(instructorId);
    }

    public void insertInstructor(final Instructor instructor) {
        executor.execute(() -> mDb.instructorDao().insertInstructor(instructor));
    }

    public void deleteInstructor(final Instructor instructor) {
        executor.execute(() -> mDb.instructorDao().deleteInstructor(instructor));
    }
}
