package com.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.c196project.viewmodels.EditorViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.c196project.utilities.Constants.INSTRUCTOR_ID_KEY;

public class InstructorDetailsActivity extends AppCompatActivity {
    @BindView(R.id.instructor_detail_email)
    TextView tvInstructorEmail;

    @BindView(R.id.instructor_detail_phone)
    TextView tvInstructorPhone;

    private Toolbar toolbar;
    private int instructorId;
    private EditorViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initViewModel();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        viewModel.activeInstructor.observe(this, instructor -> {
            tvInstructorEmail.setText(instructor.getEmail());
            tvInstructorPhone.setText(instructor.getPhone());
            toolbar.setTitle(instructor.getName());
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            instructorId = extras.getInt(INSTRUCTOR_ID_KEY);
            viewModel.loadInstructorData(instructorId);
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab_edit_instructor)
    public void openEditActivity() {
        Intent intent = new Intent(this, InstructorEditActivity.class);
        intent.putExtra(INSTRUCTOR_ID_KEY, instructorId);
        this.startActivity(intent);
        finish();
    }
}
