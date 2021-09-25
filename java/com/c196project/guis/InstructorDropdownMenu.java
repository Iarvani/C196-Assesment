package com.c196project.guis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.c196project.R;
import com.c196project.models.Instructor;
import java.util.List;

public class InstructorDropdownMenu extends PopupWindow {
    private Context context;
    private List<Instructor> instructors;
    private RecyclerView rvPopup;
    private InstructorPopupAdapter instructorAdapter;

    public InstructorDropdownMenu(Context context, List<Instructor> instructors) {
        super(context);
        this.context = context;
        this.instructors = instructors;
        setupView();
    }

    public void setInstructorSelectedListener(InstructorPopupAdapter.InstructorSelectedListener instructorSelectedListener) {
        instructorAdapter.setInstructorSelectedListener(instructorSelectedListener);
    }

    private void setupView() {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);

        rvPopup = view.findViewById(R.id.rv_popup);
        rvPopup.setHasFixedSize(true);
        rvPopup.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvPopup.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

        instructorAdapter = new InstructorPopupAdapter(instructors);
        rvPopup.setAdapter(instructorAdapter);

        setContentView(view);
    }
}
