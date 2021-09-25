package com.c196project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.models.Course;
import com.c196project.guis.CourseAdapter;
import com.c196project.guis.RecyclerContext;
import com.c196project.viewmodels.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseActivity extends AppCompatActivity implements CourseAdapter.CourseSelectedListener {
    @BindView(R.id.course_recycler_view)
    RecyclerView mCourseRecyclerView;

    @OnClick(R.id.course_fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, CourseEditActivity.class);
        startActivity(intent);
    }

    private List<Course> courseData = new ArrayList<>();
    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);
        Toolbar toolbar = findViewById(R.id.course_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        mCourseRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCourseRecyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);

                    if(courseAdapter == null) {
                        courseAdapter = new CourseAdapter(courseData, CourseActivity.this, RecyclerContext.MAIN, this);
                        mCourseRecyclerView.setAdapter(courseAdapter);
                    } else {
                        courseAdapter.notifyDataSetChanged();
                    }
                };
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.courses.observe(this, courseObserver);
    }

    @Override
    public void onCourseSelected(int position, Course course) {

    }
}
