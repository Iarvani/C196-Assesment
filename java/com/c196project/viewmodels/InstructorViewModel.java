package com.c196project.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.c196project.databases.ApplicationRepository;
import com.c196project.models.Instructor;
import java.util.List;

public class InstructorViewModel extends AndroidViewModel {
    public LiveData<List<Instructor>> instructors;
    private ApplicationRepository repository;

    public InstructorViewModel(@NonNull Application application) {
        super(application);

        repository = ApplicationRepository.getInstance(application.getApplicationContext());
        instructors = repository.instructors;
    }
}
