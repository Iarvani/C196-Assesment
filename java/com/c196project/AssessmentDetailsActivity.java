package com.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.c196project.utilities.TextFormats;
import com.c196project.viewmodels.EditorViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.c196project.utilities.Constants.ASSESSMENT_ID_KEY;

public class AssessmentDetailsActivity extends AppCompatActivity {
    @BindView(R.id.ass_detail_date)
    TextView tvAssessmentDate;

    @BindView(R.id.ass_detail_type)
    TextView tvAssessmentType;

    private Toolbar toolbar;
    private int assessmentId;
    private EditorViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initViewModel();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        viewModel.activeAssessment.observe(this, assessment -> {
            tvAssessmentDate.setText(TextFormats.fullDateFormat.format(assessment.getDate()));
            tvAssessmentType.setText(assessment.getAssessmentType().toString());
            toolbar.setTitle(assessment.getTitle());
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            assessmentId = extras.getInt(ASSESSMENT_ID_KEY);
            viewModel.loadAssessmentData(assessmentId);
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab_edit_ass)
    public void openEditActivity() {
        Intent intent = new Intent(this, AssessmentEditActivity.class);
        intent.putExtra(ASSESSMENT_ID_KEY, assessmentId);
        this.startActivity(intent);
        finish();
    }
}
