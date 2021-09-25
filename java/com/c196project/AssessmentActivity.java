package com.c196project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.models.Assessment;
import com.c196project.guis.AssessmentAdapter;
import com.c196project.guis.RecyclerContext;
import com.c196project.viewmodels.AssessmentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssessmentActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentSelectedListener {
    @BindView(R.id.ass_recycler_view)
    RecyclerView assessmentRecyclerView;

    @OnClick(R.id.ass_fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, AssessmentEditActivity.class);
        startActivity(intent);
    }

    private List<Assessment> assessmentData = new ArrayList<>();
    private AssessmentAdapter assessmentController;
    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_main);
        Toolbar toolbar = findViewById(R.id.ass_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        assessmentRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        assessmentRecyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);

                    if(assessmentController == null) {
                        assessmentController = new AssessmentAdapter(assessmentData, AssessmentActivity.this, RecyclerContext.MAIN, this);
                        assessmentRecyclerView.setAdapter(assessmentController);
                    } else {
                        assessmentController.notifyDataSetChanged();
                    }
                };
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.assessments.observe(this, assessmentObserver);
    }

    @Override
    public void onAssessmentSelected(int position, Assessment assessment) {

    }
}
