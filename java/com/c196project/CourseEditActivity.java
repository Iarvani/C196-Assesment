package com.c196project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.c196project.models.CourseStatus;
import com.c196project.utilities.TextFormats;
import com.c196project.viewmodels.EditorViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.c196project.utilities.Constants.COURSE_ID_KEY;
import static com.c196project.utilities.Constants.EDITING_KEY;
import static com.c196project.utilities.Constants.TERM_ID_KEY;

public class CourseEditActivity extends AppCompatActivity {
    @BindView(R.id.course_edit_title)
    EditText tvCourseTitle;

    @BindView(R.id.course_edit_start)
    EditText tvCourseStartDate;

    @BindView(R.id.course_edit_end)
    EditText tvCourseEndDate;

    @BindView(R.id.course_edit_start_btn)
    ImageButton btnStartDate;

    @BindView(R.id.course_edit_end_btn)
    ImageButton btnEndDate;

    @BindView(R.id.course_edit_status_dropdown)
    Spinner spCourseStatus;

    @BindView(R.id.course_edit_note)
    EditText tvNote;

    private EditorViewModel viewModel;
    private boolean newCourse, editing;
    private int termId = -1;
    private ArrayAdapter<CourseStatus> courseStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            editing = savedInstanceState.getBoolean(EDITING_KEY);
        }
        initViewModel();
        addSpinnerItems();
    }

    private void addSpinnerItems() {
        courseStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CourseStatus.values());
        spCourseStatus.setAdapter(courseStatusAdapter);
    }

    private CourseStatus getSpinnerValue() {
        return (CourseStatus) spCourseStatus.getSelectedItem();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        viewModel.activeCourse.observe(this, course -> {
            if(course != null && !editing) {
                tvCourseTitle.setText(course.getTitle());
                tvCourseStartDate.setText(TextFormats.fullDateFormat.format(course.getStartDate()));
                tvCourseEndDate.setText(TextFormats.fullDateFormat.format(course.getAnticipatedEndDate()));
                tvNote.setText(course.getNote());
                int position = getSpinnerPosition(course.getCourseStatus());
                spCourseStatus.setSelection(position);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            setTitle(getString(R.string.new_course));
            newCourse = true;
        } else if (extras.containsKey(TERM_ID_KEY)){ // Check if this is adding a course to a term
            termId = extras.getInt(TERM_ID_KEY);
            Log.v("DEBUG", "Extras term ID: " + termId);
            setTitle(getString(R.string.new_course));
        } else {
            setTitle(getString(R.string.edit_course));
            int courseId = extras.getInt(COURSE_ID_KEY);
            viewModel.loadCourseData(courseId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!newCourse) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private int getSpinnerPosition(CourseStatus courseStatus) {
        return courseStatusAdapter.getPosition(courseStatus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if(item.getItemId() == R.id.action_delete) {
            viewModel.deleteCourse();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    public void saveAndReturn() {
        try {
            Date startDate = TextFormats.fullDateFormat.parse(tvCourseStartDate.getText().toString());
            Date endDate = TextFormats.fullDateFormat.parse(tvCourseEndDate.getText().toString());
            viewModel.saveCourse(tvCourseTitle.getText().toString(), startDate, endDate, getSpinnerValue(), termId, tvNote.getText().toString());
            Log.v("Saved Course", tvCourseTitle.toString());
        } catch (ParseException e) {
            Log.v("Exception", e.getLocalizedMessage());
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.course_edit_start_btn)
    public void courseStartDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvCourseStartDate.setText(TextFormats.fullDateFormat.format(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.course_edit_end_btn)
    public void courseEndDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvCourseEndDate.setText(TextFormats.fullDateFormat.format(myCalendar.getTime()));
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}