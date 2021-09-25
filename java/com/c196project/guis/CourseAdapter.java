package com.c196project.guis;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.c196project.CourseDetailsActivity;
import com.c196project.CourseEditActivity;
import com.c196project.R;
import com.c196project.models.Course;
import com.c196project.utilities.TextFormats;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.c196project.utilities.Constants.COURSE_ID_KEY;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final List<Course> courses;
    private final Context context;
    private final RecyclerContext recyclerContext;
    private CourseSelectedListener courseSelectedListener;

    public CourseAdapter(List<Course> courses, Context context, RecyclerContext recyclerContext, CourseSelectedListener courseSelectedListener) {
        this.courses = courses;
        this.context = context;
        this.recyclerContext = recyclerContext;
        this.courseSelectedListener = courseSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_list_cardview, parent, false);
        return new ViewHolder(view, courseSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        final Course course = courses.get(position);
        holder.tvTitle.setText(course.getTitle());
        String startAndEnd = TextFormats.cardDateFormat.format(course.getStartDate()) + " to " + TextFormats.cardDateFormat.format(course.getAnticipatedEndDate());
        holder.tvDates.setText(startAndEnd);

        switch(recyclerContext) {
            case MAIN:
                Log.v("rContext", "rContext is " + recyclerContext.name());
                holder.courseFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit));
                holder.courseImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CourseDetailsActivity.class);
                    intent.putExtra(COURSE_ID_KEY, course.getId());
                    context.startActivity(intent);
                });

                holder.courseFab.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CourseEditActivity.class);
                    intent.putExtra(COURSE_ID_KEY, course.getId());
                    context.startActivity(intent);
                });
                break;
            case CHILD:
                holder.courseFab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete));
                holder.courseFab.setOnClickListener(v -> {
                    if(courseSelectedListener != null){
                        courseSelectedListener.onCourseSelected(position, course);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_course_title)
        TextView tvTitle;
        @BindView(R.id.card_course_fab)
        FloatingActionButton courseFab;
        @BindView(R.id.card_course_dates)
        TextView tvDates;
        @BindView(R.id.btn_course_details)
        ImageButton courseImageBtn;
        CourseSelectedListener courseSelectedListener;

        public ViewHolder(View itemView, CourseSelectedListener courseSelectedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.courseSelectedListener = courseSelectedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            courseSelectedListener.onCourseSelected(getAdapterPosition(), courses.get(getAdapterPosition()));
        }
    }

    public interface CourseSelectedListener {
        void onCourseSelected(int position, Course course);
    }
}