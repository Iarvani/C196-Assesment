package com.c196project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.guis.InstructorAdapter;
import com.c196project.guis.RecyclerContext;
import com.c196project.models.Instructor;
import com.c196project.viewmodels.InstructorViewModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructorActivity extends AppCompatActivity implements InstructorAdapter.InstructorSelectedListener {
    @BindView(R.id.instructor_recycler_view)
    RecyclerView mInstructorRecyclerView;

    @OnClick(R.id.instructor_fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, InstructorEditActivity.class);
        startActivity(intent);
    }

    private List<Instructor> instructorData = new ArrayList<>();
    private InstructorAdapter instructorAdapter;
    private InstructorViewModel instructorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);
        Toolbar toolbar = findViewById(R.id.instructor_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        mInstructorRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mInstructorRecyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Instructor>> instructorObserver =
                instructorEntities -> {
                    instructorData.clear();
                    instructorData.addAll(instructorEntities);

                    if(instructorAdapter == null) {
                        instructorAdapter = new InstructorAdapter(instructorData, InstructorActivity.this, RecyclerContext.MAIN, this);
                        mInstructorRecyclerView.setAdapter(instructorAdapter);
                    } else {
                        instructorAdapter.notifyDataSetChanged();
                    }
                };
        instructorViewModel = new ViewModelProvider(this).get(InstructorViewModel.class);
        instructorViewModel.instructors.observe(this, instructorObserver);
    }

    @Override
    public void onInstructorSelected(int position, Instructor instructor) {

    }
}
