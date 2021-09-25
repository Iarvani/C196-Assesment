package com.c196project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.c196project.databases.ApplicationRepository;
import com.c196project.models.Assessment;
import com.c196project.models.Course;
import com.c196project.models.Instructor;
import com.c196project.models.Term;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<Term>> terms;
    public LiveData<List<Course>> courses;
    public LiveData<List<Assessment>> assessments;
    public LiveData<List<Instructor>> instructors;
    private ApplicationRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = ApplicationRepository.getInstance(application.getApplicationContext());
        terms = repository.getAllTerms();
        courses = repository.getAllCourses();
        assessments = repository.getAllAssessments();
        instructors = repository.getAllInstructors();

    }

    public LiveData<List<Term>> getAllTerms() {
        return repository.getAllTerms();
    }

    public void addSampleData() {
        repository.addSampleData();
    }

    public void deleteAllData() {
        repository.deleteAllData();
    }
}