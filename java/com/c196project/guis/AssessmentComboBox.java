package com.c196project.guis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.R;
import com.c196project.models.Assessment;

import java.util.List;

public class AssessmentComboBox extends PopupWindow {
    private Context context;
    private List<Assessment> assessments;
    private RecyclerView rvPopup;
    private AssessmentPopupAdapter assessmentAdapter;

    public AssessmentComboBox(Context context, List<Assessment> assessments) {
        super(context);
        this.context = context;
        this.assessments = assessments;
        setupView();
    }

    public void setAssessmentSelectedListener(AssessmentPopupAdapter.AssessmentSelectedListener assessmentSelectedListener) {
        assessmentAdapter.setAssessmentSelectedListener(assessmentSelectedListener);
    }

    private void setupView() {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);

        rvPopup = view.findViewById(R.id.rv_popup);
        rvPopup.setHasFixedSize(true);
        rvPopup.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvPopup.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        assessmentAdapter = new AssessmentPopupAdapter(assessments);
        rvPopup.setAdapter(assessmentAdapter);

        setContentView(view);
    }
}
