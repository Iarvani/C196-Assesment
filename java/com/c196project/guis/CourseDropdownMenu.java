package com.c196project.guis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.R;
import com.c196project.models.Course;

import java.util.List;

public class CourseDropdownMenu extends PopupWindow {
    private Context context;
    private List<Course> courses;
    private RecyclerView rvPopup;
    private CoursePopupAdapter courseAdapter;

    public CourseDropdownMenu(Context context, List<Course> courses) {
        super(context);
        this.context = context;
        this.courses = courses;
        setupView();
    }

    public void setCourseSelectedListener(CoursePopupAdapter.CourseSelectedListener courseSelectedListener) {
        courseAdapter.setCourseSelectedListener(courseSelectedListener);
    }

    private void setupView() {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);

        rvPopup = view.findViewById(R.id.rv_popup);
        rvPopup.setHasFixedSize(true);
        rvPopup.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvPopup.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        courseAdapter = new CoursePopupAdapter(courses);
        rvPopup.setAdapter(courseAdapter);

        setContentView(view);
    }


}
