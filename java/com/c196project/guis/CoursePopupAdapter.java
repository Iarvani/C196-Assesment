package com.c196project.guis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c196project.R;
import com.c196project.models.Course;

import java.util.List;

public class CoursePopupAdapter extends RecyclerView.Adapter<CoursePopupAdapter.CourseViewHolder> {
    private List<Course> courses;
    private CourseSelectedListener courseSelectedListener;

    public CoursePopupAdapter(List<Course> courses){
        super();
        this.courses = courses;
    }

    public void setCourseSelectedListener(CoursePopupAdapter.CourseSelectedListener courseSelectedListener) {
        this.courseSelectedListener = courseSelectedListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, final int position) {
        final Course course = courses.get(position);
        holder.tvCourseTitle.setText(course.getTitle());
        holder.itemView.setOnClickListener(view -> {
            if(courseSelectedListener != null){
                courseSelectedListener.onCourseSelected(position, course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView tvCourseTitle;
        ImageView ivIcon;

        public CourseViewHolder(View itemView) {
            super(itemView);
            tvCourseTitle = itemView.findViewById(R.id.tv_course_title);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    public interface CourseSelectedListener {
        void onCourseSelected(int position, Course course);
    }
}
