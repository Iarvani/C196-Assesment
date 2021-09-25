package com.c196project;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.c196project.viewmodels.EditorViewModel;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.c196project.utilities.Constants.COURSE_ID_KEY;
import static com.c196project.utilities.Constants.EDITING_KEY;
import static com.c196project.utilities.Constants.INSTRUCTOR_ID_KEY;

public class InstructorEditActivity extends AppCompatActivity {
    @BindView(R.id.instructor_edit_name)
    EditText tvInstructorName;

    @BindView(R.id.instructor_edit_email)
    EditText tvInstructorEmail;

    @BindView(R.id.instructor_edit_phone)
    EditText tvInstructorPhone;

    private EditorViewModel mViewModel;
    private boolean newInstructor, editing;
    private int courseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instructor_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        mViewModel.activeInstructor.observe(this, instructor -> {
            if(instructor != null && !editing) {
                tvInstructorName.setText(instructor.getName());
                tvInstructorEmail.setText(instructor.getEmail());
                tvInstructorPhone.setText(instructor.getPhone());
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            //TODO refractor stings
            setTitle(getString(R.string.new_instructor));
            newInstructor = true;
        } else if (extras.containsKey(COURSE_ID_KEY)) {
            courseId = extras.getInt(COURSE_ID_KEY);
            setTitle(getString(R.string.new_instructor));
        } else {
            setTitle(getString(R.string.edit_instructor));
            int instructorId = extras.getInt(INSTRUCTOR_ID_KEY);
            mViewModel.loadInstructorData(instructorId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!newInstructor) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if(item.getItemId() == R.id.action_delete) {
            mViewModel.deleteInstructor();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    public void saveAndReturn() {
        mViewModel.saveInstructor(tvInstructorName.getText().toString(), tvInstructorEmail.getText().toString(), tvInstructorPhone.getText().toString(), courseId);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
