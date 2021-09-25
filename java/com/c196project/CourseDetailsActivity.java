package com.c196project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.guis.InstructorAdapter;
import com.c196project.guis.InstructorDropdownMenu;
import com.c196project.models.Instructor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.c196project.models.Assessment;
import com.c196project.guis.AssessmentAdapter;
import com.c196project.guis.AssessmentComboBox;
import com.c196project.guis.RecyclerContext;
import com.c196project.utilities.TextFormats;
import com.c196project.viewmodels.EditorViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.c196project.utilities.Constants.COURSE_ID_KEY;

public class CourseDetailsActivity extends AppCompatActivity implements AssessmentAdapter.AssessmentSelectedListener, InstructorAdapter.InstructorSelectedListener {
    @BindView(R.id.course_detail_start)
    TextView tvCourseStartDate;

    @BindView(R.id.course_detail_end)
    TextView tvCourseEndDate;

    @BindView(R.id.rview_course_detail_assessments)
    RecyclerView mAssRecyclerView;

    @BindView(R.id.rview_course_detail_instructors)
    RecyclerView mInstructorsRecyclerView;

    @BindView(R.id.course_detail_status)
    TextView tvCourseStatus;

    @BindView(R.id.course_detail_note)
    TextView tvCourseNote;

    @BindView(R.id.fab_add_assessment)
    FloatingActionButton fabAddAssessment;

    @BindView(R.id.fab_add_instructor)
    FloatingActionButton fabAddInstructor;

    private List<Assessment> assessmentData = new ArrayList<>();
    private List<Assessment> unassignedAssessments = new ArrayList<>();
    private List<Instructor> instructorData = new ArrayList<>();
    private List<Instructor> unassignedInstructors = new ArrayList<>();
    private Toolbar toolbar;
    private int courseId;
    private AssessmentAdapter assessmentAdapter;
    private InstructorAdapter instructorAdapter;
    private EditorViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        mAssRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAssRecyclerView.setLayoutManager(layoutManager);

        mInstructorsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        mInstructorsRecyclerView.setLayoutManager(layoutManager1);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        viewModel.activeCourse.observe(this, course -> {
            tvCourseStartDate.setText(TextFormats.fullDateFormat.format(course.getStartDate()));
            tvCourseEndDate.setText(TextFormats.fullDateFormat.format(course.getAnticipatedEndDate()));
            tvCourseStatus.setText(course.getCourseStatus().toString());
            tvCourseNote.setText(course.getNote());
            toolbar.setTitle(course.getTitle());
        });
        final Observer<List<Instructor>> instructorObserver =
                instructorEntities -> {
                    instructorData.clear();
                    instructorData.addAll(instructorEntities);

                    if(instructorAdapter == null) {
                        instructorAdapter = new InstructorAdapter(instructorData, CourseDetailsActivity.this, RecyclerContext.CHILD, this);
                        mInstructorsRecyclerView.setAdapter(instructorAdapter);
                    } else {
                        instructorAdapter.notifyDataSetChanged();
                    }
                };

        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);

                    if(assessmentAdapter == null) {
                        assessmentAdapter = new AssessmentAdapter(assessmentData, CourseDetailsActivity.this, RecyclerContext.CHILD, this);
                        mAssRecyclerView.setAdapter(assessmentAdapter);
                    } else {
                        assessmentAdapter.notifyDataSetChanged();
                    }
                };

        final Observer<List<Instructor>> unassignedInstructorObserver =
                instructorEntities -> {
                    unassignedInstructors.clear();
                    unassignedInstructors.addAll(instructorEntities);
                };

        final Observer<List<Assessment>> unassignedAssessmentObserver =
                assessmentEntities -> {
                    unassignedAssessments.clear();
                    unassignedAssessments.addAll(assessmentEntities);
                };

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            courseId = extras.getInt(COURSE_ID_KEY);
            viewModel.loadCourseData(courseId);
        } else {
            finish();
        }

        viewModel.getAssessmentsInCourse(courseId).observe(this, assessmentObserver);
        viewModel.getUnassignedAssessments().observe(this, unassignedAssessmentObserver);
        viewModel.getInstructorsInCourse(courseId).observe(this, instructorObserver);
        viewModel.getUnassignedInstructors().observe(this, unassignedInstructorObserver);
    }

    @OnClick(R.id.fab_edit_course)
    public void openEditActivity() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        this.startActivity(intent);
        finish();
    }

    @OnClick(R.id.fab_add_assessment)
    public void assessmentAddButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new or existing Assessment?");
        builder.setMessage("Would you like to add an existing assessment to this course or create a new assessment?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(COURSE_ID_KEY, courseId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            if(unassignedAssessments.size() >= 1) {
                final AssessmentComboBox menu = new AssessmentComboBox(this, unassignedAssessments);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp(200));
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddAssessment);
                menu.setAssessmentSelectedListener((position, assessment) -> {
                    menu.dismiss();
                    assessment.setCourseId(courseId);
                    viewModel.overwriteAssessment(assessment, courseId);
                });
            } else {
                Toast.makeText(getApplicationContext(), "There are no unassigned assessments. Please create a new assessment.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.fab_add_instructor)
    public void instructorAddButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new or existing Instructor?");
        builder.setMessage("Would you like to add an existing instructor to this course, or create a new instructor?");
        builder.setIcon(R.drawable.ic_add);
        builder.setPositiveButton("New", (dialog, id) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, InstructorEditActivity.class);
            intent.putExtra(COURSE_ID_KEY, courseId);
            this.startActivity(intent);
        });
        builder.setNegativeButton("Existing", (dialog, id) -> {
            if(unassignedInstructors.size() >= 1) {
                final InstructorDropdownMenu menu = new InstructorDropdownMenu(this, unassignedInstructors);
                menu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                menu.setWidth(getPxFromDp(200));
                menu.setOutsideTouchable(true);
                menu.setFocusable(true);
                menu.showAsDropDown(fabAddInstructor);
                menu.setInstructorSelectedListener((position, instructor) -> {
                    menu.dismiss();
                    instructor.setCourseId(courseId);
                    viewModel.overwriteInstructor(instructor, courseId);
                });
            } else {
                Toast.makeText(getApplicationContext(), "There are no unassigned instructors. Please create a new instructor.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.course_detail_share_fab)
    public void shareNote() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = tvCourseNote.getText().toString();
        String shareSub = "Notes for course: " + getTitle();
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    private int getPxFromDp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
//TODO add instructorSelected method
    @Override
    public void onAssessmentSelected(int position, Assessment assessment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove this assessment?");
        builder.setMessage("This will not delete the assessment, only remove it from this course.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Continue", (dialog, id) -> {
            dialog.dismiss();
            viewModel.overwriteAssessment(assessment, -1);
            assessmentAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onInstructorSelected(int position, Instructor instructor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove this instructor?");
        builder.setMessage("This will not delete the instructor, only remove it from this course.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Continue", (dialog, id) -> {
            dialog.dismiss();
            viewModel.overwriteInstructor(instructor, -1);
            instructorAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
