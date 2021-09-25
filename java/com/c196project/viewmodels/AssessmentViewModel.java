package com.c196project.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.c196project.databases.ApplicationRepository;
import com.c196project.models.Assessment;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    public LiveData<List<Assessment>> assessments;
    private ApplicationRepository repository;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);

        repository = ApplicationRepository.getInstance(application.getApplicationContext());
        assessments = repository.assessments;
    }
}
