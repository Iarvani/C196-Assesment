package com.c196project.viewmodels;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.c196project.databases.ApplicationRepository;
import com.c196project.models.Term;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    public LiveData<List<Term>> terms;
    private ApplicationRepository repository;

    public TermViewModel(@NonNull Application application) {
        super(application);

        repository = ApplicationRepository.getInstance(application.getApplicationContext());
        terms = repository.terms;
    }
}