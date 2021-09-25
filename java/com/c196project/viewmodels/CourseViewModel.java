package com.c196project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.c196project.databases.ApplicationRepository;
import com.c196project.models.Course;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    public LiveData<List<Course>> courses;
    private ApplicationRepository repository;

    public CourseViewModel(@NonNull Application application) {
        super(application);

        repository = ApplicationRepository.getInstance(application.getApplicationContext());
        courses = repository.courses;
    }
}