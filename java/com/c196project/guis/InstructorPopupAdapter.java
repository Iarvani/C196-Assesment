package com.c196project.guis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.c196project.R;
import com.c196project.models.Instructor;
import java.util.List;

public class InstructorPopupAdapter extends RecyclerView.Adapter<InstructorPopupAdapter.InstructorViewHolder> {
    private List<Instructor> instructors;
    private InstructorPopupAdapter.InstructorSelectedListener instructorSelectedListener;

    public InstructorPopupAdapter(List<Instructor> instructors) {
        super();
        this.instructors = instructors;
    }

    public void setInstructorSelectedListener(InstructorPopupAdapter.InstructorSelectedListener instructorSelectedListener) {
        this.instructorSelectedListener = instructorSelectedListener;
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, final int position) {
        final Instructor instructor = instructors.get(position);
        holder.tvInstructorName.setText(instructor.getName());
        holder.itemView.setOnClickListener(view -> {
            if(instructorSelectedListener != null) {
                instructorSelectedListener.onInstructorSelected(position, instructor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return instructors.size();
    }

    static class InstructorViewHolder extends RecyclerView.ViewHolder {
        TextView tvInstructorName;
        ImageView ivIcon;

        public InstructorViewHolder(View itemView) {
            super(itemView);
            tvInstructorName = itemView.findViewById(R.id.tv_instructor_name);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    public interface InstructorSelectedListener {
        void onInstructorSelected(int position, Instructor instructor);
    }
}
